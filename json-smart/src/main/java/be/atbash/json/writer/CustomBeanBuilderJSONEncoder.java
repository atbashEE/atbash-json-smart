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

import be.atbash.json.JSONUtil;
import be.atbash.json.accessor.BeansAccess;
import be.atbash.json.parser.reader.JSONEncoderBuilder;
import be.atbash.util.PublicAPI;

/**
 * Atbash added file
 */
@PublicAPI
public abstract class CustomBeanBuilderJSONEncoder<T, U> extends CustomBeanJSONEncoder<T> implements JSONEncoderBuilder<T, U> {

    protected final BeansAccess<U> beansAccessBuilder;

    /**
     * @param clz        The class which will be generated
     * @param builderClz The builder class which will be able to create the class.
     */
    protected CustomBeanBuilderJSONEncoder(Class<T> clz, Class<U> builderClz) {
        super(clz);
        this.beansAccessBuilder = BeansAccess.get(builderClz, JSONUtil.JSON_SMART_FIELD_FILTER);
    }

    @Override
    public U createBuilder() {
        return beansAccessBuilder == null ? null : beansAccessBuilder.newInstance();
    }

    @Override
    protected void setCustomValue(T current, String key, Object value) {

    }

}

