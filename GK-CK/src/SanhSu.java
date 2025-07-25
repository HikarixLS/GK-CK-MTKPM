import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Lớp SanhSu - đại diện cho hàng sành sứ
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
    public String getThongTinChiTiet() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Nhà sản xuất: %s, Ngày nhập kho: %s",
                           nhaSanXuat, ngayNhapKho.format(formatter));
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + getThongTinChiTiet();
    }
}
