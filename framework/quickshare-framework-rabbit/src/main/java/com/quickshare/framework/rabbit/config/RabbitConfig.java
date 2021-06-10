package com.quickshare.framework.rabbit.config;

import com.quickshare.common.encrytor.DesUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author liu_ke
 */
@Configuration
@ConditionalOnProperty(name = {"spring.rabbitmq.host","spring.rabbitmq.virtual-host"})
public class RabbitConfig {

    private final String PWD_KEY = "quckshre";

    @Value("${spring.rabbitmq.virtual-host:}")
    private String vHost;
    @Value("${spring.rabbitmq.host:127.0.0.1}")
    private String host;
    @Value("${spring.rabbitmq.port:5672}")
    private int port;
    @Value("${spring.rabbitmq.username:}")
    private String userName;
    @Value("${spring.rabbitmq.password:}")
    private String password;

    @Bean(name = "com.quickshare.framework.rabbit.config.RabbitConfig.connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(vHost);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        String pwd = password;
        try{
            pwd = DesUtils.decrypt(pwd,PWD_KEY);
        } catch (Exception e) {
            // do nothing
        }
        connectionFactory.setPassword(pwd);
        return connectionFactory;
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Primary
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                           ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory =  new SimpleRabbitListenerContainerFactory();
        factory.setMaxConcurrentConsumers(1);
        // 消费者个数
        factory.setConcurrentConsumers(1);
        // 设置确认模式为手工确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(factory, connectionFactory);
        // 设置每个消费者获取的最大的消息数量
        factory.setPrefetchCount(1);
        return factory;
    }

    @Bean
    public SimpleMessageListenerContainer mqMessageContainer(final SimpleRabbitListenerContainerFactory factory) throws AmqpException {
        SimpleMessageListenerContainer container = factory.createListenerContainer();
        container.setExposeListenerChannel(true);
        return container;
    }
}
