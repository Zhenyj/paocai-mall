package com.zyj.paocai.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.OrderTo;
import com.zyj.paocai.common.entity.to.StockLockedTo;
import com.zyj.paocai.common.entity.to.WareLockTo;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:24:53
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 批量获取sku是否有库存
     *
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkuHasStockBatch(List<Long> skuIds);

    /**
     * 添加库存信息
     *
     * @param addStocks
     */
    void addStock(List<PurchaseDetailEntity> addStocks);

    /**
     * 获取sku是否有库存
     *
     * @param skuId
     * @return
     */
    SkuHasStockVo getSkuHasStock(Long skuId);

    /**
     * 获取sku是否有库存，包含需要的商品数量
     *
     * @param skuIdCountVos
     * @return
     */
    List<SkuHasStockVo> getSkuHasStockBatchWithNums(List<SkuIdCountVo> skuIdCountVos);

    /**
     * 订单锁定库存
     *
     * @param wareLockTos
     */
    void orderLockStock(List<WareLockTo> wareLockTos);

    /**
     * 解锁库存
     *
     * @param order
     */
    void unlockStock(OrderTo order);

    /**
     * 解锁库存
     *
     * @param to
     */
    void unlockStock(StockLockedTo to);

    /**
     * 获取sku是否有库存，包含需要的商品数量
     * @param skuId
     * @param count
     * @return
     */
    SkuHasStockVo getSkuHasStockWithNum(Long skuId, Integer count);
}

