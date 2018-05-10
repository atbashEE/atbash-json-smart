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
package be.atbash.json.asm.bean;

import be.atbash.json.asm.BeansAccess;

@SuppressWarnings("rawtypes")
public class BEnumPrivAc extends BeansAccess {

    @Override
    public void set(Object object, int methodIndex, Object value) {
        if (methodIndex == 0) {
            if (value != null)
            // value = TEnum.valueOf((String) value);
            {
                value = TEnum.valueOf(value.toString());
            }
            ((BEnumPriv) object).setValue((TEnum) value);
            return;
        }
        throw new be.atbash.json.asm.ex.NoSuchFieldException("mapping BEnumPriv failed to map field:".concat(Integer.toString(methodIndex)));
    }

    @Override
    public Object get(Object object, int methodIndex) {
        if (methodIndex == 0) {
            return ((BEnumPriv) object).getValue();
        }
        throw new be.atbash.json.asm.ex.NoSuchFieldException("mapping BEnumPriv failed to map field:".concat(Integer.toString(methodIndex)));
    }

    @Override
    public void set(Object object, String methodIndex, Object value) {
        if (methodIndex.equals("value")) {
            if (value != null)
            // value = TEnum.valueOf((String) value);
            {
                value = TEnum.valueOf(value.toString());
            }
            ((BEnumPriv) object).setValue((TEnum) value);
            return;
        }
        throw new be.atbash.json.asm.ex.NoSuchFieldException("mapping BEnumPriv failed to map field:".concat(methodIndex));
    }

    @Override
    public Object get(Object object, String methodIndex) {
        if (methodIndex.equals("value")) {
            return ((BEnumPriv) object).getValue();
        }
        throw new be.atbash.json.asm.ex.NoSuchFieldException("mapping BEnumPriv failed to map field:".concat(methodIndex));
    }

    @Override
    public Object newInstance() {
        return new BEnumPriv();
    }
}
