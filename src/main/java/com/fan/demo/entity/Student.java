package com.fan.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName: Student
 * @Description:
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "fan_stu")
public class Student {

    @TableField(value = "COLLEGE_ID")
    private String collegeId;
    @TableField(value = "MAJOR_ID")
    private String majorId;
    @TableField(value = "CLASS_ID")
    private String classId;
    @TableId(value = "recid", type = IdType.AUTO)
    private String id;
    @TableField(value = "ORDER_ID")
    private String orderId;
    private String name;
    @TableField(value = "CARD_ID")
    private String cardId;
    private String phone;
    private String email;
    private String gender;
    private String birthday;
    @TableField(value = "AVATAR_URL")
    private String avatarUrl;
    private String nation;
    private String political;
    @TableField(value = "PLACE_PROVINCE")
    private String placeProvince;
    @TableField(value = "PLACE_CITY")
    private String placeCity;
    @TableField(value = "PLACE_ADDRESS")
    private String placeAddress;
    @TableField(value = "PRESENT_ADDRESS")
    private String presentAddress;
    @TableField(value = "ENTRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date entryDate;
    private String status;

}
