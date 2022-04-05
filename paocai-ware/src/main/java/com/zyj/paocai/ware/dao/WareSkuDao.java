package com.zyj.paocai.ware.dao;

import com.zyj.paocai.common.entity.to.SkuHasStockVo;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:24:53
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    /**
     * 批量获取sku是否有库存
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkuStockInfoBySkuIds(@Param("skuIds") List<Long> skuIds);

    /**
     * 根据采购详单获取已有的库存信息
     *
     * @param details
     * @return
     */
    List<WareSkuEntity> getStockInfosByPurchaseDetail(@Param("details") List<PurchaseDetailEntity> details);

    /**
     * 获取sku是否有库存
     * @param skuId
     * @return
     */
    SkuHasStockVo getSkuHasStockBySkuId(@Param("skuId") Long skuId);
}
