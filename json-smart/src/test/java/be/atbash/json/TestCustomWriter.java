/*
 * Copyright 2017-2021 Rudy De Busscher (https://www.atbash.be)
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

import be.atbash.json.testclasses.Currency;
import be.atbash.json.testclasses.MyColor;
import be.atbash.json.testclasses.PriceWithWriter;
import be.atbash.json.testclasses.ProductWithWriter;
import be.atbash.json.writer.JSONWriter;
import be.atbash.util.codec.Hex;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class TestCustomWriter {

    @Test
    public void testWriter_simple() {
        PriceWithWriter price = new PriceWithWriter(123.45, Currency.EURO);
        String json = JSONValue.toJSONString(price);
        assertThat(json).isEqualTo("\"123.45€\"");

    }

    @Test
    public void testWriter_list() {
        List<PriceWithWriter> data = new ArrayList<>();
        data.add(new PriceWithWriter(123.45, Currency.EURO));
        data.add(new PriceWithWriter(86.34, Currency.USD));
        String json = JSONValue.toJSONString(data);
        assertThat(json).isEqualTo("[\"123.45€\",\"86.34USD\"]");
    }

    @Test
    public void testWriter_bean() {
        PriceWithWriter price = new PriceWithWriter(123.45, Currency.EURO);

        String json = JSONValue.toJSONString(new ProductWithWriter("Atbash", price));
        assertThat(json).isEqualTo("{\"name\":\"Atbash\",\"price\":\"123.45€\"}");
    }

    @Test
    public void testCustomWriter() {
        JSONValue.registerWriter(MyColor.class, new MyColorWriter());

        String json = JSONValue.toJSONString(new MyColor(255, 128, 64));
        assertThat(json).isEqualTo("{\"value\":\"FF8040\"}");
    }

    private class MyColorWriter implements JSONWriter<MyColor> {
        @Override
        public <E extends MyColor> void writeJSONString(E value, Appendable out) throws IOException {
            byte[] bytes = {(byte) value.getR(), (byte) value.getG(), (byte) value.getB()};
            out.append("{\"value\":\"");
            out.append(Hex.encodeToString(bytes));
            out.append("\"}");
        }
    }
}
