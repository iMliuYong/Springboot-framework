package com.quickshare.framework.core.impl;

import com.quickshare.framework.core.service.AppInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用启动器
 * @author liu_ke
 */
@Component
public class ApplicationStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private List<AppInitializer> initializers;

    private static Logger log = LoggerFactory.getLogger("default");

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent()==null){
            init();
        }
    }

    private void init(){
        if(initializers == null || initializers.isEmpty()){
            return;
        }
        for (AppInitializer initializer: initializers) {
            try {
                initializer.init();
            }
            catch (Exception e){
                log.error("初始化应用遇到问题",e);
            }
        }
    }
}
