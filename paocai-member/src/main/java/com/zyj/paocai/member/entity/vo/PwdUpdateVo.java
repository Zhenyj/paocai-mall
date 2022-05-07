package com.zyj.paocai.member.entity.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author lulx
 * @date 2022-05-07 21:18
 **/
@Data
public class PwdUpdateVo {

    /** 旧密码 */
    @Length(min = 2, max = 16, message = "密码长度必须在6-18个字符之间")
    @NotBlank(message = "必须填写旧密码")
    private String oldPass;
    /** 新密码 */
    @Length(min = 6, max = 18, message = "密码长度必须在6-18个字符之间")
    @NotBlank(message = "必须填写密码")
    private String pass;
    /** 确认密码 */
    @Length(min = 6, max = 18, message = "密码长度必须在6-18个字符之间")
    @NotBlank(message = "必须填写确认密码")
    private String checkPass;
}
