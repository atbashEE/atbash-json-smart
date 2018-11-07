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
 * you may not use this file except json compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to json writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.atbash.json.parser.reader.DefaultJSONEncoder;
import be.atbash.json.parser.reader.JSONEncoder;
import be.atbash.json.parser.reader.JSONEncoderBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

import static be.atbash.json.parser.ParseException.*;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 *
 * @author Uriel Chemouni &lt;uchemouni@gmail.com&gt;
 */
class JSONParserString {
    private static final byte EOI = 0x1A;
    private static final char MAX_STOP = 126; // '}' -> 125
    private static boolean[] stopAll = new boolean[MAX_STOP];
    private static boolean[] stopArray = new boolean[MAX_STOP];
    private static boolean[] stopKey = new boolean[MAX_STOP];
    private static boolean[] stopValue = new boolean[MAX_STOP];
    private static boolean[] stopX = new boolean[MAX_STOP];

    static {
        stopKey[':'] = stopKey[EOI] = true;
        stopValue[','] = stopValue['}'] = stopValue[EOI] = true;
        stopArray[','] = stopArray[']'] = stopArray[EOI] = true;
        stopX[EOI] = true;
        stopAll[','] = stopAll[':'] = true;
        stopAll[']'] = stopAll['}'] = stopAll[EOI] = true;
    }

    /*
     * Parsing flags
     */
    private final boolean acceptLeadinZero;
    private final boolean acceptNaN;
    private final boolean acceptNonQuote;
    private final boolean acceptSimpleQuote;
    private final boolean acceptUselessComma;
    private final boolean checkTailingData;
    private final boolean checkTailingSpace;
    private final boolean ignoreControlChar;
    private final boolean useHiPrecisionFloat;
    private final boolean useIntegerStorage;
    private final boolean reject127;

    /*
     * End of static declaration
     */
    private String json;
    private int len;
    private char c;
    private String lastKey;

    private final MSB sb = new MSB(15);
    private Object xo;
    private String xs;
    private int pos;

    JSONParserString(int permissiveMode) {

        acceptNaN = (permissiveMode & JSONParser.ACCEPT_NAN) > 0;
        acceptNonQuote = (permissiveMode & JSONParser.ACCEPT_NON_QUOTE) > 0;
        acceptSimpleQuote = (permissiveMode & JSONParser.ACCEPT_SIMPLE_QUOTE) > 0;
        ignoreControlChar = (permissiveMode & JSONParser.IGNORE_CONTROL_CHAR) > 0;
        useIntegerStorage = (permissiveMode & JSONParser.USE_INTEGER_STORAGE) > 0;
        acceptLeadinZero = (permissiveMode & JSONParser.ACCEPT_LEADING_ZERO) > 0;
        acceptUselessComma = (permissiveMode & JSONParser.ACCEPT_USELESS_COMMA) > 0;
        useHiPrecisionFloat = (permissiveMode & JSONParser.USE_HI_PRECISION_FLOAT) > 0;
        checkTailingData = (permissiveMode & (JSONParser.ACCEPT_TAILLING_DATA | JSONParser.ACCEPT_TAILLING_SPACE)) != (JSONParser.ACCEPT_TAILLING_DATA | JSONParser.ACCEPT_TAILLING_SPACE);
        checkTailingSpace = (permissiveMode & JSONParser.ACCEPT_TAILLING_SPACE) == 0;
        reject127 = (permissiveMode & JSONParser.REJECT_127_CHAR) > 0;

    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    public Object parse(String json) {
        return parse(json, DefaultJSONEncoder.getInstance());
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    public <T> T parse(String json, JSONEncoder<T> jsonEncoder) {
        if (json == null) {
            throw new IllegalArgumentException("Parameter JSON text cannot be null");
        }
        this.json = json;
        len = json.length();
        return parse(jsonEncoder);
    }

    private <T> T parse(JSONEncoder<T> jsonEncoder) {
        pos = -1;
        T result;
        read();
        result = readFirst(jsonEncoder);
        if (checkTailingData) {
            if (!checkTailingSpace) {
                skipSpace();
            }
            if (c != EOI) {
                throw new ParseException(pos - 1, ERROR_UNEXPECTED_TOKEN, c);
            }
        }

        xs = null;
        xo = null;
        return result;
    }

    private void checkControlChar() {
        if (ignoreControlChar) {
            return;
        }
        int l = xs.length();
        for (int i = 0; i < l; i++) {
            char c = xs.charAt(i);
            if (c <= 31) {
                throw new ParseException(pos + i, ERROR_UNEXPECTED_CHAR, c);
            }
            if (c == 127 && reject127) {
                throw new ParseException(pos + i, ERROR_UNEXPECTED_CHAR, c);
            }
        }
    }

    private void checkLeadingZero() {
        int len = xs.length();
        if (len == 1) {
            return;
        }
        if (len == 2) {
            if ("00".equals(xs)) {
                throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
            }
            return;
        }
        char c1 = xs.charAt(0);
        char c2 = xs.charAt(1);
        if (c1 == '-') {
            char c3 = xs.charAt(2);
            if (c2 == '0' && c3 >= '0' && c3 <= '9') {
                throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
            }
            return;
        }
        if (c1 == '0' && c2 >= '0' && c2 <= '9') {
            throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, xs);
        }
    }

    private Number extractFloat() {
        if (!acceptLeadinZero) {
            checkLeadingZero();
        }
        if (!useHiPrecisionFloat) {
            return Float.parseFloat(xs);
        }
        if (xs.length() > 18) {
            // follow JSonIJ parsing method
            return new BigDecimal(xs);
        }
        return Double.parseDouble(xs);
    }

    private Number parseNumber(String s) {
        // pos
        int p = 0;
        // len
        int l = s.length();
        // max pos long base 10 len
        int max = 19;
        boolean neg;

        if (s.charAt(0) == '-') {
            p++;
            max++;
            neg = true;
            if (!acceptLeadinZero && l >= 3 && s.charAt(1) == '0') {
                throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, s);
            }
        } else {
            neg = false;
            if (!acceptLeadinZero && l >= 2 && s.charAt(0) == '0') {
                throw new ParseException(pos, ERROR_UNEXPECTED_LEADING_0, s);
            }
        }

        boolean mustCheck;
        if (l < max) {
            max = l;
            mustCheck = false;
        } else if (l > max) {
            return new BigInteger(s, 10);
        } else {
            max = l - 1;
            mustCheck = true;
        }

        long r = 0;
        while (p < max) {
            r = (r * 10L) + ('0' - s.charAt(p++));
        }
        if (mustCheck) {
            boolean isBig;
            if (r > -922337203685477580L) {
                isBig = false;
            } else if (r < -922337203685477580L) {
                isBig = true;
            } else {
                if (neg) {
                    isBig = s.charAt(p) > '8';
                } else {
                    isBig = s.charAt(p) > '7';
                }
            }
            if (isBig) {
                return new BigInteger(s, 10);
            }
            r = r * 10L + ('0' - s.charAt(p));
        }
        if (neg) {
            if (useIntegerStorage && r >= Integer.MIN_VALUE) {
                return (int) r;
            }
            return r;
        }
        r = -r;
        if (useIntegerStorage && r <= Integer.MAX_VALUE) {
            return (int) r;
        }
        return r;
    }

    private <T> T readArray(JSONEncoder<T> jsonEncoder) {
        Object current = jsonEncoder.createArray();
        if (c != '[') {
            throw new RuntimeException("Internal Error");
        }
        read();
        boolean needData = false;
        // special case needData is false and can close is true
        if (c == ',' && !acceptUselessComma) {
            throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
        }
        for (; ; ) {
            switch (c) {
                case ' ':
                case '\r':
                case '\n':
                case '\t':
                    read();
                    continue;
                case ']':
                    if (needData && !acceptUselessComma) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    }
                    read(); /* unstack */
                    //
                    return jsonEncoder.convert(current);
                case ':':
                case '}':
                    throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                case ',':
                    if (needData && !acceptUselessComma) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    }
                    read();
                    needData = true;
                    continue;
                case EOI:
                    throw new ParseException(pos - 1, ERROR_UNEXPECTED_EOF, "EOF");
                default:
                    jsonEncoder.addValue(current, readMain(jsonEncoder, stopArray));
                    needData = false;
            }
        }
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    private <T> T readFirst(JSONEncoder<T> jsonEncoder) {
        for (; ; ) {
            switch (c) {
                // skip spaces
                case ' ':
                case '\r':
                case '\n':
                case '\t':
                    read();
                    continue;
                    // invalid stats
                case ':':
                case '}':
                case ']':
                    throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    // start object
                case '{':
                    return readObject(jsonEncoder);
                // start Array
                case '[':
                    return readArray(jsonEncoder);
                // start string
                case '"':
                case '\'':
                    readString();
                    //
                    return jsonEncoder.convert(xs);
                // string or null
                case 'n':
                    readNQString(stopX);
                    if ("null".equals(xs)) {
                        //
                        return null;
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return jsonEncoder.convert(xs);
                // string or false
                case 'f':
                    readNQString(stopX);
                    if ("false".equals(xs)) {
                        //
                        return jsonEncoder.convert(Boolean.FALSE);
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return jsonEncoder.convert(xs);
                // string or true
                case 't':
                    readNQString(stopX);
                    if ("true".equals(xs)) {
                        //
                        return jsonEncoder.convert(Boolean.TRUE);
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return jsonEncoder.convert(xs);
                // string or NaN
                case 'N':
                    readNQString(stopX);
                    if (!acceptNaN) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    if ("NaN".equals(xs)) {
                        //
                        return jsonEncoder.convert(Float.NaN);
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return jsonEncoder.convert(xs);
                // digits
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                    xo = readNumber(stopX);
                    //
                    return jsonEncoder.convert(xo);
                default:
                    readNQString(stopX);
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return jsonEncoder.convert(xs);
            }
        }
    }

    /**
     * use to return Primitive Type, or String, Or JsonObject or JsonArray
     * generated by a ContainerFactory
     */
    private Object readMain(JSONEncoder<?> jsonEncoder, boolean[] stop) {
        for (; ; ) {
            switch (c) {
                // skip spaces
                case ' ':
                case '\r':
                case '\n':
                case '\t':
                    read();
                    continue;
                    // invalid stats
                case ':':
                case '}':
                case ']':
                    throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    // start object
                case '{':
                    return readObject(jsonEncoder.startObject(lastKey));
                // start Array
                case '[':
                    return readArray(jsonEncoder.startArray(lastKey));
                // start string
                case '"':
                case '\'':
                    readString();
                    //
                    return xs;
                // string or null
                case 'n':
                    readNQString(stop);
                    if ("null".equals(xs)) {
                        //
                        return null;
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return xs;
                // string or false
                case 'f':
                    readNQString(stop);
                    if ("false".equals(xs)) {
                        //
                        return Boolean.FALSE;
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return xs;
                // string or true
                case 't':
                    readNQString(stop);
                    if ("true".equals(xs)) {
                        //
                        return Boolean.TRUE;
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return xs;
                // string or NaN
                case 'N':
                    readNQString(stop);
                    if (!acceptNaN) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    if ("NaN".equals(xs)) {
                        //
                        return Float.NaN;
                    }
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return xs;
                // digits
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                    //
                    //
                    return readNumber(stop);
                default:
                    readNQString(stop);
                    if (!acceptNonQuote) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                    }
                    //
                    return xs;
            }
        }
    }

    private <T, U> T readObject(JSONEncoder<T> jsonEncoder) {
        //
        if (c != '{') {
            throw new RuntimeException("Internal Error");
        }
        T current = jsonEncoder.createObject();
        U builder = null;
        if (jsonEncoder instanceof JSONEncoderBuilder) {
            builder = ((JSONEncoderBuilder<T, U>) jsonEncoder).createBuilder();
        }
        boolean needData = false;
        boolean acceptData = true;
        for (; ; ) {
            read();
            switch (c) {
                case ' ':
                case '\r':
                case '\t':
                case '\n':
                    continue;
                case ':':
                case ']':
                case '[':
                case '{':
                    throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                case '}':
                    if (needData && !acceptUselessComma) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    }
                    read(); /* unstack */
                    //
                    return jsonEncoder.convert(current);
                case ',':
                    if (needData && !acceptUselessComma) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    }
                    acceptData = needData = true;
                    continue;
                case '"':
                case '\'':
                default:
                    if (c == '\"' || c == '\'') {
                        readString();
                    } else {
                        readNQString(stopKey);
                        if (!acceptNonQuote) {
                            throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                        }
                    }
                    String key = xs;
                    if (!acceptData) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, key);
                    }

                    // Skip spaces
                    skipSpace();

                    if (c != ':') {
                        if (c == EOI) {
                            throw new ParseException(pos - 1, ERROR_UNEXPECTED_EOF, null);
                        }
                        throw new ParseException(pos - 1, ERROR_UNEXPECTED_CHAR, c);
                    }
                    readNoEnd(); /* skip : */
                    lastKey = key;
                    Object value = readMain(jsonEncoder, stopValue);
                    if (builder == null) {
                        jsonEncoder.setValue(current, key, value);
                    } else {
                        ((JSONEncoderBuilder<T, U>) jsonEncoder).setBuilderValue(builder, key, value);

                    }
                    lastKey = null;

                    // should loop skipping read step
                    skipSpace();
                    if (c == '}') {
                        read(); /* unstack */
                        //
                        if (builder == null) {
                            return jsonEncoder.convert(current);
                        } else {
                            return ((JSONEncoderBuilder<T, U>) jsonEncoder).build(builder);
                        }
                    }
                    if (c == EOI) // Fixed on 18/10/2011 reported by vladimir
                    {
                        throw new ParseException(pos - 1, ERROR_UNEXPECTED_EOF, null);
                    }
                    if (c == ',') {
                        acceptData = needData = true;
                    } else {
                        throw new ParseException(pos - 1, ERROR_UNEXPECTED_TOKEN, c);
                    }
            }
        }
    }

    private void readString2() {
        char sep = c;
        for (; ; ) {
            read();
            switch (c) {
                case EOI:
                    throw new ParseException(pos - 1, ERROR_UNEXPECTED_EOF, null);
                case '"':
                case '\'':
                    if (sep == c) {
                        read();
                        xs = sb.toString();
                        return;
                    }
                    sb.append(c);
                    break;
                case '\\':
                    read();
                    switch (c) {
                        case 't':
                            sb.append('\t');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case '\'':
                            sb.append('\'');
                            break;
                        case '"':
                            sb.append('"');
                            break;
                        case 'u':
                            sb.append(readUnicode(4));
                            break;
                        case 'x':
                            sb.append(readUnicode(2));
                            break;
                        default:
                            break;
                    }
                    break;
                case '\0': // end of string
                case (char) 1: // Start of heading
                case (char) 2: // Start of text
                case (char) 3: // End of text
                case (char) 4: // End of transmission
                case (char) 5: // Enquiry
                case (char) 6: // Acknowledge
                case (char) 7: // Bell
                case '\b': // 8: backSpase
                case '\t': // 9: horizontal tab
                case '\n': // 10: new line
                case (char) 11: // Vertical tab
                case '\f': // 12: form feed
                case '\r': // 13: return carriage
                case (char) 14: // Shift Out, alternate character set
                case (char) 15: // Shift In, resume defaultn character set
                case (char) 16: // Data link escape
                case (char) 17: // XON, with XOFF to pause listings;
                case (char) 18: // Device control 2, block-mode flow control
                case (char) 19: // XOFF, with XON is TERM=18 flow control
                case (char) 20: // Device control 4
                case (char) 21: // Negative acknowledge
                case (char) 22: // Synchronous idle
                case (char) 23: // End transmission block, not the same as EOT
                case (char) 24: // Cancel line, MPE echoes !!!
                case (char) 25: // End of medium, Control-Y interrupt
                    // (char) 26: // Substitute == EOI
                case (char) 27: // escape
                case (char) 28: // File Separator
                case (char) 29: // Group Separator
                case (char) 30: // Record Separator
                case (char) 31: // Unit Separator
                    if (ignoreControlChar) {
                        continue;
                    }
                    throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                case (char) 127: // del
                    if (ignoreControlChar) {
                        continue;
                    }
                    if (reject127) {
                        throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
                    }
                default:
                    sb.append(c);
            }
        }
    }

    private char readUnicode(int totalChars) {
        int value = 0;
        for (int i = 0; i < totalChars; i++) {
            value = value * 16;
            read();
            if (c <= '9' && c >= '0') {
                value += c - '0';
            } else if (c <= 'F' && c >= 'A') {
                value += (c - 'A') + 10;
            } else if (c >= 'a' && c <= 'f') {
                value += (c - 'a') + 10;
            } else if (c == EOI) {
                throw new ParseException(pos, ERROR_UNEXPECTED_EOF, "EOF");
            } else {
                throw new ParseException(pos, ERROR_UNEXPECTED_UNICODE, c);
            }
        }
        return (char) value;
    }

    private void skipDigits() {
        while (!(c < '0' || c > '9')) {
            read();
        }
    }

    private void skipNQString(boolean[] stop) {
        while (!(c == EOI || (c < MAX_STOP && stop[c]))) {
            read();
        }
    }

    private void skipSpace() {
        while (!(c > ' ' || c == EOI)) {
            read();
        }
    }

    private void readNQString(boolean[] stop) {
        int start = pos;
        skipNQString(stop);
        extractStringTrim(start, pos);
    }

    private Object readNumber(boolean[] stop) {
        int start = pos;
        // accept first char digit or -
        read();
        skipDigits();

        // Integer digit
        if (c != '.' && c != 'E' && c != 'e') {
            skipSpace();
            if (c < MAX_STOP && !stop[c] && c != EOI) {
                // convert string
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) {
                    throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                }
                return xs;
            }
            extractStringTrim(start, pos);
            return parseNumber(xs);
        }
        // floating point
        if (c == '.') {
            //
            read();
            skipDigits();
        }
        if (c != 'E' && c != 'e') {
            skipSpace();
            if (c < MAX_STOP && !stop[c] && c != EOI) {
                // convert string
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) {
                    throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                }
                return xs;
            }
            extractStringTrim(start, pos);
            return extractFloat();
        }
        sb.append('E');
        read();
        if (c == '+' || c == '-' || c >= '0' && c <= '9') {
            sb.append(c);
            read(); // skip first char
            skipDigits();
            skipSpace();
            if (c < MAX_STOP && !stop[c] && c != EOI) {
                // convert string
                skipNQString(stop);
                extractStringTrim(start, pos);
                if (!acceptNonQuote) {
                    throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
                }
                return xs;
            }
            extractStringTrim(start, pos);
            return extractFloat();
        } else {
            skipNQString(stop);
            extractStringTrim(start, pos);
            if (!acceptNonQuote) {
                throw new ParseException(pos, ERROR_UNEXPECTED_TOKEN, xs);
            }
            if (!acceptLeadinZero) {
                checkLeadingZero();
            }
            return xs;
        }
    }

    private void readString() {
        if (!acceptSimpleQuote && c == '\'') {
            if (acceptNonQuote) {
                readNQString(stopAll);
                return;
            }
            throw new ParseException(pos, ERROR_UNEXPECTED_CHAR, c);
        }
        int tmpP = json.indexOf(c, pos + 1);
        if (tmpP == -1) {
            throw new ParseException(len, ERROR_UNEXPECTED_EOF, null);
        }
        extractString(pos + 1, tmpP);
        if (xs.indexOf('\\') == -1) {
            checkControlChar();
            pos = tmpP;
            read();
            return;
        }
        sb.clear();
        readString2();
    }

    private void extractString(int beginIndex, int endIndex) {
        xs = json.substring(beginIndex, endIndex);
    }

    private void extractStringTrim(int start, int stop) {
        while (start < stop - 1 && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        while (stop - 1 > start && Character.isWhitespace(json.charAt(stop - 1))) {
            stop--;
        }
        extractString(start, stop);
    }

    /**
     * Read next char or END OF INPUT
     */
    private void read() {
        if (++pos >= len) {
            c = EOI;
        } else {
            c = json.charAt(pos);
        }
    }

    /**
     * read json can not be EOI
     */
    private void readNoEnd() {
        if (++pos >= len) {
            c = EOI;
            throw new ParseException(pos - 1, ERROR_UNEXPECTED_EOF, "EOF");
        } else {
            c = json.charAt(pos);
        }
    }

    private static class MSB {
        char[] b;
        int p;

        MSB(int size) {
            b = new char[size];
            p = -1;
        }

        void append(char c) {
            p++;
            if (b.length <= p) {
                char[] t = new char[b.length * 2 + 1];
                System.arraycopy(b, 0, t, 0, b.length);
                b = t;
            }
            b[p] = c;
        }

        public String toString() {
            return new String(b, 0, p + 1);
        }

        void clear() {
            p = -1;
        }
    }

}
