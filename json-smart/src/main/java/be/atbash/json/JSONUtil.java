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

import be.atbash.json.annotate.JsonIgnore;
import be.atbash.json.accessor.FieldFilter;
import be.atbash.json.parser.MappedBy;
import be.atbash.json.style.JSONStyle;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
public final class JSONUtil {

    public static final JsonSmartFieldFilter JSON_SMART_FIELD_FILTER = new JsonSmartFieldFilter();

    private JSONUtil() {
    }

    public static Object convertToX(Object obj, Class<?> dest) {
        if (obj == null) {
            return null;
        }
        if (dest.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        if (dest.isPrimitive()) {
            if (obj instanceof Number) {
                return obj;
            }
            if (dest == int.class) {
                return Integer.valueOf(obj.toString());
            } else if (dest == short.class) {
                return Short.valueOf(obj.toString());
            } else if (dest == long.class) {
                return Long.valueOf(obj.toString());
            } else if (dest == byte.class) {
                return Byte.valueOf(obj.toString());
            } else if (dest == float.class) {
                return Float.valueOf(obj.toString());
            } else if (dest == double.class) {
                return Double.valueOf(obj.toString());
            } else if (dest == char.class) {
                String asString = dest.toString();
                if (asString.length() > 0) {
                    return asString.charAt(0);
                }
            } else if (dest == boolean.class) {
                return obj;
            }
            throw new AtbashUnexpectedException(String.format("Primitive: Can not convert %s to %s", obj.getClass().getName(), dest.getName()));
        } else {
            if (dest.isEnum()) {
                return Enum.valueOf((Class<Enum>) dest, obj.toString());
            }
            if (dest == Integer.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).intValue();
                } else {
                    return Integer.valueOf(obj.toString());
                }
            }
            if (dest == Long.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).longValue();
                } else {
                    return Long.valueOf(obj.toString());
                }
            }
            if (dest == Short.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).shortValue();
                } else {
                    return Short.valueOf(obj.toString());
                }
            }
            if (dest == Byte.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).byteValue();
                } else {
                    return Byte.valueOf(obj.toString());
                }
            }
            if (dest == Float.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).floatValue();
                } else {
                    return Float.valueOf(obj.toString());
                }
            }
            if (dest == Double.class) {
                if (obj instanceof Number) {
                    return ((Number) obj).doubleValue();
                } else {
                    return Double.valueOf(obj.toString());
                }
            }
            if (dest == Character.class) {
                String asString = dest.toString();
                if (asString.length() > 0) {
                    return asString.charAt(0);
                }
            }

            // New Atbash functionality
            MappedBy mappedBy = dest.getAnnotation(MappedBy.class);
            if (mappedBy != null) {
                try {
                    // FIXME Must we also support the CustomBeanJSONEncoder like in JSONReader.getEncoder(java.lang.Class<T>) ?
                    //
                    return mappedBy.encoder().newInstance().parse(obj);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new AtbashUnexpectedException(e);
                }
            }
            throw new AtbashUnexpectedException(String.format("Object: Can not Convert %s to %s", obj.getClass().getName(), dest.getName()));
        }
    }

    /**
     * Write a Key : value entry to a stream
     */
    public static void writeJSONKV(String key, Object value, Appendable out) throws IOException {
        if (key == null) {
            out.append("null");
        } else {
            out.append('"');
            JSONStyle.getDefault().escape(key, out);
            out.append('"');
        }
        JSONStyle.getDefault().objectEndOfKey(out);
        if (value instanceof String) {
            JSONStyle.getDefault().writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out);
        }
        JSONStyle.getDefault().objectElmStop(out);
    }

    public static class JsonSmartFieldFilter implements FieldFilter {

        private boolean canUse(Field field) {
            JsonIgnore ignore = field.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

        @Override
        public boolean canUse(Field field, Method method) {
            boolean result = canUse(field);
            if (!result) {
                return false;
            }
            if (method == null) {
                return true;
            }
            JsonIgnore ignore = method.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

    }

}
