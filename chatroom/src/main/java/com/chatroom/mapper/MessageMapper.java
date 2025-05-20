package com.chatroom.mapper;

import com.chatroom.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    @Insert("insert into message (username,content,sendTime) VALUES (#{username},#{content},#{sendTime})")
    int insert(Message message);
}
