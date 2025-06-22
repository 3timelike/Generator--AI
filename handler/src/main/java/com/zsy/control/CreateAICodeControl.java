package com.zsy.control;

import com.zsy.generateTable.GenerateAIControl;
import com.zsy.generateTable.GenerateChatTableAIConfig;
import com.zsy.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/createAI")
public class CreateAICodeControl {


    @GetMapping("/SpringAI")
    public Result createTable(@RequestParam("url") String outFile) {
        GenerateAIControl generateAIControl =  new GenerateAIControl();
        GenerateChatTableAIConfig generateChatTableAIConfig = new GenerateChatTableAIConfig();
        generateChatTableAIConfig.generateAIConfig(outFile);
        generateAIControl.generateAIConfig(outFile);
        return Result.ok();
    }
}
