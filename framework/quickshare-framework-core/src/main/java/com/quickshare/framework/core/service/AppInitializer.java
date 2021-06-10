package com.quickshare.framework.core.service;

/**
 * 应用初始化
 * @author liu_ke
 */
public interface AppInitializer {

    /**
     * 初始化任务
     * @throws Exception 异常
     */
    void init() throws Exception;
}
