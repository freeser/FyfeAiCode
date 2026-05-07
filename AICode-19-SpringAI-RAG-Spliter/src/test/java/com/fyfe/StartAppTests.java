package com.fyfe;

import com.alibaba.cloud.ai.transformer.splitter.RecursiveCharacterTextSplitter;
import com.alibaba.cloud.ai.transformer.splitter.SentenceSplitter;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import java.util.List;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {
    @Test
    public void test01(@Value("classpath:files/云南南天前端面试（8年Vue3+TS）10页A4极简速记.pdf") Resource resource) {
        // 按照Token长度进行拆分，问题：造成语言割裂
        List<Document> documents = new PagePdfDocumentReader(resource).read();

        // 创建Token拆分器
        TokenTextSplitter build = new TokenTextSplitter(200,100,5,10000,true);
        List<Document> apply = build.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "======" + "拆分后的文档数" + apply.size());
        apply.forEach(System.out::println);
    }

    @Test
    public void test02(@Value("classpath:files/云南南天前端面试（8年Vue3+TS）10页A4极简速记.pdf") Resource resource) {
        List<Document> documents = new PagePdfDocumentReader(resource).read();

        // 创建语义化拆分器
        SentenceSplitter build = new SentenceSplitter(300);

        List<Document> apply = build.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "======" + "拆分后的文档数" + apply.size());
        apply.forEach(System.out::println);
    }

    @Test
    public void test03(@Value("classpath:files/云南南天前端面试（8年Vue3+TS）10页A4极简速记.pdf") Resource resource) {
        List<Document> documents = new PagePdfDocumentReader(resource).read();

        // 创建递归拆分器
        RecursiveCharacterTextSplitter build = new RecursiveCharacterTextSplitter();

        List<Document> apply = build.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "======" + "拆分后的文档数" + apply.size());
        apply.forEach(System.out::println);
    }
}
