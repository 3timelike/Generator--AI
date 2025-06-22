package com.zsy.contron;

import com.zsy.util.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/create")
public class CreateTableControl {

    //创建表的接口
    @PostMapping("/table")
    public Result createTable(@RequestBody String tableDefinitionJson){
          return null;
    }

    //创建service的接口

    //创建mapper的接口

    //创建dto，vo，po的接口
}
