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

import be.atbash.json.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ParseToJSONObjectTest {

    @Test
    public void testSimple() {
        String content = "{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}";
        JSONObject jsonObject = JSONValue.parse(content, JSONObject.class);

        Assertions.assertThat(jsonObject).containsOnlyKeys("stringValue", "intValue", "flag");
        Assertions.assertThat(jsonObject).containsEntry("stringValue", "JUnit");
        Assertions.assertThat(jsonObject).containsEntry("intValue", 123);
        Assertions.assertThat(jsonObject).containsEntry("flag", Boolean.TRUE);
    }

    @Test
    public void testSimpleWithParser() {
        String content = "{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}";
        JSONObject jsonObject = new JSONParser().parse(content, JSONObject.class);

        Assertions.assertThat(jsonObject).containsOnlyKeys("stringValue", "intValue", "flag");
        Assertions.assertThat(jsonObject).containsEntry("stringValue", "JUnit");
        Assertions.assertThat(jsonObject).containsEntry("intValue", 123);
        Assertions.assertThat(jsonObject).containsEntry("flag", Boolean.TRUE);
    }

    @Test
    public void testNested() {
        String content = "{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true,\"nested\":{\"key 2\":321,\"key 1\":\"Element 1\"}}";
        JSONObject jsonObject = JSONValue.parse(content, JSONObject.class);

        Assertions.assertThat(jsonObject).containsOnlyKeys("stringValue", "intValue", "flag", "nested");
        Assertions.assertThat(jsonObject).containsEntry("stringValue", "JUnit");
        Assertions.assertThat(jsonObject).containsEntry("intValue", 123);
        Assertions.assertThat(jsonObject).containsEntry("flag", Boolean.TRUE);
        Assertions.assertThat(jsonObject.get("nested")).isInstanceOf(JSONObject.class);

        JSONObject nested = (JSONObject) jsonObject.get("nested");
        Assertions.assertThat(nested).containsOnlyKeys("key 1", "key 2");
        Assertions.assertThat(nested).containsEntry("key 1", "Element 1");
        Assertions.assertThat(nested).containsEntry("key 2", 321);
    }

    @Test
    public void testNestedWithParser() {
        String content = "{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true,\"nested\":{\"key 2\":321,\"key 1\":\"Element 1\"}}";
        JSONObject jsonObject = new JSONParser().parse(content, JSONObject.class);

        Assertions.assertThat(jsonObject).containsOnlyKeys("stringValue", "intValue", "flag", "nested");
        Assertions.assertThat(jsonObject).containsEntry("stringValue", "JUnit");
        Assertions.assertThat(jsonObject).containsEntry("intValue", 123);
        Assertions.assertThat(jsonObject).containsEntry("flag", Boolean.TRUE);
        Assertions.assertThat(jsonObject.get("nested")).isInstanceOf(JSONObject.class);

        JSONObject nested = (JSONObject) jsonObject.get("nested");
        Assertions.assertThat(nested).containsOnlyKeys("key 1", "key 2");
        Assertions.assertThat(nested).containsEntry("key 1", "Element 1");
        Assertions.assertThat(nested).containsEntry("key 2", 321);
    }
}
