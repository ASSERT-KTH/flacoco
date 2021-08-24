package fr.spoonlabs.flacoco.core.test.method;

import java.util.Objects;

/**
 * Contains all the information about a single test unit, such as the test class and method names
 *
 * @author andre15silva
 */
public class StringTestMethod implements TestMethod {

    private String fullyQualifiedClassName;

    private String fullyQualifiedMethodName;

    public StringTestMethod(String fullyQualifiedClassName, String simpleMethodName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
        this.fullyQualifiedMethodName = fullyQualifiedClassName + "#" + simpleMethodName;
    }

    public String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    public String getFullyQualifiedMethodName() {
        return fullyQualifiedMethodName;
    }

    @Override
    public String toString() {
        return "[Manual]TestMethod=" + getFullyQualifiedMethodName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) return false;
        TestMethod that = (TestMethod) o;
        return Objects.equals(getFullyQualifiedMethodName(), that.getFullyQualifiedMethodName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullyQualifiedMethodName());
    }
}
