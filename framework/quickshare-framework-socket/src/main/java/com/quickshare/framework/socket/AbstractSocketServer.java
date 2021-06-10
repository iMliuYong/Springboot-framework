package com.quickshare.framework.socket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu_ke
 */
@Component
public abstract class AbstractSocketServer {

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpenInternal(Session session){
        this.session = session;
        Map<String, List<String>> stringListMap = session.getRequestParameterMap();
        Map<String,String> requestMap = new HashMap<>();
        if(stringListMap!=null && !stringListMap.isEmpty()){
            for (Map.Entry<String, List<String>> entry:stringListMap.entrySet()) {
                List<String> values = entry.getValue();
                String value = null;
                if(values!=null && !values.isEmpty()){
                    value = values.get(0);
                }
                requestMap.put(entry.getKey(),value);
            }
        }
        onOpen(session,requestMap);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onCloseInternal(){
        onClose();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessageInternal(String message, Session session) {
        onMessage(message,session);
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onErrorInternal(Session session, Throwable error) {
        onError();
    }

    /**
     * 实现服务器主动推送
     */
    protected void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    protected void onOpen(Session session, Map<String,String> requestParams){

    }

    protected void onClose(){

    }

    /**
     * 发生错误时的处理
     */
    protected abstract void onError();

    /**
     * 接收到消息处理
     * @param message 客户端发送过来的消息
     * @param session 客户端的连接会话
     */
    protected abstract void onMessage(String message, Session session);
}
