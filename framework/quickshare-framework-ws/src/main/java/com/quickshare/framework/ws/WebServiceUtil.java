package com.quickshare.framework.ws;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.security.auth.callback.CallbackHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * webservice工具类
 * @author liu_ke
 */
public class WebServiceUtil {

    public static WSS4JInInterceptor createWSS4JInInterceptor(PasswordType passwordType,CallbackHandler callbackHandler){
        Map<String, Object> inProps = new HashMap<>();
        //设置加密类型
        inProps.put(WSHandlerConstants.ACTION,WSHandlerConstants.USERNAME_TOKEN);
        //设置密码类型为加密
        if(passwordType == PasswordType.TEXT) {
            inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PASSWORD_TEXT);
        }
        else{
            inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PASSWORD_DIGEST);
        }
        //密码回调函数
        inProps.put(WSHandlerConstants.PW_CALLBACK_REF, callbackHandler);
        return new WSS4JInInterceptor(inProps);
    }

    public static WSS4JOutInterceptor createWSS4JOutInterceptor(String userCode, PasswordType passwordType, CallbackHandler callbackHandler){
        Map<String, Object> outProps = new HashMap<>();
        //设置加密类型
        outProps.put(WSHandlerConstants.ACTION,WSHandlerConstants.USERNAME_TOKEN);
        //设置密码类型为加密
        if(passwordType == PasswordType.TEXT) {
            outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        }
        else{
            outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
        }
        //密码回调函数
        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callbackHandler);
        if(StringUtils.isNotEmpty(userCode)){
            outProps.put(WSHandlerConstants.USER,userCode);
        }
        return new WSS4JOutInterceptor(outProps);
    }
}
