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

/* Modified from the Hibernate Validator project
 * Original authors: Hardy Ferentschik, Gunnar Morling and Kevin Pollet.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {

    private static final String PROPERTY_ACCESSOR_PREFIX_GET = "get";
    private static final String PROPERTY_ACCESSOR_PREFIX_IS = "is";
    private static final String PROPERTY_MUTATOR_PREFIX = "set";

    /**
     * @param containingClass never null
     * @param propertyName    never null
     * @return sometimes null
     */
    public static Method getGetterMethod(Class<?> containingClass, String propertyName) {
        String getterName = PROPERTY_ACCESSOR_PREFIX_GET
                + (propertyName.isEmpty() ? "" : propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
        try {
            return containingClass.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            // intentionally empty
        }
        String isserName = PROPERTY_ACCESSOR_PREFIX_IS
                + (propertyName.isEmpty() ? "" : propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
        try {
            Method method = containingClass.getMethod(isserName);
            if (method.getReturnType() == boolean.class) {
                return method;
            }
        } catch (NoSuchMethodException e) {
            // intentionally empty
        }
        return null;
    }

    /**
     * @param containingClass never null
     * @param fieldName       never null
     * @return sometimes null
     */
    public static Field getField(Class<?> containingClass, String fieldName) {
        try {
            return containingClass.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * @param containingClass never null
     * @param propertyType    never null
     * @param propertyName    never null
     * @return null if it doesn't exist
     */
    public static Method getSetterMethod(Class<?> containingClass, Class<?> propertyType, String propertyName) {
        String setterName = PROPERTY_MUTATOR_PREFIX
                + (propertyName.isEmpty() ? "" : propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
        try {
            return containingClass.getMethod(setterName, propertyType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private ReflectionHelper() {
    }
}
