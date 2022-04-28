package com.zyj.paocai.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.member.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:23:57
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户获取收货地址数据
     * @return
     */
    List<MemberReceiveAddressEntity> listForUser();

    /**
     * 获取默认收货地址
     * @return
     */
    MemberReceiveAddressEntity getDefaultAddress();

    /**
     * 添加收货地址
     * @param memberReceiveAddress
     */
    void saveAddress(MemberReceiveAddressEntity memberReceiveAddress);

    /**
     * 更新收货地址
     * @param memberReceiveAddress
     */
    void updateAddress(MemberReceiveAddressEntity memberReceiveAddress);

    /**
     * 删除收货地址
     * @param id
     */
    void removeAddress(Long id);
}

