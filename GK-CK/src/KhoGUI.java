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
    
    public KhoGUI() {
        try {
            dao = new HangHoaDAO();
            System.out.println("✅ Kết nối database OK!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi DB: " + e.getMessage());
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
        cboLoai = new JComboBox<>(new String[]{"ThucPham", "DienMay", "SanhSu"});
        
        // Table
        String[] cols = {"Mã", "Tên", "Loại", "SL", "Giá", "VAT%"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectItem();
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
        topPanel.add(createBtn("📋 Xem", this::xemDS));
        topPanel.add(createBtn("➕ Thêm", this::them));
        topPanel.add(createBtn("🔍 Tìm", this::tim));
        topPanel.add(createBtn("⏰ Hết hạn", this::hetHan));
        topPanel.add(createBtn("📊 Thống kê", this::thongKe));
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
        
        gbc.gridx = 0; gbc.gridy = 0; leftPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 1; leftPanel.add(txtMa, gbc);
        gbc.gridx = 0; gbc.gridy = 1; leftPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1; leftPanel.add(txtTen, gbc);
        gbc.gridx = 0; gbc.gridy = 2; leftPanel.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1; leftPanel.add(cboLoai, gbc);
        gbc.gridx = 0; gbc.gridy = 3; leftPanel.add(new JLabel("SL:"), gbc);
        gbc.gridx = 1; leftPanel.add(txtSL, gbc);
        gbc.gridx = 0; gbc.gridy = 4; leftPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1; leftPanel.add(txtGia, gbc);
        
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
    }
    
    private JButton createBtn(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    // 8 Chức năng chính
    private void xemDS() { loadData(); }
    
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
                LocalDate ngaySX = LocalDate.now().minusDays(30);
                LocalDate ngayHH = LocalDate.now().plusDays(30);
                ThucPham tp = new ThucPham(ma, ten, sl, gia, ngaySX, ngayHH, "NCC1");
                ok = dao.themThucPham(tp);
            } else if ("DienMay".equals(loai)) {
                DienMay dm = new DienMay(ma, ten, sl, gia, 12, 100.0);
                ok = dao.themDienMay(dm);
            } else {
                LocalDate ngayNhap = LocalDate.now();
                SanhSu ss = new SanhSu(ma, ten, sl, gia, "NSX1", ngayNhap);
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
    
    private void tim() {
        String keyword = JOptionPane.showInputDialog("🔍 Nhập từ khóa:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                List<HangHoa> list = dao.layDanhSachHangHoa();
                model.setRowCount(0);
                int count = 0;
                for (HangHoa hh : list) {
                    if (hh.getMaHang().toLowerCase().contains(keyword.toLowerCase()) ||
                        hh.getTenHang().toLowerCase().contains(keyword.toLowerCase())) {
                        addRow(hh);
                        count++;
                    }
                }
                msg("🔍 Tìm thấy " + count + " kết quả cho: " + keyword);
            } catch (Exception e) {
                msg("❌ Lỗi tìm kiếm: " + e.getMessage());
            }
        }
    }
    
    private void hetHan() {
        try {
            List<ThucPham> list = dao.timSanPhamSapHetHan();
            model.setRowCount(0);
            if (list.isEmpty()) {
                msg("✅ Không có sản phẩm sắp hết hạn");
            } else {
                for (ThucPham tp : list) {
                    addRow(tp);
                }
                msg("⚠️ Có " + list.size() + " sản phẩm sắp hết hạn!");
            }
        } catch (Exception e) {
            msg("❌ Lỗi: " + e.getMessage());
        }
    }
    
    private void thongKe() {
        try {
            List<HangHoa> list = dao.layDanhSachHangHoa();
            int tp = 0, dm = 0, ss = 0;
            double tongGT = 0;
            
            for (HangHoa hh : list) {
                tongGT += hh.getSoLuongTon() * hh.getDonGia();
                if (hh instanceof ThucPham) tp++;
                else if (hh instanceof DienMay) dm++;
                else ss++;
            }
            
            String thongKe = String.format(
                "📊 THỐNG KÊ KHO\n" +
                "🍎 Thực phẩm: %d\n" +
                "⚡ Điện máy: %d\n" +
                "🏺 Sành sứ: %d\n" +
                "💰 Tổng giá trị: %,.0f VND\n" +
                "📦 Tổng cộng: %d mặt hàng",
                tp, dm, ss, tongGT, list.size()
            );
            msg(thongKe);
        } catch (Exception e) {
            msg("❌ Lỗi thống kê: " + e.getMessage());
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
            txtMa.setText((String) table.getValueAt(row, 0));
            txtTen.setText((String) table.getValueAt(row, 1));
            String loai = (String) table.getValueAt(row, 2);
            cboLoai.setSelectedItem(loai);
            txtSL.setText(table.getValueAt(row, 3).toString());
            String gia = table.getValueAt(row, 4).toString().replaceAll("[,.]", "");
            txtGia.setText(gia);
            msg("✅ Đã chọn: " + txtMa.getText());
        }
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
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KhoGUI().setVisible(true);
        });
    }
}
