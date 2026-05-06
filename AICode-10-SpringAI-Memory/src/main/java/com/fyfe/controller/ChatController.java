package com.fyfe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient client;

    @CrossOrigin("*")
    @RequestMapping(value = "/dashChat", produces = "text/stream;charset=UTF-8") // event-
    public Flux<String> dashChat(@RequestParam(value = "question") String question) {
        return client.prompt().user(question).stream().content();
    }
}