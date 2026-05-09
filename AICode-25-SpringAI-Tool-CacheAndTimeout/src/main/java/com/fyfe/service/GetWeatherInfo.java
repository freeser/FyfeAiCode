package com.fyfe.service;

import com.fyfe.cache.InfoCache;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class GetWeatherInfo {

    /**
     * 天气查询工具
     * 使用@Tool注解声明，供AI模型调用
     *
     * 说明：
     * - Thread.sleep(5000L) 模拟外部API的响应延迟
     * - 实际生产中应替换为真实的HTTP调用
     */
//    @Tool(name = "get_weather_by_annotation", description = "根据城市名称查询当前天气")
//    public String getWeather(
//            @ToolParam(description = "要查询的城市名称（如北京、上海）") String city)
//            throws InterruptedException {
//        // 模拟外部API调用延迟（实际生产中为真实HTTP请求）
//        Thread.sleep(5000L);
//        return String.format("【注解方式】%s 当前天气：晴，温度26℃，湿度60%%", city);
//    }

    /**
     * 带缓存的天气查询工具
     *
     * 缓存逻辑说明：
     * 1. 先从缓存中查询是否已有结果
     * 2. 如果缓存命中，直接返回缓存结果（避免重复调用外部API）
     * 3. 如果缓存未命中，调用外部API获取结果
     * 4. 将结果存入缓存后返回
     *
     * 适用场景：
     * - 天气数据更新频率较低（小时级/天级）
     * - 同一城市会被多次查询
     */
//    @Tool(name = "get_weather_by_annotation", description = "根据城市名称查询当前天气")
//    public String getWeather(@ToolParam(description = "要查询的城市名称（如北京、上海）") String city) throws InterruptedException {
//
//        // 1. 先查询缓存
//        String cachedResult = InfoCache.weatherCache.get(city);
//
//        // 2. 缓存命中，直接返回
//        if (cachedResult != null && !cachedResult.isEmpty()) {
//            System.out.println("【缓存命中】直接从缓存返回结果");
//            return cachedResult;
//        }
//
//        // 3. 缓存未命中，调用外部API
//        System.out.println("【缓存未命中】调用外部API获取数据");
//
//        // 模拟外部API调用（实际生产中为真实HTTP请求）
//        String result = String.format("【注解方式】%s 当前天气：晴，温度26℃，湿度60%%", city);
//        Thread.sleep(5000L);
//
//        // 4. 将结果存入缓存
//        InfoCache.weatherCache.put(city, result);
//
//        return result;
//    }


    /**
     * 带超时控制的天气查询工具
     *
     * 实现原理：
     * 1. 使用 CompletableFuture.supplyAsync() 异步执行工具逻辑
     * 2. .orTimeout(3, TimeUnit.SECONDS) 设置3秒超时
     * 3. .exceptionally() 处理超时异常，返回友好提示
     * 4. .join() 阻塞等待结果
     *
     * 配置说明：
     * - 超时时间应根据外部API的平均响应时间设置
     * - 通常设置为平均响应时间的2-3倍
     */
    @Tool(name = "get_weather_by_annotation", description = "根据城市名称查询当前天气")
    public String getWeather(@ToolParam(description = "要查询的城市名称（如北京、上海）") String city) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
                    // ===== 异步执行的工具逻辑 =====

                    // 1. 尝试从缓存获取结果
                    String cachedResult = InfoCache.weatherCache.get(city);
                    if (cachedResult != null && !cachedResult.isEmpty()) {
                        return cachedResult;
                    }

                    // 2. 模拟外部API调用（5秒）
                    String result = String.format("【注解方式】%s 当前天气：晴，温度26℃，湿度60%%", city);
                    try {
                        Thread.sleep(5000L);  // 模拟5秒延迟
                    } catch (InterruptedException e) {
                        // 处理线程中断
                        Thread.currentThread().interrupt();
                    }

                    // 3. 存入缓存
                    InfoCache.weatherCache.put(city, result);

                    return result;

                })
                // 设置3秒超时（如果3秒内未完成则抛出TimeoutException）
                .orTimeout(3, TimeUnit.SECONDS)

                // 超时异常处理，返回友好提示
                .exceptionally(e -> "天气查询超时，请稍后重试")

                // 阻塞等待最终结果
                .join();
    }

}
