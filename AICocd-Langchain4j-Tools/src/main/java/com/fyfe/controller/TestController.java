package com.fyfe.controller;

import com.fyfe.config.AIConfig;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/memory")
@RequiredArgsConstructor
public class TestController {
    private final AIConfig.Assistant assistant;

    @RequestMapping("/chat/{question}")
    public String myMemory(@PathVariable String question) {
        return assistant.chat(question);
    }

    @RequestMapping(value = "/stream/{question}", produces = "text/stream;charset=utf-8")
    public Flux<String> fluxMemory(@PathVariable String question) {
        TokenStream stream = assistant.stream(question); // 流式响应
        return Flux.create(fluxSink -> {
            stream.onPartialResponse(fluxSink::next) // 等价于partialResponse -> sink.next(partialResponse)
                    .onCompleteResponse(chatResponse -> fluxSink.complete())
                    .onError(fluxSink::error)
                    .start();
        });
    }
}
