package com.quickshare.framework.mongodb.dynamic;

import java.util.*;

/**
 * @author liu_ke
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private static String defaultKey;

    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    private final static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    /**
     * 获取数据源
     * @return
     */
    public static String getDataSourceKey() {
        String key = contextHolder.get();
        if(key == null || "".equals(key)){
            key = defaultKey;
        }
        return key;
    }

    /**
     * 重置数据源
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    /**
     * 判断是否包含数据源
     * @param key 数据源key
     * @return
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }

    /**
     * 添加数据源keys
     * @param keys
     * @return
     */
    public static boolean addDataSourceKeys(String defaultKey,Collection<? extends Object> keys) {
        DynamicDataSourceContextHolder.defaultKey = defaultKey;
        return dataSourceKeys.addAll(keys);
    }
}
