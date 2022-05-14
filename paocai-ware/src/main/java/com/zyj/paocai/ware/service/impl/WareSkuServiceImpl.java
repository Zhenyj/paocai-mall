package com.zyj.paocai.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.OrderTo;
import com.zyj.paocai.common.entity.to.StockDetailTo;
import com.zyj.paocai.common.entity.to.StockLockedTo;
import com.zyj.paocai.common.entity.to.WareLockTo;
import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.ware.constant.WareConstant;
import com.zyj.paocai.ware.dao.WareSkuDao;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.WareOrderTaskDetailEntity;
import com.zyj.paocai.ware.entity.WareOrderTaskEntity;
import com.zyj.paocai.ware.entity.WareSkuEntity;
import com.zyj.paocai.ware.entity.vo.SkuWareHasStock;
import com.zyj.paocai.ware.feign.ProductFeignService;
import com.zyj.paocai.ware.service.WareOrderTaskDetailService;
import com.zyj.paocai.ware.service.WareOrderTaskService;
import com.zyj.paocai.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

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

    /**
     * 商品是否有库存
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuHasStockVo getSkuHasStock(Long skuId) {
        SkuHasStockVo vo = wareSkuDao.getSkuHasStockBySkuId(skuId);
        return vo;
    }

    /**
     * 获取sku是否有库存，包含需要的商品数量
     *
     * @param skuIdCountVos
     * @return
     */
    @Override
    public List<SkuHasStockVo> getSkuHasStockBatchWithNums(List<SkuIdCountVo> skuIdCountVos) {
        List<SkuHasStockVo> skuHasStockVos = wareSkuDao.getSkuStockInfoBySkuIdWithNums(skuIdCountVos);
        if (skuIdCountVos.size() != skuHasStockVos.size()) {
            throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg(), BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
        }
        return skuHasStockVos;
    }

    /**
     * 订单锁定库存
     *
     * @param wareLockTos
     */
    @Transactional
    @Override
    public void orderLockStock(List<WareLockTo> wareLockTos) {
        List<WareOrderTaskEntity> tasks = new ArrayList<>(wareLockTos.size());
        List<SkuIdCountVo> locks = new ArrayList<>(wareLockTos.size() + 1);
        HashMap<Long, WareOrderTaskEntity> skuIdTaskMap = new HashMap<>(wareLockTos.size());
        List<Long> skuIds = new ArrayList<>(wareLockTos.size() + 1);
        //保存库存工作单详情 （追溯）
        for (WareLockTo wareLockTo : wareLockTos) {
            WareOrderTaskEntity task = new WareOrderTaskEntity();
            task.setOrderSn(wareLockTo.getOrderSn());
            tasks.add(task);
            locks.addAll(wareLockTo.getSkuIdCountVos());
            for (SkuIdCountVo skuIdCountVo : wareLockTo.getSkuIdCountVos()) {
                skuIdTaskMap.put(skuIdCountVo.getSkuId(), task);
                skuIds.add(skuIdCountVo.getSkuId());
            }
        }
        if (CollectionUtils.isEmpty(tasks)) {
            throw new RRException(BizCodeEnum.CREATE_ORDER_TASK_EXCEPTION.getMsg(),
                    BizCodeEnum.CREATE_ORDER_TASK_EXCEPTION.getCode());
        }
        wareOrderTaskService.saveBatch(tasks);
        // TODO 可以按照下单的收货地址判断就近仓库是否有库存，目前使用简单的逻辑代替
        // 获取所有商品相关的库存仓库信息
        List<SkuWareHasStock> skuWareHasStocks = wareSkuDao.listWareIdHasSkuStockBatch(locks);
        // 遍历所有仓库进行锁库存操作
        for (SkuWareHasStock skuWareHasStock : skuWareHasStocks) {
            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareIds = skuWareHasStock.getWareIds();
            if (wareIds == null || wareIds.size() == 0) {
                throw new RRException(BizCodeEnum.NO_STOCK_EXCEPTION.getMsg() + ",skuId" + skuId,
                        BizCodeEnum.NO_STOCK_EXCEPTION.getCode());
            }
            String skuName = skuWareHasStock.getSkuName();
            Integer count = skuWareHasStock.getCount();
            // 是否已经锁定库存
            boolean skuStockLocked = false;
            //1、如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录发给MQ
            //2、锁定失败。前面保存信息就回滚了。
            for (Long wareId : wareIds) {
                int num = wareSkuDao.lockSkuStock(skuId, wareId, count);
                if (num != 0) {
                    skuStockLocked = true;
                    // 防止回滚以后找不到数据
                    WareOrderTaskDetailEntity taskDetail = new WareOrderTaskDetailEntity();
                    taskDetail.setSkuId(skuId);
                    taskDetail.setSkuName(skuName);
                    taskDetail.setSkuNum(count);
                    WareOrderTaskEntity task = skuIdTaskMap.get(skuId);
                    taskDetail.setTaskId(task.getId());
                    taskDetail.setWareId(wareId);
                    taskDetail.setLockStatus(0);
                    wareOrderTaskDetailService.save(taskDetail);
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(task.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(taskDetail, detailTo);
                    lockedTo.setDetailTo(detailTo);
                    // 发送MQ
                    break;
                }
            }
            if (!skuStockLocked) {
                throw new RRException(BizCodeEnum.NO_STOCK_EXCEPTION.getMsg() + ",skuId" + skuId,
                        BizCodeEnum.NO_STOCK_EXCEPTION.getCode());
            }
        }
    }

    /**
     * 订单关闭解锁库存
     *
     * @param order
     */
    @Override
    public void unlockStock(OrderTo order) {
        // 查询最新的库存状态
        String orderSn = order.getOrderSn();
        WareOrderTaskEntity task = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        if (task == null) {
            throw new RRException(BizCodeEnum.GET_ORDER_TASK_EXCEPTION.getMsg() + ",orderSn:" + orderSn,
                    BizCodeEnum.GET_ORDER_TASK_EXCEPTION.getCode());
        }
        // 库存解锁、更新库存工作单状态
        wareSkuDao.unlockStockAndCancelTask(orderSn, WareConstant.LockStatusEnum.UNLOCKED.getStatus());
    }

    @Override
    public void unlockStock(StockLockedTo to) {

    }
}