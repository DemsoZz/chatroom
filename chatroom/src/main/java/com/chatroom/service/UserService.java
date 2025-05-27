package com.chatroom.service;

import com.chatroom.mapper.UserMapper;
import com.chatroom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> userlist() {
        return userMapper.userlist();
    }

    public User loginService(String username, String password) {
        return userMapper.findByUnameAndPassword(username, password);
    }

    public User registerService(User user) {
        //当新用户的用户名已存在时
        if (userMapper.findByUname(user.getUsername()) != null) {
            // 无法注册
            return null;
        } else {
            // 插入用户到数据库
            int result = userMapper.addUser(user);
            if (result > 0) {
                return user;
            } else {
                return null;
            }
        }
    }
}
