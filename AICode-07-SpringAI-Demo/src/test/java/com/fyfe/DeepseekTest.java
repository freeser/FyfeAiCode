package com.fyfe;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class DeepseekTest {

    @Resource
    private DeepSeekChatModel deepSeekChatModel;

    @Test
    public void test03(){
        String response = deepSeekChatModel.call("你好");
        System.out.println("response = " + response);
    }

}
