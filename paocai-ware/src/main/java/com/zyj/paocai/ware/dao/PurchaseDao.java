package com.zyj.paocai.ware.dao;

import com.zyj.paocai.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 采购信息
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:24:53
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {

    /**
     * 更新修改时间
     *
     * @param purchaseId
     */
    void updateTime(@Param("purchaseId") Long purchaseId);
}
