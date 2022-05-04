package com.fan.demo.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fan.demo.common.Result;
import com.fan.demo.entity.Order;
import com.fan.demo.entity.Student;
import com.fan.demo.entity.User;
import com.fan.demo.service.MajorService;
import com.fan.demo.service.OrderService;
import com.fan.demo.service.StudentService;
import com.fan.demo.utils.TokenUtils;
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
 * @ClassName: OrderController
 * @Description:
 * @Author fancy
 * @Date 2022/5/2
 * @Version 1.0
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MajorService majorService;

    @PostMapping("/{stuCardId}")
    public Result createOrderByStuCardId(@PathVariable String stuCardId) {

        Student student = studentService.getStuByCardId(stuCardId);
        Double price = majorService.getPriceById(student.getMajorId());
        orderService.createOrderByStu(student, price);

        return Result.success();
    }

    /**
     * 接收个人中心的信息返回缴费状态
     * @param CardId
     * @return
     */
    @GetMapping("/status/{CardId}")
    public Result findStatusById(@PathVariable String CardId) {
        String status = orderService.getStautsByCardId(CardId);
        return Result.success(status);
    }

    /**
     * 根据当前登录用户生成订单
     * @param
     * @return
     */
    public Result createOrderByCurrentUser() {
        // 利用token获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null) {
            String cardId = currentUser.getCardId();
            Student stu = studentService.findStuByUserCardId(cardId);
            String majorId = stu.getMajorId();
            Double price = majorService.getPriceById(majorId);
            orderService.createOrderByStu(stu, price);
        }
        return Result.success();
    }


    /**
     * 新增&修改
     * @param order
     * @return
     */
    @PostMapping
    public boolean save(@RequestBody Order order) {
        return orderService.saveOrUpdate(order);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return orderService.removeById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return orderService.removeByIds(ids);
    }

    /**
     * 查询所有数据
     * @return
     */
    @GetMapping
    public List<Order> findAll() {
        return orderService.list();
    }

    /**
     * 导出数据接口
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Order> list = orderService.list();
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
            List<Order> list = reader.readAll(Order.class);
            orderService.saveBatch(list);
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
    public IPage<Order> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String no,
                                   @RequestParam(defaultValue = "") String stuName,
                                   @RequestParam(defaultValue = "") String alipayNo,
                                   @RequestParam(defaultValue = "") String status) {
        IPage<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (!no.equals("")) {
            queryWrapper.like("no", no);
        }
        if (!stuName.equals("")) {
            queryWrapper.like("STU_NAME", stuName);
        }
        if (!alipayNo.equals("")) {
            queryWrapper.like("ALIPAY_NO", alipayNo);
        }
        if (!status.equals("")) {
            queryWrapper.like("STATUS", status);
        }
        queryWrapper.orderByDesc("id");
        return orderService.page(page, queryWrapper);
    }

}
