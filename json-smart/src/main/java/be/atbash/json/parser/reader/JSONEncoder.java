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
package be.atbash.json.parser.reader;

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import be.atbash.util.exception.AtbashUnexpectedException;

import java.lang.reflect.Type;

/**
 * Default datatype decoder use by Json-smart to store data.
 * JSONEncoder creates from JSON an instance.
 *
 * @param <T>
 * @author uriel Chemouni
 */
// Original name was JsonReaderI
public abstract class JSONEncoder<T> {

    private static final String ERR_MSG = "Invalid or non Implemented method";

    /**
     * called when json-smart parser meet an object key
     */
    public JSONEncoder<?> startObject(String key) {
        throw new AtbashUnexpectedException(ERR_MSG + " startObject(String key) in " + this.getClass() + " key=" + key);
    }

    /**
     * called when json-smart parser start an array.
     *
     * @param key the destination key name, or null.
     */
    public JSONEncoder<?> startArray(String key) {
        throw new AtbashUnexpectedException(ERR_MSG + " startArray in " + this.getClass() + " key=" + key);
    }

    /**
     * called when json-smart done parsing a value
     */
    public void setValue(T current, String key, Object value) {
        throw new AtbashUnexpectedException(ERR_MSG + " setValue in " + this.getClass() + " key=" + key);
    }

    /**
     * -------------
     */
    public Object getValue(T current, String key) {
        throw new AtbashUnexpectedException(ERR_MSG + " getValue(Object current, String key) in " + this.getClass() + " key=" + key);
    }

    // Object current,
    public Type getType(String key) {
        throw new AtbashUnexpectedException(ERR_MSG + " getType(String key) in " + this.getClass() + " key=" + key);
    }

    /**
     * add a value in an array json object.
     */
    public void addValue(Object current, Object value) {
        throw new AtbashUnexpectedException(ERR_MSG + " addValue(Object current, Object value) in " + this.getClass());
    }

    /**
     * use to instantiate a new object that will be used as an object
     */
    public T createObject() {
        throw new AtbashUnexpectedException(ERR_MSG + " createObject() in " + this.getClass());
    }

    /**
     * use to instantiate a new object that will be used as an array
     */
    public Object createArray() {
        throw new AtbashUnexpectedException(ERR_MSG + " createArray() in " + this.getClass());
    }

    /**
     * Allow a decoder to convert a temporary structure to the final data format.
     * <p>
     * example: convert an List&lt;Integer&gt; to an int[]
     */
    public T convert(Object current) {
        return (T) current;
    }
}
