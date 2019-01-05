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
package be.atbash.json;

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

import be.atbash.json.writer.JSONWriterFactory;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong &lt;fangyidong@yahoo.com.cn&gt;
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
public class JSONArray extends ArrayList<Object> implements JSONAware {

    /**
     * Convert a list to JSON text. The result is a JSON array. If this list is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
     *
     * @param list
     * @return JSON text, or "null" if list is null.
     * @see JSONValue#toJSONString(Object)
     */
    public static String toJSONString(List<?> list) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(list, sb);
        } catch (IOException e) {
            throw new AtbashUnexpectedException(e);
        }
        return sb.toString();
    }

    /**
     * Encode a list into JSON text and write it to out. If this list is also a
     * JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific
     * behaviours will be ignored at this top level.
     *
     * @param list
     * @param out
     * @see JSONValue#writeJSONString(Object, Appendable)
     */
    private static void writeJSONString(Iterable<?> list, Appendable out)
            throws IOException {
        if (list == null) {
            out.append("null");
            return;
        }
        JSONWriterFactory.getInstance().getJsonIterableWriter().writeJSONString(list, out);
    }

    /**
     * Appends the specified element and returns this.
     * Handy alternative to add(E e) method.
     *
     * @param element element to be appended to this array.
     * @return this
     */
    public JSONArray appendElement(Object element) {
        add(element);
        return this;
    }

    public void merge(Object object) {
        merge(this, object);
    }

    /**
     * Explicitely Serialize Object as JSon String
     */
    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        try {

            JSONWriterFactory.getInstance().getJsonIterableWriter().writeJSONString(this, sb);

        } catch (IOException e) {
            throw new AtbashUnexpectedException(e);
        }
        return sb.toString();
    }

    /**
     * Override toString().
     */
    @Override
    public String toString() {
        return toJSONString();
    }

    // FIXME Testing of this and the other methods
    public static JSONArray merge(JSONArray array1, Object array2) {
        if (array2 == null) {
            return array1;
        }
        if (array2 instanceof JSONArray) {
            return merge(array1, (JSONArray) array2);
        }
        array1.add(array2);
        return array1;
    }

    public static JSONArray merge(JSONArray array1, JSONArray array2) {
        array1.addAll(array2);
        return array1;
    }

}
