package com.fyfe;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import java.util.List;

@SpringBootTest(classes = StartApp.class)
public class StartAppTests {
    @Test
    public void test01(@Value("classpath:files/云南南天前端面试（8年Vue3+TS）10页A4极简速记.pdf") Resource resource) {
        // 采用PDF读取器读取对应的PDF文件
        List<Document> read = new PagePdfDocumentReader(resource).read();
        for (Document document : read) {
            System.out.println(document);
        }
    }

    @Test
    public void test02(@Value("classpath:files/投诉举报工单管理系统的设计与实现.docx") Resource resource) {
        // 采用PDF读取器读取对应的PDF文件
        List<Document> documents = new TikaDocumentReader(resource).read();
        documents.forEach(System.out::println);
    }
}
