package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class ChatTimeOutConfig {

    @Autowired
    private JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(
                        ClientHttpRequestFactories.get(
                                JdkClientHttpRequestFactory.class,
                                ClientHttpRequestFactorySettings.DEFAULTS
                                        .withConnectTimeout(Duration.ofMinutes(3)) //设定连接超时时间位3分钟
                                        .withReadTimeout(Duration.ofMinutes(3))    //设定获取结果超时时间为3分钟
                        )
                );
    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(jdbcChatMemoryRepository).build();
    }


    @Bean(value = "memoryClient")
    ChatClient chatClient(DashScopeChatModel dashScopeChatModel, ChatMemory chatMemory) {
        return ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors( // 使用PromptChatMemoryAdvisor，这个Advisor是专门用于适配ChatMemory对象的对话记忆拦截器
                        PromptChatMemoryAdvisor.builder(chatMemory).build()
                ).build();
    }
}
