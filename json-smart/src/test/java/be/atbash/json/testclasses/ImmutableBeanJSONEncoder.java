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
package be.atbash.json.testclasses;

import be.atbash.json.writer.CustomBeanBuilderJSONEncoder;

public class ImmutableBeanJSONEncoder extends CustomBeanBuilderJSONEncoder<ImmutableBean, ImmutableBeanBuilder> {

    public ImmutableBeanJSONEncoder() {
        super(ImmutableBean.class, ImmutableBeanBuilder.class);
    }

    public void setBuilderValue(ImmutableBeanBuilder builder, String key, Object value) {
        if ("name".equals(key)) {
            builder.withName(value.toString());
        }
        if ("age".equals(key)) {
            builder.withAge((Integer) value);  // Actually we should check if the type matches of course
        }
    }

    @Override
    public ImmutableBean build(ImmutableBeanBuilder builder) {
        return builder.build();
    }
}
