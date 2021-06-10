package com.quickshare.samples.rabbit;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu_ke
 */
@Configuration
public class MyRabbitConfig {

    @Bean(name = "simpleMessageListenerContainer2")
    public SimpleMessageListenerContainer simpleMessageListenerContainer2(SimpleRabbitListenerContainerFactory factory){
        factory.setPrefetchCount(3);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        SimpleMessageListenerContainer container = factory.createListenerContainer();
        container.setExposeListenerChannel(true);
        container.addQueueNames("test.01.001");
        container.setMessageListener(new MyConsumer2());
        return container;
    }

    @Bean(name = "simpleMessageListenerContainer1")
    public SimpleMessageListenerContainer simpleMessageListenerContainer1(SimpleRabbitListenerContainerFactory factory){
        factory.setPrefetchCount(3);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        SimpleMessageListenerContainer container = factory.createListenerContainer();
        container.setExposeListenerChannel(true);
        container.addQueueNames("test.01.001");
        container.setMessageListener(new MyConsumer());
        return container;
    }
}
