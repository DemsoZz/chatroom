package com.chatroom.ws;

import com.alibaba.fastjson.JSONObject;
import com.chatroom.config.GetHttpSessionConfig;
import com.chatroom.mapper.MessageMapper;
import com.chatroom.pojo.Message;
import com.chatroom.utils.SpringContextUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {

    private static final Map<String, Session> userSession = new ConcurrentHashMap<>();

    private static final int MAX_MESSAGE_COUNT = 100;

    private MessageMapper messageMapper;

    private MessageMapper getMessageMapper(){
        if(messageMapper == null){
            messageMapper = SpringContextUtil.getBean(MessageMapper.class);
        }
        return messageMapper;
    }

    private void broadcast(String message,Session mySession) {
        try {
                Set<Map.Entry<String, Session>> entries = userSession.entrySet();
                for (Map.Entry<String, Session> entry : entries) {
                    if((!entry.getValue().equals(mySession))&&entry.getValue().isOpen()){
                        Session session = entry.getValue();
                        session.getBasicRemote().sendText(message);
                    }
                }
        }catch (Exception e) {
            log.error("广播时出错：{}", e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String username = (String) httpSession.getAttribute("user");
        userSession.put(username, session);
        broadcast(username + "加入群聊",null);
        log.info("{}连接成功", username);
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
                broadcast(message,session);
                session.getBasicRemote().sendText("发送成功");
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
                broadcast(entry.getKey()+"离开了",session );
                userSession.remove(entry.getKey());
            }
        }
    }

}
