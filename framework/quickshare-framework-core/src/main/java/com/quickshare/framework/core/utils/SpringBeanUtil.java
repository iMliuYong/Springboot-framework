package com.quickshare.framework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * SpringBean辅助工具类
 * @author liu_ke
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext ctx = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}