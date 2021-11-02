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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Test suite for the Lang package.
 *
 * @author Stephen Colebourne
 * @author <a href="mailto:ridesmet@users.sourceforge.net">Ringo De Smet</a>
 * @author Matthew Hawthorne
 * @version $Id: LangTestSuite.java,v 1.18 2003/05/24 12:11:02 scolebourne Exp $
 */
public class LangTestSuite extends TestCase {
    
    /**
     * Construct a new instance.
     */
    public LangTestSuite(String name) {
        super(name);
    }

    /**
     * Command-line interface.
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Get the suite of tests
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("Commons-Lang Tests");
        suite.addTest(ArrayUtilsTest.suite());
        suite.addTest(BooleanUtilsTest.suite());
        suite.addTest(CharSetUtilsTest.suite());
        suite.addTest(ClassUtilsTest.suite());
        suite.addTest(EntitiesTest.suite());
        suite.addTest(IllegalClassExceptionTest.suite());
        suite.addTest(IncompleteArgumentExceptionTest.suite());
        suite.addTest(NotImplementedExceptionTest.suite());
        suite.addTest(NullArgumentExceptionTest.suite());
        suite.addTest(NumberRangeTest.suite());
        suite.addTest(NumberUtilsTest.suite());
        suite.addTest(ObjectUtilsTest.suite());
        suite.addTest(RandomStringUtilsTest.suite());
        suite.addTest(SerializationUtilsTest.suite());
        suite.addTest(StringUtilsTrimEmptyTest.suite());
        suite.addTest(StringUtilsSubstringTest.suite());
        suite.addTest(StringUtilsEqualsIndexOfTest.suite());
        suite.addTest(StringUtilsIsTest.suite());
        suite.addTest(StringEscapeUtilsTest.suite());
        suite.addTest(SystemUtilsTest.suite());
        suite.addTest(UnhandledExceptionTest.suite());
        suite.addTest(WordWrapUtilsTest.suite());
        return suite;
    }
}
