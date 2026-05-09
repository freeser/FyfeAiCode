package com.fyfe;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.fyfe.common.Product;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;


@SpringBootTest(classes = StartApp.class)
public class StartAppTests {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    @Autowired
    private BeanOutputConverter<Product> productOutputConverter;

    @Test
    public void test01() {
        // 方式一：手动构建提示词（不推荐）
        // String prompt = "推荐一款电子产品，返回JSON格式：{\"name\": \"...\", \"price\": ..., \"category\": \"...\"}";

        // 方式二：使用BeanOutputConverter自动生成格式指令（推荐）
        // getFormat()方法返回根据Product结构自动生成的JSON Schema指令
        String format = productOutputConverter.getFormat();

        String prompt = """
                请根据用户需求推荐一款合适的商品。
                用户需求：需要一款适合办公使用的电子设备
                
                请按照以下JSON格式返回结果：
                %s
                """.formatted(format);

        // 调用大模型
        ChatResponse response = dashScopeChatModel.call(
                new Prompt(prompt)
        );

        // 获取模型输出的JSON字符串
        String jsonContent = response.getResult().getOutput().getText();
        System.out.println("模型输出的JSON：" + jsonContent);

        // 使用BeanOutputConverter将JSON转换为Product对象
        // 内部使用Jackson进行JSON反序列化
        Product product = null;
        if (jsonContent != null) {
            product = productOutputConverter.convert(jsonContent);
        }

        // 输出转换后的对象
        System.out.println("转换后的Product对象：" + product);
        if (product != null) {
            System.out.println("商品名称：" + product.getName());
            System.out.println("商品价格：" + product.getPrice());
            System.out.println("商品分类：" + product.getCategory());
        }
    }

    /**
     * 使用ChatClient实现结构化输出
     * 更加简洁的API设计
     */
    @Test
    public void testChatClientOutput() {
        // 创建ChatClient
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();

        // 直接指定输出类型为Product
        // Spring AI会自动处理格式指令和转换
        Product product = chatClient.prompt()
                .user(u -> u.text("推荐一款适合游戏的电脑"))
//                .advisors(new PrinterAroundAdvisor())  // 打印中间结果（可选）
                .call()
                .entity(Product.class);  // 指定输出类型

        if (product != null) {
            System.out.println("商品名称：" + product.getName());
            System.out.println("商品价格：" + product.getPrice());
        }
    }

    /**
     * 使用ChatClient输出List类型
     */
    @Test
    public void testChatClientListOutput() {
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();

        // 使用ParameterizedTypeReference指定泛型类型
        List<Product> products = chatClient.prompt()
                .user("推荐3款无线蓝牙耳机")
                .call()
                .entity(new ParameterizedTypeReference<List<Product>>() {});

        if (products != null) {
            products.forEach(System.out::println);
        }
    }
}
