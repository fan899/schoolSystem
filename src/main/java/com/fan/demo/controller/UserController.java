package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.User;
import com.fan.demo.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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


    // 导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("id","序号");
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatar", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = null;
        ServletOutputStream out = null;
        try {
            fileName = URLEncoder.encode("用户信息", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 导入数据接口
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) {
        InputStream is = null;
        ExcelReader reader = null;
        try {
            is = file.getInputStream();
            reader = ExcelUtil.getReader(is);
            List<User> list = reader.readAll(User.class);
//            System.out.println(list);
            userService.saveBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
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
