package com.fan.demo.controller.dto;

import lombok.Data;
/*
* 接收前端登录请求的参数
*/
@Data // get/set
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
}
