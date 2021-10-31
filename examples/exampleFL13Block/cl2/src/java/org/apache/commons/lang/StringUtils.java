/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.lang;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <p>Common <code>String</code> manipulation routines.</p>
 *
 * <p>Originally from 
 * <a href="http://jakarta.apache.org/turbine/">Turbine</a> and the
 * GenerationJavaCore library.</p>
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:gcoladonato@yahoo.com">Greg Coladonato</a>
 * @author <a href="mailto:bayard@generationjava.com">Henri Yandell</a>
 * @author <a href="mailto:ed@apache.org">Ed Korthof</a>
 * @author <a href="mailto:rand_mcneely@yahoo.com">Rand McNeely</a>
 * @author Stephen Colebourne
 * @author <a href="mailto:fredrik@westermarck.com">Fredrik Westermarck</a>
 * @author Holger Krauth
 * @author <a href="mailto:alex@purpletech.com">Alexander Day Chaffee</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author Arun Mammen Thomas
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @since 1.0
 * @version $Id: StringUtils.java,v 1.47 2003/06/21 22:24:55 bayard Exp $
 */
public class StringUtils {

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static int PAD_LIMIT = 8192;

    /**
     * <p>A <code>String</code> containing all blank characters.</p>
     *
     * <p>Used for efficient blank padding.  The length of the string expands as needed.</p>
     */
    private static String blanks = new String(" ");

    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     *
     * <p>Used for efficient blank padding.  The length of each string expands as needed.</p>
     */
    private final static String[] padding = new String[Character.MAX_VALUE];
       // String.concat about twice as fast as StringBuffer.append

    /**
     * <p><code>StringUtils<code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>StringUtils.trim(" foo ");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StringUtils() {
    }

    // Empty
    //--------------------------------------------------------------------------

    /**
     * <p>Removes control characters, including whitespace, from both
     * ends of this String, handling <code>null</code> by returning
     * an empty String.</p>
     * 
     * <pre>
     * StringUtils.clean("abc")         = "abc"
     * StringUtils.clean("    abc    ") = "abc"
     * StringUtils.clean("     ")       = ""
     * StringUtils.clean("")            = ""
     * StringUtils.clean(null)          = ""
     * </pre>
     *
     * @see java.lang.String#trim()
     * @param str the String to check
     * @return the trimmed text (never <code>null</code>)
     * @deprecated Use the clearer named {@link #trimToEmpty(String)}.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String clean(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * <p>Removes control characters, including whitespace, from both
     * ends of this String, handling <code>null</code> by returning
     * <code>null</code>.</p>
     * 
     * <p>The string is trimmed using {@link String#trim()}.</p>
     * 
     * <pre>
     * StringUtils.trim("abc")         = "abc"
     * StringUtils.trim("    abc    ") = "abc"
     * StringUtils.trim("     ")       = ""
     * StringUtils.trim("")            = ""
     * StringUtils.trim(null)          = null
     * </pre>
     *
     * @see java.lang.String#trim()
     * @param str the String to be trimmed
     * @return the trimmed text (or <code>null</code>)
     */
    public static String trim(String str) {
        return (str == null ? null : str.trim());
    }

    /** 
     * <p>Removes control characters, including whitespace, from both  
     * ends of this string returning <code>null</code> if the string is 
     * empty after the trim or if it is <code>null</code>.
     * 
     * <p>The string is trimmed using {@link String#trim()}.</p>
     * 
     * <pre>
     * StringUtils.trimToNull("abc")         = "abc"
     * StringUtils.trimToNull("    abc    ") = "abc"
     * StringUtils.trimToNull("     ")       = null
     * StringUtils.trimToNull("")            = null
     * StringUtils.trimToNull(null)          = null
     * </pre>
     *  
     * @see java.lang.String#trim()
     * @param str the String to be trimmed.
     * @return the trimmed string, or null if it's empty or null
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return (ts == null || ts.length() == 0 ? null : ts);
    }

    /** 
     * <p>Removes control characters, including whitespace, from both 
     * ends of this string returning an empty string if the string is
     * empty after the trim or if it is <code>null</code>.
     * 
     * <p>The string is trimmed using {@link String#trim()}.</p>
     * 
     * <pre>
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty(null)          = ""
     * </pre>
     *  
     * @see java.lang.String#trim()
     * @param str the String to be trimmed
     * @return the trimmed string, or an empty string if it's empty or null
     */
    public static String trimToEmpty(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * <p>Deletes all 'space' characters from a String.</p>
     *
     * <p>Spaces are defined as <code>{' ', '\t', '\r', '\n', '\b'}</code>
     * in line with the deprecated {@link Character#isSpace(char)}.</p>
     *
     * @param str String target to delete spaces from
     * @return the String without spaces
     * @throws NullPointerException
     */
    public static String deleteSpaces(String str) {
        return CharSetUtils.delete(str, " \t\r\n\b");
    }

    /**
     * <p>Deletes all whitespaces from a String.</p>
     *
     * <p>Whitespace is defined by
     * {@link Character#isWhitespace(char)}.</p>
     *
     * @param str String target to delete whitespace from
     * @return the String without whitespaces
     * @throws NullPointerException
     */
    public static String deleteWhitespace(String str) {
        StringBuffer buffer = new StringBuffer();
        int sz = str.length();
        for (int i=0; i<sz; i++) {
            if(!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            }
        }
        return buffer.toString();
    }

    /**
     * <p>Checks if a String is non <code>null</code> and is
     * not empty (<code>length > 0</code>).</p>
     *
     * @param str the String to check
     * @return true if the String is non-null, and not length zero
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * <p>Checks if a (trimmed) String is <code>null</code> or empty.</p>
     *
     * @param str the String to check
     * @return <code>true</code> if the String is <code>null</code>, or
     *  length zero once trimmed
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    // Equals and IndexOf
    //--------------------------------------------------------------------------

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * @see java.lang.String#equals(Object)
     * @param str1 the first string
     * @param str2 the second string
     * @return <code>true</code> if the Strings are equal, case sensitive, or
     *  both <code>null</code>
     */
    public static boolean equals(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
     * the case.</p>
     *
     * <p><code>Nulls</code> are handled without exceptions. Two <code>null</code>
     * references are considered equal. Comparison is case insensitive.</p>
     *
     * @see java.lang.String#equalsIgnoreCase(String)
     * @param str1  the first string
     * @param str2  the second string
     * @return <code>true</code> if the Strings are equal, case insensitive, or
     *  both <code>null</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
    }

    /**
     * <p>Find the first index of any of a set of potential substrings.</p>
     *
     * <p><code>null</code> String will return <code>-1</code>.</p>
     * 
     * @param str the String to check
     * @param searchStrs the Strings to search for
     * @return the first index of any of the searchStrs in str
     * @throws NullPointerException if any of searchStrs[i] is <code>null</code>
     */
    public static int indexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;

        // String's can't have a MAX_VALUEth index.
        int ret = Integer.MAX_VALUE;

        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            tmp = str.indexOf(searchStrs[i]);
            if (tmp == -1) {
                continue;
            }

            if (tmp < ret) {
                ret = tmp;
            }
        }

        return (ret == Integer.MAX_VALUE) ? -1 : ret;
    }

    /**
     * <p>Find the latest index of any of a set of potential substrings.</p>
     *
     * <p><code>null</code> string will return <code>-1</code>.</p>
     * 
     * @param str  the String to check
     * @param searchStrs  the Strings to search for
     * @return the last index of any of the Strings
     * @throws NullPointerException if any of searchStrs[i] is <code>null</code>
     */
    public static int lastIndexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }
        int sz = searchStrs.length;
        int ret = -1;
        int tmp = 0;
        for (int i = 0; i < sz; i++) {
            tmp = str.lastIndexOf(searchStrs[i]);
            if (tmp > ret) {
                ret = tmp;
            }
        }
        return ret;
    }

    // Substring
    //--------------------------------------------------------------------------
    
    /**
     * <p>Gets a substring from the specified string avoiding exceptions.</p>
     *
     * <p>A negative start position can be used to start <code>n</code>
     * characters from the end of the String.</p>
     * 
     * @param str the String to get the substring from
     * @param start the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @return substring from start position
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }

        return str.substring(start);
    }
    
    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     *
     * <p>A negative start position can be used to start/end <code>n</code>
     * characters from the end of the String.</p>
     * 
     * @param str the String to get the substring from
     * @param start the position to start from, negative means
     *  count back from the end of the string by this many characters
     * @param end the position to end at (exclusive), negative means
     *  count back from the end of the String by this many characters
     * @return substring from start position to end positon
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            // check this works.
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * <p>Gets the leftmost <code>n</code> characters of a String.</p>
     *
     * <p>If <code>n</code> characters are not available, or the
     * String is <code>null</code>, the String will be returned without
     * an exception.</p>
     *
     * @param str the String to get the leftmost characters from
     * @param len the length of the required String
     * @return the leftmost characters
     * @throws IllegalArgumentException if len is less than zero
     */
    public static String left(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if ((str == null) || (str.length() <= len)) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    /**
     * <p>Gets the rightmost <code>n</code> characters of a String.</p>
     *
     * <p>If <code>n</code> characters are not available, or the String
     * is <code>null</code>, the String will be returned without an
     * exception.</p>
     *
     * @param str the String to get the rightmost characters from
     * @param len the length of the required String
     * @return the leftmost characters
     * @throws IllegalArgumentException if len is less than zero
     */
    public static String right(String str, int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if ((str == null) || (str.length() <= len)) {
            return str;
        } else {
            return str.substring(str.length() - len);
        }
    }

    /**
     * <p>Gets <code>n</code> characters from the middle of a String.</p>
     *
     * <p>If <code>n</code> characters are not available, the remainder
     * of the String will be returned without an exception. If the
     * String is <code>null</code>, <code>null</code> will be returned.</p>
     *
     * @param str the String to get the characters from
     * @param pos the position to start from
     * @param len the length of the required String
     * @return the leftmost characters
     * @throws IndexOutOfBoundsException if pos is out of bounds
     * @throws IllegalArgumentException if len is less than zero
     */
    public static String mid(String str, int pos, int len) {
        if ((pos < 0) ||
            (str != null && pos > str.length())) {
            throw new StringIndexOutOfBoundsException("String index " + pos + " is out of bounds");
        }
        if (len < 0) {
            throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        }
        if (str == null) {
            return null;
        }
        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    // Splitting
    //--------------------------------------------------------------------------
    
    /**
     * <p>Splits the provided text into a array, using whitespace as the
     * separator.</p>
     *
     * <p>The separator is not included in the returned String array.</p>
     *
     * @param str the String to parse
     * @return an array of parsed Strings 
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * @see #split(String, String, int)
     */
    public static String[] split(String text, String separator) {
        return split(text, separator, -1);
    }

    /**
     * <p>Splits the provided text into a array, based on a given separator.</p>
     *
     * <p>The separator is not included in the returned String array. The
     * maximum number of splits to perfom can be controlled. A <code>null</code>
     * separator will cause parsing to be on whitespace.</p>
     *
     * <p>This is useful for quickly splitting a String directly into
     * an array of tokens, instead of an enumeration of tokens (as
     * <code>StringTokenizer</code> does).</p>
     *
     * @param str The string to parse.
     * @param separator Characters used as the delimiters. If
     *  <code>null</code>, splits on whitespace.
     * @param max The maximum number of elements to include in the
     *  array.  A zero or negative value implies no limit.
     * @return an array of parsed Strings 
     */
    public static String[] split(String str, String separator, int max) {
        StringTokenizer tok = null;
        if (separator == null) {
            // Null separator means we're using StringTokenizer's default
            // delimiter, which comprises all whitespace characters.
            tok = new StringTokenizer(str);
        } else {
            tok = new StringTokenizer(str, separator);
        }

        int listSize = tok.countTokens();
        if (max > 0 && listSize > max) {
            listSize = max;
        }

        String[] list = new String[listSize];
        int i = 0;
        int lastTokenBegin = 0;
        int lastTokenEnd = 0;
        while (tok.hasMoreTokens()) {
            if (max > 0 && i == listSize - 1) {
                // In the situation where we hit the max yet have
                // tokens left over in our input, the last list
                // element gets all remaining text.
                String endToken = tok.nextToken();
                lastTokenBegin = str.indexOf(endToken, lastTokenEnd);
                list[i] = str.substring(lastTokenBegin);
                break;
            } else {
                list[i] = tok.nextToken();
                lastTokenBegin = str.indexOf(list[i], lastTokenEnd);
                lastTokenEnd = lastTokenBegin + list[i].length();
            }
            i++;
        }
        return list;
    }

    // Joining
    //--------------------------------------------------------------------------
    /**
     * <p>Concatenates elements of an array into a single String.</p>
     *
     * <p>The difference from join is that concatenate has no delimiter.</p>
     * 
     * @param array the array of values to concatenate.
     * @return the concatenated string.
     */
    public static String concatenate(Object[] array) {
        return join(array, null);
    }
    
    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list. A
     * <code>null</code> separator is the same as a blank String.</p>
     *
     * @param array the array of values to join together
     * @param separator the separator character to use
     * @return the joined String
     */
    public static String join(Object[] array, String separator) {
        int arraySize = array.length;

        // ArraySize ==  0: Len = 0
        // ArraySize > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all strings are roughly equally long)
        int bufSize 
            = ((arraySize == 0) ? 0 
                : arraySize * (array[0].toString().length() 
                    + ((separator != null) ? separator.length(): 0)));

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if ((separator != null) && (i > 0)) {
                buf.append(separator);
         }
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list. A
     *
     * @param array the array of values to join together
     * @param separator the separator character to use
     * @return the joined String
     */
    public static String join(Object[] array, char separator) {
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : (array[0].toString().length() + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided <code>Iterator</code> into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list. A
     * <code>null</code> separator is the same as a blank String.</p>
     *
     * @param iterator the <code>Iterator</code> of values to join together
     * @param separator  the separator character to use
     * @return the joined String
     */
    public static String join(Iterator iterator, String separator) {
        StringBuffer buf = new StringBuffer(256);  // Java default is 16, probably too small
        while (iterator.hasNext()) {
            buf.append(iterator.next());
            if ((separator != null) && iterator.hasNext()) {
                buf.append(separator);
            }
         }
        return buf.toString();
    }

    /**
     * <p>Joins the elements of the provided <code>Iterator</code> into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list. A
     *
     * @param iterator the <code>Iterator</code> of values to join together
     * @param separator  the separator character to use
     * @return the joined String
     */
    public static String join(Iterator iterator, char separator) {
        StringBuffer buf = new StringBuffer(256);  // Java default is 16, probably too small
        while (iterator.hasNext()) {
            buf.append(iterator.next());
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }



    // Replacing
    //--------------------------------------------------------------------------
    
    /**
     * <p>Replace a String with another String inside a larger String, once.</p>
     * 
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     * 
     * @see #replace(String text, String repl, String with, int max)
     * @param text text to search and replace in
     * @param repl String to search for
     * @param with String to replace with
     * @return the text with any replacements processed
     */
    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    /**
     * <p>Replace all occurances of a String within another String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     * 
     * @see #replace(String text, String repl, String with, int max)
     * @param text text to search and replace in
     * @param repl String to search for
     * @param with String to replace with
     * @return the text with any replacements processed
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    /**
     * <p>Replace a String with another String inside a larger String,
     * for the first <code>max</code> values of the search String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * @param text text to search and replace in
     * @param repl String to search for
     * @param with String to replace with
     * @param max maximum number of values to replace, or <code>-1</code> if no maximum
     * @return the text with any replacements processed
     */
    public static String replace(String text, String repl, String with, int max) {
        if (text == null || repl == null || with == null || repl.length() == 0) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * <p>Overlay a part of a String with another String.</p>
     *
     * @param text String to do overlaying in
     * @param overlay String to overlay
     * @param start int to start overlaying at
     * @param end int to stop overlaying before
     * @return String with overlayed text
     * @throws NullPointerException if text or overlay is <code>null</code>
     */
    public static String overlayString(String text, String overlay, int start, int end) {
        return new StringBuffer(start + overlay.length() + text.length() - end + 1)
            .append(text.substring(0, start))
            .append(overlay)
            .append(text.substring(end))
            .toString();
    }

    // Centering
    //--------------------------------------------------------------------------
    
    /**
     * <p>Center a String in a larger String of size <code>n</code>.<p>
     *
     * <p>Uses spaces as the value to buffer the String with.
     * Equivalent to <code>center(str, size, " ")</code>.</p>
     *
     * @param str String to center
     * @param size int size of new String
     * @return String containing centered String
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String center(String str, int size) {
        return center(str, size, " ");
    }

    /**
     * <p>Center a String in a larger String of size <code>n</code>.</p>
     *
     * <p>Uses a supplied String as the value to buffer the String with.</p>
     *
     * @param str String to center
     * @param size int size of new String
     * @param delim String to buffer the new String with
     * @return String containing centered String
     * @throws NullPointerException if str or delim is <code>null</code>
     * @throws ArithmeticException if delim is the empty String
     */
    public static String center(String str, int size, String delim) {
        int sz = str.length();
        int p = size - sz;
        if (p < 1) {
            return str;
        }
        str = leftPad(str, sz + p / 2, delim);
        str = rightPad(str, size, delim);
        return str;
    }

    // Chomping
    //--------------------------------------------------------------------------

    /**
     * <p>Remove one newline from end of a String if it's there,
     * otherwise leave it alone.  A newline is "\n", "\r", or "\r\n".
     * <p>
     * Note that this behavior has changed from 1.0.  It
     * now more closely matches Perl chomp.  For the previous behavior,
     * use slice(String).
     *
     * @param str String to chomp a newline from
     * @return String without newline
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String chomp(String str) {
        if (str.length() == 0) {
            return str;
        }

        if (str.length() == 1) {
            if ("\r".equals(str) || "\n".equals(str)) {
                return "";
            }
            else {
                return str;
            }
        }

        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);

        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                lastIdx--;
            }
        } else if (last == '\r') {

        } else {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }

    /**
     * <p>Remove one string (the separator) from the end of another
     * string if it's there, otherwise leave it alone.
     * <p>
     *
     * Note that this behavior has changed from 1.0.  It
     * now more closely matches Perl chomp.  For the previous behavior,
     * use {@link #slice(String,String)}.
     *
     * @param str string to chomp from
     * @param separator separator string
     * @return String without trailing separator
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String chomp(String str, String separator) {
        if (str.length() == 0) {
            return str;
        }
        if (str.endsWith(separator)) {
            return str.substring(0, str.length() - separator.length());
        }
        return str;
    }

    /**
     * <p>Remove a newline if and only if it is at the end
     * of the supplied String.</p>
     * 
     * @param str String to chomp from
     * @return String without chomped ending
     * @throws NullPointerException if str is <code>null</code>
     * @deprecated Use {@link #chomp(String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String chompLast(String str) {
        return chompLast(str, "\n");
    }
    
    /**
     * <p>Remove a value if and only if the String ends with that value.</p>
     * 
     * @param str String to chomp from
     * @param sep String to chomp
     * @return String without chomped ending
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #chomp(String,String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String chompLast(String str, String sep) {
        if (str.length() == 0) {
            return str;
        }
        String sub = str.substring(str.length() - sep.length());
        if (sep.equals(sub)) {
            return str.substring(0, str.length() - sep.length());
        } else {
            return str;
        }
    }

    /** 
     * <p>Remove everything and return the last value of a supplied String, and
     * everything after it from a String.
     * [That makes no sense. Just use sliceRemainder() :-)]</p>
     *
     * @param str String to chomp from
     * @param sep String to chomp
     * @return String chomped
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #sliceRemainder(String,String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String getChomp(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        if (idx == str.length() - sep.length()) {
            return sep;
        } else if (idx != -1) {
            return str.substring(idx);
        } else {
            return "";
        }
    }

    /** 
     * <p>Remove the first value of a supplied String, and everything before it
     * from a String.</p>
     *
     * @param str String to chomp from
     * @param sep String to chomp
     * @return String without chomped beginning
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #sliceFirstRemainder(String,String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String prechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx != -1) {
            return str.substring(idx + sep.length());
        } else {
            return str;
        }
    }

    /** 
     * <p>Remove and return everything before the first value of a
     * supplied String from another String.</p>
     *
     * @param str String to chomp from
     * @param sep String to chomp
     * @return String prechomped
     * @throws NullPointerException if str or sep is <code>null</code>
     * @deprecated Use {@link #sliceFirst(String,String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String getPrechomp(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx != -1) {
            return str.substring(0, idx + sep.length());
        } else {
            return "";
        }
    }

    // Chopping
    //--------------------------------------------------------------------------
    
    /**
     * <p>Remove the last character from a String.</p>
     *
     * <p>If the String ends in <code>\r\n</code>, then remove both
     * of them.</p>
     *
     * @param str String to chop last character from
     * @return String without last character
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String chop(String str) {
        if ("".equals(str)) {
            return "";
        }
        if (str.length() == 1) {
            return "";
        }
        int lastIdx = str.length() - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (ret.charAt(lastIdx - 1) == '\r') {
                return ret.substring(0, lastIdx - 1);
            }
        }
        return ret;
    }

    /**
     * <p>Remove <code>\n</code> from end of a String if it's there.
     * If a <code>\r</code> precedes it, then remove that too.</p>
     *
     * @param str String to chop a newline from
     * @return String without newline
     * @throws NullPointerException if str is <code>null</code>
     * @deprecated Use {@link #chomp(String)} instead.
     *             Method will be removed in Commons Lang 3.0.
     */
    public static String chopNewline(String str) {
        int lastIdx = str.length() - 1;
        if (lastIdx == 0) {
            return "";
        }
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                lastIdx--;
            }
        } else {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }


    // Slicing
    //--------------------------------------------------------------------------

    /**
     * <p>Remove the last newline, and everything after it from a String.</p>
     * (This method was formerly named chomp or chopNewline.)
     *
     * @param str String to slice the newline from
     * @return String without sliced newline
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String slice(String str) {
        return slice(str, "\n");
    }

    /**
     * <p>Find the last occurence of a separator String;
     * remove it and everything after it.</p>
     * (This method was formerly named chomp.)
     *
     * @param str String to slice from
     * @param sep String to slice
     * @return String without sliced ending
     * @throws NullPointerException if str or sep is <code>null</code>
     */
    public static String slice(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        if (idx != -1) {
            return str.substring(0, idx);
        } else {
            return str;
        }
    }

    /**
     * <p>Find the last occurence of a separator String, and return
     * everything after it.</p>
     * (This method was formerly named getchomp. Also, now it does not
     * include the separator in the return value.)
     *
     * @param str String to slice from
     * @param sep String to slice
     * @return String sliced
     * @throws NullPointerException if str or sep is <code>null</code>
     */
    public static String sliceRemainder(String str, String sep) {
        int idx = str.lastIndexOf(sep);
        if (idx == str.length() - sep.length()) {
            return "";
        } else if (idx != -1) {
            return str.substring(idx + sep.length());
        } else {
            return "";
        }
    }

    /**
     * <p>Find the first occurence of a separator String, and return
     * everything after it.</p>
     * (This method was formerly named prechomp.  Also, previously
     * it included the separator in the return value; now it does not.)
     *
     * @param str String to slice from
     * @param sep String to slice
     * @return String without sliced beginning
     * @throws NullPointerException if str or sep is <code>null</code>
     */
    public static String sliceFirstRemainder(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx != -1) {
            return str.substring(idx + sep.length());
        } else {
            return str;
        }
    }

    /**
     * <p>Find the first occurence of a separator string;
     * return everything before it (but not including the separator).</p>
     * (This method was formerly named getPrechomp.  Also, it used to
     * include the separator, but now it does not.)
     *
     * @param str String to slice from
     * @param sep String to slice
     * @return String presliced
     * @throws NullPointerException if str or sep is <code>null</code>
     */
    public static String sliceFirst(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx != -1) {
            return str.substring(0, idx);
        } else {
            return "";
        }
    }

    // Conversion
    //--------------------------------------------------------------------------
    
    // spec 3.10.6
    /**
     * <p>Escapes any values it finds into their String form.</p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>As of Lang 2.0, this calls {@link StringEscapeUtils#escapeJava(java.lang.String)}
     * behind the scenes.  For convenience, this method is not deprecated.
     * </p>
     * @see StringEscapeUtils#escapeJava(java.lang.String)
     * @param str String to escape values in
     * @return String with escaped values
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String escape(String str) {
        return StringEscapeUtils.escapeJava(str);
    }

    /**
     * Unescapes any Java literals found in the String. For example, 
     * it will turn a sequence of '\' and 'n' into a newline character, 
     * unless the '\' is preceded by another '\'.
     * <p>
     * As of Lang 2.0, this calls {@link StringEscapeUtils#unescapeJava(java.lang.String)}
     * behind the scenes.  For convenience, this method is not deprecated.
     * <p>
     * @see StringEscapeUtils#unescapeJava(java.lang.String)
     */
    public static String unescape(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }

    // Padding
    //--------------------------------------------------------------------------
    
    /**
     * <p>Repeat a String <code>n</code> times to form a
     * new string.</p>
     *
     * @param str String to repeat
     * @param repeat number of times to repeat str
     * @return String with repeated String
     * @throws NegativeArraySizeException if <code>repeat < 0</code>
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String repeat(String str, int repeat) {
        if (str.length() == 1 && repeat <= PAD_LIMIT) {
           return padding(repeat, str.charAt(0));
        }

        StringBuffer buffer = new StringBuffer(repeat * str.length());
        for (int i = 0; i < repeat; i++) {
            buffer.append(str);
        }
        return buffer.toString();
    }

    /**
     * <p>Returns blank padding with a given length.</p>
     *
     * @param repeat number of times to repeat a blank
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if repeat < 0
     */
    private static String padding(int repeat) {
        while (blanks.length() < repeat)  {
            blanks = blanks.concat(blanks);
        }
        return blanks.substring(0, repeat);
    }

    /**
     * <p>Returns padding using the specified delimiter repeated to a given length.
     * </p>
     *
     * @param repeat number of times to repeat delim
     * @param delim character to repeat
     * @return String with repeated character
     * @throws NullPointerException if delim is <code>null</code>
     * @throws IndexOutOfBoundsException if repeat < 0
     */

    private static String padding(int repeat, char delim) {
        if (padding[delim] == null) {
            padding[delim] = String.valueOf(delim);
        }
        while (padding[delim].length() < repeat) {
            padding[delim] = padding[delim].concat(padding[delim]);
        }
        return padding[delim].substring(0, repeat);
    }

    /**
     * <p>Right pad a String with spaces.</p>
     *
     * <p>The String is padded to the size of <code>n</code>.</p>
     * 
     * @param str String to pad out
     * @param size number of times to repeat str
     * @return right padded String or original String if no padding is necessary
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String rightPad(String str, int size) {
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original string when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, " ");
        }
        return str + padding(pads);
    }

    /**
     * <p>Right pad a String with a specified character.</p>
     *
     * <p>The String is padded to the size of <code>n</code>.</p>
     *
     * @param str String to pad out
     * @param size size to pad to
     * @param delim character to pad with
     * @return right padded String or original String if no padding is necessary
     * @throws NullPointerException if str or delim is <code>null<code>
     */
    public static String rightPad(String str, int size, char delim) {
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original string when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(delim));
        }
        return str + padding(pads, delim);
    }

    /**
     * <p>Right pad a String with a specified string.</p>
     *
     * <p>The String is padded to the size of <code>n</code>.</p>
     *
     * @param str String to pad out
     * @param size size to pad to
     * @param delim String to pad with
     * @return right padded String or original String if no padding is necessary
     * @throws NullPointerException if str or delim is <code>null<code>
     * @throws ArithmeticException if delim is the empty String
     */
    public static String rightPad(String str, int size, String delim) {
        if (delim.length() == 1 && size - str.length() <= PAD_LIMIT) {
           return rightPad(str, size, delim.charAt(0));
        }

        size = (size - str.length()) / delim.length();
        if (size > 0) {
            str += repeat(delim, size);
        }
        return str;
    }

    /**
     * <p>Left pad a String with spaces.</p>
     *
     * <p>The String is padded to the size of <code>n<code>.</p>
     *
     * @param str String to pad out
     * @param size size to pad to
     * @return left padded String or original String if no padding is necessary
     * @throws NullPointerException if str or delim is <code>null<code>
     */
    public static String leftPad(String str, int size) {
        int pads = size - str.length();
        if (pads <= 0) { 
            return str; // returns original string when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, " ");
        }
        return padding(pads).concat(str);
    }

    /**
     * Left pad a String with a specified character. Pad to a size of n.
     *
     * @param str String to pad out
     * @param size size to pad to
     * @param delim character to pad with
     * @return left padded String or original String if no padding is necessary
     * @throws NullPointerException if str or delim is <code>null</code>
     */
    public static String leftPad(String str, int size, char delim) {
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original string when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, " ");
        }
        return padding(pads, delim).concat(str);
    }

    /**
     * Left pad a String with a specified string. Pad to a size of n.
     *
     * @param str String to pad out
     * @param size size to pad to
     * @param delim String to pad with
     * @return left padded String or original String if no padding is necessary
     * @throws NullPointerException if str or delim is null
     * @throws ArithmeticException if delim is the empty string
     */
    public static String leftPad(String str, int size, String delim) {
        if (delim.length() == 1 && size - str.length() <= PAD_LIMIT)
           return leftPad(str, size, delim.charAt(0));
        size = (size - str.length()) / delim.length();
        if (size > 0) {
            str = repeat(delim, size) + str;
        }
        return str;
    }

    // Stripping
    //--------------------------------------------------------------------------
    
    /**
     * <p>Remove whitespace from the front and back of a String.</p>
     * 
     * @param str the String to remove whitespace from
     * @return the stripped String
     */
    public static String strip(String str) {
        return strip(str, null);
    }
    /**
     * <p>Remove a specified String from the front and back of a
     * String.</p>
     *
     * <p>If whitespace is wanted to be removed, used the
     * {@link #strip(java.lang.String)} method.</p>
     * 
     * @param str the String to remove a string from
     * @param delim the String to remove at start and end
     * @return the stripped String
     */
    public static String strip(String str, String delim) {
        str = stripStart(str, delim);
        return stripEnd(str, delim);
    }

    /**
     * <p>Strip whitespace from the front and back of every String
     * in the array.</p>
     * 
     * @param strs the Strings to remove whitespace from
     * @return the stripped Strings
     */
    public static String[] stripAll(String[] strs) {
        return stripAll(strs, null);
    }
 
    /**
     * <p>Strip the specified delimiter from the front and back of
     * every String in the array.</p>
     * 
     * @param strs the Strings to remove a String from
     * @param delimiter the String to remove at start and end
     * @return the stripped Strings
     */
    public static String[] stripAll(String[] strs, String delimiter) {
        if ((strs == null) || (strs.length == 0)) {
            return strs;
        }
        int sz = strs.length;
        String[] newArr = new String[sz];
        for (int i = 0; i < sz; i++) {
            newArr[i] = strip(strs[i], delimiter);
        }
        return newArr;
    }   

    /**
     * <p>Strip any of a supplied String from the end of a String.</p>
     *
     * <p>If the strip String is <code>null</code>, whitespace is
     * stripped.</p>
     * 
     * @param str the String to remove characters from
     * @param strip the String to remove
     * @return the stripped String
     */
    public static String stripEnd(String str, String strip) {
        if (str == null) {
            return null;
        }
        int end = str.length();
 
        if (strip == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else {
            while ((end != 0) && (strip.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    /**
     * <p>Strip any of a supplied String from the start of a String.</p>
     *
     * <p>If the strip String is <code>null</code>, whitespace is
     * stripped.</p>
     * 
     * @param str the String to remove characters from
     * @param strip the String to remove
     * @return the stripped String
     */
    public static String stripStart(String str, String strip) {
        if (str == null) {
            return null;
        }
 
        int start = 0;
 
        int sz = str.length();
 
        if (strip == null) {
            while ((start != sz) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else {
            while ((start != sz) && (strip.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        return str.substring(start);
    }

    // Case conversion
    //--------------------------------------------------------------------------
    
    /**
     * <p>Convert a String to upper case, <code>null</code> String
     * returns <code>null</code>.</p>
     * 
     * @param str the String to uppercase
     * @return the upper cased String
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    /**
     * <p>Convert a String to lower case, <code>null</code> String
     * returns <code>null</code>.</p>
     * 
     * @param str the string to lowercase
     * @return the lower cased String
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    /**
     * <p>Uncapitalise a String.</p>
     *
     * <p>That is, convert the first character into lower-case.
     * <code>null</code> is returned as <code>null</code>.</p>
     *
     * @param str the String to uncapitalise
     * @return uncapitalised String
     */
    public static String uncapitalise(String str) {
        if (str == null) {
            return null;
        }
        else if (str.length() == 0) {
            return "";
        }
        else {
            return new StringBuffer(str.length())
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
        }
    }

    /**
     * <p>Capitalise a String.</p>
     *
     * <p>That is, convert the first character into title-case.
     * <code>null</code> is returned as <code>null</code>.</p>
     *
     * @param str the String to capitalise
     * @return capitalised String
     */
    public static String capitalise(String str) {
        if (str == null) {
            return null;
        }
        else if (str.length() == 0) {
            return "";
        }
        else {
            return new StringBuffer(str.length())
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
        }
    }

    /**
     * <p>Swaps the case of String.</p>
     *
     * <p>Properly looks after making sure the start of words
     * are Titlecase and not Uppercase.</p>
     *
     * <p><code>null</code> is returned as <code>null</code>.</p>
     * 
     * @param str the String to swap the case of
     * @return the modified String
     */
    public static String swapCase(String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);

        boolean whitespace = false;
        char ch = 0;
        char tmp = 0;

        for (int i = 0; i < sz; i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                if (whitespace) {
                    tmp = Character.toTitleCase(ch);
                } else {
                    tmp = Character.toUpperCase(ch);
                }
            } else {
                tmp = ch;
            }
            buffer.append(tmp);
            whitespace = Character.isWhitespace(ch);
        }
        return buffer.toString();
    }


    /**
     * <p>Capitalise all the words in a String.</p>
     *
     * <p>Uses {@link Character#isWhitespace(char)} as a
     * separator between words.</p>
     *
     * <p><code>null</code> will return <code>null</code>.</p>
     *
     * @param str the String to capitalise
     * @return capitalised String
     */
    public static String capitaliseAllWords(String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);
        boolean space = true;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                space = true;
            } else if (space) {
                buffer.append(Character.toTitleCase(ch));
                space = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * <p>Uncapitalise all the words in a string.</p>
     *
     * <p>Uses {@link Character#isWhitespace(char)} as a
     * separator between words.</p>
     *
     * <p><code>null</code> will return <code>null</code>.</p>
     *
     * @param str  the string to uncapitalise
     * @return uncapitalised string
     */
    public static String uncapitaliseAllWords(String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);
        boolean space = true;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                space = true;
            } else if (space) {
                buffer.append(Character.toLowerCase(ch));
                space = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    // Nested extraction
    //--------------------------------------------------------------------------
    
    /**
     * <p>Get the String that is nested in between two instances of the
     * same String.</p>
     *
     * <p>If <code>str</code> is <code>null</code>, will
     * return <code>null</code>.</p>
     *
     * @param str the String containing nested-string
     * @param tag the String before and after nested-string
     * @return the String that was nested, or <code>null</code>
     * @throws NullPointerException if tag is <code>null</code>
     */
    public static String getNestedString(String str, String tag) {
        return getNestedString(str, tag, tag);
    }
    
    /**
     * <p>Get the String that is nested in between two Strings.</p>
     *
     * @param str the String containing nested-string
     * @param open the String before nested-string
     * @param close the String after nested-string
     * @return the String that was nested, or <code>null</code>
     * @throws NullPointerException if open or close is <code>null</code>
     */
    public static String getNestedString(String str, String open, String close) {
        if (str == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * <p>How many times is the substring in the larger String.</p>
     *
     * <p><code>null</code> returns <code>0</code>.</p>
     * 
     * @param str the String to check
     * @param sub the substring to count
     * @return the number of occurances, 0 if the String is <code>null</code>
     * @throws NullPointerException if sub is <code>null</code>
     */
    public static int countMatches(String str, String sub) {
        if (sub.equals("")) {
            return 0;
        }
        if (str == null) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    // Character Tests
    //--------------------------------------------------------------------------
    
    /**
     * <p>Checks if the String contains only unicode letters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains letters, and is non-null
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetter(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only whitespace.</p>
     *
     * <p><code>null</code> will return <code>false</code>. An
     * empty String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains whitespace, and is non-null
     */
    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters and
     * space (<code>' '</code>).</p>
     *
     * <p><code>null</code> will return <code>false</code>. An
     * empty String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains letters and space,
     *  and is non-null
     */
    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetter(str.charAt(i)) == false) &&
                (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters or digits.</p>
     *
     * <p><code>null</code> will return <code>false</code>. An empty
     * String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains letters or digits,
     *  and is non-null
     */
    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetterOrDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode letters, digits
     * or space (<code>' '</code>).</p>
     *
     * <p><code>null</code> will return <code>false</code>. An empty
     * String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains letters, digits or space,
     *  and is non-null
     */
    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isLetterOrDigit(str.charAt(i)) == false) &&
                (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only unicode digits or space
     * (<code>' '</code>).</p>
     *
     * <p><code>null</code> will return <code>false</code>. An empty
     * String will return <code>true</code>.</p>
     * 
     * @param str the String to check
     * @return <code>true</code> if only contains digits or space,
     *  and is non-null
     */
    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isDigit(str.charAt(i)) == false) &&
                (str.charAt(i) != ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only certain chars.</p>
     *
     * @param str the String to check
     * @param validChars a string of valid chars
     * @return true if it only contains valid chars and is non-null
     */
    public static boolean containsOnly(String str, String validChars) {
        if (str == null || validChars == null) {
            return false;
        }
        return containsOnly(str, validChars.toCharArray());
    }
    
    /**
     * <p>Checks if the String contains only certain chars.</p>
     *
     * @param str the String to check
     * @param validChars an array of valid chars
     * @return true if it only contains valid chars and is non-null
     */
     /* rewritten
    public static boolean containsOnly(String str, char[] validChars) {
        if (str == null || validChars == null) {
            return false;
        }
        int strSize = str.length();
        int validSize = validChars.length;
        for (int i = 0; i < strSize; i++) {
            char ch = str.charAt(i);
            boolean contains = false;
            for (int j = 0; j < validSize; j++) {
                if (validChars[j] == ch) {
                    contains = true;
                    break;
                }
            }
            if (contains == false) {
                return false;
            }
        }
        return true;
    }
    */

    /**
     * <p>Checks that the String does not contain certain chars.</p>
     *
     * @param str the String to check
     * @param invalidChars a string of invalid chars
     * @return true if it contains none of the invalid chars, or is null
     */
    public static boolean containsNone(String str, String invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        return containsNone(str, invalidChars.toCharArray());
    }
    
    /**
     * <p>Checks that the String does not contain certain chars.</p>
     *
     * @param str the String to check
     * @param invalidChars an array of invalid chars
     * @return true if it contains none of the invalid chars, or is null
     */
    public static boolean containsNone(String str, char[] invalidChars) {
        if (str == null || invalidChars == null) {
            return true;
        }
        int strSize = str.length();
        int validSize = invalidChars.length;
        for (int i = 0; i < strSize; i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < validSize; j++) {
                if (invalidChars[j] == ch) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>Checks if the String contains only certain chars.</p>
     *
     * @param str the String to check
     * @param valid an array of valid chars
     * @return true if it only contains valid chars and is non-null
     */
    public static boolean containsOnly(String str, char[] valid) {
        // All these pre-checks are to maintain API with an older version
        if( (valid == null) || (str == null) ) {
            return false;
        }
        if(str.length() == 0) {
            return true;
        }
        if(valid.length == 0) {
            return false;
        }
        return indexOfAnyBut(str, valid) == -1;
    }

    /**
     * <p>Search a String to find the first index of any
     * character not in the given set of characters.</p>
     * 
     * @param str  the String to check
     * @param searchChars  the chars to search for
     * @return the index of any of the chars
     * @throws NullPointerException if either str or searchChars is <code>null</code>
     */
     public static int indexOfAnyBut(String str, char[] searchChars) {
         if(searchChars == null) {
             return -1;
         }
         return indexOfAnyBut(str, new String(searchChars));
     }

    /**
     * <p>Search a String to find the first index of any
     * character not in the given set of characters.</p>
     * 
     * @param str  the String to check
     * @param searchChars  a String containing the chars to search for
     * @return the last index of any of the chars
     * @throws NullPointerException if either str or searchChars is <code>null</code>
     */
    public static int indexOfAnyBut(String str, String searchChars) {
        if (str == null || searchChars == null) {
            return -1;
        }

        for (int i = 0; i < str.length(); i ++) {
           if (searchChars.indexOf(str.charAt(i)) < 0) {
               return i;
           }
        }

        return -1;
    }

    // Defaults
    //--------------------------------------------------------------------------
    
    /**
     * <p>Returns either the passed in <code>Object</code> as a String,
     * or, if the <code>Object</code> is <code>null</code>, an empty
     * String.</p>
     * 
     * @param obj the Object to check
     * @return the passed in Object's toString, or blank if it was
     *  <code>null</code>
     */
    public static String defaultString(Object obj) {
        return defaultString(obj, "");
    }

    /**
     * <p>Returns either the passed in <code>Object</code> as a String,
     * or, if the <code>Object</code> is <code>null</code>, a passed
     * in default String.</p>
     * 
     * @param obj the Object to check
     * @param defaultString  the default String to return if str is
     *  <code>null</code>
     * @return the passed in string, or the default if it was
     *  <code>null</code>
     */
    public static String defaultString(Object obj, String defaultString) {
        return (obj == null) ? defaultString : obj.toString();
    }

    // Reversing
    //--------------------------------------------------------------------------

    /**
     * <p>Reverse a String.</p>
     *
     * <p><code>null</code> String returns <code>null</code>.</p>
     * 
     * @param str the String to reverse
     * @return the reversed String
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * <p>Reverses a String that is delimited by a specific character.</p>
     *
     * <p>The Strings between the delimiters are not reversed.
     * Thus java.lang.String becomes String.lang.java (if the delimiter
     * is <code>'.'</code>).</p>
     * 
     * @param str the String to reverse
     * @param delimiter the delimiter to use
     * @return the reversed String
     */
    public static String reverseDelimitedString(String str, String delimiter) {
        // could implement manually, but simple way is to reuse other, 
        // probably slower, methods.
        String[] strs = split(str, delimiter);
        ArrayUtils.reverse(strs);
        return join(strs, delimiter);
    }

    // Abbreviating
    //--------------------------------------------------------------------------

    /**
     * Turn "Now is the time for all good men" into "Now is the time for..."
     * <p>
     * Specifically:
     * <p>
     * If str is less than max characters long, return it.
     * Else abbreviate it to (substring(str, 0, max-3) + "...").
     * If maxWidth is less than 3, throw an IllegalArgumentException.
     * In no case will it return a string of length greater than maxWidth.
     *
     * @param maxWidth maximum length of result string
     */
    public static String abbreviate(String s, int maxWidth) {
        return abbreviate(s, 0, maxWidth);
    }

    /**
     * Turn "Now is the time for all good men" into "...is the time for..."
     * <p>
     * Works like abbreviate(String, int), but allows you to specify a "left edge"
     * offset.  Note that this left edge is not necessarily going to be the leftmost
     * character in the result, or the first
     * character following the ellipses, but it will appear somewhere in the result.
     * In no case will it return a string of length greater than maxWidth.
     *
     * @param offset left edge of source string
     * @param maxWidth maximum length of result string
     */
    public static String abbreviate(String s, int offset, int maxWidth) {
        if (maxWidth < 4)
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        if (s.length() <= maxWidth)
            return s;
        if (offset > s.length())
            offset = s.length();
        if ((s.length() - offset) < (maxWidth-3))
            offset = s.length() - (maxWidth-3);
        if (offset <= 4)
            return s.substring(0, maxWidth-3) + "...";
        if (maxWidth < 7)
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        if ((offset + (maxWidth-3)) < s.length())
            return "..." + abbreviate(s.substring(offset), maxWidth-3);
        return "..." + s.substring(s.length() - (maxWidth-3));
    }

    // Difference
    //--------------------------------------------------------------------------

    /**
     * Compare two strings, and return the portion where they differ.
     * (More precisely, return the remainder of the second string,
     * starting from where it's different from the first.)
     * <p>
     * For example, <code>difference("i am a machine", "i am a robot") -> "robot"</code>
     *
     * @return the portion of s2 where it differs from s1; returns the empty string ("") if they are equal
     */
    public static String difference(String s1, String s2) {
        int at = differenceAt(s1, s2);
        if (at == -1) {
            return "";
        }
        return s2.substring(at);
    }

    /**
     * <p>Compare two strings, and return the index at which the strings begin to differ.</p>
     * 
     * <p>For example, <code>differenceAt("i am a machine", "i am a robot") -> 7</code></p>
     *
     * @return the index where s2 and s1 begin to differ; -1 if they are equal
     */
    public static int differenceAt(String s1, String s2) {
        int i;
        for (i=0; i<s1.length() && i<s2.length(); ++i) {
            if (s1.charAt(i) != s2.charAt(i)) {
                break;
            }
        }
        if (i<s2.length() || i<s1.length()) {
            return i;
        }
        return -1;
    }


    // Misc
    //--------------------------------------------------------------------------

    /**
     * <p>Find the Levenshtein distance between two Strings.</p>
     *
     * <P>This is the number of changes needed to change one String into
     * another. Where each change is a single character modification.</p>
     *
     * <p>This implemmentation of the levenshtein distance algorithm
     * is from <a href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a></p>
     * 
     * @param s the first String
     * @param t the second String
     * @return result distance
     * @throws NullPointerException if s or t is <code>null</code>
     */
    public static int getLevenshteinDistance(String s, String t) {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1
        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3
        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);

            // Step 4
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);

                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                // Step 6
                d[i][j] = NumberUtils.minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }

        // Step 7
        return d[n][m];
    }

}

