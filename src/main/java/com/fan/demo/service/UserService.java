package com.fan.demo.service;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.controller.dto.UserDTO;
import com.fan.demo.entity.User;
import com.fan.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private static final Log LOG = Log.get();

    public boolean login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); // 新建QueryWrapper对象
        queryWrapper.eq("username", userDTO.getUsername()); // 传入username
        queryWrapper.eq("password", userDTO.getPassword()); // 传入password

//        List<User> list = list(queryWrapper); // 将查询结果全部存入list中
//        return list.size() != 0; // 当集合的长度不为0时，即内部存有数据时，为true

        try {
            User one = getOne(queryWrapper);
            return one != null;
        } catch (Exception e) { // 当返回的数据不止一条时，直接返回false
            LOG.error(e); // 出错时把错误写入log输出
            return false;
        }

        // 当数据库中存在相同用户名和密码的数据时，会返回两条或多条数据，进而导致后台报错，因为getOne仅能接收一条数据
//        User one = getOne(queryWrapper); // 根据传入的数据搜索，如果能够找到符合的数据，则存入one对象，否则为null
//        return one != null;
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
