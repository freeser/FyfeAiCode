package fyfe.code.demo01;

import dev.langchain4j.model.openai.OpenAiChatModel;

public class TestDeepSeek {
    public static void main(String[] args) {
        //演示通过langchain4j调用deepseek模型//DeepSeek API使用与 OpenAI 兼容的 API格式，通过修改配置，// 您可以使用 OpenAI SDK 来访问 DeepSeek API，或使用与 OpenAI API 兼容的软件。
        //ChatModel ChatClient
        //ChatClient客户端 发起调用的
        //ChatModel约束 约束不同模型统一API入口
        //ChatModel只是一个接口 不同的模型 有不同的实现类
        OpenAiChatModel build = OpenAiChatModel.builder()
                .apiKey("sk-ca0b2dc9e0ca461cab4ca0dbd62c841b")
                .baseUrl("https://api.deepseek.com")
                .modelName("deepseek-chat")
                .build();

        // 通过chatmodel封闭的chat方法进行调用
        String msg = build.chat("你好，今天是多少度");
        System.out.println("今天是多少度：" + msg);
    }
}
