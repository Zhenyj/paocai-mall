package com.zyj.paocai.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.entity.to.*;
import com.zyj.paocai.common.entity.vo.MemberPrice;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.coupon.dao.SkuFullReductionDao;
import com.zyj.paocai.coupon.entity.MemberPriceEntity;
import com.zyj.paocai.coupon.entity.SkuBoundsEntity;
import com.zyj.paocai.coupon.entity.SkuFullReductionEntity;
import com.zyj.paocai.coupon.entity.SkuLadderEntity;
import com.zyj.paocai.coupon.service.MemberPriceService;
import com.zyj.paocai.coupon.service.SkuBoundsService;
import com.zyj.paocai.coupon.service.SkuFullReductionService;
import com.zyj.paocai.coupon.service.SkuLadderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Autowired
    SkuBoundsService skuBoundsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取sku当前启用的相关满减信息
     * @param skuId
     * @return
     */
    @Override
    public List<SkuFullReductionEntity> getActiveReductionsBySkuId(Long skuId) {
        return baseMapper.getActiveReductionsBySkuId(skuId);
    }

    /**
     * 保存sku优惠信息
     * @param skuReductionTo
     */
    @Transactional
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 1、sku的优惠、满减信息，paocai-sms数据库中sms_sku_ladder、sms_sku_full_reduction、sms_member_price
        // sms_sku_ladder
        saveSkuLadder(skuReductionTo);

        // 2、sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
            this.save(skuFullReductionEntity);
        }

        // 3、sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        saveMemberPrice(skuReductionTo, memberPrice);
    }

    /**
     * 获取sku优惠信息
     * @param skuId
     * @return
     */
    @Override
    public SkuPromotionTo getSkuPromotion(Long skuId) {
        SkuPromotionTo skuPromotionTo = new SkuPromotionTo();
        // 获取满减信息
        List<SkuFullReductionEntity> reductionEntities = getActiveReductionsBySkuId(skuId);
        if(!CollectionUtils.isEmpty(reductionEntities)){
            List<SkuFullReductionTo> reductionTos = reductionEntities.stream().map(item -> {
                SkuFullReductionTo to = new SkuFullReductionTo();
                BeanUtils.copyProperties(item, to);
                return to;
            }).collect(Collectors.toList());
            skuPromotionTo.setReductions(reductionTos);
        }
        // 获取阶梯价格信息
        List<SkuLadderEntity> ladderEntities = skuLadderService.getActiveLaddersBySkuId(skuId);
        if(!CollectionUtils.isEmpty(ladderEntities)){
            List<SkuLadderTo> ladders = ladderEntities.stream().map(item -> {
                SkuLadderTo to = new SkuLadderTo();
                BeanUtils.copyProperties(item, to);
                return to;
            }).collect(Collectors.toList());
            skuPromotionTo.setLadders(ladders);
        }
        // 获取商品sku积分信息
        SkuBoundsEntity bounds = skuBoundsService.getBoundsBySkuId(skuId);
        if(bounds != null){
            SkuBoundsTo skuBoundsTo = new SkuBoundsTo();
            BeanUtils.copyProperties(bounds,skuBoundsTo);
            skuPromotionTo.setBounds(skuBoundsTo);
        }
        return skuPromotionTo;
    }

    /**
     * 批量获取sku优惠信息
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuPromotionTo> getSkuPromotionBatch(List<Long> skuIds) {
        List<SkuPromotionTo> skuPromotionTos = skuIds.stream().map(skuId -> {
            SkuPromotionTo skuPromotion = getSkuPromotion(skuId);
            skuPromotion.setSkuId(skuId);
            return skuPromotion;
        }).collect(Collectors.toList());
        return skuPromotionTos;
    }


    /**
     * 保存sku阶梯价格信息
     * @param skuReductionTo
     */
    private void saveSkuLadder(SkuReductionTo skuReductionTo) {
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
    }

    /**
     * 保存会员价格信息
     * @param skuReductionTo
     * @param memberPrice
     */
    private void saveMemberPrice(SkuReductionTo skuReductionTo, List<MemberPrice> memberPrice) {
        List<MemberPriceEntity> memberPriceEntities = memberPrice.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(item -> {
            return item.getMemberPrice().compareTo(BigDecimal.ZERO) == 1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);
    }

}