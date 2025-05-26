package com.chatroom.service;

import com.chatroom.mapper.MessageMapper;
import com.chatroom.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private static final int MAX_MESSAGE_COUNT = 100;

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> getAllMessages() {
        int messageCount = messageMapper.getMessageCount();
        if(messageCount > MAX_MESSAGE_COUNT) {
            messageMapper.deleteMessage();
        }
        return messageMapper.selectAll();
    }
}
