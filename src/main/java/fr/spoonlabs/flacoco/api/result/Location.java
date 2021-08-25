package fr.spoonlabs.flacoco.api.result;

import java.util.Objects;

public class Location {

    private String className;

    private Integer lineNumber;

    public Location(String className, Integer lineNumber) {
        this.className = className;
        this.lineNumber = lineNumber;
    }

    public String getClassName() {
        return className;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return this.className + "@-@" + this.lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(className, location.className) && Objects.equals(lineNumber, location.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, lineNumber);
    }
}
