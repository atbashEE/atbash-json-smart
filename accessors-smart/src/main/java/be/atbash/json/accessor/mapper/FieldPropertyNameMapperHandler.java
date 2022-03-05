/*
 * Copyright 2017-2022 Rudy De Busscher (https://www.atbash.be)
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
package be.atbash.json.accessor.mapper;

import be.atbash.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 *
 */

public final class FieldPropertyNameMapperHandler {

    private static FieldPropertyNameMapperHandler INSTANCE;

    private final List<FieldPropertyNameMapper> mappers;

    private FieldPropertyNameMapperHandler() {
        mappers = new ArrayList<>();

        for (FieldPropertyNameMapper fieldPropertyNameMapper : ServiceLoader.load(FieldPropertyNameMapper.class)) {
            mappers.add(fieldPropertyNameMapper);
        }
    }

    public String definePropertyName(Field field) {
        String result = null;
        for (FieldPropertyNameMapper mapper : mappers) {
            result = mapper.getPropertyName(field);
            if (StringUtils.hasText(result)) {
                break;
            }
        }

        if (result == null) {
            result = field.getName();
        }
        return result;
    }

    public static FieldPropertyNameMapperHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FieldPropertyNameMapperHandler();
        }
        return INSTANCE;
    }
}
