package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.Classes;
import com.fan.demo.service.ClassesService;
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
 * @ClassName: ClassController
 * @Description:
 * @Author fancy
 * @Date 2022/5/1
 * @Version 1.0
 */
@Api(tags = "班级管理")
@RestController
@RequestMapping("/classes")
public class ClassesController {
    @Autowired(required = false)
    private ClassesService classesService;

    /**
     * 新增&修改
     * @param classes
     * @return
     */
    @PostMapping
    public boolean save(@RequestBody Classes classes) {
        return classesService.saveOrUpdate(classes);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return classesService.removeById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return classesService.removeByIds(ids);
    }

    /**
     * 查询所有院系数据
     * @return
     */
    @GetMapping
    public List<Classes> findAll() {
        return classesService.list();
    }

    /**
     * 数据导出
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Classes> list = classesService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = null;
        ServletOutputStream out = null;

        try {
            fileName = URLEncoder.encode("班级信息", "UTF-8");
            response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");
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

    /**
     * 数据导入
     * @param file
     * @return
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) {
        InputStream is = null;
        ExcelReader reader = null;

        try {
            is = file.getInputStream();
            reader = ExcelUtil.getReader(is);
            List<Classes> list = reader.readAll(Classes.class);
            classesService.saveBatch(list);
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

    @GetMapping("/page")
    public IPage<Classes> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String classesName) {
        IPage<Classes> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        if (!classesName.equals("")) {
            queryWrapper.like("name", classesName);
        }
        return classesService.page(page, queryWrapper);
    }

}
