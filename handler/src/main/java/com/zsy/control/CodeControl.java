package com.zsy.control;

import com.zsy.service.MetaDataService;
import com.zsy.util.CodeGenerator;
import com.zsy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/code")
public class CodeControl {

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private MetaDataService metaDataService;

    /**
     * 生成指定表的代码
     */
    @PostMapping("/generate")
    public Result generate(@RequestParam String tableName) {
        try {
            codeGenerator.generateCode(tableName);
            return Result.ok(tableName + "代码生成成功");
        } catch (Exception e) {
            return Result.error("代码生成失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有表名
     */
    @GetMapping("/tables")
    public Result<List<String>> getAllTables() {
        try {
            return Result.ok(metaDataService.getAllTableNames());
        } catch (SQLException e) {
            return Result.error("获取表列表失败: " + e.getMessage());
        }
    }
}