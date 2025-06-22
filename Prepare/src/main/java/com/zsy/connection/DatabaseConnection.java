package com.zsy.connection;

import com.zsy.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection database(DatabaseConfig databaseConfig) {
        // 构造完整URL，包含数据库名和自动创建参数
        String url = "jdbc:mysql://" + databaseConfig.getUrl() + ":" +
                databaseConfig.getPort() + "/" + databaseConfig.getDatabaseName() +
                "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC";

        String user = databaseConfig.getUsername();
        String password = databaseConfig.getPassword();

        try {
            // 1. 显式加载驱动(可选，JDBC 4.0+会自动加载)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 测试连接
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                System.out.println("数据库连接成功！");

                // 3. 检查数据库是否已创建
                if (conn.getCatalog() == null || conn.getCatalog().isEmpty()) {
                    System.out.println("连接成功，但未选择数据库");
                }

                return conn;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL驱动未找到，请检查依赖：" + e.getMessage());
            throw new RuntimeException("驱动加载失败", e);
        } catch (SQLException e) {
            System.err.println("数据库连接失败，详细错误：");
            e.printStackTrace();

            // 提供更友好的错误信息
            if (e.getMessage().contains("Access denied")) {
                System.err.println("认证失败，请检查用户名/密码");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("数据库不存在且自动创建失败，请手动创建数据库");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("网络连接问题，请检查：");
                System.err.println("1. MySQL服务是否运行");
                System.err.println("2. 防火墙设置");
                System.err.println("3. 连接地址和端口是否正确");
            }

            throw new RuntimeException("数据库连接失败", e);
        }
    }
}