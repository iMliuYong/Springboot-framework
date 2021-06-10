package com.quickshare.common.bean.exception.sql;

import com.quickshare.common.bean.exception.BaseException;

/**
 * 数据主键重复错误
 */
public class DuplicateKeyException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DuplicateKeyException(Exception ex, int code, String msg, Object[] args) {
        super(ex, code, msg, args);
    }

    public DuplicateKeyException(Exception ex, String name,String value){
        super(ex, -12000, "%s[%s]已存在", name, value);
    }
}