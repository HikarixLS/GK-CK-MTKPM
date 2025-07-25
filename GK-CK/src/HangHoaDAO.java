import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO để thao tác với MySQL database cho hệ thống quản lý kho hàng
 */
public class HangHoaDAO {
    private DatabaseConnection dbConnection;
    
    public HangHoaDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    // Thêm thực phẩm
    public boolean themThucPham(ThucPham thucPham) {
        String sql = "CALL SP_ThemThucPham(?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, thucPham.getMaHang());
            cstmt.setString(2, thucPham.getTenHang());
            cstmt.setInt(3, thucPham.getSoLuongTon());
            cstmt.setDouble(4, thucPham.getDonGia());
            cstmt.setDate(5, Date.valueOf(thucPham.getNgaySanXuat()));
            cstmt.setDate(6, Date.valueOf(thucPham.getNgayHetHan()));
            cstmt.setString(7, thucPham.getNhaCungCap());
            
            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                System.out.println(rs.getString("Message"));
                return "SUCCESS".equals(result);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm thực phẩm: " + e.getMessage());
            // Fallback: thử insert trực tiếp nếu stored procedure lỗi
            return themThucPhamDirect(thucPham);
        }
        return false;
    }
    
    // Thêm thực phẩm trực tiếp (fallback)
    private boolean themThucPhamDirect(ThucPham thucPham) {
        String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'ThucPham', ?, ?, 0.05)";
        String sqlThucPham = "INSERT INTO ThucPham (MaHang, NgaySanXuat, NgayHetHan, NhaCungCap) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlThucPham)) {
                
                // Insert vào bảng HangHoa
                pstmt1.setString(1, thucPham.getMaHang());
                pstmt1.setString(2, thucPham.getTenHang());
                pstmt1.setInt(3, thucPham.getSoLuongTon());
                pstmt1.setDouble(4, thucPham.getDonGia());
                pstmt1.executeUpdate();
                
                // Insert vào bảng ThucPham
                pstmt2.setString(1, thucPham.getMaHang());
                pstmt2.setDate(2, Date.valueOf(thucPham.getNgaySanXuat()));
                pstmt2.setDate(3, Date.valueOf(thucPham.getNgayHetHan()));
                pstmt2.setString(4, thucPham.getNhaCungCap());
                pstmt2.executeUpdate();
                
                conn.commit();
                System.out.println("Thêm thực phẩm thành công!");
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm thực phẩm (direct): " + e.getMessage());
            return false;
        }
    }
    
    // Thêm điện máy
    public boolean themDienMay(DienMay dienMay) {
        String sql = "CALL SP_ThemDienMay(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, dienMay.getMaHang());
            cstmt.setString(2, dienMay.getTenHang());
            cstmt.setInt(3, dienMay.getSoLuongTon());
            cstmt.setDouble(4, dienMay.getDonGia());
            cstmt.setInt(5, dienMay.getThoiGianBaoHanh());
            cstmt.setDouble(6, dienMay.getCongSuat());
            
            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                System.out.println(rs.getString("Message"));
                return "SUCCESS".equals(result);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm điện máy: " + e.getMessage());
            return themDienMayDirect(dienMay);
        }
        return false;
    }
    
    // Thêm điện máy trực tiếp
    private boolean themDienMayDirect(DienMay dienMay) {
        String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'DienMay', ?, ?, 0.10)";
        String sqlDienMay = "INSERT INTO DienMay (MaHang, ThoiGianBaoHanh, CongSuat) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlDienMay)) {
                
                pstmt1.setString(1, dienMay.getMaHang());
                pstmt1.setString(2, dienMay.getTenHang());
                pstmt1.setInt(3, dienMay.getSoLuongTon());
                pstmt1.setDouble(4, dienMay.getDonGia());
                pstmt1.executeUpdate();
                
                pstmt2.setString(1, dienMay.getMaHang());
                pstmt2.setInt(2, dienMay.getThoiGianBaoHanh());
                pstmt2.setDouble(3, dienMay.getCongSuat());
                pstmt2.executeUpdate();
                
                conn.commit();
                System.out.println("Thêm điện máy thành công!");
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm điện máy (direct): " + e.getMessage());
            return false;
        }
    }
    
    // Thêm sành sứ
    public boolean themSanhSu(SanhSu sanhSu) {
        String sql = "CALL SP_ThemSanhSu(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, sanhSu.getMaHang());
            cstmt.setString(2, sanhSu.getTenHang());
            cstmt.setInt(3, sanhSu.getSoLuongTon());
            cstmt.setDouble(4, sanhSu.getDonGia());
            cstmt.setString(5, sanhSu.getNhaSanXuat());
            cstmt.setDate(6, Date.valueOf(sanhSu.getNgayNhapKho()));
            
            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                System.out.println(rs.getString("Message"));
                return "SUCCESS".equals(result);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sành sứ: " + e.getMessage());
            return themSanhSuDirect(sanhSu);
        }
        return false;
    }
    
    // Thêm sành sứ trực tiếp
    private boolean themSanhSuDirect(SanhSu sanhSu) {
        String sqlHangHoa = "INSERT INTO HangHoa (MaHang, TenHang, LoaiHang, SoLuongTon, DonGia, VAT) VALUES (?, ?, 'SanhSu', ?, ?, 0.10)";
        String sqlSanhSu = "INSERT INTO SanhSu (MaHang, NhaSanXuat, NgayNhapKho) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlSanhSu)) {
                
                pstmt1.setString(1, sanhSu.getMaHang());
                pstmt1.setString(2, sanhSu.getTenHang());
                pstmt1.setInt(3, sanhSu.getSoLuongTon());
                pstmt1.setDouble(4, sanhSu.getDonGia());
                pstmt1.executeUpdate();
                
                pstmt2.setString(1, sanhSu.getMaHang());
                pstmt2.setString(2, sanhSu.getNhaSanXuat());
                pstmt2.setDate(3, Date.valueOf(sanhSu.getNgayNhapKho()));
                pstmt2.executeUpdate();
                
                conn.commit();
                System.out.println("Thêm sành sứ thành công!");
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sành sứ (direct): " + e.getMessage());
            return false;
        }
    }
    
    // Lấy danh sách tất cả hàng hóa
    public List<HangHoa> layDanhSachHangHoa() {
        List<HangHoa> danhSach = new ArrayList<>();
        
        // Lấy dữ liệu trực tiếp từ database bằng JOIN
        String sql = "SELECT h.MaHang, h.TenHang, h.LoaiHang, h.SoLuongTon, h.DonGia, " +
                     "tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap, " +
                     "dm.ThoiGianBaoHanh, dm.CongSuat, " +
                     "ss.NhaSanXuat, ss.NgayNhapKho " +
                     "FROM HangHoa h " +
                     "LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang " +
                     "LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang " +
                     "LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang " +
                     "ORDER BY h.LoaiHang, h.TenHang";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String maHang = rs.getString("MaHang");
                String tenHang = rs.getString("TenHang");
                String loaiHang = rs.getString("LoaiHang");
                int soLuongTon = rs.getInt("SoLuongTon");
                double donGia = rs.getDouble("DonGia");
                
                HangHoa hangHoa = null;
                
                switch (loaiHang) {
                    case "ThucPham":
                        Date ngaySX = rs.getDate("NgaySanXuat");
                        Date ngayHH = rs.getDate("NgayHetHan");
                        String nhaCungCap = rs.getString("NhaCungCap");
                        if (ngaySX != null && ngayHH != null && nhaCungCap != null) {
                            hangHoa = new ThucPham(maHang, tenHang, soLuongTon, donGia,
                                ngaySX.toLocalDate(), ngayHH.toLocalDate(), nhaCungCap);
                        }
                        break;
                        
                    case "DienMay":
                        int thoiGianBH = rs.getInt("ThoiGianBaoHanh");
                        double congSuat = rs.getDouble("CongSuat");
                        hangHoa = new DienMay(maHang, tenHang, soLuongTon, donGia, thoiGianBH, congSuat);
                        break;
                        
                    case "SanhSu":
                        String nhaSanXuat = rs.getString("NhaSanXuat");
                        Date ngayNhap = rs.getDate("NgayNhapKho");
                        if (nhaSanXuat != null && ngayNhap != null) {
                            hangHoa = new SanhSu(maHang, tenHang, soLuongTon, donGia,
                                nhaSanXuat, ngayNhap.toLocalDate());
                        }
                        break;
                }
                
                if (hangHoa != null) {
                    danhSach.add(hangHoa);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách hàng hóa: " + e.getMessage());
        }
        
        return danhSach;
    }
    
    
    // Tìm hàng hóa theo mã
    public HangHoa timHangHoa(String maHang) {
        String sql = "SELECT * FROM VW_HangHoaChiTiet WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHang);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String loaiHang = rs.getString("LoaiHang");
                
                if ("ThucPham".equals(loaiHang)) {
                    return taoThucPhamTuResultSet(rs);
                } else if ("DienMay".equals(loaiHang)) {
                    return taoDienMayTuResultSet(rs);
                } else if ("SanhSu".equals(loaiHang)) {
                    return taoSanhSuTuResultSet(rs);
                }
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Lỗi tìm hàng hóa, thử query trực tiếp: " + e.getMessage());
            return timHangHoaDirect(maHang);
        }
        
        return null;
    }
    
    // Tìm hàng hóa trực tiếp
    private HangHoa timHangHoaDirect(String maHang) {
        String sql = "SELECT h.*, tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap, " +
                    "dm.ThoiGianBaoHanh, dm.CongSuat, ss.NhaSanXuat, ss.NgayNhapKho " +
                    "FROM HangHoa h " +
                    "LEFT JOIN ThucPham tp ON h.MaHang = tp.MaHang " +
                    "LEFT JOIN DienMay dm ON h.MaHang = dm.MaHang " +
                    "LEFT JOIN SanhSu ss ON h.MaHang = ss.MaHang " +
                    "WHERE h.MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHang);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String loaiHang = rs.getString("LoaiHang");
                
                if ("ThucPham".equals(loaiHang)) {
                    return taoThucPhamTuResultSet(rs);
                } else if ("DienMay".equals(loaiHang)) {
                    return taoDienMayTuResultSet(rs);
                } else if ("SanhSu".equals(loaiHang)) {
                    return taoSanhSuTuResultSet(rs);
                }
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Lỗi tìm hàng hóa (direct): " + e.getMessage());
        }
        
        return null;
    }
    
    // Xóa hàng hóa
    public boolean xoaHangHoa(String maHang) {
        String sql = "DELETE FROM HangHoa WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maHang);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi xóa hàng hóa: " + e.getMessage());
        }
        return false;
    }
    
    // Tìm sản phẩm sắp hết hạn
    public List<ThucPham> timSanPhamSapHetHan() {
        List<ThucPham> danhSach = new ArrayList<>();
        
        try {
            String sql = "CALL SP_TimSanPhamSapHetHan()";
            Connection conn = dbConnection.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            
            while (rs.next()) {
                String maHang = rs.getString("MaHang");
                HangHoa hangHoa = timHangHoa(maHang);
                if (hangHoa instanceof ThucPham) {
                    danhSach.add((ThucPham) hangHoa);
                }
            }
            rs.close();
            cstmt.close();
            
        } catch (SQLException e) {
            System.err.println("Lỗi tìm sản phẩm sắp hết hạn: " + e.getMessage());
        }
        
        return danhSach;
    }
    
    // Helper methods để tạo objects từ ResultSet
    private ThucPham taoThucPhamTuResultSet(ResultSet rs) throws SQLException {
        String maHang = rs.getString("MaHang");
        String tenHang = rs.getString("TenHang");
        int soLuongTon = rs.getInt("SoLuongTon");
        double donGia = rs.getDouble("DonGia");
        
        Date ngaySanXuatDate = rs.getDate("NgaySanXuat");
        Date ngayHetHanDate = rs.getDate("NgayHetHan");
        String nhaCungCap = rs.getString("NhaCungCap");
        
        if (ngaySanXuatDate == null || ngayHetHanDate == null || nhaCungCap == null) {
            return null;
        }
        
        LocalDate ngaySanXuat = ngaySanXuatDate.toLocalDate();
        LocalDate ngayHetHan = ngayHetHanDate.toLocalDate();
        
        return new ThucPham(maHang, tenHang, soLuongTon, donGia, ngaySanXuat, ngayHetHan, nhaCungCap);
    }
    
    private DienMay taoDienMayTuResultSet(ResultSet rs) throws SQLException {
        String maHang = rs.getString("MaHang");
        String tenHang = rs.getString("TenHang");
        int soLuongTon = rs.getInt("SoLuongTon");
        double donGia = rs.getDouble("DonGia");
        int thoiGianBaoHanh = rs.getInt("ThoiGianBaoHanh");
        double congSuat = rs.getDouble("CongSuat");
        
        return new DienMay(maHang, tenHang, soLuongTon, donGia, thoiGianBaoHanh, congSuat);
    }
    
    private SanhSu taoSanhSuTuResultSet(ResultSet rs) throws SQLException {
        String maHang = rs.getString("MaHang");
        String tenHang = rs.getString("TenHang");
        int soLuongTon = rs.getInt("SoLuongTon");
        double donGia = rs.getDouble("DonGia");
        String nhaSanXuat = rs.getString("NhaSanXuat");
        
        Date ngayNhapKhoDate = rs.getDate("NgayNhapKho");
        if (ngayNhapKhoDate == null || nhaSanXuat == null) {
            return null;
        }
        
        LocalDate ngayNhapKho = ngayNhapKhoDate.toLocalDate();
        
        return new SanhSu(maHang, tenHang, soLuongTon, donGia, nhaSanXuat, ngayNhapKho);
    }
    
    // Test DAO
    public static void main(String[] args) {
        System.out.println("=== TEST MYSQL DAO ===");
        
        HangHoaDAO dao = new HangHoaDAO();
        
        // Test thêm thực phẩm
        ThucPham tp = new ThucPham("TP999", "Test Sữa", 10, 20000,
                                  LocalDate.now(), LocalDate.now().plusDays(7), "Test Company");
        
        if (dao.themThucPham(tp)) {
            System.out.println("✅ Test thêm thực phẩm: THÀNH CÔNG");
        } else {
            System.out.println("❌ Test thêm thực phẩm: THẤT BẠI");
        }
        
        // Test lấy danh sách
        List<HangHoa> danhSach = dao.layDanhSachHangHoa();
        System.out.println("📋 Số lượng hàng hóa trong database: " + danhSach.size());
        
        // Test tìm hàng hóa
        HangHoa hangHoa = dao.timHangHoa("TP999");
        if (hangHoa != null) {
            System.out.println("🔍 Tìm thấy: " + hangHoa.getTenHang());
        }
        
        // Test xóa
        if (dao.xoaHangHoa("TP999")) {
            System.out.println("🗑️ Xóa test data: THÀNH CÔNG");
        }
        
        System.out.println("\n=== TEST HOÀN THÀNH ===");
    }
    
    /**
     * Cập nhật thông tin cơ bản của hàng hóa
     */
    public boolean capNhatThongTinCoBan(String maHang, String tenHang, int soLuong, double donGia) {
        String sql = "UPDATE HangHoa SET TenHang = ?, SoLuongTon = ?, DonGia = ? WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenHang);
            pstmt.setInt(2, soLuong);
            pstmt.setDouble(3, donGia);
            pstmt.setString(4, maHang);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Loi cap nhat hang hoa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tính tổng số lượng cho từng loại hàng hóa
     */
    public void tinhTongSoLuongTheoLoai() {
        String sql = "SELECT LoaiHang, COUNT(*) as SoMatHang, SUM(SoLuongTon) as TongSoLuong, " +
                    "SUM(SoLuongTon * DonGia) as TongGiaTri FROM HangHoa GROUP BY LoaiHang";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\n=== THONG KE TONG SO LUONG THEO LOAI ===");
            System.out.printf("%-15s %-12s %-15s %-20s%n", "Loai Hang", "So Mat Hang", "Tong SL Ton", "Tong Gia Tri (VND)");
            System.out.println("=".repeat(70));
            
            while (rs.next()) {
                String loaiHang = rs.getString("LoaiHang");
                int soMatHang = rs.getInt("SoMatHang");
                int tongSoLuong = rs.getInt("TongSoLuong");
                double tongGiaTri = rs.getDouble("TongGiaTri");
                
                System.out.printf("%-15s %-12d %-15d %,-20.0f%n", 
                    loaiHang, soMatHang, tongSoLuong, tongGiaTri);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi tinh tong so luong theo loai: " + e.getMessage());
        }
    }
    
    /**
     * Tính trung bình số lượng tồn của hàng điện máy
     */
    public void tinhTrungBinhSoLuongDienMay() {
        String sql = "SELECT COUNT(*) as SoMatHang, AVG(SoLuongTon) as TrungBinhSL, " +
                    "MIN(SoLuongTon) as MinSL, MAX(SoLuongTon) as MaxSL " +
                    "FROM HangHoa WHERE LoaiHang = 'DienMay'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                int soMatHang = rs.getInt("SoMatHang");
                double trungBinhSL = rs.getDouble("TrungBinhSL");
                int minSL = rs.getInt("MinSL");
                int maxSL = rs.getInt("MaxSL");
                
                System.out.println("\n=== THONG KE HANG DIEN MAY ===");
                System.out.println("So mat hang dien may: " + soMatHang);
                if (soMatHang > 0) {
                    System.out.printf("Trung binh so luong ton: %.2f%n", trungBinhSL);
                    System.out.println("So luong ton thap nhat: " + minSL);
                    System.out.println("So luong ton cao nhat: " + maxSL);
                } else {
                    System.out.println("Khong co hang dien may nao trong kho!");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Loi tinh trung binh so luong dien may: " + e.getMessage());
        }
    }
    
    /**
     * Tìm sản phẩm thực phẩm còn 1 tuần là hết hạn (7 ngày)
     */
    public List<ThucPham> timSanPhamSapHetHanTrongTuan() {
        List<ThucPham> danhSach = new ArrayList<>();
        String sql = "SELECT h.*, tp.NgaySanXuat, tp.NgayHetHan, tp.NhaCungCap " +
                    "FROM HangHoa h JOIN ThucPham tp ON h.MaHang = tp.MaHang " +
                    "WHERE tp.NgayHetHan BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                    "ORDER BY tp.NgayHetHan";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String maHang = rs.getString("MaHang");
                String tenHang = rs.getString("TenHang");
                int soLuongTon = rs.getInt("SoLuongTon");
                double donGia = rs.getDouble("DonGia");
                LocalDate ngaySX = rs.getDate("NgaySanXuat").toLocalDate();
                LocalDate ngayHH = rs.getDate("NgayHetHan").toLocalDate();
                String nhaCungCap = rs.getString("NhaCungCap");
                
                ThucPham tp = new ThucPham(maHang, tenHang, soLuongTon, donGia, ngaySX, ngayHH, nhaCungCap);
                danhSach.add(tp);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi tim san pham sap het han trong tuan: " + e.getMessage());
        }
        
        return danhSach;
    }
    
    /**
     * Cập nhật thông tin chi tiết theo loại hàng
     */
    public boolean capNhatThucPham(String maHang, String tenHang, int soLuong, double donGia,
                                  LocalDate ngaySX, LocalDate ngayHH, String nhaCungCap) {
        String sqlHangHoa = "UPDATE HangHoa SET TenHang = ?, SoLuongTon = ?, DonGia = ? WHERE MaHang = ?";
        String sqlThucPham = "UPDATE ThucPham SET NgaySanXuat = ?, NgayHetHan = ?, NhaCungCap = ? WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlThucPham)) {
                
                // Cập nhật bảng HangHoa
                pstmt1.setString(1, tenHang);
                pstmt1.setInt(2, soLuong);
                pstmt1.setDouble(3, donGia);
                pstmt1.setString(4, maHang);
                pstmt1.executeUpdate();
                
                // Cập nhật bảng ThucPham
                pstmt2.setDate(1, Date.valueOf(ngaySX));
                pstmt2.setDate(2, Date.valueOf(ngayHH));
                pstmt2.setString(3, nhaCungCap);
                pstmt2.setString(4, maHang);
                pstmt2.executeUpdate();
                
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi cap nhat thuc pham: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin điện máy
     */
    public boolean capNhatDienMay(String maHang, String tenHang, int soLuong, double donGia,
                                 int thoiGianBaoHanh, double congSuat) {
        String sqlHangHoa = "UPDATE HangHoa SET TenHang = ?, SoLuongTon = ?, DonGia = ? WHERE MaHang = ?";
        String sqlDienMay = "UPDATE DienMay SET ThoiGianBaoHanh = ?, CongSuat = ? WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlDienMay)) {
                
                pstmt1.setString(1, tenHang);
                pstmt1.setInt(2, soLuong);
                pstmt1.setDouble(3, donGia);
                pstmt1.setString(4, maHang);
                pstmt1.executeUpdate();
                
                pstmt2.setInt(1, thoiGianBaoHanh);
                pstmt2.setDouble(2, congSuat);
                pstmt2.setString(3, maHang);
                pstmt2.executeUpdate();
                
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi cap nhat dien may: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin sành sứ
     */
    public boolean capNhatSanhSu(String maHang, String tenHang, int soLuong, double donGia,
                                String nhaSanXuat, LocalDate ngayNhapKho) {
        String sqlHangHoa = "UPDATE HangHoa SET TenHang = ?, SoLuongTon = ?, DonGia = ? WHERE MaHang = ?";
        String sqlSanhSu = "UPDATE SanhSu SET NhaSanXuat = ?, NgayNhapKho = ? WHERE MaHang = ?";
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlHangHoa);
                 PreparedStatement pstmt2 = conn.prepareStatement(sqlSanhSu)) {
                
                pstmt1.setString(1, tenHang);
                pstmt1.setInt(2, soLuong);
                pstmt1.setDouble(3, donGia);
                pstmt1.setString(4, maHang);
                pstmt1.executeUpdate();
                
                pstmt2.setString(1, nhaSanXuat);
                pstmt2.setDate(2, Date.valueOf(ngayNhapKho));
                pstmt2.setString(3, maHang);
                pstmt2.executeUpdate();
                
                conn.commit();
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Loi cap nhat sanh su: " + e.getMessage());
            return false;
        }
    }
}
