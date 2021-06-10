package com.quickshare.framework.ws;

import com.quickshare.framework.ws.impl.DefaultServerCallbackHandler;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import javax.security.auth.callback.CallbackHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * webservice配置信息
 * @author liu_ke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface WebServiceInfo {

    /**
     * 是否安全认证
     * @return
     */
    boolean usePolicy() default true;

    Class<? extends CallbackHandler> callbackHandler() default DefaultServerCallbackHandler.class;

    /**
     * 服务路径
     * @return
     */
    String serverPath();
}
