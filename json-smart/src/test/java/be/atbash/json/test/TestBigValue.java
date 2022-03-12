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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class TestBigValue {
    private final String bigStr = "12345678901234567890123456789";

    /**
     * test BigDecimal serialization
     */
    @Test
    public void testBigDecimal() {
        HashMap<String, Object> map = new HashMap<>();
        BigDecimal bigDec = new BigDecimal(bigStr + "." + bigStr);
        map.put("big", bigDec);
        String test = JSONValue.toJSONString(map);
        String result = "{\"big\":" + bigStr + "." + bigStr + "}";
        Assertions.assertThat(test).isEqualTo(result);
        JSONObject obj = (JSONObject) JSONValue.parse(test);
        Assertions.assertThat(obj).containsEntry("big", bigDec);
        Assertions.assertThat(obj.get("big")).isInstanceOf(BigDecimal.class);
    }

    /**
     * test BigInteger serialization
     */
    @Test
    public void testBigInteger() {
        HashMap<String, Object> map = new HashMap<>();
        BigInteger bigInt = new BigInteger(bigStr);
        map.put("big", bigInt);
        String test = JSONValue.toJSONString(map);
        String result = "{\"big\":" + bigStr + "}";
        Assertions.assertThat(test).isEqualTo(result);
        JSONObject obj = (JSONObject) JSONValue.parse(test);
        Assertions.assertThat(obj).containsEntry("big", bigInt);
        Assertions.assertThat(obj.get("big")).isInstanceOf(BigInteger.class);
    }
}
