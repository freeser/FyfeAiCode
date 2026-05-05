package com.fyfe.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class TestController {
    private final QwenChatModel qwenChatModel;

    @RequestMapping("/chat/{question}")
    public String chat(@PathVariable("question") String question) {
        System.out.println("当前提问的问题：" + question);
        return qwenChatModel.chat(question);
    }
}
