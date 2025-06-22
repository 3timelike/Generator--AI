package com.zsy.util;


import com.zsy.entiy.ColumnInfo;
import com.zsy.entiy.TableInfo;
import com.zsy.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 代码生成器
 */
@Component
public class CodeGenerator {

    @Autowired
    private MetaDataService metaDataService;

    private static final String OUTPUT_PATH = "C:\\Users\\24244\\Desktop\\TEST";

    /**
     * 生成指定表的所有代码
     */
    public void generateCode(String tableName) throws Exception {
        // 1. 获取表结构信息
        TableInfo tableInfo = metaDataService.getTableStructure(tableName);
        if (tableInfo == null) {
            throw new RuntimeException("表不存在: " + tableName);
        }

        // 2. 创建基础包结构
        createPackageDirs();

        // 3. 生成各层代码
        generateController(tableInfo);

        generatePO(tableInfo);
        generateQuery(tableInfo);
        generateVO(tableInfo);

        generateMapper(tableInfo);
        generateService(tableInfo);

        generateServiceImpl(tableInfo);
        System.out.println("代码生成完成，输出路径: " + OUTPUT_PATH);
    }

    private void createPackageDirs() {
        new File(OUTPUT_PATH + "\\entity").mkdirs();
        new File(OUTPUT_PATH + "\\mapper").mkdirs();
        new File(OUTPUT_PATH + "\\service").mkdirs();
        new File(OUTPUT_PATH + "\\controller").mkdirs();
    }

    // 其他生成方法将在下面实现...
    /**
     * 生成PO(持久化对象)
     */
    private void generatePO(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "PO";
        String packageName = "package com.zsy.entity;\n\n";

        StringBuilder imports = new StringBuilder();
        imports.append("import com.baomidou.mybatisplus.annotation.*;\n");
        imports.append("import lombok.Data;\n");
        imports.append("import lombok.EqualsAndHashCode;\n");
        imports.append("import java.util.Date;\n\n");

        StringBuilder classContent = new StringBuilder();
        classContent.append("@Data\n");
        classContent.append("@EqualsAndHashCode(callSuper = false)\n");
        classContent.append("@TableName(\"").append(tableInfo.getTableName()).append("\")\n");
        classContent.append("public class ").append(className).append(" {\n");

        // 添加主键
        classContent.append("    @TableId(type = IdType.AUTO)\n");
        classContent.append("    private Long id;\n\n");

        // 添加表字段
        for (ColumnInfo column : tableInfo.getColumns()) {
            String fieldType = getJavaType(column.getDataType());
            String fieldName = toCamelCase(column.getColumnName(), false);

            //classContent.append("    /** ").append(column.getComment()).append(" */\n");
            classContent.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
        }

        // 公共字段
        classContent.append("    @TableField(fill = FieldFill.INSERT)\n");
        classContent.append("    private Date createTime;\n\n");
        classContent.append("    @TableField(fill = FieldFill.INSERT_UPDATE)\n");
        classContent.append("    private Date updateTime;\n\n");
        classContent.append("    @Version\n");
        classContent.append("    private Integer version;\n\n");
        classContent.append("    @TableLogic\n");
        classContent.append("    private Integer deleted;\n");

        classContent.append("}");

        String content = packageName + imports.toString() + classContent.toString();
        writeFile("entity", className + ".java", content);
    }

    /**
     * 数据库类型转Java类型
     */
    private String getJavaType(String dbType) {
        if (dbType == null) return "Object";

        dbType = dbType.toLowerCase();
        if (dbType.contains("varchar") || dbType.contains("text") || dbType.contains("char")) {
            return "String";
        } else if (dbType.contains("int")) {
            return "Integer";
        } else if (dbType.contains("bigint")) {
            return "Long";
        } else if (dbType.contains("decimal") || dbType.contains("numeric")) {
            return "BigDecimal";
        } else if (dbType.contains("date") || dbType.contains("time")) {
            return "Date";
        } else if (dbType.contains("bit")) {
            return "Boolean";
        } else if (dbType.contains("float")) {
            return "Float";
        } else if (dbType.contains("double")) {
            return "Double";
        }
        return "Object";
    }
    /**
     * 生成Mapper接口
     */
    private void generateMapper(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "Mapper";
        String poClassName = toCamelCase(tableInfo.getTableName(), true) + "PO";

        String content = "package com.zsy.mapper;\n\n" +
                "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n" +
                "import com.zsy.entity." + poClassName + ";\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " Mapper接口\n" +
                " */\n" +
                "public interface " + className + " extends BaseMapper<" + poClassName + "> {\n" +
                "    // 可添加自定义SQL方法\n" +
                "}";

        writeFile("mapper", className + ".java", content);
    }
    /**
     * 生成Service接口
     */
    private void generateService(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "Service";
        String poClassName = toCamelCase(tableInfo.getTableName(), true) + "PO";
        String voClassName = toCamelCase(tableInfo.getTableName(), true) + "VO";
        String queryClassName = toCamelCase(tableInfo.getTableName(), true) + "Query";

        String content = "package com.zsy.service;\n\n" +
                "import com.baomidou.mybatisplus.extension.service.IService;\n" +
                "import com.zsy.entity." + poClassName + ";\n" +
                "import com.zsy.entity.query." + queryClassName + ";\n" +
                "import com.zsy.entity.vo.PageVO;\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " 服务接口\n" +
                " */\n" +
                "public interface " + className + " extends IService<" + poClassName + "> {\n" +
                "    /**\n" +
                "     * 分页查询\n" +
                "     */\n" +
                "    PageVO<" + poClassName + "> pageList(" + queryClassName + " query);\n\n" +
                "    /**\n" +
                "     * 根据ID获取详情\n" +
                "     */\n" +
                "    " + poClassName + " getDetail(Long id);\n\n" +
                "    /**\n" +
                "     * 新增记录\n" +
                "     */\n" +
                "    boolean create(" + poClassName + " entity);\n\n" +
                "    /**\n" +
                "     * 更新记录\n" +
                "     */\n" +
                "    boolean update(" + poClassName + " entity);\n\n" +
                "    /**\n" +
                "     * 删除记录\n" +
                "     */\n" +
                "    boolean delete(Long id);\n" +
                "}";

        writeFile("service", className + ".java", content);
    }
    /**
     * 生成VO对象
     */
    private void generateVO(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "VO";
        String poClassName = toCamelCase(tableInfo.getTableName(), true) + "PO";

        String content = "package com.zsy.entity.vo;\n\n" +
                "import com.zsy.entity." + poClassName + ";\n" +
                "import lombok.Data;\n" +
                "import lombok.EqualsAndHashCode;\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " 视图对象\n" +
                " */\n" +
                "@Data\n" +
                "@EqualsAndHashCode(callSuper = true)\n" +
                "public class " + className + " extends " + poClassName + " {\n" +
                "    // 可添加额外展示字段\n" +
                "}";

        writeFile("entity\\vo", className + ".java", content);
    }
    /**
     * 生成Service实现类
     */
    private void generateServiceImpl(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "ServiceImpl";
        String interfaceName = toCamelCase(tableInfo.getTableName(), true) + "Service";
        String poClassName = toCamelCase(tableInfo.getTableName(), true) + "PO";
        String mapperClassName = toCamelCase(tableInfo.getTableName(), true) + "Mapper";
        String queryClassName = toCamelCase(tableInfo.getTableName(), true) + "Query";

        String content = "package com.zsy.service.impl;\n\n" +
                "import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\n" +
                "import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n" +
                "import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n" +
                "import com.zsy.entity." + poClassName + ";\n" +
                "import com.zsy.entity.query." + queryClassName + ";\n" +
                "import com.zsy.entity.vo.PageVO;\n" +
                "import com.zsy.mapper." + mapperClassName + ";\n" +
                "import com.zsy.service." + interfaceName + ";\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " 服务实现\n" +
                " */\n" +
                "@Service\n" +
                "public class " + className + " extends ServiceImpl<" + mapperClassName + ", " + poClassName + "> implements " + interfaceName + " {\n\n" +
                "    @Override\n" +
                "    public PageVO<" + poClassName + "> pageList(" + queryClassName + " query) {\n" +
                "        Page<" + poClassName + "> page = new Page<>(query.getPageNum(), query.getPageSize());\n" +
                "        QueryWrapper<" + poClassName + "> wrapper = new QueryWrapper<>();\n\n" +
                "        // 构建查询条件\n" +
                "        if (query.getKeyword() != null) {\n" +
                "            wrapper.like(\"name\", query.getKeyword());\n" +
                "        }\n\n" +
                "        if (query.getStartTime() != null && query.getEndTime() != null) {\n" +
                "            wrapper.between(\"create_time\", query.getStartTime(), query.getEndTime());\n" +
                "        }\n\n" +
                "        Page<" + poClassName + "> result = this.page(page, wrapper);\n" +
                "        return PageVO.build(result);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + poClassName + " getDetail(Long id) {\n" +
                "        return this.getById(id);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public boolean create(" + poClassName + " entity) {\n" +
                "        return this.save(entity);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public boolean update(" + poClassName + " entity) {\n" +
                "        return this.updateById(entity);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public boolean delete(Long id) {\n" +
                "        return this.removeById(id);\n" +
                "    }\n" +
                "}";

        writeFile("service\\impl", className + ".java", content);
    }
    /**
     * 生成Controller
     */
    private void generateController(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "Controller";
        String poClassName = toCamelCase(tableInfo.getTableName(), true) + "PO";
        String serviceName = toCamelCase(tableInfo.getTableName(), true) + "Service";
        String voClassName = toCamelCase(tableInfo.getTableName(), true) + "VO";
        String queryClassName = toCamelCase(tableInfo.getTableName(), true) + "Query";

        String content = "package com.zsy.controller;\n\n" +
                "import com.zsy.entity." + poClassName + ";\n" +
                "import com.zsy.entity.query." + queryClassName + ";\n" +
                "import com.zsy.entity.vo.PageVO;\n" +
                "import com.zsy.service." + serviceName + ";\n" +
                "import com.zsy.util.Result;\n" +
                "import org.springframework.web.bind.annotation.*;\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " 控制器\n" +
                " */\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/" + toCamelCase(tableInfo.getTableName(), false) + "\")\n" +
                "public class " + className + " {\n\n" +
                "    private final " + serviceName + " " + toCamelCase(tableInfo.getTableName(), false) + "Service;\n\n" +
                "    public " + className + "(" + serviceName + " " + toCamelCase(tableInfo.getTableName(), false) + "Service) {\n" +
                "        this." + toCamelCase(tableInfo.getTableName(), false) + "Service = " + toCamelCase(tableInfo.getTableName(), false) + "Service;\n" +
                "    }\n\n" +
                "    /**\n" +
                "     * 分页查询\n" +
                "     */\n" +
                "    @GetMapping\n" +
                "    public Result<PageVO<" + poClassName + ">> pageList(" + queryClassName + " query) {\n" +
                "        return Result.ok(" + toCamelCase(tableInfo.getTableName(), false) + "Service.pageList(query));\n" +
                "    }\n\n" +
                "    /**\n" +
                "     * 获取详情\n" +
                "     */\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public Result<" + poClassName + "> getDetail(@PathVariable Long id) {\n" +
                "        return Result.ok(" + toCamelCase(tableInfo.getTableName(), false) + "Service.getDetail(id));\n" +
                "    }\n\n" +
                "    /**\n" +
                "     * 新增记录\n" +
                "     */\n" +
                "    @PostMapping\n" +
                "    public Result<Boolean> create(@RequestBody " + poClassName + " entity) {\n" +
                "        return Result.ok(" + toCamelCase(tableInfo.getTableName(), false) + "Service.create(entity));\n" +
                "    }\n\n" +
                "    /**\n" +
                "     * 更新记录\n" +
                "     */\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public Result<Boolean> update(@PathVariable Long id, @RequestBody " + poClassName + " entity) {\n" +
                "        entity.setId(id);\n" +
                "        return Result.ok(" + toCamelCase(tableInfo.getTableName(), false) + "Service.update(entity));\n" +
                "    }\n\n" +
                "    /**\n" +
                "     * 删除记录\n" +
                "     */\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public Result<Boolean> delete(@PathVariable Long id) {\n" +
                "        return Result.ok(" + toCamelCase(tableInfo.getTableName(), false) + "Service.delete(id));\n" +
                "    }\n" +
                "}";

        writeFile("controller", className + ".java", content);
    }
    /**
     * 将下划线命名转为驼峰命名
     */
    private String toCamelCase(String str, boolean firstLetterUpperCase) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpper = firstLetterUpperCase;

        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }

        return result.toString();
    }

    /**
     * 写入文件
     */
    private void writeFile(String packageName, String fileName, String content) throws IOException {
        File dir = new File(OUTPUT_PATH + "\\" + packageName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
    /**
     * 生成Query对象
     */
    private void generateQuery(TableInfo tableInfo) throws IOException {
        String className = toCamelCase(tableInfo.getTableName(), true) + "Query";

        String content = "package com.zsy.entity.query;\n\n" +
                "import lombok.Data;\n" +
                "import java.util.Date;\n\n" +
                "/**\n" +
                " * " + tableInfo.getTableName() + " 查询条件\n" +
                " */\n" +
                "@Data\n" +
                "public class " + className + " {\n" +
                "    private Integer pageNum = 1;\n" +
                "    private Integer pageSize = 10;\n" +
                "    private String keyword;\n" +
                "    private Date startTime;\n" +
                "    private Date endTime;\n" +
                "    // 可根据实际需求添加更多查询条件\n" +
                "}";

        writeFile("entity\\query", className + ".java", content);
    }
}