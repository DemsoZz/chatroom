package com.chatroom.service;

import com.chatroom.pojo.User;

import java.util.List;

public interface UserService {

    List<User> userlist();

    int addUser(User user);
}
