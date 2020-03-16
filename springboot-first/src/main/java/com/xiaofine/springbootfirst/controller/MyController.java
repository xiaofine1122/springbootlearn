package com.xiaofine.springbootfirst.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Value("${my.name}")
    private String myname;

    @Value("${my.age}")
    private int age;

    @RequestMapping("/my")
    public String myname() {


        return "my name is" + myname + "age is " + age;
    }
}
