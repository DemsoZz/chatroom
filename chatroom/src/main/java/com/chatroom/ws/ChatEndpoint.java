package com.chatroom.ws;

import com.alibaba.fastjson.JSONObject;
import com.chatroom.mapper.MessageMapper;
import com.chatroom.pojo.BroadcastMsg;
import com.chatroom.pojo.Message;
import com.chatroom.utils.SpringContextUtil;
import com.chatroom.utils.TokenUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint(value = "/chat/{token}")
@Component
public class ChatEndpoint {

    private static final Map<String, Session> userSession = new ConcurrentHashMap<>();

    private static final int MAX_MESSAGE_COUNT = 50;

    private MessageMapper messageMapper;

    private MessageMapper getMessageMapper(){
        if(messageMapper == null){
            messageMapper = SpringContextUtil.getBean(MessageMapper.class);
        }
        return messageMapper;
    }

    private void broadcast(BroadcastMsg message, Session mySession) {
        try {
                Set<Map.Entry<String, Session>> entries = userSession.entrySet();
                for (Map.Entry<String, Session> entry : entries) {
                    if((!entry.getValue().equals(mySession))&&entry.getValue().isOpen()){
                        Session session = entry.getValue();
                        session.getBasicRemote().sendText(JSONObject.toJSONString(message));
                    }
                }
        }catch (Exception e) {
            log.error("广播时出错：{}", e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token")String token) {
        String username;
        if (TokenUtil.verify(token)) {
            // 从Token中提取用户信息
            username = TokenUtil.getTokenInfo(token);

            // 将用户信息存储在WebSocket会话中
        } else {
            throw new SecurityException("Invalid token");
        }
        if(username != null){
            userSession.put(username, session);
            BroadcastMsg message = new BroadcastMsg(true,null,username + "加入群聊",null);
            broadcast(message,null);
            log.info("{}连接成功", username);
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String username = jsonObject.getString("username");
        String content = jsonObject.getString("content");
        String avatar = jsonObject.getString("avatar");
        try{
            Message msg = new Message(username,avatar,content,LocalDateTime.now());
            MessageMapper mapper = getMessageMapper();
            int res = mapper.insert(msg);
            int count = mapper.getMessageCount();
            if(count>MAX_MESSAGE_COUNT){
                int minId = mapper.getMinId();
                mapper.deleteMessage(minId);
            }
            if (res > 0) {
                BroadcastMsg broadcastMsg = new BroadcastMsg(false,username,content,avatar);
                broadcast(broadcastMsg,session);
                log.info("{}:{}", username, content);
            }else {
                log.info("消息保存失败");
            }
        }catch (Exception e){
            log.error("发消息错误：{}", e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        for (Map.Entry<String, Session> entry : userSession.entrySet()) {
            if(entry.getValue().equals(session)){
                BroadcastMsg message = new BroadcastMsg(true,null,entry.getKey()+"离开了",null);
                broadcast(message,session );
                userSession.remove(entry.getKey());
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket连接出错{}", error.getMessage());
    }

}
