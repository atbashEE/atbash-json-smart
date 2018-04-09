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
package be.atbash.json.parser.reader;

import be.atbash.json.parser.CustomJSONEncoder;

/**
 * A JSONEncoder which calls the Developer specified CustomJSONEncoder and which will be used to convert the (string) value to the required type.
 */
// Atbash added file.
public class JSONEncoderWrappedCustomEncoder<T> extends JSONEncoder<T> {

    private final CustomJSONEncoder<T> customEncoder;

    public JSONEncoderWrappedCustomEncoder(JSONReader base, CustomJSONEncoder<T> customEncoder) {
        super(base);
        this.customEncoder = customEncoder;
    }

    @Override
    public T convert(Object current) {
        return customEncoder.parse(current);
    }
}
