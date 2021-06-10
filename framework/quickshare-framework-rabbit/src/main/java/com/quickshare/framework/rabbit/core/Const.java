package com.quickshare.framework.rabbit.core;

/**
 * @author liu_ke
 */
public class Const {

    public final static String RETRY ="@retry";
    public final static String FAILED ="@failed";

    public final static String ERR_VHOST_EXISTS = "Error: vhost_already_exists:";
    public final static String ERR_USER_EXISTS = "Error: user_already_exists:";
    public final static String ERR_NOSUCH_VHOST = "Error: no_such_vhost:";

    /**
     * 创建exchange语句
     * 1.vhonst名; 2.exchange名; 3.类型:direct.
     */
    public final static String CREATE_EXCHANGE = "rabbit_exchange:declare({resource, <<\\\"%s\\\">>, exchange, <<\\\"%s\\\">>}, %s, true, false, false, []).";
    /**
     * 创建普通queue语句
     * 1.vhonst名; 2.queue名.
     */
    public final static String CREATE_QUEUE = "rabbit_amqqueue:declare({resource, <<\\\"%s\\\">>, queue, <<\\\"%s\\\">>}, true, false, [], none).";
    /**
     * 创建延迟queue语句
     * 1.vhonst名; 2.queue名; 3.死信交换机exchange; 4.延迟时间(ms) 5.延迟routing-key.
     */
    public final static String CREATE_QUEUE_DELAY = "rabbit_amqqueue:declare({resource, <<\\\"%s\\\">>, queue, <<\\\"%s\\\">>}, true, false, [{<<\\\"x-dead-letter-exchange\\\">>,longstr,<<\\\"%s\\\">>},{<<\\\"x-message-ttl\\\">>,signedint,%s},{<<\\\"x-dead-letter-routing-key\\\">>,longstr,<<\\\"%s\\\">>}], none).";
    /**
     * 绑定队列
     * 1.vhonst名; 2.exchange名; 3.routing-key; 4.vhonst名; 5.queue名
     */
    public final static String BINDING_QUEUE = "rabbit_binding:add({binding,{resource, <<\\\"%s\\\">>, exchange, <<\\\"%s\\\">>}, <<\\\"%s\\\">>, {resource, <<\\\"%s\\\">>, queue, <<\\\"%s\\\">>}, []}).";
}
