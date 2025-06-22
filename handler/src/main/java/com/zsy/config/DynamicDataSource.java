package com.zsy.config;

import com.zsy.util.DynamicDataSourceContextHolder;
import lombok.Getter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源路由
 */
@Getter
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources.putAll(targetDataSources);
        super.setTargetDataSources(this.targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    public synchronized void addDataSource(String key, DataSource dataSource) {
        this.targetDataSources.put(key, dataSource);
        super.setTargetDataSources(this.targetDataSources);
        super.afterPropertiesSet();
    }

    public synchronized void removeDataSource(String key) {
        this.targetDataSources.remove(key);
        super.setTargetDataSources(this.targetDataSources);
        super.afterPropertiesSet();
    }
    // 添加初始化方法
    public void init() {
        super.afterPropertiesSet();
    }

}