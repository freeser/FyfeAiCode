package com.fyfe.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.fyfe.common.Order;
import com.fyfe.common.Product;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 输出模型的转换器配置
// 配置调用大模型时，如果指定了输出那种类型的数据
// 大模型会自动的输出对应的类型
// 本地调用对应转换器进行数据的转换
@Configuration
public class StructuredOutputConfig {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    // 1.为Product创建专用的Converter子类(显式绑定泛型)
    public static class ProductOutputConverter extends BeanOutputConverter<Product> {
        public ProductOutputConverter() {
            super(Product.class);
        }
    }
    // 2.为0rder创建专用的Converter子类
    public static class OrderOutputConverter extends BeanOutputConverter<Order> {
        public OrderOutputConverter() {
            super(Order.class);
        }
    }

    @Bean
    public BeanOutputConverter<Product> productOutputConverter() {
        return new ProductOutputConverter();
    }

    @Bean
    public BeanOutputConverter<Order> orderOutputConverter() {
        return new OrderOutputConverter();
    }
}
