package com.quickshare.framework.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Iterator;

/**
 * @author liu_ke
 */
public class DataSourceCondition implements ConfigurationCondition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        MutablePropertySources sources = ((ConfigurableEnvironment)conditionContext.getEnvironment()).getPropertySources();
        Iterator<PropertySource<?>> is = sources.iterator();
        boolean containsUrl = false;
        while (is.hasNext()){
            PropertySource ps = is.next();
            if(ps instanceof OriginTrackedMapPropertySource){
                for (String key:((OriginTrackedMapPropertySource)ps).getSource().keySet()) {
                    if(key.endsWith("mongo.host")){
                        containsUrl = true;
                        break;
                    }
                }
            }
            else if(ps instanceof PropertiesPropertySource){
                for (String key: ((PropertiesPropertySource)ps).getSource().keySet()) {
                    if(key.endsWith("mongo.host")){
                        containsUrl = true;
                        break;
                    }
                }
            }

            if(containsUrl){
                break;
            }
        }
        return containsUrl;
    }

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
