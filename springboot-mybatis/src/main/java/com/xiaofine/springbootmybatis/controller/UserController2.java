package com.xiaofine.springbootmybatis.controller;

import com.xiaofine.springbootmybatis.server.UserService2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user")
@MapperScan("com.xiaofine.springbootmybatis.dao")
public class UserController2 {

    @Autowired
    UserService2 userService2;

    @RequestMapping("/transfer")
    public void transfer(){
        userService2.transfer();
    }
}
