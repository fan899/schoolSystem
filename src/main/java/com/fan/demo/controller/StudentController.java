package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.entity.Student;
import com.fan.demo.service.StudentService;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: StudentController
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Api(tags = "学生管理")
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 新增&修改
     * @param student
     * @return
     */
    @PostMapping
    public boolean save(@RequestBody Student student) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        // 当id为null时，是新增数据，需要设置录入时间
        if (student.getId() == null) {
            student.setEntryDate(date);
        }
        return studentService.saveOrUpdate(student);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return studentService.removeById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return studentService.removeByIds(ids);
    }

    /**
     * 查询所有数据
     * @return
     */
    @GetMapping
    public List<Student> findAll() {
        return studentService.list();
    }

    /**
     * 导出数据接口
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Student> list = studentService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = null;
        ServletOutputStream out = null;

        try {
            fileName = URLEncoder.encode("学生信息", "UTF-8");
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
            List<Student> list = reader.readAll(Student.class);
            studentService.saveBatch(list);
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
    public IPage<Student> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "") String cardId,
                                   @RequestParam(defaultValue = "") String phone) {
        IPage<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!name.equals("")) {
            queryWrapper.like("name", name);
        }
        if (!cardId.equals("")) {
            queryWrapper.like("card_id", cardId);
        }
        if (!phone.equals("")) {
            queryWrapper.like("phone", phone);
        }
        queryWrapper.orderByDesc("id");
        return studentService.page(page, queryWrapper);
    }
}
