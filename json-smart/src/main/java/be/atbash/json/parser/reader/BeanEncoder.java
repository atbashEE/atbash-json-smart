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
package be.atbash.json.parser.reader;

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.atbash.json.JSONUtil;
import be.atbash.json.accessor.BeansAccess;
import be.atbash.json.accessor.ConvertDate;
import be.atbash.json.parser.CustomJSONEncoder;
import be.atbash.json.parser.MappedBy;
import be.atbash.util.exception.AtbashUnexpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Optional;

public class BeanEncoder<T> extends JSONEncoder<T> {

    private static final JSONEncoder<Date> jsonEncoderDate;

    private final Class<T> clz;
    protected final BeansAccess<T> beansAccess;

    static {
        jsonEncoderDate = new ArraysJSONEncoder<Date>() {
            @Override
            public Date convert(Object current) {
                return ConvertDate.convertToDate(current);
            }
        };
    }

    public BeanEncoder(Class<T> clz) {
        this.clz = clz;
        if (hasProperConstructor(clz)) {
            beansAccess = BeansAccess.get(clz, JSONUtil.JSON_SMART_FIELD_FILTER);

        } else {
            beansAccess = null;

        }
    }

    private boolean hasProperConstructor(Class<T> clz) {
        boolean result = false;
        for (Constructor<?> constructor : clz.getConstructors()) {
            if ((constructor.getModifiers() & Modifier.PUBLIC) != 0 && constructor.getParameterTypes().length == 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void setValue(T current, String key, Object value) {
        // Atbash support for MappedBy
        Optional<Field> field = beansAccess.getField(key);
        if (!field.isPresent()) {
            // annotated with JsonIgnore or unexisting field
            return;
        }

        MappedBy mappedBy = field.get().getType().getAnnotation(MappedBy.class);
        if (mappedBy != null) {
            if (!(mappedBy.encoder().equals(CustomJSONEncoder.NOPJSONEncoder.class))) {

                try {
                    value = mappedBy.encoder().newInstance().parse(value);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new AtbashUnexpectedException(e);
                }
            }

        }
        beansAccess.set(current, field.get().getName(), value);
    }

    @Override
    public Object getValue(T current, String key) {
        return beansAccess.get(current, key);
    }

    @Override
    public Type getType(String key) {
        Optional<Field> field = beansAccess.getField(key);
        return field.map(Field::getGenericType).orElse(null);
    }

    @Override
    public JSONEncoder<?> startArray(String key) {
        Type type = getType(key);
        if (type == null) {
            throw new IncorrectContentException("Can not find Array '" + key + "' field in " + clz);
        }
        return JSONEncoderFactory.getInstance().getEncoder(type);
    }

    @Override
    public JSONEncoder<?> startObject(String key) {
        Type type = getType(key);
        if (type == null) {
            throw new IncorrectContentException("Can not find Object '" + key + "' field in " + clz);
        }
        return JSONEncoderFactory.getInstance().getEncoder(type);
    }

    @Override
    public T createObject() {
        return beansAccess == null ? null : beansAccess.newInstance();
    }

    static JSONEncoder<Date> getJsonEncoderDate() {
        return jsonEncoderDate;
    }
}
