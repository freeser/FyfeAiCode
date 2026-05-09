package com.fyfe;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {

//    @Resource
//    private ChatClient chatClient;
//
//    @Test
//    public void test01() {
//        String result = chatClient.prompt().user("请帮我查询上海的天气情况")
//                .call()
//                .content();
//        System.out.println(result);
//    }


    @Resource
    private ChatClient chatClient;

    @Test
    public void test01() {
        String result = chatClient.prompt()
                .user("请帮我查询上海的天气情况，并告诉我现在是几点了")
                .call()
                .content();
        System.out.println(result);
    }
}
