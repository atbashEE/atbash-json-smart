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

import be.atbash.json.testclasses.Bean5;
import be.atbash.json.testclasses.NestedBean5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBoolean {

    @Test
    public void writeWithBoolean() {
        Bean5 bean5 = new Bean5("JUnit", 123, true);
        String content = JSONValue.toJSONString(bean5);
        Assertions.assertThat(content).isEqualTo("{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}");
    }

    @Test
    public void parseWithBoolean() {
        String content = "{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}";
        Bean5 bean5 = JSONValue.parse(content, Bean5.class);
        Assertions.assertThat(bean5.isFlag()).isTrue();
    }

    @Test
    public void writeNestedWithBoolean() {
        Bean5 bean5 = new Bean5("JUnit", 123, true);
        NestedBean5 nested = new NestedBean5();
        nested.setBean(bean5);
        String content = JSONValue.toJSONString(nested);
        Assertions.assertThat(content).isEqualTo("{\"bean\":{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}}");
    }

    @Test
    public void parseWithNestedBoolean() {
        String content = "{\"bean\":{\"stringValue\":\"JUnit\",\"intValue\":123,\"flag\":true}}";
        NestedBean5 nested = JSONValue.parse(content, NestedBean5.class);
        Assertions.assertThat(nested.getBean().isFlag()).isTrue();
    }
}
