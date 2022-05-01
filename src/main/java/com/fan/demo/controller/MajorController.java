package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.Major;
import com.fan.demo.service.MajorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName: MajorController
 * @Description:
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Api(tags = "专业管理")
@RestController
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    /**
     * 新增&修改
     * @param major
     * @return
     */
    public boolean save(@RequestBody Major major) {
        return majorService.saveOrUpdate(major);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return majorService.removeById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return majorService.removeByIds(ids);
    }

    /**
     * 查询所有数据
     * @return
     */
    @GetMapping
    public List<Major> findAll() {
        return majorService.list();
    }

    /**
     * 导出数据接口
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Major> list = majorService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = null;
        ServletOutputStream out = null;

        try {
            fileName = URLEncoder.encode("专业信息", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/import")
    public Boolean imp(MultipartFile file) {
        InputStream is = null;
        ExcelReader reader = null;
        try {
            is = file.getInputStream();
            reader = ExcelUtil.getReader(is);
            List<Major> list = reader.readAll(Major.class);
            majorService.saveBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param majorName
     * @return
     */
    @GetMapping("/page")
    public IPage<Major> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String majorName) {
        IPage<Major> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        if (!majorName.equals("")) {
            queryWrapper.like("name", majorName);
        }
        return majorService.page(page, queryWrapper);
    }

}