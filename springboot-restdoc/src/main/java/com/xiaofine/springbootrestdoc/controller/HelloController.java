package com.xiaofine.springbootrestdoc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class HelloController {


    @RequestMapping("/")
    public Map<String, Object> HelloWorld(){
        return Collections.singletonMap("message","hello world");
    }
}
