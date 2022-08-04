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
import be.atbash.json.parser.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFloat {
    static String[] TRUE_NUMBERS = new String[]{"1.0", "123.456", "1.0E1", "123.456E12", "1.0E+1",
            "123.456E+12", "1.0E-1", "123.456E-12", "1.0e1", "123.456e12", "1.0e+1", "123.456e+12", "1.0e-1",
            "123.456e-12"};

    static String[] FALSE_NUMBERS = new String[]{"1.0%", "123.45.6", "1.0E", "++123.456E12", "+-01",
            "1.0E+1.2"};

    static String[] PARSE_FAILURES = new String[]{"-.", "2e+", "[45e-"};

    @Test
    public void testFloat() {
        JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
        for (String s : TRUE_NUMBERS) {
            String json = "{v:" + s + "}";
            Double val = Double.valueOf(s.trim());
            JSONObject obj = (JSONObject) p.parse(json);
            Object value = obj.get("v");
            Assertions.assertThat(value).as("Should be parse as double").isEqualTo(val);
        }
    }

    @Test
    public void testNonFloat() {
        JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
        for (String s : FALSE_NUMBERS) {
            String json = "{v:" + s + "}";
            JSONObject obj = (JSONObject) p.parse(json);
            Assertions.assertThat(obj).as("Should be parse as string").containsEntry("v", s);

            String correct = "{\"v\":\"" + s + "\"}";
            Assertions.assertThat(obj.toJSONString()).as("Should be re serialized as").isEqualTo(correct);
        }
    }

    @Test
    public void testParseException() {
        JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
        for (String s : PARSE_FAILURES) {
            String json = "{v:" + s + "}";
            Assertions.assertThatThrownBy(
                    () -> p.parse(json)
            ).isInstanceOf(ParseException.class);

        }
    }

    /**
     * Error reported in issue 44
     */
    @Test
    public void testUUID() {
        String UUID = "58860611416142319131902418361e88";
        JSONObject obj = new JSONObject();
        obj.put("uuid", UUID);
        String compressed = obj.toJSONString();
        Assertions.assertThat(compressed).contains("\"uuid\":\"");
    }
}
