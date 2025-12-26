# Hướng dẫn Cài đặt và Chạy Task Manager (Dành cho Người dùng)

## 📥 Tải ứng dụng

### Cách 1: Tải từ GitHub Releases (Khuyến nghị)
1. Truy cập: https://github.com/ct070261/task-manager-app/releases
2. Tải file **TaskManager-1.0.0.zip** (hoặc phiên bản mới nhất)
3. Giải nén vào thư mục bất kỳ

### Cách 2: Build từ Source Code
Xem hướng dẫn chi tiết trong file [BUILD.md](BUILD.md)

## 📋 Yêu cầu hệ thống

### Phần mềm cần cài đặt:

#### 1. Java Runtime Environment (JRE) 17 hoặc cao hơn
- **Download:** https://adoptium.net/
- **Lưu ý:** Chỉ cần JRE (Java Runtime Environment), không cần JDK
- **Kiểm tra:** Mở Command Prompt (Windows) hoặc Terminal (Mac/Linux), gõ:
  ```bash
  java -version
  ```
  Phải hiển thị version 17 trở lên

#### 2. MySQL Server 8.0+
- **Download:** https://dev.mysql.com/downloads/mysql/
- **Hoặc XAMPP:** https://www.apachefriends.org/ (đã bao gồm MySQL)
- **Hoặc WAMP:** https://www.wampserver.com/ (chỉ Windows)

## 🔧 Cài đặt

### Bước 1: Cài đặt Java

1. **Tải JRE:**
   - Truy cập: https://adoptium.net/
   - Chọn Version: **17 - LTS** (hoặc cao hơn)
   - Chọn Operating System: Windows/macOS/Linux
   - Click **Download JRE**

2. **Cài đặt:**
   - Windows: Chạy file `.msi`, làm theo hướng dẫn
   - macOS: Mở file `.pkg`, làm theo hướng dẫn
   - Linux: Xem hướng dẫn trên website

3. **Kiểm tra:**
   ```bash
   java -version
   ```
   Nếu hiển thị lỗi "command not found", cần thêm Java vào PATH

### Bước 2: Cài đặt MySQL

#### Cách A: Cài MySQL Server (Khuyến nghị)

1. **Tải MySQL:**
   - Truy cập: https://dev.mysql.com/downloads/mysql/
   - Chọn version 8.0 hoặc cao hơn
   - Download MySQL Installer

2. **Cài đặt:**
   - Chạy MySQL Installer
   - Chọn "Developer Default" hoặc "Server only"
   - Làm theo wizard, đặt mật khẩu root (ghi nhớ mật khẩu này!)

3. **Khởi động MySQL:**
   - Windows: MySQL sẽ tự động chạy như service
   - Mac/Linux: 
     ```bash
     sudo systemctl start mysql  # Linux
     sudo mysql.server start     # Mac
     ```

#### Cách B: Sử dụng XAMPP (Đơn giản hơn)

1. **Tải XAMPP:**
   - Windows/Mac: https://www.apachefriends.org/
   - Chọn version có PHP 8.x

2. **Cài đặt:**
   - Chạy installer, chọn ít nhất MySQL và phpMyAdmin
   - Cài vào thư mục mặc định

3. **Khởi động:**
   - Mở XAMPP Control Panel
   - Click "Start" bên cạnh MySQL
   - Đợi MySQL báo "Running"

### Bước 3: Tạo Database

#### Sử dụng MySQL Workbench:

1. **Mở MySQL Workbench**
   - Kết nối đến MySQL Server (localhost:3306)
   - Username: `root`
   - Password: (mật khẩu bạn đã đặt khi cài MySQL)

2. **Tạo Database:**
   - File → Open SQL Script
   - Chọn file `database/schema.sql` (trong thư mục TaskManager đã giải nén)
   - Click icon ⚡ (Execute) hoặc nhấn `Ctrl+Shift+Enter`

3. **Kiểm tra:**
   - Refresh schema list (bên trái)
   - Thấy database `task_manager_db` với 2 bảng: `tasks` và `daily_reviews`

#### Sử dụng phpMyAdmin (nếu dùng XAMPP):

1. **Mở phpMyAdmin:**
   - Truy cập: http://localhost/phpmyadmin

2. **Import Database:**
   - Click tab "Import"
   - Click "Choose File", chọn file `database/schema.sql`
   - Click "Go" ở cuối trang

3. **Kiểm tra:**
   - Bên trái, thấy database `task_manager_db`

### Bước 4: Cấu hình kết nối Database

1. **Tạo file cấu hình:**
   - Mở thư mục TaskManager (nơi chứa file JAR)
   - Tạo file mới tên: `application.properties`
   - Mở bằng Notepad/TextEdit

2. **Nhập nội dung:**
   ```properties
   db.url=jdbc:mysql://localhost:3306/task_manager_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD
   db.driver=com.mysql.cj.jdbc.Driver
   ```

3. **Thay YOUR_MYSQL_PASSWORD:**
   - Nếu dùng XAMPP: Để trống (xóa YOUR_MYSQL_PASSWORD, chỉ giữ `db.password=`)
   - Nếu dùng MySQL Server: Thay bằng mật khẩu root của MySQL

4. **Lưu file**

**Lưu ý:** File `application.properties` phải nằm cùng thư mục với file `.jar`

### Bước 5: Chạy ứng dụng

#### Windows:
1. **Cách 1:** Double-click file `run.bat`
2. **Cách 2:** Mở Command Prompt, chạy:
   ```bash
   cd path\to\TaskManager
   java -jar task-manager-app-1.0.0-executable.jar
   ```

#### Mac/Linux:
1. **Cách 1:** Mở Terminal, chạy:
   ```bash
   cd path/to/TaskManager
   chmod +x run.sh
   ./run.sh
   ```
2. **Cách 2:** Chạy trực tiếp:
   ```bash
   java -jar task-manager-app-1.0.0-executable.jar
   ```

## ✅ Kiểm tra

Nếu mọi thứ OK, bạn sẽ thấy:
- ✅ Cửa sổ Task Manager xuất hiện
- ✅ Có thể thêm, sửa, xóa tasks
- ✅ Thống kê hiển thị đúng

## 🐛 Xử lý lỗi thường gặp

### Lỗi 1: "Java command not found"

**Nguyên nhân:** Java chưa được cài hoặc chưa thêm vào PATH

**Giải pháp:**
1. Kiểm tra Java đã cài chưa:
   - Windows: Vào `C:\Program Files\Java`, xem có thư mục JRE/JDK không
   - Mac: Chạy `/usr/libexec/java_home`
   
2. Thêm Java vào PATH:
   - Windows:
     - Control Panel → System → Advanced → Environment Variables
     - Thêm `C:\Program Files\Java\jre-17\bin` vào PATH
   - Mac/Linux: Thêm vào `.bashrc` hoặc `.zshrc`:
     ```bash
     export JAVA_HOME=/path/to/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```

### Lỗi 2: "Unable to connect to MySQL"

**Nguyên nhân:** MySQL chưa chạy hoặc thông tin kết nối sai

**Giải pháp:**
1. **Kiểm tra MySQL đã chạy:**
   - XAMPP: Mở Control Panel, xem MySQL có "Running" không
   - Windows Service: Services → MySQL → Status = Running
   - Linux: `sudo systemctl status mysql`

2. **Kiểm tra kết nối:**
   - Mở MySQL Workbench hoặc phpMyAdmin
   - Thử kết nối với username/password trong `application.properties`
   
3. **Kiểm tra file config:**
   - File `application.properties` phải cùng thư mục với JAR
   - Username/password phải đúng
   - Database `task_manager_db` đã được tạo

### Lỗi 3: "Table doesn't exist"

**Nguyên nhân:** Chưa chạy file `schema.sql`

**Giải pháp:**
1. Mở MySQL Workbench hoặc phpMyAdmin
2. Chạy file `database/schema.sql`
3. Kiểm tra:
   ```sql
   USE task_manager_db;
   SHOW TABLES;
   ```
   Phải thấy 2 bảng: `tasks` và `daily_reviews`

### Lỗi 4: Ứng dụng không hiển thị

**Giải pháp:**
1. Kiểm tra Java version >= 17
2. Kiểm tra có dùng đúng file `*-executable.jar` (14MB)
3. Chạy từ Command Prompt/Terminal để xem lỗi chi tiết
4. Kiểm tra JavaFX đã được bao gồm (file JAR phải ~14MB)

### Lỗi 5: "Class not found: com.taskmanager.Main"

**Giải pháp:**
- Tải lại file JAR
- Đảm bảo dùng file `task-manager-app-1.0.0-executable.jar`
- Không giải nén file JAR

## 📁 Cấu trúc thư mục

Sau khi cài đặt, bạn nên có:

```
TaskManager/
├── task-manager-app-1.0.0-executable.jar  (File chính - 14MB)
├── application.properties                 (Cấu hình database)
├── run.bat                                (Script chạy Windows)
├── run.sh                                 (Script chạy Mac/Linux)
├── database/
│   └── schema.sql                         (Database schema)
└── README.md                              (Hướng dẫn)
```

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Đọc lại phần "Xử lý lỗi thường gặp" ở trên
2. Kiểm tra Java version: `java -version` (phải >= 17)
3. Kiểm tra MySQL đang chạy
4. Tạo issue trên GitHub: https://github.com/ct070261/task-manager-app/issues

## 🎉 Hoàn tất!

Bây giờ bạn có thể:
- ✅ Quản lý tasks hàng ngày
- ✅ Theo dõi tiến độ công việc
- ✅ Đánh giá hiệu suất làm việc
- ✅ Xem thống kê và biểu đồ
- ✅ Xem lịch sử công việc

Chúc bạn sử dụng hiệu quả! 🚀
