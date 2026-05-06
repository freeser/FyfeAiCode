package com.fyfe;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = MainTest.class)
class MainTestTests {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    @Autowired
    private ChatMemory chatMemory;

    @Test
    public void testMemory() {
        // 构建一个ChatMemory对象用来存储对话的消息内容
//        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        String id = UUID.randomUUID().toString(); // 唯一标识符,表示某一个用户与大模型之间的关联

        // 第一轮对话
        // 使用唯一标识符对不同的对话进行隔离，将用户的消息存储在下来
        chatMemory.add(id, new UserMessage("我36岁是一个java全栈工程师"));

        // Prompt是SpringAI抽象出来的封装的发送给大模型的包含:提示词内存 上下文信息推理参数等所有的输入信息
        // 可以屏蔽不同的厂商大模型之间输入的差异
        // 不需要关心底层细节，只需要直接构建对象即可
        String text = dashScopeChatModel.call(
                Prompt.builder()
                    // 在ChatMemory中将所有的消息都查询出来
                    .messages(chatMemory.get(id))
                    .build()
            )
            .getResult()
            .getOutput()
            .getText();
        // 将AI返回的消息进程存储
        chatMemory.add(id, new AssistantMessage(text));
        System.out.println(text);

        System.out.println("\n----------------------");
        // 第二轮对话
        // 将用户第二次的对话存储到ChatMemory中
        chatMemory.add(id, new UserMessage("我的工作是什么"));
        String text2 = dashScopeChatModel.call(
                        Prompt.builder()
                                // 再次从ChatMemory中查找到所有存储的消息
                                .messages(chatMemory.get(id))
                                .build()
                )
                .getResult()
                .getOutput()
                .getText();
        chatMemory.add(id, new UserMessage(text2));
        System.out.println(text2);
    }

}
