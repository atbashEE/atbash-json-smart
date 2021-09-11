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
package be.atbash.json.accessor.bean;

public class BTest {
    public int pubIntValue;
    public String pubStrValue;
    private Integer privIntValue;
    private String privStrValue;
    public boolean pubBoolValue;
    public Integer pubIntegerValue;
    public TEnum pubTEnum;

    public void setPrivIntValue(Integer privIntValue) {
        this.privIntValue = privIntValue;
    }

    public Integer getPrivIntValue() {
        return privIntValue;
    }

    public void setPrivStrValue(String privStrValue) {
        this.privStrValue = privStrValue;
    }

    public String getPrivStrValue() {
        return privStrValue;
    }

    public String toString() {
        return "Public(i:" + pubIntValue + " s:" + pubStrValue + "B: " + pubBoolValue + ") Private(i:" + privIntValue
                + " s:" + privStrValue + ")";
    }
}
