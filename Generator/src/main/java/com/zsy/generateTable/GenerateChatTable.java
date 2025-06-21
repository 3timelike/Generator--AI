package com.zsy.generateTable;

import com.zsy.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateChatTable {
    public void ChatTable(DatabaseConfig dbConfig) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
             Statement stmt = conn.createStatement()) {

            String sql = """
            CREATE TABLE IF NOT EXISTS ai_chat_history (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                session_id VARCHAR(64) NOT NULL,
                user_input TEXT NOT NULL,
                ai_response TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_session (session_id)
            )
            """;

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("表创建失败: " + e.getMessage());
        }
    }

}
