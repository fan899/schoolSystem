package com.fan.demo.exception;

import com.fan.demo.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


// 用于在特定的处理器类、方法中处理异常的注解
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    * 如果抛出的是ServiceException，则调用该方法
    * */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }


}
