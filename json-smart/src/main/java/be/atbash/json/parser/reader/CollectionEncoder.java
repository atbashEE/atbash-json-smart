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

import be.atbash.json.JSONArray;
import be.atbash.json.JSONObject;
import be.atbash.json.JSONUtil;
import be.atbash.json.accessor.BeansAccess;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public final class CollectionEncoder {
    // CollectionEncoder is just holder of the 4 collection encoders
    private CollectionEncoder() {
    }

    public static class MapType<T> extends JSONEncoder<T> {
        private final ParameterizedType type;
        private final Class<?> rawClass;
        private final Class<?> instance;

        private final Type keyType;
        private final Type valueType;

        private final Class<?> keyClass;
        private final Class<?> valueClass;

        private JSONEncoder<?> subJSONEncoder;

        MapType(ParameterizedType type) {
            this.type = type;
            this.rawClass = (Class<?>) type.getRawType();
            if (rawClass.isInterface()) {
                instance = JSONObject.class;
            } else {
                instance = rawClass;
            }

            keyType = type.getActualTypeArguments()[0];
            valueType = type.getActualTypeArguments()[1];
            if (keyType instanceof Class) {
                keyClass = (Class<?>) keyType;
            } else {
                keyClass = (Class<?>) ((ParameterizedType) keyType).getRawType();
            }
            if (valueType instanceof Class) {
                valueClass = (Class<?>) valueType;
            } else {
                valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
            }
        }

        @Override
        public T createObject() {
            try {
                return (T) instance.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AtbashUnexpectedException(e);
            }
        }

        @Override
        public JSONEncoder<?> startArray(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(valueType);
            }
            return subJSONEncoder;
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(valueType);
            }
            return subJSONEncoder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setValue(Object current, String key, Object value) {
            ((Map<Object, Object>) current).put(JSONUtil.convertToX(key, keyClass),
                    JSONUtil.convertToX(value, valueClass));
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object getValue(Object current, String key) {
            return ((Map<String, Object>) current).get(JSONUtil.convertToX(key, keyClass));
        }

        @Override
        public Type getType(String key) {
            return type;
        }
    }

    public static class MapClass<T> extends JSONEncoder<T> {
        private final Class<?> type;
        private final Class<?> instance;
        private final BeansAccess<?> beansAccess;

        MapClass(Class<?> type) {
            this.type = type;
            if (type.isInterface()) {
                instance = JSONObject.class;
            } else {
                instance = type;
            }
            beansAccess = BeansAccess.get(instance, JSONUtil.JSON_SMART_FIELD_FILTER);
        }

        @Override
        public T createObject() {
            return (T) beansAccess.newInstance();
        }

        @Override
        public JSONEncoder<?> startArray(String key) {
            return DefaultJSONEncoder.getInstance(); // _ARRAY
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            return DefaultJSONEncoder.getInstance(); // _MAP
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setValue(Object current, String key, Object value) {
            ((Map<String, Object>) current).put(key, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object getValue(Object current, String key) {
            return ((Map<String, Object>) current).get(key);
        }

        @Override
        public Type getType(String key) {
            return type;
        }
    }

    public static class ListType<T> extends JSONEncoder<T> {
        private final ParameterizedType type;
        private final Class<?> rawClass;
        private final Class<?> instance;
        private final BeansAccess<?> beansAccess;

        private final Type valueType;
        private final Class<?> valueClass;

        private JSONEncoder<?> subJSONEncoder;

        ListType(ParameterizedType type) {
            this.type = type;
            this.rawClass = (Class<?>) type.getRawType();
            if (rawClass.isInterface()) {
                instance = JSONArray.class;
            } else {
                instance = rawClass;
            }
            beansAccess = BeansAccess.get(instance, JSONUtil.JSON_SMART_FIELD_FILTER); // NEW
            valueType = type.getActualTypeArguments()[0];
            if (valueType instanceof Class) {
                valueClass = (Class<?>) valueType;
            } else {
                valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
            }
        }

        @Override
        public Object createArray() {
            return beansAccess.newInstance();
        }

        @Override
        public JSONEncoder<?> startArray(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(type.getActualTypeArguments()[0]);
            }
            return subJSONEncoder;
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(type.getActualTypeArguments()[0]);
            }
            return subJSONEncoder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void addValue(Object current, Object value) {
            ((List<Object>) current).add(JSONUtil.convertToX(value, valueClass));
        }
    }

    public static class ListClass<T> extends JSONEncoder<T> {
        final Class<?> type;
        final Class<?> instance;
        final BeansAccess<?> beansAccess;

        ListClass(Class<?> clazz) {
            this.type = clazz;
            if (clazz.isInterface()) {
                instance = JSONArray.class;
            } else {
                instance = clazz;
            }
            beansAccess = BeansAccess.get(instance, JSONUtil.JSON_SMART_FIELD_FILTER);
        }

        @Override
        public Object createArray() {
            return beansAccess.newInstance();
        }

        @Override
        public JSONEncoder<?> startArray(String key) {
            return DefaultJSONEncoder.getInstance();// _ARRAY;
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            return DefaultJSONEncoder.getInstance();// _MAP;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void addValue(Object current, Object value) {
            ((List<Object>) current).add(value);
        }
    }

}
