package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.College;
import com.fan.demo.service.CollegeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName: CollegeService
 * @Description: 院校service
 * @Author fancy
 * @Date 2022/4/20
 * @Version 1.0
 */
@Api(tags = "院系管理")
@RestController
@RequestMapping("/college")
public class CollegeController {
    @Autowired
    private CollegeService collegeService;

    /**
     * 新增&修改
     * @param college
     * @return
     */
    @PostMapping
    public boolean save(@RequestBody College college) {
        return collegeService.saveOrUpdate(college);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return collegeService.removeById(id);
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return collegeService.removeByIds(ids);
    }

    /**
     * 查询所有院系数据
     * @return
     */
    @GetMapping
    public List<College> findAll() {
        return collegeService.list();
    }

    /**
     * 数据导出
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<College> list = collegeService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = null;
        ServletOutputStream out = null;
        try {
            fileName = URLEncoder.encode("院系信息", "UTF-8");
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
            List<College> list = reader.readAll(College.class);
            collegeService.saveBatch(list);
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
     *分页查询
     * @param pageNum
     * @param pageSize
     * @param collegeName
     * @return
     */
    @GetMapping("/page")
    public IPage<College> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String collegeName) {
        IPage<College> page = new Page<>(pageNum, pageSize);
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        if (!collegeName.equals("")) {
            queryWrapper.like("name", collegeName);
        }
        return collegeService.page(page, queryWrapper);
    }
}
