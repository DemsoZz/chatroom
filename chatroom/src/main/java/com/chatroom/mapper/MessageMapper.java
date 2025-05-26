package com.chatroom.mapper;

import com.chatroom.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("insert into message (username,content,sendTime) VALUES (#{username},#{content},#{sendTime})")
    int insert(Message message);

    @Select("SELECT messageId,username,content,sendTime from message")
    List<Message> selectAll();
}
