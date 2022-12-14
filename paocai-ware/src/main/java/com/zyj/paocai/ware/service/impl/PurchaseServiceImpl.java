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
     * ???????????????????????????
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
            log.info("???????????????????????????????????????");
            return;
        }
        List<PurchaseEntity> entities = baseMapper.selectBatchIds(ids);
        //1??????????????????????????????????????????????????????
        Date now = new Date();
        for (PurchaseEntity entity : entities) {
            if (entity.getStatus() != WareConstant.PurchaseStatusEnum.CREATED.getCode() &&
                    entity.getStatus() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                throw new RRException("?????????:" + entity.getId() + "??????????????????????????????", BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
            }
            entity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            entity.setUpdateTime(now);
        }
        //2???????????????????????????
        this.updateBatchById(entities);

        //3???????????????????????????
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
            //1???????????????
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // ????????????????????????
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = detailService.listByIds(items);
        for (PurchaseDetailEntity entity : purchaseDetailEntities) {
            // ????????????????????????0,1???????????????
            if (entity.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.CREATED.getCode()) || entity.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode())) {
                entity.setPurchaseId(finalPurchaseId);
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            } else {
                throw new RRException("????????????:" + entity.getId() + "????????????",BizCodeEnum.WARE_SERVICE_EXCEPTION.getCode());
            }
        }
        detailService.updateBatchById(purchaseDetailEntities);

        if (mergeVo.getPurchaseId() != null) {
            baseMapper.updateTime(finalPurchaseId);
        }
    }

    /**
     * ????????????
     *
     * @param doneVo
     */
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();
        //????????????????????????
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        // ????????????????????????
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        // ??????????????????????????????
        List<PurchaseDetailEntity> addStocks = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //3?????????????????????????????????
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                addStocks.add(entity);
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        // ?????????????????????
        detailService.updateBatchById(updates);
        // ??????????????????
        wareSkuService.addStock(addStocks);

        // ?????????????????????
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }
}