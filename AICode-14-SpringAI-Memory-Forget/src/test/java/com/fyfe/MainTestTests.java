package com.fyfe;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = MainTest.class)
class MainTestTests {
    @Autowired
    @Qualifier("memoryClient")
    private ChatClient client;


    @Autowired
    @Qualifier("digClient")
    private ChatClient digestClient;

    @Autowired
    ChatMemory chatMemory;

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

    @Test
    public void testMemory03() {
        String key = "test-key";
        List<Message> messages = chatMemory.get(key);
        if (messages.size() >= 5) {
            // 使用digestClient将5条消息进行摘要
            AssistantMessage assistantMessage = digestClient.prompt()
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, key))
                    .call()
                    .chatResponse()
                    .getResult()
                    .getOutput();
            chatMemory.clear(key);
            chatMemory.add(key, assistantMessage);
        }

        String content = client.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, key))
                .user("请介绍一下我")
                .call()
                .content();

        System.out.println(content);
    }

}
