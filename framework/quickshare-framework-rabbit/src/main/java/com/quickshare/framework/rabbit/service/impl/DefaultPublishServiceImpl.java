package com.quickshare.framework.rabbit.service.impl;

import com.quickshare.common.json.JsonConvert;
import com.quickshare.framework.rabbit.service.PublishService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ReturnListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnProperty(name = "spring.rabbitmq.host")
public class DefaultPublishServiceImpl implements PublishService {

    protected final String defaultCharsetName = "UTF-8";

    private final ConnectionFactory factory;

    public DefaultPublishServiceImpl(ConnectionFactory factory){
        this.factory = factory;
    }

    @Override
    public <T> void publish(String exchange, Map<String,T> datas) throws IOException {
        publish(exchange,datas,null, UUID.randomUUID().toString());
    }

    @Override
    public <T> void publish(String exchange, Map<String,T> datas, Map<String,Object> headers, String messageId) throws IOException {
        if(datas==null || datas.isEmpty()){
            throw new RuntimeException("数据不存在。");
        }
        if(datas.size()>1) {
            publishWithTran(null,exchange, datas, true, headers, messageId);
        }
        else {
            Map.Entry<String,T> dataEntry = datas.entrySet().iterator().next();
            publish(exchange,dataEntry.getKey(),dataEntry.getValue(),headers,messageId);
        }
    }

    @Override
    public void publish(String exchange, String routingKey, byte[] data, Map<String, Object> headers, String messageId) throws IOException {
        Connection connection = factory.createConnection();
        AMQP.BasicProperties basicProperties = genBasicProperties(headers,messageId);
        Channel channel = connection.createChannel(false);
        channel.basicPublish(exchange,routingKey,true, basicProperties,data);
        try {
            channel.close();
        } catch (TimeoutException e) {
            // do nothing
        }
    }

    @Override
    public void publish(String exchange, String routingKey, Object data, Map<String,Object> headers, String messageId) throws IOException {
        String strData = JsonConvert.toString(data);
        publish(exchange,routingKey,strData.getBytes(defaultCharsetName),headers,messageId);
    }

    @Override
    public Channel publishWithTran(Channel channel, String exchange, String routingKey, Object data, Map<String, Object> headers, String messageId) throws IOException {
        try {
            if(channel == null) {
                Connection connection = factory.createConnection();
                channel = connection.createChannel(true);
            }
            String strData = JsonConvert.toString(data);
            AMQP.BasicProperties basicProperties = genBasicProperties(headers, messageId);
            channel.txSelect();
            channel.basicPublish(exchange,routingKey,true, basicProperties,strData.getBytes(defaultCharsetName));
            return channel;
        }
        catch (IOException e){
            if(channel != null){
                try {
                    channel.txRollback();
                    channel.close();
                } catch (IOException| TimeoutException e1) {
                    // todo logger
                }
            }
            throw new IOException(e);
        }
    }

    @Override
    public Channel publishWithTran(String exchange, String routingKey, Object data, Map<String, Object> headers, String messageId) throws IOException {
        return publishWithTran(null,exchange,routingKey,data,headers,messageId);
    }

    @Override
    public <T> Channel publishWithTran(Channel channel,String exchange, Map<String,T> datas, boolean autoCommit, Map<String,Object> headers, String messageId) throws IOException {
        try {
            if(channel == null) {
                Connection connection = factory.createConnection();
                channel = connection.createChannel(true);
            }
            AMQP.BasicProperties basicProperties = genBasicProperties(headers, messageId);
            //channel.addReturnListener(createReturnListener());
            channel.txSelect();
            for (Map.Entry<String,T> dataMap:datas.entrySet()) {
                String strData = JsonConvert.toString(dataMap.getValue());
                channel.basicPublish(exchange,dataMap.getKey(),true, basicProperties,strData.getBytes(defaultCharsetName));
            }
            if(autoCommit) {
                channel.txCommit();
                channel.close();
            }
            return channel;
        }
        catch (IOException | TimeoutException e){
            if(channel != null){
                try {
                    channel.txRollback();
                    channel.close();
                } catch (IOException| TimeoutException e1) {
                    // todo logger
                }
            }
            throw new IOException(e);
        }
    }

    private AMQP.BasicProperties genBasicProperties(Map<String,Object> headers,String messageId){
        AMQP.BasicProperties properties = new AMQP.BasicProperties("application/json", defaultCharsetName, headers, 2, 0, (String)null, (String)null, (String)null, messageId, new Date(System.currentTimeMillis()), (String)null, (String)null, (String)null, (String)null);
        return properties;
    }

    private ReturnListener createReturnListener(){
        ReturnListener listener = new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) throws IOException {
                System.out.println(replyText);
            }
        };
        return listener;
    }
}
