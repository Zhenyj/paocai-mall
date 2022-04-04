package com.zyj.paocai.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.SkuHasStockVo;
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
     * 是否有库存
     *
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    /**
     * 添加库存信息
     * @param addStocks
     */
    void addStock(List<PurchaseDetailEntity> addStocks);
}

