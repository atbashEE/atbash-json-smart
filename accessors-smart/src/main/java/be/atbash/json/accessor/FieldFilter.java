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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * allow to control read/write access to field
 */
public interface FieldFilter {

    /**
     * Determines if this field can be used in the Java - Json conversion. For example, an
     * annotation JsonIgnore can be used to indicate the field should be ignored.
     * @param field  The Field in question
     * @param method optional, when no getter for field null is passed to this method.
     * @return true when Field should be used.
     */
    boolean canUse(Field field, Method method);

}
