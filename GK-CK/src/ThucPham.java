import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Lớp ThucPham - đại diện cho hàng thực phẩm
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
    
    // Kiểm tra xem sản phẩm có hết hạn trong 1 tuần không
    public boolean hetHanTrong1Tuan() {
        LocalDate now = LocalDate.now();
        long soNgayConLai = ChronoUnit.DAYS.between(now, ngayHetHan);
        return soNgayConLai <= 7 && soNgayConLai >= 0;
    }
    
    @Override
    public String getThongTinChiTiet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Ngày sản xuất: %s, Ngày hết hạn: %s, Nhà cung cấp: %s",
                           ngaySanXuat.format(formatter), ngayHetHan.format(formatter), nhaCungCap);
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + getThongTinChiTiet();
    }
}
