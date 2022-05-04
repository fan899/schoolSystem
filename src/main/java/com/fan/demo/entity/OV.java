package com.fan.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OV
 * @Description: User和Order的缴费状态
 * @Author fancy
 * @Date 2022/5/4
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OV extends User implements Serializable {

    private String status;
    private String no;
    private Double price;

}
