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
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static be.atbash.json.parser.ParseException.ERROR_UNEXPECTED_TOKEN;
import static be.atbash.json.test.MustThrows.testInvalidJson;
import static org.assertj.core.api.Assertions.assertThat;

public class TestInts {

    @Test
    public void testIntMax() {
        String s = "{t:" + Integer.MAX_VALUE + "}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void testIntMin() {
        String s = "{t:" + Integer.MIN_VALUE + "}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    public void testIntResult() {
        String s = "{\"t\":1}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
        assertThat(o.get("t")).isEqualTo(1);

        o = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(s);
        assertThat(o.get("t")).isEqualTo(1L);

        o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(1);
    }

    @Test
    public void testInt() {
        String s = "{t:90}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(90);
    }

    @Test
    public void testIntNeg() {
        String s = "{t:-90}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(-90);
    }

    @Test
    public void testBigInt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(Integer.MAX_VALUE);
        }
        String bigText = sb.toString();
        BigInteger big = new BigInteger(bigText, 10);
        String s = "{t:" + bigText + "}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(big);
    }

    @Test
    public void testBigDoubleInt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(Integer.MAX_VALUE);
        }
        sb.append('.');
        for (int i = 0; i < 10; i++) {
            sb.append(Integer.MAX_VALUE);
        }

        String bigText = sb.toString();
        BigDecimal big = new BigDecimal(bigText);
        String s = "{\"t\":" + bigText + "}";
        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
        assertThat(o.get("t")).isEqualTo(big);
        o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(big);
    }

    @Test
    public void testJunkTrailingData() {
        String s = "{\"t\":124}$ifsisg045";

        JSONObject o = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(s);
        assertThat(o.get("t")).isEqualTo(124L);

        testInvalidJson(s, JSONParser.MODE_RFC4627, ERROR_UNEXPECTED_TOKEN);
        // o = (JSONObject) new JSONParser(JSONParser.MODE_RFC4627).parse(s);
        // assertEquals(o.get("t"), 124);

        o = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(s);
        assertThat(o.get("t")).isEqualTo(124);
    }
}
