package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.College;
import com.fan.demo.mapper.CollegeMapper;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CollegeController
 * @Description: 院校Controller
 * @Author fancy
 * @Date 2022/4/20
 * @Version 1.0
 */
@Service
public class CollegeService extends ServiceImpl<CollegeMapper, College> {


}
