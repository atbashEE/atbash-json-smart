/*
 * Copyright 2017-2019 Rudy De Busscher (https://www.atbash.be)
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
package be.atbash.json.asm;

import be.atbash.json.asm.bean.BTest;
import be.atbash.json.asm.bean.TEnum;

@SuppressWarnings("rawtypes")
public class BTestBeansAccessB extends BeansAccess {

    public BTestBeansAccessB() {
        Accessor[] accs = ASMUtil.getAccessors(BTest.class, null);
        super.setAccessor(accs);
    }

    /**
     * set field value by fieldname
     */
    @Override
    public void set(Object object, String methodName, Object value) {
        if ("privIntValue".equals(methodName)) {
            ((BTest) object).setPrivIntValue(DefaultConverter.convertToIntObject(value));
            return;
        }
        if ("privStrValue".equals(methodName)) {
            if (value != null) {
                value = value.toString();
            }
            ((BTest) object).setPrivStrValue((String) value);
            return;
        }

        if ("pubStrValue".equals(methodName)) {
            if (value != null) {
                value = value.toString();
            }
            ((BTest) object).pubStrValue = (String) value;
            return;
        }
        if ("pubIntValue".equals(methodName)) {
            ((BTest) object).pubIntValue = DefaultConverter.convertToIntObject(value);
            return;
        }
        if ("pubBoolValue".equals(methodName)) {
            ((BTest) object).pubBoolValue = DefaultConverter.convertToBool(value);
            return;
        }
        if ("pubIntegerValue".equals(methodName)) {
            ((BTest) object).pubIntegerValue = DefaultConverter.convertToIntObject(value);
            return;
        }
        if ("pubTEnum".equals(methodName)) {
            ((BTest) object).pubTEnum = TEnum.valueOf((String) value);
        }
    }

    /**
     * get field value by fieldname
     */
    @Override
    public Object get(Object object, String methodName) {
        if ("privIntValue".equals(methodName)) {
            return ((BTest) object).getPrivIntValue();
        }
        if ("privStrValue".equals(methodName)) {
            return ((BTest) object).getPrivStrValue();
        }
        if ("pubStrValue".equals(methodName)) {
            return ((BTest) object).pubStrValue;
        }
        if ("pubIntValue".equals(methodName)) {
            return ((BTest) object).pubIntValue;
        }
        if ("pubBoolValue".equals(methodName)) {
            return ((BTest) object).pubBoolValue;
        }
        if ("pubIntegerValue".equals(methodName)) {
            return ((BTest) object).pubIntegerValue;
        }
        if ("pubTEnum".equals(methodName)) {
            return ((BTest) object).pubTEnum;
        }
        return null;
    }

    @Override
    public void set(Object object, int methodIndex, Object value) {
        switch (methodIndex) {
            case 0: // privIntValue;
                ((BTest) object).setPrivIntValue(((Number) value).intValue());
                break;
            case 1: // privStrValue;
                ((BTest) object).setPrivStrValue((String) value);
                break;
            case 2: // pubStrValue;
                ((BTest) object).pubStrValue = (String) value;
                break;
            case 3: // pubIntValue;
                ((BTest) object).pubIntValue = ((Number) value).intValue();
                break;
            case 4: // pubBoolValue;
                ((BTest) object).pubBoolValue = (Boolean) value;
                break;
            case 5:
                ((BTest) object).pubIntegerValue = DefaultConverter.convertToIntObject(value);
                break;
            case 6:
                ((BTest) object).pubTEnum = TEnum.valueOf((String) value);
                break;
            default:
                break;
        }
    }

    public void setInt(Object object, int methodIndex, Object value) {
        if (methodIndex == 0) {
            ((BTest) object).setPrivIntValue(((Number) value).intValue());
            return;
        }
        if (methodIndex == 1) {
            ((BTest) object).setPrivStrValue((String) value);
            return;
        }
        if (methodIndex == 2) {
            ((BTest) object).pubStrValue = (String) value;
            return;
        }
        if (methodIndex == 3) {
            ((BTest) object).pubIntValue = ((Number) value).intValue();
            return;
        }
        if (methodIndex == 4) {
            ((BTest) object).pubBoolValue = (Boolean) value;
            return;
        }
        if (methodIndex == 5) {
            ((BTest) object).pubBoolValue = (Boolean) value;
            return;
        }
        if (methodIndex == 6) {
            ((BTest) object).pubBoolValue = (Boolean) value;
            return;
        }
        if (methodIndex == 7) {
            ((BTest) object).pubBoolValue = (Boolean) value;
            return;
        }
        if (methodIndex == 8) {
            ((BTest) object).pubBoolValue = (Boolean) value;
        }
    }

    @Override
    public Object get(Object object, int methodIndex) {
        switch (methodIndex) {
            case 0: // privIntValue;
                return ((BTest) object).getPrivIntValue();
            case 1: // privStrValue;
                return ((BTest) object).getPrivStrValue();
            case 2: // pubStrValue;
                return ((BTest) object).pubStrValue;
            case 3: // pubIntValue;
                return ((BTest) object).pubIntValue;
            case 4: // privStrValue;
                return ((BTest) object).pubBoolValue;
            case 5: // privStrValue;
                return ((BTest) object).pubIntegerValue;
            case 6: // privStrValue;
                return ((BTest) object).pubTEnum;
            default:
                break;
        }
        return null;
    }

    // public Object getInt(Object object, int methodIndex) {
    // if (methodIndex == 0)
    // return ((BTest) object).getPrivIntValue();
    // if (methodIndex == 1)
    // return ((BTest) object).getPrivStrValue();
    // if (methodIndex == 2)
    // return ((BTest) object).pubStrValue;
    // if (methodIndex == 3)
    // return ((BTest) object).pubIntValue;
    // if (methodIndex == 4)
    // return ((BTest) object).pubBoolValue;
    // return null;
    // }

    @Override
    public Object newInstance() {
        return new BTest();
    }
}
