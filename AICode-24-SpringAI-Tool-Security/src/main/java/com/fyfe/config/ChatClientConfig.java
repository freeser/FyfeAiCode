package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.fyfe.service.GetWeatherInfo;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @Resource
    private GetWeatherInfo getWeatherInfo;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(dashScopeChatModel)
                .defaultTools(getWeatherInfo)
                .build();
    }
}
