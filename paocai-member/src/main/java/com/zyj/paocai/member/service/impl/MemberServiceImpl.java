package com.zyj.paocai.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.member.constant.MemberConstant;
import com.zyj.paocai.member.dao.MemberDao;
import com.zyj.paocai.member.entity.MemberEntity;
import com.zyj.paocai.member.entity.MemberLevelEntity;
import com.zyj.paocai.member.service.MemberLevelService;
import com.zyj.paocai.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelService memberLevelService;

    @Autowired
    MemberDao memberDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 注册
     *
     * @param userRegisterTo
     */
    @Transactional
    @Override
    public void regist(UserRegisterTo userRegisterTo) {

        // 判断用户名是否唯一
        if (!checkPhoneUnique(userRegisterTo.getPhone())) {
            throw new RRException(BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
        // 判断手机号码是否唯一
        if (!checkUsernameUnique(userRegisterTo.getUsername())) {
            throw new RRException(BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }
        if(!checkEmailUnique(userRegisterTo.getEmail())){
            throw new RRException(BizCodeEnum.EMAIL_EXIST_EXCEPTION.getMsg());
        }
        MemberEntity member = new MemberEntity();
        // 设置会员等级
        MemberLevelEntity levelEntity = memberLevelService.getDefaultLevel();
        if (levelEntity == null) {
            log.error("系统没有默认的会员等级");
            throw new RRException("系统没有默认的会员等级",BizCodeEnum.MEMBER_SERVICE_EXCEPTION.getCode());
        }

        // 密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userRegisterTo.getPassword());
        member.setPassword(encode);
        member.setLevelId(levelEntity.getId());
        member.setMobile(userRegisterTo.getPhone());
        member.setEmail(userRegisterTo.getEmail());
        member.setUsername(userRegisterTo.getUsername());
        // 默认昵称与用户名相同
        member.setNickname(userRegisterTo.getUsername());
        member.setGender(MemberConstant.GENDER_DEFAULT);
        member.setCreateTime(new Date());
        save(member);
    }

    /**
     * 账号密码登录
     *
     * @param userLoginTo
     * @return
     */
    @Override
    public MemberEntity login(UserLoginTo userLoginTo) {
        String account = userLoginTo.getLoginAccount();
        String password = userLoginTo.getPassword();
        // 获取相关用户（用户名、手机号、邮箱）
        List<MemberEntity> members = memberDao.getMemberByAccount(account);
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (MemberEntity member : members) {
            if (passwordEncoder.matches(password, member.getPassword())) {
                // 不将密码返回
                member.setPassword("");
                return member;
            }
        }
        return null;
    }

    /**
     * 判断邮箱是否唯一
     * @param email
     * @return
     */
    private Boolean checkEmailUnique(String email) {
        int count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("email", email));
        return count == 0;
    }


    /**
     * 判断用户名是否唯一
     *
     * @param username
     */
    @Override
    public Boolean checkUsernameUnique(String username) {
        int count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        return count == 0;
    }

    /**
     * 更新用户信息
     * @param member
     */
    @Transactional
    @Override
    public MemberEntity updateUserInfo(MemberEntity member) {
        updateById(member);
        MemberEntity newInfo = getById(member.getId());
        newInfo.setPassword("");
        return newInfo;
    }

    /**
     * 判断手机号码是否唯一
     *
     * @param phone
     * @return
     */
    private Boolean checkPhoneUnique(String phone) {
        int count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        return count == 0;
    }

}