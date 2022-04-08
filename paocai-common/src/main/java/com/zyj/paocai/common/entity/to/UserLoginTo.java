package com.zyj.paocai.common.entity.to;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录用户封装类
 * @author lulx
 * @date 2022-04-08 10:38
 **/
@Data
public class UserLoginTo {


    @NotBlank(message = "用户名错误")
    private String loginAccount;

    @NotBlank(message = "密码错误")
    private String password;
}
