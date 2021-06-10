package com.quickshare.samples.ws;

import com.quickshare.framework.ws.WebService;
import com.quickshare.framework.ws.WebServiceInfo;

/**
 * @author liu_ke
 */
@WebServiceInfo(serverPath = "test2")
@javax.jws.WebService(targetNamespace = "www.quickshare.com.cn")
public class Test2ServiceImpl extends WebService {

    public String sayHello(String name){
        return "Hello:"+name;
    }
}
