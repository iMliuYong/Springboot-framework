package com.quickshare.common.bean.exception;

/**
 * 基础错误
 */
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BaseException(String msg){
        super(msg);
        this.code = -10000;
    }

    public BaseException(String msg,Throwable ex){
        super(msg,ex);
        this.code = -10000;
    }

    public BaseException(int code, String msg, Object... args){
        super(String.format(msg, args));
        this.code = code;
    }

    public BaseException(Throwable ex,int code, String msg, Object... args){
        super(String.format(msg, args), ex);
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}