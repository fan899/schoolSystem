package com.fan.demo.service;

import com.fan.demo.entity.User;
import com.fan.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public int save(User user) {
        if (user.getId() == null) {
            return userMapper.insert(user);  //当userId为空时，表示新增数据
        }else {
            return userMapper.update(user);  //不为空时表示更新已有数据
        }
    }


}
