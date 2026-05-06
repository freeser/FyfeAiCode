package com.fyfe.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@RestController
@RequestMapping(value = "/stream/ai", produces = "text/stream;charset=utf-8") // 流式响应进行返回
@RequiredArgsConstructor
public class TestController02 {
    private final QwenStreamingChatModel qwenStreamingChatModel;

    @RequestMapping(value = "/chat/{question}", produces = "text/stream;charset=utf-8")
    public Flux<String> getStream(@PathVariable("question") String question) {
        // 将结果 封闭到Flux中
        Flux<String> result = Flux.create(stringFluxSink -> {
            qwenStreamingChatModel.chat(question, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
//                    StreamingChatResponseHandler.super.onPartialResponse(partialResponse);
                    // 一旦有结果则自动调用
                    stringFluxSink.next(partialResponse); // 将大模型的结果封装到响应中
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    // 结束时自动调用的方法
                    stringFluxSink.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    // 发生异常时自动触发
                    throwable.printStackTrace(); // 打印异常信息
                    stringFluxSink.error(throwable);
                }
            });
        });
        return result;
    }
}
