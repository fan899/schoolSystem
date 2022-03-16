package com.fan.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.User;
import com.fan.demo.mapper.UserMapper;
import com.fan.demo.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "信息管理")
@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired(required = false)
//    private UserMapper userMapper;

    @Autowired(required = false)
    private UserService userService;

    // 新增&修改
    @PostMapping
    public boolean save(@RequestBody User user) { // @RequestBody可以把前台json数据映射到实体类中
        // 用userService判断是更新还是新增(mybatis-plus)
        return userService.saveUser(user);
    }

    // 删除数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) { //可以将URL中占位符参数{/id}绑定到处理器类的方法形参中，所以这三个id要完全一致
        return userService.removeById(id); // 使用mybatis-plus接管
    }

    // 批量删除
    @PostMapping("/del/batch") // 修改接收方法，使其可以接收数组
    public boolean deleteBatch(@RequestBody List<Integer> ids) { //将请求参数绑定到你控制器的方法参数上
        return userService.removeByIds(ids); // 使用mybatis-plus接管
    }

    // 查询所有数据
    @GetMapping
    public List<User> findAll() {
        return userService.list(); // 使用mybatis-plus接管
    }


//    // 分页查询&总条数
//    // 分页查询的计算公式： y = (pageNum - 1) * pageSize; x = 每页显示多少条数据
//    // SQL中的limit语句 limit y, x
//    // limit中的y是截取值，即从第几个开始显示，默认起始值为0
//    // limit不输入y的话，默认为0
//    @GetMapping("/page") // 路径： /user/page?pageNum=1&pageSize=10
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username) { // @RequestParam负责接收链接中get方式的参数并传入方法中
//        // 获取前端页码pageNum计算limit语句的参数
//        pageNum = (pageNum - 1) * pageSize;
//        // Map集合，存放分页查询的返回数据和该数据表的总条数
//        Map<String, Object> res = new HashMap<>();
//        // 字符串拼接，方便进行模糊查询
//        username = "%"+username+"%";
//        // 查询该数据表的总条数&模糊查询
//        Integer total = userMapper.selectTotal(username);
//        // 根据前端的页码数返回分页查询的结果&模糊查询
//        List<User> data = userMapper.selectPage(pageNum, pageSize, username);
//        // 把两个数据存入map集合
//        res.put("data",data);
//        res.put("total",total);
//        return res;
    // 分页查询 mybatis-plus
    @GetMapping("/page") // 路径： /user/page?pageNum=1&pageSize=10
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String username, // defaultValue 意思为默认值
                                        @RequestParam(defaultValue = "") String email,
                                        @RequestParam(defaultValue = "") String address) { // @RequestParam负责接收链接中get方式的参数并传入方法中
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!username.equals("")) { // 需要判断，当字段不为空时才会执行拼接操作，否则会以空为条件搜索
            queryWrapper.like("username",username); // 用户名模糊查询
        }
        if (!email.equals("")) {
            queryWrapper.and(w -> w.like("email", email));
        }
        if (!address.equals("")) {
            queryWrapper.like("address", address); // 不添加and也可以，框架会自动补上
        }
        queryWrapper.orderByDesc("id"); // 修改为根据id倒序显示
//        queryWrapper.or().like("email", email); // 这个是sql语句 or条件
        return userService.page(page, queryWrapper);// 需要传入翻页对象page和实体对象封装操作类queryWrapper

    }
}
