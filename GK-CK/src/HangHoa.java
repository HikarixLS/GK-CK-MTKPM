/**
 * Lớp cha HangHoa - đại diện cho hàng hóa chung
 */
public abstract class HangHoa {
    protected String maHang;
    protected String tenHang;
    protected int soLuongTon;
    protected double donGia;
    
    // Constructor
    public HangHoa(String maHang, String tenHang, int soLuongTon, double donGia) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
    }
    
    // Getters và Setters
    public String getMaHang() {
        return maHang;
    }
    
    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }
    
    public String getTenHang() {
        return tenHang;
    }
    
    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }
    
    public int getSoLuongTon() {
        return soLuongTon;
    }
    
    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
    
    // Phương thức trừu tượng để tính VAT
    public abstract double tinhVAT();
    
    // Phương thức toString
    @Override
    public String toString() {
        return String.format("Ma hang: %s, Ten hang: %s, So luong ton: %d, Don gia: %.2f, VAT: %.2f%%", 
                           maHang, tenHang, soLuongTon, donGia, tinhVAT() * 100);
    }
    
    // Phương thức để lấy thông tin chi tiết (sẽ được ghi đè ở các lớp con)
    public abstract String getThongTinChiTiet();
}
