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
package be.atbash.json;

import be.atbash.json.JSONObject;
import be.atbash.json.JSONValue;
import be.atbash.json.TypeReference;
import be.atbash.json.testclasses.Bean1;
import be.atbash.json.testclasses.Bean3;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class TestMap {

    @Test
    public void testMapEncoder_Simple() {
        Map<String, Integer> data = new HashMap<>();
        data.put("value1", 123);
        data.put("value2", 0);
        data.put("value3", -321);
        String result = JSONValue.toJSONString(data);

        // The exact order of the entries within a Map is not determined. Therefor we only test fragments
        assertThat(result).contains("\"value3\":-321");
        assertThat(result).contains("\"value1\":123");
        assertThat(result).contains("\"value2\":0");
    }

    @Test
    public void testMapParser_Simple() {
        Map<String, Integer> data = JSONValue.parse("{\"value3\":-321,\"value1\":123,\"value2\":0}", HashMap.class);
        assertThat(data).hasSize(3);
        assertThat(data).containsKeys("value1", "value2", "value3");
        assertThat(data).containsEntry("value1", 123);
        assertThat(data).containsEntry("value2", 0);
        assertThat(data).containsEntry("value3", -321);
    }

    @Test
    public void testMapParser_Simple_OtherImpl() {
        Map<String, Integer> data = JSONValue.parse("{\"value3\":-321,\"value1\":123,\"value2\":0}", LinkedHashMap.class);
        assertThat(data).hasSize(3);
        assertThat(data).containsKeys("value1", "value2", "value3");
        assertThat(data).containsEntry("value1", 123);
        assertThat(data).containsEntry("value2", 0);
        assertThat(data).containsEntry("value3", -321);
    }

    @Test
    public void testMapEncoder_Bean() {
        Map<String, Bean1> data = new HashMap<>();
        data.put("bean1", new Bean1("value1", 123));
        data.put("bean2", new Bean1("value2", 0));
        data.put("bean3", new Bean1("value3", -321));

        String result = JSONValue.toJSONString(data);

        // The exact order of the entries within a Map is not determined. Therefor we only test fragments
        assertThat(result).contains("\"bean1\":{\"stringValue\":\"value1\",\"intValue\":123}");
        assertThat(result).contains("\"bean3\":{\"stringValue\":\"value3\",\"intValue\":-321}");
        assertThat(result).contains("\"bean2\":{\"stringValue\":\"value2\",\"intValue\":0}");
    }

    @Test
    public void testMapParser_Bean() {

        Map<String, JSONObject> data = (Map<String, JSONObject>) JSONValue.parse("{\"bean1\":{\"stringValue\":\"value1\",\"intValue\":123},\"bean3\":{\"stringValue\":\"value3\",\"intValue\":-321},\"bean2\":{\"stringValue\":\"value2\",\"intValue\":0}}", HashMap.class);

        assertThat(data).hasSize(3);
        assertThat(data).containsKeys("bean1", "bean2", "bean3");
        assertThat(data).containsEntry("bean1", new JSONObject().appendField("stringValue", "value1").appendField("intValue", 123));
        assertThat(data).containsEntry("bean2", new JSONObject().appendField("stringValue", "value2").appendField("intValue", 0));
        assertThat(data).containsEntry("bean3", new JSONObject().appendField("stringValue", "value3").appendField("intValue", -321));
    }

    @Test
    public void testMapParser_BeanParameterTypeReferenced() {

        Map<String, Bean1> data = (Map<String, Bean1>) JSONValue.parse("{\"bean1\":{\"stringValue\":\"value1\",\"intValue\":123},\"bean3\":{\"stringValue\":\"value3\",\"intValue\":-321},\"bean2\":{\"stringValue\":\"value2\",\"intValue\":0}}", new TypeReference<Map<String, Bean1>>() {
        });

        assertThat(data).hasSize(3);
        assertThat(data).containsKeys("bean1", "bean2", "bean3");
        assertThat(data).containsEntry("bean1", new Bean1("value1", 123));
        assertThat(data).containsEntry("bean2", new Bean1("value2", 0));
        assertThat(data).containsEntry("bean3", new Bean1("value3", -321));

    }

    @Test
    public void testMapEncoder_BeanWithMap() {

        Bean3 data = new Bean3();
        Map<String, Bean1> map = new HashMap<>();
        map.put("bean1", new Bean1("value1", 123));
        map.put("bean2", new Bean1("value2", 0));
        map.put("bean3", new Bean1("value3", -321));
        data.setBean1Map(map);

        String result = JSONValue.toJSONString(data);

        // The exact order of the entries within a Map is not determined. Therefor we only test fragments
        assertThat(result).startsWith("{\"bean1Map\":");
        assertThat(result).contains("{\"bean1\":{\"stringValue\":\"value1\",\"intValue\":123}");
        assertThat(result).contains("\"bean3\":{\"stringValue\":\"value3\",\"intValue\":-321}");
        assertThat(result).contains("\"bean2\":{\"stringValue\":\"value2\",\"intValue\":0}");
    }

    @Test
    public void testMapParser_BeanWithMap() {

        Bean3 data = JSONValue.parse("{\"bean1Map\":{\"bean1\":{\"stringValue\":\"value1\",\"intValue\":123},\"bean3\":{\"stringValue\":\"value3\",\"intValue\":-321},\"bean2\":{\"stringValue\":\"value2\",\"intValue\":0}}}", Bean3.class);

        assertThat(data.getBean1Map()).hasSize(3);
        assertThat(data.getBean1Map()).containsKeys("bean1", "bean2", "bean3");
        assertThat(data.getBean1Map()).containsEntry("bean1", new Bean1("value1", 123));
        assertThat(data.getBean1Map()).containsEntry("bean2", new Bean1("value2", 0));
        assertThat(data.getBean1Map()).containsEntry("bean3", new Bean1("value3", -321));

    }

}
