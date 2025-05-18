package com.chatroom.controller;

import com.chatroom.pojo.Result;
import com.chatroom.pojo.User;
import com.chatroom.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class UserController extends HttpServlet {

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

            session.setAttribute("user",user);
            return Result.success();

    }

}
