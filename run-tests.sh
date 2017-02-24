#!bin/bash

echo "Running tests..."

MAIN_SRC=src/main/java
TESTS_SRC=src/tests/java
TARGET=target

JUNIT=src/tests/resources/junit-4.12.jar
HAMCREST=src/tests/resources/hamcrest-core-1.3.jar

TEST=org.junit.runner.JUnitCore

# Compile source files

# -- TestBrancher
javac -d $TARGET -cp $JUNIT -sourcepath $MAIN_SRC "$TESTS_SRC/com/packager/TestBrancher.java"



# Run 

# -- TestBrancher
java -cp $JUNIT:$HAMCREST:$TARGET $TEST com.packager.TestBrancher
