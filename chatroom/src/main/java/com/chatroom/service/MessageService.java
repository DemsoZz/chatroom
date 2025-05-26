package com.chatroom.service;

import com.chatroom.mapper.MessageMapper;
import com.chatroom.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> getAllMessages() {
        return messageMapper.selectAll();
    }
}
