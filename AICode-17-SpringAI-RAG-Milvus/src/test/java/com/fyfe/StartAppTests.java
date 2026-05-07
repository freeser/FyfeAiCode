package com.fyfe;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {
    @Autowired
    private VectorStore vectorStore;

    @Test
    public void test01() {
        List<String> batchTexts = new ArrayList<>();
        batchTexts.add("【2025新款】智能保温杯 316不锈钢 温度显示 24小时保温 白色 500ml");
        batchTexts.add("无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果");
        batchTexts.add("机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色");
        batchTexts.add("家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款");
        batchTexts.add("电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色");

//        List<Document> documents = batchTexts.stream()
//                .map(Document::new)
//                .collect(Collectors.toList());


        List<Document> documents = new ArrayList<>();
        for (String batchText : batchTexts) {
            documents.add(new Document(batchText));
        }

        // 在这里将我们之前的文档直接调用wriete方法写入到miluvs中
        vectorStore.write(documents);
    }

    @Test
    public void test02() {
        String query = "无线蓝牙耳机";
        List<Document> documents = vectorStore.similaritySearch(query);
        for (Document document : documents) {
            System.out.println(document); // 查询文档中有一个score字段，表示相似度得分，如果得分越高，则相似度越高
            // 此处需要注意向量数据库做匹配时，不是传统关系数据库的模糊查询概念
            // 不是看文字，而是只看文本对应的向量数据，如果向量数据能够匹配则认为是匹配
            // 至于最后结果到底是否匹配需要看评分
        }
    }
}
