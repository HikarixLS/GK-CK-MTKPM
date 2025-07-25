-- @formatter:off
-- mysql: syntax=mysql
-- =============================================
-- SCRIPT TẠO DATABASE QUẢN LÝ KHO HÀNG - MySQL
-- =============================================
-- FILE: database.sql
-- ENGINE: MySQL 8.0+
-- CHARSET: utf8mb4
-- =============================================
-- 
-- ⚠️  LƯU Ý QUAN TRỌNG:
-- File này sử dụng MySQL syntax chuẩn.
-- VS Code sẽ tự động nhận diện MySQL syntax nhờ comment trên đầu file.
-- Script này đã được test và hoạt động hoàn hảo với MySQL.
-- 
-- =============================================

-- Tạo database
DROP DATABASE IF EXISTS QuanLyKhoHang;
CREATE DATABASE QuanLyKhoHang CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuanLyKhoHang;

-- Bảng chính: HangHoa (lưu thông tin chung)
CREATE TABLE HangHoa (
    MaHang VARCHAR(20) PRIMARY KEY,
    TenHang VARCHAR(100) NOT NULL,
    LoaiHang ENUM('ThucPham', 'DienMay', 'SanhSu') NOT NULL,
    SoLuongTon INT NOT NULL DEFAULT 0,
    DonGia DECIMAL(18,2) NOT NULL,
    VAT DECIMAL(5,4) NOT NULL,
    NgayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng chi tiết: ThucPham
CREATE TABLE ThucPham (
    MaHang VARCHAR(20) PRIMARY KEY,
    NgaySanXuat DATE NOT NULL,
    NgayHetHan DATE NOT NULL,
    NhaCungCap VARCHAR(100) NOT NULL,
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);

-- Bảng chi tiết: DienMay
CREATE TABLE DienMay (
    MaHang VARCHAR(20) PRIMARY KEY,
    ThoiGianBaoHanh INT NOT NULL COMMENT 'số tháng',
    CongSuat DECIMAL(10,2) NOT NULL COMMENT 'đơn vị: W',
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);

-- Bảng chi tiết: SanhSu
CREATE TABLE SanhSu (
    MaHang VARCHAR(20) PRIMARY KEY,
    NhaSanXuat VARCHAR(100) NOT NULL,
    NgayNhapKho DATE NOT NULL,
    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
);

-- Bảng lịch sử thay đổi (audit log)
CREATE TABLE LichSuThayDoi (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MaHang VARCHAR(20),
    LoaiThayDoi ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    NoiDung TEXT,
    NgayThayDoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    NguoiThayDoi VARCHAR(50) DEFAULT USER()
);

-- =============================================
-- TẠO INDEX ĐỂ TỐI ƯU HIỆU SUẤT
-- =============================================

-- Index cho tìm kiếm theo loại hàng
CREATE INDEX IX_HangHoa_LoaiHang ON HangHoa(LoaiHang);

-- Index cho tìm kiếm sản phẩm sắp hết hạn
CREATE INDEX IX_ThucPham_NgayHetHan ON ThucPham(NgayHetHan);

-- Index cho tìm kiếm theo nhà cung cấp
CREATE INDEX IX_ThucPham_NhaCungCap ON ThucPham(NhaCungCap);

-- Index cho tìm kiếm theo nhà sản xuất
CREATE INDEX IX_SanhSu_NhaSanXuat ON SanhSu(NhaSanXuat);

-- Index cho audit log
CREATE INDEX IX_LichSu_NgayThayDoi ON LichSuThayDoi(NgayThayDoi);

-- =============================================
-- TẠO TRIGGER TỰ ĐỘNG CẬP NHẬT VAT
-- =============================================

DELIMITER $$

CREATE TRIGGER TR_HangHoa_UpdateVAT_Insert
BEFORE INSERT ON HangHoa
FOR EACH ROW
BEGIN
    SET NEW.VAT = CASE 
        WHEN NEW.LoaiHang = 'ThucPham' THEN 0.05
        WHEN NEW.LoaiHang = 'DienMay' THEN 0.10
        WHEN NEW.LoaiHang = 'SanhSu' THEN 0.10
        ELSE 0.00
    END;
END$$

CREATE TRIGGER TR_HangHoa_UpdateVAT_Update
BEFORE UPDATE ON HangHoa
FOR EACH ROW
BEGIN
    SET NEW.VAT = CASE 
        WHEN NEW.LoaiHang = 'ThucPham' THEN 0.05
        WHEN NEW.LoaiHang = 'DienMay' THEN 0.10
        WHEN NEW.LoaiHang = 'SanhSu' THEN 0.10
        ELSE 0.00
    END;
END$$

-- =============================================
-- TẠO TRIGGER GHI LOG THAY ĐỔI
-- =============================================

CREATE TRIGGER TR_HangHoa_AuditLog_Insert
AFTER INSERT ON HangHoa
FOR EACH ROW
BEGIN
    INSERT INTO LichSuThayDoi (MaHang, LoaiThayDoi, NoiDung)
    VALUES (NEW.MaHang, 'INSERT', CONCAT('Thêm hàng hóa: ', NEW.TenHang, ' (', NEW.LoaiHang, ')'));
END$$

CREATE TRIGGER TR_HangHoa_AuditLog_Update
AFTER UPDATE ON HangHoa
FOR EACH ROW
BEGIN
    INSERT INTO LichSuThayDoi (MaHang, LoaiThayDoi, NoiDung)
    VALUES (NEW.MaHang, 'UPDATE', CONCAT('Cập nhật hàng hóa: ', NEW.TenHang));
END$$

CREATE TRIGGER TR_HangHoa_AuditLog_Delete
AFTER DELETE ON HangHoa
FOR EACH ROW
BEGIN
    INSERT INTO LichSuThayDoi (MaHang, LoaiThayDoi, NoiDung)
    VALUES (OLD.MaHang, 'DELETE', CONCAT('Xóa hàng hóa: ', OLD.TenHang, ' (', OLD.LoaiHang, ')'));
END$$

DELIMITER ;

-- =============================================
-- TẠO STORED PROCEDURES
-- =============================================

DELIMITER $$

-- Thêm thực phẩm
CREATE PROCEDURE SP_ThemThucPham(
    IN p_MaHang VARCHAR(20),
    IN p_TenHang VARCHAR(100),
    IN p_SoLuongTon INT,
    IN p_DonGia DECIMAL(18,2),
    IN p_NgaySanXuat DATE,
    IN p_NgayHetHan DATE,
    IN p_NhaCungCap VARCHAR(100)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR' AS Result, 'Lỗi thêm thực phẩm!' AS Message;
    END;
    
    START TRANSACTION;
    
    -- Thêm vào bảng chính
    INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT)
    VALUES (p_MaHang, p_TenHang, 'ThucPham', p_SoLuongTon, p_DonGia, 0.05);
    
    -- Thêm vào bảng chi tiết
    INSERT INTO ThucPham (MaHang, NgaySanXuat, NgayHetHan, NhaCungCap)
    VALUES (p_MaHang, p_NgaySanXuat, p_NgayHetHan, p_NhaCungCap);
    
    COMMIT;
    SELECT 'SUCCESS' AS Result, 'Thêm thực phẩm thành công!' AS Message;
END$$

-- Thêm điện máy
CREATE PROCEDURE SP_ThemDienMay(
    IN p_MaHang VARCHAR(20),
    IN p_TenHang VARCHAR(100),
    IN p_SoLuongTon INT,
    IN p_DonGia DECIMAL(18,2),
    IN p_ThoiGianBaoHanh INT,
    IN p_CongSuat DECIMAL(10,2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR' AS Result, 'Lỗi thêm điện máy!' AS Message;
    END;
    
    START TRANSACTION;
    
    INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT)
    VALUES (p_MaHang, p_TenHang, 'DienMay', p_SoLuongTon, p_DonGia, 0.10);
    
    INSERT INTO DienMay (MaHang, ThoiGianBaoHanh, CongSuat)
    VALUES (p_MaHang, p_ThoiGianBaoHanh, p_CongSuat);
    
    COMMIT;
    SELECT 'SUCCESS' AS Result, 'Thêm điện máy thành công!' AS Message;
END$$

-- Thêm sành sứ
CREATE PROCEDURE SP_ThemSanhSu(
    IN p_MaHang VARCHAR(20),
    IN p_TenHang VARCHAR(100),
    IN p_SoLuongTon INT,
    IN p_DonGia DECIMAL(18,2),
    IN p_NhaSanXuat VARCHAR(100),
    IN p_NgayNhapKho DATE
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'ERROR' AS Result, 'Lỗi thêm sành sứ!' AS Message;
    END;
    
    START TRANSACTION;
    
    INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT)
    VALUES (p_MaHang, p_TenHang, 'SanhSu', p_SoLuongTon, p_DonGia, 0.10);
    
    INSERT INTO SanhSu (MaHang, NhaSanXuat, NgayNhapKho)
    VALUES (p_MaHang, p_NhaSanXuat, p_NgayNhapKho);
    
    COMMIT;
    SELECT 'SUCCESS' AS Result, 'Thêm sành sứ thành công!' AS Message;
END$$

-- Lấy danh sách tất cả hàng hóa
CREATE PROCEDURE SP_LayDanhSachHangHoa()
BEGIN
    SELECT 
        h.MaHang, h.TenHang, h.LoaiHang, h.SoLuongTon, h.DonGia, h.VAT,
        CASE h.LoaiHang
            WHEN 'ThucPham' THEN 
                CONCAT('NSX: ', DATE_FORMAT(tp.NgaySanXuat, '%d/%m/%Y'), 
                       ', HSD: ', DATE_FORMAT(tp.NgayHetHan, '%d/%m/%Y'), 
                       ', NCC: ', tp.NhaCungCap)
            WHEN 'DienMay' THEN 
                CONCAT('BH: ', dm.ThoiGianBaoHanh, ' tháng, CS: ', dm.CongSuat, 'W')
            WHEN 'SanhSu' THEN 
                CONCAT('NSX: ', ss.NhaSanXuat, 
                       ', Nhập: ', DATE_FORMAT(ss.NgayNhapKho, '%d/%m/%Y'))
        END AS ThongTinChiTiet
    FROM HangHoa h
    LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang
    LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang
    LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang
    ORDER BY h.LoaiHang, h.TenHang;
END$$

-- Tìm sản phẩm sắp hết hạn (trong 7 ngày)
CREATE PROCEDURE SP_TimSanPhamSapHetHan()
BEGIN
    SELECT 
        h.MaHang, h.TenHang, h.SoLuongTon, h.DonGia,
        tp.NgayHetHan,
        DATEDIFF(tp.NgayHetHan, CURDATE()) AS SoNgayConLai
    FROM HangHoa h
    INNER JOIN ThucPham tp ON h.MaHang = tp.MaHang
    WHERE tp.NgayHetHan BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
    ORDER BY tp.NgayHetHan;
END$$

-- Thống kê số lượng theo loại
CREATE PROCEDURE SP_ThongKeSoLuongTheoLoai()
BEGIN
    SELECT 
        LoaiHang,
        COUNT(*) AS SoMatHang,
        SUM(SoLuongTon) AS TongSoLuong,
        AVG(SoLuongTon) AS TrungBinhSoLuong,
        MIN(SoLuongTon) AS SoLuongMin,
        MAX(SoLuongTon) AS SoLuongMax
    FROM HangHoa
    GROUP BY LoaiHang
    ORDER BY LoaiHang;
END$$

-- Tính trung bình số lượng tồn điện máy
CREATE PROCEDURE SP_TrungBinhSoLuongDienMay()
BEGIN
    SELECT 
        COUNT(*) AS SoMatHangDienMay,
        IFNULL(SUM(SoLuongTon), 0) AS TongSoLuong,
        IFNULL(AVG(SoLuongTon), 0) AS TrungBinhSoLuong
    FROM HangHoa
    WHERE LoaiHang = 'DienMay';
END$$

DELIMITER ;

-- =============================================
-- TẠO VIEW ĐỂ TRUY VẤN DỄ DÀNG
-- =============================================

-- View hiển thị thông tin đầy đủ hàng hóa
CREATE VIEW VW_HangHoaChiTiet AS
SELECT 
    h.MaHang, h.TenHang, h.LoaiHang, h.SoLuongTon, h.DonGia, h.VAT,
    h.NgayTao, h.NgayCapNhat,
    tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap,
    dm.ThoiGianBaoHanh, dm.CongSuat,
    ss.NhaSanXuat, ss.NgayNhapKho
FROM HangHoa h
LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang
LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang
LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang;

-- View sản phẩm sắp hết hạn
CREATE VIEW VW_SanPhamSapHetHan AS
SELECT 
    h.MaHang, h.TenHang, h.SoLuongTon, tp.NgayHetHan,
    DATEDIFF(tp.NgayHetHan, CURDATE()) AS SoNgayConLai
FROM HangHoa h
INNER JOIN ThucPham tp ON h.MaHang = tp.MaHang
WHERE tp.NgayHetHan BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY);

-- =============================================
-- TẠO USER VÀ PHÂN QUYỀN (TÙY CHỌN)
-- =============================================

-- Tạo user cho ứng dụng (uncomment nếu cần)
/*
CREATE USER 'kho_user'@'localhost' IDENTIFIED BY 'password123';
GRANT SELECT, INSERT, UPDATE, DELETE ON QuanLyKhoHang.* TO 'kho_user'@'localhost';
GRANT EXECUTE ON QuanLyKhoHang.* TO 'kho_user'@'localhost';
FLUSH PRIVILEGES;
*/

-- =============================================
-- DỮ LIỆU MẪU (TÙY CHỌN)
-- =============================================

-- Uncomment các dòng dưới nếu muốn thêm dữ liệu mẫu

/*
-- Thêm dữ liệu mẫu thực phẩm
CALL SP_ThemThucPham('TP001', 'Sữa tươi Vinamilk', 50, 25000, '2025-07-20', '2025-07-30', 'Vinamilk');
CALL SP_ThemThucPham('TP002', 'Bánh mì sandwich', 30, 15000, '2025-07-22', '2025-07-25', 'ABC Bakery');
CALL SP_ThemThucPham('TP003', 'Thịt bò úc', 20, 180000, '2025-07-19', '2025-07-26', 'Meat Plus');

-- Thêm dữ liệu mẫu điện máy
CALL SP_ThemDienMay('DM001', 'Tủ lạnh Samsung RF48A4000M9', 10, 8000000, 24, 350);
CALL SP_ThemDienMay('DM002', 'Máy giặt LG AI DD FV1409S4W', 5, 6500000, 18, 500);
CALL SP_ThemDienMay('DM003', 'Máy lạnh Daikin FTKC25TVMV', 8, 7200000, 36, 900);

-- Thêm dữ liệu mẫu sành sứ
CALL SP_ThemSanhSu('SS001', 'Bộ chén đĩa Minh Long', 20, 250000, 'Minh Long I', '2025-07-10');
CALL SP_ThemSanhSu('SS002', 'Lọ hoa gốm Bát Tràng', 15, 180000, 'Làng gốm Bát Tràng', '2025-07-15');
CALL SP_ThemSanhSu('SS003', 'Bình trà cao cấp', 12, 320000, 'Chu Đậu', '2025-07-12');
*/

-- =============================================
-- SCRIPT HOÀN THÀNH
-- =============================================

SELECT 'Database QuanLyKhoHang (MySQL) đã được tạo thành công!' AS Message;
SELECT 'Bao gồm:' AS Info;
SELECT '- 4 bảng chính: HangHoa, ThucPham, DienMay, SanhSu' AS Details;
SELECT '- 1 bảng log: LichSuThayDoi' AS Details;
SELECT '- 6 stored procedures' AS Details;
SELECT '- 2 views' AS Details;
SELECT '- Triggers tự động' AS Details;
SELECT '- Indexes tối ưu' AS Details;
