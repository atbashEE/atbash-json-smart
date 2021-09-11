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
package be.atbash.json.accessor.bean;

import be.atbash.json.accessor.BeansAccess;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestAsmAtom {

    @Test
    public void testpub() {
        // int fieldID = 0;
        String fieldID = "value";
        {
            BeansAccess<BStrPub> ac = BeansAccess.get(BStrPub.class);
            BStrPub p = new BStrPub();
            String val = "toto";
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BLongPub> ac = BeansAccess.get(BLongPub.class);
            BLongPub p = new BLongPub();
            Long val = 123L;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBooleanPub> ac = BeansAccess.get(BBooleanPub.class);
            BBooleanPub p = new BBooleanPub();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBoolPub> ac = BeansAccess.get(BBoolPub.class);
            BBoolPub p = new BBoolPub();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BEnumPriv> ac = BeansAccess.get(BEnumPriv.class);
            BEnumPriv p = new BEnumPriv();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BObjectPriv> ac = BeansAccess.get(BObjectPriv.class);
            BObjectPriv p = new BObjectPriv();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }

    }

    @Test
    public void testPriv() {
        // int fieldID = 0;
        String fieldID = "value";
        {
            BeansAccess<BStrPriv> ac = BeansAccess.get(BStrPriv.class);
            BStrPriv p = new BStrPriv();
            String val = "toto";
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BLongPriv> ac = BeansAccess.get(BLongPriv.class);
            BLongPriv p = new BLongPriv();
            Long val = 123L;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBooleanPriv> ac = BeansAccess.get(BBooleanPriv.class);
            BBooleanPriv p = new BBooleanPriv();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BBoolPriv> ac = BeansAccess.get(BBoolPriv.class);
            BBoolPriv p = new BBoolPriv();
            Boolean val = Boolean.TRUE;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BEnumPub> ac = BeansAccess.get(BEnumPub.class);
            BEnumPub p = new BEnumPub();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }
        {
            BeansAccess<BObjectPub> ac = BeansAccess.get(BObjectPub.class);
            BObjectPub p = new BObjectPub();
            TEnum val = TEnum.V2;
            ac.set(p, fieldID, val);
            assertEquals(val, ac.get(p, fieldID));
        }

    }

}