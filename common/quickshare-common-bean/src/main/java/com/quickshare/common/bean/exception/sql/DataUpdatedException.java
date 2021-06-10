package com.quickshare.common.bean.exception.sql;

import com.quickshare.common.bean.exception.BaseException;

/**
 * DataUpdatedException
 */
public class DataUpdatedException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DataUpdatedException(Exception ex, int code, String msg, Object[] args) {
        super(ex, code, msg, args);
    }

    public DataUpdatedException(String name,String value){
        super(-20000, "%s[%s]已更新，请刷新后再次操作", name, value);
    }
}