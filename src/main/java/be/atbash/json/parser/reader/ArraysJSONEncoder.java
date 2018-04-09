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

        public GenericJSONEncoder(Class<T> type) {
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
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(componentType);
            }
            return subJSONEncoder;
        }

        @Override
        public JSONEncoder<?> startObject(String key) {
            if (subJSONEncoder == null) {
                subJSONEncoder = JSONEncoderFactory.getInstance().getEncoder(componentType);
            }
            return subJSONEncoder;
        }
    }

    private static JSONEncoder<int[]> JSONEncoderPrimInt;
    private static JSONEncoder<Integer[]> JSONEncoderInt;
    private static JSONEncoder<short[]> JSONEncoderPrimShort;
    private static JSONEncoder<Short[]> JSONEncoderShort;
    private static JSONEncoder<byte[]> JSONEncoderPrimByte;
    private static JSONEncoder<Byte[]> JSONEncoderByte;
    private static JSONEncoder<char[]> JSONEncoderPrimChar;
    private static JSONEncoder<Character[]> JSONEncoderChar;
    private static JSONEncoder<long[]> JSONEncoderPrimLong;
    private static JSONEncoder<Long[]> JSONEncoderLong;
    private static JSONEncoder<float[]> JSONEncoderPrimFloat;
    private static JSONEncoder<Float[]> JSONEncoderFloat;
    private static JSONEncoder<double[]> JSONEncoderPrimDouble;
    private static JSONEncoder<Double[]> JSONEncoderDouble;
    private static JSONEncoder<boolean[]> JSONEncoderPrimBool;
    private static JSONEncoder<Boolean[]> JSONEncoderBool;

    static {
        JSONEncoderPrimInt = new ArraysJSONEncoder<int[]>() {
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

        JSONEncoderInt = new ArraysJSONEncoder<Integer[]>() {
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

        JSONEncoderPrimShort = new ArraysJSONEncoder<short[]>() {
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

        JSONEncoderShort = new ArraysJSONEncoder<Short[]>() {
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

        JSONEncoderPrimByte = new ArraysJSONEncoder<byte[]>() {
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

        JSONEncoderByte = new ArraysJSONEncoder<Byte[]>() {
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

        JSONEncoderPrimChar = new ArraysJSONEncoder<char[]>() {
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

        JSONEncoderChar = new ArraysJSONEncoder<Character[]>() {
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

        JSONEncoderPrimLong = new ArraysJSONEncoder<long[]>() {
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

        JSONEncoderLong = new ArraysJSONEncoder<Long[]>() {
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

        JSONEncoderPrimFloat = new ArraysJSONEncoder<float[]>() {
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

        JSONEncoderFloat = new ArraysJSONEncoder<Float[]>() {
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

        JSONEncoderPrimDouble = new ArraysJSONEncoder<double[]>() {
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

        JSONEncoderDouble = new ArraysJSONEncoder<Double[]>() {
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

        JSONEncoderPrimBool = new ArraysJSONEncoder<boolean[]>() {
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

        JSONEncoderBool = new ArraysJSONEncoder<Boolean[]>() {
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

    public static JSONEncoder<int[]> getJSONEncoderPrimInt() {
        return JSONEncoderPrimInt;
    }

    public static JSONEncoder<Integer[]> getJSONEncoderInt() {
        return JSONEncoderInt;
    }

    public static JSONEncoder<short[]> getJSONEncoderPrimShort() {
        return JSONEncoderPrimShort;
    }

    public static JSONEncoder<Short[]> getJSONEncoderShort() {
        return JSONEncoderShort;
    }

    public static JSONEncoder<byte[]> getJSONEncoderPrimByte() {
        return JSONEncoderPrimByte;
    }

    public static JSONEncoder<Byte[]> getJSONEncoderByte() {
        return JSONEncoderByte;
    }

    public static JSONEncoder<char[]> getJSONEncoderPrimChar() {
        return JSONEncoderPrimChar;
    }

    public static JSONEncoder<Character[]> getJSONEncoderChar() {
        return JSONEncoderChar;
    }

    public static JSONEncoder<long[]> getJSONEncoderPrimLong() {
        return JSONEncoderPrimLong;
    }

    public static JSONEncoder<Long[]> getJSONEncoderLong() {
        return JSONEncoderLong;
    }

    public static JSONEncoder<float[]> getJSONEncoderPrimFloat() {
        return JSONEncoderPrimFloat;
    }

    public static JSONEncoder<Float[]> getJSONEncoderFloat() {
        return JSONEncoderFloat;
    }

    public static JSONEncoder<double[]> getJSONEncoderPrimDouble() {
        return JSONEncoderPrimDouble;
    }

    public static JSONEncoder<Double[]> getJSONEncoderDouble() {
        return JSONEncoderDouble;
    }

    public static JSONEncoder<boolean[]> getJSONEncoderPrimBool() {
        return JSONEncoderPrimBool;
    }

    public static JSONEncoder<Boolean[]> getJSONEncoderBool() {
        return JSONEncoderBool;
    }
}
