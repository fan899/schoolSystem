package com.fan.demo.exception;

/**
 * ServiceException是自定义异常，需要继承Java内部提供的RuntimeException
 */
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        // 调用父类的构造器
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
