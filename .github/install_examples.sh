#!/bin/bash

# Script that generates the required target files for each example project

mvn clean test -DskipTests -B -f examples/exampleFL1/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL2/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL3/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL4JUnit5/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL5JUnit3/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL6Mixed/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL7SameNamedMethods/FLtest1/
