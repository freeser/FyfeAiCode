package com.fyfe.config;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import io.milvus.client.MilvusServiceClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// RAG检索增强配置类
@Configuration
public class VectorStoreConfig {
    // 注入必要的Bean组件
    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;  // 阿里云Embedding模型

    @Autowired
    private MilvusServiceClient milvusServiceClient;           // Milvus客户端

    @Autowired
    private DashScopeChatModel dashScopeChatModel;             // 阿里云Chat模型

    @Autowired
    private VectorStore milvusVectorStore;                     // 向量存储

    @Autowired
    private DashScopeRerankModel rerankModel;

    // 配置VectorStore Bean
    @Bean
    public VectorStore milvusVectorStore() {
        return MilvusVectorStore.builder(milvusServiceClient, dashScopeEmbeddingModel).build();
    }

    // 配置ChatClient Bean
    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(dashScopeChatModel) // 客户端配置向量数据库的查询拦截器，首先正式向大模型发起调用的时候，首先会先去向量数据库中查询对应的文档片段
                // 取出相似度90%以上的文本然后再打包一块发送给大模型
                .defaultAdvisors(
                        RetrievalAugmentationAdvisor.builder()
                                .documentRetriever(
                                        VectorStoreDocumentRetriever.builder()
                                                .similarityThreshold(.5)  // 设置较高的相似度阈值
                                                .vectorStore(milvusVectorStore)
                                                .build()
                                )
                                .queryAugmenter(
                                        ContextualQueryAugmenter.builder()
                                                .allowEmptyContext(false) // 不允许上下文为空
                                                .emptyContextPromptTemplate(
                                                        PromptTemplate.builder()
                                                                .template("根据您的问题，系统未能找到相关的文档信息。为了更好地帮助您，请提供更多详细信息或尝试重新表述您的问题。")
                                                                .build()
                                                )
                                                .build()
                                )

                                // 配置重新增强器，将系统从RAG数据库中召回的数据进行综合的规整
                                .queryTransformers(
                                        RewriteQueryTransformer.builder()
                                                .chatClientBuilder(ChatClient.builder(dashScopeChatModel))
                                                // 设置系统提示，指导LLM如何重写查询
                                                .targetSearchSystem("你是一个词汇清理的专家，主要工作是将用户的模糊问题提取出专业的词汇，以提高向量检索的精度，注意不要有任何多余的解释")
                                                .build()
                                )
                                .build(),
                        new RetrievalRerankAdvisor(
                                milvusVectorStore,
                                rerankModel, // 对从向量数据库中召回的数据，进行重新编排【重新做相似度匹配】
                                SearchRequest.builder()
                                        .topK(200) // 给向量中Top200个结果，给到重排序大模型
                                        .similarityThreshold(.4)// 200的相关性必需大于0.4
                                        .build()
                        )
                )
                .build();
    }
}