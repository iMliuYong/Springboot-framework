package com.quickshare.framework.ws;

import com.quickshare.framework.ws.impl.DefaultClientCallbackHandler;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

/**
 * @author liu_ke
 */
public class WebServiceClient {

    private final Client client;

    public WebServiceClient(String wsdl){
        this(wsdl,false,null,null,null);
    }


    public WebServiceClient(String wsdl, boolean usePolicy, String userCode, String userPwd, PasswordType passwordType){
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient(wsdl);
        if(usePolicy){
            WSS4JOutInterceptor wss4JOutInterceptor = WebServiceUtil.createWSS4JOutInterceptor(userCode,passwordType,new DefaultClientCallbackHandler(userCode,userPwd));
            client.getOutInterceptors().add(new SAAJOutInterceptor());
            client.getOutInterceptors().add(wss4JOutInterceptor);
        }
    }

    public String invoke(String method,Object... objects) throws Exception {
        Object[] result = client.invoke(method,objects);
        if(result == null || result.length == 0){
            return null;
        }
        return result[0].toString();
    }
}
