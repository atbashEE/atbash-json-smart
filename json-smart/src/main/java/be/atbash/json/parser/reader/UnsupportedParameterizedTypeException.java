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

import be.atbash.util.exception.AtbashException;

import java.lang.reflect.ParameterizedType;

/**
 *
 */

public class UnsupportedParameterizedTypeException extends AtbashException {

    public UnsupportedParameterizedTypeException(ParameterizedType type) {
        // TODO type.getActualTypeArguments() can be null or empty?
        super(String.format("No encoder found for type %s, please register a custom encoder or use generic types List and Map.", type.getActualTypeArguments()[0].toString()))
        ;
    }
}
