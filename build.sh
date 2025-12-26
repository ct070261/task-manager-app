#!/bin/bash
# Build script for Task Manager Application (Unix/Linux/Mac)

echo "========================================"
echo "  Building Task Manager Application"
echo "========================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH!"
    echo ""
    echo "Please install Maven and try again."
    echo "Download from: https://maven.apache.org/download.cgi"
    echo ""
    exit 1
fi

echo "Cleaning previous build..."
mvn clean

echo ""
echo "Building application (this may take a few minutes)..."
mvn package

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Build failed!"
    echo "Please check the error messages above."
    echo ""
    exit 1
fi

echo ""
echo "========================================"
echo "  Build Successful!"
echo "========================================"
echo ""
echo "Application JAR created at:"
echo "  target/task-manager-app-1.0.0-executable.jar"
echo ""
echo "To run the application, execute:"
echo "  ./run.sh"
echo ""
