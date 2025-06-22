package com.zsy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableTransactionManagement
public class DataBaseDruidConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        // 添加验证配置
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        return dataSource;
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(DataSource masterDataSource) {
        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();
        targetDataSources.put("master", masterDataSource);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);

        // 显式初始化
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}