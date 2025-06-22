package com.zsy.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AIConfigModel {
     String DefaultSystemMessage = "你是一个Java高手，熟读各种源码";
     String configFile = "import org.springframework.ai.chat.client.ChatClient;\n" +
             "import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;\n" +
             "import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;\n" +
             "import org.springframework.ai.chat.memory.ChatMemory;\n" +
             "import org.springframework.ai.chat.memory.InMemoryChatMemory;\n" +
             "import org.springframework.ai.ollama.OllamaChatModel;\n" +
             "import org.springframework.context.annotation.Bean;\n" +
             "import org.springframework.context.annotation.Configuration;\n" +
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
             "                .defaultSystem(\"" + DefaultSystemMessage +"\")\n" +
             "                .defaultAdvisors(\n" +
             "                        new SimpleLoggerAdvisor(),\n" +
             "                        new MessageChatMemoryAdvisor(chatMemory)\n" +
             "                )\n" +
             "                .build();\n" +
             "    }\n" +
             "}";


}
