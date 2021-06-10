package com.quickshare.framework.datasource.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据源配置
 * 
 * @author liu_ke
 */
@ConfigurationProperties
public class DataSourceProperty {

    private String url;

    private String username;

    private String password;

    private String slaveUrl;

    private String slaveUsername;

    private String slavePassword;

    private String driverclassname;

    private String splitkey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverclassname() {
        return driverclassname;
    }

    public void setDriverclassname(String driverclassname) {
        this.driverclassname = driverclassname;
    }

    public String getSplitkey() {
        return splitkey;
    }

    public void setSplitkey(String splitkey) {
        this.splitkey = splitkey;
    }

    public String getSlaveUrl() {
        return slaveUrl;
    }

    public void setSlaveUrl(String slaveUrl) {
        this.slaveUrl = slaveUrl;
    }

    public String getSlaveUsername() {
        return slaveUsername;
    }

    public void setSlaveUsername(String slaveUsername) {
        this.slaveUsername = slaveUsername;
    }

    public String getSlavePassword() {
        return slavePassword;
    }

    public void setSlavePassword(String slavePassword) {
        this.slavePassword = slavePassword;
    }
}
