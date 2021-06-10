package com.quickshare.framework.ws.impl;

import com.quickshare.common.encrytor.CodeValueCipher;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

/**
 * 客户端回调
 * @author liu_ke
 */
public class DefaultClientCallbackHandler implements CallbackHandler {

    private final String userCode;

    private final String userPwd;

    public DefaultClientCallbackHandler(String userCode, String encryptorPwd){
        this.userCode = userCode;
        this.userPwd = CodeValueCipher.decrypt(userCode,encryptorPwd);
    }

    @Override
    public void handle(Callback[] callbacks){
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            String identifier = pc.getIdentifier();
            if(userCode.equals(identifier)){
                pc.setPassword(userPwd);
            }
        }
    }
}
