package com.quickshare.framework.json.config;

import com.quickshare.framework.json.handler.DynamicFieldReturnValueHandler;
import com.quickshare.framework.json.serializer.SerializerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_ke
 */
@Configuration
@ConditionalOnProperty(name = "quickshare.json.serialize.dynamicfield.enabled",havingValue = "true")
public class InitializingHandler implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Bean
    public DynamicFieldReturnValueHandler dynamicFieldReturnValueHandler(SerializerFactory factory,
                                                                         RequestResponseBodyMethodProcessor processor){
        return new DynamicFieldReturnValueHandler(factory, processor);
    }

    @Bean
    public SerializerFactory factory(){
        return new SerializerFactory();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        this.decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                SerializerFactory factory = factory();
                factory.init();
                DynamicFieldReturnValueHandler decorator = dynamicFieldReturnValueHandler(factory,
                        (RequestResponseBodyMethodProcessor)handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }
}
