package com.xiaofine.springbootjpa.controller;

import com.xiaofine.springbootjpa.dao.UserDao;
import com.xiaofine.springbootjpa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String addUser(@RequestParam(value = "name")String name,
                   @RequestParam(value = "email")String email){
        User user =new User();
        user.setName(name);
        user.setEmail(email);

        User user1 = userDao.saveAndFlush(user);
        return user1.toString();
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") int id ,
                             @RequestParam(value = "name",required = true)String name,
                             @RequestParam(value = "email",required = true) String email){
        User user =new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        User user1 = userDao.saveAndFlush(user);
        return user1.toString();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") int id){
        //        return .orElse(null).toString();

        return userDao.findById(id).orElse(null);
//        Optional<User> user = userDao.findById(id);
//        if (user.isPresent()){
//            return user.get();
//        }else {
//            return null;
//        }
    }

    @RequestMapping(value="list",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.findAll();
    }
}
