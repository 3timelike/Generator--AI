package com.zsy.util;

/**
 * 动态数据源上下文持有者，用于切换数据源
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}