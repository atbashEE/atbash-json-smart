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

/*
 * Based on version from Octaplanner.
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 */

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;
import be.atbash.json.accessor.ex.NoSuchFieldException;

public class FastPropertyMemberAccessor {

    private final Class<?> propertyType;
    private final String propertyName;
    private final Method getterMethod;
    private final Function getterFunction;
    private final Method setterMethod;
    private final BiConsumer setterFunction;
    private final Field field;

    public FastPropertyMemberAccessor(Class<?> beanClass, String propertyName) {
        this.propertyName = propertyName;

        getterMethod = ReflectionHelper.getGetterMethod(beanClass, propertyName);

        if (getterMethod == null) {
            // We don't have a getter, see if we have a public property?

            field = ReflectionHelper.getField(beanClass, propertyName);
            if (field != null && (field.getModifiers() & Modifier.PUBLIC) != 0) {
                propertyType = field.getType();
            } else {
                propertyType = null;
                throw new NoSuchFieldException(String.format("Property %s not found in class %s", propertyName, beanClass));
            }
            setterMethod = null;
            getterFunction = null;
            setterFunction = null;
        } else {
            propertyType = getterMethod.getReturnType();

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            getterFunction = createGetterFunction(lookup);

            // We have getter, try to find setter.
            setterMethod = ReflectionHelper.getSetterMethod(beanClass, getterMethod.getReturnType(), propertyName);
            if (setterMethod != null) {
                setterFunction = createSetterFunction(lookup);
            } else {
                setterFunction = null;
            }

            field = null;
        }
    }

    private Function createGetterFunction(MethodHandles.Lookup lookup) {
        Class<?> declaringClass = getterMethod.getDeclaringClass();
        CallSite getterSite;
        try {
            getterSite = LambdaMetafactory.metafactory(lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(declaringClass, getterMethod.getName(), MethodType.methodType(propertyType)),
                    MethodType.methodType(propertyType, declaringClass));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Lambda creation failed for getterMethod (" + getterMethod + ").\n", e);
        } catch (LambdaConversionException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Lambda creation failed for getterMethod (" + getterMethod + ").", e);
        }
        try {
            return (Function) getterSite.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Lambda creation failed for getterMethod (" + getterMethod + ").", e);
        }
    }

    private BiConsumer createSetterFunction(MethodHandles.Lookup lookup) {
        if (setterMethod == null) {
            return null;
        }
        Class<?> declaringClass = setterMethod.getDeclaringClass();
        CallSite setterSite;
        try {
            setterSite = LambdaMetafactory.metafactory(lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    lookup.findVirtual(declaringClass, setterMethod.getName(), MethodType.methodType(void.class, propertyType)),
                    MethodType.methodType(void.class, declaringClass, propertyType));
        } catch (LambdaConversionException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException("Lambda creation failed for setterMethod (" + setterMethod + ").", e);
        }
        try {
            return (BiConsumer) setterSite.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Lambda creation failed for setterMethod (" + setterMethod + ").", e);
        }
    }

    public String getName() {
        return propertyName;
    }

    public Class<?> getType() {
        return propertyType;
    }

    public Object executeGetter(Object bean) {
        if (getterFunction != null) {
            return getterFunction.apply(bean);
        } else {
            try {
                return field.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                // FIXME
            }
        }
        return null;
    }

    public boolean supportSetter() {
        return setterMethod != null;
    }

    public void executeSetter(Object bean, Object value) {
        if (setterFunction != null) {
            setterFunction.accept(bean, value);
        } else {
            try {
                field.set(bean, convertValue(value, field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ///FIXME
            }
        }
    }

    private Object convertValue(Object value, Class<?> type) {
        if (Short.class.equals(type) || short.class.equals(type)) {
            return Short.valueOf(value.toString());
        }
        if (Byte.class.equals(type) || byte.class.equals(type)) {
            return Byte.valueOf(value.toString());
        }
        if (Float.class.equals(type) || float.class.equals(type)) {
            return Float.valueOf(value.toString());
        }
        if (Date.class.equals(type)) {
            return ConvertDate.convertToDate(value.toString());
        }
        return value;
    }

    @Override
    public String toString() {
        return "bean property " + propertyName + " on " + getterMethod.getDeclaringClass();
    }
}
