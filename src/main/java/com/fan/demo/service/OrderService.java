package com.fan.demo.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.common.Constants;
import com.fan.demo.entity.Order;
import com.fan.demo.entity.Student;
import com.fan.demo.entity.User;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: OrderService
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    /**
     * 根据学生信息生成订单
     * @param stu
     */
    public void createOrderByStu(Student stu, Double price) {

        Order order = new Order();

        order.setNo(DateUtil.format(new Date(), "yyyyMMdd") + System.currentTimeMillis());
        order.setStuId(stu.getId());
        order.setStuCard(stu.getCardId());
        order.setStuName(stu.getName());
        order.setPrice(price);
        order.setOrderTime(new Date());
        order.setStatus("未支付");

        try {
            save(order);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }

    }
}
