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

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TestDateConvert {
    // we do not test the century
    SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    SimpleDateFormat sdfLT = new SimpleDateFormat("dd/MM/yy HH:mm");

    @Test
    public void testDateFR() throws Exception {
        String expectedDateText = "23/01/12 13:42:12";
        ArrayList<String> tests = new ArrayList<>();
        tests.add("23 janvier 2012 13:42:12");
        tests.add("lundi 23 janvier 2012 13:42:12");
        tests.add("2012-01-23 13:42:12");
        // need to use the same time Zone
        // tests.add("Thu Jan 23 13:42:12 PST 2012");
        //
        // tests.add("Thu Jan 23 13:42:12 CET 2012");
        ConvertDate.convertToDate(null);
        for (String testDate : tests) {
            String jobName = "Parsing FR Date:" + testDate;
            Date parsed;
            try {
                parsed = ConvertDate.convertToDate(testDate);
            } catch (Exception e) {
                throw new Exception(jobName, e);
            }
            assertEquals(jobName, expectedDateText, sdfFull.format(parsed));
        }
    }

    @Test
    public void testAdvanceTimeStamp() {
        String testDate = "2014-08-27T12:53:10+02:00";
        Date date = ConvertDate.convertToDate(testDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String text = format.format(date);
        assertEquals("2014-08-27 12:53:10", text);
        // FIXME Is this Correct. It seems that TimeZone is ignored in ConvertDate.convertToDate.
    }

    @Test
    public void testDateUS() throws Exception {
        testDateLocalized(Locale.US);
    }

    @Test
    public void testDateFRANCE() throws Exception {
        testDateLocalized(Locale.FRANCE);
    }

    @Test
    public void testDateCANADA() throws Exception {
        testDateLocalized(Locale.CANADA);
    }

    @Test
    public void testDateGERMANY() throws Exception {
        testDateLocalized(Locale.GERMANY);
    }

    @Test
    public void testDateITALY() throws Exception {
        testDateLocalized(Locale.ITALY);
    }

    // MISSING JAPAN / CHINA

    public void testDateLocalized(Locale locale) throws Exception {
        // PM test
        fullTestDate(sdfFull.parse("23/01/2012 13:42:59"), locale);
        // AM test
        fullTestDate(sdfFull.parse("23/01/2012 01:42:59"), locale);
    }

    /**
     * Parse all JDK DateTimeFormat
     */
    public void fullTestDate(Date expectedDate, Locale locale) throws Exception {
        fullTestDate(expectedDate, locale, "SHORT", DateFormat.SHORT);
        fullTestDate(expectedDate, locale, "MEDIUM", DateFormat.MEDIUM);
        fullTestDate(expectedDate, locale, "LONG", DateFormat.LONG);
        fullTestDate(expectedDate, locale, "FULL", DateFormat.FULL);
    }

    public void fullTestDate(Date expectedDate, Locale locale, String sizeName, int sizeId) throws Exception {
        String jobName = "Test date format Local:" + locale + " format: " + sizeName;
        DateFormat format = DateFormat.getDateTimeInstance(sizeId, sizeId, locale);
        String testDate = format.format(expectedDate);
        Date parse;
        try {
            // can not parse US Date in short mode.
            if (sizeId == DateFormat.SHORT && locale.equals(Locale.US)) {
                return;
            }
            //parse = ConvertDate.convertToDate(obj)(testDate);
            else {
                parse = ConvertDate.convertToDate(testDate);
            }
        } catch (Exception e) {
            throw new Exception(jobName, e);
        }
        //System.err.println("TEST: " + testDate + " readed as: " + resultStr);
        // is source format contains second
        if (testDate.contains("59")) {
            String resultStr = sdfFull.format(parse);
            String expectedDateText = sdfFull.format(expectedDate);
            assertEquals(jobName, expectedDateText, resultStr);
        } else {
            String resultStr = sdfLT.format(parse);
            String expectedDateText = sdfLT.format(expectedDate);
            assertEquals(jobName, expectedDateText, resultStr);
        }
//			System.err.printf("no sec for Format %-6s %-40s -> %10s\n", sizeName, testDate, resultStr);
    }

}
