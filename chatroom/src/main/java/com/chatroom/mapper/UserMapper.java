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

    @Insert("insert into users (username, password, avatar) VALUES (#{username},#{password},#{avatar})")
    int addUser(User user);

    @Select(" SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User findByUnameAndPassword(String username, String password);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUname(String uname);
}
