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
import be.atbash.json.asm.Accessor;
import be.atbash.json.asm.BeansAccess;
import be.atbash.json.style.JSONStyle;

import java.io.IOException;

// This is the net.minidev.BeansWriterASM
public class BeansWriter implements JSONWriter<Object> {
    public <E> void writeJSONString(E value, Appendable out) throws IOException {
        Class<?> cls = value.getClass();
        boolean needSep = false;
        @SuppressWarnings("rawtypes")
        BeansAccess fields = BeansAccess.get(cls, JSONUtil.JSON_SMART_FIELD_FILTER);
        JSONStyle.getDefault().objectStart(out);
        for (Accessor field : fields.getAccessors()) {
            @SuppressWarnings("unchecked")
            Object v = fields.get(value, field.getIndex());
            if (v == null && JSONStyle.getDefault().ignoreNull()) {
                continue;
            }
            if (needSep) {
                JSONStyle.getDefault().objectNext(out);
            } else {
                needSep = true;
            }
            String key = field.getPropertyName();
            JSONUtil.writeJSONKV(key, v, out);
        }
        JSONStyle.getDefault().objectStop(out);

    }

}
