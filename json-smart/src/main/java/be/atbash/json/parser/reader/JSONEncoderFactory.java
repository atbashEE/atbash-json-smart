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

import be.atbash.json.parser.CustomJSONEncoder;
import be.atbash.json.parser.MappedBy;
import be.atbash.json.writer.CustomBeanJSONEncoder;
import be.atbash.util.exception.AtbashException;
import be.atbash.util.reflection.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONEncoderFactory {
    private static final Object LOCK = new Object();
    private static JSONEncoderFactory INSTANCE;

    private ConcurrentHashMap<Type, JSONEncoder<?>> cache;

    private JSONEncoderFactory() {
    }

    private void initEncoders() {
        cache = new ConcurrentHashMap<>(20);

        cache.put(Date.class, BeanEncoder.getJsonEncoderDate());

        cache.put(int[].class, ArraysJSONEncoder.getJSONEncoderPrimInt());
        cache.put(Integer[].class, ArraysJSONEncoder.getJSONEncoderInt());

        cache.put(short[].class, ArraysJSONEncoder.getJSONEncoderPrimShort());
        cache.put(Short[].class, ArraysJSONEncoder.getJSONEncoderShort());

        cache.put(long[].class, ArraysJSONEncoder.getJSONEncoderPrimLong());
        cache.put(Long[].class, ArraysJSONEncoder.getJSONEncoderLong());

        cache.put(byte[].class, ArraysJSONEncoder.getJSONEncoderPrimByte());
        cache.put(Byte[].class, ArraysJSONEncoder.getJSONEncoderByte());

        cache.put(char[].class, ArraysJSONEncoder.getJSONEncoderPrimChar());
        cache.put(Character[].class, ArraysJSONEncoder.getJSONEncoderChar());

        cache.put(float[].class, ArraysJSONEncoder.getJSONEncoderPrimFloat());
        cache.put(Float[].class, ArraysJSONEncoder.getJSONEncoderFloat());

        cache.put(double[].class, ArraysJSONEncoder.getJSONEncoderPrimDouble());
        cache.put(Double[].class, ArraysJSONEncoder.getJSONEncoderDouble());

        cache.put(boolean[].class, ArraysJSONEncoder.getJSONEncoderPrimBool());
        cache.put(Boolean[].class, ArraysJSONEncoder.getJSONEncoderBool());
    }

    // FIXME Example of programmatic and through Config.
    public <T> void registerEncoder(Class<T> type, JSONEncoder<T> jsonEncoder) {
        cache.put(type, jsonEncoder);
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

        if (type.isArray()) {
            encoder = new ArraysJSONEncoder.GenericJSONEncoder<>(type);
        } else if (List.class.isAssignableFrom(type)) {
            encoder = new CollectionEncoder.ListClass<>(type);
        } else if (Map.class.isAssignableFrom(type)) {
            encoder = new CollectionEncoder.MapClass<>(type);
        } else {
            // Check for the @MappedBy
            MappedBy mappedBy = type.getAnnotation(MappedBy.class);
            if (mappedBy != null) {
                if (!(mappedBy.encoder().equals(CustomJSONEncoder.NOPJSONEncoder.class))) {

                    encoder = new JSONEncoderWrappedCustomEncoder((CustomJSONEncoder) ClassUtils.newInstance(mappedBy.encoder()));

                } else {
                    if (!(mappedBy.beanEncoder().equals(CustomBeanJSONEncoder.NOPCustomBeanJSONEncoder.class))) {
                        encoder = ClassUtils.newInstance(mappedBy.beanEncoder());
                    }
                }
            }

            // use bean class
            if (encoder == null) {
                encoder = new BeanEncoder<>(type);
            }
        }
        cache.putIfAbsent(type, encoder);
        return encoder;
    }

    @SuppressWarnings("unchecked")
    private <T> JSONEncoder<T> getEncoder(ParameterizedType type) {
        JSONEncoder<T> encoder = (JSONEncoder<T>) cache.get(type);
        if (encoder != null) {
            return encoder;
        }
        Class<T> clz = (Class<T>) type.getRawType();
        if (List.class.isAssignableFrom(clz)) {
            encoder = new CollectionEncoder.ListType<>(type);
        } else if (Map.class.isAssignableFrom(clz)) {
            encoder = new CollectionEncoder.MapType<>(type);
        }
        if (encoder == null) {
            throw new UnsupportedParameterizedTypeException(type);
        }
        cache.putIfAbsent(type, encoder);
        return encoder;
    }

    /**
     * Returns an instance of the factory, create and initialize if needed.
     */
    public static JSONEncoderFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new JSONEncoderFactory();
                    INSTANCE.initEncoders();
                }
            }
        }
        return INSTANCE;
    }

}
