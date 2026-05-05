package fyfe.code.demo01;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class TestDashscope {
    public static void main(String[] args) {
        QwenChatModel build = QwenChatModel.builder()
                .apiKey("sk-8de8b8e9f68048b18bf58c582b0cd2cc")
//                .modelName("deepseek-v4-pro")
                // qwen3.6-plus 无法使用
                .modelName("qwen-plus")
                .build();
        String msg = build.chat("你好，今天北京的天气如何");
        System.out.println("北京的天气: " + msg);
    }
}
