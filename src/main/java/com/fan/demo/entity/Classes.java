package com.fan.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: Class
 * @Description: 班级实体类
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "fan_class")
@ToString
public class Classes implements Serializable {

    @TableId(value = "recid",type = IdType.AUTO)
    private Integer id;
    private String majorId;
    private String collegeId;
    @TableField(value = "name")
    private String className;
    @TableField(value = "CLASS_MEMBER_CELLING")
    // 限制人数上限
    private Integer celling;

}
