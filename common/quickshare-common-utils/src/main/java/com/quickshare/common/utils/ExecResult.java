package com.quickshare.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liu_ke
 */
public class ExecResult {

    /**
     * 0
     */
    private int code = -1;

    private String error;

    private List<String> output;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        if(!StringUtils.isEmpty(error)){
            code = -1;
        }
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }
}
