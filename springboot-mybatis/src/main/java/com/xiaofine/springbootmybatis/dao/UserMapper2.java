package com.xiaofine.springbootmybatis.dao;

import org.apache.ibatis.annotations.Param;

public interface UserMapper2 {

    int update(@Param("name") String name,@Param("id") int id);
}
