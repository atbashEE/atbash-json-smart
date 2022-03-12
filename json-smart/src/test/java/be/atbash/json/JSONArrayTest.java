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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class JSONArrayTest {

    @Test
    void fromList() {
        List<Object> data = new ArrayList<>();
        data.add("Element 1");
        data.add(123);
        JSONArray array = new JSONArray(data);
        String x = array.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",123]");
    }

    @Test
    void toJSONString() {
        JSONArray array = new JSONArray();
        array.appendElement("Element 1").add(123);
        String x = array.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",123]");
    }

    @Test
    void toJSONString_withList() {
        List<Object> data = new ArrayList<>();
        data.add("Element 1");
        data.add(123);

        String x = JSONArray.toJSONString(data);
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",123]");
    }

    @Test
    void merge_asStatic() {
        JSONArray array = new JSONArray();
        array.appendElement("Element 1").add("Element 2");

        JSONArray merged = JSONArray.merge(array, "Element 3");
        Assertions.assertThat(merged).containsExactly("Element 1", "Element 2", "Element 3");
        String x = merged.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\"]");
        Assertions.assertThat(merged == array).isTrue();

    }

    @Test
    void merge_asStatic_withNull() {
        JSONArray array = new JSONArray();
        array.appendElement("Element 1").add("Element 2");

        JSONArray merged = JSONArray.merge(array, null);
        Assertions.assertThat(merged).containsExactly("Element 1", "Element 2");

    }

    @Test
    void merge_asStatic_withArray() {
        JSONArray array1 = new JSONArray();
        array1.appendElement("Element 1").add("Element 2");

        JSONArray array2 = new JSONArray();
        array2.appendElement("Element 3").add("Element 4");

        JSONArray merged = JSONArray.merge(array1, array2);
        Assertions.assertThat(merged).containsExactly("Element 1", "Element 2", "Element 3", "Element 4");
        String x = merged.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\",\"Element 4\"]");
        Assertions.assertThat(merged == array1).isTrue();
    }

    @Test
    void merge() {
        JSONArray array = new JSONArray();
        array.appendElement("Element 1").add("Element 2");

         array.merge( "Element 3");
        Assertions.assertThat(array).containsExactly("Element 1", "Element 2", "Element 3");
        String x = array.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\"]");

    }

    @Test
    void merge_withNull() {
        JSONArray array = new JSONArray();
        array.appendElement("Element 1").add("Element 2");

         array.merge( null);
        Assertions.assertThat(array).containsExactly("Element 1", "Element 2");
    }

    @Test
    void merge_withArray() {
        JSONArray array1 = new JSONArray();
        array1.appendElement("Element 1").add("Element 2");

        JSONArray array2 = new JSONArray();
        array2.appendElement("Element 3").add("Element 4");

        array1.merge(array2);
        Assertions.assertThat(array1).containsExactly("Element 1", "Element 2", "Element 3", "Element 4");
        String x = array1.toJSONString();
        Assertions.assertThat(x).isEqualTo("[\"Element 1\",\"Element 2\",\"Element 3\",\"Element 4\"]");

    }

}