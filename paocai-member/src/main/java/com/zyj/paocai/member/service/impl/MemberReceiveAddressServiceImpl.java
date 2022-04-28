package com.zyj.paocai.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.vo.MemberRespVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.member.dao.MemberReceiveAddressDao;
import com.zyj.paocai.member.entity.MemberReceiveAddressEntity;
import com.zyj.paocai.member.interceptor.LoginInfoInterceptor;
import com.zyj.paocai.member.service.MemberReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 用户获取收货地址数据
     *
     * @return
     */
    @Override
    public List<MemberReceiveAddressEntity> listForUser() {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        List<MemberReceiveAddressEntity> address = baseMapper.selectList(new QueryWrapper<MemberReceiveAddressEntity>()
                .eq("member_id", member.getId()).orderByDesc("default_status"));
        return address;
    }

    /**
     * 获取默认收货地址
     *
     * @return
     */
    @Override
    public MemberReceiveAddressEntity getDefaultAddress() {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        return baseMapper.selectOne(new QueryWrapper<MemberReceiveAddressEntity>()
                .eq("member_id", member.getId())
                .eq("default_status", 1));
    }

    /**
     * 新增收货地址
     *
     * @param memberReceiveAddress
     */
    @Transactional
    @Override
    public void saveAddress(MemberReceiveAddressEntity memberReceiveAddress) {
        if (memberReceiveAddress.getDefaultStatus().equals(Constant.ONE)) {
            // 如果新增收货地址为默认收货地址需判断是否已有默认收货地址并修改
            MemberReceiveAddressEntity defaultAddress = getDefaultAddress();
            if (defaultAddress != null) {
                defaultAddress.setDefaultStatus(Constant.ZERO);
                updateById(defaultAddress);
            }
        }
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        memberReceiveAddress.setMemberId(member.getId());
        save(memberReceiveAddress);
    }

    /**
     * @param memberReceiveAddress
     */
    @Override
    public void updateAddress(MemberReceiveAddressEntity memberReceiveAddress) {
        if (memberReceiveAddress.getDefaultStatus().equals(Constant.ONE)) {
            // 如果更改收货地址为默认收货地址需判断与已有默认收货地址相同id，不同则修改
            MemberReceiveAddressEntity defaultAddress = getDefaultAddress();
            if (!defaultAddress.getId().equals(memberReceiveAddress.getId())) {
                defaultAddress.setDefaultStatus(Constant.ZERO);
                updateById(defaultAddress);
            }
        }
        updateById(memberReceiveAddress);
    }

    /**
     * 删除收货地址
     *
     * @param id
     */
    @Override
    public void removeAddress(Long id) {
        MemberRespVo member = LoginInfoInterceptor.loginInfo.get();
        int num = baseMapper.delete(new QueryWrapper<MemberReceiveAddressEntity>()
                .eq("id", id)
                .eq("member_id", member.getId()));
        if (num != 1) {
            throw new RuntimeException("删除失败,id:" + id + "member_id:" + member.getId());
        }
    }
}