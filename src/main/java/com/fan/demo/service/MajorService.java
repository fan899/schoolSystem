package com.fan.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fan.demo.common.Constants;
import com.fan.demo.entity.Major;
import com.fan.demo.exception.ServiceException;
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

    /**
     * 根据专业id获取专业学费
     * @param majorId
     * @return
     */
    public Double getPriceById(String majorId) {
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RECID", majorId);
        Major one = null;

        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (one != null) {
            return one.getPrice();
        } else {
            throw new ServiceException(Constants.CODE_400, "专业不存在");
        }
    }
}
