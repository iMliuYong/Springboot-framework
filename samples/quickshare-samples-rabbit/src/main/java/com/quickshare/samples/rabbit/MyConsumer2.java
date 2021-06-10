package com.quickshare.samples.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.Map;

/**
 * @author liu_ke
 */
public class MyConsumer2 implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message data, Channel channel) throws Exception{
        String queue = data.getMessageProperties().getConsumerQueue();
        Map<String,Object> headers = data.getMessageProperties().getHeaders();
    }
}
