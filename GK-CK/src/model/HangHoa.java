package model;

/**
 * Lớp cha HangHoa - đại diện cho hàng hóa chung
 * Model trong MVC Pattern
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
    
    // Method abstract để các lớp con implement
    // Phương thức trừu tượng bắt buộc implement
    public abstract double tinhVAT();
    
    public abstract boolean daKho();
    
    /**
     * Tính giá có VAT
     * @return giá đã bao gồm VAT
     */
    public double getGiaCoVAT() {
        return donGia * (1 + tinhVAT());
    }
    
    /**
     * Lấy tỷ lệ VAT của sản phẩm (%)
     * @return tỷ lệ VAT theo phần trăm
     */
    public double getVATRate() {
        return tinhVAT() * 100.0;
    }
    
    /**
     * Tính tổng giá trị có VAT cho số lượng tồn
     * @return tổng giá trị kho có VAT
     */
    public double getTongGiaTriCoVAT() {
        return getGiaCoVAT() * soLuongTon;
    }
    
    /**
     * Tính đánh giá VAT (deprecated - dùng getGiaCoVAT thay thế)
     */
    @Deprecated
    public double danhGiaVAT() {
        return getGiaCoVAT();
    }
    
    @Override
    public String toString() {
        return "HangHoa{" +
                "maHang='" + maHang + '\'' +
                ", tenHang='" + tenHang + '\'' +
                ", soLuongTon=" + soLuongTon +
                ", donGia=" + donGia +
                '}';
    }
}
