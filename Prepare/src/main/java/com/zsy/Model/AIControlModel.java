package com.zsy.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIControlModel {
      String file = "import lombok.RequiredArgsConstructor;\n" +
              "import org.springframework.ai.chat.client.ChatClient;\n" +
              "import org.springframework.web.bind.annotation.RequestMapping;\n" +
              "import org.springframework.web.bind.annotation.RequestParam;\n" +
              "import org.springframework.web.bind.annotation.RestController;\n" +
              "import reactor.core.publisher.Flux;\n" +
              "\n" +
              "import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;\n" +
              "\n" +
              "@RequiredArgsConstructor\n" +
              "@RestController\n" +
              "@RequestMapping(\"/ai\")\n" +
              "public class ChatController {\n" +
              "\n" +
              "    private final ChatClient chatClient;\n" +
              "@RequestMapping(value = \"/chat\", produces = \"text/html;charset=UTF-8\")\n" +
              "    public Flux<String> chat(@RequestParam(defaultValue = \"你是谁\") String prompt, String chatId) {\n" +
              "    \n" +
              "        return chatClient\n" +
              "                .prompt(prompt)\n" +
              "                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))\n" +
              "                .stream()\n" +
              "                .content();\n" +
              "    }\n" +
              "}";
}
