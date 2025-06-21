package com.zsy.generateTable;

import com.zsy.Model.AIConfigModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GenerateChatTableAIConfig {
    public void generateAIConfig(String outputDir) {
        File file  = new File(outputDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String context = new AIConfigModel().getConfigFile();
        File javaFile = new File(outputDir, "ChatClientConfig.java");
        try {
            Files.writeString(javaFile.toPath(), context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
