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

import be.atbash.json.testclasses.ImmutableBean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestImmutableBean {

    @Test
    public void testImmutable() {
        ImmutableBean bean = JSONValue.parse("{\"name\":\"Atbash\",\"age\":2}", ImmutableBean.class);
        Assertions.assertThat(bean).isNotNull();
        Assertions.assertThat(bean.getName()).isEqualTo("Atbash");
        Assertions.assertThat(bean.getAge()).isEqualTo(2);
    }
}
