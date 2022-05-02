package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.Student;
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
}
