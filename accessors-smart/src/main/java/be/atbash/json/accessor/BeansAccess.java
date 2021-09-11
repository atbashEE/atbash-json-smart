/*
 * Copyright 2017-2021 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.json.accessor;

import be.atbash.json.accessor.mapper.FieldPropertyNameMapperHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Allow access reflect field using LambdaMetafactory (from the JDK) and no
 * reflection issues on newer JDKs.
 *
 * @author uriel Chemouni
 * @author Rudy De Busscher
 */
public final class BeansAccess<T> {

    /**
     * cache used to store built BeansAccess
     */
    private static ConcurrentHashMap<Class<?>, BeansAccess<?>> cache = new ConcurrentHashMap<>();

    private final FieldPropertyNameMapperHandler nameMapperHandler = FieldPropertyNameMapperHandler.getInstance();
    private final HashMap<String, FastPropertyMemberAccessor> map = new HashMap<>();
    private final Class<?> beanClass;
    private final List<Field> fields;
    private final Map<String, Field> jsonNames;

    /**
     * @param beanClass
     * @param filter    used when looking up all fields of the class and if they should be filtered (discarded) or not.
     *                  Providing null means all Discovered Fields will be used.
     */
    public BeansAccess(Class<?> beanClass, FieldFilter filter) {
        this.beanClass = beanClass;
        fields = Collections.unmodifiableList(defineFields(beanClass, filter));
        jsonNames = fields.stream()
                .collect(Collectors.toMap(nameMapperHandler::definePropertyName
                        , f -> f
                        , (key1, key2) -> key1));  // In case of duplicates

    }

    /**
     * Find all fields in Class and superclass(es).
     *
     * @param beanClass
     * @param filter
     * @return
     */
    private List<Field> defineFields(Class<?> beanClass, FieldFilter filter) {
        List<Field> result = new ArrayList<>();
        result.addAll(Arrays.stream(beanClass.getDeclaredFields())
                .filter(f -> canUse(f, filter))
                .collect(Collectors.toList()));
        if (!beanClass.equals(Object.class)) {
            result.addAll(defineFields(beanClass.getSuperclass(), filter));
        }
        return result;
    }

    /**
     * Can the field be used or should it be discarded?
     *
     * @param field
     * @param filter
     * @return
     */
    private boolean canUse(Field field, FieldFilter filter) {
        if (filter == null) {
            return true;
        }
        Method getterMethod = ReflectionHelper.getGetterMethod(field.getDeclaringClass(), field.getName());
        return filter.canUse(field, getterMethod);
    }

    /**
     * return the BeansAccess corresponding to a type
     *
     * @param type to be access
     * @return the BeansAccess
     */
    public static <P> BeansAccess<P> get(Class<P> type) {
        return get(type, null);
    }

    /**
     * return the BeansAccess corresponding to a type
     *
     * @param type to be access
     * @return the BeansAccess
     */
    public static <P> BeansAccess<P> get(Class<P> type, FieldFilter filter) {

        BeansAccess<P> access = (BeansAccess<P>) cache.get(type);
        if (access == null) {
            access = new BeansAccess<>(type, filter);
            cache.put(type, access);
        }

        return access;

    }

    public Optional<Field> getField(String propertyName) {
        Optional<Field> result = fields.stream()
                .filter(f -> propertyName.equals(f.getName()))
                .findAny();
        if (!result.isPresent()) {
            result = Optional.ofNullable(jsonNames.get(propertyName));
        }
        return result;
    }

    public List<Field> getFields() {
        return fields;
    }

    /**
     * set field value by fieldName
     */
    public void set(T object, String propertyName, Object value) {
        FastPropertyMemberAccessor memberAccessor = map.get(propertyName);
        if (memberAccessor == null) {
            memberAccessor = new FastPropertyMemberAccessor(object.getClass(), propertyName);

            map.put(propertyName, memberAccessor);
        }

        memberAccessor.executeSetter(object, value);
    }

    /**
     * get field value by fieldname
     */
    public Object get(T object, String propertyName) {
        FastPropertyMemberAccessor memberAccessor = map.get(propertyName);
        if (memberAccessor == null) {
            memberAccessor = new FastPropertyMemberAccessor(object.getClass(), propertyName);

            map.put(propertyName, memberAccessor);
        }
        return memberAccessor.executeGetter(object);
    }

    public T newInstance() {
        try {
            return (T) beanClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
