package view;

import controller.HangHoaController;
import model.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GUI Main Window cho Warehouse Management System
 * View Layer trong MVC Pattern
 */
public class KhoGUI extends JFrame {
    private HangHoaController controller;
    
    // Components
    private JTabbedPane tabbedPane;
    private JTable bangHangHoa;
    private DefaultTableModel tableModel;
    
    // Form components cho thêm/sửa
    private JTextField txtMaHang, txtTenHang, txtSoLuong, txtDonGia;
    private JTextField txtNgaySanXuat, txtNgayHetHan, txtNhaCungCap;
    private JTextField txtThoiGianBaoHanh, txtCongSuat;
    private JTextField txtNhaSanXuat, txtNgayNhapKho;
    private JComboBox<String> cbLoaiHang;
    
    // Status components
    private JLabel lblStatus;
    private JLabel lblTongSoLuong, lblTongGiaTri;
    
    public KhoGUI() {
        this.controller = new HangHoaController();
        initializeGUI();
        loadData();
    }
    
    private void initializeGUI() {
        setTitle("🏪 Warehouse Management System - MVC Pattern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Set Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Use default if cannot set look and feel
        }
        
        createComponents();
        layoutComponents();
        attachEventListeners();
        
        setVisible(true);
    }
    
    private void createComponents() {
        // Main tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Status bar
        lblStatus = new JLabel("📊 Sẵn sàng");
        lblStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Statistics labels
        lblTongSoLuong = new JLabel("Tổng SP: 0");
        lblTongGiaTri = new JLabel("Tổng giá trị: 0 VNĐ");
        
        // Table for displaying data với cột VAT
        String[] columnNames = {"Mã hàng", "Tên hàng", "Loại", "SL tồn", "Đơn giá", "VAT(%)", "Giá có VAT", "Thông tin đặc biệt"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bangHangHoa = new JTable(tableModel);
        bangHangHoa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangHangHoa.getTableHeader().setReorderingAllowed(false);
        
        // Cải thiện độ rộng cột
        bangHangHoa.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã hàng
        bangHangHoa.getColumnModel().getColumn(1).setPreferredWidth(180); // Tên hàng
        bangHangHoa.getColumnModel().getColumn(2).setPreferredWidth(90);  // Loại
        bangHangHoa.getColumnModel().getColumn(3).setPreferredWidth(60);  // SL tồn
        bangHangHoa.getColumnModel().getColumn(4).setPreferredWidth(100); // Đơn giá
        bangHangHoa.getColumnModel().getColumn(5).setPreferredWidth(60);  // VAT(%)
        bangHangHoa.getColumnModel().getColumn(6).setPreferredWidth(120); // Giá có VAT
        bangHangHoa.getColumnModel().getColumn(7).setPreferredWidth(200); // Thông tin đặc biệt
        
        // Form components - tất cả cùng kích thước và style
        final int FIELD_COLUMNS = 20; // Kích thước chuẩn cho tất cả text field
        final Dimension FIELD_SIZE = new Dimension(280, 28); // Kích thước cố định lớn hơn
        final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 12); // Font đồng nhất
        
        txtMaHang = new JTextField(FIELD_COLUMNS);
        txtMaHang.setPreferredSize(FIELD_SIZE);
        txtMaHang.setFont(FIELD_FONT);
        txtTenHang = new JTextField(FIELD_COLUMNS);
        txtTenHang.setPreferredSize(FIELD_SIZE);
        txtTenHang.setFont(FIELD_FONT);
        txtSoLuong = new JTextField(FIELD_COLUMNS);
        txtSoLuong.setPreferredSize(FIELD_SIZE);
        txtSoLuong.setFont(FIELD_FONT);
        txtDonGia = new JTextField(FIELD_COLUMNS);
        txtDonGia.setPreferredSize(FIELD_SIZE);
        txtDonGia.setFont(FIELD_FONT);
        
        // Specific fields - cùng kích thước và style
        txtNgaySanXuat = new JTextField(FIELD_COLUMNS);
        txtNgaySanXuat.setPreferredSize(FIELD_SIZE);
        txtNgaySanXuat.setFont(FIELD_FONT);
        txtNgayHetHan = new JTextField(FIELD_COLUMNS);
        txtNgayHetHan.setPreferredSize(FIELD_SIZE);
        txtNgayHetHan.setFont(FIELD_FONT);
        txtNhaCungCap = new JTextField(FIELD_COLUMNS);
        txtNhaCungCap.setPreferredSize(FIELD_SIZE);
        txtNhaCungCap.setFont(FIELD_FONT);
        txtThoiGianBaoHanh = new JTextField(FIELD_COLUMNS);
        txtThoiGianBaoHanh.setPreferredSize(FIELD_SIZE);
        txtThoiGianBaoHanh.setFont(FIELD_FONT);
        txtCongSuat = new JTextField(FIELD_COLUMNS);
        txtCongSuat.setPreferredSize(FIELD_SIZE);
        txtCongSuat.setFont(FIELD_FONT);
        txtNhaSanXuat = new JTextField(FIELD_COLUMNS);
        txtNhaSanXuat.setPreferredSize(FIELD_SIZE);
        txtNhaSanXuat.setFont(FIELD_FONT);
        txtNgayNhapKho = new JTextField(FIELD_COLUMNS);
        txtNgayNhapKho.setPreferredSize(FIELD_SIZE);
        txtNgayNhapKho.setFont(FIELD_FONT);
        
        cbLoaiHang = new JComboBox<>(new String[]{"Thực phẩm", "Điện máy", "Sành sứ"});
        cbLoaiHang.setPreferredSize(FIELD_SIZE);
        cbLoaiHang.setFont(FIELD_FONT);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Main tab - Danh sách hàng hóa
        JPanel mainPanel = createMainPanel();
        tabbedPane.addTab("📦 Danh sách hàng hóa", mainPanel);
        
        // Add tab - Thêm hàng hóa
        JPanel addPanel = createAddPanel();
        tabbedPane.addTab("➕ Thêm hàng hóa", addPanel);
        
        // Statistics tab
        JPanel statsPanel = createStatsPanel();
        tabbedPane.addTab("📊 Thống kê", statsPanel);
        
        // Search tab
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("🔍 Tìm kiếm", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(lblStatus, BorderLayout.WEST);
        
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.add(lblTongSoLuong);
        infoPanel.add(new JLabel(" | "));
        infoPanel.add(lblTongGiaTri);
        statusPanel.add(infoPanel, BorderLayout.EAST);
        
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table with scroll pane
        JScrollPane scrollPane = new JScrollPane(bangHangHoa);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnRefresh = new JButton("🔄 Làm mới");
        JButton btnEdit = new JButton("✏️ Sửa");
        JButton btnDelete = new JButton("🗑️ Xóa");
        JButton btnViewDetail = new JButton("👁️ Xem chi tiết");
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewDetail);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Event listeners for buttons
        btnRefresh.addActionListener(e -> {
            loadData();
            updateStatus("✅ Đã làm mới dữ liệu");
        });
        
        btnEdit.addActionListener(e -> editSelectedItem());
        btnDelete.addActionListener(e -> deleteSelectedItem());
        btnViewDetail.addActionListener(e -> viewDetailSelectedItem());
        
        return panel;
    }
    
    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding đều
        gbc.anchor = GridBagConstraints.WEST; // Căn trái
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal để đều nhau
        
        // Common fields với kích thước đều nhau
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; // Label column
        formPanel.add(new JLabel("Loại hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; // Field column
        formPanel.add(cbLoaiHang, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mã hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtMaHang, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Tên hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtTenHang, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Số lượng tồn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtSoLuong, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(txtDonGia, gbc);
        
        // Dynamic fields panel
        JPanel dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.setBorder(new TitledBorder("Thông tin đặc biệt"));
        
        // Thực phẩm panel
        JPanel thucPhamPanel = createThucPhamPanel();
        dynamicPanel.add(thucPhamPanel, "Thực phẩm");
        
        // Điện máy panel
        JPanel dienMayPanel = createDienMayPanel();
        dynamicPanel.add(dienMayPanel, "Điện máy");
        
        // Sành sứ panel
        JPanel sanhSuPanel = createSanhSuPanel();
        dynamicPanel.add(sanhSuPanel, "Sành sứ");
        
        // Listen to combo box changes
        cbLoaiHang.addActionListener(e -> {
            CardLayout cl = (CardLayout) dynamicPanel.getLayout();
            cl.show(dynamicPanel, (String) cbLoaiHang.getSelectedItem());
        });
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dynamicPanel, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("➕ Thêm");
        JButton btnClear = new JButton("🧹 Xóa form");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Event listeners
        btnAdd.addActionListener(e -> addNewItem());
        btnClear.addActionListener(e -> clearForm());
        
        return panel;
    }
    
    private JPanel createThucPhamPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panel.add(new JLabel("Ngày sản xuất (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtNgaySanXuat, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        panel.add(new JLabel("Ngày hết hạn (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtNgayHetHan, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        panel.add(new JLabel("Nhà cung cấp:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtNhaCungCap, gbc);
        
        return panel;
    }
    
    private JPanel createDienMayPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panel.add(new JLabel("Thời gian bảo hành (tháng):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtThoiGianBaoHanh, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        panel.add(new JLabel("Công suất (KW):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtCongSuat, gbc);
        
        return panel;
    }
    
    private JPanel createSanhSuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panel.add(new JLabel("Nhà sản xuất:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtNhaSanXuat, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        panel.add(new JLabel("Ngày nhập kho (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(txtNgayNhapKho, gbc);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea txtStats = new JTextArea(20, 50);
        txtStats.setEditable(false);
        txtStats.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtStats);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnUpdateStats = new JButton("📊 Cập nhật thống kê");
        btnUpdateStats.addActionListener(e -> {
            updateStatistics(txtStats);
            updateStatus("✅ Đã cập nhật thống kê");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnUpdateStats);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Search form
        JPanel searchForm = new JPanel(new FlowLayout());
        searchForm.add(new JLabel("Mã hàng:"));
        JTextField txtSearch = new JTextField(15);
        searchForm.add(txtSearch);
        
        JButton btnSearch = new JButton("🔍 Tìm kiếm");
        JButton btnSearchExpiring = new JButton("⚠️ Sản phẩm sắp hết hạn");
        
        searchForm.add(btnSearch);
        searchForm.add(btnSearchExpiring);
        
        panel.add(searchForm, BorderLayout.NORTH);
        
        // Results area
        JTextArea txtResults = new JTextArea(20, 50);
        txtResults.setEditable(false);
        txtResults.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtResults);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Event listeners
        btnSearch.addActionListener(e -> {
            String maHang = txtSearch.getText().trim();
            if (maHang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            HangHoa hangHoa = controller.timHangHoa(maHang);
            if (hangHoa != null) {
                txtResults.setText(formatHangHoaDetail(hangHoa));
                updateStatus("✅ Tìm thấy hàng hóa: " + maHang);
            } else {
                txtResults.setText("❌ Không tìm thấy hàng hóa với mã: " + maHang);
                updateStatus("❌ Không tìm thấy: " + maHang);
            }
        });
        
        btnSearchExpiring.addActionListener(e -> {
            List<ThucPham> expiring = controller.timSanPhamSapHetHan();
            StringBuilder sb = new StringBuilder();
            sb.append("⚠️ SẢN PHẨM SẮP HẾT HẠN TRONG TUẦN:\n");
            sb.append("=====================================\n\n");
            
            if (expiring.isEmpty()) {
                sb.append("✅ Không có sản phẩm nào sắp hết hạn!");
            } else {
                for (ThucPham tp : expiring) {
                    sb.append(formatHangHoaDetail(tp)).append("\n");
                    sb.append("-----------------------------------\n");
                }
            }
            
            txtResults.setText(sb.toString());
            updateStatus("⚠️ Tìm thấy " + expiring.size() + " sản phẩm sắp hết hạn");
        });
        
        return panel;
    }
    
    private void attachEventListeners() {
        // Window closing event
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(
                    KhoGUI.this,
                    "Bạn có chắc muốn thoát chương trình?",
                    "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
    
    // ================= EVENT HANDLERS =================
    
    private void addNewItem() {
        try {
            String loaiHang = (String) cbLoaiHang.getSelectedItem();
            String maHang = txtMaHang.getText().trim();
            String tenHang = txtTenHang.getText().trim();
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            double donGia = Double.parseDouble(txtDonGia.getText().trim());
            
            boolean success = false;
            
            switch (loaiHang) {
                case "Thực phẩm":
                    success = controller.themThucPham(maHang, tenHang, soLuong, donGia,
                        txtNgaySanXuat.getText().trim(),
                        txtNgayHetHan.getText().trim(),
                        txtNhaCungCap.getText().trim());
                    break;
                    
                case "Điện máy":
                    success = controller.themDienMay(maHang, tenHang, soLuong, donGia,
                        Integer.parseInt(txtThoiGianBaoHanh.getText().trim()),
                        Double.parseDouble(txtCongSuat.getText().trim()));
                    break;
                    
                case "Sành sứ":
                    success = controller.themSanhSu(maHang, tenHang, soLuong, donGia,
                        txtNhaSanXuat.getText().trim(),
                        txtNgayNhapKho.getText().trim());
                    break;
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Thêm hàng hóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadData();
                updateStatus("✅ Đã thêm: " + maHang);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm hàng hóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtMaHang.setText("");
        txtTenHang.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtNgaySanXuat.setText("");
        txtNgayHetHan.setText("");
        txtNhaCungCap.setText("");
        txtThoiGianBaoHanh.setText("");
        txtCongSuat.setText("");
        txtNhaSanXuat.setText("");
        txtNgayNhapKho.setText("");
        cbLoaiHang.setSelectedIndex(0);
    }
    
    private void editSelectedItem() {
        int selectedRow = bangHangHoa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng chọn một hàng hóa để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maHang = (String) tableModel.getValueAt(selectedRow, 0);
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa != null) {
            // Open edit dialog
            EditDialog editDialog = new EditDialog(this, hangHoa, controller);
            editDialog.setVisible(true);
            
            if (editDialog.isUpdated()) {
                loadData();
                updateStatus("✅ Đã cập nhật: " + maHang);
            }
        }
    }
    
    private void deleteSelectedItem() {
        int selectedRow = bangHangHoa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng chọn một hàng hóa để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maHang = (String) tableModel.getValueAt(selectedRow, 0);
        String tenHang = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa hàng hóa:\n" + maHang + " - " + tenHang + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            if (controller.xoaHangHoa(maHang)) {
                JOptionPane.showMessageDialog(this, "✅ Xóa hàng hóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                updateStatus("✅ Đã xóa: " + maHang);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Xóa hàng hóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewDetailSelectedItem() {
        int selectedRow = bangHangHoa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng chọn một hàng hóa để xem chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maHang = (String) tableModel.getValueAt(selectedRow, 0);
        HangHoa hangHoa = controller.timHangHoa(maHang);
        
        if (hangHoa != null) {
            String detail = formatHangHoaDetail(hangHoa);
            JOptionPane.showMessageDialog(this, detail, "Chi tiết hàng hóa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ================= UTILITY METHODS =================
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<HangHoa> danhSach = controller.layDanhSachHangHoa();
        
        for (HangHoa hh : danhSach) {
            String loai = "";
            String thongTinDacBiet = "";
            
            if (hh instanceof ThucPham) {
                ThucPham tp = (ThucPham) hh;
                loai = "Thực phẩm";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                thongTinDacBiet = "HSD: " + tp.getNgayHetHan().format(formatter);
            } else if (hh instanceof DienMay) {
                DienMay dm = (DienMay) hh;
                loai = "Điện máy";
                thongTinDacBiet = "BH: " + dm.getThoiGianBaoHanh() + " tháng, " + dm.getCongSuat() + "KW";
            } else if (hh instanceof SanhSu) {
                SanhSu ss = (SanhSu) hh;
                loai = "Sành sứ";
                thongTinDacBiet = "NSX: " + ss.getNhaSanXuat();
            }
            
            Object[] row = {
                hh.getMaHang(),
                hh.getTenHang(),
                loai,
                hh.getSoLuongTon(),
                String.format("%,.0f VNĐ", hh.getDonGia()),
                String.format("%.1f%%", hh.getVATRate()),
                String.format("%,.0f VNĐ", hh.getGiaCoVAT()),
                thongTinDacBiet
            };
            
            tableModel.addRow(row);
        }
        
        updateStatisticsBar();
    }
    
    private void updateStatisticsBar() {
        List<HangHoa> danhSach = controller.layDanhSachHangHoa();
        int tongSoLuong = danhSach.size();
        double tongGiaTriCoVAT = controller.tinhTongGiaTriCoVAT();
        
        lblTongSoLuong.setText("Tổng SP: " + tongSoLuong);
        lblTongGiaTri.setText(String.format("Tổng giá trị có VAT: %,.0f VNĐ", tongGiaTriCoVAT));
    }
    
    private void updateStatistics(JTextArea txtStats) {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 THỐNG KÊ KHO HÀNG\n");
        sb.append("==========================================\n\n");
        
        List<HangHoa> danhSach = controller.layDanhSachHangHoa();
        
        int soThucPham = controller.getSoLuongTheoLoai("thucpham");
        int soDienMay = controller.getSoLuongTheoLoai("dienmay");
        int soSanhSu = controller.getSoLuongTheoLoai("sanhsu");
        
        sb.append("🍎 Thực phẩm: ").append(soThucPham).append(" sản phẩm\n");
        sb.append("⚡ Điện máy: ").append(soDienMay).append(" sản phẩm\n");
        sb.append("🏺 Sành sứ: ").append(soSanhSu).append(" sản phẩm\n");
        sb.append("📦 Tổng cộng: ").append(danhSach.size()).append(" sản phẩm\n\n");
        
        double tongGiaTri = controller.tinhTongGiaTriKho();
        double tongGiaTriCoVAT = controller.tinhTongGiaTriCoVAT();
        sb.append(String.format("💰 Tổng giá trị kho: %,.0f VNĐ\n", tongGiaTri));
        sb.append(String.format("💰 Tổng giá trị có VAT: %,.0f VNĐ\n", tongGiaTriCoVAT));
        
        double trungBinhDienMay = controller.tinhTrungBinhSoLuongDienMay();
        sb.append(String.format("📊 TB số lượng điện máy: %.2f\n\n", trungBinhDienMay));
        
        // Sản phẩm sắp hết hạn
        List<ThucPham> sapHetHan = controller.timSanPhamSapHetHan();
        sb.append("⚠️ Sản phẩm sắp hết hạn: ").append(sapHetHan.size()).append(" sản phẩm\n");
        
        if (!sapHetHan.isEmpty()) {
            sb.append("Chi tiết:\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (ThucPham tp : sapHetHan) {
                sb.append("   - ").append(tp.getMaHang()).append(" (").append(tp.getTenHang())
                  .append(") - HSD: ").append(tp.getNgayHetHan().format(formatter)).append("\n");
            }
        }
        
        txtStats.setText(sb.toString());
    }
    
    private String formatHangHoaDetail(HangHoa hangHoa) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        sb.append("🏷️ CHI TIẾT HÀNG HÓA\n");
        sb.append("================================\n");
        sb.append("Mã hàng: ").append(hangHoa.getMaHang()).append("\n");
        sb.append("Tên hàng: ").append(hangHoa.getTenHang()).append("\n");
        sb.append("Số lượng tồn: ").append(hangHoa.getSoLuongTon()).append("\n");
        sb.append(String.format("Đơn giá: %,.0f VNĐ\n", hangHoa.getDonGia()));
        sb.append(String.format("VAT: %.1f%%\n", hangHoa.getVATRate()));
        sb.append(String.format("Giá có VAT: %,.0f VNĐ\n", hangHoa.getGiaCoVAT()));
        sb.append("Hàng khó bán: ").append(hangHoa.daKho() ? "Có" : "Không").append("\n\n");
        
        if (hangHoa instanceof ThucPham) {
            ThucPham tp = (ThucPham) hangHoa;
            sb.append("🍎 THÔNG TIN THỰC PHẨM:\n");
            sb.append("Ngày sản xuất: ").append(tp.getNgaySanXuat().format(formatter)).append("\n");
            sb.append("Ngày hết hạn: ").append(tp.getNgayHetHan().format(formatter)).append("\n");
            sb.append("Nhà cung cấp: ").append(tp.getNhaCungCap()).append("\n");
            
        } else if (hangHoa instanceof DienMay) {
            DienMay dm = (DienMay) hangHoa;
            sb.append("⚡ THÔNG TIN ĐIỆN MÁY:\n");
            sb.append("Thời gian bảo hành: ").append(dm.getThoiGianBaoHanh()).append(" tháng\n");
            sb.append("Công suất: ").append(dm.getCongSuat()).append(" KW\n");
            
        } else if (hangHoa instanceof SanhSu) {
            SanhSu ss = (SanhSu) hangHoa;
            sb.append("🏺 THÔNG TIN SÀNH SỨ:\n");
            sb.append("Nhà sản xuất: ").append(ss.getNhaSanXuat()).append("\n");
            sb.append("Ngày nhập kho: ").append(ss.getNgayNhapKho().format(formatter)).append("\n");
        }
        
        return sb.toString();
    }
    
    private void updateStatus(String message) {
        lblStatus.setText(message);
        
        // Clear status after 3 seconds
        Timer timer = new Timer(3000, e -> lblStatus.setText("📊 Sẵn sàng"));
        timer.setRepeats(false);
        timer.start();
    }
    
    // ================= MAIN METHOD =================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new KhoGUI();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "❌ Lỗi khởi tạo ứng dụng: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
