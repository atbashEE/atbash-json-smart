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

import be.atbash.json.parser.reader.JSONEncoder;
import be.atbash.json.parser.reader.UnsupportedParameterizedTypeException;
import be.atbash.json.testclasses.MyColor;
import be.atbash.json.testclasses.MyImplementation;
import be.atbash.json.testclasses.MyType;
import be.atbash.util.codec.Hex;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCustomEncoder {

    @Test
    public void customEncoder_parameterizedType() {
        JSONValue.registerEncoder(new TypeReference<MyType<String>>() {
        }, new MyImplementationEncoder());
        MyType<String> result = JSONValue.parse("[\"value1\"]", new TypeReference<MyType<String>>() {
        });
        Assertions.assertThat(result.getValue()).isEqualTo("value1");
    }

    @Test
    public void customEncoder_unknownParameterizedType() {
        Assertions.assertThatThrownBy(() ->
                JSONValue.parse("[\"value1\"]", new TypeReference<MyType<Double>>() {
                })
        ).isInstanceOf(UnsupportedParameterizedTypeException.class);

    }

    @Test
    public void customEncoder_class() {
        JSONValue.registerEncoder(MyColor.class, new RGBEncoder());
        MyColor color = JSONValue.parse("{\"value\":\"FF8040\"}", MyColor.class);
        Assertions.assertThat(color.getR()).isEqualTo(255);
        Assertions.assertThat(color.getG()).isEqualTo(128);
        Assertions.assertThat(color.getB()).isEqualTo(64);
    }

    public static class MyImplementationEncoder extends JSONEncoder {
        @Override
        public Object convert(Object current) {
            return super.convert(current);
        }

        @Override
        public Object createArray() {
            return new MyImplementation();
        }

        @Override
        public void addValue(Object current, Object value) {
            ((MyImplementation) current).setValue(value.toString());
        }
    }

    public static class RGBEncoder extends JSONEncoder<MyColor> {

        @Override
        public void setValue(MyColor current, String key, Object value) {
            if ("value".equals(key)) {
                byte[] byteValues = Hex.decode(value.toString());

                if (byteValues.length == 3) {
                    current.setR(asInt(byteValues[0]));
                    current.setG(asInt(byteValues[1]));
                    current.setB(asInt(byteValues[2]));
                }
            } else {
                super.setValue(current, key, value);
            }
        }

        private int asInt(byte byteValue) {
            int result = byteValue;
            if (result < 0) {
                result += 256;
            }
            return result;
        }

        @Override
        public MyColor createObject() {
            return new MyColor();
        }
    }
}
