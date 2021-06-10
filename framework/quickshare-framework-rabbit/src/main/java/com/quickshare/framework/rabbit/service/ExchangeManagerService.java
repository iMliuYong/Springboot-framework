package com.quickshare.framework.rabbit.service;

import com.quickshare.common.utils.CmdUtil;
import com.quickshare.common.utils.ExecResult;
import com.quickshare.framework.rabbit.core.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnProperty(name = "spring.rabbitmq.host")
public class ExchangeManagerService {

    private final RabbitAdmin rabbitAdmin;

    public ExchangeManagerService(RabbitAdmin rabbitAdmin){
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createExchange(String name){
        rabbitAdmin.declareExchange(new DirectExchange(name));
    }

    public void createQueue(String name){
        rabbitAdmin.declareQueue(new Queue(name,true,false,false));
    }

    public void createDelayQueue(String queueName,String deadExchangeName,int delayMinutes,String deadRoutingKey){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", deadExchangeName);
        arguments.put("x-message-ttl", delayMinutes * 60 * 1000);
        arguments.put("x-dead-letter-routing-key", deadRoutingKey);
        rabbitAdmin.declareQueue(new Queue(queueName,true,false,false,arguments));
    }

    public void binding(String exchangeName,String queueName){
        rabbitAdmin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, queueName, null));
    }
}
