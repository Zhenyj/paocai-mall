package com.zyj.paocai.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.member.entity.MemberEntity;

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
}

