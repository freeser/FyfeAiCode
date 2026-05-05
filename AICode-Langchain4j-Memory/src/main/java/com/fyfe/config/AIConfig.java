package com.fyfe.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {
    public interface Assistant {
        String chat(String question); // 阻塞式调用
        // 流式调用
        TokenStream stream(String question);
    }

    @Bean
    public Assistant assist(QwenStreamingChatModel qwenStreamingChatModel, QwenChatModel qwenChatModel) {
        // 创建记忆会话的对象【所谓的记忆其实就是将以前的圣诞内容进行拼接到新的问题中】
        // 随着圣诞的进行，Token会越来越长
        // 往往做记忆对话的时候会限制token的长度
        MessageWindowChatMemory withMaxMessages = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(Assistant.class)
                .chatModel(qwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemory(withMaxMessages)
                .build();
    }
}
