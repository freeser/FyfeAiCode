package com.fyfe;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.Map;

@SpringBootTest
class MainTestTests {

    @Test
    public void test01(@Autowired ChatClient chatClient) {
        System.out.println(chatClient.prompt()
                .user("你好")
                .call()
                .content());
    }

    @Test
    public void test02(@Autowired ChatClient chatClient) {
        System.out.println(chatClient.prompt()
                .system("你的名字叫小黑")
                .user("你好")
                .call()
                .content());
    }

    @Test
    public void test03(@Autowired ChatClient chatClient) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("你的名字是{name}")
                .build();
        Map<String, Object> map = Map.of("name", "小红");
        System.out.println(chatClient.prompt()
                .system(promptTemplate.render(map))
                .user("你好")
                .call()
                .content());
    }

    @Test
    public void test04(@Autowired ChatClient chatClient, @Value("classpath:files/test.st") Resource resource) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .resource(resource)
                .build();
        Map<String, Object> map = Map.of("name", "小绿");
        String content = chatClient.prompt()
                .system(promptTemplate.render(map))
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

}
