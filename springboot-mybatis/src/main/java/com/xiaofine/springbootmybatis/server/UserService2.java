package com.xiaofine.springbootmybatis.server;

import com.xiaofine.springbootmybatis.dao.UserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService2 {

    @Autowired()
    private UserMapper2 userMapper2;

    @Transactional
    public void transfer() throws RuntimeException{
        userMapper2.update("22235111566",1);
        int i=1/0;
        userMapper2.update("333",2);
    }

}
