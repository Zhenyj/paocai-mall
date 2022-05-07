package com.zyj.paocai.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.member.entity.MemberEntity;
import com.zyj.paocai.member.entity.vo.PwdUpdateVo;

import java.util.Map;

/**
 * 会员
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:23:57
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册
     * @param userRegisterTo
     */
    void regist(UserRegisterTo userRegisterTo);

    /**
     * 账号密码登录
     * @param userLoginTo
     * @return
     */
    MemberEntity login(UserLoginTo userLoginTo);

    /**
     * 校验用户名唯一
     * @param username
     * @return
     */
    Boolean checkUsernameUnique(String username);

    /**
     * 更新用户信息
     * @param member
     */
    MemberEntity updateUserInfo(MemberEntity member);

    /**
     * 修改密码
     * @param vo
     */
    void pwdUpdate(PwdUpdateVo vo);
}

