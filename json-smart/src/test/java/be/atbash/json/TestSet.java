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

import be.atbash.json.testclasses.Bean1;
import be.atbash.json.testclasses.Bean6;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class TestSet {

    @Test
    public void testSetEncoder_String() {
        Set<String> data = new HashSet<>();
        data.add("value1");
        data.add("value2");
        data.add("value3");
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("[\"value2\",\"value1\",\"value3\"]");
    }

    @Test
    public void testSetParse_String() {
        Set<String> result = JSONValue.parse("[\"value1\",\"value2\",\"value3\"]", Set.class);
        assertThat(result).hasSize(3);
        assertThat(result).contains("value1", "value2", "value3");
        assertThat(result).isInstanceOf(HashSet.class);
    }

    @Test
    public void testSetParse_String_duplicates() {
        Set<String> result = JSONValue.parse("[\"value1\",\"value2\",\"value1\",\"value3\"]", Set.class);
        assertThat(result).hasSize(3);
        assertThat(result).contains("value1", "value2", "value3");
        assertThat(result).isInstanceOf(HashSet.class);
    }

    @Test
    public void testSetParse_String_order() {
        Set<String> result = JSONValue.parse("[\"value1\",\"value2\",\"value3\"]", LinkedHashSet.class);
        assertThat(result).hasSize(3);
        assertThat(result).containsSequence("value1", "value2", "value3");
        assertThat(result).isNotExactlyInstanceOf(HashSet.class);
    }

    @Test
    public void testSetEncoder_Bean() {
        Set<Bean1> data = new HashSet<>();
        data.add(new Bean1("value1", 123));
        data.add(new Bean1("value2", 0));
        data.add(new Bean1("value3", -321));
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("[{\"stringValue\":\"value3\",\"intValue\":-321},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value1\",\"intValue\":123}]");
    }

    @Test
    public void testSetParse_Bean() {
        Set<JSONObject> result = JSONValue.parse("[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]", HashSet.class);
        assertThat(result.iterator().next()).isInstanceOf(JSONObject.class);
    }

    @Test
    public void testSetParse_BeanParameterTypeReferenced() {
        Set<Bean1> result = (Set<Bean1>) JSONValue.parse("[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]", new TypeReference<Set<Bean1>>() {
        });
        assertThat(result.iterator().next().getStringValue()).isEqualTo("value3");
    }

    @Test
    public void testSetEncoder_BeanWithSet() {
        Set<Bean1> list = new HashSet<>();
        list.add(new Bean1("value1", 123));
        list.add(new Bean1("value2", 0));
        list.add(new Bean1("value3", -321));

        Bean6 data = new Bean6();
        data.setBean1Set(list);
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("{\"bean1Set\":[{\"stringValue\":\"value3\",\"intValue\":-321},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value1\",\"intValue\":123}]}");
    }

    @Test
    public void testSetParse_BeanWithSet() {
        Bean6 bean6 = JSONValue.parse("{\"bean1Set\":[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]}", Bean6.class);
        assertThat(bean6.getBean1Set()).hasSize(3);
        Bean1 bean1 = bean6.getBean1Set().iterator().next();
        assertThat(bean1).isInstanceOf(Bean1.class);
        assertThat(bean1.getStringValue()).isEqualTo("value3");
    }

}
