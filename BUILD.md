# Hướng dẫn Build và Phân phối Task Manager App

## Tổng quan

Tài liệu này hướng dẫn cách build Task Manager App thành ứng dụng standalone có thể chạy trên bất kỳ máy tính nào có Java.

## Yêu cầu để Build

### Phần mềm cần thiết:
1. **JDK 17 hoặc cao hơn** (JDK 21 khuyến nghị)
   - Download: https://adoptium.net/
   - Kiểm tra: `java -version`

2. **Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Kiểm tra: `mvn -version`

3. **Git** (để clone repository)
   - Download: https://git-scm.com/

## Các phương pháp Build

### Phương pháp 1: Sử dụng Script Build (Đơn giản nhất)

#### Windows:
```bash
# Clone repository (nếu chưa có)
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app

# Chạy script build
build.bat
```

#### Linux/Mac:
```bash
# Clone repository (nếu chưa có)
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app

# Cấp quyền thực thi
chmod +x build.sh

# Chạy script build
./build.sh
```

**Kết quả:** File JAR sẽ được tạo tại `target/task-manager-app-1.0.0-executable.jar`

### Phương pháp 2: Sử dụng Maven Command Line

```bash
# Clone repository
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app

# Clean và build
mvn clean package

# Hoặc skip tests (nếu có)
mvn clean package -DskipTests
```

**Kết quả:** 
- File JAR executable: `target/task-manager-app-1.0.0-executable.jar` (14MB)
- File JAR thông thường: `target/task-manager-app-1.0.0.jar` (75KB)

### Phương pháp 3: Sử dụng IntelliJ IDEA

1. **Mở Project**
   - File → Open → Chọn thư mục `task-manager-app`
   
2. **Build**
   - View → Tool Windows → Maven
   - Trong Maven tab, expand `task-manager-app`
   - Expand `Lifecycle`
   - Double-click `clean`
   - Double-click `package`

3. **Hoặc dùng Terminal trong IntelliJ**
   - View → Tool Windows → Terminal
   - Chạy: `mvn clean package`

## Chạy ứng dụng đã build

### Phương pháp 1: Sử dụng Script Run (Khuyến nghị)

#### Windows:
```bash
run.bat
```

#### Linux/Mac:
```bash
chmod +x run.sh
./run.sh
```

### Phương pháp 2: Chạy trực tiếp với Java

```bash
java -jar target/task-manager-app-1.0.0-executable.jar
```

## Phân phối ứng dụng

### Cách 1: Phân phối JAR file

**File cần thiết:**
```
task-manager-app/
├── target/task-manager-app-1.0.0-executable.jar  (File chính - 14MB)
├── run.bat                                        (Script chạy Windows)
├── run.sh                                         (Script chạy Linux/Mac)
└── README.md                                      (Hướng dẫn)
```

**Các bước phân phối:**

1. **Tạo thư mục phân phối:**
```bash
# Windows
mkdir dist
copy target\task-manager-app-1.0.0-executable.jar dist\
copy run.bat dist\
copy run.sh dist\
copy README.md dist\

# Linux/Mac
mkdir dist
cp target/task-manager-app-1.0.0-executable.jar dist/
cp run.bat dist/
cp run.sh dist/
cp README.md dist/
```

2. **Tạo file ZIP:**
```bash
# Windows (PowerShell)
Compress-Archive -Path dist\* -DestinationPath TaskManager-1.0.0.zip

# Linux/Mac
zip -r TaskManager-1.0.0.zip dist/*

# Hoặc dùng tar
tar -czf TaskManager-1.0.0.tar.gz dist/*
```

3. **Phân phối:**
   - Upload lên GitHub Releases
   - Chia sẻ qua email, Google Drive, v.v.
   - Host trên website

### Cách 2: Tạo Installer (Nâng cao)

Sử dụng **jpackage** (có sẵn từ JDK 14+) để tạo installer native cho từng platform:

#### Windows Installer (.exe):
```bash
jpackage --input target --name TaskManager --main-jar task-manager-app-1.0.0-executable.jar --main-class com.taskmanager.Main --type exe --win-menu --win-shortcut
```

#### macOS Installer (.dmg):
```bash
jpackage --input target --name TaskManager --main-jar task-manager-app-1.0.0-executable.jar --main-class com.taskmanager.Main --type dmg
```

#### Linux Package (.deb):
```bash
jpackage --input target --name TaskManager --main-jar task-manager-app-1.0.0-executable.jar --main-class com.taskmanager.Main --type deb
```

## Hướng dẫn cài đặt cho người dùng cuối

### Yêu cầu hệ thống:
1. **Java Runtime Environment (JRE) 17+**
   - Download: https://adoptium.net/
   - Chỉ cần JRE, không cần JDK

2. **MySQL Server 8.0+**
   - Download: https://dev.mysql.com/downloads/mysql/
   - Hoặc XAMPP/WAMP

### ⚠️ Lưu ý Bảo mật

Khi phân phối ứng dụng, cần lưu ý các vấn đề bảo mật sau:

1. **Kết nối Database:**
   - Cấu hình mặc định sử dụng `useSSL=false` cho đơn giản (chỉ phù hợp với localhost)
   - Với môi trường production hoặc kết nối qua mạng, bật SSL:
     ```properties
     db.url=jdbc:mysql://hostname:3306/task_manager_db?useSSL=true&requireSSL=true&verifyServerCertificate=true
     ```

2. **Bảo vệ Thông tin:**
   - Không hard-code mật khẩu trong source code
   - Sử dụng file `application.properties` bên ngoài JAR
   - Bảo vệ file cấu hình với quyền hạn phù hợp (chmod 600)

3. **Khuyến nghị:**
   - Sử dụng mật khẩu mạnh cho MySQL
   - Không dùng tài khoản `root` cho production
   - Tạo user riêng với quyền hạn tối thiểu cần thiết

### Các bước cài đặt:

1. **Cài đặt Java**
   - Download và cài đặt JRE 17 hoặc cao hơn
   - Kiểm tra: Mở Command Prompt/Terminal, gõ `java -version`

2. **Cài đặt MySQL**
   - Download và cài đặt MySQL Server
   - Khởi động MySQL Service
   - Import database từ file `database/schema.sql`

3. **Cấu hình kết nối Database**
   - Giải nén file TaskManager-1.0.0.zip
   - Tạo file `application.properties` cùng thư mục với JAR file:
   ```properties
   db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=your_password
   db.driver=com.mysql.cj.jdbc.Driver
   ```

4. **Chạy ứng dụng**
   - Windows: Double-click `run.bat`
   - Linux/Mac: Chạy `./run.sh` trong Terminal
   - Hoặc: `java -jar task-manager-app-1.0.0-executable.jar`

## Cấu trúc File JAR

File `task-manager-app-1.0.0-executable.jar` là một **Fat JAR** (Uber JAR) chứa:

- ✅ Tất cả class files của ứng dụng
- ✅ JavaFX dependencies (javafx-controls, javafx-fxml)
- ✅ MySQL Connector/J
- ✅ ControlsFX library
- ✅ Tất cả resource files (FXML, CSS, properties)
- ✅ MANIFEST.MF với Main-Class đã được set

**Kích thước:** ~14MB (vì bao gồm tất cả dependencies)

## Build Configuration trong pom.xml

Ứng dụng sử dụng **Maven Shade Plugin** để tạo Fat JAR:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.1</version>
    <configuration>
        <shadedArtifactAttached>true</shadedArtifactAttached>
        <shadedClassifierName>executable</shadedClassifierName>
        <transformers>
            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                <mainClass>com.taskmanager.Main</mainClass>
            </transformer>
        </transformers>
    </configuration>
</plugin>
```

## Troubleshooting Build Issues

### Lỗi: "Maven command not found"

**Giải pháp:**
- Cài đặt Maven từ https://maven.apache.org/download.cgi
- Thêm Maven vào PATH environment variable
- Kiểm tra: `mvn -version`

### Lỗi: "JAVA_HOME not set"

**Giải pháp:**
- Set JAVA_HOME environment variable
  ```bash
  # Windows
  setx JAVA_HOME "C:\Program Files\Java\jdk-17"
  
  # Linux/Mac
  export JAVA_HOME=/path/to/jdk-17
  ```

### Lỗi: Build thành công nhưng JAR không chạy

**Kiểm tra:**
1. Đảm bảo sử dụng file `*-executable.jar` (14MB), không phải file 75KB
2. Kiểm tra Java version: `java -version` (phải >= 17)
3. Kiểm tra MySQL đã khởi động
4. Kiểm tra file `application.properties` đúng thông tin

### Lỗi: "Dependencies download failed"

**Giải pháp:**
1. Kiểm tra kết nối internet
2. Xóa Maven cache:
   ```bash
   # Windows
   rmdir /s /q %USERPROFILE%\.m2\repository
   
   # Linux/Mac
   rm -rf ~/.m2/repository
   ```
3. Build lại: `mvn clean package -U`

### Build quá chậm

**Giải pháp:**
1. Sử dụng Maven daemon:
   ```bash
   mvnd clean package
   ```
2. Tăng heap size cho Maven:
   ```bash
   set MAVEN_OPTS=-Xmx1024m
   mvn clean package
   ```
3. Skip tests (nếu có):
   ```bash
   mvn clean package -DskipTests
   ```

## Advanced: Tối ưu hóa JAR size

Nếu muốn giảm kích thước JAR file:

1. **Loại bỏ dependencies không cần thiết**
   - Review `pom.xml` và xóa dependencies không dùng

2. **Sử dụng ProGuard để minify**
   - Thêm ProGuard plugin vào `pom.xml`
   - Cấu hình để giữ Main class và JavaFX classes

3. **Tách dependencies ra ngoài**
   - Không dùng Fat JAR
   - Ship dependencies trong thư mục `lib/`
   - Update classpath trong run script

## Kết luận

Với hướng dẫn trên, bạn có thể:
- ✅ Build ứng dụng thành JAR file standalone
- ✅ Chạy ứng dụng trên bất kỳ máy tính nào có Java
- ✅ Phân phối ứng dụng cho người dùng khác
- ✅ Tạo installer cho các platform khác nhau

**Lưu ý quan trọng:**
- File JAR chứa code nhưng KHÔNG chứa MySQL
- Người dùng cuối phải tự cài MySQL và import database
- Cần cấu hình `application.properties` đúng thông tin database

Chúc bạn thành công! 🚀
