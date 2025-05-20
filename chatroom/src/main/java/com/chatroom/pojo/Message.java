package com.chatroom.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private String username;
    private String content;
    private LocalDateTime sendTime;
}
