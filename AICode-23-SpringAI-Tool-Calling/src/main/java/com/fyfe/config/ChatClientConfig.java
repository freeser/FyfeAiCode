package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.fyfe.service.GetWeatherInfo;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ChatClientConfig {
    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @Resource
    private GetWeatherInfo getWeatherInfo;

    @Bean
    public ChatClient getChatClient() {
        return ChatClient.builder(dashScopeChatModel)
                .defaultTools(getWeatherInfo) // 将工具方法注册到大模型上【让程序扫描Tool注解，如果扫描则进行注册】
                .build();
    }
}
