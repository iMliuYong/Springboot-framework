package com.quickshare.framework.mongodb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * app配置
 * @author liu_ke
 */
public class AppsProperty {

    private final Map<String, AppProperty> app = new HashMap<>();

    public Map<String, AppProperty> getApp() {
        return app;
    }
}