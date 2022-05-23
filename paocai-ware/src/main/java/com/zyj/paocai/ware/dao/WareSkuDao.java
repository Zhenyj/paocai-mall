package com.zyj.paocai.ware.dao;

import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyj.paocai.ware.entity.vo.SkuWareHasStock;
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

    /**
     *
     * @param skuIdCountVos
     * @return
     */
    List<SkuHasStockVo> getSkuStockInfoBySkuIdWithNums(@Param("skuIdCountVos") List<SkuIdCountVo> skuIdCountVos);

    /**
     * 获取sku哪些仓库有库存
     * @param skuId
     * @return
     */
    List<Long> listWareIdHasSkuStock(@Param("skuId") Long skuId);

    /**
     * 锁定库存
     * @param skuId
     * @param wareId
     * @param count
     * @return
     */
    int lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("count") Integer count);

    /**
     * 库存解锁、更新库存工作单状态
     * @param orderSn
     * @param status
     */
    void unlockStockAndCancelTask(@Param("orderSn") String orderSn, @Param("status") int status);

    /**
     * 批量获取sku哪些仓库有库存
     * @param SkuIdCountVos
     * @return
     */
    List<SkuWareHasStock> listWareIdHasSkuStockBatch(@Param("SkuIdCountVos") List<SkuIdCountVo> SkuIdCountVos);

    SkuHasStockVo getSkuStockInfoBySkuIdWithNum(@Param("skuId") Long skuId, @Param("count") Integer count);
}
