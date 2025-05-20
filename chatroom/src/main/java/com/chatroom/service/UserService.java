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

    public int addUser(User user) {
        return userMapper.addUser(user);
    }
}
