package com.zsy.service;

import com.zsy.entiy.ColumnInfo;
import com.zsy.entiy.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetaDataService {

    @Autowired
    private DataSource dataSource;

    public List<TableInfo> getTables() throws SQLException {
        List<TableInfo> tables = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            // 获取当前数据库名称
            String catalog = conn.getCatalog();

            // 获取表信息
            ResultSet rs = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                TableInfo table = new TableInfo();
                table.setTableName(rs.getString("TABLE_NAME"));
                table.setRemarks(rs.getString("REMARKS"));
                tables.add(table);
            }

            // 获取每个表的列信息
            for (TableInfo table : tables) {
                ResultSet columns = metaData.getColumns(catalog, null, table.getTableName(), "%");
                List<ColumnInfo> columnList = new ArrayList<>();
                while (columns.next()) {
                    ColumnInfo column = new ColumnInfo();
                    column.setColumnName(columns.getString("COLUMN_NAME"));
                    column.setDataType(columns.getString("TYPE_NAME"));
                    column.setColumnSize(columns.getInt("COLUMN_SIZE"));
                    column.setNullable(columns.getInt("NULLABLE") == 1);
                    columnList.add(column);
                }
                table.setColumns(columnList);
            }
        }

        return tables;
    }

    /**
     * 获取指定表的结构信息
     */
    public TableInfo getTableStructure(String tableName) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            // 获取表基本信息
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);

            // 获取列信息
            List<ColumnInfo> columns = new ArrayList<>();
            try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, null)) {
                while (columnsRs.next()) {
                    ColumnInfo column = new ColumnInfo();
                    column.setColumnName(columnsRs.getString("COLUMN_NAME"));
                    column.setDataType(columnsRs.getString("TYPE_NAME"));
                    column.setColumnSize(columnsRs.getInt("COLUMN_SIZE"));
                    column.setNullable(columnsRs.getInt("NULLABLE") == 1);
                    //column.setRemarks(columnsRs.getString("REMARKS"));
                    columns.add(column);
                }
            }
            tableInfo.setColumns(columns);

            // 获取主键信息
//            try (ResultSet pkRs = metaData.getPrimaryKeys(null, null, tableName)) {
//                if (pkRs.next()) {
//                    tableInfo.setPrimaryKey(pkRs.getString("COLUMN_NAME"));
//                }
//            }

            return tableInfo;
        }
    }

    /**
     * 获取数据库中所有表名
     */
    public List<String> getAllTableNames() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
        }
        return tables;
    }
}
