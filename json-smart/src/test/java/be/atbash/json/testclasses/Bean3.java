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

import java.util.Map;

/**
 *
 */

public class Bean3 {

    private Map<String, Bean1> bean1Map;

    public Map<String, Bean1> getBean1Map() {
        return bean1Map;
    }

    public void setBean1Map(Map<String, Bean1> bean1Map) {
        this.bean1Map = bean1Map;
    }
}
