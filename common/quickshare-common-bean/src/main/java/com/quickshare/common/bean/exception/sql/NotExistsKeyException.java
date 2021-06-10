package com.quickshare.common.bean.exception.sql;

import com.quickshare.common.bean.exception.BaseException;

/**
 * 数据主键不存在错误
 */
public class NotExistsKeyException extends BaseException {

    private static final long serialVersionUID = 1L;

    public NotExistsKeyException(Exception ex, int code, String msg, Object[] args) {
        super(ex, code, msg, args);
    }

    public NotExistsKeyException(String name,String value){
        super(-20003, "%s[%s]不存在", name, value);
    }
}