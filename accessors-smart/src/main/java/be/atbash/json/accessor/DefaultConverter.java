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
package be.atbash.json.accessor;

import be.atbash.json.accessor.ex.ConvertException;

// Method names are used by convention, so don't change them!
public class DefaultConverter {

    private DefaultConverter() {
    }

    public static int convertToInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
    }

    public static Integer convertToIntObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Integer.class) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Integer");
    }

    public static short convertToShort(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        if (obj instanceof String) {
            return Short.parseShort((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to short");
    }

    public static Short convertToShortObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Short.class) {
            return (Short) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Short");
    }

    public static long convertToLong(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            return Long.parseLong((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to long");
    }

    public static Long convertToLongObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Long.class) {
            return (Long) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        throw new ConvertException("Primitive: Can not convert value '" + obj + "' As " + obj.getClass().getName() + " to Long");
    }

    public static byte convertToByte(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof String) {
            return Byte.parseByte((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to byte");
    }

    public static Byte convertToByteObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Byte.class) {
            return (Byte) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Byte");
    }

    public static float convertToFloat(Object obj) {
        if (obj == null) {
            return 0f;
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof String) {
            return Float.parseFloat((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
    }

    public static Float convertToFloatObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Float.class) {
            return (Float) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
    }

    public static double convertToDouble(Object obj) {
        if (obj == null) {
            return 0.0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
    }

    public static Double convertToDoubleObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Double.class) {
            return (Double) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
    }

    public static char convertToChar(Object obj) {
        if (obj == null) {
            return ' ';
        }
        if (obj instanceof String) {
            if (((String) obj).length() > 0) {
                return ((String) obj).charAt(0);
            } else {
                return ' ';
            }
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to char");
    }

    public static Character convertToCharObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Character.class) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            if (((String) obj).length() > 0) {
                return ((String) obj).charAt(0);
            } else {
                return ' ';
            }
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Character");
    }

    public static boolean convertToBool(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == Boolean.class) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        }
        if (obj instanceof Number) {
            return !obj.toString().equals("0");
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to boolean");
    }

    public static Boolean convertToBoolObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> c = obj.getClass();
        if (c == Boolean.class) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        }
        throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Boolean");
    }
}
