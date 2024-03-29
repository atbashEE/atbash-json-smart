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
package be.atbash.json.style;

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

import java.io.IOException;

/**
 * JSONStyle object configure JSonSerializer reducing output size.
 *
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 * @author Rudy De Busscher
 */
public class JSONStyle {

    private static final JSONStyle DEFAULT = new JSONStyle();  // NO-COMPRESS of smart-json

    private final boolean _ignore_null = true;  // FIXME Config?

    private final JStylerObj.StringProtector esc = JStylerObj.ESCAPE4Web;

    private JSONStyle() {
    }

    public boolean ignoreNull() {
        return _ignore_null;
    }

    public void writeString(Appendable out, String value) throws IOException {
        out.append('"');
        escape(value, out);
        out.append('"');
    }

    public void escape(String s, Appendable out) {
        if (s == null) {
            return;
        }
        esc.escape(s, out);
    }

    /**
     * begin Object
     */
    public void objectStart(Appendable out) throws IOException {
        out.append('{');
    }

    /**
     * terminate Object
     */
    public void objectStop(Appendable out) throws IOException {
        out.append('}');
    }

    /**
     * Start the first Object element
     */
    public void objectFirstStart(Appendable out) {
        // No action required.
    }

    /**
     * Start a new Object element
     */
    public void objectNext(Appendable out) throws IOException {
        out.append(',');
    }

    /**
     * End Of Object element
     */
    public void objectElmStop(Appendable out) {
        // No action required.
    }

    /**
     * end of Key in json Object
     */
    public void objectEndOfKey(Appendable out) throws IOException {
        out.append(':');
    }

    /**
     * Array start
     */
    public void arrayStart(Appendable out) throws IOException {
        out.append('[');
    }

    /**
     * Array Done
     */
    public void arrayStop(Appendable out) throws IOException {
        out.append(']');
    }

    /**
     * Start the first Array element
     */
    public void arrayFirstObject(Appendable out) {
        // No action required.
    }

    /**
     * Start a new Array element
     */
    public void arrayNextElement(Appendable out) throws IOException {
        out.append(',');
    }

    /**
     * End of an Array element
     */
    public void arrayObjectEnd(Appendable out) {
        // No action required.
    }

    public static JSONStyle getDefault() {
        return DEFAULT;
    }
}
