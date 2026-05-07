package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class ChatConfig {
    private final DashScopeChatModel dashScopeChatModel;

    @Bean(value = "baseClient")
    public ChatClient chatClient(@Value("classpath:files/test.st") Resource resource) {
        // 通过chatModel构建chatCLient
        // chatCLient对大模型发起调用的对象【设定调用时候系统 的提示词】
//        return ChatClient.builder(dashScopeChatModel)
//                .defaultSystem("你是一名Java开发工程师，你的名字是小白") // 没有使用模板，直接设定提示词
//                                                                        // 弊端，不灵活，此处写死名字为小白，永久就是小折，如果 要修改则很麻烦
//                                                                        // 解决：采用提示词模板，通过占位符对希望被改变的信息进行占位，然后通过值 去对占位符设置
//                .build();
//        PromptTemplate 提示词模板核心类：意义可以构建动态的提示词，语法：XXX{占位符} 通过map key与占位符对应，生效的时候：提示词就是Key对应的值
        PromptTemplate template = PromptTemplate.builder().resource(resource).build();
        HashMap<String, Object> map = new HashMap<>();
        map.put("zhiye", "Java");
        map.put("name", "小黑");
        return ChatClient.builder(dashScopeChatModel)
                .defaultSystem(template.render(map)).build();

    }

}