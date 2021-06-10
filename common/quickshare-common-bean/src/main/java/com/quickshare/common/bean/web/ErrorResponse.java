package com.quickshare.common.bean.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.quickshare.common.bean.exception.BaseException;

/**
 * ErrorResponse
 */
public class ErrorResponse extends BaseResponse<String>{

    public ErrorResponse(Throwable throwable){
        this.setCode(1);
        this.setMessage(throwable.getMessage());

        if(BaseException.class.isInstance(throwable)){
            this.setCode(((BaseException)throwable).getCode());
        }
        Throwable baseException = null;
        while (throwable != null){
            throwable = throwable.getCause();
            if(throwable != null){
                if(throwable instanceof BaseException){
                    break;
                }
                baseException = throwable;
            }
        }
        if(baseException != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            baseException.printStackTrace(pw);
            this.detailMessage = sw.toString();
        }
    }

    /**
     * 错误详情
     */
    private String detailMessage;

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    
}