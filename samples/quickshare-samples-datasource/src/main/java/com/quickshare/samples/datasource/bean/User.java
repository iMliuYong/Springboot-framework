package com.quickshare.samples.datasource.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.annotation.ManagedBean;

/**
 * @author liu_ke
 */
@ManagedBean
@Scope(value = "request",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class User {

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
