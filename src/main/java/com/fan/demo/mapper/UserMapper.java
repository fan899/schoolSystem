package com.fan.demo.mapper;

import com.fan.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Mapper
public interface UserMapper {

    @Select("select * from sys_user")
    List<User> findAll();

    @Insert("INSERT INTO sys_user(username, password, nickname, email, phone, address) VALUES " +
            "(#{username}, #{password}, #{nickname}, #{email}, #{phone}, #{address}) ")
    int insert(User user);


//    @Update("update sys_user set username = #{username}, nickname = #{nickname}, email = #{email}, " +
//            "phone = #{phone}, address = #{address} where id = #{id}")
//    使用动态sql接管
    int update(User user);

    @Delete("delete from sys_user where id = #{id}")
    int deleteById(@Param("id") Integer id);  //@Param注解的作用是给参数命名,参数命名后就能根据名字得到参数值,正确的将参数传入sql语句中


    @Select("select * from sys_user where username like #{username} limit #{pageNum}, #{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize, String username);

    @Select("select count(*) from sys_user where username like #{username}")
    int selectTotal(String username);
}
