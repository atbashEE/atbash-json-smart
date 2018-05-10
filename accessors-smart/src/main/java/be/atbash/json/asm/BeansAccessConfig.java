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
package be.atbash.json.asm;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class BeansAccessConfig {
    /**
     * Field type convertor for all classes
     *
     * Convertor classes should contains mapping method Prototyped as:
     *
     * public static DestinationType Method(Object data);
     *
     * @see DefaultConverter
     */
    //static protected LinkedHashSet<Class<?>> globalMapper = new LinkedHashSet<Class<?>>();

    /**
     * Field type convertor for custom Class
     * <p>
     * Convertor classes should contains mapping method Prototyped as:
     * <p>
     * public static DestinationType Method(Object data);
     *
     * @see DefaultConverter
     */
    static protected HashMap<Class<?>, LinkedHashSet<Class<?>>> classMapper = new HashMap<Class<?>, LinkedHashSet<Class<?>>>();

    /**
     * FiledName remapper for a specific class or interface
     */
    static protected HashMap<Class<?>, HashMap<String, String>> classFiledNameMapper = new HashMap<Class<?>, HashMap<String, String>>();

    static {
        addTypeMapper(Object.class, DefaultConverter.class);
        addTypeMapper(Object.class, ConvertDate.class);
    }

//	/**
//	 * Field type convertor for all classes
//	 * 
//	 * Convertor classes should contains mapping method Prototyped as:
//	 * 
//	 * public static DestinationType Method(Object data);
//	 * 
//	 * @see DefaultConverter
//	 */
//	public static void addGlobalTypeMapper(Class<?> mapper) {
//		synchronized (globalMapper) {
//			globalMapper.add(mapper);
//		}
//	}

    /**
     * Field type convertor for all classes
     * <p>
     * Convertor classes should contains mapping method Prototyped as:
     * <p>
     * public static DestinationType Method(Object data);
     *
     * @see DefaultConverter
     */
    public static void addTypeMapper(Class<?> clz, Class<?> mapper) {
        synchronized (classMapper) {
            LinkedHashSet<Class<?>> h = classMapper.get(clz);
            if (h == null) {
                h = new LinkedHashSet<Class<?>>();
                classMapper.put(clz, h);
            }

            h.add(mapper);
        }
    }
}
