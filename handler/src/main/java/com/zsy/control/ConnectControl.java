package com.zsy.control;

import com.zsy.config.DatabaseConfig;
import com.zsy.config.DynamicDataSource;
import com.zsy.entiy.DbConnectionInfo;
import com.zsy.entiy.TableInfo;
import com.zsy.generateTable.GenerateChatTableAIConfig;
import com.zsy.service.DataSourceService;
import com.zsy.service.MetaDataService;
import com.zsy.util.DynamicDataSourceContextHolder;
import com.zsy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/connection")
public class ConnectControl {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private DynamicDataSource dynamicDataSource;

    // 连接数据库
    @PostMapping("/connect")
    public Result connect(@RequestBody DbConnectionInfo info) throws SQLException {
        if (!dataSourceService.testConnection(info)) {
            return Result.error("连接数据库失败");
        }

        String dataSourceKey = generateKey(info);
        dataSourceService.addDataSource(dataSourceKey, info);

        // 设置当前线程使用该数据源
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceKey);

        return Result.ok("连接数据库成功");
    }

    // 获取表结构
    @GetMapping("/tables")
    public Result<List<TableInfo>> getTables() throws SQLException {
        try {
            List<TableInfo> tables = metaDataService.getTables();
            return Result.ok(tables);
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }

    // 断开连接
    @PostMapping("/disconnect")
    public Result<?> disconnect(@RequestParam String key) {
        dynamicDataSource.removeDataSource(key);
        return Result.ok();
    }

    private String generateKey(DbConnectionInfo info) {
        return DigestUtils.md5DigestAsHex(
                (info.getUrl() + info.getUsername()).getBytes()
        );
    }

}
