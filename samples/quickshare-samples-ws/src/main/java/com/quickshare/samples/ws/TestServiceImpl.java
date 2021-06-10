package com.quickshare.samples.ws;

import com.quickshare.framework.ws.WebService;
import com.quickshare.framework.ws.WebServiceInfo;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 * @author liu_ke
 */
@WebServiceInfo(serverPath = "test",usePolicy = false)
@javax.jws.WebService(serviceName = "HIPService", targetNamespace = "urn:hl7-org:v3")
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class TestServiceImpl extends WebService {

    @WebMethod(operationName = "HIPMessageServer")
    @WebResult(name = "payload")
    public String sayHello(String name){
        return "Hello:"+name;
    }
}
