#!/bin/bash

# Script that generates the required target files for each example project

mvn clean test -DskipTests -B -f examples/exampleFL1/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL2/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL3/FLtest1/
mvn clean test -DskipTests -B -f examples/exampleFL4/FLtest1/
