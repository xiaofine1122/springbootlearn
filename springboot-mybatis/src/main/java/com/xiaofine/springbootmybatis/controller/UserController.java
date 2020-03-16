package com.xiaofine.springbootmybatis.controller;

import com.xiaofine.springbootmybatis.entity.User;
import com.xiaofine.springbootmybatis.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String addUser(@RequestParam(value = "name")String name,
                          @RequestParam(value = "email")String email){
        int t = userService.add(name, email);
        if(t==1){
            return "success";
        }else {
            return "fail";
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") int id ,
                             @RequestParam(value = "name",required = true)String name,
                             @RequestParam(value = "email",required = true) String email){

        int t = userService.update(name, email, id);
        if(t==1){
            return "success";
        }else {
            return "fail";
        }
    }

    //默认不写是GET请求
    @RequestMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") int id){
        int t = userService.delete(id);
        if(t==1){
            return "succese delete"+id;
        }else {
            return "fail";
        }
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") int id){
        return userService.findUserById(id);
    }

    @RequestMapping(value="list",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userService.findAllUsers();
    }

}
