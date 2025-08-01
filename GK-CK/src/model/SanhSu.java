package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Lớp SanhSu - đại diện cho hàng sành sứ
 * Model trong MVC Pattern
 */
public class SanhSu extends HangHoa {
    private String nhaSanXuat;
    private LocalDate ngayNhapKho;
    
    public SanhSu(String maHang, String tenHang, int soLuongTon, double donGia,
                 String nhaSanXuat, LocalDate ngayNhapKho) {
        super(maHang, tenHang, soLuongTon, donGia);
        this.nhaSanXuat = nhaSanXuat;
        this.ngayNhapKho = ngayNhapKho;
    }
    
    // Getters và Setters
    public String getNhaSanXuat() {
        return nhaSanXuat;
    }
    
    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }
    
    public LocalDate getNgayNhapKho() {
        return ngayNhapKho;
    }
    
    public void setNgayNhapKho(LocalDate ngayNhapKho) {
        this.ngayNhapKho = ngayNhapKho;
    }
    
    @Override
    public double tinhVAT() {
        return 0.10; // VAT 10% cho sành sứ
    }
    
    @Override
    public boolean daKho() {
        // Sành sứ khó bán nếu tồn kho quá lâu (> 6 tháng)
        LocalDate now = LocalDate.now();
        long monthsInStock = ChronoUnit.MONTHS.between(ngayNhapKho, now);
        return monthsInStock > 6;
    }
    
    /**
     * Lấy thông tin chi tiết về sành sứ
     */
    public String getThongTinChiTiet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Nhà sản xuất: %s, Ngày nhập kho: %s",
                           nhaSanXuat, ngayNhapKho.format(formatter));
    }
    
    /**
     * Tính số ngày đã lưu kho
     */
    public long getSoNgayTonKho() {
        return ChronoUnit.DAYS.between(ngayNhapKho, LocalDate.now());
    }
    
    /**
     * Kiểm tra xem có phải hàng tồn kho lâu không
     */
    public boolean laHangTonKhoLau() {
        return getSoNgayTonKho() > 180; // Hơn 6 tháng
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString().replace("HangHoa{", "SanhSu{") +
                ", nhaSanXuat='" + nhaSanXuat + "'" +
                ", ngayNhapKho=" + ngayNhapKho.format(formatter) + "}";
    }
}
