package com.fyfe;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class DashScopeTest {

    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @Test
    public void test02(){
        String response = dashScopeChatModel.call("你好");
        System.out.println("response = " + response);
    }

}
