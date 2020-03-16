package com.xiaofine.springbootjdbc.dao.impl;

import com.xiaofine.springbootjdbc.dao.UserDao;
import com.xiaofine.springbootjdbc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        return jdbcTemplate.update("insert into user(name,email) values(?,?)",user.getName(),user.getEmail());
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("update user SET name =?,email=? WHERE id=?",user.getName(),user.getEmail(),user.getId());
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE from user where id=?",id);
    }

    @Override
    public User findUserById(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM user where id =?",new Object[]{id}, new BeanPropertyRowMapper(User.class));
        if(list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> list = jdbcTemplate.query("SELECT * FROM user ",new BeanPropertyRowMapper(User.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }
}
