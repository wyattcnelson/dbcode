#!bin/bash

echo "Running tests..."

MAIN_SRC="src/main/java"
TESTS_SRC="src/test/java"

MAIN_CLASS="target/main"
TEST_CLASS="target/test"

JUNIT="src/test/resources/junit-4.12.jar"
HAMCREST="src/test/resources/hamcrest-core-1.3.jar"

TEST="org.junit.runner.JUnitCore"

# Compile source files

echo "Compiling TestBrancher..."

# -- TestBrancher
javac -d $TEST_CLASS -cp $JUNIT -sourcepath $MAIN_SRC "$TESTS_SRC/com/sg/packager/TestBrancher.java"



# Run 

echo "Running TestBrancher..."

# -- TestBrancher
java -cp $JUNIT:$HAMCREST:$MAIN_CLASS:$TEST_CLASS $TEST com.sg.packager.TestBrancher
