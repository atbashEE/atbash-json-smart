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

import be.atbash.json.JSONUtil;
import be.atbash.json.accessor.BeansAccess;
import be.atbash.json.accessor.mapper.FieldPropertyNameMapperHandler;
import be.atbash.json.style.JSONStyle;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

// This is the net.minidev.BeansWriterASM
public class BeansWriter implements JSONWriter<Object> {

    private FieldPropertyNameMapperHandler nameMapperHandler = FieldPropertyNameMapperHandler.getInstance();

    public <E> void writeJSONString(E value, Appendable out) throws IOException {
        Class<?> cls = value.getClass();
        boolean needSep = false;
        @SuppressWarnings("rawtypes")
        BeansAccess beansAccess = BeansAccess.get(cls, JSONUtil.JSON_SMART_FIELD_FILTER);
        JSONStyle.getDefault().objectStart(out);
        List<Field> fields = beansAccess.getFields();
        for (Field field : fields) {
            @SuppressWarnings("unchecked")
            Object v = beansAccess.get(value, field.getName());
            if (v == null && JSONStyle.getDefault().ignoreNull()) {
                continue;
            }
            if (needSep) {
                JSONStyle.getDefault().objectNext(out);
            } else {
                needSep = true;
            }

            String key = nameMapperHandler.definePropertyName(field);
            JSONUtil.writeJSONKV(key, v, out);
        }
        JSONStyle.getDefault().objectStop(out);

    }

}
