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

import be.atbash.json.JSONAware;
import be.atbash.json.JSONObject;
import be.atbash.json.parser.MappedBy;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@MappedBy(beanEncoder = TokenJSONEncoder.class)
public class Token implements JSONAware {

    private String key1;
    private String key2;
    private String key3;
    private Map<String, String> additional = new HashMap<>();

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public Map<String, String> getAdditional() {
        return additional;
    }

    public void addAdditional(String key, String value) {
        additional.put(key, value);
    }

    @Override
    public String toJSONString() {
        JSONObject result = new JSONObject();
        result.put("key1", key1);
        result.put("key2", key2);
        result.put("key3", key3);
        for (Map.Entry<String, String> entry : additional.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.toJSONString();
    }
}
