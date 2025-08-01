package model;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Lớp quản lý kết nối MySQL Database
 * Database connection trong MVC Pattern
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;
    
    // Database configuration
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/QuanLyKhoHang";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";
    
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
        // Try to load from various locations
        String[] possiblePaths = {
            "database.properties",
            "../database.properties",
            "config/database.properties"
        };
        
        boolean loaded = false;
        for (String path : possiblePaths) {
            try (FileInputStream fis = new FileInputStream(path)) {
                properties.load(fis);
                System.out.println("✅ Loaded database.properties from: " + path);
                loaded = true;
                break;
            } catch (IOException e) {
                // Try next path
            }
        }
        
        if (!loaded) {
            System.out.println("⚠️ Không thể load file database.properties, sử dụng config mặc định");
            // Set default properties
            properties.setProperty("db.url", DEFAULT_URL);
            properties.setProperty("db.username", DEFAULT_USERNAME);
            properties.setProperty("db.password", DEFAULT_PASSWORD);
        }
    }
    
    private void connect() {
        try {
            String url = properties.getProperty("db.url", DEFAULT_URL);
            String username = properties.getProperty("db.username", DEFAULT_USERNAME);
            String password = properties.getProperty("db.password", DEFAULT_PASSWORD);
            
            System.out.println("🔄 Đang kết nối đến: " + url);
            
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Kết nối MySQL thành công!");
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kiểm tra connection: " + e.getMessage());
            connect(); // Try to reconnect
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔄 Đã đóng kết nối database");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi đóng connection: " + e.getMessage());
        }
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                // Test with a simple query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Test connection failed: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Create database if not exists
     */
    public void createDatabaseIfNotExists() {
        try {
            String baseUrl = properties.getProperty("db.url", DEFAULT_URL);
            String dbName = baseUrl.substring(baseUrl.lastIndexOf("/") + 1);
            String serverUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
            
            Connection conn = DriverManager.getConnection(
                serverUrl, 
                properties.getProperty("db.username", DEFAULT_USERNAME),
                properties.getProperty("db.password", DEFAULT_PASSWORD)
            );
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName + 
                             " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("✅ Database '" + dbName + "' đã sẵn sàng");
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tạo database: " + e.getMessage());
        }
    }
}
