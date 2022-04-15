package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.vo.SkuDetailVo;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.SkuInfoEntity;
import com.zyj.paocai.product.entity.vo.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author lulx
 * @email
 * @date 2022-03-15 21:19:43
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据自定义查询参查询sku信息
     *
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 通过spuId获取相关sku信息
     *
     * @param spuId
     * @return
     */
    List<SkuInfoEntity> getSkuBySpuId(Long spuId);

    /**
     * 获取skuId与skuName映射集合
     *
     * @param skuIds
     * @return
     */
    Map<Long, String> getSkuNameInfos(List<Long> skuIds);

    /**
     * 获取商品详情页商品数据
     *
     * @param skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    SkuItemVo getItem(Long skuId) throws ExecutionException, InterruptedException;

    /**
     * 获取sku详细信息
     * @param skuId
     * @return
     */
    SkuDetailVo getSkuDetail(Long skuId);

    /**
     * 获取猜你喜欢、热销商品数据
     *
     * @param pageSize
     * @param page
     * @return
     */
    List<SkuInfoEntity> getHotSales(Integer pageSize, Integer page);
}

