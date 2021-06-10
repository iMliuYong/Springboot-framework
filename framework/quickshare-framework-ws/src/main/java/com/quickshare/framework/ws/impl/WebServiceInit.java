package com.quickshare.framework.ws.impl;

import com.quickshare.framework.core.service.AppInitializer;
import com.quickshare.framework.ws.WebService;
import com.quickshare.framework.ws.WebServiceInfo;
import org.apache.cxf.Bus;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.spring.boot.autoconfigure.CxfProperties;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liu_ke
 */
@Component
@ConditionalOnClass(CxfProperties.class)
public class WebServiceInit implements AppInitializer {

    @Autowired(required = false)
    List<WebService> serviceList;

    @Autowired(required = false)
    private Bus bus;

    @Autowired
    private SAAJInInterceptor saajInInterceptor;

    @Autowired
    private WSS4JInInterceptor wss4JInInterceptor;

    @Override
    public void init() throws Exception {
        if(serviceList == null || serviceList.isEmpty()){
            return;
        }

        if(bus == null){
            return;
        }

        for (WebService webServie: serviceList) {
            EndpointImpl ep = new EndpointImpl(bus,webServie);
            WebServiceInfo info = webServie.getClass().getAnnotation(WebServiceInfo.class);
            if(info.usePolicy()){
                ep.getInInterceptors().add(saajInInterceptor);
                ep.getInInterceptors().add(wss4JInInterceptor);
            }
            String serverPath = info.serverPath().startsWith("/")?info.serverPath():"/"+info.serverPath();
            ep.publish(serverPath);
        }
    }
}
