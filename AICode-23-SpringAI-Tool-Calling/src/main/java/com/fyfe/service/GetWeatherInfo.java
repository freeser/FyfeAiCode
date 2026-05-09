package com.fyfe.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class GetWeatherInfo {

    // tool注解：标注一个方法为工具方法（在大模型分析用户的请求，来选择调用）
    // 属性：name 表示在大模型上注册的工具名称--大模型结构化数据中就是通过name值 来匹配需要调用哪个方法
    // 属性：description 对当前方法的一个描述

    // @ToolParam 注解：描述参数
    // description:对这个参数的含义做一个描述
    // 程序 在启动时，扫描到这些注解，根据注解将信息注册到大模型上
    // 用户性趣请求的时候--请求到大模型--大模型解析用户的意图
    // 匹配用户的意图与当前注册收录的工具方法
    // 分析这个方法需要什么参数，解析用户的请求中是否有对应的能数 信息
    // 将调用信息封闭为结构化数据 响应给本地程序，本地程序解析--调用执行，[底层走的是反射，方法要写成public]
    @Tool(name = "get_weather_info", description = "根据地市的名称获取当前城市的天气情况，注意：支持查询国内的天气")
    public String getWeatherInfo(@ToolParam(description = "要查询天气的城市名称（如：北京、上海、天津），如果用户没有明确说要查哪个城市的天气，默认查询北京的") String city) {
        return city +" 当前天气：晴，温度：20；风向：北风"; // 业务方法，模拟调用第三方接口获取天气
    }
}
