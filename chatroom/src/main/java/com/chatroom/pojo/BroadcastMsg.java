package com.chatroom.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BroadcastMsg {
    private boolean isSystemMsg;
    private String userName;
    private String content;
    private String avatar;
}
