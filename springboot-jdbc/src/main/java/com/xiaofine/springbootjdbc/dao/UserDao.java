package com.xiaofine.springbootjdbc.dao;

import com.xiaofine.springbootjdbc.entity.User;

import java.util.List;

public interface UserDao {

    int add(User user);

    int update(User user);

    int delete(int id);

    User findUserById(int id);

    List<User> findAllUsers();
}
