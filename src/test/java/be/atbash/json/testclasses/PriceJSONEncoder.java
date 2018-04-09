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

import be.atbash.json.parser.CustomJSONEncoder;
import be.atbash.util.StringUtils;

/**
 *
 */

public class PriceJSONEncoder implements CustomJSONEncoder<Price> {

    @Override
    public Price parse(Object data) {
        String priceJSON = (String) data;
        Price result = null;
        if (StringUtils.hasText(priceJSON)) {
            for (Currency currency : Currency.values()) {
                if (priceJSON.endsWith(currency.getCode())) {
                    Double value = Double.valueOf(priceJSON.substring(0, priceJSON.length() - currency.getCode().length()));
                    result = new Price(value, currency);
                }
            }
        }
        return result;
    }
}
