/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
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
package org.apache.commons.lang.math;

import java.util.Random;

/**
 * <p><code>RandomUtils</code> is a wrapper that supports all possible 
 * Random methods via the java.lang.Math.random() method and its system-wide 
 * Random object.
 * 
 * @author Henri Yandell
 * @since 2.0
 * @version $Id: RandomUtils.java,v 1.2 2003/05/07 15:09:19 bayard Exp $
 */
public final class RandomUtils {

    public static Random JVM_RANDOM = new JVMRandom();

// should be possible for JVM_RANDOM?
//    public static void nextBytes(byte[]) {
//    public synchronized double nextGaussian();
//    }

    /**
     * Returns the next pseudorandom, uniformly distributed int value 
     * from the Math.random() sequence. 
     *
     * @return the random int
     */
    public static int nextInt() {
        return nextInt(JVM_RANDOM);
    }
    public static int nextInt(Random rnd) {
        return rnd.nextInt();
    }
    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 
     * (inclusive) and the specified value (exclusive), from the 
     * Math.random() sequence. 
     *
     * @param n  the specified exclusive max-value
     *
     * @return the random int
     */
    public static int nextInt(int n) {
        return nextInt(JVM_RANDOM, n);
    }
    public static int nextInt(Random rnd, int n) {
        // check this cannot return 'n'
        return rnd.nextInt(n);
    }
    /**
     * Returns the next pseudorandom, uniformly distributed long value 
     * from the Math.random() sequence.
     *
     * @return the random long
     */
    public static long nextLong() {
        return nextLong(JVM_RANDOM);
    }
    public static long nextLong(Random rnd) {
        return rnd.nextLong();
    }
    /**
     * Returns the next pseudorandom, uniformly distributed boolean value 
     * from the Math.random() sequence.
     *
     * @return the random boolean
     */
    public static boolean nextBoolean() {
        return nextBoolean(JVM_RANDOM);
    }
    public static boolean nextBoolean(Random rnd) {
        return rnd.nextBoolean();
    }
    /**
     * Returns the next pseudorandom, uniformly distributed float value 
     * between 0.0 and 1.0 from the Math.random() sequence.
     *
     * @return the random float
     */
    public static float nextFloat() {
        return nextFloat(JVM_RANDOM);
    }
    public static float nextFloat(Random rnd) {
        return rnd.nextFloat();
    }
    /**
     * Synonymous to the Math.random() call.
     *
     * @return the random double
     */
    public static double nextDouble() {
        return nextDouble(JVM_RANDOM);
    }
    public static double nextDouble(Random rnd) {
        return rnd.nextDouble();
    }
    
}
