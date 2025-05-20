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
    private HttpSession httpSession;

    private MessageMapper messageMapper;

    private MessageMapper getMessageMapper(){
        if(messageMapper == null){
            messageMapper = SpringContextUtil.getBean(MessageMapper.class);
        }
        return messageMapper;
    }

    private void broadcast(String message) {
        try {
            Set<Map.Entry<String, Session>> entries = userSession.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                session.getBasicRemote().sendText(message);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String username = (String) this.httpSession.getAttribute("user");
        userSession.put(username, session);
        broadcast(username + "加入群聊");
    }


    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String username = jsonObject.getString("username");
        String content = jsonObject.getString("content");
        try{
            Message msg = new Message();
            msg.setContent(content);
            msg.setUsername(username);
            msg.setSendTime(LocalDateTime.now());
             MessageMapper mapper = getMessageMapper();
            int res = mapper.insert(msg);
            if (res > 0) {
                log.info("消息保存成功");
            }else {
                log.info("消息保存失败");
            }
            broadcast(message);
            log.info("{}:{}", username, content);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        String username = (String) this.httpSession.getAttribute("user");
        userSession.remove(username);
        broadcast(username + "离开了");
    }

}
