package com.quickshare.common.messagecode.response;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.common.messagecode.consts.MessageCode;

import static com.quickshare.common.messagecode.consts.MessageCode.VALUES;

/**
 * @author liu_ke
 *
 */
public class CodedResponse<T> extends BaseResponse<T> {

    public CodedResponse(int code,T data){
        super(code,VALUES.getOrDefault(code, MessageCode.MESSAGE_SYS_UNKNOWN_CODE),data);
    }
}
