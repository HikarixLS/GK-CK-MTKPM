package controller;

import model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller chính để quản lý hàng hóa
 * Business Logic Layer trong MVC Pattern
 */
public class HangHoaController {
    private HangHoaDAO hangHoaDAO;
    
    public HangHoaController() {
        this.hangHoaDAO = new HangHoaDAO();
    }
    
    // ================= THÊM HÀNG HÓA =================
    
    /**
     * Thêm thực phẩm mới
     */
    public boolean themThucPham(String maHang, String tenHang, int soLuongTon, 
                               double donGia, String ngaySanXuat, String ngayHetHan, 
                               String nhaCungCap) {
        try {
            // Validate dữ liệu đầu vào
            if (!validateMaHang(maHang)) {
                throw new IllegalArgumentException("Mã hàng không hợp lệ!");
            }
            
            if (!validateTenHang(tenHang)) {
                throw new IllegalArgumentException("Tên hàng không hợp lệ!");
            }
            
            if (soLuongTon < 0) {
                throw new IllegalArgumentException("Số lượng tồn phải >= 0!");
            }
            
            if (donGia <= 0) {
                throw new IllegalArgumentException("Đơn giá phải > 0!");
            }
            
            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ngaySX = LocalDate.parse(ngaySanXuat, formatter);
            LocalDate ngayHH = LocalDate.parse(ngayHetHan, formatter);
            
            // Validate dates
            if (ngaySX.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày sản xuất không được trong tương lai!");
            }
            
            if (ngayHH.isBefore(ngaySX)) {
                throw new IllegalArgumentException("Ngày hết hạn phải sau ngày sản xuất!");
            }
            
            if (ngayHH.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Sản phẩm đã hết hạn!");
            }
            
            // Kiểm tra mã hàng đã tồn tại
            if (hangHoaDAO.timHangHoa(maHang) != null) {
                throw new IllegalArgumentException("Mã hàng đã tồn tại!");
            }
            
            // Tạo đối tượng ThucPham
            ThucPham thucPham = new ThucPham(maHang, tenHang, soLuongTon, donGia, 
                                           ngaySX, ngayHH, nhaCungCap);
            
            // Thêm vào database
            return hangHoaDAO.themThucPham(thucPham);
            
        } catch (DateTimeParseException e) {
            System.err.println("❌ Định dạng ngày không hợp lệ! Sử dụng dd/MM/yyyy");
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thêm điện máy mới
     */
    public boolean themDienMay(String maHang, String tenHang, int soLuongTon, 
                              double donGia, int thoiGianBaoHanh, double congSuat) {
        try {
            // Validate dữ liệu đầu vào
            if (!validateMaHang(maHang)) {
                throw new IllegalArgumentException("Mã hàng không hợp lệ!");
            }
            
            if (!validateTenHang(tenHang)) {
                throw new IllegalArgumentException("Tên hàng không hợp lệ!");
            }
            
            if (soLuongTon < 0) {
                throw new IllegalArgumentException("Số lượng tồn phải >= 0!");
            }
            
            if (donGia <= 0) {
                throw new IllegalArgumentException("Đơn giá phải > 0!");
            }
            
            if (thoiGianBaoHanh <= 0) {
                throw new IllegalArgumentException("Thời gian bảo hành phải > 0!");
            }
            
            if (congSuat <= 0) {
                throw new IllegalArgumentException("Công suất phải > 0!");
            }
            
            // Kiểm tra mã hàng đã tồn tại
            if (hangHoaDAO.timHangHoa(maHang) != null) {
                throw new IllegalArgumentException("Mã hàng đã tồn tại!");
            }
            
            // Tạo đối tượng DienMay
            DienMay dienMay = new DienMay(maHang, tenHang, soLuongTon, donGia, 
                                        thoiGianBaoHanh, congSuat);
            
            // Thêm vào database
            return hangHoaDAO.themDienMay(dienMay);
            
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thêm sành sứ mới
     */
    public boolean themSanhSu(String maHang, String tenHang, int soLuongTon, 
                             double donGia, String nhaSanXuat, String ngayNhapKho) {
        try {
            // Validate dữ liệu đầu vào
            if (!validateMaHang(maHang)) {
                throw new IllegalArgumentException("Mã hàng không hợp lệ!");
            }
            
            if (!validateTenHang(tenHang)) {
                throw new IllegalArgumentException("Tên hàng không hợp lệ!");
            }
            
            if (soLuongTon < 0) {
                throw new IllegalArgumentException("Số lượng tồn phải >= 0!");
            }
            
            if (donGia <= 0) {
                throw new IllegalArgumentException("Đơn giá phải > 0!");
            }
            
            if (nhaSanXuat == null || nhaSanXuat.trim().isEmpty()) {
                throw new IllegalArgumentException("Nhà sản xuất không được để trống!");
            }
            
            // Parse date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ngayNK = LocalDate.parse(ngayNhapKho, formatter);
            
            // Validate date
            if (ngayNK.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày nhập kho không được trong tương lai!");
            }
            
            // Kiểm tra mã hàng đã tồn tại
            if (hangHoaDAO.timHangHoa(maHang) != null) {
                throw new IllegalArgumentException("Mã hàng đã tồn tại!");
            }
            
            // Tạo đối tượng SanhSu
            SanhSu sanhSu = new SanhSu(maHang, tenHang, soLuongTon, donGia, 
                                     nhaSanXuat, ngayNK);
            
            // Thêm vào database
            return hangHoaDAO.themSanhSu(sanhSu);
            
        } catch (DateTimeParseException e) {
            System.err.println("❌ Định dạng ngày không hợp lệ! Sử dụng dd/MM/yyyy");
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định: " + e.getMessage());
            return false;
        }
    }
    
    // ================= TÌM KIẾM & HIỂN THỊ =================
    
    /**
     * Tìm hàng hóa theo mã
     */
    public HangHoa timHangHoa(String maHang) {
        if (!validateMaHang(maHang)) {
            System.err.println("❌ Mã hàng không hợp lệ!");
            return null;
        }
        
        return hangHoaDAO.timHangHoa(maHang);
    }
    
    /**
     * Lấy danh sách tất cả hàng hóa
     */
    public List<HangHoa> layDanhSachHangHoa() {
        return hangHoaDAO.layDanhSachHangHoa();
    }
    
    /**
     * Tìm sản phẩm sắp hết hạn trong tuần
     */
    public List<ThucPham> timSanPhamSapHetHan() {
        return hangHoaDAO.timSanPhamSapHetHanTrongTuan();
    }
    
    // ================= CẬP NHẬT & XÓA =================
    
    /**
     * Cập nhật thông tin hàng hóa
     */
    public boolean capNhatHangHoa(String maHang, String tenHang, int soLuongTon, double donGia) {
        try {
            // Validate dữ liệu
            if (!validateMaHang(maHang)) {
                throw new IllegalArgumentException("Mã hàng không hợp lệ!");
            }
            
            if (!validateTenHang(tenHang)) {
                throw new IllegalArgumentException("Tên hàng không hợp lệ!");
            }
            
            if (soLuongTon < 0) {
                throw new IllegalArgumentException("Số lượng tồn phải >= 0!");
            }
            
            if (donGia <= 0) {
                throw new IllegalArgumentException("Đơn giá phải > 0!");
            }
            
            // Tìm hàng hóa hiện tại
            HangHoa hangHoa = hangHoaDAO.timHangHoa(maHang);
            if (hangHoa == null) {
                throw new IllegalArgumentException("Không tìm thấy hàng hóa với mã: " + maHang);
            }
            
            // Cập nhật thông tin
            hangHoa.setTenHang(tenHang);
            hangHoa.setSoLuongTon(soLuongTon);
            hangHoa.setDonGia(donGia);
            
            return hangHoaDAO.capNhatHangHoa(hangHoa);
            
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa hàng hóa
     */
    public boolean xoaHangHoa(String maHang) {
        if (!validateMaHang(maHang)) {
            System.err.println("❌ Mã hàng không hợp lệ!");
            return false;
        }
        
        // Kiểm tra hàng hóa có tồn tại không
        HangHoa hangHoa = hangHoaDAO.timHangHoa(maHang);
        if (hangHoa == null) {
            System.err.println("❌ Không tìm thấy hàng hóa với mã: " + maHang);
            return false;
        }
        
        return hangHoaDAO.xoaHangHoa(maHang);
    }
    
    // ================= THỐNG KÊ =================
    
    /**
     * Hiển thị thống kê số lượng theo loại
     */
    public void hienThiThongKe() {
        hangHoaDAO.thongKeSoLuongTheoLoai();
    }
    
    /**
     * Tính trung bình số lượng tồn kho điện máy
     */
    public double tinhTrungBinhSoLuongDienMay() {
        return hangHoaDAO.tinhTrungBinhSoLuongDienMay();
    }
    
    // ================= VALIDATION METHODS =================
    
    private boolean validateMaHang(String maHang) {
        return maHang != null && !maHang.trim().isEmpty() && maHang.length() <= 10;
    }
    
    private boolean validateTenHang(String tenHang) {
        return tenHang != null && !tenHang.trim().isEmpty() && tenHang.length() <= 100;
    }
    
    // ================= BUSINESS LOGIC =================
    
    /**
     * Kiểm tra hàng hóa có khó bán không
     */
    public boolean laHangKhoBan(String maHang) {
        HangHoa hangHoa = timHangHoa(maHang);
        return hangHoa != null && hangHoa.daKho();
    }
    
    /**
     * Tính tổng giá trị kho hàng
     */
    public double tinhTongGiaTriKho() {
        List<HangHoa> danhSach = layDanhSachHangHoa();
        return danhSach.stream()
                      .mapToDouble(hh -> hh.getSoLuongTon() * hh.getDonGia())
                      .sum();
    }
    
    /**
     * Tính tổng giá trị kho hàng có VAT
     */
    public double tinhTongGiaTriCoVAT() {
        List<HangHoa> danhSach = layDanhSachHangHoa();
        return danhSach.stream()
                      .mapToDouble(hh -> hh.getTongGiaTriCoVAT())
                      .sum();
    }
    
    /**
     * Lấy số lượng hàng hóa theo loại
     */
    public int getSoLuongTheoLoai(String loaiHang) {
        List<HangHoa> danhSach = layDanhSachHangHoa();
        
        switch (loaiHang.toLowerCase()) {
            case "thucpham":
                return (int) danhSach.stream().filter(hh -> hh instanceof ThucPham).count();
            case "dienmay":
                return (int) danhSach.stream().filter(hh -> hh instanceof DienMay).count();
            case "sanhsu":
                return (int) danhSach.stream().filter(hh -> hh instanceof SanhSu).count();
            default:
                return 0;
        }
    }
}
