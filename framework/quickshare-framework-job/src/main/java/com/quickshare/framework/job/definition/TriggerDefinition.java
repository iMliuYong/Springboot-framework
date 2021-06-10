package com.quickshare.framework.job.definition;

import com.quickshare.common.json.JsonConvert;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author liu_ke
 */
public class TriggerDefinition {

    private String jobId;

    private String id;

    private String name;

    private String cron;

    private String memo;

    private String data;

    private Map<String,Object> dataMap;

    private boolean enabled;

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

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        if(!StringUtils.isEmpty(data)) {
            try {
                this.dataMap = JsonConvert.toObject(data, Map.class);
            }
            catch (Exception e){
                this.dataMap = null;
            }
        }
        else{
            this.dataMap = null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
        this.data = JsonConvert.toString(dataMap);
    }
}
