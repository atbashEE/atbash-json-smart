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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArraysJSONEncoder<T> extends JSONEncoder<T> {
    public ArraysJSONEncoder(JSONReader base) {
        super(base);
    }

    @Override
    public Object createArray() {
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addValue(Object current, Object value) {
        ((List<Object>) current).add(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(Object current) {
        return (T) current;
    }

    public static class GenericJSONEncoder<T> extends ArraysJSONEncoder<T> {
        final Class<?> componentType;
        JSONEncoder<?> subJSONEncoder;

        public GenericJSONEncoder(JSONReader base, Class<T> type) {
            super(base);
            this.componentType = type.getComponentType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public T convert(Object current) {
            int p = 0;

            Object[] r = (Object[]) Array.newInstance(componentType, ((List<?>) current).size());
            for (Object e : ((List<?>) current)) {
                r[p++] = e;
            }
            return (T) r;
        }

        @Override
        public JSONEncoder<?> startArray(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = getJSONReader().getEncoder(componentType);
            }
            return subJSONEncoder;
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = getJSONReader().getEncoder(componentType);
            }
            return subJSONEncoder;
        }
    }

    public static JSONEncoder<int[]> JSONEncoderPrimInt = new ArraysJSONEncoder<int[]>(null) {
        @Override
        public int[] convert(Object current) {
            int p = 0;
            int[] r = new int[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).intValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Integer[]> JSONEncoderInt = new ArraysJSONEncoder<Integer[]>(null) {
        @Override
        public Integer[] convert(Object current) {
            int p = 0;
            Integer[] r = new Integer[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Integer) {
                    r[p] = (Integer) e;
                } else {
                    r[p] = ((Number) e).intValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<short[]> JSONEncoderPrimShort = new ArraysJSONEncoder<short[]>(null) {
        @Override
        public short[] convert(Object current) {
            int p = 0;
            short[] r = new short[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).shortValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Short[]> JSONEncoderShort = new ArraysJSONEncoder<Short[]>(null) {
        @Override
        public Short[] convert(Object current) {
            int p = 0;
            Short[] r = new Short[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Short) {
                    r[p] = (Short) e;
                } else {
                    r[p] = ((Number) e).shortValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<byte[]> JSONEncoderPrimByte = new ArraysJSONEncoder<byte[]>(null) {
        @Override
        public byte[] convert(Object current) {
            int p = 0;
            byte[] r = new byte[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).byteValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Byte[]> JSONEncoderByte = new ArraysJSONEncoder<Byte[]>(null) {
        @Override
        public Byte[] convert(Object current) {
            int p = 0;
            Byte[] r = new Byte[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Byte) {
                    r[p] = (Byte) e;
                } else {
                    r[p] = ((Number) e).byteValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<char[]> JSONEncoderPrimChar = new ArraysJSONEncoder<char[]>(null) {
        @Override
        public char[] convert(Object current) {
            int p = 0;
            char[] r = new char[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = e.toString().charAt(0);
            }
            return r;
        }
    };

    public static JSONEncoder<Character[]> JSONEncoderChar = new ArraysJSONEncoder<Character[]>(null) {
        @Override
        public Character[] convert(Object current) {
            int p = 0;
            Character[] r = new Character[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                r[p] = e.toString().charAt(0);
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<long[]> JSONEncoderPrimLong = new ArraysJSONEncoder<long[]>(null) {
        @Override
        public long[] convert(Object current) {
            int p = 0;
            long[] r = new long[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).intValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Long[]> JSONEncoderLong = new ArraysJSONEncoder<Long[]>(null) {
        @Override
        public Long[] convert(Object current) {
            int p = 0;
            Long[] r = new Long[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Float) {
                    r[p] = ((Long) e);  // FIXME Check original Code
                } else {
                    r[p] = ((Number) e).longValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<float[]> JSONEncoderPrimFloat = new ArraysJSONEncoder<float[]>(null) {
        @Override
        public float[] convert(Object current) {
            int p = 0;
            float[] r = new float[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).floatValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Float[]> JSONEncoderFloat = new ArraysJSONEncoder<Float[]>(null) {
        @Override
        public Float[] convert(Object current) {
            int p = 0;
            Float[] r = new Float[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Float) {
                    r[p] = ((Float) e);
                } else {
                    r[p] = ((Number) e).floatValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<double[]> JSONEncoderPrimDouble = new ArraysJSONEncoder<double[]>(null) {
        @Override
        public double[] convert(Object current) {
            int p = 0;
            double[] r = new double[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = ((Number) e).doubleValue();
            }
            return r;
        }
    };

    public static JSONEncoder<Double[]> JSONEncoderDouble = new ArraysJSONEncoder<Double[]>(null) {
        @Override
        public Double[] convert(Object current) {
            int p = 0;
            Double[] r = new Double[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Double) {
                    r[p] = ((Double) e);
                } else {
                    r[p] = ((Number) e).doubleValue();
                }
                p++;
            }
            return r;
        }
    };

    public static JSONEncoder<boolean[]> JSONEncoderPrimBool = new ArraysJSONEncoder<boolean[]>(null) {
        @Override
        public boolean[] convert(Object current) {
            int p = 0;
            boolean[] r = new boolean[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                r[p++] = (Boolean) e;
            }
            return r;
        }
    };

    public static JSONEncoder<Boolean[]> JSONEncoderBool = new ArraysJSONEncoder<Boolean[]>(null) {
        @Override
        public Boolean[] convert(Object current) {
            int p = 0;
            Boolean[] r = new Boolean[((List<?>) current).size()];
            for (Object e : ((List<?>) current)) {
                if (e == null) {
                    continue;
                }
                if (e instanceof Boolean) {
                    r[p] = (Boolean) e;
                } else if (e instanceof Number) {
                    r[p] = ((Number) e).intValue() != 0;
                } else {
                    throw new RuntimeException("can not convert " + e + " toBoolean");
                }
                p++;
            }
            return r;
        }
    };
}
