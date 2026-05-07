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
    public void testMemory04() {
        System.out.println(client.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "key1"))
                .user("我的身份证号码是 83749279383729475 ，请问我的生日是多少")
                .call()
                .content());
    }
}
