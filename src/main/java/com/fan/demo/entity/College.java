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
 * @ClassName: College
 * @Description: 院校实体类
 * @Author fancy
 * @Date 2022/4/20
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName(value = "FAN_COLLEGE")
public class College implements Serializable {
    @TableId(value = "recid",type = IdType.AUTO)
    private Integer recId;
    private String name;
    private String intro;
//    @TableField(value = "MAJOR_NUMBER")
    private Integer majorNumber;
//    @TableField(value = "STU_NUMBER")
    private Integer stuNumber;
    private String president;
}
