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
package be.atbash.json.testclasses;

import java.util.Objects;

/**
 *
 */

public class Bean5 {

    private String stringValue;
    private int intValue;
    private boolean flag;

    // required since the JSONEncoder/Parser needs to create an instance.
    public Bean5() {
    }

    // Convenient constructor for easier testing
    public Bean5(String stringValue, int intValue, boolean flag) {
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.flag = flag;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bean5)) {
            return false;
        }

        Bean5 bean1 = (Bean5) o;

        if (intValue != bean1.intValue) {
            return false;
        }
        if (flag != bean1.flag) {
            return false;
        }
        return Objects.equals(stringValue, bean1.stringValue);
    }

    @Override
    public int hashCode() {
        int result = stringValue != null ? stringValue.hashCode() : 0;
        result = 31 * result + intValue;
        result = 31 * result + (flag ? 1 : 0);
        return result;
    }
}
