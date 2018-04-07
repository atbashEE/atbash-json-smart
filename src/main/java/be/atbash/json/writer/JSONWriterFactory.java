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

import be.atbash.json.JSONAware;
import be.atbash.json.JSONValue;
import be.atbash.json.style.JSONStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Old name JSONWriter
public class JSONWriterFactory {
    private ConcurrentHashMap<Class<?>, JSONWriter<?>> data;
    private LinkedList<WriterByInterface> writerInterfaces;

    public JSONWriterFactory() {
        data = new ConcurrentHashMap<>();
        writerInterfaces = new LinkedList<>();
        init();
    }

    static class WriterByInterface {
        Class<?> _interface;
        JSONWriter<?> _writer;

        WriterByInterface(Class<?> _interface, JSONWriter<?> _writer) {
            this._interface = _interface;
            this._writer = _writer;
        }
    }

    private final static JSONWriter<JSONAware> JSONAwareJSONWriter = new JSONWriter<JSONAware>() {
        public <E extends JSONAware> void writeJSONString(E value, Appendable out) throws IOException {
            out.append(value.toJSONString());
        }
    };

    /**
     * try to find a Writer by Checking implemented interface
     *
     * @param clazz class to serialize
     * @return a Writer or null
     */
    @SuppressWarnings("rawtypes")
    public JSONWriter getWriterByInterface(Class<?> clazz) {
        for (WriterByInterface w : writerInterfaces) {
            if (w._interface.isAssignableFrom(clazz)) {
                return w._writer;
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public JSONWriter getWriter(Class cls) {
        return data.get(cls);
    }

    public final static JSONWriter<Iterable<?>> JSONIterableWriter = new JSONWriter<Iterable<?>>() {
        public <E extends Iterable<?>> void writeJSONString(E list, Appendable out) throws IOException {
            boolean first = true;
            JSONStyle.DEFAULT.arrayStart(out);
            for (Object value : list) {
                if (first) {
                    first = false;
                    JSONStyle.DEFAULT.arrayfirstObject(out);
                } else {
                    JSONStyle.DEFAULT.arrayNextElm(out);
                }
                if (value == null) {
                    out.append("null");
                } else {
                    JSONValue.writeJSONString(value, out);
                }
                JSONStyle.DEFAULT.arrayObjectEnd(out);
            }
            JSONStyle.DEFAULT.arrayStop(out);
        }
    };

    private final static JSONWriter<Enum<?>> EnumWriter = new JSONWriter<Enum<?>>() {
        public <E extends Enum<?>> void writeJSONString(E value, Appendable out) throws IOException {
            @SuppressWarnings("rawtypes")
            String s = value.name();
            JSONStyle.DEFAULT.writeString(out, s);
        }
    };

    public final static JSONWriter<Map<String, ?>> JSONMapWriter = new JSONWriter<Map<String, ?>>() {
        public <E extends Map<String, ?>> void writeJSONString(E map, Appendable out) throws IOException {
            boolean first = true;
            JSONStyle.DEFAULT.objectStart(out);
            /*
             * do not use <String, Object> to handle non String key maps
             */
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v == null && JSONStyle.DEFAULT.ignoreNull()) {
                    continue;
                }
                if (first) {
                    JSONStyle.DEFAULT.objectFirstStart(out);
                    first = false;
                } else {
                    JSONStyle.DEFAULT.objectNext(out);
                }
                JSONWriterFactory.writeJSONKV(entry.getKey().toString(), v, out);
                // compression.objectElmStop(out);
            }
            JSONStyle.DEFAULT.objectStop(out);
        }
    };

    /**
     * Json-Smart V2 Beans serialiser
     */
    public final static JSONWriter<Object> beansWriter = new BeansWriter();

    /**
     * Json-Smart ArrayWriterClass
     */
    public final static JSONWriter<Object> arrayWriter = new ArrayWriter();

    /**
     * ToString Writer
     */
    public final static JSONWriter<Object> toStringWriter = new JSONWriter<Object>() {
        public void writeJSONString(Object value, Appendable out) throws IOException {
            out.append(value.toString());
        }
    };

    private void init() {
        registerWriter(new JSONWriter<String>() {
            public void writeJSONString(String value, Appendable out) throws IOException {
                JSONStyle.DEFAULT.writeString(out, value);
            }
        }, String.class);

        registerWriter(new JSONWriter<Double>() {
            public void writeJSONString(Double value, Appendable out) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Double.class);

        registerWriter(new JSONWriter<Date>() {
            public void writeJSONString(Date value, Appendable out) throws IOException {
                out.append('"');
                JSONValue.escape(value.toString(), out);
                out.append('"');
            }
        }, Date.class);

        registerWriter(new JSONWriter<Float>() {
            public void writeJSONString(Float value, Appendable out) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Float.class);

        registerWriter(toStringWriter, Integer.class, Long.class, Byte.class, Short.class, BigInteger.class, BigDecimal.class);
        registerWriter(toStringWriter, Boolean.class);

        /*
         * Array
         */

        registerWriter(new JSONWriter<int[]>() {
            public void writeJSONString(int[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (int b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Integer.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, int[].class);

        registerWriter(new JSONWriter<short[]>() {
            public void writeJSONString(short[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (short b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Short.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, short[].class);

        registerWriter(new JSONWriter<long[]>() {
            public void writeJSONString(long[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (long b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Long.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, long[].class);

        registerWriter(new JSONWriter<float[]>() {
            public void writeJSONString(float[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (float b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Float.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, float[].class);

        registerWriter(new JSONWriter<double[]>() {
            public void writeJSONString(double[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (double b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Double.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, double[].class);

        registerWriter(new JSONWriter<boolean[]>() {
            public void writeJSONString(boolean[] value, Appendable out) throws IOException {
                boolean needSep = false;
                JSONStyle.DEFAULT.arrayStart(out);
                for (boolean b : value) {
                    if (needSep) {
                        JSONStyle.DEFAULT.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Boolean.toString(b));
                }
                JSONStyle.DEFAULT.arrayStop(out);
            }
        }, boolean[].class);

        registerWriterInterface(JSONAware.class, JSONWriterFactory.JSONAwareJSONWriter);
        registerWriterInterface(Map.class, JSONWriterFactory.JSONMapWriter);
        registerWriterInterface(Iterable.class, JSONWriterFactory.JSONIterableWriter);
        registerWriterInterface(Enum.class, JSONWriterFactory.EnumWriter);
        registerWriterInterface(Number.class, JSONWriterFactory.toStringWriter);
    }

    /**
     * associate an Writer to a interface With Hi priority
     *
     * @param interFace interface to map
     * @param writer    writer Object
     * @deprecated use registerWriterInterfaceFirst
     */
    public void addInterfaceWriterFirst(Class<?> interFace, JSONWriter<?> writer) {
        registerWriterInterfaceFirst(interFace, writer);
    }

    /**
     * associate an Writer to a interface With Low priority
     *
     * @param interFace interface to map
     * @param writer    writer Object
     * @deprecated use registerWriterInterfaceLast
     */
    public void addInterfaceWriterLast(Class<?> interFace, JSONWriter<?> writer) {
        registerWriterInterfaceLast(interFace, writer);
    }

    /**
     * associate an Writer to a interface With Low priority
     *
     * @param interFace interface to map
     * @param writer    writer Object
     */
    public void registerWriterInterfaceLast(Class<?> interFace, JSONWriter<?> writer) {
        writerInterfaces.addLast(new WriterByInterface(interFace, writer));
    }

    /**
     * associate an Writer to a interface With Hi priority
     *
     * @param interFace interface to map
     * @param writer    writer Object
     */
    public void registerWriterInterfaceFirst(Class<?> interFace, JSONWriter<?> writer) {
        writerInterfaces.addFirst(new WriterByInterface(interFace, writer));
    }

    /**
     * an alias for registerWriterInterfaceLast
     *
     * @param interFace interface to map
     * @param writer    writer Object
     */
    public void registerWriterInterface(Class<?> interFace, JSONWriter<?> writer) {
        registerWriterInterfaceLast(interFace, writer);
    }

    /**
     * associate an Writer to a Class
     *
     * @param writer
     * @param cls
     */
    public <T> void registerWriter(JSONWriter<T> writer, Class<?>... cls) {
        for (Class<?> c : cls) {
            data.put(c, writer);
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
            JSONValue.escape(key, out);
            out.append('"');
        }
        JSONStyle.DEFAULT.objectEndOfKey(out);
        if (value instanceof String) {
            JSONStyle.DEFAULT.writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out);
        }
        JSONStyle.DEFAULT.objectElmStop(out);
    }
}
