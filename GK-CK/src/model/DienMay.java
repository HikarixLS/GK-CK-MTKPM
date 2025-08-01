package model;

/**
 * Lớp DienMay - đại diện cho hàng điện máy
 * Model trong MVC Pattern
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
    public boolean daKho() {
        // Điện máy khó bán nếu số lượng tồn > 50 hoặc công suất < 100W
        return soLuongTon > 50 || congSuat < 100;
    }
    
    /**
     * Lấy thông tin chi tiết về điện máy
     */
    public String getThongTinChiTiet() {
        return String.format("Thoi gian bao hanh: %d thang, Cong suat: %.2f W",
                           thoiGianBaoHanh, congSuat);
    }
    
    /**
     * Kiểm tra xem điện máy có tiết kiệm điện không
     */
    public boolean laTietKiemDien() {
        return congSuat <= 1000; // Dưới 1000W được coi là tiết kiệm điện
    }
    
    @Override
    public String toString() {
        return super.toString().replace("HangHoa{", "DienMay{") +
                ", thoiGianBaoHanh=" + thoiGianBaoHanh + " thang" +
                ", congSuat=" + congSuat + " W}";
    }
}
