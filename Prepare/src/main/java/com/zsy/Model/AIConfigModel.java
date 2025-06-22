package com.zsy.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AIConfigModel {
     String DefaultSystemMessage = "你是一个Java高手，熟读各种源码";
     String configFile = "import java.util.*;\n" +
             "\n" +
             "@Configuration\n" +
             "public class ChatClientConfig {\n" +
             "\n" +
             "    @Bean\n" +
             "    public ChatMemory chatMemory() {\n" +
             "        return new InMemoryChatMemory();\n" +
             "    }\n" +
             "\n" +
             "    @Bean \n" +
             "    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory) {\n" +
             "        return ChatClient\n" +
             "                .builder(model)\n" +
             "                .defaultSystem(\"" + DefaultSystemMessage + "\")\n" +
             "                .defaultAdvisors(\n" +
             "                        new SimpleLoggerAdvisor(),\n" +
             "                        new MessageChatMemoryAdvisor(chatMemory)\n" +
             "                )\n" +
             "                .build();\n" +
             "    }\n" +
             "}\n";


}
