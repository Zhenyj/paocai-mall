package com.zyj.paocai.member.dao;

import com.zyj.paocai.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:23:57
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    /**
     * 获取相关用户（用户名、手机号、邮箱）
     * @param account
     * @return
     */
    List<MemberEntity> getMemberByAccount(@Param("account") String account);
}
