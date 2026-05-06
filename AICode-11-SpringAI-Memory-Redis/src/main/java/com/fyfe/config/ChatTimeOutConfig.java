package com.fyfe.config;

import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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

    // 注入redis操作Memory对象
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;
    @Bean
    public ChatMemory chatMemory(){
        // 专门用于操作Redistribution的记忆存储
        JedisRedisChatMemoryRepository redisChatMemoryRepository = JedisRedisChatMemoryRepository.builder()
                .host(host) // 添加reids的主机
                .port(port) // 添加redis的端口
                // .user 配置了用户的需要在这里添加用户名
                 .password(password) // 配置了密码的需要在这里设置密码
                .build();
        return MessageWindowChatMemory
                .builder()
                .maxMessages(10) // 最大消息数
                .chatMemoryRepository(redisChatMemoryRepository)
                .build();
    }
}
