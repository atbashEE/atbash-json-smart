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

import be.atbash.json.testclasses.BeanFieldMapping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFieldMapper {

    @Test
    public void testWriterWithFieldMapping() {
        BeanFieldMapping value = new BeanFieldMapping("value1", 123);
        String json = JSONValue.toJSONString(value);
        Assertions.assertThat(json).isEqualTo("{\"property\":\"value1\",\"intValue\":123}");
    }

    @Test
    public void testParserWithFieldMapping() {
        BeanFieldMapping bean = JSONValue.parse("{\"property\":\"value1\",\"intValue\":123}", BeanFieldMapping.class);
        Assertions.assertThat(bean.getStringValue()).isEqualTo("value1");
        Assertions.assertThat(bean.getIntValue()).isEqualTo(123);
    }

}
