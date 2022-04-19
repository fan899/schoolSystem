package com.fan.demo.exception;

import com.fan.demo.common.Result;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.Response;
import javax.xml.ws.Service;


// 用于在特定的处理器类、方法中处理异常的注解
@ControllerAdvice
public class ExceptionHandler {

    /*
    * 如果抛出的是ServiceException，则调用该方法
    * */
    @org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error("-999", se.getMessage());
    }


}
