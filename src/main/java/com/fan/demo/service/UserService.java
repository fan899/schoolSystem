package com.fan.demo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.common.Constants;
import com.fan.demo.common.Result;
import com.fan.demo.controller.dto.UserDTO;
import com.fan.demo.entity.User;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.mapper.UserMapper;
import com.fan.demo.utils.TokenUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public UserDTO  login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); // 新建QueryWrapper对象
        queryWrapper.eq("username", userDTO.getUsername()); // 传入username
        queryWrapper.eq("password", userDTO.getPassword()); // 传入password

//        List<User> list = list(queryWrapper); // 将查询结果全部存入list中
//        return list.size() != 0; // 当集合的长度不为0时，即内部存有数据时，为true
        User one = null;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {  // 当返回的数据不止一条时，会发生异常被下面的catch捕获
            LOG.error(e); // 出错时把错误写入log输出
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        // 如果没发生异常就执行下列判断
        if (one != null) { // 判断数据库内是否有对应的数据
            // 将one所包含的user信息copy到userDTO中
            // 因为one存储的是整个User实体类的信息
            // 而userDTO存储的仅是前端需要的若干条信息
            // 所以需要将one的需要的数据利用hutool映射到userDTO中
            // 参数一：被复制的对象
            // 参数二：被粘贴的对象
            // 是否忽略大小写
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getCardId(), one.getPassword());
            userDTO.setToken(token);
            return userDTO;
        } else { // 当one=null时，说明数据库内部没有对应的数据，则抛出下面这个异常
            throw new ServiceException(Constants.CODE_400, "用户名或密码错误");
        }
        // 当数据库中存在相同用户名和密码的数据时，会返回两条或多条数据，进而导致后台报错，因为getOne仅能接收一条数据
//        User one = getOne(queryWrapper); // 根据传入的数据搜索，如果能够找到符合的数据，则存入one对象，否则为null
//        return one != null;
    }

    public User register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); // 新建QueryWrapper对象
        queryWrapper.eq("card_id", userDTO.getCardId()); // 传入身份证号码
        queryWrapper.eq("phone", userDTO.getPhone()); // 传入手机号码
        User one = null;

        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e); // 出错时把错误写入log输出
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (one == null) { // 当数据库检索不到时，才执行插入操作
            // one为null，需要new 成 User对象
            one = new User();
            // 将userDTO的数据copy to one里面 userDTO -> user
            BeanUtil.copyProperties(userDTO, one, true);
            one.setCreatedTime(new Date());
            // 执行插入
            save(one);
        } else { // 如果one不为null，则说明数据库内有相同的身份证和手机号
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    /**
     * 根据身份证号查找用户
     * @param cardId
     * @return
     */
    public User userInfoByCardId(String cardId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("CARD_ID", cardId);
        User one = null;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e); // 出错时把错误写入log输出
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (one != null) {
            return one;
        }else {
            throw new ServiceException(Constants.CODE_600, "找不到对应用户");
        }
    }

    /**
     * 根据身份证号查找用户
     * @param cardId
     * @return
     */
    public User getUserByCardId(String cardId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        User one = null;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e); // 出错时把错误写入log输出
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            return one;
        }else {
            throw new ServiceException(Constants.CODE_600, "找不到对应用户");
        }
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
