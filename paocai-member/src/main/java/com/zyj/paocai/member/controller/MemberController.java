package com.zyj.paocai.member.controller;

import com.zyj.paocai.common.constant.AuthConstant;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.JwtUtils;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.member.entity.MemberEntity;
import com.zyj.paocai.member.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;


/**
 * 会员
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:23:57
 */
@RestController
@RequestMapping("member/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/checkUsernameUnique")
    public R<Boolean> checkUsernameUnique(@RequestParam("username") String username) {
        Boolean b = memberService.checkUsernameUnique(username);
        if (b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
    }

    /**
     * 注册
     *
     * @param userRegisterTo
     * @return
     */
    @PostMapping("/regist")
    public R regist(@RequestBody @Valid UserRegisterTo userRegisterTo, BindingResult result) {
        if (result.hasErrors()) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), result.getFieldError().getDefaultMessage());
        }
        memberService.regist(userRegisterTo);
        return R.ok();
    }

    /**
     * 账号密码登录
     *
     * @param userLoginTo
     * @param result
     * @return
     */
    @PostMapping("/login")
    public R<MemberEntity> login(@RequestBody @Valid UserLoginTo userLoginTo, BindingResult result) {
        if (result.hasErrors()) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), result.getFieldError().getDefaultMessage());
        }
        MemberEntity member = memberService.login(userLoginTo);
        if (member == null) {
            return R.error("账号或密码错误");
        }
        return R.ok(member);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok(member);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R<MemberEntity> update(@RequestBody MemberEntity member, HttpSession session,
                                  HttpServletResponse response) {
        MemberEntity newInfo = memberService.updateUserInfo(member);
        // 将最新用户信息放入session中（redis）
        session.setAttribute(AuthConstant.LOGIN_USER, newInfo);
        // 返回最新的用户信息过期时间一天
        session.setMaxInactiveInterval(60 * 60 * 24);
        MemberRespVo memberRespVo = new MemberRespVo();
        BeanUtils.copyProperties(newInfo, memberRespVo);
        String token = JwtUtils.getToken(memberRespVo);
        Cookie tokenCookie = new Cookie("token", token);
        tokenCookie.setMaxAge(60 * 60 * 24 * 7);
        tokenCookie.setDomain(Constant.PAOCAI_DOMAIN);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
        return R.ok(newInfo);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
