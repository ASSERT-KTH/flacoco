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
package org.apache.commons.lang.enums;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Abstract superclass for type-safe enums.
 * <p>
 * One feature of the C programming language lacking in Java is enumerations. The
 * C implementation based on ints was poor and open to abuse. The original Java
 * recommendation and most of the JDK also uses int constants. It has been recognised
 * however that a more robust type-safe class-based solution can be designed. This
 * class follows the basic Java type-safe enumeration pattern.
 * <p>
 * <em>NOTE:</em>Due to the way in which Java ClassLoaders work, comparing Enum objects
 * should always be done using the equals() method, not ==. The equals() method will
 * try == first so in most cases the effect is the same.
 * 
 * <h4>Simple Enums</h4>
 * To use this class, it must be subclassed. For example:
 *
 * <pre>
 * public final class ColorEnum extends Enum {
 *   public static final ColorEnum RED = new ColorEnum("Red");
 *   public static final ColorEnum GREEN = new ColorEnum("Green");
 *   public static final ColorEnum BLUE = new ColorEnum("Blue");
 *
 *   private ColorEnum(String color) {
 *     super(color);
 *   }
 * 
 *   public static ColorEnum getEnum(String color) {
 *     return (ColorEnum) getEnum(ColorEnum.class, color);
 *   }
 * 
 *   public static Map getEnumMap() {
 *     return getEnumMap(ColorEnum.class);
 *   }
 * 
 *   public static List getEnumList() {
 *     return getEnumList(ColorEnum.class);
 *   }
 * 
 *   public static Iterator iterator() {
 *     return iterator(ColorEnum.class);
 *   }
 * }
 * </pre>
 * <p>
 * As shown, each enums has a name. This can be accessed using <code>getName</code>.
 * <p>
 * The <code>getEnum</code> and <code>iterator</code> methods are recommended. 
 * Unfortunately, Java restrictions require these to be coded as shown in each subclass.
 * An alternative choice is to use the {@link EnumUtils} class.
 * 
 * <h4>Subclassed Enums</h4>
 * A hierarchy of Enum classes can be built. In this case, the superclass is 
 * unaffected by the addition of subclasses (as per normal Java). The subclasses
 * may add additional Enum constants <i>of the type of the superclass</i>. The
 * query methods on the subclass will return all of the Enum constants from the
 * superclass and subclass.
 *
 * <pre>
 * public class ExtraColorEnum extends ColorEnum {
 *   // NOTE: Color enums declared above is final, change that to get this
 *   // example to compile.
 *   public static final ColorEnum YELLOW = new ExtraColorEnum("Yellow");
 *
 *   private ExtraColorEnum(String color) {
 *     super(color);
 *   }
 * 
 *   public static ColorEnum getEnum(String color) {
 *     return (ColorEnum) getEnum(ExtraColorEnum.class, color);
 *   }
 * 
 *   public static Map getEnumMap() {
 *     return getEnumMap(ExtraColorEnum.class);
 *   }
 * 
 *   public static List getEnumList() {
 *     return getEnumList(ExtraColorEnum.class);
 *   }
 * 
 *   public static Iterator iterator() {
 *     return iterator(ExtraColorEnum.class);
 *   }
 * }
 * </pre>
 * 
 * This example will return RED, GREEN, BLUE, YELLOW from the List and iterator 
 * methods in that order. The RED, GREEN and BLUE instances will be the same (==) 
 * as those from the superclass ColorEnum. Note that YELLOW is declared as a
 * ColorEnum and not an ExtraColorEnum.
 * 
 * <h4>Functional Enums</h4>
 * The enums can have functionality by using anonymous inner classes
 * [Effective Java, Bloch01]:
 * 
 * <pre>
 * public abstract class OperationEnum extends Enum {
 *   public static final OperationEnum PLUS = new OperationEnum("Plus") {
 *     public double eval(double a, double b) {
 *       return (a + b);
 *     }
 *   };
 *   public static final OperationEnum MINUS = new OperationEnum("Minus") {
 *     public double eval(double a, double b) {
 *       return (a - b);
 *     }
 *   };
 *
 *   private OperationEnum(String color) {
 *     super(color);
 *   }
 * 
 *   public abstract double eval(double a, double b);
 * 
 *   public static OperationEnum getEnum(String name) {
 *     return (OperationEnum) getEnum(OperationEnum.class, name);
 *   }
 * 
 *   public static Map getEnumMap() {
 *     return getEnumMap(OperationEnum.class);
 *   }
 * 
 *   public static List getEnumList() {
 *     return getEnumList(OperationEnum.class);
 *   }
 * 
 *   public static Iterator iterator() {
 *     return iterator(OperationEnum.class);
 *   }
 * }
 * </pre>
 * <p>
 * <em>NOTE:</em> This class originated in the Jakarta Avalon project.
 * </p>
 *
 * @author Stephen Colebourne
 * @author Chris Webb
 * @author Mike Bowler
 * @since 1.0
 * @version $Id: Enum.java,v 1.10 2003/02/06 20:13:07 scolebourne Exp $
 */
public abstract class Enum implements Comparable, Serializable {
    // After discussion, the default size for HashMaps is used, as the
    // sizing algorithm changes across the JDK versions
    
    /**
     * An empty map, as JDK1.2 didn't have an empty map
     */
    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(0));
    /**
     * Map, key of class name, value of Entry.
     */
    private static final Map cEnumClasses = new HashMap();
    /**
     * The string representation of the Enum.
     */
    private final String iName;

    /**
     * Enable the iterator to retain the source code order
     */
    private static class Entry {
        /** Map of Enum name to Enum */
        final Map map = new HashMap();
        /** List of Enums in source code order */
        final List list = new ArrayList(25);

        /**
         * Restrictive constructor
         */
        private Entry() {
        }
    }

    /**
     * Constructor to add a new named item to the enumeration.
     *
     * @param name  the name of the enums object
     * @throws IllegalArgumentException if the name is null or a blank string
     */
    protected Enum(String name) {
        super();
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("The Enum name must not be empty or null");
        }
        iName = name;
        Class enumClass = Enum.getEnumClass(getClass());
        Entry entry = (Entry) cEnumClasses.get(enumClass);
        if (entry == null) {
            entry = createEntry(getClass());
            cEnumClasses.put(enumClass, entry);
        }
        if (entry.map.containsKey(name)) {
            throw new IllegalArgumentException("The Enum name must be unique, '" + name + "' has already been added");
        }
        entry.map.put(name, this);
        entry.list.add(this);
    }

    /**
     * Handle the deserialization of the class to ensure that multiple
     * copies are not wastefully created, or illegal enums types created.
     * @return the resolved object
     */
    protected Object readResolve() {
        Entry entry = (Entry) cEnumClasses.get(Enum.getEnumClass(getClass()));
        if (entry == null) {
            return null;
        }
        return (Enum) entry.map.get(getName());
    }
    
    //--------------------------------------------------------------------------------

    /**
     * Gets an Enum object by class and name.
     * 
     * @param enumClass  the class of the Enum to get, must not be null
     * @param name  the name of the Enum to get, may be null
     * @return the enums object, or null if the enums does not exist
     * @throws IllegalArgumentException if the enums class is null
     */
    protected static Enum getEnum(Class enumClass, String name) {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return null;
        }
        return (Enum) entry.map.get(name);
    }

    /**
     * Gets the Map of Enum objects by name using the Enum class.
     * If the requested class has no enums objects an empty Map is returned.
     * 
     * @param enumClass  the class of the Enum to get, must not be null
     * @return the enums object Map
     * @throws IllegalArgumentException if the enums class is null
     * @throws IllegalArgumentException if the enums class is not a subclass of Enum
     */
    protected static Map getEnumMap(Class enumClass) {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return EMPTY_MAP;
        }
        return Collections.unmodifiableMap(entry.map);
    }

    /**
     * Gets the List of Enum objects using the Enum class.
     * The list is in the order that the objects were created (source code order).
     * If the requested class has no enums objects an empty List is returned.
     * 
     * @param enumClass  the class of the Enum to get, must not be null
     * @return the enums object Map
     * @throws IllegalArgumentException if the enums class is null
     * @throws IllegalArgumentException if the enums class is not a subclass of Enum
     */
    protected static List getEnumList(Class enumClass) {
        Entry entry = getEntry(enumClass);
        if (entry == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(entry.list);
    }

    /**
     * Gets an iterator over the Enum objects in an Enum class.
     * The iterator is in the order that the objects were created (source code order).
     * If the requested class has no enums objects an empty Iterator is returned.
     * 
     * @param enumClass  the class of the Enum to get, must not be null
     * @return an iterator of the Enum objects
     * @throws IllegalArgumentException if the enums class is null
     * @throws IllegalArgumentException if the enums class is not a subclass of Enum
     */
    protected static Iterator iterator(Class enumClass) {
        return Enum.getEnumList(enumClass).iterator();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an entry from the map of Enums.
     * 
     * @param enumClass  the class of the Enum to get
     * @return the enums entry
     */
    private static Entry getEntry(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        if (Enum.class.isAssignableFrom(enumClass) == false) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        }
        Entry entry = (Entry) cEnumClasses.get(enumClass);
        return entry;
    }
    
    /**
     * Creates an entry for storing the Enums.
     * This accounts for subclassed Enums.
     * 
     * @param enumClass  the class of the Enum to get
     * @return the enums entry
     */
    private static Entry createEntry(Class enumClass) {
        Entry entry = new Entry();
        Class cls = enumClass.getSuperclass();
        while (cls != null && cls != Enum.class && cls != ValuedEnum.class) {
            Entry loopEntry = (Entry) cEnumClasses.get(cls);
            if (loopEntry != null) {
                entry.list.addAll(loopEntry.list);
                entry.map.putAll(loopEntry.map);
                break;  // stop here, as this will already have had superclasses added
            }
            cls = cls.getSuperclass();
        }
        return entry;
    }
    
    /**
     * Convert a class to the actual common enums class.
     * This accounts for anonymous inner classes.
     * 
     * @param cls  the class to get the name for
     * @return the class name
     */
    protected static Class getEnumClass(Class cls) {
        String className = cls.getName();
        int index = className.lastIndexOf('$');
        if (index > -1) {
            // is it an anonymous inner class?
            String inner = className.substring(index + 1);
            if (inner.length() > 0 &&
                inner.charAt(0) >= '0' &&
                inner.charAt(0) < '9') {
                return cls.getSuperclass();
            }
        }
        return cls;
    }

    //-----------------------------------------------------------------------
    /**
     * Retrieve the name of this Enum item, set in the constructor.
     * 
     * @return the <code>String</code> name of this Enum item
     */
    public final String getName() {
        return iName;
    }

    /**
     * Tests for equality. Two Enum objects are considered equal
     * if they have the same class names and the same names.
     * Identity is tested for first, so this method usually runs fast.
     *
     * @param other  the other object to compare for equality
     * @return true if the Enums are equal
     */
    public final boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other.getClass() == this.getClass()) {
            // shouldn't happen, but...
            return iName.equals(((Enum) other).iName);
        } else if (getEnumClass(other.getClass()).getName().equals(getEnumClass(this.getClass()).getName())) {
            // different classloaders
            try {
                // try to avoid reflection
                return iName.equals(((Enum) other).iName);

            } catch (ClassCastException ex) {
                // use reflection
                try {
                    Method mth = other.getClass().getMethod("getName", null);
                    String name = (String) mth.invoke(other, null);
                    return iName.equals(name);
                } catch (NoSuchMethodException ex2) {
                    // ignore - should never happen
                } catch (IllegalAccessException ex2) {
                    // ignore - should never happen
                } catch (InvocationTargetException ex2) {
                    // ignore - should never happen
                }
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns a suitable hashCode for the enumeration.
     *
     * @return a hashcode based on the name
     */
    public final int hashCode() {
        return 7 + iName.hashCode();
    }

    /**
     * Tests for order. The default ordering is alphabetic by name, but this
     * can be overridden by subclasses.
     * 
     * @see java.lang.Comparable#compareTo(Object)
     * @param other  the other object to compare to
     * @return -ve if this is less than the other object, +ve if greater than, 0 of equal
     * @throws ClassCastException if other is not an Enum
     * @throws NullPointerException if other is null
     */
    public int compareTo(Object other) {
        return iName.compareTo(((Enum) other).iName);
    }

    /**
     * Human readable description of this Enum item. For use when debugging.
     * 
     * @return String in the form <code>type[name]</code>, for example:
     * <code>Color[Red]</code>. Note that the package name is stripped from
     * the type name.
     */
    public String toString() {
        String shortName = Enum.getEnumClass(getClass()).getName();
        int pos = shortName.lastIndexOf('.');
        if (pos != -1) {
            shortName = shortName.substring(pos + 1);
        }
        shortName = shortName.replace('$', '.');
        return shortName + "[" + getName() + "]";
    }
}
