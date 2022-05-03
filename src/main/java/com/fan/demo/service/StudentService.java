package com.fan.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.common.Constants;
import com.fan.demo.entity.Student;
import com.fan.demo.exception.ServiceException;
import com.fan.demo.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName: StudentService
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    /**
     * 判断当前登录的用户是否在学生表内
     * @param cardId
     */
    public Student findStuByUserCardId(String cardId) {

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STU_CARD", cardId);
        Student one;

        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (one != null) { // 说明当前登录的用户存在学生表，可以生成订单
            return one;
        }else { // 说明当前用户不是学生，无法生成订单
            throw new ServiceException(Constants.CODE_400, "学生不存在");
        }

    }

    public Student getStuByCardId(String stuCardId) {

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("CARD_ID", stuCardId);

        Student one = null;

        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (one != null) {
            return one;
        }else {
            throw new ServiceException(Constants.CODE_400, "学生不存在");
        }


    }
}
