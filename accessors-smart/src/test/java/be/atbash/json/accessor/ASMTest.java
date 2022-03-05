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
package be.atbash.json.accessor;

import be.atbash.json.accessor.bean.BTestMixture;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ASMTest {

    @Test
    public void testGet() {

        BeansAccess<BTestMixture> acBT = BeansAccess.get(BTestMixture.class);
        // BeansAccess acHand = new BTestBeansAccessB();

        HashMap<String, String> m = new HashMap<>();
        m.put("A", "ab");
        m.put("B", "Bc");
        m.put("C", "cD");
        m.put("D", "de");
        m.put("E", "ef");

        // String clsPath = FastMap1Builder.getName(m.size());
        // String clsName = clsPath.replace("/", ".");

        byte[] data;

        // data = FastMap1Builder.dump(m.size());
        // data = FastMap2Builder.dump(m);
        // data = FastMapTestDump.dump(clsPath);

        // DynamicClassLoader loader = new
        // DynamicClassLoader(BTest.class.getClassLoader());
        // byte[] data = BTestBeansAccessDump.dump();
        // Class<FastMap> cls = (Class<FastMap>) loader.defineClass(clsName,
        // data);
        // Constructor<FastMap> c = (Constructor<FastMap>)
        // cls.getConstructors()[0];
        // FastMap f = c.newInstance(m);
        // f = new FastMapTest_2<String>(m);
        // f = new FastMapTest_3();
        // f = new FastMapTest_2<String>(m);
        // f = new FastMapTest_3();
        // System.out.println(m.get("A"));
        // 4 entré
        // map => 1.279
        // fastMap => 3.323
        // FastMapTest_1 3.323
        // FastMapTest_2 3.323
        // FastMapTest_3 0.015

        // 3 entry
        // map => 0.983
        // fastmap => 1.014
        // 2 entry
        // map => 0,920
        // fastMap => 0,608

        // 7 entry
        // f 2.667
        // m 0,640

        // 6 entree
        // f 2.215
        // m 0,608

        // 4 entree
        // f 0.032
        // m 0,593

        // 5 entree
        // f
        // m 0.609
        // V2 2.402
        // V3 2.247
        // for (int i = 0; i < 20000; i++) {
        // f.get("A");
        // f.get("B");
        // f.get("C");
        // f.get("D");
        // f.get("E");
        // f.get("F");
        // f.get("G");
        // f.get("H");
        // f.get("I");
        // }
        // System.gc();
        // long T = System.nanoTime();
        // for (int i = 0; i < 20000000; i++) {
        // m.get("A");
        // m.get("B");
        // m.get("C");
        // m.get("D");
        // m.get("E");
        // m.get("F");
        // m.get("G");
        // m.get("H");
        // m.get("I");
        // }
        // T = System.nanoTime() - T;
        // System.out.println(NumberFormat.getInstance().format(T));
        // 10 774 968
        // 596 295 451
        // 2 321 087 341

        BeansAccess<BTestMixture> ac;
        ac = acBT;
        // ac = acHand;
        // ac = acASMHand;
        subtext(ac);
        // T1 = System.currentTimeMillis();
        // for (int i = 0; i < 2000000; i++)
        // subtext(ac);
        // T1 = System.currentTimeMillis() - T1;
        // System.out.println("// Time: " + T1);
    }

    private void subtext(BeansAccess<BTestMixture> acc) {
        BTestMixture t = new BTestMixture();
        acc.set(t, "pubBoolValue", true);
        acc.set(t, "pubIntValue", 13);
        acc.set(t, "pubStrValue", "Test");
        acc.set(t, "privIntValue", 16);
        acc.set(t, "privStrValue", "test Priv");
        assertEquals(13, acc.get(t, "pubIntValue"));
        acc.set(t, "pubIntValue", 14);
        assertEquals(14, acc.get(t, "pubIntValue"));
    }
}