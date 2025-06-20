package com.chatroom.controller;

import com.chatroom.pojo.Result;
import com.chatroom.pojo.User;
import com.chatroom.service.UserService;
import com.chatroom.utils.TokenUtil;
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
        return Result.success("success",userList);
    }

    @PostMapping("/login")
    public Result addUser(@RequestBody User user){  //封装JSON数据
        User res = userService.loginService(user.getUsername(), user.getPassword());
       if (res != null) {
            String token = TokenUtil.signToken(user.getUsername());
            log.info("{}登录成功", user.getUsername());
            return Result.success(token, res);
        } else {
            log.info("{}登录失败", user.getUsername());
            return Result.error("账号或密码错误！");
        }

    }

    @PostMapping("/register")
    public Result registerController(@RequestBody User newUser) {
        User user = userService.registerService(newUser);
        if (user != null) {
            log.info("{}注册成功", newUser.getUsername());
            return Result.success("success",newUser);
        } else {
            log.info("{}注册失败", newUser.getUsername());
            return Result.error("用户名已存在！");
        }

    }

}
