/*
 * Copyright 2017-2022 Rudy De Busscher (https://www.atbash.be)
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

import be.atbash.json.accessor.ex.NoSuchFieldException;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FastPropertyMemberAccessor {

    private  static final String GETTER_METHOD = "getterMethod";

    private final Class<?> propertyType;
    private final String propertyName;
    private final Method getterMethod;
    private final Function<Object, Object> getterFunction;
    private final Method setterMethod;
    private final BiConsumer<Object, Object> setterFunction;
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

    private Function<Object, Object> createGetterFunction(MethodHandles.Lookup lookup) {
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
            throw new IllegalStateException(getLambdaCreationErrorMessage(GETTER_METHOD, getterMethod), e);
        } catch (LambdaConversionException | NoSuchMethodException e) {
            throw new IllegalArgumentException(getLambdaCreationErrorMessage(GETTER_METHOD, getterMethod), e);
        }
        try {
            return (Function<Object, Object>) getterSite.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException(getLambdaCreationErrorMessage(GETTER_METHOD, getterMethod), e);
        }
    }

    private String getLambdaCreationErrorMessage(String methodType, Method method) {
        return "Lambda creation failed for " + methodType + "(" + method + ").";
    }

    private BiConsumer<Object, Object> createSetterFunction(MethodHandles.Lookup lookup) {
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

            setterSite = createSetterFunctionJDK11(lookup, declaringClass);
        }
        try {
            return (BiConsumer<Object, Object>) setterSite.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException(getLambdaCreationErrorMessage("setterMethod", setterMethod), e);
        }
    }

    private CallSite createSetterFunctionJDK11(MethodHandles.Lookup lookup, Class<?> declaringClass) {
        CallSite setterSite;
        // This is required for JDK 11
        // Everything should be an object.
        Class<?> updatedPropertyType = null;
        if (propertyType == Integer.TYPE) {
            updatedPropertyType = Integer.class;
        }
        if (propertyType == Long.TYPE) {
            updatedPropertyType = Long.class;
        }
        if (propertyType == Boolean.TYPE) {
            updatedPropertyType = Boolean.class;
        }
        try {
            setterSite = LambdaMetafactory.metafactory(lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    lookup.findVirtual(declaringClass, setterMethod.getName(), MethodType.methodType(void.class, propertyType)),
                    MethodType.methodType(void.class, declaringClass, updatedPropertyType));
        } catch (LambdaConversionException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException(getLambdaCreationErrorMessage("setterMethod", setterMethod), e);
        }
        return setterSite;
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
                throw new AtbashUnexpectedException(e);
            }
        }
    }

    public boolean supportSetter() {
        return setterMethod != null;
    }

    @SuppressWarnings("squid:S3011")
    public void executeSetter(Object bean, Object value) {
        if (setterFunction != null) {
            setterFunction.accept(bean, value);
        } else {
            try {
                field.set(bean, convertValue(value, field.getType()));
            } catch (IllegalAccessException e) {
                throw new AtbashUnexpectedException(e);
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
