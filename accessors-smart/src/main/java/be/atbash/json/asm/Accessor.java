/*
 * Copyright 2017-2018 Rudy De Busscher (https://www.atbash.be)
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

import be.atbash.json.asm.mapper.FieldPropertyNameMapperHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Contains all information needed to access a java field.
 * <p>
 * field, getter setter
 * <p>
 * this object is used internally by BeansAccess
 *
 * @author Uriel Chemouni
 * @author Rudy De Busscher
 * @see BeansAccess
 */
public class Accessor {

    private static final FieldPropertyNameMapperHandler MAPPER_HANDLER = new FieldPropertyNameMapperHandler();

    /**
     * Field to access
     */
    protected Field field;
    /**
     * Setter Methods if available
     */
    protected Method setter;
    /**
     * getter Methods if available
     */
    protected Method getter;
    /**
     * Filed index in object
     */
    protected int index;
    /**
     * Filed Class
     */
    protected Class<?> type;
    /**
     * Filed Type using JDK 5+ generics if available
     */
    protected Type genericType;

    protected String fieldName;

    protected String propertyName;

    protected Annotation[] annotations;

    /**
     * getter for index
     *
     * @return Index
     */
    public int getIndex() {
        return index;
    }

    /**
     * is the field access using Field access type
     *
     * @return if Accessor is public
     */
    public boolean isPublic() {
        return setter == null;
    }

    /**
     * is the field is an enum field
     *
     * @return if Accessor return an Enum Class
     */
    public boolean isEnum() {
        return type.isEnum();
    }

    /**
     * return the field name
     *
     * @return the field name
     */
    public String getName() {
        return fieldName;
    }

    /**
     * return field Class
     *
     * @return field Class
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * return generics field Type.
     *
     * @return generics field Type.
     */
    public Type getGenericType() {
        return genericType;
    }

    /**
     * @return true if the field can be read or write
     */
    public boolean isUsable() {
        return field != null || getter != null || setter != null;
    }

    /**
     * @return true if the field can be read
     */
    public boolean isReadable() {
        return field != null || getter != null;
    }

    /**
     * @return true if the field can be write
     */
    public boolean isWritable() {
        return field != null || getter != null;
    }

    /**
     * The property name for the field. This can be the field name or another name specified by JSONProperty of json-smart or any other framework supported through the SPI.
     *
     * @return The property name within JSON
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Return the specified annotation on the field.
     *
     * @param annotationClass
     * @param <T>
     * @return The annotation instance on the field or null when no such annotation is present.
     */
    public <T> T getAnnotation(Class<T> annotationClass) {
        T result = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                result = (T) annotation;
            }
        }
        return result;
    }

    /**
     * build accessor for a field
     *
     * @param c     the handled class
     * @param field the field to access
     */
    public Accessor(Class<?> c, Field field, FieldFilter filter) {
        this.fieldName = field.getName();
        int m = field.getModifiers();

        if ((m & (Modifier.STATIC | Modifier.TRANSIENT)) > 0) {
            return;
        }

        if ((m & Modifier.PUBLIC) > 0) {
            this.field = field;
        }

        String name = ASMUtil.getSetterName(field.getName());
        try {
            setter = c.getDeclaredMethod(name, field.getType());
        } catch (Exception e) {
            // setter is not found, but we can carry on.
        }
        boolean isBool = field.getType().equals(Boolean.TYPE);
        if (isBool) {
            name = ASMUtil.getIsName(field.getName());
        } else {
            name = ASMUtil.getGetterName(field.getName());
        }
        try {
            getter = c.getDeclaredMethod(name);
        } catch (Exception e) {
            // getter is not found, but we can carry on.
        }
        if (getter == null && isBool) {
            try {
                getter = c.getDeclaredMethod(ASMUtil.getGetterName(field.getName()));
            } catch (Exception e) {
                // getter is not found, but we can carry on.
            }
        }

        // Field not public and no getter and/or setter -> not a candidate
        // Same as isUseable(), but should not use public method from within constructor (do not allow functionality change from within subclass)
        if (this.field == null && getter == null && setter == null) {
            return;
        }

        if (getter != null && !filter.canUse(field, getter)) {
            getter = null;
        }

        if (setter != null && !filter.canUse(field, setter)) {
            setter = null;
        }

        // Field not public and no getter and/or setter which we can use -> not a candidate
        // Same as isUseable(), but should not use public method from within constructor (do not allow functionality change from within subclass)
        if (getter == null && setter == null && this.field == null) {
            return;
        }

        this.type = field.getType();
        this.genericType = field.getGenericType();

        annotations = field.getDeclaredAnnotations();

        propertyName = MAPPER_HANDLER.definePropertyName(this);
    }

}
