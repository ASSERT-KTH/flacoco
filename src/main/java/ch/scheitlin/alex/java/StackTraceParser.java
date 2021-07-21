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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides a function to parse a java stack trace represented as a {@code String} and map it to a {@code StackTrace}
 * object.
 */
public class StackTraceParser {
    // A typical stack trace element looks like follows:
    // com.myPackage.myClass.myMethod(myClass.java:1)
    // component        example             allowed signs
    // ---------------- ------------------- ------------------------------------------------------------
    // module name:     java.base           alphabetical / numbers
    // package name:    com.myPackage       alphabetical / numbers
    // class name:      myClass             alphabetical / numbers / $-sign for anonymous inner classes
    // method name:     myMethod            alphabetical / numbers / $-sign for lambda expressions
    // file name:       myClass.java        alphabetical / numbers
    // line number:     1                   integer

    // The following lines show some example stack trace elements:
    // java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)   // stack trace element with module name
    // org.junit.Assert.fail(Assert.java:86)                                            // typical stack trace element
    // sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)                      // native method
    // org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)                  // anonymous inner classes
    // org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)                  // lambda expressions
    // org.apache.maven.surefire.junit4.JUnit4TestSet.execute(JUnit4TestSet.java:53)    // numbers for package and class names

    // Using the predefined structure of a stack trace element and allowed signs for its components, the following
    // regular expression can be used to parse stack trace elements and it's components. Parentheses ('(', ')') are used
    // to extract the components and '?:' is used to group the signs but not creating capture groups. Additionally, the
    // typical stack trace output has a leading tab and 'at ' before the stack trace element.

    // ^\tat (?:((?:[\d\w]*\.)*[\d\w]*)\/)?((?:(?:[\d\w]*\.)*[\d\w]*))\.([\d\w\$]*)\.([\d\w\$]*)\((?:(?:([\d\w]*\.java):(\d*))|([\d\w\s]*))\)$
    // group 1: module
    // group 2: package
    // group 3: class name
    // group 4: method name
    // group 5: file name | null
    // group 6: line number | null
    // group 7: null | string
    private static String LINE_SEPARATOR_REGEX = "\\r?\\n|\\r";
    private static String STACK_TRACE_LINE_REGEX = "^\\tat (?:((?:[\\d\\w]*\\.)*[\\d\\w]*)\\/)?((?:(?:[\\d\\w]*\\.)*[\\d\\w]*))\\.([\\d\\w\\$]*)\\.([\\d\\w\\$]*)\\((?:(?:([\\d\\w]*\\.java):(\\d*))|([\\d\\w\\s]*))\\)$";
    private static Pattern STACK_TRACE_LINE_PATTERN = Pattern.compile(STACK_TRACE_LINE_REGEX);

    /**
     * Reads a java stack trace represented as a {@code List} of {@code String}s and maps it to a {@code StackTrace}
     * object with {@code java.lang.StackTraceElement}s.
     *
     * @param stackTraceLines the java stack trace as a {@code List} of {@code String}s
     * @return a StackTrace containing the first (error) line and a list of {@code StackTraceElements}
     * @throws Exception if a stack trace line could not be parsed to a {@code java.lang.StackTraceElement}
     */
    public static StackTrace parse(List<String> stackTraceLines) throws Exception {
        StringBuilder builder = new StringBuilder();

        for (String line : stackTraceLines) {
            builder.append(line).append(System.lineSeparator());
        }

        return parse(builder.substring(0, builder.length() - 1));
    }

    /**
     * Reads a java stack trace represented as a {@code String} and maps it to a {@code StackTrace} object with
     * {@code java.lang.StackTraceElement}s.
     *
     * @param stackTraceString the java stack trace as a {@code String}
     * @return a StackTrace containing the first (error) line and a list of {@code StackTraceElements}
     * @throws Exception if a stack trace line could not be parsed to a {@code java.lang.StackTraceElement}
     */
    public static StackTrace parse(String stackTraceString) throws Exception {
        String[] lines = stackTraceString.split(LINE_SEPARATOR_REGEX);

        String firstLine = lines[0];
        List<StackTraceElement> stackTraceLines = new ArrayList<StackTraceElement>();

        for (int i = 1; i < lines.length; i++) {
            Matcher matcher = STACK_TRACE_LINE_PATTERN.matcher(lines[i]);

            if (matcher.matches()) {
                // pass null if no module information is available
                String moduleName = null;
                if (matcher.group(1) != null) {
                    moduleName = matcher.group(1);
                }

                String packageName = matcher.group(2);
                String className = matcher.group(3);
                String methodName = matcher.group(4);

                // pass null if no file information is available
                String fileName = null;
                if (matcher.group(5) != null) {
                    fileName = matcher.group(5);
                }

                // pass -1 if no line number information is available
                int lineNumber = -1;
                if (matcher.group(6) != null) {
                    lineNumber = Integer.valueOf(matcher.group(6));
                }

                // pass -2 as if the method containing the execution point is a native method
                if (matcher.group(7) != null && matcher.group(7).equals("Native Method")) {
                    lineNumber = -2;
                }

                StackTraceElement element = new StackTraceElement(
                        (moduleName != null ? moduleName + "/" : "") + packageName + "." + className,
                        methodName,
                        fileName,
                        lineNumber
                );

                // check whether the parsed stack trace element corresponds to the original one
                if (!("\tat " + element.toString()).equals(lines[i])) {
                    throw new Exception("ERROR: Stack trace line could not be parsed to StackTraceElement:\n" +
                            "\tOriginal stack trace line:\t" + lines[i] + "\n" +
                            "\tParsed StackTraceElement:\t" + "\tat " + element.toString());
                }

                stackTraceLines.add(element);
            }
        }

        return new StackTrace(firstLine, stackTraceLines);
    }
}
