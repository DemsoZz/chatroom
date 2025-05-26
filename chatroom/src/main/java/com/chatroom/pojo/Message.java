package com.chatroom.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message {
    private String username;
    private String avatar;
    private String content;
    private LocalDateTime sendTime;
}
