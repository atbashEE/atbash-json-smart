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
import be.atbash.json.parser.JSONParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFloatStrict {

    @Test
    public void testFloat() {
        for (String s : TestFloat.TRUE_NUMBERS) {
            String json = "{\"v\":" + s + "}";
            Double val = Double.valueOf(s.trim());
            JSONObject obj = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(json);
            Object value = obj.get("v");
            Assertions.assertThat(value).as("Should be parse as double").isEqualTo(val);
        }
    }

    @Test
    public void testNonFloat() {
        for (String s : TestFloat.FALSE_NUMBERS) {
            String json = "{\"v\":" + s + "}";
            MustThrows.testStrictInvalidJson(json, -1);
        }

        for (String s : TestFloat.PARSE_FAILURES) {
            String json = "{\"v\":" + s + "}";
            MustThrows.testStrictInvalidJson(json, -1);
        }
    }
}
