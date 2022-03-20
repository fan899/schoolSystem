package com.fan.demo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data // 代替idea的get/set方法
@NoArgsConstructor // 无参构造器
@AllArgsConstructor // 有参构造器
@TableName(value = "sys_user") // mybatis-plus默认是按照实体类的名字去数据库内搜索数据表，当名字不一致时，需要手动设置value指向对应的表
@ToString
public class User {

    @TableId(type = IdType.AUTO) // 向mybatis注明这个属性为数据库的主键，尤其是实体类的主键名和数据表的主键名不一致时
    private Integer id;
    private String username;
    @JsonIgnore //返回的json数据不包含该属性
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "avatar_url") // 将实体类的avatar绑定到数据表的avatar_url，别名
    private String avatar; // 不用注解的方法绑定的话，可以使用驼峰命名，改成avatarUrl，一样可用
}
