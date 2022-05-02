package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.Order;
import com.fan.demo.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName: OrderService
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
}
