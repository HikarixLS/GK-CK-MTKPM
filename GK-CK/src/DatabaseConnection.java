import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Lớp quản lý kết nối MySQL Database
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;
    
    private DatabaseConnection() {
        loadProperties();
        connect();
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        // Try parent directory first (where .bat files are)
        try (FileInputStream fis = new FileInputStream("../database.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            // Fallback: try current directory
            try (FileInputStream fis = new FileInputStream("database.properties")) {
                properties.load(fis);
            } catch (IOException e2) {
                System.err.println("Không thể load file database.properties: " + e2.getMessage());
                // Sử dụng cấu hình mặc định
                setDefaultProperties();
            }
        }
    }
    
    private void setDefaultProperties() {
        properties.setProperty("db.server", "localhost");
        properties.setProperty("db.port", "3306");
        properties.setProperty("db.database", "QuanLyKhoHang");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }
    
    private void connect() {
        try {
            String driver = properties.getProperty("db.driver");
            Class.forName(driver);
            
            String server = properties.getProperty("db.server", "localhost");
            String port = properties.getProperty("db.port", "3306");
            String database = properties.getProperty("db.database", "QuanLyKhoHang");
            String username = properties.getProperty("db.username", "root");
            String password = properties.getProperty("db.password", "");
            
            // Trước tiên kết nối không có database để tạo database
            createDatabaseIfNotExists(server, port, database, username, password);
            
            String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8",
                server, port, database
            );
            
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối MySQL thành công!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy MySQL JDBC Driver: " + e.getMessage());
            System.err.println("Hãy download MySQL Connector/J từ:");
            System.err.println("https://dev.mysql.com/downloads/connector/j/");
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối MySQL: " + e.getMessage());
            System.err.println("Kiểm tra lại:");
            System.err.println("1. MySQL Server đang chạy");
            System.err.println("2. Thông tin trong database.properties");
            System.err.println("3. Username/password đúng");
            System.err.println("4. Database QuanLyKhoHang đã được tạo");
        }
    }
    
    // Tạo database nếu chưa tồn tại
    private void createDatabaseIfNotExists(String server, String port, String database, String username, String password) {
        try {
            // Kết nối đến MySQL server (không chỉ định database)
            String url = String.format(
                "jdbc:mysql://%s:%s/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8",
                server, port
            );
            
            Connection tempConn = DriverManager.getConnection(url, username, password);
            Statement stmt = tempConn.createStatement();
            
            // Kiểm tra database có tồn tại không
            String checkDB = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + database + "'";
            ResultSet rs = stmt.executeQuery(checkDB);
            
            if (!rs.next()) {
                // Tạo database mới
                String createDB = "CREATE DATABASE " + database + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                stmt.executeUpdate(createDB);
                System.out.println("Đã tạo database: " + database);
                
                // Tạo các bảng cơ bản
                createTables(database, username, password, server, port);
            } else {
                System.out.println("Database đã tồn tại: " + database);
            }
            
            rs.close();
            stmt.close();
            tempConn.close();
            
        } catch (SQLException e) {
            System.err.println("Lỗi tạo database: " + e.getMessage());
        }
    }
    
    // Tạo các bảng cơ bản
    private void createTables(String database, String username, String password, String server, String port) {
        try {
            String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8",
                server, port, database
            );
            
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            
            // Tạo bảng HangHoa
            String createHangHoa = """
                CREATE TABLE IF NOT EXISTS HangHoa (
                    MaHang VARCHAR(20) PRIMARY KEY,
                    TenHang VARCHAR(100) NOT NULL,
                    LoaiHang ENUM('ThucPham', 'DienMay', 'SanhSu') NOT NULL,
                    SoLuongTon INT NOT NULL DEFAULT 0,
                    DonGia DECIMAL(18,2) NOT NULL,
                    VAT DECIMAL(5,4) NOT NULL,
                    NgayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    NgayCapNhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
                """;
            stmt.execute(createHangHoa);
            
            // Tạo bảng ThucPham
            String createThucPham = """
                CREATE TABLE IF NOT EXISTS ThucPham (
                    MaHang VARCHAR(20) PRIMARY KEY,
                    NgaySanXuat DATE NOT NULL,
                    NgayHetHan DATE NOT NULL,
                    NhaCungCap VARCHAR(100) NOT NULL,
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                )
                """;
            stmt.execute(createThucPham);
            
            // Tạo bảng DienMay
            String createDienMay = """
                CREATE TABLE IF NOT EXISTS DienMay (
                    MaHang VARCHAR(20) PRIMARY KEY,
                    ThoiGianBaoHanh INT NOT NULL COMMENT 'số tháng',
                    CongSuat DECIMAL(10,2) NOT NULL COMMENT 'đơn vị: W',
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                )
                """;
            stmt.execute(createDienMay);
            
            // Tạo bảng SanhSu
            String createSanhSu = """
                CREATE TABLE IF NOT EXISTS SanhSu (
                    MaHang VARCHAR(20) PRIMARY KEY,
                    NhaSanXuat VARCHAR(100) NOT NULL,
                    NgayNhapKho DATE NOT NULL,
                    FOREIGN KEY (MaHang) REFERENCES HangHoa(MaHang) ON DELETE CASCADE
                )
                """;
            stmt.execute(createSanhSu);
            
            // Thêm dữ liệu mẫu
            stmt.execute("INSERT IGNORE INTO HangHoa VALUES ('TP001', 'Bánh mì kẹp thịt', 'ThucPham', 50, 25000, 0.05, NOW(), NOW())");
            stmt.execute("INSERT IGNORE INTO ThucPham VALUES ('TP001', '2025-07-25', '2025-07-27', 'Tiệm bánh ABC')");
            
            stmt.execute("INSERT IGNORE INTO HangHoa VALUES ('DM001', 'Tủ lạnh Samsung RT35', 'DienMay', 10, 8500000, 0.10, NOW(), NOW())");
            stmt.execute("INSERT IGNORE INTO DienMay VALUES ('DM001', 24, 150.5)");
            
            stmt.execute("INSERT IGNORE INTO HangHoa VALUES ('SS001', 'Bộ chén đĩa Minh Long', 'SanhSu', 25, 450000, 0.08, NOW(), NOW())");
            stmt.execute("INSERT IGNORE INTO SanhSu VALUES ('SS001', 'Minh Long Co.', '2025-07-01')");
            
            stmt.close();
            conn.close();
            System.out.println("Đã tạo bảng và dữ liệu mẫu!");
            
        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra kết nối: " + e.getMessage());
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối MySQL");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
    
    // Test kết nối
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                // Thử thực hiện một query đơn giản
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                boolean hasResult = rs.next();
                rs.close();
                stmt.close();
                return hasResult;
            }
        } catch (SQLException e) {
            System.err.println("Test kết nối thất bại: " + e.getMessage());
        }
        return false;
    }
}
