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
package be.atbash.json.test;

import be.atbash.json.JSONObject;
import be.atbash.json.JSONValue;
import be.atbash.json.TypeReference;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class TestList {

    @Test
    public void testListEncoder_String() {
        List<String> data = new ArrayList<>();
        data.add("value1");
        data.add("value2");
        data.add("value3");
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("[\"value1\",\"value2\",\"value3\"]");
    }

    @Test
    public void testListParse_String() {
        ArrayList<String> result = JSONValue.parse("[\"value1\",\"value2\",\"value3\"]", ArrayList.class);
        assertThat(result).containsSequence("value1", "value2", "value3");
    }

    @Test
    public void testListParse_String_OtherImpl() {
        LinkedList<String> result = JSONValue.parse("[\"value1\",\"value2\",\"value3\"]", LinkedList.class);
        assertThat(result).containsSequence("value1", "value2", "value3");
    }

    @Test
    public void testListEncoder_Bean() {
        List<Bean1> data = new ArrayList<>();
        data.add(new Bean1("value1", 123));
        data.add(new Bean1("value2", 0));
        data.add(new Bean1("value3", -321));
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]");
    }

    @Test
    public void testListParse_Bean() {
        List<JSONObject> result = JSONValue.parse("[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]", ArrayList.class);
        assertThat(result.get(0)).isInstanceOf(JSONObject.class);
    }

    @Test
    public void testListParse_BeanParameterTypeReferenced() {
        List<Bean1> result = (List<Bean1>) JSONValue.parse("[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]", new TypeReference<List<Bean1>>() {
        });
        assertThat(result.get(0).getStringValue()).isEqualTo("value1");
    }

    @Test
    public void testListEncoder_BeanWithList() {
        List<Bean1> list = new ArrayList<>();
        list.add(new Bean1("value1", 123));
        list.add(new Bean1("value2", 0));
        list.add(new Bean1("value3", -321));

        Bean2 data = new Bean2();
        data.setBean1List(list);
        String result = JSONValue.toJSONString(data);
        assertThat(result).isEqualTo("{\"bean1List\":[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]}");
    }

    @Test
    public void testListParse_BeanWithList() {
        Bean2 bean2 = JSONValue.parse("{\"bean1List\":[{\"stringValue\":\"value1\",\"intValue\":123},{\"stringValue\":\"value2\",\"intValue\":0},{\"stringValue\":\"value3\",\"intValue\":-321}]}", Bean2.class);
        assertThat(bean2.getBean1List()).hasSize(3);
        assertThat(bean2.getBean1List().get(0)).isInstanceOf(Bean1.class);
        assertThat(bean2.getBean1List().get(0).getStringValue()).isEqualTo("value1");
    }

}
