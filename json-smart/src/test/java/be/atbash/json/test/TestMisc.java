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
package be.atbash.json.test;

import be.atbash.json.JSONArray;
import be.atbash.json.JSONObject;
import be.atbash.json.JSONValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMisc {

    @Test
    public void testIssue23() {
        String s = JSONValue.toJSONString(new int[]{1, 2, 50, 1234, 10000});
        assertThat(s).isEqualTo("[1,2,50,1234,10000]");
    }

    @Test
    public void testEmptyStrict() {
        String s = "{\"key1\":\"v1\", \"key2\":{}, \"key3\":[]}";
        JSONObject o = (JSONObject) JSONValue.parse(s);

        assertThat(o.get("key1")).isEqualTo("v1");
        assertThat((JSONObject) o.get("key2")).isEmpty();
        assertThat((JSONArray) o.get("key3")).isEmpty();
    }

    @Test
    public void testBool() {
        String s = "{\"key1\":\"v1\", \"key2\":{}, \"key3\":[]}";
        JSONObject o = (JSONObject) JSONValue.parse(s);

        assertThat(o.get("key1")).isEqualTo("v1");
        assertThat((JSONObject) o.get("key2")).isEmpty();
        assertThat((JSONArray) o.get("key3")).isEmpty();
    }

    @Test
    public void testInt() {
        String s = "123";
        Object o = JSONValue.parse(s);
        assertThat(o).isEqualTo(123);
    }

    @Test
    public void testFloat() {
        String s = "123.5";
        Object o = JSONValue.parse(s);
        assertThat(o).isEqualTo(123.5);
    }

    @Test
    public void testFloat2() {
        String s = "123.5E1";
        Object o = JSONValue.parse(s);
        assertThat(o).isEqualTo(1235d);
    }

    @Test
    public void testFloat3() {
        String s = "123..5";
        Object o = JSONValue.parse(s);
        assertThat(o).isEqualTo("123..5");
    }

    @Test
    public void testFloat4() {
        String s = "123é.5";
        Object o = JSONValue.parse(s);
        assertThat(o).isEqualTo(123);
    }

}
