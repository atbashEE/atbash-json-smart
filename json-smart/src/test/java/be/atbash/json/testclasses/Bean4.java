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

import be.atbash.json.annotate.JsonIgnore;

/**
 *
 */

public class Bean4 {

    @JsonIgnore
    private String stringValue;
    private int intValue;

    // required since the JSONEncoder/Parser needs to create an instance.
    public Bean4() {
    }

    // Convenient constructor for easier testing
    public Bean4(String stringValue, int intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bean4)) {
            return false;
        }

        Bean4 bean1 = (Bean4) o;

        if (intValue != bean1.intValue) {
            return false;
        }
        return stringValue.equals(bean1.stringValue);
    }

    @Override
    public int hashCode() {
        int result = stringValue.hashCode();
        result = 31 * result + intValue;
        return result;
    }
}
