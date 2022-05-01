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
 * @ClassName: Major
 * @Description: 专业实体类
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "fan_major")
@ToString
public class Major implements Serializable {

    @TableId(value = "recid",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "college_id")
    private String collegeId;
    @TableField(value = "name")
    private String majorName;
    @TableField(value = "TUITION_FEE")
    private Double price;
    @TableField(value = "STU_NUMBER")
    private Long stuNumber;

}
