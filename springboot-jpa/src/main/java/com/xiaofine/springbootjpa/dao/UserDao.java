package com.xiaofine.springbootjpa.dao;

import com.xiaofine.springbootjpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {

}
