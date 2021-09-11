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
import be.atbash.json.testclasses.Price;
import be.atbash.json.testclasses.Product;
import be.atbash.json.testclasses.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class TestJSONAware {

    @Test
    public void testJSONAware_toJSON_basic() {
        Price price = new Price(123.45, Currency.EURO);
        String json = JSONValue.toJSONString(price);
        assertThat(json).isEqualTo("\"123.45€\"");
    }

    @Test
    public void testJSONAware_toJSON_List() {
        List<Price> data = new ArrayList<>();
        data.add(new Price(123.45, Currency.EURO));
        data.add(new Price(86.34, Currency.USD));
        String json = JSONValue.toJSONString(data);
        assertThat(json).isEqualTo("[\"123.45€\",\"86.34USD\"]");
    }

    @Test
    public void testJSONAware_toJSON_bean() {
        Price price = new Price(123.45, Currency.EURO);

        String json = JSONValue.toJSONString(new Product("Atbash", price));
        assertThat(json).isEqualTo("{\"name\":\"Atbash\",\"price\":\"123.45€\"}");
    }

    @Test
    public void testMappedBy_fromJSON_basic() {
        Price price = JSONValue.parse("\"123.45€\"", Price.class);

        assertThat(price.getValue()).isEqualTo(123.45);
        assertThat(price.getCurrency()).isEqualTo(Currency.EURO);
    }

    @Test
    public void testMappedBy_fromJSON_List() {

        List<Price> prices = (List<Price>) JSONValue.parse("[\"123.45€\",\"86.34USD\"]", new TypeReference<List<Price>>() {
        });

        assertThat(prices).hasSize(2);
        assertThat(prices.get(0).getValue()).isEqualTo(123.45);
        assertThat(prices.get(0).getCurrency()).isEqualTo(Currency.EURO);

    }

    @Test
    public void testMappedBy_fromJSON_bean() {
        Product product = JSONValue.parse("{\"price\":\"123.45€\",\"name\":\"Atbash\"}", Product.class);

        assertThat(product.getName()).isEqualTo("Atbash");
        assertThat(product.getPrice().getValue()).isEqualTo(123.45);
        assertThat(product.getPrice().getCurrency()).isEqualTo(Currency.EURO);
    }

    @Test
    public void testJSONAware_toJSON_token() {
        Token token = new Token();
        token.setKey1("keyValue1");
        token.setKey2("keyValue2");
        token.setKey3("keyValue3");

        token.addAdditional("key4", "value4");
        token.addAdditional("key5", "value5");

        String json = JSONValue.toJSONString(token);
        assertThat(json).contains("\"key1\":\"keyValue1\"");
        assertThat(json).contains("\"key2\":\"keyValue2\"");
        assertThat(json).contains("\"key3\":\"keyValue3\"");
        assertThat(json).contains("\"key4\":\"value4\"");
        assertThat(json).contains("\"key5\":\"value5\"");
    }

    @Test
    public void testMappedByBean_fromJSON_token() {
        Token token = JSONValue.parse("{\"key4\":\"value4\",\"key3\":\"keyValue3\",\"key5\":\"value5\",\"key2\":\"keyValue2\",\"key1\":\"keyValue1\"}", Token.class);
        assertThat(token.getKey1()).isEqualTo("keyValue1");
        assertThat(token.getKey2()).isEqualTo("keyValue2");
        assertThat(token.getKey3()).isEqualTo("keyValue3");

        assertThat(token.getAdditional()).hasSize(2);
        assertThat(token.getAdditional()).containsEntry("key4", "value4");
        assertThat(token.getAdditional()).containsEntry("key5", "value5");

    }

    @Test
    public void testMappedByBean_fromJSON_tokenList() {
        List<Token> tokens = (List<Token>) JSONValue.parse("[{\"key4\":\"value4\",\"key3\":\"keyValue3\",\"key5\":\"value5\",\"key2\":\"keyValue2\",\"key1\":\"keyValue1\"}" +
                ",{\"key4Bis\":\"value4b\",\"key3\":\"keyValue13\",\"key5Bis\":\"value5b\",\"key2\":\"keyValue&2\",\"key1\":\"keyValue11\"} ]", new TypeReference<List<Token>>() {
        });

        assertThat(tokens).hasSize(2);

        Token token = tokens.get(0);
        assertThat(token.getKey1()).isEqualTo("keyValue1");
        assertThat(token.getKey2()).isEqualTo("keyValue2");
        assertThat(token.getKey3()).isEqualTo("keyValue3");

        assertThat(token.getAdditional()).hasSize(2);
        assertThat(token.getAdditional()).containsEntry("key4", "value4");
        assertThat(token.getAdditional()).containsEntry("key5", "value5");

        token = tokens.get(1);
        assertThat(token.getKey1()).isEqualTo("keyValue11");
        assertThat(token.getKey2()).isEqualTo("keyValue&2");
        assertThat(token.getKey3()).isEqualTo("keyValue13");

        assertThat(token.getAdditional()).hasSize(2);
        assertThat(token.getAdditional()).containsEntry("key4Bis", "value4b");
        assertThat(token.getAdditional()).containsEntry("key5Bis", "value5b");

    }
}
