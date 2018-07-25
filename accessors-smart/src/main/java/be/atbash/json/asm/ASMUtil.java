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
package be.atbash.json.asm;

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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * ASM Utils used to simplify class generation
 *
 * @author uriel Chemouni
 */
public final class ASMUtil {

    private ASMUtil() {
    }

    /**
     * Extract all Accessor for the field of the given class.
     *
     * @param type
     * @return all Accessor available
     */
    static Accessor[] getAccessors(Class<?> type, FieldFilter filter) {
        Class<?> nextClass = type;
        HashMap<String, Accessor> map = new HashMap<>();
        if (filter == null) {
            filter = BasicFieldFilter.SINGLETON;
        }
        while (nextClass != Object.class) {
            Field[] declaredFields = nextClass.getDeclaredFields();

            for (Field field : declaredFields) {
                String fn = field.getName();
                if (map.containsKey(fn)) {
                    continue;
                }
                Accessor acc = new Accessor(nextClass, field, filter);
                if (!acc.isUsable()) {
                    continue;
                }
                map.put(fn, acc);
            }
            nextClass = nextClass.getSuperclass();
        }
        return map.values().toArray(new Accessor[map.size()]);
    }

    /**
     * Append the call of proper autoboxing method for the given primitif type.
     */
    static void autoBoxing(MethodVisitor mv, Type fieldType) {
        switch (fieldType.getSort()) {
            case Type.BOOLEAN:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                break;
            case Type.BYTE:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                break;
            case Type.CHAR:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                break;
            case Type.SHORT:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                break;
            case Type.INT:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                break;
            case Type.FLOAT:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                break;
            case Type.LONG:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                break;
            case Type.DOUBLE:
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                break;
        }
    }

    /**
     * return a array of new Label (used for switch/case generation)
     *
     * @param cnt number of label to return
     */
    static Label[] newLabels(int cnt) {
        Label[] r = new Label[cnt];
        for (int i = 0; i < cnt; i++) {
            r[i] = new Label();
        }
        return r;
    }

    static String getSetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 's';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c += 'A' - 'a';
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    static String getGetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 'g';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c += 'A' - 'a';
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    static String getIsName(String key) {
        int len = key.length();
        char[] b = new char[len + 2];
        b[0] = 'i';
        b[1] = 's';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c += 'A' - 'a';
        }
        b[2] = c;
        for (int i = 1; i < len; i++) {
            b[i + 2] = key.charAt(i);
        }
        return new String(b);
    }

}
