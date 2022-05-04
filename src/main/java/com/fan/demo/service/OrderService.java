package com.fan.demo.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.common.Constants;
import com.fan.demo.entity.Order;
import com.fan.demo.entity.Student;
import com.fan.demo.entity.User;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * 根据支付宝返回的数据更新订单状态
     * @param tradeNo 本系统的订单号
     * @param orderStatus 订单状态
     * @param gmtPayment 付款时间
     * @param alipayTradeNo 支付宝交易流水
     */
    public void updateStateToPayed(String tradeNo, String orderStatus, String gmtPayment, String alipayTradeNo) {

        // 将字符串转换为date类型
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date payTime = null;
        try {
            payTime = df.parse(gmtPayment);
        } catch (ParseException e) {
            throw new ServiceException(Constants.CODE_500, "日期转换出错");
        }

        // 根据订单流水号查找订单，生成对应的实体
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no", tradeNo);
        Order one = getOne(queryWrapper);

        one.setStatus(orderStatus);
        one.setPayTime(payTime);
        one.setAlipayNo(alipayTradeNo);

        try {
            saveOrUpdate(one);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"付款状态更新失败");
        }

    }

    /**
     * 更新退款状态
     * @param tradeNo 本系统的订单号
     * @param orderStatus 订单状态
     * @param now 退款时间
     */
    public void updatePayState(String tradeNo, String orderStatus, Date now) {

        // 根据订单流水号查找订单，生成对应的实体
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no", tradeNo);
        Order one = getOne(queryWrapper);

        one.setReturnTime(now);
        one.setStatus(orderStatus);

        try {
            saveOrUpdate(one);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"退款订单修改异常");
        }

    }

    /**
     * 根据身份证号查询用户订单的缴费状态
     * @param cardId
     * @return
     */
    public String getStautsByCardId(String cardId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STU_CARD", cardId);
        Order one = null;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"查询订单状态时出现系统错误");
        }
        if (one != null) {
            return one.getStatus();
        } else {
            throw new ServiceException(Constants.CODE_600, "找不到订单");
        }
    }

    /**
     * 根据身份证号搜索订单数据
     * @param cardId
     * @return
     */
    public Order orderInfoByCardId(String cardId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STU_CARD", cardId);
        Order one = null;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"查询订单状态时出现系统错误");
        }
        if (one != null) {
            return one;
        } else {
            throw new ServiceException(Constants.CODE_600, "找不到订单");
        }

    }
}
