/*
 * Copyright 2017-2019 Rudy De Busscher (https://www.atbash.be)
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
package be.atbash.json.asm;

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.atbash.json.asm.ex.NoSuchFieldException;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allow access reflect field using runtime generated accessor. BeansAccessor is
 * faster than java.lang.reflect.Method.invoke()
 *
 * @author uriel Chemouni
 */
public abstract class BeansAccess<T> {
    private HashMap<String, Accessor> map;
    private Accessor[] accs;

    void setAccessor(Accessor[] accs) {
        int i = 0;
        this.accs = accs;
        map = new HashMap<>();
        for (Accessor acc : accs) {
            acc.index = i++;
            map.put(acc.getPropertyName(), acc);
        }
    }

    public Map<String, Accessor> getMap() {
        return map;
    }

    public Accessor[] getAccessors() {
        return accs;
    }

    /**
     * cache used to store built BeansAccess
     */
    private static ConcurrentHashMap<Class<?>, BeansAccess<?>> cache = new ConcurrentHashMap<>();

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
        {
            @SuppressWarnings("unchecked")
            BeansAccess<P> access = (BeansAccess<P>) cache.get(type);
            if (access != null) {
                return access;
            }
        }
        // extract all access methods
        Accessor[] accs = ASMUtil.getAccessors(type, filter);

        // create new class name
        String className = type.getName();
        String accessClassName;
        if (className.startsWith("java.util.")) {
            accessClassName = "be.atbash.json.asm." + className + "AccAccess";
        } else {
            accessClassName = className.concat("AccAccess");
        }

        // extend class base loader
        DynamicClassLoader loader = new DynamicClassLoader(type.getClassLoader());
        // try to load existing class
        Class<?> accessClass = null;
        try {
            accessClass = loader.loadClass(accessClassName);
        } catch (ClassNotFoundException ignored) {
            // TODO
        }

        LinkedList<Class<?>> parentClasses = getParents(type);

        // if the class do not exists build it
        if (accessClass == null) {
            BeansAccessBuilder builder = new BeansAccessBuilder(type, accs, loader);
            for (Class<?> c : parentClasses) {
                builder.addConversion(BeansAccessConfig.classMapper.get(c));
            }
            accessClass = builder.build();
        }
        try {
            @SuppressWarnings("unchecked")
            BeansAccess<P> access = (BeansAccess<P>) accessClass.newInstance();
            access.setAccessor(accs);
            if (!cache.containsKey(type)) {
                cache.put(type, access);
            }
            // add fieldname alias
            for (Class<?> c : parentClasses) {
                addAlias(access, BeansAccessConfig.classFieldNameMapper.get(c));
            }
            return access;
        } catch (Exception ex) {
            throw new AtbashUnexpectedException("Error constructing accessor class: " + accessClassName);
        }
    }

    private static LinkedList<Class<?>> getParents(Class<?> type) {
        LinkedList<Class<?>> m = new LinkedList<>();
        while (type != null && !type.equals(Object.class)) {
            m.addLast(type);
            for (Class<?> c : type.getInterfaces()) {
                m.addLast(c);
            }
            type = type.getSuperclass();
        }
        m.addLast(Object.class);
        return m;
    }

    /**
     *
     */
    private static void addAlias(BeansAccess<?> access, HashMap<String, String> m) {
        if (m == null) {
            return;
        }
        HashMap<String, Accessor> changes = new HashMap<>();
        for (Entry<String, String> e : m.entrySet()) {
            Accessor a1 = access.map.get(e.getValue());
            if (a1 != null) {
                changes.put(e.getValue(), a1);
            }
        }
        access.map.putAll(changes);
    }

    /**
     * set field value by field index
     */
    public abstract void set(T object, int methodIndex, Object value);

    /**
     * get field value by field index
     */
    public abstract Object get(T object, int methodIndex);

    /**
     * create a new targeted object
     */
    public abstract T newInstance();

    /**
     * set field value by fieldname
     */
    public void set(T object, String methodName, Object value) {
        int i = getIndex(methodName);
        if (i == -1) {
            throw new NoSuchFieldException(String.format(" %s in %s to put value : %s ", methodName, object.getClass(), value));
        }
        set(object, i, value);
    }

    /**
     * get field value by fieldname
     */
    public Object get(T object, String methodName) {
        return get(object, getIndex(methodName));
    }

    /**
     * Returns the index of the field accessor.
     */
    public int getIndex(String name) {
        Accessor ac = map.get(name);
        if (ac == null) {
            return -1;
        }
        return ac.getIndex();
    }
}
