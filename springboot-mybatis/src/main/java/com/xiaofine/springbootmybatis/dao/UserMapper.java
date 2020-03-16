package com.xiaofine.springbootmybatis.dao;

import com.xiaofine.springbootmybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name,email) values(#{name},#{email})")
    int add(@Param("name") String name,@Param("email") String email);
    @Update("update user set name = #{name},email =#{email} where id=#{id} ")
    int update(@Param("name") String name, @Param("email") String email,@Param("id") int id);
    @Delete("delete from user where id =  #{id}")
    int delete(@Param("id") int id);
    @Select("select id,name,email from user where id =#{id}")
    User findUser(@Param("id") int id);
    @Select("select id,name,email from user")
    List<User> findUserList();

}
