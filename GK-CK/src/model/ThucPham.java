package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Lớp ThucPham - đại diện cho hàng thực phẩm
 * Model trong MVC Pattern
 */
public class ThucPham extends HangHoa {
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private String nhaCungCap;
    
    public ThucPham(String maHang, String tenHang, int soLuongTon, double donGia,
                   LocalDate ngaySanXuat, LocalDate ngayHetHan, String nhaCungCap) {
        super(maHang, tenHang, soLuongTon, donGia);
        this.ngaySanXuat = ngaySanXuat;
        this.ngayHetHan = ngayHetHan;
        this.nhaCungCap = nhaCungCap;
    }
    
    // Getters và Setters
    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }
    
    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }
    
    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }
    
    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }
    
    public String getNhaCungCap() {
        return nhaCungCap;
    }
    
    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    
    @Override
    public double tinhVAT() {
        return 0.05; // VAT 5% cho thực phẩm
    }
    
    @Override
    public boolean daKho() {
        // Kiểm tra sản phẩm có khó bán không (gần hết hạn)
        LocalDate now = LocalDate.now();
        long daysUntilExpiry = ChronoUnit.DAYS.between(now, ngayHetHan);
        return daysUntilExpiry <= 7; // Trong vòng 1 tuần
    }
    
    /**
     * Kiểm tra xem sản phẩm có sắp hết hạn không
     */
    public boolean sapHetHan() {
        LocalDate now = LocalDate.now();
        long daysUntilExpiry = ChronoUnit.DAYS.between(now, ngayHetHan);
        return daysUntilExpiry <= 7 && daysUntilExpiry >= 0;
    }
    
    /**
     * Kiểm tra xem sản phẩm đã hết hạn chưa
     */
    public boolean daHetHan() {
        return LocalDate.now().isAfter(ngayHetHan);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString().replace("HangHoa{", "ThucPham{") +
                ", ngaySanXuat=" + ngaySanXuat.format(formatter) +
                ", ngayHetHan=" + ngayHetHan.format(formatter) +
                ", nhaCungCap='" + nhaCungCap + "'}";
    }
}
