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

/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import java.lang.reflect.Method;

/**
 * Simple extension from ClassLoader overiding the loadClass(String name,
 * boolean resolve) method and allowing to register new classes
 *
 * @author uriel
 */
class DynamicClassLoader extends ClassLoader {
    DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    private final static String BEAN_AC = BeansAccess.class.getName();
    /**
     * Predefined define defineClass method signature (name, bytes, offset,
     * length)
     */
    private final static Class<?>[] DEF_CLASS_SIG = new Class[]{String.class, byte[].class, int.class, int.class};

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        /*
         * check class by fullname as String.
         */
        if (name.equals(BEAN_AC)) {
            return BeansAccess.class;
        }
        /*
         * Use default class loader
         */
        return super.loadClass(name, resolve);
    }

    /**
     * Call defineClass into the parent classLoader using the
     * method.setAccessible(boolean) hack
     *
     * @see ClassLoader#defineClass(String, byte[], int, int)
     */
    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            // Attempt to load the access class in the same loader, which makes
            // protected and default access members accessible.
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", DEF_CLASS_SIG);
            method.setAccessible(true);
            return (Class<?>) method.invoke(getParent(), new Object[]{name, bytes, 0, bytes.length});
        } catch (Exception ignored) {
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
