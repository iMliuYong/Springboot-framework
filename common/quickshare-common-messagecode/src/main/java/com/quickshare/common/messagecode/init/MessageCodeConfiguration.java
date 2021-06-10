package com.quickshare.common.messagecode.init;

import com.quickshare.common.messagecode.properties.MessageCodeProperties;
import org.springframework.context.annotation.Configuration;

import static com.quickshare.common.messagecode.consts.MessageCode.VALUES;

/**
 * @author liu_ke
 */
@Configuration
public class MessageCodeConfiguration{

    public MessageCodeConfiguration(MessageCodeProperties properties){
        VALUES.putAll(properties.getMessagecode());
    }
}
