package com.quickshare.framework.datasource.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * app配置
 * 
 * @author liu_ke
 */
@ConfigurationProperties(prefix = "")
public class AppsProperty {

    private final Map<String, AppProperty> app = new HashMap<>();

    public Map<String, AppProperty> getApp() {
        return app;
    }
}