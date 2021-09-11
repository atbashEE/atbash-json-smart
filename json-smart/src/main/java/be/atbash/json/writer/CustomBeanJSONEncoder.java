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
package be.atbash.json.writer;

import be.atbash.json.parser.reader.BeanEncoder;
import be.atbash.util.PublicAPI;

/**
 * Atbash added file
 */
@PublicAPI
public abstract class CustomBeanJSONEncoder<T> extends BeanEncoder<T> {

    /**
     * Reader can be link to the JsonReader Base
     *
     * @param clz
     */
    protected CustomBeanJSONEncoder(Class<T> clz) {
        super(clz);
    }

    protected abstract void setCustomValue(T current, String key, Object value);

    @Override
    public void setValue(T current, String key, Object value) {
        if (!beansAccess.getField(key).isPresent()) {
            // No field found, so ask for custom handling
            setCustomValue(current, key, value);
        } else {
            beansAccess.set(current, key, value);
        }
    }

    public static class NOPCustomBeanJSONEncoder extends CustomBeanJSONEncoder {

        public NOPCustomBeanJSONEncoder() {
            super(Object.class);
        }

        @Override
        public void setCustomValue(Object current, String key, Object value) {
            throw new UnsupportedOperationException("This implementation is just a marker, not a real implementation");
        }
    }
}

