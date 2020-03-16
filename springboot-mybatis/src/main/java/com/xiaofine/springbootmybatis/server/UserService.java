package com.xiaofine.springbootmybatis.server;

import com.xiaofine.springbootmybatis.dao.UserMapper;
import com.xiaofine.springbootmybatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired()
    private UserMapper userMapper;

    public int add(String name,String email) {
        return userMapper.add(name,email);
    }

    public int update(String name,String email,int id) {
        return userMapper.update(name,email,id);
    }

    public int delete(int id) {
        return userMapper.delete(id);
    }

    public User findUserById(int id) {
        return userMapper.findUser(id);
    }

    public List<User> findAllUsers() {
        return userMapper.findUserList();
    }
}
