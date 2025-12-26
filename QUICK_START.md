# Quick Reference - Task Manager Build & Run

## 🚀 Cho Người Dùng Cuối (End Users)

### Yêu cầu:
- ✅ Java 17+ (JRE) - [Download](https://adoptium.net/)
- ✅ MySQL 8.0+ - [Download](https://dev.mysql.com/downloads/mysql/)

### Cài đặt nhanh:
1. Tải file ZIP từ Releases
2. Giải nén
3. Import database: `database/schema.sql`
4. Đổi tên `application.properties.sample` → `application.properties`
5. Sửa mật khẩu MySQL trong file
6. Chạy:
   - Windows: `run.bat`
   - Linux/Mac: `./run.sh`

**📖 Hướng dẫn chi tiết:** [INSTALL.md](INSTALL.md)

---

## 👨‍💻 Cho Nhà Phát Triển (Developers)

### Yêu cầu:
- ✅ JDK 17+ - [Download](https://adoptium.net/)
- ✅ Maven 3.6+ - [Download](https://maven.apache.org/download.cgi)
- ✅ MySQL 8.0+

### Build nhanh:

#### Cách 1: Script tự động (Khuyến nghị)
```bash
# Windows
build.bat

# Linux/Mac
chmod +x build.sh
./build.sh
```

#### Cách 2: Maven trực tiếp
```bash
mvn clean package
```

#### Cách 3: IntelliJ IDEA
1. Open project
2. Maven → Lifecycle → clean
3. Maven → Lifecycle → package

### Chạy từ source:

#### Cách 1: Maven JavaFX Plugin
```bash
mvn javafx:run
```

#### Cách 2: JAR đã build
```bash
java -jar target/task-manager-app-1.0.0-executable.jar
```

#### Cách 3: IntelliJ IDEA
Run `Main.java` directly

### Tạo package phân phối:
```bash
# Windows
create-dist.bat

# Linux/Mac
./create-dist.sh
```

**📖 Hướng dẫn chi tiết:** [BUILD.md](BUILD.md)

---

## 📦 File Output

### Sau khi build (`mvn clean package`):
- `target/task-manager-app-1.0.0.jar` - JAR thông thường (75KB)
- `target/task-manager-app-1.0.0-executable.jar` - Fat JAR (14MB) ⭐

### Sau khi tạo dist (`./create-dist.sh`):
- `dist/` - Thư mục phân phối
- `TaskManager-1.0.0-*.zip` - Package đã nén

---

## 🛠️ Các lệnh hữu ích

### Maven Commands:
```bash
# Build
mvn clean package                 # Build với dependencies
mvn clean package -DskipTests     # Build không test
mvn clean install                 # Build và install vào .m2

# Run
mvn javafx:run                    # Chạy ứng dụng
mvn clean                         # Xóa target/

# Debug
mvn dependency:tree               # Xem dependency tree
mvn help:effective-pom            # Xem effective POM
```

### Git Commands:
```bash
# Clone
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app

# Pull latest
git pull origin main
```

### Run Commands:
```bash
# Direct JAR
java -jar target/task-manager-app-1.0.0-executable.jar

# With specific heap size
java -Xmx512m -jar target/task-manager-app-1.0.0-executable.jar

# With debug
java -jar target/task-manager-app-1.0.0-executable.jar --debug
```

---

## 📂 Cấu trúc Thư mục

```
task-manager-app/
├── src/                          # Source code
│   ├── main/java/               # Java files
│   └── main/resources/          # Resources (FXML, CSS, properties)
├── database/                     # Database schema
│   └── schema.sql
├── target/                       # Build output (gitignored)
│   ├── *.jar                    # JAR files
│   └── classes/                 # Compiled classes
├── dist/                         # Distribution package (gitignored)
├── pom.xml                       # Maven configuration
├── build.sh / build.bat          # Build scripts
├── run.sh / run.bat              # Run scripts
├── create-dist.sh / .bat         # Distribution scripts
├── BUILD.md                      # Build guide
├── INSTALL.md                    # Installation guide
└── README.md                     # Main documentation
```

---

## 🔥 Troubleshooting

| Lỗi | Giải pháp |
|-----|-----------|
| `java: command not found` | Cài Java và thêm vào PATH |
| `mvn: command not found` | Cài Maven và thêm vào PATH |
| `Unable to connect to MySQL` | Khởi động MySQL service |
| `Table doesn't exist` | Chạy `database/schema.sql` |
| `JavaFX not found` | Build lại: `mvn clean package` |
| `Main class not found` | Dùng file `*-executable.jar` |

**📖 Troubleshooting chi tiết:** [BUILD.md](BUILD.md#troubleshooting-build-issues) & [INSTALL.md](INSTALL.md#xử-lý-lỗi-thường-gặp)

---

## 📞 Hỗ trợ

- **GitHub Issues:** https://github.com/ct070261/task-manager-app/issues
- **Documentation:** README.md, BUILD.md, INSTALL.md

---

## ⚡ Quick Commands Cheat Sheet

```bash
# Full workflow - Developers
git clone https://github.com/ct070261/task-manager-app.git
cd task-manager-app
mvn clean package
java -jar target/task-manager-app-1.0.0-executable.jar

# Or using scripts
./build.sh
./run.sh

# Create distribution
./create-dist.sh

# Full workflow - End Users
unzip TaskManager-1.0.0.zip
cd dist
# Import schema.sql vào MySQL
cp application.properties.sample application.properties
# Edit application.properties
./run.sh
```

---

**Version:** 1.0.0  
**Last Updated:** 2025-12-26
