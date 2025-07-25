/**
 * Lớp DienMay - đại diện cho hàng điện máy
 */
public class DienMay extends HangHoa {
    private int thoiGianBaoHanh; // số tháng
    private double congSuat; // đơn vị: W
    
    public DienMay(String maHang, String tenHang, int soLuongTon, double donGia,
                  int thoiGianBaoHanh, double congSuat) {
        super(maHang, tenHang, soLuongTon, donGia);
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.congSuat = congSuat;
    }
    
    // Getters và Setters
    public int getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }
    
    public void setThoiGianBaoHanh(int thoiGianBaoHanh) {
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }
    
    public double getCongSuat() {
        return congSuat;
    }
    
    public void setCongSuat(double congSuat) {
        this.congSuat = congSuat;
    }
    
    @Override
    public double tinhVAT() {
        return 0.10; // VAT 10% cho điện máy
    }
    
    @Override
    public String getThongTinChiTiet() {
        return String.format("Thoi gian bao hanh: %d thang, Cong suat: %.2f W",
                           thoiGianBaoHanh, congSuat);
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + getThongTinChiTiet();
    }
}
