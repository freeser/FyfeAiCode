package com.fyfe.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MyAdvisor implements BaseAdvisor {
    @Autowired
    @Qualifier(value = "piiClient")
    private ChatClient chatClient;

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        // 将用户的消息查询出来
        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();
        System.out.println("userMessage = " + userMessage.getText());
        // 使用PII模型对用户消息进行脱敏
        String cleaned = chatClient.prompt()
                .user("请对以下文本脱敏：\n" + userMessage.getText())
                .call()
                .content();
        System.out.println("cleaned = " + cleaned);
        // 将脱敏的消息重新封装成为用户消息
        // 将消息封装到请求中进行返回
        return chatClientRequest
                .mutate()
                .prompt(Prompt.builder().content(cleaned).messages().build())
                .build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}