@echo off

:: Compile the test classes

:: --TestBrancher
javac -d target -cp src\test\resources\junit-4.12.jar -sourcepath src\main\java src\test\java\com\packager\TestBrancher.java


:: Run the tests

:: -- TestBrancher
java -cp src\test\resources\junit-4.12.jar;src\test\resources\hamcrest-core-1.3.jar;target org.junit.runner.JUnitCore com.packager.TestBrancher
