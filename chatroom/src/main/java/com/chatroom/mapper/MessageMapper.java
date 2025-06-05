package com.chatroom.mapper;

import com.chatroom.pojo.Message;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("insert into message (username,avatar,content,sendTime) VALUES (#{username},#{avatar},#{content},#{sendTime})")
    int insert(Message message);

    @Select("SELECT username,avatar,content,sendTime from message")
    List<Message> selectAll();

    @Select("SELECT COUNT(*) from message")
    int getMessageCount();

    @Delete("delete from message where messageId = #{minId}")
    int deleteMessage(int minId);

    @Select("SELECT MIN(messageId) from message")
    int getMinId();
}
