import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class KhoGUI extends JFrame {
    private HangHoaDAO dao;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMa, txtTen, txtSL, txtGia;
    private JComboBox<String> cboLoai;
    private JTextArea txtKQ;

    // Trường cho Thực phẩm
    private JTextField txtNgaySX, txtNgayHH, txtNhaCungCap;
    private JLabel lblNgaySX, lblNgayHH, lblNhaCungCap;

    // Trường cho Điện máy
    private JTextField txtBaoHanh, txtCongSuat;
    private JLabel lblBaoHanh, lblCongSuat;

    // Trường cho Sành sứ
    private JTextField txtNhaSanXuat, txtNgayNhapKho;
    private JLabel lblNhaSanXuat, lblNgayNhapKho;

    public KhoGUI() {
        try {
            dao = new HangHoaDAO();
            System.out.println("✅ Kết nối database OK!");
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "❌ Lỗi kết nối database!\n\n" +
                            "Chi tiết: " + e.getMessage() + "\n\n" +
                            "Vui lòng kiểm tra:\n" +
                            "1. XAMPP đã khởi động MySQL chưa?\n" +
                            "2. Database 'QuanLyKhoHang' đã tồn tại chưa?\n" +
                            "3. File database.properties có đúng thông tin?\n\n" +
                            "Chương trình sẽ thoát.",
                    "Lỗi Database",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("🏪 Quản Lý Kho");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Components
        txtMa = new JTextField(10);
        txtTen = new JTextField(15);
        txtSL = new JTextField(8);
        txtGia = new JTextField(10);
        cboLoai = new JComboBox<>(new String[] { "ThucPham", "DienMay", "SanhSu" });

        // Khởi tạo các trường chi tiết
        txtNgaySX = new JTextField(10);
        txtNgayHH = new JTextField(10);
        txtNhaCungCap = new JTextField(15);
        txtBaoHanh = new JTextField(8);
        txtCongSuat = new JTextField(10);
        txtNhaSanXuat = new JTextField(15);
        txtNgayNhapKho = new JTextField(10);

        // Thêm listener để hiện/ẩn trường theo loại
        cboLoai.addActionListener(e -> updateFieldsVisibility());

        // Table
        String[] cols = { "Mã", "Tên", "Loại", "SL", "Giá", "VAT%" };
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                selectItem();
        });

        // Result area
        txtKQ = new JTextArea(4, 30);
        txtKQ.setEditable(false);
        txtKQ.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));

        // Layout
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Top - Buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(createBtn("📋 Xem tất cả", this::xemDS));
        topPanel.add(createBtn("➕ Thêm", this::them));
        topPanel.add(createBtn("⏰ Hết hạn", this::hetHan));
        topPanel.add(createBtn("📊 Tổng theo loại", this::tongTheoLoai));
        topPanel.add(createBtn("📈 TB điện máy", this::trungBinhDienMay));
        topPanel.add(createBtn("❌ Xóa", this::xoa));
        topPanel.add(createBtn("✏️ Sửa", this::sua));
        topPanel.add(createBtn("🚪 Thoát", this::thoat));
        add(topPanel, BorderLayout.NORTH);

        // Center - Split
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left - Form
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("📝 Thông Tin"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        // Thông tin chung
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtMa, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        leftPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtTen, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        leftPanel.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(cboLoai, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        leftPanel.add(new JLabel("SL:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtSL, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        leftPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtGia, gbc);

        // Separator
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        leftPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Trường cho Thực phẩm
        lblNgaySX = new JLabel("🍎 Ngày SX (dd/MM/yyyy):");
        gbc.gridx = 0;
        gbc.gridy = 6;
        leftPanel.add(lblNgaySX, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtNgaySX, gbc);

        lblNgayHH = new JLabel("🍎 Ngày HH (dd/MM/yyyy):");
        gbc.gridx = 0;
        gbc.gridy = 7;
        leftPanel.add(lblNgayHH, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtNgayHH, gbc);

        lblNhaCungCap = new JLabel("🍎 Nhà cung cấp:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        leftPanel.add(lblNhaCungCap, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtNhaCungCap, gbc);

        // Trường cho Điện máy
        lblBaoHanh = new JLabel("⚡ Bảo hành (tháng):");
        gbc.gridx = 0;
        gbc.gridy = 9;
        leftPanel.add(lblBaoHanh, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtBaoHanh, gbc);

        lblCongSuat = new JLabel("⚡ Công suất (W):");
        gbc.gridx = 0;
        gbc.gridy = 10;
        leftPanel.add(lblCongSuat, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtCongSuat, gbc);

        // Trường cho Sành sứ
        lblNhaSanXuat = new JLabel("🏺 Nhà sản xuất:");
        gbc.gridx = 0;
        gbc.gridy = 11;
        leftPanel.add(lblNhaSanXuat, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtNhaSanXuat, gbc);

        lblNgayNhapKho = new JLabel("🏺 Ngày nhập (dd/MM/yyyy):");
        gbc.gridx = 0;
        gbc.gridy = 12;
        leftPanel.add(lblNgayNhapKho, gbc);
        gbc.gridx = 1;
        leftPanel.add(txtNgayNhapKho, gbc);

        // Right - Table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("📊 Danh Sách"));
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        split.setLeftComponent(leftPanel);
        split.setRightComponent(rightPanel);
        split.setDividerLocation(350);
        add(split, BorderLayout.CENTER);

        // Bottom - Result
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("💬 Kết Quả"));
        bottomPanel.add(new JScrollPane(txtKQ), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Khởi tạo hiển thị ban đầu
        updateFieldsVisibility();
    }

    private void updateFieldsVisibility() {
        String loai = (String) cboLoai.getSelectedItem();

        // Ẩn tất cả trước
        setVisibleTP(false);
        setVisibleDM(false);
        setVisibleSS(false);

        // Hiện theo loại
        if ("ThucPham".equals(loai)) {
            setVisibleTP(true);
        } else if ("DienMay".equals(loai)) {
            setVisibleDM(true);
        } else if ("SanhSu".equals(loai)) {
            setVisibleSS(true);
        }

        revalidate();
        repaint();
    }

    private void setVisibleTP(boolean visible) {
        // Set visible cho label và field của thực phẩm
        lblNgaySX.setVisible(visible);
        txtNgaySX.setVisible(visible);
        lblNgayHH.setVisible(visible);
        txtNgayHH.setVisible(visible);
        lblNhaCungCap.setVisible(visible);
        txtNhaCungCap.setVisible(visible);
    }

    private void setVisibleDM(boolean visible) {
        // Set visible cho label và field của điện máy
        lblBaoHanh.setVisible(visible);
        txtBaoHanh.setVisible(visible);
        lblCongSuat.setVisible(visible);
        txtCongSuat.setVisible(visible);
    }

    private void setVisibleSS(boolean visible) {
        // Set visible cho label và field của sành sứ
        lblNhaSanXuat.setVisible(visible);
        txtNhaSanXuat.setVisible(visible);
        lblNgayNhapKho.setVisible(visible);
        txtNgayNhapKho.setVisible(visible);
    }

    private JButton createBtn(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // Các chức năng chính
    private void xemDS() {
        loadData();
        msg("📋 Đã load danh sách tất cả hàng hóa trong kho");
    }

    private void them() {
        try {
            String ma = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                msg("❌ Nhập đầy đủ mã và tên!");
                return;
            }

            int sl = Integer.parseInt(txtSL.getText().trim());
            double gia = Double.parseDouble(txtGia.getText().trim());
            String loai = (String) cboLoai.getSelectedItem();

            boolean ok = false;
            if ("ThucPham".equals(loai)) {
                String ngaySXStr = txtNgaySX.getText().trim();
                String ngayHHStr = txtNgayHH.getText().trim();
                String nhaCungCap = txtNhaCungCap.getText().trim();

                if (ngaySXStr.isEmpty() || ngayHHStr.isEmpty() || nhaCungCap.isEmpty()) {
                    msg("❌ Nhập đầy đủ thông tin thực phẩm!");
                    return;
                }

                LocalDate ngaySX = parseDate(ngaySXStr);
                LocalDate ngayHH = parseDate(ngayHHStr);
                if (ngaySX == null || ngayHH == null) {
                    msg("❌ Ngày không đúng định dạng dd/MM/yyyy!");
                    return;
                }

                ThucPham tp = new ThucPham(ma, ten, sl, gia, ngaySX, ngayHH, nhaCungCap);
                ok = dao.themThucPham(tp);

            } else if ("DienMay".equals(loai)) {
                String baoHanhStr = txtBaoHanh.getText().trim();
                String congSuatStr = txtCongSuat.getText().trim();

                if (baoHanhStr.isEmpty() || congSuatStr.isEmpty()) {
                    msg("❌ Nhập đầy đủ thông tin điện máy!");
                    return;
                }

                int baoHanh = Integer.parseInt(baoHanhStr);
                double congSuat = Double.parseDouble(congSuatStr);
                DienMay dm = new DienMay(ma, ten, sl, gia, baoHanh, congSuat);
                ok = dao.themDienMay(dm);

            } else {
                String nhaSanXuat = txtNhaSanXuat.getText().trim();
                String ngayNhapStr = txtNgayNhapKho.getText().trim();

                if (nhaSanXuat.isEmpty() || ngayNhapStr.isEmpty()) {
                    msg("❌ Nhập đầy đủ thông tin sành sứ!");
                    return;
                }

                LocalDate ngayNhap = parseDate(ngayNhapStr);
                if (ngayNhap == null) {
                    msg("❌ Ngày không đúng định dạng dd/MM/yyyy!");
                    return;
                }

                SanhSu ss = new SanhSu(ma, ten, sl, gia, nhaSanXuat, ngayNhap);
                ok = dao.themSanhSu(ss);
            }

            if (ok) {
                msg("✅ Thêm thành công!");
                loadData();
                clear();
            } else {
                msg("❌ Lỗi thêm hàng!");
            }
        } catch (Exception e) {
            msg("❌ Lỗi: " + e.getMessage());
        }
    }

    private void hetHan() {
        try {
            List<ThucPham> list = dao.timSanPhamSapHetHanTrongTuan();
            model.setRowCount(0);
            if (list.isEmpty()) {
                msg("✅ Không có sản phẩm sắp hết hạn trong 1 tuần");
            } else {
                for (ThucPham tp : list) {
                    addRow(tp);
                }
                msg("⚠️ Có " + list.size() + " sản phẩm sắp hết hạn trong 1 tuần!");
            }
        } catch (Exception e) {
            msg("❌ Lỗi: " + e.getMessage());
        }
    }

    private void tongTheoLoai() {
        try {
            // Sử dụng cửa sổ popup để hiển thị thống kê
            StringBuilder result = new StringBuilder();
            result.append("📊 TỔNG SỐ LƯỢNG THEO TỪNG LOẠI HÀNG HÓA\n");
            result.append("═══════════════════════════════════════\n\n");

            List<HangHoa> danhSach = dao.layDanhSachHangHoa();

            // Đếm theo loại
            int tongThucPham = 0, slThucPham = 0;
            int tongDienMay = 0, slDienMay = 0;
            int tongSanhSu = 0, slSanhSu = 0;

            for (HangHoa hh : danhSach) {
                if (hh instanceof ThucPham) {
                    tongThucPham += hh.getSoLuongTon();
                    slThucPham++;
                } else if (hh instanceof DienMay) {
                    tongDienMay += hh.getSoLuongTon();
                    slDienMay++;
                } else if (hh instanceof SanhSu) {
                    tongSanhSu += hh.getSoLuongTon();
                    slSanhSu++;
                }
            }

            result.append(String.format("🍎 THỰC PHẨM:\n"));
            result.append(String.format("   - Số mặt hàng: %d\n", slThucPham));
            result.append(String.format("   - Tổng số lượng tồn: %d\n\n", tongThucPham));

            result.append(String.format("⚡ ĐIỆN MÁY:\n"));
            result.append(String.format("   - Số mặt hàng: %d\n", slDienMay));
            result.append(String.format("   - Tổng số lượng tồn: %d\n\n", tongDienMay));

            result.append(String.format("🏺 SÀNH SỨ:\n"));
            result.append(String.format("   - Số mặt hàng: %d\n", slSanhSu));
            result.append(String.format("   - Tổng số lượng tồn: %d\n\n", tongSanhSu));

            result.append("═══════════════════════════════════════\n");
            result.append(String.format("📦 TỔNG CỘNG: %d mặt hàng, %d sản phẩm",
                    danhSach.size(), tongThucPham + tongDienMay + tongSanhSu));

            // Hiển thị trong dialog
            JTextArea textArea = new JTextArea(result.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "📊 Thống kê số lượng theo loại", JOptionPane.INFORMATION_MESSAGE);

            msg("📊 Đã hiển thị thống kê số lượng theo loại");

        } catch (Exception e) {
            msg("❌ Lỗi thống kê: " + e.getMessage());
        }
    }

    private void trungBinhDienMay() {
        try {
            List<HangHoa> danhSach = dao.layDanhSachHangHoa();

            // Lọc ra điện máy
            int tongSoLuong = 0;
            int soDienMay = 0;

            for (HangHoa hh : danhSach) {
                if (hh instanceof DienMay) {
                    tongSoLuong += hh.getSoLuongTon();
                    soDienMay++;
                }
            }

            StringBuilder result = new StringBuilder();
            result.append("📈 TRUNG BÌNH SỐ LƯỢNG TỒN - HÀNG ĐIỆN MÁY\n");
            result.append("═══════════════════════════════════════════\n\n");

            if (soDienMay == 0) {
                result.append("❌ Không có hàng điện máy nào trong kho!");
            } else {
                double trungBinh = (double) tongSoLuong / soDienMay;
                result.append(String.format("⚡ Số mặt hàng điện máy: %d\n", soDienMay));
                result.append(String.format("📦 Tổng số lượng tồn: %d\n", tongSoLuong));
                result.append(String.format("📊 Trung bình số lượng tồn: %.2f\n\n", trungBinh));

                // Chi tiết từng sản phẩm
                result.append("📋 CHI TIẾT TỪNG SẢN PHẨM:\n");
                result.append("─────────────────────────────────────────\n");
                for (HangHoa hh : danhSach) {
                    if (hh instanceof DienMay) {
                        DienMay dm = (DienMay) hh;
                        result.append(String.format("• %s (%s): %d sản phẩm\n",
                                dm.getTenHang(), dm.getMaHang(), dm.getSoLuongTon()));
                    }
                }
            }

            // Hiển thị trong dialog
            JTextArea textArea = new JTextArea(result.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 350));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "📈 Trung bình số lượng tồn - Điện máy", JOptionPane.INFORMATION_MESSAGE);

            msg("📈 Đã tính trung bình số lượng tồn điện máy");

        } catch (Exception e) {
            msg("❌ Lỗi tính trung bình: " + e.getMessage());
        }
    }

    private void xoa() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            msg("❌ Chọn hàng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "⚠️ Xóa hàng: " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.xoaHangHoa(ma)) {
                msg("✅ Xóa thành công!");
                loadData();
                clear();
            } else {
                msg("❌ Lỗi xóa!");
            }
        }
    }

    private void sua() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            msg("❌ Chọn hàng cần sửa!");
            return;
        }

        try {
            String ten = txtTen.getText().trim();
            int sl = Integer.parseInt(txtSL.getText().trim());
            double gia = Double.parseDouble(txtGia.getText().trim());

            if (dao.capNhatThongTinCoBan(ma, ten, sl, gia)) {
                msg("✅ Cập nhật thành công!");
                loadData();
            } else {
                msg("❌ Lỗi cập nhật!");
            }
        } catch (Exception e) {
            msg("❌ Lỗi: " + e.getMessage());
        }
    }

    private void thoat() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "🚪 Thoát chương trình?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Helper methods
    private void loadData() {
        model.setRowCount(0);
        try {
            List<HangHoa> list = dao.layDanhSachHangHoa();
            for (HangHoa hh : list) {
                addRow(hh);
            }
            msg("📋 Đã tải " + list.size() + " sản phẩm");
        } catch (Exception e) {
            msg("❌ Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void addRow(HangHoa hh) {
        Object[] row = {
                hh.getMaHang(),
                hh.getTenHang(),
                hh.getClass().getSimpleName(),
                hh.getSoLuongTon(),
                String.format("%,.0f", hh.getDonGia()),
                String.format("%.1f%%", hh.tinhVAT() * 100)
        };
        model.addRow(row);
    }

    private void selectItem() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maHang = (String) table.getValueAt(row, 0);
            txtMa.setText(maHang);
            txtTen.setText((String) table.getValueAt(row, 1));
            String loai = (String) table.getValueAt(row, 2);
            cboLoai.setSelectedItem(loai);
            txtSL.setText(table.getValueAt(row, 3).toString());
            String gia = table.getValueAt(row, 4).toString().replaceAll("[,.]", "");
            txtGia.setText(gia);

            // Load thông tin chi tiết từ database
            try {
                HangHoa hh = dao.timHangHoa(maHang);
                if (hh != null) {
                    // Clear tất cả field trước
                    clearDetailFields();

                    if (hh instanceof ThucPham) {
                        ThucPham tp = (ThucPham) hh;
                        txtNgaySX.setText(
                                tp.getNgaySanXuat().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        txtNgayHH.setText(
                                tp.getNgayHetHan().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        txtNhaCungCap.setText(tp.getNhaCungCap());
                    } else if (hh instanceof DienMay) {
                        DienMay dm = (DienMay) hh;
                        txtBaoHanh.setText(String.valueOf(dm.getThoiGianBaoHanh()));
                        txtCongSuat.setText(String.valueOf(dm.getCongSuat()));
                    } else if (hh instanceof SanhSu) {
                        SanhSu ss = (SanhSu) hh;
                        txtNhaSanXuat.setText(ss.getNhaSanXuat());
                        txtNgayNhapKho.setText(
                                ss.getNgayNhapKho().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi load chi tiết: " + e.getMessage());
            }

            msg("✅ Đã chọn: " + txtMa.getText());
        }
    }

    private void clearDetailFields() {
        // Clear các trường chi tiết
        txtNgaySX.setText("");
        txtNgayHH.setText("");
        txtNhaCungCap.setText("");
        txtBaoHanh.setText("");
        txtCongSuat.setText("");
        txtNhaSanXuat.setText("");
        txtNgayNhapKho.setText("");
    }

    private void msg(String text) {
        txtKQ.setText(text);
    }

    private void clear() {
        txtMa.setText("");
        txtTen.setText("");
        txtSL.setText("");
        txtGia.setText("");
        cboLoai.setSelectedIndex(0);

        // Clear các trường chi tiết
        clearDetailFields();

        updateFieldsVisibility();
    }

    private LocalDate parseDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            if (parts.length != 3)
                return null;
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("🖼️ Đang khởi động GUI Mode...");
        System.out.println("🔍 Kiểm tra kết nối database...");

        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = new KhoGUI();
                frame.setVisible(true);
                System.out.println("✅ GUI đã khởi động thành công!");
            } catch (Exception e) {
                System.err.println("❌ Lỗi khởi động GUI: " + e.getMessage());
                e.printStackTrace();

                // Hiển thị dialog lỗi
                JOptionPane.showMessageDialog(null,
                        "❌ Không thể khởi động GUI!\n\n" +
                                "Lỗi: " + e.getMessage() + "\n\n" +
                                "Vui lòng thử chạy Console Mode.",
                        "Lỗi GUI",
                        JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
        });
    }
}
