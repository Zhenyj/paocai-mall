package com.zyj.paocai.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.ware.dao.WareSkuDao;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.WareSkuEntity;
import com.zyj.paocai.ware.feign.ProductFeignService;
import com.zyj.paocai.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        if (StringUtils.hasText(wareId)) {
            wrapper.eq("ware_id", Long.parseLong(wareId));
        }

        String skuId = (String) params.get("skuId");
        if (StringUtils.hasText(skuId)) {
            wrapper.eq("sku_id", Long.parseLong(skuId));
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 获取sku是否有库存
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuHasStockVo> getSkuHasStockBatch(List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = wareSkuDao.getSkuStockInfoBySkuIds(skuIds);
        if (skuIds.size() != skuHasStockVos.size()) {
            throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
        }
        return skuHasStockVos;
    }

    /**
     * @param addStocks
     */
    @Transactional
    @Override
    public void addStock(List<PurchaseDetailEntity> addStocks) {
        // 1、获取已有的库存记录(skuId与wareId相同)
        List<WareSkuEntity> wareSkus = wareSkuDao.getStockInfosByPurchaseDetail(addStocks);

        // 2、获取相关商品的名称
        List<Long> skuIds = addStocks.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
        R<Map<Long, String>> r = productFeignService.getSkuNameInfos(skuIds);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
        Map<Long, String> skuNameMap = r.getData();
        if (skuNameMap == null) {
            throw new RRException("远程获取商品信息失败", BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
        }

        // 保存库存信息
        List<WareSkuEntity> wareSkuEntities = addStocks.stream().map(item -> {
            WareSkuEntity entity = null;
            boolean flag = false;
            for (WareSkuEntity wareSkuEntity : wareSkus) {
                if (wareSkuEntity.getWareId().equals(item.getWareId()) &&
                        wareSkuEntity.getSkuId().equals(item.getSkuId())) {
                    flag = true;
                    entity = wareSkuEntity;
                    break;
                }
            }
            if (flag) {
                // 已存在库存信息
                entity.setStock(entity.getStock() + item.getSkuNum());
            } else {
                // 新增库存信息
                entity = new WareSkuEntity();
                entity.setSkuId(item.getSkuId());
                entity.setSkuName(skuNameMap.get(item.getSkuId()));
                entity.setStock(item.getSkuNum());
                entity.setWareId(item.getWareId());
                entity.setStockLocked(0);
            }
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(wareSkuEntities);
    }

    @Override
    public SkuHasStockVo getSkuHasStock(Long skuId) {
        SkuHasStockVo vo = wareSkuDao.getSkuHasStockBySkuId(skuId);
        return vo;
    }

}