package com.chatroom.controller;

import com.chatroom.pojo.Result;
import com.chatroom.pojo.User;
import com.chatroom.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Result userList(){
        log.info("查询数据");
        List<User> userList = userService.userlist();
        return Result.success(userList);
    }

    @PostMapping("/login")
    public Result addUser(@RequestBody User user, HttpSession session){  //封装JSON数据

            session.setAttribute("user",user.getUsername());
            return Result.success();

    }

}
