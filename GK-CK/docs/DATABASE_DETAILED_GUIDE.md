# HƯỚNG DẪN MYSQL CHO HỆ THỐNG QUẢN LÝ KHO

## 🎯 Tổng quan
Phiên bản MySQL của hệ thống quản lý kho cung cấp:
- ✅ **Dễ cài đặt**: MySQL miễn phí và có sẵn trong XAMPP/WAMP
- ✅ **Cross-platform**: Chạy được trên Windows, Linux, Mac
- ✅ **High Performance**: Tối ưu cho ứng dụng nhỏ và vừa
- ✅ **Community Support**: Tài liệu phong phú, cộng đồng lớn
- ✅ **Cloud Ready**: Dễ deploy lên cloud (AWS, Google Cloud, etc.)

## 🚀 Quick Start

### Bước 1: Cài đặt MySQL
```bash
# Option A: Tải MySQL Community Server
https://dev.mysql.com/downloads/mysql/

# Option B: Sử dụng XAMPP (khuyến nghị cho người mới)
https://www.apachefriends.org/download.html

# Option C: Sử dụng Docker
docker run --name mysql-kho -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql:8.0
```

### Bước 2: Setup Database
```bash
# Chạy script tự động
setup_mysql.bat

# Hoặc manual setup:
mysql -u root -p -e "CREATE DATABASE QuanLyKho_MySQL;"
mysql -u root -p QuanLyKho_MySQL < database_mysql.sql
```

### Bước 3: Compile và chạy
```bash
# Tải MySQL Connector/J và đặt tên mysql-connector-java.jar
compile_mysql.bat

# Chạy GUI
run_mysql_gui.bat

# Hoặc chạy Console
run_mysql_console.bat
```

## 📁 Cấu trúc files MySQL

```
MySQL Database Files:
├── database_mysql.sql          # Schema và stored procedures
├── database_mysql.properties   # Cấu hình kết nối
├── DatabaseConnectionMySQL.java # Connection manager
├── HangHoaDAOMySQL.java        # Data access layer
├── setup_mysql.bat             # Auto setup script
├── compile_mysql.bat           # Compile với MySQL
├── run_mysql_console.bat       # Chạy console
├── run_mysql_gui.bat           # Chạy GUI
└── mysql-connector-java.jar    # JDBC driver (cần tải)
```

## 🗃️ Database Schema

### Bảng chính
```sql
-- Bảng cha lưu thông tin chung
CREATE TABLE HangHoa (
    MaHang VARCHAR(10) PRIMARY KEY,
    TenHang VARCHAR(100) NOT NULL,
    LoaiHang ENUM('ThucPham', 'DienMay', 'SanhSu') NOT NULL,
    SoLuongTon INT NOT NULL DEFAULT 0,
    DonGia DECIMAL(15,2) NOT NULL,
    VAT DECIMAL(4,3) NOT NULL,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng con cho thực phẩm
CREATE TABLE ThucPham (
    MaHang VARCHAR(10) PRIMARY KEY,
    NgaySanXuat DATE NOT NULL,
    NgayHetHan DATE NOT NULL,
    NhaCungCap VARCHAR(100) NOT NULL,
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);

-- Bảng con cho điện máy
CREATE TABLE DienMay (
    MaHang VARCHAR(10) PRIMARY KEY,
    ThoiGianBaoHanh INT NOT NULL DEFAULT 12,
    CongSuat DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);

-- Bảng con cho sành sứ
CREATE TABLE SanhSu (
    MaHang VARCHAR(10) PRIMARY KEY,
    NhaSanXuat VARCHAR(100) NOT NULL,
    NgayNhapKho DATE NOT NULL,
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);
```

### Bảng audit
```sql
-- Bảng log audit
CREATE TABLE HangHoa_Audit (
    AuditID INT AUTO_INCREMENT PRIMARY KEY,
    MaHang VARCHAR(10),
    ThaoTac ENUM('INSERT', 'UPDATE', 'DELETE'),
    DuLieuCu JSON,
    DuLieuMoi JSON,
    NguoiThucHien VARCHAR(100) DEFAULT USER(),
    ThoiGian DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Stored Procedures

### Thêm hàng hóa
```sql
-- Thêm thực phẩm với validation
DELIMITER //
CREATE PROCEDURE SP_ThemThucPham(
    IN p_MaHang VARCHAR(10),
    IN p_TenHang VARCHAR(100),
    IN p_SoLuongTon INT,
    IN p_DonGia DECIMAL(15,2),
    IN p_NgaySanXuat DATE,
    IN p_NgayHetHan DATE,
    IN p_NhaCungCap VARCHAR(100)
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR' AS Result, 'Lỗi thêm thực phẩm!' AS Message;
    END;
    
    START TRANSACTION;
    
    -- Kiểm tra mã hàng đã tồn tại
    SELECT COUNT(*) INTO v_count FROM HangHoa WHERE MaHang = p_MaHang;
    IF v_count > 0 THEN
        SELECT 'ERROR' AS Result, 'Mã hàng đã tồn tại!' AS Message;
        ROLLBACK;
    ELSE
        -- Thêm vào bảng HangHoa
        INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT)
        VALUES (p_MaHang, p_TenHang, 'ThucPham', p_SoLuongTon, p_DonGia, 0.05);
        
        -- Thêm vào bảng ThucPham
        INSERT INTO ThucPham (MaHang, NgaySanXuat, NgayHetHan, NhaCungCap)
        VALUES (p_MaHang, p_NgaySanXuat, p_NgayHetHan, p_NhaCungCap);
        
        COMMIT;
        SELECT 'SUCCESS' AS Result, 'Thêm thực phẩm thành công!' AS Message;
    END IF;
END //
DELIMITER ;
```

### Tìm sản phẩm sắp hết hạn
```sql
DELIMITER //
CREATE PROCEDURE SP_TimSanPhamSapHetHan()
BEGIN
    SELECT h.MaHang, h.TenHang, tp.NgayHetHan,
           DATEDIFF(tp.NgayHetHan, CURDATE()) AS SoNgayConLai
    FROM HangHoa h
    JOIN ThucPham tp ON h.MaHang = tp.MaHang
    WHERE tp.NgayHetHan BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
    ORDER BY tp.NgayHetHan ASC;
END //
DELIMITER ;
```

## 📊 Views và Functions

### View tổng hợp
```sql
-- View hiển thị thông tin đầy đủ
CREATE VIEW VW_HangHoaChiTiet AS
SELECT 
    h.MaHang,
    h.TenHang,
    h.LoaiHang,
    h.SoLuongTon,
    h.DonGia,
    h.VAT,
    (h.DonGia * (1 + h.VAT)) AS GiaBanTinhVAT,
    tp.NgaySanXuat,
    tp.NgayHetHan,
    tp.NhaCungCap,
    dm.ThoiGianBaoHanh,
    dm.CongSuat,
    ss.NhaSanXuat,
    ss.NgayNhapKho,
    h.NgayTao,
    h.NgayCapNhat
FROM HangHoa h
LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang
LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang
LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang;
```

### Functions tiện ích
```sql
-- Function tính tổng giá trị kho
DELIMITER //
CREATE FUNCTION FN_TinhTongGiaTriKho() RETURNS DECIMAL(20,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE v_total DECIMAL(20,2) DEFAULT 0;
    SELECT SUM(SoLuongTon * DonGia * (1 + VAT)) INTO v_total FROM HangHoa;
    RETURN IFNULL(v_total, 0);
END //
DELIMITER ;
```

## 🔒 Triggers

### Audit triggers
```sql
-- Trigger audit cho INSERT
DELIMITER //
CREATE TRIGGER TR_HangHoa_Insert_Audit
AFTER INSERT ON HangHoa
FOR EACH ROW
BEGIN
    INSERT INTO HangHoa_Audit (MaHang, ThaoTac, DuLieuMoi)
    VALUES (NEW.MaHang, 'INSERT', JSON_OBJECT(
        'MaHang', NEW.MaHang,
        'TenHang', NEW.TenHang,
        'LoaiHang', NEW.LoaiHang,
        'SoLuongTon', NEW.SoLuongTon,
        'DonGia', NEW.DonGia,
        'VAT', NEW.VAT
    ));
END //
DELIMITER ;
```

## ⚙️ Cấu hình kết nối

### File database_mysql.properties
```properties
# MySQL Database Configuration
mysql.host=localhost
mysql.port=3306
mysql.database=QuanLyKho_MySQL
mysql.username=root
mysql.password=your_password_here

# Connection Pool Settings
mysql.initialPoolSize=5
mysql.maxPoolSize=20
mysql.acquireIncrement=2
mysql.maxIdleTime=3600
mysql.connectionTimeout=30000

# Advanced Settings
mysql.useSSL=false
mysql.allowPublicKeyRetrieval=true
mysql.serverTimezone=Asia/Ho_Chi_Minh
mysql.characterEncoding=UTF-8
```

### Lớp DatabaseConnectionMySQL
```java
public class DatabaseConnectionMySQL {
    private static DatabaseConnectionMySQL instance;
    private String url;
    private String username;
    private String password;
    
    private DatabaseConnectionMySQL() {
        loadProperties();
    }
    
    public static synchronized DatabaseConnectionMySQL getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionMySQL();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
```

## 🧪 Testing

### Test kết nối
```bash
# Test connection class
java -cp .;mysql-connector-java.jar DatabaseConnectionMySQL

# Test DAO operations
java -cp .;mysql-connector-java.jar HangHoaDAOMySQL
```

### Test SQL trực tiếp
```sql
-- Test stored procedures
CALL SP_ThemThucPham('TEST001', 'Test Product', 10, 25000, '2024-01-01', '2024-12-31', 'Test Supplier');
CALL SP_LayDanhSachHangHoa();
CALL SP_TimSanPhamSapHetHan();

-- Test views
SELECT * FROM VW_HangHoaChiTiet WHERE LoaiHang = 'ThucPham';

-- Test functions
SELECT FN_TinhTongGiaTriKho() AS TongGiaTriKho;
```

## 🔧 Troubleshooting

### Lỗi thường gặp

#### 1. Lỗi kết nối MySQL
```
Error: Access denied for user 'root'@'localhost'
Solution: Kiểm tra username/password trong database_mysql.properties
```

#### 2. Lỗi timezone
```
Error: The server time zone value 'xxx' is unrecognized
Solution: Thêm ?serverTimezone=Asia/Ho_Chi_Minh vào connection string
```

#### 3. Lỗi SSL
```
Error: SSL connection required
Solution: Thêm ?useSSL=false vào connection string
```

#### 4. Lỗi JDBC Driver
```
Error: No suitable driver found
Solution: Tải mysql-connector-java.jar từ https://dev.mysql.com/downloads/connector/j/
```

### Performance tuning
```sql
-- Tối ưu performance
-- 1. Thêm indexes
CREATE INDEX IDX_HangHoa_LoaiHang ON HangHoa(LoaiHang);
CREATE INDEX IDX_ThucPham_NgayHetHan ON ThucPham(NgayHetHan);
CREATE INDEX IDX_HangHoa_TenHang ON HangHoa(TenHang);

-- 2. Optimize tables
OPTIMIZE TABLE HangHoa;
OPTIMIZE TABLE ThucPham;
OPTIMIZE TABLE DienMay;
OPTIMIZE TABLE SanhSu;

-- 3. Analyze performance
EXPLAIN SELECT * FROM VW_HangHoaChiTiet WHERE LoaiHang = 'ThucPham';
```

## 🌐 Deployment Options

### Local Development
```bash
# XAMPP/WAMP
1. Cài XAMPP/WAMP
2. Start MySQL service
3. Import database_mysql.sql qua phpMyAdmin
4. Chạy ứng dụng
```

### Production
```bash
# MySQL Server
1. Cài MySQL Server 8.0+
2. Tạo user riêng cho ứng dụng
3. Set privileges phù hợp
4. Backup định kỳ
```

### Cloud Deployment
```bash
# AWS RDS
1. Tạo RDS MySQL instance
2. Cập nhật security groups
3. Update connection string
4. Deploy ứng dụng lên EC2

# Google Cloud SQL
1. Tạo Cloud SQL MySQL instance
2. Set authorized networks
3. Update connection properties
4. Deploy to App Engine/GKE
```

## 📈 Monitoring

### Database monitoring
```sql
-- Kiểm tra performance
SHOW PROCESSLIST;
SHOW ENGINE INNODB STATUS;

-- Kiểm tra size database
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'QuanLyKho_MySQL'
GROUP BY table_schema;

-- Audit log summary
SELECT ThaoTac, COUNT(*) as SoLuong, DATE(ThoiGian) as Ngay
FROM HangHoa_Audit
GROUP BY ThaoTac, DATE(ThoiGian)
ORDER BY Ngay DESC;
```

## 🔄 Migration từ SQL Server

### Bước chuyển đổi
1. **Export data** từ SQL Server
2. **Convert scripts** sang MySQL syntax
3. **Update Java code** để sử dụng MySQL DAO
4. **Test thoroughly** trước khi production
5. **Backup** data trước khi switch

### Script migration helper
```sql
-- Convert SQL Server data types to MySQL
-- NVARCHAR -> VARCHAR with utf8mb4
-- DATETIME -> DATETIME
-- MONEY -> DECIMAL(15,2)
-- IDENTITY -> AUTO_INCREMENT
```

## 📚 Tài liệu tham khảo

- **MySQL Official Docs**: https://dev.mysql.com/doc/
- **MySQL Connector/J**: https://dev.mysql.com/doc/connector-j/8.0/en/
- **MySQL Performance**: https://dev.mysql.com/doc/refman/8.0/en/optimization.html
- **MySQL Security**: https://dev.mysql.com/doc/refman/8.0/en/security.html

---

## ✨ Kết luận

Phiên bản MySQL của hệ thống quản lý kho cung cấp:
- 🎯 **Easy Setup**: Setup nhanh với XAMPP/WAMP
- 🚀 **High Performance**: Tối ưu cho ứng dụng SME
- 💰 **Cost Effective**: Miễn phí và open source
- 🌍 **Cross Platform**: Hỗ trợ đa nền tảng
- ☁️ **Cloud Ready**: Dễ dàng deploy lên cloud

Hệ thống đã sẵn sàng cho production với đầy đủ features enterprise như audit trail, stored procedures, triggers, và performance optimization!
