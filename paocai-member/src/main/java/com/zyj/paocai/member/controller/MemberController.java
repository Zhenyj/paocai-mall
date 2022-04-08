package com.zyj.paocai.member.controller;

import com.zyj.paocai.common.entity.to.UserLoginTo;
import com.zyj.paocai.common.entity.to.UserRegisterTo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.member.entity.MemberEntity;
import com.zyj.paocai.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
