package com.chatroom.service.impl;

import com.chatroom.mapper.UserMapper;
import com.chatroom.pojo.User;
import com.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> userlist() {
        return userMapper.userlist();
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }
}
