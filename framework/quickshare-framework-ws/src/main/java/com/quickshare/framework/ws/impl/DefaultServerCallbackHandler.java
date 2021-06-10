package com.quickshare.framework.ws.impl;

import com.quickshare.framework.ws.WsSecurityService;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * @author liu_ke
 */
public class DefaultServerCallbackHandler implements CallbackHandler {

    @Autowired
    private WsSecurityService wsSecurityService;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            String identifier = pc.getIdentifier();
            pc.setPassword(wsSecurityService.getPwd(identifier));
        }
    }
}
