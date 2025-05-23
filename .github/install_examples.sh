#!/bin/bash
# Script that generates the required target files for each example project
JAVA_MAJOR_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)

# Java version needs to be lower than 11, since support was dropped
# If the source version is 1.1 OR 1.2, we can only install JUnit3 tests AND we need to use 1.3 as the source version since Java 8 does not support earlier versions
if [ $JAVA_MAJOR_VERSION -lt "11" ] && ([ "$SRC_VERSION" = "1.1" ] || [ "$SRC_VERSION" = "1.2" ]); then
    mvn clean test -DskipTests -Dmaven.compiler.source="1.3" -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL5JUnit3/FLtest1/
    mvn clean test -DskipTests -Dmaven.compiler.source="1.3" -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL12Compliance4/FLtest1/
    exit 0
fi

# Java version needs to be lower than 11, since support was dropped
# If the source version is 1.3 OR 1.4, we can only install JUnit3 tests
if [ $JAVA_MAJOR_VERSION -lt "11" ] && ([ "$SRC_VERSION" = "1.3" ] ||  [ "$SRC_VERSION" = "1.4" ]); then
    mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL5JUnit3/FLtest1/
    mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL12Compliance4/FLtest1/
    exit 0
fi

# Compile maven projects
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL1/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL2/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL3/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL4JUnit5/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL5JUnit3/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL6Mixed/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL7SameNamedMethods/FLtest1/
mvn clean test -DskipTests -Dmaven.compiler.source=$SRC_VERSION -Dmaven.compiler.target=$SRC_VERSION -B -f examples/exampleFL11/FLtest1/

# Compile real projects
if [ $JAVA_MAJOR_VERSION -eq "8" ]; then
    mvn clean test -DskipTests -Dmaven.compiler.source=1.8 -Dmaven.compiler.target=1.8 -B -f examples/math_70/
    mvn clean test -DskipTests -Dmaven.compiler.source=1.5 -Dmaven.compiler.target=1.5 -B -f examples/cl2/
fi

# Copy compiled classes to non-maven mirror projects
rm -r examples/exampleFL8NotMaven/bin/
mkdir -p examples/exampleFL8NotMaven/bin/
cp -r examples/exampleFL1/FLtest1/target/classes examples/exampleFL8NotMaven/bin/classes
cp -r examples/exampleFL1/FLtest1/target/test-classes examples/exampleFL8NotMaven/bin/test-classes

rm -r examples/exampleFL9NotMavenMultiple/bin/
mkdir -p examples/exampleFL9NotMavenMultiple/bin/
cp -r examples/exampleFL7SameNamedMethods/FLtest1/target/classes examples/exampleFL9NotMavenMultiple/bin/classes
cp -r examples/exampleFL7SameNamedMethods/FLtest1/target/test-classes examples/exampleFL9NotMavenMultiple/bin/test-classes1
rm -f examples/exampleFL9NotMavenMultiple/bin/test-classes1/fr/spoonlabs/FLtest1/CalculatorDuplicatedTest.class
cp -r examples/exampleFL7SameNamedMethods/FLtest1/target/test-classes examples/exampleFL9NotMavenMultiple/bin/test-classes2
rm -f examples/exampleFL9NotMavenMultiple/bin/test-classes2/fr/spoonlabs/FLtest1/CalculatorTest.class
