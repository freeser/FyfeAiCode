package com.fyfe;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {
    @Autowired
    private ChatClient chatClient;

    // 基础检索流程演示
    @Test
    public void test01() {
        // 发起查询
        String response = chatClient.prompt("给我介绍一款无线蓝牙耳机").call().content();
        System.out.println(response);
    }
}
