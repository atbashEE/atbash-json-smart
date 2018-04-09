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
package be.atbash.json.parser.reader;

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

import java.io.IOException;
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

    private static String ERR_MSG = "Invalid or non Implemented status";
    private final JSONReader jsonReader;

    /**
     * Encoder is linked to a JsonReader.
     *
     * @param jsonReader
     */
    public JSONEncoder(JSONReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    public JSONReader getJSONReader() {
        return jsonReader;
    }

    /**
     * called when json-smart parser meet an object key
     */
    public JSONEncoder<?> startObject(String key) throws IOException {
        throw new RuntimeException(ERR_MSG + " startObject(String key) in " + this.getClass() + " key=" + key);
    }

    /**
     * called when json-smart parser start an array.
     *
     * @param key the destination key name, or null.
     */
    public JSONEncoder<?> startArray(String key) throws IOException {
        throw new RuntimeException(ERR_MSG + " startArray in " + this.getClass() + " key=" + key);
    }

    /**
     * called when json-smart done parsing a value
     */
    public void setValue(Object current, String key, Object value) throws IOException {
        throw new RuntimeException(ERR_MSG + " setValue in " + this.getClass() + " key=" + key);
    }

    /**
     * -------------
     */
    public Object getValue(Object current, String key) {
        throw new RuntimeException(ERR_MSG + " getValue(Object current, String key) in " + this.getClass() + " key=" + key);
    }

    // Object current,
    public Type getType(String key) {
        throw new RuntimeException(ERR_MSG + " getType(String key) in " + this.getClass() + " key=" + key);
    }

    /**
     * add a value in an array json object.
     */
    public void addValue(Object current, Object value) {
        throw new RuntimeException(ERR_MSG + " addValue(Object current, Object value) in " + this.getClass());
    }

    /**
     * use to instantiate a new object that will be used as an object
     */
    public Object createObject() {
        throw new RuntimeException(ERR_MSG + " createObject() in " + this.getClass());
    }

    /**
     * use to instantiate a new object that will be used as an array
     */
    public Object createArray() {
        throw new RuntimeException(ERR_MSG + " createArray() in " + this.getClass());
    }

    /**
     * Allow a decoder to convert a temporary structure to the final data format.
     * <p>
     * example: convert an List&lt;Integer&gt; to an int[]
     */
    @SuppressWarnings("unchecked")
    public T convert(Object current) {
        return (T) current;
    }
}
