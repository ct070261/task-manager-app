#!/bin/bash
# Create distribution package for Task Manager App (Unix/Linux/Mac)

echo "========================================"
echo "  Creating Distribution Package"
echo "========================================"
echo ""

# Check if JAR file exists
if [ ! -f "target/task-manager-app-1.0.0-executable.jar" ]; then
    echo "ERROR: JAR file not found!"
    echo ""
    echo "Please build the application first:"
    echo "  ./build.sh"
    echo ""
    exit 1
fi

# Create dist directory
echo "Creating distribution directory..."
rm -rf dist
mkdir -p dist

# Copy files
echo "Copying files..."
cp target/task-manager-app-1.0.0-executable.jar dist/
cp run.bat dist/
cp run.sh dist/
cp README.md dist/
cp BUILD.md dist/
cp INSTALL.md dist/

# Copy database folder
echo "Copying database files..."
cp -r database dist/

# Copy sample application.properties
echo "Creating sample configuration..."
cat > dist/application.properties.sample << 'EOF'
db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD
db.driver=com.mysql.cj.jdbc.Driver

app.name=Task Manager
app.version=1.0.0
EOF

# Create README for dist
echo "Creating distribution README..."
cat > dist/README-FIRST.txt << 'EOF'
# Task Manager Application v1.0.0

## Quick Start:

1. Install Java 17+ from https://adoptium.net/
2. Install MySQL 8.0+ from https://dev.mysql.com/downloads/mysql/
3. Import database: Run database/schema.sql in MySQL
4. Configure: Rename application.properties.sample to application.properties
5. Edit application.properties with your MySQL password
6. Run: 
   - Windows: Double-click run.bat
   - Linux/Mac: Run ./run.sh

## Full Documentation:
- Installation Guide: INSTALL.md
- Build Guide: BUILD.md
- User Guide: README.md

## Support:
https://github.com/ct070261/task-manager-app
EOF

# Make scripts executable
chmod +x dist/run.sh
chmod +x dist/build.sh 2>/dev/null || true

# Create ZIP archive
echo ""
echo "Creating ZIP archive..."

# Detect OS
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    zip -r TaskManager-1.0.0-macOS.zip dist/
    PACKAGE_NAME="TaskManager-1.0.0-macOS.zip"
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    zip -r TaskManager-1.0.0-Linux.zip dist/
    PACKAGE_NAME="TaskManager-1.0.0-Linux.zip"
else
    # Generic Unix
    zip -r TaskManager-1.0.0.zip dist/
    PACKAGE_NAME="TaskManager-1.0.0.zip"
fi

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "  Distribution Package Created!"
    echo "========================================"
    echo ""
    echo "Package location: $PACKAGE_NAME"
    echo "Package contents: dist/"
    echo ""
    echo "You can now distribute:"
    echo "  - $PACKAGE_NAME (for distribution)"
    echo "  - dist/ folder (for manual copy)"
    echo ""
else
    echo ""
    echo "WARNING: Could not create ZIP automatically."
    echo "Please manually create archive from the dist folder:"
    echo "  tar -czf TaskManager-1.0.0.tar.gz dist/"
    echo ""
fi

echo "Files included:"
ls -lh dist/
echo ""
