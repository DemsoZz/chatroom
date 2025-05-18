package com.chatroom.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private int userId;
    private String username;
    private String password;
}
