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
package be.atbash.json.parser;

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

import be.atbash.json.JSONValue;
import be.atbash.json.writer.JSONEncoder;

/**
 * Stripped down version of the original JSONParser, only support for Strings.
 * Parser creates from JSON a Java instance.
 */
public class JSONParser {
    /**
     * allow simple quote as String quoting char
     */
    public final static int ACCEPT_SIMPLE_QUOTE = 1;
    /**
     * allow non quoted test
     */
    public final static int ACCEPT_NON_QUOTE = 2;
    /**
     * Parse NaN as Float.NaN
     */
    public final static int ACCEPT_NAN = 4;
    /**
     * Ignore control char in input text.
     */
    public final static int IGNORE_CONTROL_CHAR = 8;
    /**
     * Use int datatype to store number when it's possible.
     *
     * @since 1.0.7
     */
    public final static int USE_INTEGER_STORAGE = 16;
    /**
     * Throws exception on excessive 0 leading in digits
     *
     * @since 1.0.7
     */
    public final static int ACCEPT_LEADING_ZERO = 32;
    /**
     * Throws exception on useless comma in object and array
     *
     * @since 1.0.8
     */
    public final static int ACCEPT_USELESS_COMMA = 64;
    /**
     * Allow Json-smart to use Double or BigDecimal to store floating point
     * value
     * <p>
     * You may need to disable HI_PRECISION_FLOAT feature on 32bit to improve
     * parsing performances.
     *
     * @since 1.0.9
     */
    public final static int USE_HI_PRECISION_FLOAT = 128;
    /**
     * If enabled json-smart will throws exception if datas are present after
     * the end of the Json data.
     *
     * @since 1.0.9-2
     */
    public final static int ACCEPT_TAILLING_DATA = 256;
    /**
     * smart mode, fastest parsing mode. accept lots of non standard json syntax
     *
     * @since 2.0.1
     */
    public final static int ACCEPT_TAILLING_SPACE = 512;
    /**
     * smart mode, fastest parsing mode. accept lots of non standard json syntax
     *
     * @since 2.2.2
     */
    public final static int REJECT_127_CHAR = 1024;

    /**
     * smart mode, fastest parsing mode. accept lots of non standard json syntax
     *
     * @since 1.0.6
     */
    public final static int MODE_PERMISSIVE = -1;
    /**
     * strict RFC4627 mode.
     * <p>
     * slower than PERMISIF MODE.
     *
     * @since 1.0.6
     */
    public final static int MODE_RFC4627 = USE_INTEGER_STORAGE | USE_HI_PRECISION_FLOAT | ACCEPT_TAILLING_SPACE;
    /**
     * Parse Object like json-simple
     * <p>
     * Best for an iso-bug json-simple API port.
     *
     * @since 1.0.7
     */
    public final static int MODE_JSON_SIMPLE = ACCEPT_USELESS_COMMA | USE_HI_PRECISION_FLOAT | ACCEPT_TAILLING_DATA | ACCEPT_TAILLING_SPACE | REJECT_127_CHAR;
    /**
     * Strictest parsing mode
     *
     * @since 2.0.1
     */
    public final static int MODE_STRICTEST = USE_INTEGER_STORAGE | USE_HI_PRECISION_FLOAT | REJECT_127_CHAR;
    /**
     * Default json-smart processing mode
     */
    public static int DEFAULT_PERMISSIVE_MODE = (System.getProperty("JSON_SMART_SIMPLE") != null) ? MODE_JSON_SIMPLE
            : MODE_PERMISSIVE;

    /*
     * internal fields
     */
    private int mode;

    private JSONParserString pString;

    /**
     * cached constructor
     *
     * @return instance of JSONParserString
     */
    private JSONParserString getPString() {
        if (pString == null) {
            pString = new JSONParserString(mode);
        }
        return pString;
    }

    /**
     * mode = DEFAULT_PERMISSIVE_MODE
     */
    public JSONParser() {
        this.mode = DEFAULT_PERMISSIVE_MODE;
    }

    public JSONParser(int permissifMode) {
        this.mode = permissifMode;
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    public Object parse(String in) {
        return getPString().parse(in);
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    public <T> T parse(String in, JSONEncoder<T> JSONEncoder) {
        return getPString().parse(in, JSONEncoder);
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    public <T> T parse(String in, Class<T> mapTo) {
        return getPString().parse(in, JSONValue.defaultReader.getEncoder(mapTo));
    }
}
