#!/bin/bash

# Define the name of your Java file without the extension
JAVA_FILE="NeuralNetworkOptimization"
# Define the directory to store .class files
CLASS_DIR="class_files"

# Create a directory for class files if it doesn't exist
mkdir -p "${CLASS_DIR}"

# Compile all Java files in the current directory
javac *.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."

    # Move all .class files to the designated directory
    mv *.class "${CLASS_DIR}/"

    # Run the compiled class file from the class_files directory
    java -cp "${CLASS_DIR}" "${JAVA_FILE}"

else
    echo "Compilation failed."
fi
