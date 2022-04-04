package com.zyj.paocai.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.ware.dao.PurchaseDetailDao;
import com.zyj.paocai.ware.entity.PurchaseDetailEntity;
import com.zyj.paocai.ware.service.PurchaseDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Slf4j
@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        // key=123&status=0&wareId=1
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)) {
            wrapper.and(w -> {
                w.eq("id", key).or().eq("purchase_id", key);
            });
        }

        String status = (String) params.get("status");
        if (StringUtils.hasText(status)) {
            wrapper.eq("status", Integer.parseInt(status));
        }

        String wareId = (String) params.get("wareId");
        if (StringUtils.hasText(wareId)) {
            Long id = Long.parseLong(wareId);
            if (id > 0) {
                wrapper.eq("ware_id", id);
            }
        }


        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
        List<PurchaseDetailEntity> purchaseId = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));

        return purchaseId;
    }

}