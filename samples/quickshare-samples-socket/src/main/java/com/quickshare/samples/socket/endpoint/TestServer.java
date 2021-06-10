package com.quickshare.samples.socket.endpoint;

import com.quickshare.framework.socket.AbstractSocketServer;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu_ke
 */
@ServerEndpoint("/test")
@Component
public class TestServer extends AbstractSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,TestServer> webSocketMap = new ConcurrentHashMap<>();
    /**接收userId*/
    private String userId="";

    @Override
    protected void onOpen(Session session,Map<String,String> requestParams) {
        String userId = requestParams.getOrDefault("userId","");
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            //addOnlineCount();
            //在线数加1
        }

        //log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            //log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    @Override
    protected void onError() {

    }

    @Override
    protected void onMessage(String message, Session session) {
        System.out.println(message);
    }
}
