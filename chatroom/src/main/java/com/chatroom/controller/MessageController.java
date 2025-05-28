package com.chatroom.controller;

import com.chatroom.pojo.Message;
import com.chatroom.pojo.Result;
import com.chatroom.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/message")
    public Result getMessages(){
        List<Message> messages = messageService.getAllMessages();
        if(messages != null){
            log.info("获取消息记录成功");
            return Result.success("success",messages);
        }else {
            log.info("获取消息记录失败");
            return Result.error("messages is null");
        }
    }
}
