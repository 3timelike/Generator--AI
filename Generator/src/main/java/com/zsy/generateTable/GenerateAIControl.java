package com.zsy.generateTable;

import com.zsy.Model.AIConfigModel;
import com.zsy.Model.AIControlModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GenerateAIControl {
    public void generateAIConfig(String outputDir) {
        File file  = new File(outputDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String context = new AIControlModel().getFile();
        File javaFile = new File(outputDir, "ChatController.java");
        try {
            Files.writeString(javaFile.toPath(), context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
