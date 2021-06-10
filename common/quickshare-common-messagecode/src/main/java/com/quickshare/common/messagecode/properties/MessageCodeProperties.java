package com.quickshare.common.messagecode.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
@Component
@PropertySource(value= {"messagecode.properties"},ignoreResourceNotFound=true,encoding="UTF-8",name="messagecode.properties")
@PropertySource(value= {"internalmessagecode.properties"},ignoreResourceNotFound=true,encoding="UTF-8",name="internalmessagecode.properties")
@ConfigurationProperties(prefix = "")
public class MessageCodeProperties {

    private final Map<Integer,String> messagecode = new HashMap<>();

    public Map<Integer, String> getMessagecode() {
        return messagecode;
    }
}
