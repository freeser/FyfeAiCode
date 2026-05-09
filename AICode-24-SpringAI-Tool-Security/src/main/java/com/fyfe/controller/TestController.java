package com.fyfe.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class TestController {
    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/call")
    public String call(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call().content();
    }
}
