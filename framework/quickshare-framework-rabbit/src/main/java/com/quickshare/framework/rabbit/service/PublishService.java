package com.quickshare.framework.rabbit.service;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Map;

/**
 * @author liu_ke
 */
public interface PublishService {

    <T> void publish(String exchange, Map<String,T> datas) throws IOException;

    <T> void publish(String exchange, Map<String,T> datas,Map<String,Object> headers,String messageId) throws IOException;

    void publish(String exchange, String routingKey, Object data,Map<String,Object> headers,String messageId) throws IOException;

    void publish(String exchange, String routingKey, byte[] data,Map<String,Object> headers,String messageId) throws IOException;

    /**
     * 使用事务提交
     * @param exchange
     * @param routingKey
     * @param data
     * @param headers
     * @param messageId
     * @return
     * @throws IOException
     */
    Channel publishWithTran(String exchange, String routingKey, Object data,Map<String,Object> headers,String messageId) throws IOException;

    /**
     * 使用事务提交
     * @param channel
     * @param exchange
     * @param routingKey
     * @param data
     * @param headers
     * @param messageId
     * @return
     * @throws IOException
     */
    Channel publishWithTran(Channel channel,String exchange, String routingKey, Object data,Map<String,Object> headers,String messageId) throws IOException;

    <T> Channel publishWithTran(Channel channel,String exchange, Map<String,T> datas,boolean autoCommit,Map<String,Object> headers,String messageId) throws IOException;
}
