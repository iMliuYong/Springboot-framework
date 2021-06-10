package com.quickshare.framework.job.definition;

import java.util.List;

/**
 * job定义
 * @author wangy
 */
public class JobDefinition {

    private String id;

    private String name;

    private String memo;

    private boolean enabled;

    private List<TriggerDefinition> triggers;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<TriggerDefinition> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerDefinition> triggers) {
        this.triggers = triggers;
    }
}
