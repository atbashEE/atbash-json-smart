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
package be.atbash.json;

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

import be.atbash.json.writer.JSONWriterFactory;
import be.atbash.util.PublicAPI;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A JSON object. Key value pairs are unordered. JSONObject supports
 * java.util.Map interface.
 *
 * @author FangYidong &lt;fangyidong@yahoo.com.cn&gt;
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
@PublicAPI
public class JSONObject extends HashMap<String, Object> implements JSONAware {

    public JSONObject() {
    }

    /**
     * Allows creation of a JSONObject from a Map. After that, both the
     * generated JSONObject and the Map can be modified independently.
     */
    public JSONObject(Map<String, ?> map) {
        super(map);
    }

    /**
     * Puts value to object and returns this.
     * Handy alternative to put(String key, Object value) method.
     *
     * @param fieldName  key with which the specified value is to be associated
     * @param fieldValue value to be associated with the specified key
     * @return this
     */
    public JSONObject appendField(String fieldName, Object fieldValue) {
        put(fieldName, fieldValue);
        return this;
    }

    /**
     * A Simple Helper object to String.
     *
     * @return a value.toString() or null
     */
    public String getAsString(String key) {
        Object obj = this.get(key);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    /**
     * A Simple Helper cast an Object to a Number.
     *
     * @return a Number or null
     */
    public Number getAsNumber(String key) {
        Object obj = this.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return (Number) obj;
        }
        return Long.valueOf(obj.toString());
    }

    /**
     * serialize Object as json to an stream
     */
    public void writeJSONString(Appendable out) throws IOException {
        writeJSON(this, out);
    }

    public void merge(Object object) {
        merge(this, object);
    }

    public String toJSONString() {
        return toJSONString(this);
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }

    /**
     * Convert a map to JSON text. The result is a JSON object. If this map is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
     *
     * @param map The Map to convert.
     * @return JSON text, or "null" if map is null.
     * @see JSONValue#toJSONString(Object)
     */
    public static String toJSONString(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSON(map, sb);
        } catch (IOException e) {
            // can not append on a StringBuilder
            throw new AtbashUnexpectedException(e);
        }
        return sb.toString();
    }

    /**
     * Encode a map into JSON text and write it to out. If this map is also a
     * JSONAware or JSONStreamAware, JSONAware or JSONStreamAware specific
     * behaviours will be ignored at this top level.
     *
     * @see JSONValue#writeJSONString(Object, Appendable)
     */
    private static void writeJSON(Map<String, ?> map, Appendable out)
            throws IOException {
        if (map == null) {
            out.append("null");
            return;
        }
        JSONWriterFactory.getInstance().getJsonMapWriter().writeJSONString(map, out);
    }

    public static JSONObject merge(JSONObject object1, Object object2) {
        if (object2 == null) {
            return object1;
        }
        if (object2 instanceof JSONObject) {
            return merge(object1, (JSONObject) object2);
        }
        throw new JSONMergeException("JSON merge : Cannot merge JSONObject with " + object2.getClass());
    }

    public static JSONObject merge(JSONObject object1, JSONObject object2) {
        if (object2 == null) {
            return object1;
        }
        for (Map.Entry<String, Object> entry : object1.entrySet()) {

            Object value1 = entry.getValue();
            Object value2 = object2.get(entry.getKey());
            if (value2 == null) {
                continue;
            }
            if (value1 instanceof JSONArray) {
                object1.put(entry.getKey(), JSONArray.merge((JSONArray) value1, value2));
                continue;
            }
            if (value1 instanceof JSONObject) {
                object1.put(entry.getKey(), merge((JSONObject) value1, value2));
                continue;
            }
            if (value1.equals(value2)) {
                continue;
            }
            if (value1.getClass().equals(value2.getClass())) {
                throw new JSONMergeException("JSON merge can not merge two " + value1.getClass().getName() + " Object together");
            }
            throw new JSONMergeException("JSON merge can not merge " + value1.getClass().getName() + " with " + value2.getClass().getName());
        }

        for (Map.Entry<String, Object> entry : object2.entrySet()) {

            if (object1.containsKey(entry.getKey())) {
                continue;
            }
            object1.put(entry.getKey(), entry.getValue());
        }
        return object1;
    }

}
