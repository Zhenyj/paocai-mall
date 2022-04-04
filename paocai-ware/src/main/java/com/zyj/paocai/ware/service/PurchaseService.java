package com.zyj.paocai.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.ware.entity.PurchaseEntity;
import com.zyj.paocai.ware.vo.MergeVo;
import com.zyj.paocai.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     * @param params
     * @return
     */
    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    /**
     * 领取采购单
     * @param ids
     */
    void received(List<Long> ids);

    /**
     * 合并采购单
     * @param mergeVo
     */
    void mergePurchase(MergeVo mergeVo);

    /**
     * 完成采购
     * @param doneVo
     */
    void done(PurchaseDoneVo doneVo);
}

