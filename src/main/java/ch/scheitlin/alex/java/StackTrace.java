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

/**
 * <p>Represents a java stack trace.</p>
 *
 * <p>The first line contains the error that happened and all following lines (stack trace
 * elements) indicate which pieces of code lead to this error.</p>
 */
public class StackTrace {
    /**
     * The first line of the stack trace containing the error that happened.
     */
    private String firstLine;

    /**
     * The stack trace lines of the stack trace indicating which pieces of code lead to the error mentioned at the first
     * line.
     */
    private List<StackTraceElement> stackTraceLines;

    /**
     * Creates a new instance of {@code StackTrace}.
     *
     * @param firstLine       the first line of the stack trace containing the error that happened
     * @param stackTraceLines the stack trace lines of the stack trace indicating which pieces of code lead to the error
     *                        mentioned at the first line
     */
    public StackTrace(String firstLine, List<StackTraceElement> stackTraceLines) {
        this.firstLine = firstLine;
        this.stackTraceLines = stackTraceLines;
    }

    /**
     * Gets the first line of the stack trace.
     *
     * @return the first line of the stack trace containing the error that happened
     */
    public String getFirstLine() {
        return this.firstLine;
    }

    /**
     * Gets the stack trace lines of the stack trace.
     *
     * @return the stack trace lines of the stack trace indicating which pieces of code lead to the error mentioned at
     * the first line
     */
    public List<StackTraceElement> getStackTraceLines() {
        return this.stackTraceLines;
    }

    /**
     * Returns the original stack trace.
     *
     * @return the original stack trace
     */
    public String getOriginalStackTrace() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.firstLine).append(System.lineSeparator());
        for (StackTraceElement element : stackTraceLines) {
            builder.append("\tat ").append(element).append(System.lineSeparator());
        }

        return builder.substring(0, builder.length() - 1);
    }

    /**
     * Returns all lines of the stack trace of the specified package.
     *
     * @param packageName the package name to get the stack trace lines from
     * @return a List of StackTraceElements of the specified package
     */
    public List<StackTraceElement> getLinesOfPackage(String packageName) {
        List<StackTraceElement> linesOfPackage = new ArrayList<StackTraceElement>();

        for (StackTraceElement line : this.stackTraceLines) {
            if (line.toString().startsWith(packageName)) {
                linesOfPackage.add(line);
            }
        }

        return linesOfPackage;
    }
}
