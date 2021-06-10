package com.quickshare.common.messagecode.response;

import com.quickshare.common.bean.exception.BaseException;
import com.quickshare.common.messagecode.consts.MessageCode;

import static com.quickshare.common.messagecode.consts.MessageCode.VALUES;

/**
 * @author liu_ke
 */
public class CodedException extends BaseException {

    public CodedException(int code, Object... args){
        super(code,VALUES.getOrDefault(code, MessageCode.MESSAGE_SYS_UNKNOWN_CODE),args);
    }
}
