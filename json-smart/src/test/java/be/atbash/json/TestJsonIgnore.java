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

import be.atbash.json.testclasses.Bean4;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonIgnore {

    @Test
    public void testIgnore_Writer() {
        Bean4 bean4 = new Bean4("Atbash", 123);
        String data = JSONValue.toJSONString(bean4);

        Assertions.assertThat(data).isEqualTo("{\"intValue\":123}");
    }

    @Test
    public void testIgnore_Encoder() {
        Bean4 data = JSONValue.parse("{\"intValue\":123, \"stringValue\":\"Ignored\"}", Bean4.class);

        Assertions.assertThat(data.getIntValue()).isEqualTo(123);
        Assertions.assertThat(data.getStringValue()).isNull();
    }
}
