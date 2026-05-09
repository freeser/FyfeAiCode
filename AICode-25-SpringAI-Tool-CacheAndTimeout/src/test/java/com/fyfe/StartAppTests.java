package com.fyfe;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {


    @Resource
    private ChatClient chatClient;

    /**
     * 测试无缓存情况下的工具调用性能
     */
    @Test
    public void test01(){
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        String question = "广州的天气怎么样";

        String content = chatClient.prompt()
                .user(question)
                .call()
                .content();

        System.out.println(content);
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * 测试有缓存情况下的工具调用性能
     * 预期：第二次调用应从缓存直接返回，大幅减少耗时
     */
    @Test
    public void test02(){
        String question = "广州的天气怎么样";

        // ===== 第一次调用 =====
        long startTime1 = System.currentTimeMillis();
        String content1 = chatClient.prompt()
                .user(question)
                .call()
                .content();
        System.out.println(content1);
        System.out.println("第一次耗时：" + (System.currentTimeMillis() - startTime1) + "ms");

        // ===== 第二次调用（预期从缓存返回）=====
        long startTime2 = System.currentTimeMillis();
        String content2 = chatClient.prompt()
                .user(question)
                .call()
                .content();
        System.out.println(content2);
        System.out.println("第二次耗时：" + (System.currentTimeMillis() - startTime2) + "ms");
    }

    /**
     * 测试超时控制效果
     * 预期：3秒超时，快速返回错误提示
     */
    @Test
    public void test03(){
        long startTime = System.currentTimeMillis();

        String question = "北京的天气怎么样";

        String content = chatClient.prompt()
                .user(question)
                .call()
                .content();

        System.out.println("返回结果：" + content);
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
