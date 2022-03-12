package com.fan.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    @JsonIgnore //返回的json数据不包含该属性
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;

}
