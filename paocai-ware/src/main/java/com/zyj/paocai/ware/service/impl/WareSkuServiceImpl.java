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
     * ??????sku???????????????
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
        // 1??????????????????????????????(skuId???wareId??????)
        List<WareSkuEntity> wareSkus = wareSkuDao.getStockInfosByPurchaseDetail(addStocks);

        // 2??????????????????????????????
        List<Long> skuIds = addStocks.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
        R<Map<Long, String>> r = productFeignService.getSkuNameInfos(skuIds);
        if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
            throw new RRException(r.getMsg(), r.getCode());
        }
        Map<Long, String> skuNameMap = r.getData();
        if (skuNameMap == null) {
            throw new RRException("??????????????????????????????", BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
        }

        // ??????????????????
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
                // ?????????????????????
                entity.setStock(entity.getStock() + item.getSkuNum());
            } else {
                // ??????????????????
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
     * ?????????????????????
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
     * ????????????sku?????????????????????????????????????????????
     *
     * @param skuIdCountVos
     * @return
     */
    @Override
    public List<SkuHasStockVo> getSkuHasStockBatchWithNums(List<SkuIdCountVo> skuIdCountVos) {
        List<SkuHasStockVo> skuHasStockVos = wareSkuDao.getSkuStockInfoBySkuIdWithNums(skuIdCountVos);
        if (skuIdCountVos.size() != skuHasStockVos.size()) {
            throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg() + "skuIds:" + skuHasStockVos.toString(),
                    BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
        }
        return skuHasStockVos;
    }

    /**
     * ??????sku?????????????????????????????????????????????
     *
     * @param skuId
     * @param count
     * @return
     */
    @Override
    public SkuHasStockVo getSkuHasStockWithNum(Long skuId, Integer count) {
        SkuHasStockVo skuHasStockVo = wareSkuDao.getSkuStockInfoBySkuIdWithNum(skuId, count);
        if (skuHasStockVo == null) {
            throw new RRException(BizCodeEnum.PRODUCT_WARE_EXCEPTION.getMsg() + ",skuId:" + skuId,
                    BizCodeEnum.PRODUCT_WARE_EXCEPTION.getCode());
        }
        return skuHasStockVo;
    }

    /**
     * ??????????????????
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
        //??????????????????????????? ????????????
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
        // TODO ??????????????????????????????????????????????????????????????????????????????????????????????????????
        // ?????????????????????????????????????????????
        List<SkuWareHasStock> skuWareHasStocks = wareSkuDao.listWareIdHasSkuStockBatch(locks);
        // ???????????????????????????????????????
        for (SkuWareHasStock skuWareHasStock : skuWareHasStocks) {
            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareIds = skuWareHasStock.getWareIds();
            if (wareIds == null || wareIds.size() == 0) {
                throw new RRException(BizCodeEnum.NO_STOCK_EXCEPTION.getMsg() + ",skuId" + skuId,
                        BizCodeEnum.NO_STOCK_EXCEPTION.getCode());
            }
            String skuName = skuWareHasStock.getSkuName();
            Integer count = skuWareHasStock.getCount();
            // ????????????????????????
            boolean skuStockLocked = false;
            //1????????????????????????????????????????????????????????????????????????????????????????????????MQ
            //2???????????????????????????????????????????????????
            for (Long wareId : wareIds) {
                int num = wareSkuDao.lockSkuStock(skuId, wareId, count);
                if (num != 0) {
                    skuStockLocked = true;
                    // ?????????????????????????????????
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
                    // ??????MQ
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
     * ????????????????????????
     *
     * @param order
     */
    @Override
    public void unlockStock(OrderTo order) {
        // ???????????????????????????
        String orderSn = order.getOrderSn();
        WareOrderTaskEntity task = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        if (task == null) {
            throw new RRException(BizCodeEnum.GET_ORDER_TASK_EXCEPTION.getMsg() + ",orderSn:" + orderSn,
                    BizCodeEnum.GET_ORDER_TASK_EXCEPTION.getCode());
        }
        // ??????????????????????????????????????????
        wareSkuDao.unlockStockAndCancelTask(orderSn, WareConstant.LockStatusEnum.UNLOCKED.getStatus());
    }

    @Override
    public void unlockStock(StockLockedTo to) {

    }


}