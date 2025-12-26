#!/bin/bash
# Task Manager Application Launcher for Unix/Linux/Mac
# This script runs the Task Manager application

echo "========================================"
echo "  Task Manager Application"
echo "========================================"
echo ""

# Check if JAR file exists
if [ ! -f "target/task-manager-app-1.0.0-executable.jar" ]; then
    echo "ERROR: Application JAR file not found!"
    echo ""
    echo "Please build the application first by running:"
    echo "  mvn clean package"
    echo ""
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH!"
    echo ""
    echo "Please install JDK 17 or higher and try again."
    echo "Download from: https://adoptium.net/"
    echo ""
    exit 1
fi

echo "Starting Task Manager Application..."
echo ""

# Run the application
java -jar target/task-manager-app-1.0.0-executable.jar

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Application failed to start!"
    echo "Please check if MySQL is running and database is set up correctly."
    echo ""
    exit 1
fi
