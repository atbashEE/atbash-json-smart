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
package be.atbash.json.writer;

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

import be.atbash.json.JSONArray;
import be.atbash.json.JSONAware;
import be.atbash.json.JSONObject;
import be.atbash.json.parser.MappedBy;
import be.atbash.util.reflection.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONReader {
    private final ConcurrentHashMap<Type, JSONEncoder<?>> cache;

    public JSONEncoder<JSONAware> DEFAULT;
    public JSONEncoder<JSONAware> DEFAULT_ORDERED;

    public JSONReader() {
        cache = new ConcurrentHashMap<>(100);

        cache.put(Date.class, BeansJSONEncoder.JSONEncoderDate);

        cache.put(int[].class, ArraysJSONEncoder.JSONEncoderPrimInt);
        cache.put(Integer[].class, ArraysJSONEncoder.JSONEncoderInt);

        cache.put(short[].class, ArraysJSONEncoder.JSONEncoderPrimInt);
        cache.put(Short[].class, ArraysJSONEncoder.JSONEncoderInt);

        cache.put(long[].class, ArraysJSONEncoder.JSONEncoderPrimLong);
        cache.put(Long[].class, ArraysJSONEncoder.JSONEncoderLong);

        cache.put(byte[].class, ArraysJSONEncoder.JSONEncoderPrimByte);
        cache.put(Byte[].class, ArraysJSONEncoder.JSONEncoderByte);

        cache.put(char[].class, ArraysJSONEncoder.JSONEncoderPrimChar);
        cache.put(Character[].class, ArraysJSONEncoder.JSONEncoderChar);

        cache.put(float[].class, ArraysJSONEncoder.JSONEncoderPrimFloat);
        cache.put(Float[].class, ArraysJSONEncoder.JSONEncoderFloat);

        cache.put(double[].class, ArraysJSONEncoder.JSONEncoderPrimDouble);
        cache.put(Double[].class, ArraysJSONEncoder.JSONEncoderDouble);

        cache.put(boolean[].class, ArraysJSONEncoder.JSONEncoderPrimBool);
        cache.put(Boolean[].class, ArraysJSONEncoder.JSONEncoderBool);

        this.DEFAULT = new DefaultJSONEncoder<>(this);
        this.DEFAULT_ORDERED = new DefaultJSONEncoderOrdered(this);

        cache.put(JSONAware.class, this.DEFAULT);
        cache.put(JSONArray.class, this.DEFAULT);
        cache.put(JSONObject.class, this.DEFAULT);
    }

    public <T> void registerReader(Class<T> type, JSONEncoder<T> JSONEncoder) {
        cache.put(type, JSONEncoder);
    }

    @SuppressWarnings("unchecked")
    public <T> JSONEncoder<T> getEncoder(Type type) {
        if (type instanceof ParameterizedType) {
            return getEncoder((ParameterizedType) type);
        }
        return getEncoder((Class<T>) type);
    }

    /**
     * Get the corresponding Encoder Class, or create it on first call
     *
     * @param type to be map
     */
    public <T> JSONEncoder<T> getEncoder(Class<T> type) {
        // look for cached JSONEncoder
        @SuppressWarnings("unchecked")
        JSONEncoder<T> encoder = (JSONEncoder<T>) cache.get(type);
        if (encoder != null) {
            return encoder;
        }
        /*
         * Special handle
         */
        if (type instanceof Class) {
            if (Map.class.isAssignableFrom(type)) {
                encoder = new DefaultJSONEncoderCollection<>(this, type);
            } else if (List.class.isAssignableFrom(type)) {
                encoder = new DefaultJSONEncoderCollection<>(this, type);
            }
            if (encoder != null) {
                cache.put(type, encoder);
                return encoder;
            }
        }

        if (type.isArray()) {
            encoder = new ArraysJSONEncoder.GenericJSONEncoder<>(this, type);
        } else if (List.class.isAssignableFrom(type)) {
            encoder = new CollectionMapper.ListClass<>(this, type);
        } else if (Map.class.isAssignableFrom(type)) {
            encoder = new CollectionMapper.MapClass<>(this, type);
        } else
        // use bean class
        {

            MappedBy mappedBy = type.getAnnotation(MappedBy.class);
            if (mappedBy != null) {
                if (!(mappedBy.decoder().equals(CustomJSONEncoder.NOPCustomJSONEncoder.class))) {
                    encoder = ClassUtils.newInstance(mappedBy.decoder(), this);
                }
            }
            if (encoder == null) {
                encoder = new BeansJSONEncoder.Bean<>(this, type);
            }
        }
        cache.putIfAbsent(type, encoder);
        return encoder;
    }

    @SuppressWarnings("unchecked")
    public <T> JSONEncoder<T> getEncoder(ParameterizedType type) {
        JSONEncoder<T> map = (JSONEncoder<T>) cache.get(type);
        if (map != null) {
            return map;
        }
        Class<T> clz = (Class<T>) type.getRawType();
        if (List.class.isAssignableFrom(clz)) {
            map = new CollectionMapper.ListType<>(this, type);
        } else if (Map.class.isAssignableFrom(clz)) {
            map = new CollectionMapper.MapType<>(this, type);
        }
        cache.putIfAbsent(type, map);
        return map;
    }
}
