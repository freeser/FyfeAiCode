package com.fyfe.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具调用结果缓存类
 *
 * 使用ConcurrentHashMap的优势：
 * - 线程安全，支持高并发读写
 * - 无需额外同步措施
 * - 性能优于 synchronizedMap
 */
public class InfoCache {
    /**
     * 天气查询缓存
     * Key: 城市名称（如"北京"、"上海"）
     * Value: 天气查询结果（如"北京 当前天气：晴，温度26℃"）
     */
    public static Map<String, String> weatherCache = new ConcurrentHashMap<>();
}