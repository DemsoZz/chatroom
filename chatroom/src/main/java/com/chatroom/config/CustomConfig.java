//package com.chatroom.config;
//
//import com.chatroom.utils.TokenUtil;
//import jakarta.websocket.HandshakeResponse;
//import jakarta.websocket.server.HandshakeRequest;
//import jakarta.websocket.server.ServerEndpointConfig;
//
//import java.util.List;
//import java.util.Map;
//
//public class CustomConfig extends ServerEndpointConfig.Configurator {
//
//    @Override
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//
//        // 从HTTP请求头中获取Token
//        Map<String, List<String>> headers = request.getHeaders();
//        List<String> authHeaders = headers.get("Authorization");
//
//        if (authHeaders != null && !authHeaders.isEmpty()) {
//            String token = authHeaders.get(0).replace("Bearer ", "");
//
//            // 验证Token
//            if (TokenUtil.verify(token)) {
//                // 从Token中提取用户信息
//                String username = TokenUtil.getTokenInfo(token);
//
//                // 将用户信息存储在WebSocket会话中
//                sec.getUserProperties().put("username", username);
//            } else {
//                throw new SecurityException("Invalid token");
//            }
//        } else {
//            throw new SecurityException("Missing token");
//        }
//    }
//}
