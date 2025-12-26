@echo off
REM Create distribution package for Task Manager App (Windows)

echo ========================================
echo   Creating Distribution Package
echo ========================================
echo.

REM Check if JAR file exists
if not exist "target\task-manager-app-1.0.0-executable.jar" (
    echo ERROR: JAR file not found!
    echo.
    echo Please build the application first:
    echo   build.bat
    echo.
    pause
    exit /b 1
)

REM Create dist directory
echo Creating distribution directory...
if exist dist rmdir /s /q dist
mkdir dist

REM Copy files
echo Copying files...
copy target\task-manager-app-1.0.0-executable.jar dist\
copy run.bat dist\
copy run.sh dist\
copy README.md dist\
copy BUILD.md dist\
copy INSTALL.md dist\

REM Copy database folder
echo Copying database files...
xcopy database dist\database\ /E /I /Y

REM Copy sample application.properties
echo Creating sample configuration...
(
echo # Task Manager Application Configuration
echo # 
echo # SECURITY WARNING: This configuration uses useSSL=false for simplicity.
echo # For production use, enable SSL with proper certificates:
echo # useSSL=true^&requireSSL=true^&verifyServerCertificate=true
echo.
echo db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false^&serverTimezone=UTC^&allowPublicKeyRetrieval=true
echo db.username=root
echo db.password=YOUR_MYSQL_PASSWORD
echo db.driver=com.mysql.cj.jdbc.Driver
echo.
echo app.name=Task Manager
echo app.version=1.0.0
) > dist\application.properties.sample

REM Create README for dist
echo Creating distribution README...
(
echo # Task Manager Application v1.0.0
echo.
echo ## Quick Start:
echo.
echo 1. Install Java 17+ from https://adoptium.net/
echo 2. Install MySQL 8.0+ from https://dev.mysql.com/downloads/mysql/
echo 3. Import database: Run database/schema.sql in MySQL
echo 4. Configure: Rename application.properties.sample to application.properties
echo 5. Edit application.properties with your MySQL password
echo 6. Run: 
echo    - Windows: Double-click run.bat
echo    - Linux/Mac: Run ./run.sh
echo.
echo ## Full Documentation:
echo - Installation Guide: INSTALL.md
echo - Build Guide: BUILD.md
echo - User Guide: README.md
echo.
echo ## Support:
echo https://github.com/ct070261/task-manager-app
) > dist\README-FIRST.txt

REM Create ZIP archive
echo.
echo Creating ZIP archive...

REM Use PowerShell to create ZIP
powershell -command "Compress-Archive -Path dist\* -DestinationPath TaskManager-1.0.0-Windows.zip -Force"

if errorlevel 1 (
    echo.
    echo WARNING: Could not create ZIP automatically.
    echo Please manually create ZIP from the dist folder.
    echo.
) else (
    echo.
    echo ========================================
    echo   Distribution Package Created!
    echo ========================================
    echo.
    echo Package location: TaskManager-1.0.0-Windows.zip
    echo Package contents: dist/
    echo.
    echo You can now distribute:
    echo   - TaskManager-1.0.0-Windows.zip (for distribution)
    echo   - dist/ folder (for manual copy)
    echo.
)

echo Files included:
dir /b dist
echo.

pause
