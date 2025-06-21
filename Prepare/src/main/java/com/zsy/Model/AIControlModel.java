package com.zsy.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIControlModel {
      String file = "@RequiredArgsConstructor\n" +
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
