import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

/**
 * Lop Main - chuong trinh chinh su dung MySQL Database
 */
public class Main {
    private static HangHoaDAO hangHoaDAO;
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static void main(String[] args) {
        System.out.println("Ket noi voi MySQL Database...");
        
        // Khoi tao DAO va kiem tra ket noi
        try {
            hangHoaDAO = new HangHoaDAO();
            System.out.println("Ket noi database thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi ket noi database: " + e.getMessage());
            System.out.println("Vui long kiem tra XAMPP va database configuration.");
            return;
        }
        
        System.out.println("\n=== CHUONG TRINH QUAN LY KHO HANG HOA ===");
        System.out.println("✅ Ket noi MySQL thanh cong!");
        System.out.println("✅ Database QuanLyKhoHang da san sang!");
        
        // Hien thi menu chinh
        showMainMenu();
        
        scanner.close();
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENU CHINH - QUAN LY KHO HANG HOA ===");
            System.out.println("1. In danh sach tat ca hang hoa");
            System.out.println("2. Them hang hoa moi");
            System.out.println("3. Sua hang hoa");
            System.out.println("4. Xoa hang hoa");
            System.out.println("5. Tim kiem san pham con 1 tuan la het han");
            System.out.println("6. Tinh tong so luong cho tung loai hang hoa");
            System.out.println("7. Tinh trung binh so luong ton cua hang dien may");
            System.out.println("8. Tim kiem hang hoa theo ma/ten");
            System.out.println("9. Thong ke tong quan");
            System.out.println("10. Thoat");
            System.out.print("Chon chuc nang (1-10): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        inDanhSachTatCaHangHoa();
                        break;
                    case 2:
                        themHangHoaMoi();
                        break;
                    case 3:
                        suaHangHoa();
                        break;
                    case 4:
                        xoaHangHoa();
                        break;
                    case 5:
                        timSanPhamConMotTuanHetHan();
                        break;
                    case 6:
                        tinhTongSoLuongTheoLoai();
                        break;
                    case 7:
                        tinhTrungBinhSoLuongDienMay();
                        break;
                    case 8:
                        timKiemHangHoa();
                        break;
                    case 9:
                        thongKeTongQuan();
                        break;
                    case 10:
                        System.out.println("Cam on ban da su dung chuong trinh!");
                        return;
                    default:
                        System.out.println("Lua chon khong hop le! Vui long chon tu 1-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so tu 1-8!");
            }
        }
    }
    
    // 1. In danh sach tat ca hang hoa (thay the cho xemDanhSachHangHoa)
    private static void inDanhSachTatCaHangHoa() {
        System.out.println("\n=== DANH SACH TAT CA HANG HOA ===");
        List<HangHoa> danhSach = hangHoaDAO.layDanhSachHangHoa();
        
        if (danhSach.isEmpty()) {
            System.out.println("Khong co hang hoa nao trong kho!");
            return;
        }
        
        // In header
        System.out.printf("%-10s %-25s %-12s %-8s %-15s %-8s %-25s%n", 
            "Ma Hang", "Ten Hang", "Loai", "SL Ton", "Don Gia", "VAT%", "Thong Tin Chi Tiet");
        System.out.println("=".repeat(110));
        
        for (HangHoa hh : danhSach) {
            String chiTiet = "";
            if (hh instanceof ThucPham) {
                ThucPham tp = (ThucPham) hh;
                chiTiet = String.format("NSX:%s, HH:%s, NCC:%s", 
                    tp.getNgaySanXuat().format(formatter),
                    tp.getNgayHetHan().format(formatter),
                    tp.getNhaCungCap());
            } else if (hh instanceof DienMay) {
                DienMay dm = (DienMay) hh;
                chiTiet = String.format("BH:%d thang, CS:%.1fW", 
                    dm.getThoiGianBaoHanh(), dm.getCongSuat());
            } else if (hh instanceof SanhSu) {
                SanhSu ss = (SanhSu) hh;
                chiTiet = String.format("NSX:%s, NgayNhap:%s", 
                    ss.getNhaSanXuat(), ss.getNgayNhapKho().format(formatter));
            }
            
            System.out.printf("%-10s %-25s %-12s %-8d %,-15.0f %-8.1f %-25s%n",
                hh.getMaHang(), hh.getTenHang(), hh.getClass().getSimpleName(),
                hh.getSoLuongTon(), hh.getDonGia(), hh.tinhVAT() * 100, chiTiet);
        }
        
        System.out.println("\nTong so mat hang: " + danhSach.size());
    }
    
    private static void xemDanhSachHangHoa() {
        // Redirect to new method
        inDanhSachTatCaHangHoa();
    }
    
    private static void themHangHoaMoi() {
        System.out.println("\n=== THEM HANG HOA MOI ===");
        System.out.println("1. Them Thuc Pham");
        System.out.println("2. Them Dien May");
        System.out.println("3. Them Sanh Su");
        System.out.print("Chon loai hang hoa (1-3): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    themThucPham();
                    break;
                case 2:
                    themDienMay();
                    break;
                case 3:
                    themSanhSu();
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui long nhap so tu 1-3!");
        }
    }
    
    private static void themThucPham() {
        System.out.println("\n--- THEM THUC PHAM ---");
        try {
            System.out.print("Ma hang: ");
            String maHang = scanner.nextLine();
            
            System.out.print("Ten hang: ");
            String tenHang = scanner.nextLine();
            
            System.out.print("So luong ton: ");
            int soLuongTon = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Don gia: ");
            double donGia = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Ngay san xuat (dd/MM/yyyy): ");
            String ngaySXStr = scanner.nextLine();
            LocalDate ngaySanXuat = LocalDate.parse(ngaySXStr, formatter);
            
            System.out.print("Ngay het han (dd/MM/yyyy): ");
            String ngayHHStr = scanner.nextLine();
            LocalDate ngayHetHan = LocalDate.parse(ngayHHStr, formatter);
            
            System.out.print("Nha cung cap: ");
            String nhaCungCap = scanner.nextLine();
            
            ThucPham thucPham = new ThucPham(maHang, tenHang, soLuongTon, donGia, ngaySanXuat, ngayHetHan, nhaCungCap);
            
            if (hangHoaDAO.themThucPham(thucPham)) {
                System.out.println("✅ Them thuc pham thanh cong!");
            } else {
                System.out.println("❌ Them thuc pham that bai!");
            }
            
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
    
    private static void themDienMay() {
        System.out.println("Chuc nang them dien may dang phat trien...");
    }
    
    private static void themSanhSu() {
        System.out.println("Chuc nang them sanh su dang phat trien...");
    }
    
    private static void timKiemHangHoa() {
        System.out.println("Chuc nang tim kiem dang phat trien...");
    }
    
    private static void timSanPhamSapHetHan() {
        System.out.println("Chuc nang tim san pham sap het han dang phat trien...");
    }
    
    private static void thongKeTheoLoai() {
        System.out.println("Chuc nang thong ke dang phat trien...");
    }
    
    private static void xoaHangHoa() {
        System.out.println("Chuc nang xoa hang hoa dang phat trien...");
    }
    
    private static void capNhatHangHoa() {
        System.out.println("Chuc nang cap nhat hang hoa dang phat trien...");
    }
    
    // 3. Sua hang hoa - Chi tiet full
    private static void suaHangHoa() {
        System.out.println("\n=== SUA HANG HOA ===");
        System.out.print("Nhap ma hang can sua: ");
        String maHang = scanner.nextLine();
        
        HangHoa hangHoa = hangHoaDAO.timHangHoa(maHang);
        if (hangHoa == null) {
            System.out.println("Khong tim thay hang hoa co ma: " + maHang);
            return;
        }
        
        System.out.println("Thong tin hien tai:");
        hienThiThongTinHangHoa(hangHoa);
        
        try {
            System.out.print("Ten hang moi (Enter de giu nguyen): ");
            String tenHang = scanner.nextLine();
            if (tenHang.trim().isEmpty()) {
                tenHang = hangHoa.getTenHang();
            }
            
            System.out.print("So luong ton moi (hien tai: " + hangHoa.getSoLuongTon() + "): ");
            String slStr = scanner.nextLine();
            int soLuong = slStr.trim().isEmpty() ? hangHoa.getSoLuongTon() : Integer.parseInt(slStr);
            
            System.out.print("Don gia moi (hien tai: " + hangHoa.getDonGia() + "): ");
            String giaStr = scanner.nextLine();
            double donGia = giaStr.trim().isEmpty() ? hangHoa.getDonGia() : Double.parseDouble(giaStr);
            
            boolean result = false;
            
            if (hangHoa instanceof ThucPham) {
                ThucPham tp = (ThucPham) hangHoa;
                System.out.print("Ngay san xuat moi (dd/MM/yyyy, Enter de giu nguyen): ");
                String ngaySXStr = scanner.nextLine();
                LocalDate ngaySX = ngaySXStr.trim().isEmpty() ? tp.getNgaySanXuat() : 
                    LocalDate.parse(ngaySXStr, formatter);
                
                System.out.print("Ngay het han moi (dd/MM/yyyy, Enter de giu nguyen): ");
                String ngayHHStr = scanner.nextLine();
                LocalDate ngayHH = ngayHHStr.trim().isEmpty() ? tp.getNgayHetHan() : 
                    LocalDate.parse(ngayHHStr, formatter);
                
                System.out.print("Nha cung cap moi (Enter de giu nguyen): ");
                String nhaCungCap = scanner.nextLine();
                if (nhaCungCap.trim().isEmpty()) {
                    nhaCungCap = tp.getNhaCungCap();
                }
                
                result = hangHoaDAO.capNhatThucPham(maHang, tenHang, soLuong, donGia, ngaySX, ngayHH, nhaCungCap);
                
            } else if (hangHoa instanceof DienMay) {
                DienMay dm = (DienMay) hangHoa;
                System.out.print("Thoi gian bao hanh moi (thang, hien tai: " + dm.getThoiGianBaoHanh() + "): ");
                String bhStr = scanner.nextLine();
                int baoHanh = bhStr.trim().isEmpty() ? dm.getThoiGianBaoHanh() : Integer.parseInt(bhStr);
                
                System.out.print("Cong suat moi (W, hien tai: " + dm.getCongSuat() + "): ");
                String csStr = scanner.nextLine();
                double congSuat = csStr.trim().isEmpty() ? dm.getCongSuat() : Double.parseDouble(csStr);
                
                result = hangHoaDAO.capNhatDienMay(maHang, tenHang, soLuong, donGia, baoHanh, congSuat);
                
            } else if (hangHoa instanceof SanhSu) {
                SanhSu ss = (SanhSu) hangHoa;
                System.out.print("Nha san xuat moi (Enter de giu nguyen): ");
                String nhaSanXuat = scanner.nextLine();
                if (nhaSanXuat.trim().isEmpty()) {
                    nhaSanXuat = ss.getNhaSanXuat();
                }
                
                System.out.print("Ngay nhap kho moi (dd/MM/yyyy, Enter de giu nguyen): ");
                String ngayNhapStr = scanner.nextLine();
                LocalDate ngayNhap = ngayNhapStr.trim().isEmpty() ? ss.getNgayNhapKho() : 
                    LocalDate.parse(ngayNhapStr, formatter);
                
                result = hangHoaDAO.capNhatSanhSu(maHang, tenHang, soLuong, donGia, nhaSanXuat, ngayNhap);
            }
            
            if (result) {
                System.out.println("Cap nhat hang hoa thanh cong!");
            } else {
                System.out.println("Cap nhat hang hoa that bai!");
            }
            
        } catch (Exception e) {
            System.out.println("Loi cap nhat: " + e.getMessage());
        }
    }
    
    // 5. Tim san pham con 1 tuan la het han
    private static void timSanPhamConMotTuanHetHan() {
        System.out.println("\n=== SAN PHAM CON 1 TUAN LA HET HAN ===");
        List<ThucPham> danhSach = hangHoaDAO.timSanPhamSapHetHanTrongTuan();
        
        if (danhSach.isEmpty()) {
            System.out.println("Khong co san pham nao sap het han trong 1 tuan toi!");
            return;
        }
        
        System.out.printf("%-10s %-25s %-8s %-15s %-12s %-20s%n", 
            "Ma Hang", "Ten Hang", "SL Ton", "Don Gia", "Ngay Het Han", "Nha Cung Cap");
        System.out.println("=".repeat(95));
        
        for (ThucPham tp : danhSach) {
            System.out.printf("%-10s %-25s %-8d %,-15.0f %-12s %-20s%n",
                tp.getMaHang(), tp.getTenHang(), tp.getSoLuongTon(),
                tp.getDonGia(), tp.getNgayHetHan().format(formatter), tp.getNhaCungCap());
        }
        
        System.out.println("\nTong so san pham sap het han: " + danhSach.size());
        System.out.println("*** CHU Y: Can xu ly gap cac san pham tren! ***");
    }
    
    // 6. Tinh tong so luong cho tung loai hang hoa
    private static void tinhTongSoLuongTheoLoai() {
        System.out.println("\n=== TINH TONG SO LUONG THEO LOAI ===");
        hangHoaDAO.tinhTongSoLuongTheoLoai();
    }
    
    // 7. Tinh trung binh so luong ton cua hang dien may
    private static void tinhTrungBinhSoLuongDienMay() {
        System.out.println("\n=== THONG KE HANG DIEN MAY ===");
        hangHoaDAO.tinhTrungBinhSoLuongDienMay();
    }
    
    // 9. Thong ke tong quan
    private static void thongKeTongQuan() {
        System.out.println("\n=== THONG KE TONG QUAN ===");
        hangHoaDAO.tinhTongSoLuongTheoLoai();
        System.out.println();
        hangHoaDAO.tinhTrungBinhSoLuongDienMay();
        
        // Thong ke them
        List<HangHoa> danhSach = hangHoaDAO.layDanhSachHangHoa();
        if (!danhSach.isEmpty()) {
            double tongGiaTri = danhSach.stream()
                .mapToDouble(hh -> hh.getSoLuongTon() * hh.getDonGia())
                .sum();
            
            System.out.println("\n=== THONG TIN TONG HOP ===");
            System.out.println("Tong so mat hang trong kho: " + danhSach.size());
            System.out.printf("Tong gia tri kho hang: %,.0f VND%n", tongGiaTri);
            
            // Tim san pham co gia tri cao nhat
            HangHoa maxValue = danhSach.stream()
                .max((h1, h2) -> Double.compare(h1.getDonGia(), h2.getDonGia()))
                .orElse(null);
            if (maxValue != null) {
                System.out.printf("San pham co gia tri cao nhat: %s - %,.0f VND%n", 
                    maxValue.getTenHang(), maxValue.getDonGia());
            }
        }
    }
    
    // Helper method de hien thi thong tin hang hoa
    private static void hienThiThongTinHangHoa(HangHoa hangHoa) {
        System.out.printf("Ma: %s, Ten: %s, Loai: %s, SL: %d, Gia: %,.0f, VAT: %.1f%%%n",
            hangHoa.getMaHang(), hangHoa.getTenHang(), hangHoa.getClass().getSimpleName(),
            hangHoa.getSoLuongTon(), hangHoa.getDonGia(), hangHoa.tinhVAT() * 100);
        
        if (hangHoa instanceof ThucPham) {
            ThucPham tp = (ThucPham) hangHoa;
            System.out.printf("  Chi tiet: NSX: %s, HH: %s, NCC: %s%n",
                tp.getNgaySanXuat().format(formatter),
                tp.getNgayHetHan().format(formatter),
                tp.getNhaCungCap());
        } else if (hangHoa instanceof DienMay) {
            DienMay dm = (DienMay) hangHoa;
            System.out.printf("  Chi tiet: Bao hanh: %d thang, Cong suat: %.1fW%n",
                dm.getThoiGianBaoHanh(), dm.getCongSuat());
        } else if (hangHoa instanceof SanhSu) {
            SanhSu ss = (SanhSu) hangHoa;
            System.out.printf("  Chi tiet: NSX: %s, Ngay nhap: %s%n",
                ss.getNhaSanXuat(), ss.getNgayNhapKho().format(formatter));
        }
    }
}
