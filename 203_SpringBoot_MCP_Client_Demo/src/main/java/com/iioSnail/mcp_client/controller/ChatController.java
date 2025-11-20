package com.iioSnail.mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/chat")
public class ChatController {

    @Autowired
    private ChatClient.Builder builder;

    @Autowired
    private SyncMcpToolCallbackProvider provider;

    @PostMapping("/ask")
    public String ask(@RequestParam("question") String question) {
        ChatClient chatClient = builder
                .defaultSystem("You are an AI assistant built by iioSnail Company. Your name is Snail").build();

        return chatClient.prompt().user(question).call().content();
    }

    @PostMapping("/ask2")
    public String ask2(@RequestParam("question") String question) {
        ChatClient chatClient = builder.build();

        List<Message> messageList = new ArrayList<>();
        messageList.add(new UserMessage("Hello, My name is Amy. Who are you?"));
        messageList.add(new AssistantMessage("Hello！I'm Snail, an AI assistant. How can I help you?"));  // The Answer of LLM.
        // ... Other conversation messages.
        messageList.add(new UserMessage(question));  // New question.

        return chatClient.prompt()
                .messages(messageList)  // Pass the previous conversation messages.
                .call().content();
    }


    @PostMapping("/multiTurnChat")
    @ResponseBody
    public List<Map<String, String>> multiTurnChat(@RequestBody List<Map<String, String>> messages) {
        List<Message> messageList = new ArrayList<>();
        messages.forEach(itemMap -> {
            String role = itemMap.get("role");
            String content = itemMap.get("content");

            if ("user".equals(role)) {
                messageList.add(new UserMessage(content));
            } else if ("assistant".equals(role)) {
                messageList.add(new AssistantMessage(content));
            }
        });

        ChatClient.CallResponseSpec responseSpec = builder.build()
                .prompt()
                .messages(messageList)
                .toolCallbacks(provider.getToolCallbacks())
                .call();

        messages.add(Map.of(
                "role", "assistant",
                "content", responseSpec.content()
        ));

        return messages;
    }

}
