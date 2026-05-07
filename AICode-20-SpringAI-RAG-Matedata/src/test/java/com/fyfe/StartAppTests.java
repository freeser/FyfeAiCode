package com.fyfe;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
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
    private VectorStore vectorStore;

    @Test
    public void test01() {
        List<String> batchTexts = new ArrayList<>(); // 商品描述
        batchTexts.add("【2025新款】智能保温杯 316不锈钢 温度显示 24小时保温 白色 500ml");
        batchTexts.add("无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果");
        batchTexts.add("机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色");

        List<String> batchTexts02 = new ArrayList<>(); // 商品名
        batchTexts02.add("家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款");
        batchTexts02.add("电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色");

        List<Document> collect = batchTexts.stream().map(
                document -> Document.builder() // 构建Document对象时除了设定文本内容以外，还会设定对应的元数据
                        .text(document)
                        .metadata("source", "描述")
                        .build()
        ).collect(Collectors.toList());

        List<Document> collect02 = batchTexts02.stream().map(
                document -> Document.builder()
                        .text(document)
                        .metadata("source", "名称")
                        .build()
        ).collect(Collectors.toList());

        // 在这里将我们之前的文档直接调用wriete方法写入到miluvs中
        vectorStore.write(collect);
        vectorStore.write(collect02);
    }

    @Test
    public void test02() {
        String query = "家用投影仪"; // 设定无数据时，家用投影仪 source -- 名称
        // searchRequest 进行构建查询条件
        // filterExpression 设定指定的条件
        List<Document> documents = vectorStore.similaritySearch( // 进行查询操作
                SearchRequest.builder() // 构建 查询 条件
                        .query(query)
                        .filterExpression("source=='描述' OR source=='名称") // AND OR 构建元数据的过滤条件
                        .topK(2) // 可能 会有多条数据，返回的时候按照匹配度评分从高到低的召回2条数据
                        .build()
        );

        documents.forEach(doc -> System.out.println(doc.getText()));

//        for (Document document : documents) {
//            System.out.println(document); // 查询文档中有一个score字段，表示相似度得分，如果得分越高，则相似度越高
//            // 此处需要注意向量数据库做匹配时，不是传统关系数据库的模糊查询概念
//            // 不是看文字，而是只看文本对应的向量数据，如果向量数据能够匹配则认为是匹配
//            // 至于最后结果到底是否匹配需要看评分
//        }
    }


    // 一条数据可以设定多个元数据
    @Test
    public void test03() {
        String query = "智能手机 全碳机身 3000HA 白色 120g";

        HashMap<String, Object> map = new HashMap<>();
        map.put("source", "商品");
        map.put("category", "手机");
        map.put("price", 200);
        map.put("date", "2026-10-10");

        Document build = Document.builder().text(query)
                .metadata(map)
                .build();
        vectorStore.write(List.of(build));
    }
}
