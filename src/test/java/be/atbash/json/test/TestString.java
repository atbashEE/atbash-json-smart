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
import be.atbash.json.parser.JSONParser;
import be.atbash.json.parser.ParseException;
import org.junit.Test;

import static be.atbash.json.parser.JSONParser.MODE_PERMISSIVE;
import static be.atbash.json.parser.JSONParser.MODE_RFC4627;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class TestString {

    @Test
    public void testS0() {
        MustThrows.testStrictInvalidJson("{\"1\":\"one\"\n\"2\":\"two\"}", ParseException.ERROR_UNEXPECTED_TOKEN);
    }

    @Test
    public void testS1() {
        String text = "My Test";
        String s = "{t:\"" + text + "\"}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(text);
    }

    @Test
    public void testS2() {
        String text = "My Test";
        String s = "{t:'" + text + "'}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(text);
    }

    @Test
    public void testSEscape() {
        String text = "My\r\nTest";
        String text2 = "My\\r\\nTest";
        String s = "{t:'" + text2 + "'}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(text);
    }

    @Test
    public void testBadString()  {
        String s = "{\"t\":\"Before\u000CAfter\"}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo("Before\u000CAfter");
        try {
            // FIXME
            new JSONParser(MODE_RFC4627).parse(s);
            fail("Parsing should fail in RFC4627 mode");
        } catch (ParseException e) {
            // expected
        }
    }
}
