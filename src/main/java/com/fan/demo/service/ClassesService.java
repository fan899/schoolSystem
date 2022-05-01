package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.Classes;
import com.fan.demo.mapper.ClassesMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ClassService
 * @Description:
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Service
public class ClassesService extends ServiceImpl<ClassesMapper, Classes> {
}
