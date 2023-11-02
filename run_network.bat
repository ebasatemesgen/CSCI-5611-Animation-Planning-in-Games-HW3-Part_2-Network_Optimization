@echo off
set JAVA_FILE=NeuralNetworkOptimization
set CLASS_DIR=class_files

if not exist "%CLASS_DIR%" mkdir "%CLASS_DIR%"

echo Compiling all Java files in the current directory...
javac *.java

if %ERRORLEVEL% == 0 (
    echo Compilation successful.
    echo Moving .class files to %CLASS_DIR%...
    move *.class "%CLASS_DIR%"

    echo Running the program...
    java -cp "%CLASS_DIR%" %JAVA_FILE%
) else (
    echo Compilation failed.
)

pause
