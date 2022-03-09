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
package be.atbash.json.testMapping;

import be.atbash.json.JSONValue;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static be.atbash.json.testMapping.TestMapBeans.ColorEnum.red;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMapBeans {

    @Test
    public void testObjInts() {
        String s = "{\"vint\":[1,2,3]}";
        T1 r = JSONValue.parse(s, T1.class);
        assertThat(r.vint[2]).isEqualTo(3);
    }

    @Test
    public void testObjIntKey() {
        String s = "{\"data\":{\"1\":\"toto\"}}";
        T2 r = JSONValue.parse(s, T2.class);
        assertThat(r.data.get(1)).isEqualTo("toto");
    }

    @Test
    public void testObjEnumKey() {
        String s = "{\"data\":{\"red\":10}}";
        T3 r = JSONValue.parse(s, T3.class);
        assertThat(r.data.get(red)).isEqualTo((Integer) 10);
    }

    @Test
    public void testObjBool1() {
        String s = "{\"data\":true}";
        T4 r = JSONValue.parse(s, T4.class);
        assertThat(r.data).isEqualTo(true);
    }

    @Test
    public void testUnknownField() {
        String s = "{\"data\":\"Atbash\", \"notExisting\":\"blablabla\"}";
        T6 r = JSONValue.parse(s, T6.class);
        assertThat(r.data).isEqualTo("Atbash");
    }

    @Test
    public void testObjBool2() {
        String s = "{\"data\":true}";
        T5 r = JSONValue.parse(s, T5.class);
        assertThat(r.data).isEqualTo(true);
    }

    /**
     * class containing primitive array;
     */
    public static class T1 {
        private int[] vint;

        public int[] getVint() {
            return vint;
        }

        public void setVint(int[] vint) {
            this.vint = vint;
        }
    }

    /**
     * class containing Map interface;
     */
    public static class T2 {
        private Map<Integer, String> data;

        public Map<Integer, String> getData() {
            return data;
        }

        public void setData(Map<Integer, String> data) {
            this.data = data;
        }
    }

    public enum ColorEnum {
        bleu, green, red, yellow
    }

    public static class T3 {
        private Map<ColorEnum, Integer> data;

        public Map<ColorEnum, Integer> getData() {
            return data;
        }

        public void setData(Map<ColorEnum, Integer> data) {
            this.data = data;
        }
    }

    public static class T4 {
        private boolean data;

        public boolean getData() {
            return data;
        }

        public void setData(boolean data) {
            this.data = data;
        }
    }

    public static class T5 {
        private boolean data;

        public boolean isData() {
            return data;
        }

        public void setData(boolean data) {
            this.data = data;
        }
    }

    public static class T6 {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
