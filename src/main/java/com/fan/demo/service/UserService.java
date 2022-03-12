package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.User;
import com.fan.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public boolean saveUser(User user) {
//        if (user.getId() == null) {
//            return save(user); // mybatis-plus提供的方法，表示插入数据
//        }else {
//            return updateById(user); // 也是mybatis-plus提供的方法，根据id更新数据
//        }
        return saveOrUpdate(user); // 将更新还是新增交由mybatis-plus判断，达到以上两种方法的效果
    }

//    @Autowired(required = false)
//    private UserMapper userMapper;

//    public int save(User user) {
//        if (user.getId() == null) {
//            return userMapper.insert(user);  //当userId为空时，表示新增数据
//        }else {
//            return userMapper.update(user);  //不为空时表示更新已有数据
//        }
//    }


}
