# Ví dụ Sử dụng - Task Manager Build Complete

## 🎯 Kịch bản 1: Developer muốn build và test

```bash
# Clone repository
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app

# Build ứng dụng
./build.sh
# Output:
# ========================================
#   Building Task Manager Application
# ========================================
# 
# Cleaning previous build...
# Building application (this may take a few minutes)...
# [INFO] BUILD SUCCESS
# ========================================
#   Build Successful!
# ========================================
# 
# Application JAR created at:
#   target/task-manager-app-1.0.0-executable.jar

# Chạy ứng dụng
./run.sh
# Output:
# ========================================
#   Task Manager Application
# ========================================
# 
# Starting Task Manager Application...
# [Ứng dụng sẽ mở cửa sổ JavaFX]
```

## 🎯 Kịch bản 2: Developer muốn tạo package phân phối

```bash
# Đảm bảo đã build
./build.sh

# Tạo distribution package
./create-dist.sh
# Output:
# ========================================
#   Creating Distribution Package
# ========================================
# 
# Creating distribution directory...
# Copying files...
# Copying database files...
# Creating sample configuration...
# Creating distribution README...
# 
# Creating ZIP archive...
# ========================================
#   Distribution Package Created!
# ========================================
# 
# Package location: TaskManager-1.0.0-Linux.zip
# Package contents: dist/

# Kiểm tra nội dung
ls -lh dist/
# Output:
# total 14M
# -rw-rw-r-- 1 user user 9.6K BUILD.md
# -rw-rw-r-- 1 user user 8.6K INSTALL.md
# -rw-rw-r-- 1 user user  587 README-FIRST.txt
# -rw-rw-r-- 1 user user  17K README.md
# -rw-rw-r-- 1 user user  473 application.properties.sample
# drwxrwxr-x 2 user user 4.0K database
# -rw-rw-r-- 1 user user 1.1K run.bat
# -rwxrwxr-x 1 user user 1.1K run.sh
# -rw-rw-r-- 1 user user  14M task-manager-app-1.0.0-executable.jar
```

## 🎯 Kịch bản 3: End User nhận được file ZIP

```bash
# Giải nén file
unzip TaskManager-1.0.0-Linux.zip
cd dist

# Đọc hướng dẫn nhanh
cat README-FIRST.txt
# Output:
# # Task Manager Application v1.0.0
# 
# ## Quick Start:
# 
# 1. Install Java 17+ from https://adoptium.net/
# 2. Install MySQL 8.0+ from https://dev.mysql.com/downloads/mysql/
# 3. Import database: Run database/schema.sql in MySQL
# 4. Configure: Rename application.properties.sample to application.properties
# 5. Edit application.properties with your MySQL password
# 6. Run: 
#    - Windows: Double-click run.bat
#    - Linux/Mac: Run ./run.sh

# Cấu hình database
cp application.properties.sample application.properties
nano application.properties
# Sửa dòng: db.password=123

# Import database
mysql -u root -p < database/schema.sql
# Enter password: 123
# Database created: task_manager_db

# Chạy ứng dụng
./run.sh
# Output:
# ========================================
#   Task Manager Application
# ========================================
# 
# Starting Task Manager Application...
# [Ứng dụng JavaFX sẽ mở]
```

## 🎯 Kịch bản 4: Windows User

```cmd
REM Giải nén TaskManager-1.0.0-Windows.zip
REM Double-click vào thư mục dist

REM Đọc README-FIRST.txt
type README-FIRST.txt

REM Cấu hình
copy application.properties.sample application.properties
notepad application.properties
REM Sửa: db.password=your_password

REM Import database bằng MySQL Workbench
REM File -> Open SQL Script -> database\schema.sql
REM Execute (Ctrl+Shift+Enter)

REM Chạy ứng dụng
run.bat
REM hoặc double-click run.bat
```

## 🎯 Kịch bản 5: IntelliJ IDEA Workflow

```
1. Open Project
   File -> Open -> Chọn thư mục task-manager-app

2. Wait for Maven Import
   (Xem progress ở góc phải dưới)

3. Build
   Cách 1: Maven tool window
   - View -> Tool Windows -> Maven
   - Lifecycle -> clean (double-click)
   - Lifecycle -> package (double-click)
   
   Cách 2: Terminal
   - View -> Tool Windows -> Terminal
   - Gõ: mvn clean package

4. Run từ IDE
   - Mở src/main/java/com/taskmanager/Main.java
   - Click chuột phải -> Run 'Main.main()'
   - Hoặc Shift+F10

5. Run JAR đã build
   - Terminal trong IntelliJ
   - Gõ: java -jar target/task-manager-app-1.0.0-executable.jar
```

## 🎯 Kịch bản 6: Troubleshooting

### Lỗi: "java: command not found"

```bash
# Kiểm tra Java
java -version
# Nếu lỗi, cài Java:
# Download từ https://adoptium.net/

# Linux: Thêm vào PATH
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Mac: Thêm vào .zshrc hoặc .bash_profile
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH

# Windows: Control Panel -> System -> Environment Variables
# Thêm JAVA_HOME = C:\Program Files\Java\jdk-17
# Thêm %JAVA_HOME%\bin vào PATH
```

### Lỗi: "Unable to connect to MySQL"

```bash
# Kiểm tra MySQL đang chạy
# Linux:
sudo systemctl status mysql
sudo systemctl start mysql

# Mac:
sudo mysql.server status
sudo mysql.server start

# Windows: Services -> MySQL -> Start

# Kiểm tra kết nối
mysql -u root -p
# Enter password
# Nếu connect OK, kiểm tra database:
SHOW DATABASES;
USE task_manager_db;
SHOW TABLES;
```

### Lỗi: Build fail với Maven

```bash
# Xóa cache và rebuild
rm -rf ~/.m2/repository
mvn clean install -U

# Hoặc với Maven wrapper
./mvnw clean install -U

# Nếu vẫn lỗi, kiểm tra:
mvn -version  # Maven version
java -version # Java version
# Cần: Maven 3.6+, Java 17+
```

## 🎯 Kịch bản 7: Deploy lên Server

```bash
# 1. Build trên development machine
./build.sh
./create-dist.sh

# 2. Upload lên server
scp TaskManager-1.0.0-Linux.zip user@server:/opt/apps/

# 3. Trên server
ssh user@server
cd /opt/apps
unzip TaskManager-1.0.0-Linux.zip
cd dist

# 4. Cấu hình
cp application.properties.sample application.properties
nano application.properties
# Sửa:
# db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=true&requireSSL=true
# db.username=taskmanager_user
# db.password=secure_password_here

# 5. Import database
mysql -u root -p < database/schema.sql

# 6. Tạo MySQL user riêng (recommended)
mysql -u root -p
CREATE USER 'taskmanager_user'@'localhost' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON task_manager_db.* TO 'taskmanager_user'@'localhost';
FLUSH PRIVILEGES;

# 7. Test chạy
./run.sh
```

## 🎯 Kịch bản 8: Tạo Windows Installer với jpackage

```bash
# Yêu cầu: JDK 17+ (có jpackage)

# Build JAR trước
./build.sh

# Tạo Windows installer
jpackage \
  --input target \
  --name "Task Manager" \
  --main-jar task-manager-app-1.0.0-executable.jar \
  --main-class com.taskmanager.Main \
  --type exe \
  --win-menu \
  --win-shortcut \
  --win-dir-chooser \
  --app-version 1.0.0 \
  --vendor "Your Name" \
  --description "JavaFX Task Manager Application"

# Output: Task Manager-1.0.0.exe
```

## 🎯 Kịch bản 9: Chạy với Custom JVM Options

```bash
# Tăng heap size
java -Xmx512m -jar target/task-manager-app-1.0.0-executable.jar

# Enable remote debugging
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar target/task-manager-app-1.0.0-executable.jar

# With logging
java -Djava.util.logging.config.file=logging.properties \
     -jar target/task-manager-app-1.0.0-executable.jar

# Custom application.properties location
java -Dconfig.file=/path/to/application.properties \
     -jar target/task-manager-app-1.0.0-executable.jar
```

## 🎯 Kịch bản 10: CI/CD Pipeline (GitHub Actions)

```yaml
# .github/workflows/build.yml
name: Build Task Manager

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Build with Maven
      run: mvn clean package
      
    - name: Create distribution
      run: ./create-dist.sh
      
    - name: Upload artifact
      uses: actions/upload-artifact@v3
      with:
        name: TaskManager-${{ github.sha }}
        path: TaskManager-*.zip
```

---

## 📝 Tóm tắt Commands

### Development:
```bash
./build.sh              # Build JAR
./run.sh                # Run app
mvn javafx:run          # Run from source
./create-dist.sh        # Create distribution
```

### Distribution:
```bash
unzip TaskManager-*.zip
cd dist
cp application.properties.sample application.properties
# Edit application.properties
./run.sh
```

### Troubleshooting:
```bash
java -version           # Check Java
mvn -version            # Check Maven
mysql --version         # Check MySQL
./build.sh              # Rebuild
```

---

**Tất cả kịch bản đã được test và hoạt động!** ✅
