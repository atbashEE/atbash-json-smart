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

import be.atbash.json.JSONObject;
import be.atbash.json.JSONValue;
import be.atbash.json.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCornerCases {

    @Test
    public void testEmpty() {
        String data = (String) new JSONParser().parse("");
        Assertions.assertThat(data).isEmpty();
    }

    @Test
    public void testSpaces() {
        String data = (String) new JSONParser().parse("  ");
        Assertions.assertThat(data).isEmpty();
    }

    @Test
    public void testCurlyBraces() {
        JSONObject data = (JSONObject) new JSONParser().parse("{}");
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.keySet()).isEmpty();
    }

    @Test
    public void testNull() {
        Assertions.assertThatThrownBy(() -> new JSONParser().parse(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testNullText() {
        Object data = new JSONParser().parse("null");
        Assertions.assertThat(data).isNull();
    }

    @Test
    public void toJSONString_Null() {
        String data = JSONValue.toJSONString(null);
        Assertions.assertThat(data).isEqualTo("null");
    }
}
