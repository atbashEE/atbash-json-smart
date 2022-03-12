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

import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class JSONObjectTest {

    @Test
    void fromMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("key 1", "Element 1");
        data.put("key 2", 123);
        JSONObject obj = new JSONObject(data);
        String x = obj.toJSONString();
        Assertions.assertThat(x).isEqualTo("{\"key 2\":123,\"key 1\":\"Element 1\"}");
    }

    @Test
    void getAsString() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        String x = obj.getAsString("key 1");
        Assertions.assertThat(x).isEqualTo("Element 1");
    }

    @Test
    void getAsNumber() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        Number x = obj.getAsNumber("key 2");
        Assertions.assertThat(x).isEqualTo(123);
    }

    @Test
    void getAsNumber_parsed() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", "123");

        Number x = obj.getAsNumber("key 2");
        Assertions.assertThat(x).isEqualTo(123L);
        Assertions.assertThat(x).isInstanceOf(Long.class);
    }
    @Test
    void getAsNumber_numberType() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", BigDecimal.valueOf(321));

        Number x = obj.getAsNumber("key 2");
        Assertions.assertThat(x).isEqualTo(BigDecimal.valueOf(321));
        Assertions.assertThat(x).isInstanceOf(BigDecimal.class);
    }

    @Test
    void toJSONString() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);
        String x = obj.toJSONString();
        Assertions.assertThat(x).isEqualTo("{\"key 2\":123,\"key 1\":\"Element 1\"}");
    }

    @Test
    void writeJSONString() throws IOException {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);
        StringBuilder sb = new StringBuilder();
        obj.writeJSONString(sb);
        Assertions.assertThat(sb.toString()).isEqualTo("{\"key 2\":123,\"key 1\":\"Element 1\"}");
    }

    @Test
    void toJSONString_withMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("key 1", "Element 1");
        data.put("key 2", 123);

        String x = JSONObject.toJSONString(data);
        Assertions.assertThat(x).isEqualTo("{\"key 2\":123,\"key 1\":\"Element 1\"}");
    }

    @Test
    void toJSONString_withMap_null() {
        String x = JSONObject.toJSONString(null);
        Assertions.assertThat(x).isEqualTo("null");
    }

    @Test
    void merge_asStatic() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        Assertions.assertThatThrownBy(() ->
                JSONObject.merge(obj, "Element 3")
        ).isInstanceOf(JSONMergeException.class);

    }

    @Test
    void merge_asStatic_withNull() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        JSONObject merged = JSONObject.merge(obj, null);
        Assertions.assertThat(merged == obj).isTrue();
        Assertions.assertThat(merged).hasSize(2);
    }

    @Test
    void merge_asStatic_withJsonObject() {
        JSONObject obj1 = new JSONObject();
        obj1.appendField("key 1", "Element 1").appendField("key 2", 123);

        JSONObject obj2 = new JSONObject();
        obj2.appendField("key 3", "Element 3");

        JSONObject merged = JSONObject.merge(obj1, obj2);
        Assertions.assertThat(merged).contains(MapEntry.entry("key 1", "Element 1"), MapEntry.entry("key 2", 123), MapEntry.entry("key 3", "Element 3"));
        String x = merged.toJSONString();
        Assertions.assertThat(x).isEqualTo("{\"key 3\":\"Element 3\",\"key 2\":123,\"key 1\":\"Element 1\"}");
        Assertions.assertThat(merged == obj1).isTrue();
    }

    @Test
    void merge() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        Assertions.assertThatThrownBy(() ->
                obj.merge("Element 3")
        ).isInstanceOf(JSONMergeException.class);


        /*
        Assertions.assertThat(array).containsExactly("Element 1", "Element 2", "Element 3");
        String x = array.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\"]");


         */
    }

    @Test
    void merge_withNull() {
        JSONObject obj = new JSONObject();
        obj.appendField("key 1", "Element 1").appendField("key 2", 123);

        obj.merge(null);
        //Assertions.assertThat(obj).containsExactly("Element 1", "Element 2");
    }

    @Test
    void merge_withArray() {
        /*
        JSONArray array1 = new JSONObject();
        array1.appendElement("Element 1").add("Element 2");

        JSONArray array2 = new JSONObject();
        array2.appendElement("Element 3").add("Element 4");

        array1.merge(array2);
        Assertions.assertThat(array1).containsExactly("Element 1", "Element 2", "Element 3", "Element 4");
        String x = array1.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\",\"Element 4\"]");


         */
    }


}