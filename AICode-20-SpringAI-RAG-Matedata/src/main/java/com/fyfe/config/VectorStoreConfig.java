package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import io.milvus.client.MilvusServiceClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {
    // 将百炼大模型的向量处理模型与向量数据库的操作模型进行绑定

    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;

    @Autowired
    private MilvusServiceClient milvusServiceClient;

    public VectorStore milvusVectorStore() {
        return MilvusVectorStore.builder(milvusServiceClient, dashScopeEmbeddingModel).build();
    }
}
