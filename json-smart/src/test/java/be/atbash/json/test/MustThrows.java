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
package be.atbash.json.test;

import be.atbash.json.parser.JSONParser;
import be.atbash.json.parser.ParseException;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

public class MustThrows {

    public static void testStrictInvalidJson(String json, int exceptionType) {
        testStrictInvalidJson(json, exceptionType, null);
    }

    public static void testStrictInvalidJson(String json, int exceptionType, Class<?> cls) {
        testInvalidJson(json, JSONParser.MODE_RFC4627, exceptionType, cls);
    }

    public static void testInvalidJson(String json, int permissiveMode, int exceptionType) {
        testInvalidJson(json, permissiveMode, exceptionType, null);
    }

    public static void testInvalidJson(String json, int permissiveMode, int exceptionType, Class<?> cls) {
        JSONParser p = new JSONParser(permissiveMode);
        try {
            if (cls == null) {
                p.parse(json);
            } else {
                p.parse(json, cls);
            }
            Assertions.fail("Exception Should Occur parsing:" + json);
        } catch (ParseException e) {
            if (exceptionType != -1) {
                assertThat(e.getErrorType()).isEqualTo(exceptionType);
            }
        }
    }

}
