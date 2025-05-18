package com.chatroom.ws;

import com.alibaba.fastjson.JSONObject;
import com.chatroom.config.GetHttpSessionConfig;
import com.chatroom.controller.ChannelController;
import com.chatroom.controller.UserController;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {

    private static final Map<String, Session> userSession = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    private void broadcast(String message) {
        Collection<Session> sessions = userSession.values();
        for (Session session : sessions) {
            session.getAsyncRemote().sendText(message);
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
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String username = jsonObject.getString("username");
        String content = jsonObject.getString("content");
        Session receiverSession = userSession.get(username);
        if (receiverSession != null) {
            try {
                receiverSession.getBasicRemote().sendText(content);
            } catch (Exception e) {
                log.error("发送消息出错：", e);
            }
        } else {
            log.error("username not exist");
        }
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("连接出错", throwable);
    }
}
