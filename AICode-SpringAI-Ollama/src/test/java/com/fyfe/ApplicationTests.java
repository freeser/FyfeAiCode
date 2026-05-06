package com.fyfe;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class ApplicationTests {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Test
    public void test01() {
        String msg = ollamaChatModel.call("你好");
        System.out.println(msg);
    }

}
