package view;

import javax.swing.SwingUtilities;

/**
 * Main Application Entry Point
 * MVC Pattern Implementation
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🏪 WAREHOUSE MANAGEMENT SYSTEM");
        System.out.println("==============================");
        System.out.println("📋 MVC Pattern Implementation");
        System.out.println("==============================\n");
        
        // Check if running in console mode
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            runConsoleMode();
        } else {
            // GUI Mode - chạy thẳng GUI luôn (không cần chọn)
            runGUIMode();
        }
    }
    
    private static void runGUIMode() {
        System.out.println("🖼️ Khởi động GUI Mode...");
        System.out.println("📱 Đang mở cửa sổ ứng dụng...\n");
        
        SwingUtilities.invokeLater(() -> {
            try {
                new KhoGUI();
                System.out.println("✅ GUI đã khởi động thành công!");
            } catch (Exception e) {
                System.err.println("❌ Lỗi khởi động GUI: " + e.getMessage());
                e.printStackTrace();
                
                System.out.println("\n🔄 Chuyển sang Console Mode...");
                runConsoleMode();
            }
        });
    }
    
    private static void runConsoleMode() {
        System.out.println("🖥️ Khởi động Console Mode...");
        System.out.println("⌨️ Giao diện dòng lệnh\n");
        
        try {
            ConsoleView console = new ConsoleView();
            console.start();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khởi động Console: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
