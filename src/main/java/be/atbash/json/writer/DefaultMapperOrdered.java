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
package be.atbash.json.writer;

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import be.atbash.json.JSONArray;
import be.atbash.json.JSONAware;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultMapperOrdered extends Mapper<JSONAware> {
    protected DefaultMapperOrdered(JSONReader base) {
        super(base);
    }

    @Override
    public Mapper<JSONAware> startObject(String key) {
        return base.DEFAULT_ORDERED;
    }

    @Override
    public Mapper<JSONAware> startArray(String key) {
        return base.DEFAULT_ORDERED;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object current, String key, Object value) {
        ((Map<String, Object>) current).put(key, value);
    }

    @Override
    public Object createObject() {
        return new LinkedHashMap<String, Object>();
    }

    @Override
    public void addValue(Object current, Object value) {
        ((JSONArray) current).add(value);
    }

    @Override
    public Object createArray() {
        return new JSONArray();
    }
}
