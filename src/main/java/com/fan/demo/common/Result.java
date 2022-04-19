package com.fan.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
* 接口统一返回类
*
* */
@Data // get/set
@NoArgsConstructor // 无参构造
@AllArgsConstructor // 有参构造
public class Result {
    private String code; // 返回登录是否成功，0为成功，其他为失败
    private String msg; // 向前端返回错误信息
    private Object data; // 携带相关数据返回

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }


}
