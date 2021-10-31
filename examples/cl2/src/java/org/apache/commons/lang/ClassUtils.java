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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * <p>Provides utility methods for working for classes without using reflection.</p>
 *
 * @author Stephen Colebourne
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @since 2.0
 * @version $Id: ClassUtils.java,v 1.12 2003/05/29 21:02:53 ggregory Exp $
 */
public class ClassUtils {

    /**
     * The package separator character: <code>&#x2e;</code>
     */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    
    /**
     * The package separator String: <code>&#x2e;</code>
     */
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    
    /**
     * The inner class separator character: <code>$</code>
     */
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    
    /**
     * The inner class separator String: <code>$</code>
     */
    public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);
    
    /**
     * <p>ClassUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as
     * <code>ClassUtils.getShortClassName(cls)</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public ClassUtils() {
    }

    // -------------------------------------------------------------------------
    
    /**
     * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
     * 
     * @param object  the class to get the short name for
     * @param valueIfNull  the value to return if null
     * @return the class name of the object without the package name, or the null value
     */
    public static String getShortClassName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass().getName());
    }
    
    /**
     * <p>Gets the class name minus the package name from a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the short name for, must not be
     *  <code>null</code>
     * @return the class name without the package name
     * @throws IllegalArgumentException if the class is <code>null</code>
     */
    public static String getShortClassName(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        return getShortClassName(cls.getName());
    }
    
    /**
     * <p>Gets the class name minus the package name from a String.</p>
     *
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     * 
     * @param className  the className to get the short name for, must not be empty
     * @return the class name of the class without the package name
     * @throws IllegalArgumentException if the className is empty
     */
    public static String getShortClassName(String className) {
        if (StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("The class name must not be empty");
        }
        char[] chars = className.toCharArray();
        int lastDot = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
                lastDot = i + 1;
            } else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR) {  // handle inner classes
                chars[i] = PACKAGE_SEPARATOR_CHAR;
            }
        }
        return new String(chars, lastDot, chars.length - lastDot);
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * <p>Gets the package name of an <code>Object</code>.</p>
     * 
     * @param object  the class to get the package name for
     * @param valueIfNull  the value to return if null
     * @return the package name of the object, or the null value
     */
    public static String getPackageName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageName(object.getClass().getName());
    }
    
    /**
     * <p>Gets the package name of a <code>Class</code>.</p>
     * 
     * @param cls  the class to get the package name for, must not be
     *  <code>null</code>
     * @return the package name
     * @throws IllegalArgumentException if the class is <code>null</code>
     */
    public static String getPackageName(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        return getPackageName(cls.getName());
    }
    
    /**
     * <p>Gets the package name from a <code>String</code>.</p>
     *
     * <p>The string passed in is assumed to be a class name - it is not checked.</p>
     * 
     * @param className  the className to get the package name for, must not be empty
     * @return the package name
     * @throws IllegalArgumentException if the className is empty
     */
    public static String getPackageName(String className) {
        if (StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("The class name must not be empty");
        }
        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1) {
            return "";
        }
        return className.substring(0, i);
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * <p>Gets a <code>List</code> of superclasses for the given class.</p>
     * 
     * @param cls  the class to look up, must not be <code>null</code>
     * @return the <code>List</code> of superclasses in order going up from this one
     * @throws IllegalArgumentException if the class is <code>null</code>
     */
    public static List getAllSuperclasses(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        List classes = new ArrayList();
        Class superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }
    
    /**
     * <p>Gets a <code>List</code> of all interfaces implemented by the given
     * class and its superclasses.</p>
     *
     * <p>The order is determined by looking through each interface in turn as
     * declared in the source file and following its hieracrchy up. Then each 
     * superclass is considered in the same way. Later duplicates are ignored, 
     * so the order is maintained.</p>
     * 
     * @param cls  the class to look up, must not be <code>null</code>
     * @return the <code>List</code> of interfaces in order
     * @throws IllegalArgumentException if the class is <code>null</code>
     */
    public static List getAllInterfaces(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        List list = new ArrayList();
        while (cls != null) {
            Class[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (list.contains(interfaces[i]) == false) {
                    list.add(interfaces[i]);
                }
                List superInterfaces = getAllInterfaces(interfaces[i]);
                for (Iterator it = superInterfaces.iterator(); it.hasNext();) {
                    Class intface = (Class) it.next();
                    if (list.contains(intface) == false) {
                        list.add(intface);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return list;
    }
    
//    /**
//     * <p>Gets a <code>List</code> of subclasses of the specified class.</p>
//     *
//     * <p>This method searches the classpath to find all the subclasses
//     * of a particular class available. No classes are loaded, the 
//     * returned list contains class names, not classes.</p>
//     *
//     * @param cls  the class to find subclasses for
//     * @return the <code>List</code> of subclass String class names
//     * @throws IllegalArgumentException if the class is <code>null</code>
//     */
//    public static List getAllSubclassNames(Class cls) {
//        if (cls == null) {
//            throw new IllegalArgumentException("The class must not be null");
//        }
//        // TODO Use JavaWorld tip for searching the classpath
//        return null;
//    }

//    /**
//     * <p>Gets a <code>List</code> of subclasses of the specified class.</p>
//     *
//     * <p>This method searches the classpath to find all the subclasses
//     * of a particular class available.</p>
//     *
//     * @param cls  the class to find subclasses for
//     * @return the <code>List</code> of subclasses
//     * @throws IllegalArgumentException if the class is <code>null</code>
//     */
//    public static List getAllSubclasses(Class cls) {
//        List names = getAllSubclassNames(cls);
//        return convertClassNamesToClasses(names);
//    }

//    /**
//     * <p>Gets a <code>List</code> of implementations of the specified interface.</p>
//     *
//     * <p>This method searches the classpath to find all the implementations
//     * of a particular interface available. No classes are loaded, the 
//     * returned list contains class names, not classes.</p>
//     *
//     * @param cls  the class to find sub classes for
//     * @return the <code>List</code> of implementation String class names
//     * @throws IllegalArgumentException if the class is <code>null</code>
//     */
//    public static List getAllImplementationClassNames(Class cls) {
//        if (cls == null) {
//            throw new IllegalArgumentException("The class must not be null");
//        }
//        // TODO Use JavaWorld tip for searching the classpath
//        return null;
//    }

    // -------------------------------------------------------------------------
    
    /**
     * <p>Given a <code>List</code> of class names, this method converts them into classes.     
     *
     * A new <code>List</code> is returned. If the class name cannot be found, <code>null</code>
     * is stored in the <code>List</code>. If the class name in the <code>List</code> is
     * <code>null</code>, <code>null</code> is stored in the output <code>List</code>.</p>
     * 
     * @param classNames  the classNames to change
     * @return a <code>List</code> of Class objects corresponding to the class names
     * @throws IllegalArgumentException if the classNames is <code>null</code>
     * @throws ClassCastException if classNames contains a non String entry
     */
    public static List convertClassNamesToClasses(List classNames) {
        if (classNames == null) {
            throw new IllegalArgumentException("The class names must not be null");
        }
        List classes = new ArrayList(classNames.size());
        for (Iterator it = classNames.iterator(); it.hasNext();) {
            String className = (String) it.next();
            try {
                classes.add(Class.forName(className));
            } catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }
    
    /**
     * <p>Given a <code>List</code> of <code>Class</code> objects, this method converts
     * them into class names.
     * A new <code>List</code> is returned. <code>null</code> objects will be copied into
     * the returned list as <code>null</code>.</p>
     * 
     * @param classes  the classes to change
     * @return a <code>List</code> of Class objects corresponding to the class names
     * @throws IllegalArgumentException if the classNames is <code>null</code>
     * @throws ClassCastException if classNames contains a non Class or null entry
     */
    public static List convertClassesToClassNames(List classes) {
        if (classes == null) {
            throw new IllegalArgumentException("The classes list must not be null");
        }
        List classNames = new ArrayList(classes.size());
        for (Iterator it = classes.iterator(); it.hasNext();) {
            Class cls = (Class) it.next();
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }
    
    // -------------------------------------------------------------------------
    
    /**
     * <p>Checks if an array of Classes can be assigned to another array of Classes.</p>
     *
     * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatable with a set of method parameter types
     * (the second parameter).</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a <code>long</code>,
     * <code>float</code> or <code>double</code>. This method returns the correct
     * result for these cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method will
     * return <code>true</code> if <code>null</code> is passed in and the toClass is
     * non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See 
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param classArray  the array of Classes to check, may be <code>null</code>
     * @param toClassArray  the array of Classes to try to assign into, may be <code>null</code>
     * @return <code>true</code> if assignment possible
     */
    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        if (ArrayUtils.isSameLength(classArray, toClassArray) == false) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (isAssignable(classArray[i], toClassArray[i]) == false) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>Checks if one <code>Class</code> can be assigned to a variable of
     * another <code>Class</code>.</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return <code>true</code> if <code>null</code> is passed in and the
     * toClass is non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified <code>Class</code> parameter can be converted to the type
     * represented by this <code>Class</code> object via an identity conversion
     * widening primitive or widening reference conversion. See 
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param cls  the Class to check, may be <code>null</code>
     * @param toClass  the Class to try to assign into, must not be <code>null</code>
     * @return <code>true</code> if assignment possible
     * @throws IllegalArgumentException if the toClass is <code>null</code>
     */
    public static boolean isAssignable(Class cls, Class toClass) {
        if (toClass == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive() == false) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass) 
                    || Float.TYPE.equals(toClass) 
                    || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass) 
                    || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)) {
                return false;
            }
            if (Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) 
                    || Long.TYPE.equals(toClass) 
                    || Float.TYPE.equals(toClass) 
                    || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass) 
                    || Long.TYPE.equals(toClass) 
                    || Float.TYPE.equals(toClass) 
                    || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass) 
                    || Integer.TYPE.equals(toClass) 
                    || Long.TYPE.equals(toClass) 
                    || Float.TYPE.equals(toClass) 
                    || Double.TYPE.equals(toClass);
            }
            // should never get here
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }
    
    /**
     * <p>Is the specified class an inner class or static nested class.</p>
     * 
     * @param cls  the class to check
     * @return <code>true</code> if the class is an inner or static nested class
     * @throws IllegalArgumentException if the class is <code>null</code>
     */
    public static boolean isInnerClass(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        return (cls.getName().indexOf(INNER_CLASS_SEPARATOR_CHAR) >= 0);
    }
    
}
