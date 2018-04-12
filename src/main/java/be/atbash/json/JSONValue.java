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

import be.atbash.json.parser.JSONParser;
import be.atbash.json.parser.MappedBy;
import be.atbash.json.parser.ParseException;
import be.atbash.json.parser.reader.FakeJSONEncoder;
import be.atbash.json.parser.reader.JSONEncoder;
import be.atbash.json.parser.reader.JSONEncoderFactory;
import be.atbash.json.style.JSONStyle;
import be.atbash.json.writer.JSONWriter;
import be.atbash.json.writer.JSONWriterFactory;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSONValue is the helper class In most of the cases you should use those static
 * method to use JSON-smart.
 * <p>
 * <p>
 * The most commonly use method are {@link #T parse(String, Class<T>)}
 * {@link #toJSONString(Object)}
 *
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 * @author Rudy De Busscher
 */
public class JSONValue {

    /**
     * Parse input json as a mapTo class
     * <p>
     * mapTo can be a bean
     */
    public static <T> T parse(String json, Class<T> mapTo) {
        JSONParser parser = new JSONParser();
        return parser.parse(json, JSONEncoderFactory.getInstance().getEncoder(mapTo));
    }

    public static Object parse(String json, TypeReference mapTo) {
        JSONParser parser = new JSONParser();
        return parser.parse(json, JSONEncoderFactory.getInstance().getEncoder(mapTo.getType()));
    }

    /**
     * Parse JSON text into java object from the input source.
     *
     * @return Instance of the following: JSONObject, JSONArray, String,
     * java.lang.Number, java.lang.Boolean, null
     */
    public static Object parse(String json) {
        return new JSONParser().parse(json);
    }

    /**
     * Check Json Syntax from input String
     *
     * @return if the input is valid
     */
    public static boolean isValidJson(String s) {
        try {
            new JSONParser().parse(s, FakeJSONEncoder.getInstance());
            return true;
        } catch (ParseException e) {
            return false;
            // TODO Next version a method where we return the exception (and thus where the problem is)?
        }
    }

    /**
     * Register a serializer for a class.
     * TODO Document
     */
    public static <T> void registerWriter(Class<?> cls, JSONWriter<T> writer) {
        JSONWriterFactory.getInstance().registerWriter(writer, cls);
    }

    /**
     * register a deserializer for a class.
     * TODO Document
     */
    public static <T> void registerEncoder(Class<T> type, JSONEncoder<T> jsonEncoder) {
        JSONEncoderFactory.getInstance().registerEncoder(type, jsonEncoder);
    }

    /**
     * Encode an object into JSON text and write it to out.
     * <p>
     * If this object is a Map or a List, and it's also a JSONStreamAware or a
     * JSONAware, JSONStreamAware or JSONAware will be considered firstly.
     * <p>
     *
     * @see JSONObject#writeJSON(Map, Appendable)
     */
    @SuppressWarnings("unchecked")
    public static void writeJSONString(Object value, Appendable out) throws IOException {
        if (value == null) {
            out.append("null");
            return;
        }
        Class<?> clz = value.getClass();
        @SuppressWarnings("rawtypes")
        JSONWriter w = JSONWriterFactory.getInstance().getWriter(clz);
        if (w == null) {
            if (clz.isArray()) {
                w = JSONWriterFactory.getInstance().getArrayWriter();
            } else {
                w = JSONWriterFactory.getInstance().getWriterByInterface(value.getClass());
                // Atbash support for Custom Writer
                if (w == null) {
                    MappedBy mappedBy = value.getClass().getAnnotation(MappedBy.class);
                    if (mappedBy != null && !mappedBy.writer().equals(JSONWriter.NOPJSONWriter.class)) {
                        try {
                            w = mappedBy.writer().newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new AtbashUnexpectedException(e);
                        }
                    }
                }
                if (w == null) {
                    w = JSONWriterFactory.getInstance().getBeansWriter();
                }
            }
            JSONWriterFactory.getInstance().registerWriter(w, clz);
        }
        w.writeJSONString(value, out);
    }

    /**
     * Convert an object to JSON text.
     * <p>
     * If this object is a Map or a List, and it's also a JSONAware, JSONAware
     * will be considered firstly.
     * <p>
     * DO NOT call this method from toJSONString() of a class that implements
     * both JSONAware and Map or List with "this" as the parameter, use
     * JSONObject.toJSONString(Map) or JSONArray.toJSONString(List) instead.
     *
     * @return JSON text, or "null" if value is null or it's an NaN or an INF
     * number.
     * @see JSONObject#toJSONString(Map)
     * @see JSONArray#toJSONString(List)
     */
    public static String toJSONString(Object value) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(value, sb);
        } catch (IOException e) {
            throw new AtbashUnexpectedException(e); //Should never happen with SingBuilder
        }
        return sb.toString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
     * (U+0000 through U+001F).
     */
    // TODO Convenient method for other usecases (not used within json-smart) Move also to StringUtils.
    public static String escape(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        JSONStyle.getDefault().escape(s, sb);
        return sb.toString();
    }

}
