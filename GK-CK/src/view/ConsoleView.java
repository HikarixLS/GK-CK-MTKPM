package view;

import controller.HangHoaController;
import model.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Console View cho Warehouse Management System
 * View Layer trong MVC Pattern
 */
public class ConsoleView {
    private HangHoaController controller;
    private Scanner scanner;
    
    public ConsoleView() {
        this.controller = new HangHoaController();
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("🏪 WAREHOUSE MANAGEMENT SYSTEM - CONSOLE MODE");
        System.out.println("=========================================");
        System.out.println("📋 MVC Pattern Implementation");
        System.out.println("=========================================\n");
        
        int choice;
        do {
            displayMenu();
            choice = getIntInput("👉 Chọn chức năng (0-9): ");
            
            switch (choice) {
                case 1:
                    themHangHoa();
                    break;
                case 2:
                    hienThiDanhSach();
                    break;
                case 3:
                    timKiemHangHoa();
                    break;
                case 4:
                    capNhatHangHoa();
                    break;
                case 5:
                    xoaHangHoa();
                    break;
                case 6:
                    hienThiThongKe();
                    break;
                case 7:
                    timSanPhamSapHetHan();
                    break;
                case 8:
                    xemChiTietHangHoa();
                    break;
                case 9:
                    hienThiGiaTriKho();
                    break;
                case 0:
                    System.out.println("👋 Cảm ơn bạn đã sử dụng hệ thống!");
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
            }
            
            if (choice != 0) {
                System.out.println("\n⏸️ Nhấn Enter để tiếp tục...");
                scanner.nextLine();
            }
            
        } while (choice != 0);
    }
    
    private void displayMenu() {
        System.out.println("\n🏪 ==== MENU CHÍNH ====");
        System.out.println("1️⃣  Thêm hàng hóa");
        System.out.println("2️⃣  Hiển thị danh sách hàng hóa");
        System.out.println("3️⃣  Tìm kiếm hàng hóa");
        System.out.println("4️⃣  Cập nhật thông tin hàng hóa");
        System.out.println("5️⃣  Xóa hàng hóa");
        System.out.println("6️⃣  Hiển thị thống kê");
        System.out.println("7️⃣  Tìm sản phẩm sắp hết hạn");
        System.out.println("8️⃣  Xem chi tiết hàng hóa");
        System.out.println("9️⃣  Hiển thị giá trị kho hàng");
        System.out.println("0️⃣  Thoát");
        System.out.println("========================");
    }
    
    private void themHangHoa() {
        System.out.println("\n➕ ==== THÊM HÀNG HÓA ====");
        
        System.out.println("Chọn loại hàng hóa:");
        System.out.println("1. Thực phẩm");
        System.out.println("2. Điện máy");
        System.out.println("3. Sành sứ");
        
        int loai = getIntInput("👉 Chọn loại (1-3): ");
        
        switch (loai) {
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
                System.out.println("❌ Loại hàng hóa không hợp lệ!");
        }
    }
    
    private void themThucPham() {
        System.out.println("\n🍎 THÊM THỰC PHẨM");
        System.out.println("==================");
        
        String maHang = getStringInput("Mã hàng: ");
        String tenHang = getStringInput("Tên hàng: ");
        int soLuong = getIntInput("Số lượng tồn: ");
        double donGia = getDoubleInput("Đơn giá: ");
        String ngaySanXuat = getStringInput("Ngày sản xuất (dd/MM/yyyy): ");
        String ngayHetHan = getStringInput("Ngày hết hạn (dd/MM/yyyy): ");
        String nhaCungCap = getStringInput("Nhà cung cấp: ");
        
        if (controller.themThucPham(maHang, tenHang, soLuong, donGia, 
                                   ngaySanXuat, ngayHetHan, nhaCungCap)) {
            System.out.println("✅ Thêm thực phẩm thành công!");
        } else {
            System.out.println("❌ Thêm thực phẩm thất bại!");
        }
    }
    
    private void themDienMay() {
        System.out.println("\n⚡ THÊM ĐIỆN MÁY");
        System.out.println("================");
        
        String maHang = getStringInput("Mã hàng: ");
        String tenHang = getStringInput("Tên hàng: ");
        int soLuong = getIntInput("Số lượng tồn: ");
        double donGia = getDoubleInput("Đơn giá: ");
        int thoiGianBaoHanh = getIntInput("Thời gian bảo hành (tháng): ");
        double congSuat = getDoubleInput("Công suất (KW): ");
        
        if (controller.themDienMay(maHang, tenHang, soLuong, donGia, 
                                  thoiGianBaoHanh, congSuat)) {
            System.out.println("✅ Thêm điện máy thành công!");
        } else {
            System.out.println("❌ Thêm điện máy thất bại!");
        }
    }
    
    private void themSanhSu() {
        System.out.println("\n🏺 THÊM SÀNH SỨ");
        System.out.println("===============");
        
        String maHang = getStringInput("Mã hàng: ");
        String tenHang = getStringInput("Tên hàng: ");
        int soLuong = getIntInput("Số lượng tồn: ");
        double donGia = getDoubleInput("Đơn giá: ");
        String nhaSanXuat = getStringInput("Nhà sản xuất: ");
        String ngayNhapKho = getStringInput("Ngày nhập kho (dd/MM/yyyy): ");
        
        if (controller.themSanhSu(maHang, tenHang, soLuong, donGia, 
                                 nhaSanXuat, ngayNhapKho)) {
            System.out.println("✅ Thêm sành sứ thành công!");
        } else {
            System.out.println("❌ Thêm sành sứ thất bại!");
        }
    }
    
    private void hienThiDanhSach() {
        System.out.println("\n📦 ==== DANH SÁCH HÀNG HÓA ====");
        
        List<HangHoa> danhSach = controller.layDanhSachHangHoa();
        
        if (danhSach.isEmpty()) {
            System.out.println("📭 Danh sách trống!");
            return;
        }
        
        System.out.println("================================================================================================");
        System.out.printf("%-10s %-25s %-12s %-8s %-15s %-8s %-15s %-20s%n", 
                         "Mã hàng", "Tên hàng", "Loại", "SL tồn", "Đơn giá", "VAT(%)", "Giá có VAT", "Thông tin đặc biệt");
        System.out.println("================================================================================================");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (HangHoa hh : danhSach) {
            String loai = "";
            String thongTinDacBiet = "";
            
            if (hh instanceof ThucPham) {
                ThucPham tp = (ThucPham) hh;
                loai = "Thực phẩm";
                thongTinDacBiet = "HSD: " + tp.getNgayHetHan().format(formatter);
            } else if (hh instanceof DienMay) {
                DienMay dm = (DienMay) hh;
                loai = "Điện máy";
                thongTinDacBiet = "BH: " + dm.getThoiGianBaoHanh() + "th, " + dm.getCongSuat() + "KW";
            } else if (hh instanceof SanhSu) {
                SanhSu ss = (SanhSu) hh;
                loai = "Sành sứ";
                thongTinDacBiet = "NSX: " + ss.getNhaSanXuat();
            }
            
            System.out.printf("%-10s %-25s %-12s %-8d %,15.0f %8.1f %,15.0f %-20s%n",
                             hh.getMaHang(),
                             hh.getTenHang().length() > 25 ? hh.getTenHang().substring(0, 22) + "..." : hh.getTenHang(),
                             loai,
                             hh.getSoLuongTon(),
                             hh.getDonGia(),
                             hh.getVATRate(),
                             hh.getGiaCoVAT(),
                             thongTinDacBiet);
        }
        
        System.out.println("================================================================================================");
        System.out.printf("📊 Tổng số sản phẩm: %d | Tổng giá trị có VAT: %,.0f VNĐ%n", 
                         danhSach.size(), controller.tinhTongGiaTriCoVAT());
    }
    
    private void timKiemHangHoa() {
        System.out.println("\n🔍 ==== TÌM KIẾM HÀNG HÓA ====");
        
        String maHang = getStringInput("Nhập mã hàng cần tìm: ");
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa != null) {
            System.out.println("✅ Tìm thấy hàng hóa:");
            hienThiChiTiet(hangHoa);
        } else {
            System.out.println("❌ Không tìm thấy hàng hóa với mã: " + maHang);
        }
    }
    
    private void capNhatHangHoa() {
        System.out.println("\n✏️ ==== CẬP NHẬT HÀNG HÓA ====");
        
        String maHang = getStringInput("Nhập mã hàng cần cập nhật: ");
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa == null) {
            System.out.println("❌ Không tìm thấy hàng hóa với mã: " + maHang);
            return;
        }
        
        System.out.println("📋 Thông tin hiện tại:");
        hienThiChiTiet(hangHoa);
        
        System.out.println("\n📝 Nhập thông tin mới:");
        String tenHang = getStringInput("Tên hàng mới (hiện tại: " + hangHoa.getTenHang() + "): ");
        int soLuong = getIntInput("Số lượng tồn mới (hiện tại: " + hangHoa.getSoLuongTon() + "): ");
        double donGia = getDoubleInput("Đơn giá mới (hiện tại: " + hangHoa.getDonGia() + "): ");
        
        if (controller.capNhatHangHoa(maHang, tenHang, soLuong, donGia)) {
            System.out.println("✅ Cập nhật hàng hóa thành công!");
        } else {
            System.out.println("❌ Cập nhật hàng hóa thất bại!");
        }
    }
    
    private void xoaHangHoa() {
        System.out.println("\n🗑️ ==== XÓA HÀNG HÓA ====");
        
        String maHang = getStringInput("Nhập mã hàng cần xóa: ");
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa == null) {
            System.out.println("❌ Không tìm thấy hàng hóa với mã: " + maHang);
            return;
        }
        
        System.out.println("📋 Thông tin hàng hóa sẽ bị xóa:");
        hienThiChiTiet(hangHoa);
        
        String confirm = getStringInput("\n⚠️ Bạn có chắc muốn xóa? (y/N): ");
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            if (controller.xoaHangHoa(maHang)) {
                System.out.println("✅ Xóa hàng hóa thành công!");
            } else {
                System.out.println("❌ Xóa hàng hóa thất bại!");
            }
        } else {
            System.out.println("↩️ Đã hủy thao tác xóa.");
        }
    }
    
    private void hienThiThongKe() {
        System.out.println("\n📊 ==== THỐNG KÊ KHO HÀNG ====");
        
        int soThucPham = controller.getSoLuongTheoLoai("thucpham");
        int soDienMay = controller.getSoLuongTheoLoai("dienmay");
        int soSanhSu = controller.getSoLuongTheoLoai("sanhsu");
        int tongSo = soThucPham + soDienMay + soSanhSu;
        
        System.out.println("==========================================");
        System.out.printf("🍎 Thực phẩm:    %3d sản phẩm (%5.1f%%)%n", 
                         soThucPham, tongSo > 0 ? (soThucPham * 100.0 / tongSo) : 0);
        System.out.printf("⚡ Điện máy:     %3d sản phẩm (%5.1f%%)%n", 
                         soDienMay, tongSo > 0 ? (soDienMay * 100.0 / tongSo) : 0);
        System.out.printf("🏺 Sành sứ:      %3d sản phẩm (%5.1f%%)%n", 
                         soSanhSu, tongSo > 0 ? (soSanhSu * 100.0 / tongSo) : 0);
        System.out.println("------------------------------------------");
        System.out.printf("📦 Tổng cộng:    %3d sản phẩm%n", tongSo);
        System.out.println("==========================================");
        
        double tongGiaTri = controller.tinhTongGiaTriKho();
        System.out.printf("💰 Tổng giá trị kho: %,.0f VNĐ%n", tongGiaTri);
        
        double trungBinhDienMay = controller.tinhTrungBinhSoLuongDienMay();
        System.out.printf("📊 Trung bình SL điện máy: %.2f%n", trungBinhDienMay);
        
        // Chi tiết thống kê từ controller
        System.out.println("\n📈 THỐNG KÊ CHI TIẾT:");
        controller.hienThiThongKe();
    }
    
    private void timSanPhamSapHetHan() {
        System.out.println("\n⚠️ ==== SẢN PHẨM SẮP HẾT HẠN ====");
        
        List<ThucPham> sapHetHan = controller.timSanPhamSapHetHan();
        
        if (sapHetHan.isEmpty()) {
            System.out.println("✅ Không có sản phẩm nào sắp hết hạn trong tuần!");
            return;
        }
        
        System.out.println("🚨 Tìm thấy " + sapHetHan.size() + " sản phẩm sắp hết hạn:");
        System.out.println("================================================================");
        System.out.printf("%-10s %-25s %-12s %-15s%n", 
                         "Mã hàng", "Tên hàng", "Ngày hết hạn", "Nhà cung cấp");
        System.out.println("================================================================");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (ThucPham tp : sapHetHan) {
            System.out.printf("%-10s %-25s %-12s %-15s%n",
                             tp.getMaHang(),
                             tp.getTenHang().length() > 25 ? tp.getTenHang().substring(0, 22) + "..." : tp.getTenHang(),
                             tp.getNgayHetHan().format(formatter),
                             tp.getNhaCungCap());
        }
        System.out.println("================================================================");
    }
    
    private void xemChiTietHangHoa() {
        System.out.println("\n👁️ ==== XEM CHI TIẾT HÀNG HÓA ====");
        
        String maHang = getStringInput("Nhập mã hàng: ");
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa != null) {
            hienThiChiTiet(hangHoa);
            
            // Thông tin bổ sung
            System.out.println("\n📊 THÔNG TIN BỔ SUNG:");
            System.out.printf("💰 VAT: %.1f%%%n", hangHoa.getVATRate());
            System.out.printf("💰 Giá có VAT: %,.0f VNĐ%n", hangHoa.getGiaCoVAT());
            System.out.printf("📈 Hàng khó bán: %s%n", hangHoa.daKho() ? "Có" : "Không");
            System.out.printf("💵 Tổng giá trị có VAT: %,.0f VNĐ%n", hangHoa.getTongGiaTriCoVAT());
        } else {
            System.out.println("❌ Không tìm thấy hàng hóa với mã: " + maHang);
        }
    }
    
    private void hienThiGiaTriKho() {
        System.out.println("\n💰 ==== GIÁ TRỊ KHO HÀNG ====");
        
        List<HangHoa> danhSach = controller.layDanhSachHangHoa();
        double tongGiaTri = controller.tinhTongGiaTriKho();
        double tongGiaTriCoVAT = controller.tinhTongGiaTriCoVAT();
        
        System.out.printf("📦 Tổng số sản phẩm: %d%n", danhSach.size());
        System.out.printf("💰 Tổng giá trị kho: %,.0f VNĐ%n", tongGiaTri);
        System.out.printf("💰 Tổng giá trị có VAT: %,.0f VNĐ%n", tongGiaTriCoVAT);
        
        if (!danhSach.isEmpty()) {
            System.out.printf("📊 Giá trị trung bình/sản phẩm: %,.0f VNĐ%n", 
                             tongGiaTri / danhSach.size());
            System.out.printf("📊 Giá trị trung bình có VAT/sản phẩm: %,.0f VNĐ%n", 
                             tongGiaTriCoVAT / danhSach.size());
        }
        
        // Thống kê theo loại
        double giaTriThucPham = 0, giaTriDienMay = 0, giaTriSanhSu = 0;
        
        for (HangHoa hh : danhSach) {
            double giaTri = hh.getSoLuongTon() * hh.getDonGia();
            
            if (hh instanceof ThucPham) {
                giaTriThucPham += giaTri;
            } else if (hh instanceof DienMay) {
                giaTriDienMay += giaTri;
            } else if (hh instanceof SanhSu) {
                giaTriSanhSu += giaTri;
            }
        }
        
        System.out.println("\n💎 PHÂN TÍCH GIÁ TRỊ THEO LOẠI:");
        System.out.println("=====================================");
        System.out.printf("🍎 Thực phẩm: %,.0f VNĐ (%5.1f%%)%n", 
                         giaTriThucPham, tongGiaTri > 0 ? (giaTriThucPham * 100.0 / tongGiaTri) : 0);
        System.out.printf("⚡ Điện máy:  %,.0f VNĐ (%5.1f%%)%n", 
                         giaTriDienMay, tongGiaTri > 0 ? (giaTriDienMay * 100.0 / tongGiaTri) : 0);
        System.out.printf("🏺 Sành sứ:   %,.0f VNĐ (%5.1f%%)%n", 
                         giaTriSanhSu, tongGiaTri > 0 ? (giaTriSanhSu * 100.0 / tongGiaTri) : 0);
    }
    
    private void hienThiChiTiet(HangHoa hangHoa) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        System.out.println("🏷️ ================================");
        System.out.printf("📋 Mã hàng: %s%n", hangHoa.getMaHang());
        System.out.printf("📝 Tên hàng: %s%n", hangHoa.getTenHang());
        System.out.printf("📦 Số lượng tồn: %d%n", hangHoa.getSoLuongTon());
        System.out.printf("💰 Đơn giá: %,.0f VNĐ%n", hangHoa.getDonGia());
        
        if (hangHoa instanceof ThucPham) {
            ThucPham tp = (ThucPham) hangHoa;
            System.out.println("🍎 LOẠI: Thực phẩm");
            System.out.printf("📅 Ngày sản xuất: %s%n", tp.getNgaySanXuat().format(formatter));
            System.out.printf("⏰ Ngày hết hạn: %s%n", tp.getNgayHetHan().format(formatter));
            System.out.printf("🏭 Nhà cung cấp: %s%n", tp.getNhaCungCap());
            
        } else if (hangHoa instanceof DienMay) {
            DienMay dm = (DienMay) hangHoa;
            System.out.println("⚡ LOẠI: Điện máy");
            System.out.printf("🛡️ Thời gian bảo hành: %d tháng%n", dm.getThoiGianBaoHanh());
            System.out.printf("⚡ Công suất: %.2f KW%n", dm.getCongSuat());
            
        } else if (hangHoa instanceof SanhSu) {
            SanhSu ss = (SanhSu) hangHoa;
            System.out.println("🏺 LOẠI: Sành sứ");
            System.out.printf("🏭 Nhà sản xuất: %s%n", ss.getNhaSanXuat());
            System.out.printf("📅 Ngày nhập kho: %s%n", ss.getNgayNhapKho().format(formatter));
        }
        
        System.out.println("🏷️ ================================");
    }
    
    // ================= INPUT HELPER METHODS =================
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("❌ Không được để trống! " + prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số nguyên hợp lệ!");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Vui lòng nhập một số thực hợp lệ!");
            }
        }
    }
    
    // ================= MAIN METHOD =================
    
    public static void main(String[] args) {
        try {
            ConsoleView console = new ConsoleView();
            console.start();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khởi tạo ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
