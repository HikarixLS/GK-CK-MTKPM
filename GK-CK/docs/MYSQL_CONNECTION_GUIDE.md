# � HƯỚNG DẪN KẾT NỐI MYSQL VỚI XAMPP

> **Lưu ý:** Hướng dẫn này dành cho XAMPP (Apache + MySQL + PHP), không phải MySQL Server độc lập.

## 📋 **THÔNG TIN KẾT NỐI XAMPP**

### 🔧 **Cấu hình mặc định XAMPP:**
| Thông số | Giá trị mặc định |
|----------|------------------|
| **Host** | `localhost` |
| **Port** | `3306` |
| **Username** | `root` |
| **Password** | *(để trống)* |

## ⚡ **HƯỚNG DẪN NHANH**

Khi chạy `scripts\setup.bat`, chỉ cần **nhấn Enter** cho tất cả các prompt:

```bash
MySQL Host (default: localhost): [ENTER]
MySQL Port (default: 3306): [ENTER]
MySQL Username (default: root): [ENTER]
Enter MySQL password for user root: [ENTER]
```

## 🎯 **CHI TIẾT TỪNG BƯỚC**

### **Bước 1: Khởi động XAMPP**
```bash
# Mở XAMPP Control Panel
# Khởi động Apache và MySQL
```

### **Bước 2: Chạy setup database**
```bash
scripts\setup.bat
```

### **Bước 3: Nhập thông tin kết nối**

#### **MySQL Host:**
- Mặc định: `localhost`
- **Khuyến nghị:** Nhấn Enter

#### **MySQL Port:**
- Mặc định: `3306`
- **Khuyến nghị:** Nhấn Enter

#### **MySQL Username:**
- Mặc định: `root`
- **Khuyến nghị:** Nhấn Enter

#### **MySQL Password:**
- XAMPP mặc định: *để trống*
- **Khuyến nghị:** Nhấn Enter

## ⚠️ **XỬ LÝ SỰ CỐ**

### **Nếu XAMPP có password:**
1. Chỉ nhập password ở bước cuối
2. Các bước khác vẫn nhấn Enter
3. Không nhập password vào host/username

### **Nếu port khác 3306:**
1. Kiểm tra XAMPP Control Panel
2. Xem MySQL port trong Config
3. Nhập port chính xác

### **Nếu kết nối thất bại:**
```bash
# Kiểm tra MySQL đã chạy
# Restart XAMPP
# Chạy lại setup.bat
```

## 🔄 **CHẠY LẠI SETUP**

```bash
scripts\setup.bat
```
