package com.zyj.paocai.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户注册封装类
 *
 * @author lulx
 * @date 2022-04-08 10:52
 **/
@Data
public class UserRegisterVo {

    @Length(min = 2, max = 16, message = "用户名长度必须在2-16个字符之间")
    @NotBlank(message = "必须填写用户名")
    private String username;

    @Length(min = 6, max = 18, message = "密码长度必须在6-18个字符之间")
    @NotBlank(message = "必须填写密码")
    private String password;

    @NotBlank(message = "手机号必须填写")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "验证码必须填写")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", message = "邮箱格式错误")
    private String email;
}
