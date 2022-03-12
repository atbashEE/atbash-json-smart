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
import be.atbash.util.exception.AtbashUnexpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface.
 *
 * @author FangYidong &lt;fangyidong@yahoo.com.cn&gt;
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
public class JSONArray extends ArrayList<Object> implements JSONAware {

    public JSONArray() {
    }

    public JSONArray(Collection<?> c) {
        super(c);
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

    /**
     * Merge the Object into this JSONArray. This is, append it at the end.
     */
    public void merge(Object object) {
        merge(this, object);
    }

    /**
     * Explicitly Serialize Object as JSon String.
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
     * Override toString() and calling {@link toJSONString()}.
     */
    @Override
    public String toString() {
        return toJSONString();
    }

    /**
     * Appends the Object to the JSONArray.  If Object is a JSONArray, the elements are added individually.
     *
     * @param array  JSONArray where we append some elements.
     * @param object The Object to add.
     * @return The same as the JSONArray parameter.
     */
    public static JSONArray merge(JSONArray array, Object object) {
        if (object == null) {
            return array;
        }
        if (object instanceof JSONArray) {
            return merge(array, (JSONArray) object);
        }
        array.add(object);
        return array;
    }

    public static JSONArray merge(JSONArray array1, JSONArray array2) {
        if (array2 != null) {
            array1.addAll(array2);
        }
        return array1;
    }

    /**
     * Convert a list to JSON text. The result is a JSON array. If this list is
     * also a JSONAware, JSONAware specific behaviours will be omitted at this
     * top level.
     *
     * @param list The List to convert.
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
     * @param list The List to Convert
     * @param out  The Appendable to concat the contents to.
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

}
