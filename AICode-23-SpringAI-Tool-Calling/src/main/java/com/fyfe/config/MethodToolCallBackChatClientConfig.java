package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.fyfe.service.GetCurrentTime;
import com.fyfe.service.GetWeatherInfo;
import jakarta.annotation.Resource;
import org.apache.el.util.ReflectionUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Configuration
public class MethodToolCallBackChatClientConfig {

    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @Resource
    private GetCurrentTime getCurrentTime;

    @Resource
    private GetWeatherInfo getWeatherInfo;;

    @Bean(value = "callBackChatClient")
    public ChatClient getChatClient() {
        Method getCurrentDateTime = ReflectionUtils.findMethod(
                GetCurrentTime.class,
                "getCurrentDateTime"
        );

        MethodToolCallback callbackMethod = null;

        if (getCurrentDateTime != null) {
            callbackMethod = MethodToolCallback.builder()
                    .toolDefinition(
                            ToolDefinitions.builder(getCurrentDateTime)
                                    .name("get_current_time")
                                    .description("获取系统的当前时间")
                                    .build()
                    )
                    .toolMethod(getCurrentDateTime)
                    .toolObject(getCurrentTime)
                    .build();
        }
        return ChatClient.builder(dashScopeChatModel)
                .defaultToolCallbacks(callbackMethod)
                .defaultTools(getWeatherInfo)
                .build();
    }
}
