package com.zyj.paocai.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.ware.constant.WareConstant;
import com.zyj.paocai.ware.dao.PurchaseDao;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.entity.PurchaseEntity;
import com.zyj.paocai.ware.service.PurchaseDetailService;
import com.zyj.paocai.ware.service.PurchaseService;
import com.zyj.paocai.ware.service.WareSkuService;
import com.zyj.paocai.ware.vo.MergeVo;
import com.zyj.paocai.ware.vo.PurchaseDoneVo;
import com.zyj.paocai.ware.vo.PurchaseItemDoneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService detailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        //key=123&status=0
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(w -> {
                w.eq("id", key).or().eq("assignee_id", key).or().like("assignee_name", key);
            });
        }

        String status = (String) params.get("status");
        if (StringUtils.hasText(status)) {
            wrapper.eq("status", status);
        }

        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    /**
     * 查询未领取的采购单
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1));
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        if (ids == null && ids.size() == 0) {
            log.info("当前没有选择要领取的采购单");
            return;
        }
        List<PurchaseEntity> entities = baseMapper.selectBatchIds(ids);
        //1、确认当前采购单是新建或者已分配状态
        Date now = new Date();
        for (PurchaseEntity entity : entities) {
            if (entity.getStatus() != WareConstant.PurchaseStatusEnum.CREATED.getCode() &&
                    entity.getStatus() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                throw new RRException("采购单:" + entity.getId() + "不是新建或已分配状态", BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
            }
            entity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            entity.setUpdateTime(now);
        }
        //2、改变采购单的状态
        this.updateBatchById(entities);

        //3、改变采购项的状态
        List<PurchaseDetailEntity> details = new ArrayList<>();
        entities.forEach((item) -> {
            List<PurchaseDetailEntity> detailEntities = detailService.listDetailByPurchaseId(item.getId());
            for (PurchaseDetailEntity detailEntity : detailEntities) {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
            }
            details.addAll(detailEntities);
        });
        detailService.updateBatchById(details);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            //1、新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // 更新采购需求状态
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = detailService.listByIds(items);
        for (PurchaseDetailEntity entity : purchaseDetailEntities) {
            // 确认采购单状态是0,1才可以合并
            if (entity.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.CREATED.getCode()) || entity.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())) {
                entity.setPurchaseId(finalPurchaseId);
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            } else {
                throw new RRException("采购需求:" + entity.getId() + "无法分配",BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
            }
        }
        detailService.updateBatchById(purchaseDetailEntities);

        if (mergeVo.getPurchaseId() != null) {
            baseMapper.updateTime(finalPurchaseId);
        }
    }

    /**
     * 完成采购
     *
     * @param doneVo
     */
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();
        //改变采购项的状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        // 需要更新的采购项
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        // 需要添加库存的采购项
        List<PurchaseDetailEntity> addStocks = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //3、将成功采购的进行入库
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                addStocks.add(entity);
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        // 更新采购项信息
        detailService.updateBatchById(updates);
        // 添加库存信息
        wareSkuService.addStock(addStocks);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }
}