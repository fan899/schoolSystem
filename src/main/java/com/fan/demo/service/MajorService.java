package com.fan.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.entity.Major;
import com.fan.demo.mapper.MajorMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MajorService
 * @Description:
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Service
public class MajorService extends ServiceImpl<MajorMapper, Major> {
}
