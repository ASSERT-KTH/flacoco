/**
 * MIT License
 *
 * Copyright (c) 2018 Alex Scheitlin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.scheitlin.alex.java;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class StackTraceParserTest {

    private static String LINE_SEPARATOR_REGEX = "\\r?\\n|\\r";

    @Test
    public void parse_lines() {
        // assign variables with test data
        String expectedStackTrace = getDummyStackTrace();

        // execute methods to be tested
        List<String> stackTraceLines = Arrays.asList(expectedStackTrace.split(LINE_SEPARATOR_REGEX));
        String actualStackTrace;
        try {
            actualStackTrace = StackTraceParser.parse(stackTraceLines).getOriginalStackTrace();
        } catch (Exception e) {
            fail("Could not parse the stack trace!");
            return;
        }

        // assign result
        Assert.assertArrayEquals(expectedStackTrace.split(LINE_SEPARATOR_REGEX), actualStackTrace.split(LINE_SEPARATOR_REGEX));
    }

    @Test
    public void parse_string() {
        // assign variables with test data
        String expectedStackTrace = getDummyStackTrace();

        // execute methods to be tested
        String actualStackTrace;
        try {
            actualStackTrace = StackTraceParser.parse(expectedStackTrace).getOriginalStackTrace();
        } catch (Exception e) {
            fail("Could not parse the stack trace!");
            return;
        }

        // assign result
        Assert.assertArrayEquals(expectedStackTrace.split(LINE_SEPARATOR_REGEX), actualStackTrace.split(LINE_SEPARATOR_REGEX));
    }

    private String getDummyStackTrace() {
        try {
            return TestResourceReader.readResourceFile("dummyStackTrace.txt");
        } catch (FileNotFoundException e) {
            fail("Could not read test resource file!");
            return null;
        }
    }
}