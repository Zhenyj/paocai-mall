package com.zyj.paocai.member.controller;

import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.member.entity.MemberReceiveAddressEntity;
import com.zyj.paocai.member.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 会员收货地址
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:23:57
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 获取默认收货地址
     *
     * @return
     */
    @GetMapping("/default")
    public R<MemberReceiveAddressEntity> getDefaultAddress() {
        MemberReceiveAddressEntity address = memberReceiveAddressService.getDefaultAddress();
        return R.ok(address);
    }

    /**
     * 用户获取收货地址数据
     *
     * @return
     */
    @GetMapping("/list/user")
    public R<List<MemberReceiveAddressEntity>> listForUser() {
        List<MemberReceiveAddressEntity> addressList = memberReceiveAddressService.listForUser();
        return R.ok(addressList);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok(memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {

        memberReceiveAddressService.saveAddress(memberReceiveAddress);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress) {
        memberReceiveAddressService.updateAddress(memberReceiveAddress);
        return R.ok();
    }

    /**
     * 删除收货地址
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        memberReceiveAddressService.removeAddress(id);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
