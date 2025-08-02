package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) để thao tác với MySQL database
 * Data Access Layer trong MVC Pattern
 */
public class HangHoaDAO {
    private DatabaseConnection dbConnection;
    
    public HangHoaDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        createTablesIfNotExists();
    }
    
    /**
     * Tạo bảng nếu chưa tồn tại
     */
    private void createTablesIfNotExists() {
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Tạo bảng HangHoa
            String createHangHoa = """
                CREATE TABLE IF NOT EXISTS HangHoa (
                    MaHang VARCHAR(10) PRIMARY KEY,
                    TenHang VARCHAR(100) NOT NULL,
                    LoaiHang ENUM('ThucPham', 'DienMay', 'SanhSu') NOT NULL,
                    SoLuongTon INT NOT NULL DEFAULT 0,
                    DonGia DOUBLE NOT NULL DEFAULT 0,
                    VAT DOUBLE NOT NULL DEFAULT 0.10
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            
            // Tạo bảng ThucPham
            String createThucPham = """
                CREATE TABLE IF NOT EXISTS ThucPham (
                    MaHang VARCHAR(10) PRIMARY KEY,
                    NgaySanXuat DATE NOT NULL,
                    NgayHetHan DATE NOT NULL,
                    NhaCungCap VARCHAR(100) NOT NULL,
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            
            // Tạo bảng DienMay
            String createDienMay = """
                CREATE TABLE IF NOT EXISTS DienMay (
                    MaHang VARCHAR(10) PRIMARY KEY,
                    ThoiGianBaoHanh INT NOT NULL,
                    CongSuat DOUBLE NOT NULL,
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            
            // Tạo bảng SanhSu
            String createSanhSu = """
                CREATE TABLE IF NOT EXISTS SanhSu (
                    MaHang VARCHAR(10) PRIMARY KEY,
                    NhaSanXuat VARCHAR(100) NOT NULL,
                    NgayNhapKho DATE NOT NULL,
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;
            
            stmt.executeUpdate(createHangHoa);
            stmt.executeUpdate(createThucPham);
            stmt.executeUpdate(createDienMay);
            stmt.executeUpdate(createSanhSu);
            
            System.out.println("✅ Đã tạo/kiểm tra các bảng database");
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tạo bảng: " + e.getMessage());
        }
    }
    
    // ================= THÊM HÀNG HÓA =================
    
    public boolean themThucPham(ThucPham thucPham) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Thêm vào bảng HangHoa
            String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'ThucPham', ?, ?, ?)";
            try (PreparedStatement pstmtHangHoa = conn.prepareStatement(sqlHangHoa)) {
                pstmtHangHoa.setString(1, thucPham.getMaHang());
                pstmtHangHoa.setString(2, thucPham.getTenHang());
                pstmtHangHoa.setInt(3, thucPham.getSoLuongTon());
                pstmtHangHoa.setDouble(4, thucPham.getDonGia());
                pstmtHangHoa.setDouble(5, thucPham.tinhVAT());
                pstmtHangHoa.executeUpdate();
            }
            
            // Thêm vào bảng ThucPham
            String sqlThucPham = "INSERT INTO ThucPham (MaHang, NgaySanXuat, NgayHetHan, NhaCungCap) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtThucPham = conn.prepareStatement(sqlThucPham)) {
                pstmtThucPham.setString(1, thucPham.getMaHang());
                pstmtThucPham.setDate(2, Date.valueOf(thucPham.getNgaySanXuat()));
                pstmtThucPham.setDate(3, Date.valueOf(thucPham.getNgayHetHan()));
                pstmtThucPham.setString(4, thucPham.getNhaCungCap());
                pstmtThucPham.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            System.out.println("✅ Đã thêm thực phẩm: " + thucPham.getTenHang());
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException rollbackEx) {
                System.err.println("❌ Lỗi rollback: " + rollbackEx.getMessage());
            }
            System.err.println("❌ Lỗi thêm thực phẩm: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi reset auto-commit: " + e.getMessage());
            }
        }
    }
    
    public boolean themDienMay(DienMay dienMay) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Thêm vào bảng HangHoa
            String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'DienMay', ?, ?, ?)";
            try (PreparedStatement pstmtHangHoa = conn.prepareStatement(sqlHangHoa)) {
                pstmtHangHoa.setString(1, dienMay.getMaHang());
                pstmtHangHoa.setString(2, dienMay.getTenHang());
                pstmtHangHoa.setInt(3, dienMay.getSoLuongTon());
                pstmtHangHoa.setDouble(4, dienMay.getDonGia());
                pstmtHangHoa.setDouble(5, dienMay.tinhVAT());
                pstmtHangHoa.executeUpdate();
            }
            
            // Thêm vào bảng DienMay
            String sqlDienMay = "INSERT INTO DienMay (MaHang, ThoiGianBaoHanh, CongSuat) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtDienMay = conn.prepareStatement(sqlDienMay)) {
                pstmtDienMay.setString(1, dienMay.getMaHang());
                pstmtDienMay.setInt(2, dienMay.getThoiGianBaoHanh());
                pstmtDienMay.setDouble(3, dienMay.getCongSuat());
                pstmtDienMay.executeUpdate();
            }
            
            conn.commit();
            System.out.println("✅ Đã thêm điện máy: " + dienMay.getTenHang());
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("❌ Lỗi rollback: " + rollbackEx.getMessage());
            }
            System.err.println("❌ Lỗi thêm điện máy: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi reset auto-commit: " + e.getMessage());
            }
        }
    }
    
    public boolean themSanhSu(SanhSu sanhSu) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Thêm vào bảng HangHoa
            String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'SanhSu', ?, ?, ?)";
            try (PreparedStatement pstmtHangHoa = conn.prepareStatement(sqlHangHoa)) {
                pstmtHangHoa.setString(1, sanhSu.getMaHang());
                pstmtHangHoa.setString(2, sanhSu.getTenHang());
                pstmtHangHoa.setInt(3, sanhSu.getSoLuongTon());
                pstmtHangHoa.setDouble(4, sanhSu.getDonGia());
                pstmtHangHoa.setDouble(5, sanhSu.tinhVAT());
                pstmtHangHoa.executeUpdate();
            }
            
            // Thêm vào bảng SanhSu
            String sqlSanhSu = "INSERT INTO SanhSu (MaHang, NhaSanXuat, NgayNhapKho) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtSanhSu = conn.prepareStatement(sqlSanhSu)) {
                pstmtSanhSu.setString(1, sanhSu.getMaHang());
                pstmtSanhSu.setString(2, sanhSu.getNhaSanXuat());
                pstmtSanhSu.setDate(3, Date.valueOf(sanhSu.getNgayNhapKho()));
                pstmtSanhSu.executeUpdate();
            }
            
            conn.commit();
            System.out.println("✅ Đã thêm sành sứ: " + sanhSu.getTenHang());
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("❌ Lỗi rollback: " + rollbackEx.getMessage());
            }
            System.err.println("❌ Lỗi thêm sành sứ: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi reset auto-commit: " + e.getMessage());
            }
        }
    }
    
    // ================= TÌM KIẾM HÀNG HÓA =================
    
    public HangHoa timHangHoa(String maHang) {
        String sql = """
            SELECT h.*, tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap,
                   dm.ThoiGianBaoHanh, dm.CongSuat,
                   ss.NhaSanXuat, ss.NgayNhapKho
            FROM HangHoa h
            LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang
            LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang
            LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang
            WHERE h.MaHang = ?
            """;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHang);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String loaiHang = rs.getString("LoaiHang");
                
                switch (loaiHang) {
                    case "ThucPham":
                        return new ThucPham(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getDate("NgaySanXuat").toLocalDate(),
                            rs.getDate("NgayHetHan").toLocalDate(),
                            rs.getString("NhaCungCap")
                        );
                        
                    case "DienMay":
                        return new DienMay(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getInt("ThoiGianBaoHanh"),
                            rs.getDouble("CongSuat")
                        );
                        
                    case "SanhSu":
                        return new SanhSu(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getString("NhaSanXuat"),
                            rs.getDate("NgayNhapKho").toLocalDate()
                        );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm kiếm hàng hóa: " + e.getMessage());
        }
        
        return null;
    }
    
    // ================= DANH SÁCH HÀNG HÓA =================
    
    public List<HangHoa> layDanhSachHangHoa() {
        List<HangHoa> danhSach = new ArrayList<>();
        String sql = """
            SELECT h.*, tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap,
                   dm.ThoiGianBaoHanh, dm.CongSuat,
                   ss.NhaSanXuat, ss.NgayNhapKho
            FROM HangHoa h
            LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang
            LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang
            LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang
            ORDER BY h.MaHang
            """;
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String loaiHang = rs.getString("LoaiHang");
                HangHoa hangHoa = null;
                
                switch (loaiHang) {
                    case "ThucPham":
                        hangHoa = new ThucPham(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getDate("NgaySanXuat").toLocalDate(),
                            rs.getDate("NgayHetHan").toLocalDate(),
                            rs.getString("NhaCungCap")
                        );
                        break;
                        
                    case "DienMay":
                        hangHoa = new DienMay(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getInt("ThoiGianBaoHanh"),
                            rs.getDouble("CongSuat")
                        );
                        break;
                        
                    case "SanhSu":
                        hangHoa = new SanhSu(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getInt("SoLuongTon"),
                            rs.getDouble("DonGia"),
                            rs.getString("NhaSanXuat"),
                            rs.getDate("NgayNhapKho").toLocalDate()
                        );
                        break;
                }
                
                if (hangHoa != null) {
                    danhSach.add(hangHoa);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách hàng hóa: " + e.getMessage());
        }
        
        return danhSach;
    }
    
    // ================= TÌM SẢN PHẨM SẮP HẾT HẠN =================
    
    public List<ThucPham> timSanPhamSapHetHanTrongTuan() {
        List<ThucPham> danhSach = new ArrayList<>();
        String sql = """
            SELECT h.*, tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap
            FROM HangHoa h
            JOIN ThucPham tp ON h.MaHang = tp.MaHang
            WHERE tp.NgayHetHan BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
            ORDER BY tp.NgayHetHan
            """;
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ThucPham thucPham = new ThucPham(
                    rs.getString("MaHang"),
                    rs.getString("TenHang"),
                    rs.getInt("SoLuongTon"),
                    rs.getDouble("DonGia"),
                    rs.getDate("NgaySanXuat").toLocalDate(),
                    rs.getDate("NgayHetHan").toLocalDate(),
                    rs.getString("NhaCungCap")
                );
                danhSach.add(thucPham);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm sản phẩm sắp hết hạn: " + e.getMessage());
        }
        
        return danhSach;
    }
    
    // ================= CẬP NHẬT & XÓA =================
    
    public boolean capNhatHangHoa(HangHoa hangHoa) {
        String sql = "UPDATE HangHoa SET TenHang = ?, SoLuongTon = ?, DonGia = ? WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hangHoa.getTenHang());
            pstmt.setInt(2, hangHoa.getSoLuongTon());
            pstmt.setDouble(3, hangHoa.getDonGia());
            pstmt.setString(4, hangHoa.getMaHang());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Đã cập nhật hàng hóa: " + hangHoa.getTenHang());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật hàng hóa: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean xoaHangHoa(String maHang) {
        String sql = "DELETE FROM HangHoa WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHang);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Đã xóa hàng hóa có mã: " + maHang);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi xóa hàng hóa: " + e.getMessage());
        }
        
        return false;
    }
    
    // ================= THỐNG KÊ =================
    
    public void thongKeSoLuongTheoLoai() {
        String sql = """
            SELECT LoaiHang, COUNT(*) as SoLuong, SUM(SoLuongTon) as TongSoLuongTon
            FROM HangHoa 
            GROUP BY LoaiHang
            ORDER BY LoaiHang
            """;
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n📊 THỐNG KÊ SỐ LƯỢNG THEO LOẠI HÀNG:");
            System.out.println("=====================================");
            while (rs.next()) {
                System.out.printf("%-15s: %3d sản phẩm, tổng tồn kho: %d\n",
                    rs.getString("LoaiHang"),
                    rs.getInt("SoLuong"),
                    rs.getInt("TongSoLuongTon"));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi thống kê: " + e.getMessage());
        }
    }
    
    public double tinhTrungBinhSoLuongDienMay() {
        String sql = "SELECT AVG(SoLuongTon) as TrungBinh FROM HangHoa WHERE LoaiHang = 'DienMay'";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("TrungBinh");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tính trung bình: " + e.getMessage());
        }
        
        return 0.0;
    }
}
