package com.zsy.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.zsy.config.DynamicDataSource;
import com.zsy.entiy.DbConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataSourceService {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    /**
     * 创建Druid数据源
     */
    public DruidDataSource createDruidDataSource(DbConnectionInfo info) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(info.getUrl());
        dataSource.setUsername(info.getUsername());
        dataSource.setPassword(info.getPassword());

        // 连接池配置
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        try {
            dataSource.init();
            return dataSource;
        } catch (SQLException e) {
            throw new RuntimeException("初始化Druid数据源失败", e);
        }
    }

    /**
     * 添加数据源到动态路由
     */
    public synchronized void addDataSource(String key, DbConnectionInfo info) throws SQLException {
        if (dynamicDataSource.getTargetDataSources().containsKey(key)) {
            return;
        }

        DruidDataSource dataSource = createDruidDataSource(info);
        dynamicDataSource.addDataSource(key, dataSource);
    }

    /**
     * 测试连接是否有效
     */
    public boolean testConnection(DbConnectionInfo info) {
        try (Connection conn = createDruidDataSource(info).getConnection()) {
            return conn.isValid(5);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前所有数据源
     */
    public Map<String, DataSource> getAllDataSources() {
        Map<String, DataSource> result = new HashMap<>();
        dynamicDataSource.getTargetDataSources().forEach((k, v) -> {
            result.put(k.toString(), (DataSource) v);
        });
        return result;
    }
}