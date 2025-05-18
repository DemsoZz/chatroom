package com.chatroom.mapper;

import com.chatroom.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from users")
    List<User> userlist();

    @Insert("insert into users (account, password, nickName) VALUES (#{account},#{password},#{nickName})")
    int addUser(User user);
}
