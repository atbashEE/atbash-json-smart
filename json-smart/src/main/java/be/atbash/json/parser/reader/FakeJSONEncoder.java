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
package be.atbash.json.parser.reader;


/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Encoder ise to test if the JSON is valid with performing the entire mapping.
 */
public class FakeJSONEncoder extends JSONEncoder<Object> {
    private static final JSONEncoder<Object> INSTANCE = new FakeJSONEncoder();

    private FakeJSONEncoder() {
    }

    @Override
    public JSONEncoder<?> startObject(String key) {
        return this;
    }

    @Override
    public JSONEncoder<?> startArray(String key) {
        return this;
    }

    @Override
    public void setValue(Object current, String key, Object value) {
        // No need to set the actual value into the object.
    }

    @Override
    public void addValue(Object current, Object value) {
        // No need to set the actual value into the JSON Array.
    }

    @Override
    public Object createObject() {
        // We don't create an object here, just want to test the JSON structure.
        return null;
    }

    @Override
    public Object createArray() {
        // We don't create an object here, just want to test the JSON structure.
        return null;
    }

    public static JSONEncoder<Object> getInstance() {
        return INSTANCE;
    }
}
