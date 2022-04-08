package com.zyj.paocai.common.entity.to;

import lombok.Data;

/**
 * @author lulx
 * @date 2022-04-08 16:47
 **/
@Data
public class UserRegisterTo {
    private String username;
    private String password;
    private String phone;
    private String email;
}
