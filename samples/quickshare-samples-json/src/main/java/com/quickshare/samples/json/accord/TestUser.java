package com.quickshare.samples.json.accord;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.annotation.ManagedBean;

/**
 * @author liu_ke
 */
@ManagedBean
@Scope(value = "request",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TestUser {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
