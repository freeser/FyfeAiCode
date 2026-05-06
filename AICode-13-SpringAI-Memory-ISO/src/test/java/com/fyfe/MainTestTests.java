package com.fyfe;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = MainTest.class)
class MainTestTests {
    @Autowired
    @Qualifier("memoryClient")
    private ChatClient client;

    @Test
    public void test() {
        String content = client.prompt()
                .user("我是一名厨师，今天30岁")
                // 一般userId + 业务id
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "Test-key"))
                .call()
                .content();

        System.out.println(content);

        String content2 = client.prompt()
                .user("我今年多少岁了")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "Test-key"))
                .call()
                .content();

        System.out.println(content2);
    }

}
