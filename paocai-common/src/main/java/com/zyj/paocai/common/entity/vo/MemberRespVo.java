package com.zyj.paocai.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author lulx
 * @date 2022-01-21 20:37
 **/
@Data
public class MemberRespVo {

    /** id */
    private Long id;

    /** 会员等级id */
    private Long levelId;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 手机号码 */
    private String mobile;

    /** 邮箱 */
    private String email;

    /** 头像 */
    private String header;

    /** 性别 */
    private Integer gender;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    /** 所在城市 */
    private String city;

    /** 职业 */
    private String job;

    /** 个性签名 */
    private String sign;

    /** 用户来源 */
    private Integer sourceType;

    /** 积分 */
    private Long integration;

    /** 成长值 */
    private Long growth;

    /** 启用状态 */
    private Integer status;
}
