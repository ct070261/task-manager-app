@echo off
REM Task Manager Application Launcher for Windows
REM This script runs the Task Manager application

echo ========================================
echo   Task Manager Application
echo ========================================
echo.

REM Check if JAR file exists
if not exist "target\task-manager-app-1.0.0-executable.jar" (
    echo ERROR: Application JAR file not found!
    echo.
    echo Please build the application first by running:
    echo   mvn clean package
    echo.
    pause
    exit /b 1
)

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH!
    echo.
    echo Please install JDK 17 or higher and try again.
    echo Download from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo Starting Task Manager Application...
echo.

REM Run the application
java -jar target\task-manager-app-1.0.0-executable.jar

if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start!
    echo Please check if MySQL is running and database is set up correctly.
    echo.
    pause
)
