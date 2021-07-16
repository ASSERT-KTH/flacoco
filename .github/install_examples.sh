#!/bin/bash
# Script that generates the required target files for each example project

# Compile maven projects
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL1/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL2/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL3/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL4JUnit5/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL5JUnit3/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL6Mixed/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL7SameNamedMethods/FLtest1/

# Copy compiled classes to non-maven mirror projects
rm -r examples/exampleFL8NotMaven/bin/
mkdir -p examples/exampleFL8NotMaven/bin/
cp -r examples/exampleFL1/FLtest1/target/classes examples/exampleFL8NotMaven/bin/classes
cp -r examples/exampleFL1/FLtest1/target/test-classes examples/exampleFL8NotMaven/bin/test-classes
