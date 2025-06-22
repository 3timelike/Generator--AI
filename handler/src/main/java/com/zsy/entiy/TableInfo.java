package com.zsy.entiy;

import java.util.List;

/**
 * 表信息实体
 */
public class TableInfo {
    private String tableName;
    private String remarks;
    private List<ColumnInfo> columns;

    // Getter和Setter
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }
}

