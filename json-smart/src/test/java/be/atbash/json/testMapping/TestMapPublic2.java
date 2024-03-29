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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

public class TestMapPublic2 {
    String s = "{\"data\":{\"a\":\"b\"}}";

    @Test
    public void testMapPublicInterface() {
        T5 r = JSONValue.parse(s, T5.class);
        Assertions.assertThat(r.data).hasSize(1);
    }

    @Test
    public void testMapPublicMapClass() {
        T6 r = JSONValue.parse(s, T6.class);
        Assertions.assertThat(r.data).hasSize(1);
    }

    public static class T5 {
        public Map<String, String> data;
    }

    public static class T6 {
        public TreeMap<String, String> data;
    }

}
