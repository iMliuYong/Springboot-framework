package com.quickshare.framework.ws.impl;

import com.quickshare.common.encrytor.CodeValueCipher;
import com.quickshare.framework.ws.WsSecurityService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
public class FileWsSecurityServiceImpl implements WsSecurityService {

    private final Map<String,String> users;

    public FileWsSecurityServiceImpl(Map<String,String> users){
        for (Map.Entry<String,String> user:users.entrySet()) {
            user.setValue(CodeValueCipher.decrypt(user.getKey(),user.getValue()));
        }
        this.users = users;
    }

    @Override
    public String getPwd(String userCode) {
        return users.getOrDefault(userCode,null);
    }
}
