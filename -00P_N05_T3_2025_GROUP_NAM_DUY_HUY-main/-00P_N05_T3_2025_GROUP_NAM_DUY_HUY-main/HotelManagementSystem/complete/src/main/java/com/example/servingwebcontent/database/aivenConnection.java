package com.example.servingwebcontent.database;

import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Giữ nguyên cách gọi aivenConnection.getConnection(),
 * nhưng bên trong lấy Connection từ DataSource (HikariCP) của Spring.
 * KHÔNG tự đọc application.properties, KHÔNG dùng DriverManager.
 */
@Component
public class aivenConnection {

    private static DataSource dataSource; // được Spring tiêm qua constructor

    public aivenConnection(DataSource ds) {
        aivenConnection.dataSource = ds;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource chưa được khởi tạo bởi Spring");
        }
        // Connection trả về từ pool (HikariCP). close() sẽ TRẢ LẠI về pool, không đóng socket thật.
        return dataSource.getConnection();
    }
}
