package com.quickshare.framework.ws.config;

import com.quickshare.framework.ws.PasswordType;
import com.quickshare.framework.ws.WebServiceUtil;
import com.quickshare.framework.ws.WsSecurityService;
import com.quickshare.framework.ws.impl.DefaultServerCallbackHandler;
import com.quickshare.framework.ws.impl.FileWsSecurityServiceImpl;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.spring.boot.autoconfigure.CxfProperties;
import org.apache.cxf.ws.policy.WSPolicyFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.callback.CallbackHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
@Configuration
@ConditionalOnClass(CxfProperties.class)
public class WsSecurityConfig {

    @Bean
    @ConditionalOnMissingBean(WsSecurityService.class)
    @ConfigurationProperties(prefix = "security.users")
    public Map<String,String> users(){
        return new HashMap<>();
    }

    @Bean
    @ConditionalOnMissingBean(WsSecurityService.class)
    public WsSecurityService createDefaultWsSecurityService(){
        return new FileWsSecurityServiceImpl(users());
    }

    @Bean
    public CallbackHandler defaultCallbackHandler(){
        return new DefaultServerCallbackHandler();
    }

    @Bean
    public WSPolicyFeature policyFeature(){
        WSPolicyFeature policyFeature = new WSPolicyFeature();

        return policyFeature;
    }

    @Bean
    public SAAJInInterceptor saajInInterceptor(){
        return new SAAJInInterceptor();
    }

    @Bean
    public WSS4JInInterceptor wss4JInInterceptor(CallbackHandler callbackHandler){
        return WebServiceUtil.createWSS4JInInterceptor(PasswordType.DIGEST,callbackHandler);
    }
}
