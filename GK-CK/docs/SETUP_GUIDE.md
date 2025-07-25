# 🚀 HƯỚNG DẪN THIẾT LẬP - 3 BƯỚC

## ⚡ Khởi động nhanh

```bash
1. setup.bat              # Thiết lập cơ sở dữ liệu MySQL
2. compile.bat            # Biên dịch dự án  
3. run.bat                # Chọn GUI hoặc Console
```

## 📋 Yêu cầu

- ✅ **Java JDK 8+**
- ✅ **MySQL Server** (hoặc XAMPP/WAMP)
- ✅ **Windows** (cho file .bat)

## 🎯 Từng bước chi tiết

### Bước 1: Cài đặt MySQL
```bash
# Cách A: MySQL Server
https://dev.mysql.com/downloads/mysql/

# Cách B: XAMPP (Khuyến nghị cho người mới)
https://www.apachefriends.org/download.html
```

### Bước 2: Chạy thiết lập
```bash
setup.bat
# Script này sẽ:
# - Tạo cơ sở dữ liệu 'QuanLyKho'
# - Import schema và procedures
# - Tạo file database.properties
```

### Bước 3: Biên dịch & Chạy
```bash
# Tải mysql-connector-java.jar trước!
compile.bat

# Sau đó chạy ứng dụng
run.bat           # Chọn GUI hoặc Console
# HOẶC
run_gui.bat       # Chạy GUI trực tiếp
run_console.bat   # Chạy Console trực tiếp
```

## 🔧 Cấu hình thủ công

Nếu thiết lập tự động thất bại, hãy làm thủ công:

1. **Sao chép template**: `database/database.properties.template` → `database.properties`
2. **Chỉnh sửa cấu hình**: Điền thông tin kết nối MySQL của bạn
3. **Import SQL**: Import `database/database.sql` vào MySQL
4. **Tải JDBC**: Tải `mysql-connector-java.jar`

## 📁 Cấu trúc dự án

```
📦 Cấu trúc sạch sẽ & có tổ chức
├── 🎯 Mã nguồn Java (7 file)
│   ├── HangHoa.java, ThucPham.java, DienMay.java, SanhSu.java
│   ├── QuanLyKho.java, Main.java, QuanLyKhoGUI.java
│
├── 🗃️ Thư mục cơ sở dữ liệu
│   ├── database.sql              # Schema MySQL
│   ├── DatabaseConnection.java   # Trình quản lý kết nối
│   ├── HangHoaDAO.java          # Lớp truy cập dữ liệu
│   ├── database.properties.template # Template cấu hình
│   └── README.md                 # Tài liệu cơ sở dữ liệu
│
├── 🚀 Scripts (5 file)
│   ├── setup.bat, compile.bat, run.bat, run_gui.bat, run_console.bat
│
└── 📚 Tài liệu (4 file)
    ├── README.md, SETUP_GUIDE.md, DATABASE_GUIDE.md
    └── DATABASE_DETAILED_GUIDE.md
```

## 🎉 Xong rồi!

Siêu sạch sẽ, chỉ MySQL, thiết lập 3 bước! 🚀
