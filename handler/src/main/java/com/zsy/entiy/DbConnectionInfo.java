package com.zsy.entiy;

/**
 * 数据库连接信息实体类
 */
public class DbConnectionInfo {
    private String url;
    private String username;
    private String password;
    private String dbType; // mysql/oracle/postgresql等

    // 构造方法
    public DbConnectionInfo() {
    }

    public DbConnectionInfo(String url, String username, String password, String dbType) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbType = dbType;
    }

    // Getter和Setter
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
