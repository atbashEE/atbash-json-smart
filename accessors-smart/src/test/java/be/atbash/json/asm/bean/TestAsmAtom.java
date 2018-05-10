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
import junit.framework.TestCase;

public class TestAsmAtom extends TestCase {

    public void testpub() throws Exception {
        // int fieldID = 0;
        String fieldID = "value";
        {
            BeansAccess<BStrPub> ac = BeansAccess.get(BStrPub.class);
            BStrPub p = ac.newInstance();
            String val = "toto";
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BLongPub> ac = BeansAccess.get(BLongPub.class);
            BLongPub p = ac.newInstance();
            Long val = 123L;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBooleanPub> ac = BeansAccess.get(BBooleanPub.class);
            BBooleanPub p = ac.newInstance();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBoolPub> ac = BeansAccess.get(BBoolPub.class);
            BBoolPub p = ac.newInstance();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BEnumPriv> ac = BeansAccess.get(BEnumPriv.class);
            BEnumPriv p = ac.newInstance();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BObjectPriv> ac = BeansAccess.get(BObjectPriv.class);
            BObjectPriv p = ac.newInstance();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }

    }

    public void testPriv() throws Exception {
        // int fieldID = 0;
        String fieldID = "value";
        {
            BeansAccess<BStrPriv> ac = BeansAccess.get(BStrPriv.class);
            BStrPriv p = ac.newInstance();
            String val = "toto";
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BLongPriv> ac = BeansAccess.get(BLongPriv.class);
            BLongPriv p = ac.newInstance();
            Long val = 123L;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBooleanPriv> ac = BeansAccess.get(BBooleanPriv.class);
            BBooleanPriv p = ac.newInstance();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBoolPriv> ac = BeansAccess.get(BBoolPriv.class);
            BBoolPriv p = ac.newInstance();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BEnumPub> ac = BeansAccess.get(BEnumPub.class);
            BEnumPub p = ac.newInstance();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BObjectPub> ac = BeansAccess.get(BObjectPub.class);
            BObjectPub p = ac.newInstance();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }

    }

}