package com.fyfe;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {
    // 百炼starter中自动配置了
    // DashscopeChatModel 跟大模型交互
    // DashscopeEmbeddingModel 调用百炼大模型蹭向量操作的客户端, 专用于调用向量操作的
    // embedForResponse 改善请求，参数是一个List允许同时处理多个语料
    // 结果EmbeddingResponse对象

    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;

    /**
     * 单条文本向量化演示
     *
     * 适用场景：实时性要求高、数据量较小的场景
     * 例如：用户输入实时查询、单机小规模数据处理
     */
    @Test
    public void testSingleTextEmbedding() {
        // 1. 准备测试文本
        // 选用电商商品描述作为测试数据，贴近企业实际RAG应用场景
        String testText = "【2025新款】智能保温杯 316不锈钢材质 支持温度显示 长效保温24小时 便携车载款 白色 500ml";

        // 2. 调用Embedding模型进行向量化
        // embedForResponse()方法接收List<String>，返回EmbeddingResponse
        // 注意：参数必须是List类型，即使是单条文本也需要包装在List中
        EmbeddingResponse response = dashScopeEmbeddingModel.embedForResponse(List.of(testText));

        // 3. 获取向量结果
        // response.getResult()返回EmbeddingResult对象
        // getOutput()返回float数组，即文本的向量表示
        float[] embedding = response.getResult().getOutput();

        // 输出向量维度（用于验证）
        System.out.println("向量维度：" + embedding.length);
        // 输出向量内容（前10维，便于查看）
        System.out.println("向量前10维：" + Arrays.toString(Arrays.copyOf(embedding, 10)));
    }

    /**
     * 批量文本向量化演示
     *
     * 适用场景：大规模文档向量化处理
     * 优势：显著提升处理速度，降低API调用次数
     */
    @Test
    public void testBatchTextEmbedding() {
        // 1. 准备批量测试数据
        // 模拟电商RAG场景中的商品信息批量向量化
        List<String> batchTexts = new ArrayList<>();
        batchTexts.add("【2025新款】智能保温杯 316不锈钢 温度显示 24小时保温 白色 500ml");
        batchTexts.add("无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果");
        batchTexts.add("机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色");
        batchTexts.add("家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款");
        batchTexts.add("电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色");

        // 2. 执行批量向量化
        // 一次性将5条文本转换为向量
        // 内部会进行批量API调用，相比单条调用效率提升显著
        EmbeddingResponse batchResponse = dashScopeEmbeddingModel.embedForResponse(batchTexts);

        // 3. 处理返回结果
        // getResults()返回Iterator，便于遍历多条结果
        batchResponse.getResults().iterator().forEachRemaining(result -> {
            float[] embedding = result.getOutput();
            System.out.println("向量维度：" + embedding.length);
            System.out.println("向量前10维：" + Arrays.toString(Arrays.copyOf(embedding, 10)));
        });
    }

    /**
     * 大批量数据向量化处理
     * 自动处理批次大小限制
     */
    @Test
    public void testLargeBatchEmbedding() {
        // 模拟大规模数据（假设有100条商品数据）
        List<String> allTexts = new ArrayList<>();
        // ... 填充100条数据

        // 配置参数
        int maxBatchSize = 10;  // text-embedding-v4最大批次
        List<float[]> allEmbeddings = new ArrayList<>();

        // 分批处理
        for (int i = 0; i < allTexts.size(); i += maxBatchSize) {
            // 计算当前批次的范围
            int end = Math.min(i + maxBatchSize, allTexts.size());
            List<String> batch = allTexts.subList(i, end);

            // 执行批量向量化
            EmbeddingResponse response = dashScopeEmbeddingModel.embedForResponse(batch);

            // 收集结果
            response.getResults().iterator().forEachRemaining(result -> {
                allEmbeddings.add(result.getOutput());
            });
            System.out.println("已完成: " + end + "/" + allTexts.size());
        }
        System.out.println("总向量数: " + allEmbeddings.size());
    }
}
