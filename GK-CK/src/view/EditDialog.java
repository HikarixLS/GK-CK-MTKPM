package view;

import controller.HangHoaController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog để chỉnh sửa thông tin hàng hóa
 */
public class EditDialog extends JDialog {
    private HangHoaController controller;
    private HangHoa hangHoa;
    private boolean updated = false;
    
    private JTextField txtTenHang;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    
    public EditDialog(JFrame parent, HangHoa hangHoa, HangHoaController controller) {
        super(parent, "✏️ Chỉnh sửa hàng hóa", true);
        this.hangHoa = hangHoa;
        this.controller = controller;
        
        initComponents();
        loadData();
        
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mã hàng (read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã hàng:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaHang = new JTextField(hangHoa.getMaHang(), 15);
        txtMaHang.setEditable(false);
        txtMaHang.setBackground(Color.LIGHT_GRAY);
        formPanel.add(txtMaHang, gbc);
        
        // Tên hàng
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên hàng:"), gbc);
        gbc.gridx = 1;
        txtTenHang = new JTextField(15);
        formPanel.add(txtTenHang, gbc);
        
        // Số lượng tồn
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số lượng tồn:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField(15);
        formPanel.add(txtSoLuong, gbc);
        
        // Đơn giá
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1;
        txtDonGia = new JTextField(15);
        formPanel.add(txtDonGia, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("💾 Lưu");
        JButton btnCancel = new JButton("❌ Hủy");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event listeners
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadData() {
        txtTenHang.setText(hangHoa.getTenHang());
        txtSoLuong.setText(String.valueOf(hangHoa.getSoLuongTon()));
        txtDonGia.setText(String.valueOf(hangHoa.getDonGia()));
    }
    
    private void saveChanges() {
        try {
            String tenHang = txtTenHang.getText().trim();
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            double donGia = Double.parseDouble(txtDonGia.getText().trim());
            
            if (controller.capNhatHangHoa(hangHoa.getMaHang(), tenHang, soLuong, donGia)) {
                updated = true;
                JOptionPane.showMessageDialog(this, 
                    "✅ Cập nhật thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Cập nhật thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Vui lòng nhập đúng định dạng số!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isUpdated() {
        return updated;
    }
}
