package com.quickshare.framework.environment.dto;

/**
 * 环境信息
 * @author liu_ke
 */
public class ProfileInfo {

    private String app;

    private String id;

    private String name;

    /**
     * 克隆来源ID
     */
    private String sourceId;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
