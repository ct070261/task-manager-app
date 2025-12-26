@echo off
REM Build script for Task Manager Application (Windows)

echo ========================================
echo   Building Task Manager Application
echo ========================================
echo.

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH!
    echo.
    echo Please install Maven and try again.
    echo Download from: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

echo Cleaning previous build...
call mvn clean

echo.
echo Building application (this may take a few minutes)...
call mvn package

if errorlevel 1 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above.
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Build Successful!
echo ========================================
echo.
echo Application JAR created at:
echo   target\task-manager-app-1.0.0-executable.jar
echo.
echo To run the application, execute:
echo   run.bat
echo.
pause
