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
 * @ClassName: Orders
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "fan_order")
public class Order {

    @TableId(value = "recid", type = IdType.AUTO)
    private Integer id;
    private String no;
    @TableField(value = "STU_ID")
    private String stuId;
    @TableField(value = "STU_CARD")
    private String stuCard;
    @TableField(value = "STU_NAME")
    private String stuName;
    @TableField(value = "PAYABLE_PRICE")
    private Double price;
    @TableField(value = "PAY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    @TableField(value = "ALIPAY_NO")
    private String alipayNo;
    @TableField(value = "RETURN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;
    @TableField(value = "ORDER_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;
    @TableField(value = "PAY_METHOD")
    private String payMethod;
    private String status;
    @TableField(value = "ORDER_COMMENT")
    private String orderComment;


}
