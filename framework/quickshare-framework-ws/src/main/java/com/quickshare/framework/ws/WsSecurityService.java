package com.quickshare.framework.ws;

/**
 * WebService 安全认证服务
 * @author liu_ke
 */
public interface WsSecurityService {

    /**
     * 获取密码
     * @param userCode 账号代码
     * @return
     */
    String getPwd(String userCode);
}
