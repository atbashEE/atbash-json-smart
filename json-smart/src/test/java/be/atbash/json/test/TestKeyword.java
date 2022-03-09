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
import org.junit.jupiter.api.Test;

import static be.atbash.json.parser.JSONParser.MODE_PERMISSIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestKeyword {

    @Test
    public void testBool() {
        String s = "{t:true}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(true);

        s = "{t:false}";
        o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(false);
    }

    @Test
    public void testNull() {
        String s = "{t:null}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isNull();
    }

    @Test
    public void testNaN() {
        String s = "{t:NaN}";
        JSONObject o = (JSONObject) new JSONParser(MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(Float.NaN);
    }

    @Test
    public void testNaNStrict() {
        String s = "{\"t\":NaN}";
        MustThrows.testStrictInvalidJson(s, ParseException.ERROR_UNEXPECTED_TOKEN);
    }

}
