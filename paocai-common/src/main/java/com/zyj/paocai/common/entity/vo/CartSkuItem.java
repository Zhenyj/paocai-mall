package com.zyj.paocai.common.entity.vo;

import com.zyj.paocai.common.entity.to.SkuFullReductionTo;
import com.zyj.paocai.common.entity.to.SkuLadderTo;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车商品项
 *
 * @author lulx
 * @date 2022-04-21 17:27
 **/
@Data
public class CartSkuItem {
    /** id */
    private Long skuId;
    /** 商品名 */
    private String skuName;
    /** 标题 */
    private String skuTitle;
    /** 副标题 */
    private String skuSubtitle;
    /** 默认图片 */
    private String skuDefaultImg;
    /**原价*/
    private BigDecimal originalPrice;
    /** 会员价（现价） */
    private BigDecimal price;
    /** 数量 */
    private Integer count = 1;
    /**原需总价*/
    private BigDecimal originalTotalPrice;
    /** 总价 */
    private BigDecimal totalPrice;
    /** 优惠价格 */
    private BigDecimal discount;
    /** 销售属性 */
    private List<AttrBaseVo> attrs;
    /** 满减信息 */
    private List<SkuFullReductionTo> fullReductions;
    /** 商品阶梯价格（满几件几折） */
    private List<SkuLadderTo> ladders;

    /**
     * 计算商品的价格
     */
    public void calculate() {
        originalTotalPrice = price.multiply(new BigDecimal(count));
        totalPrice = originalTotalPrice;
        discount = new BigDecimal(0);
        if (CollectionUtils.isEmpty(fullReductions) && CollectionUtils.isEmpty(ladders)) {
            return;
        }
        if (!CollectionUtils.isEmpty(fullReductions) && !CollectionUtils.isEmpty(ladders)) {
            // 既有满减优惠又有阶梯价格优惠
            calculateMaxDiscount();
        } else if (!CollectionUtils.isEmpty(fullReductions)) {
            // 只有满减优惠
            calculateOnlyFullReduction();
        } else {
            // 只有阶梯价格优惠
            calculateOnlyLadder();
        }
    }

    /**
     * 当既有满减优惠和阶梯价格优惠时计算最大优惠
     * 如果同时满足，则先进行阶梯价格优惠计算，再重新计算是否满足满减优惠
     */
    private void calculateMaxDiscount() {
        SkuLadderTo ladder = getSatisfyLadder();
        SkuFullReductionTo reduction = null;
        if(ladder != null){
            BigDecimal price = ladder.getPrice();
            // 先计算阶梯价格后，再重新计算是否符合满减优惠
            reduction = getSatisfyReduction(price.multiply(new BigDecimal(count)));
            // 只满足阶梯价格优惠
            if(reduction == null){
                totalPrice = price.multiply(new BigDecimal(count));
                discount = originalTotalPrice.subtract(totalPrice);
                return;
            }
            totalPrice = price.multiply(new BigDecimal(count)).subtract(reduction.getReducePrice());
            discount = originalTotalPrice.subtract(totalPrice);
            return;
        }
        // 只有满足满减优惠
        if(reduction != null){
            discount = reduction.getReducePrice();
            totalPrice = originalTotalPrice.subtract(discount);
        }
    }


    /**
     * 仅阶梯价格优惠
     */
    private void calculateOnlyLadder() {
        SkuLadderTo actionLadder = getSatisfyLadder();
        if (actionLadder != null) {
            totalPrice = actionLadder.getPrice().multiply(new BigDecimal(count));
            discount = originalTotalPrice.subtract(totalPrice);
        }
    }

    /**
     * 获取满足条件的最大优惠ladder
     *
     * @return
     */
    private SkuLadderTo getSatisfyLadder() {
        SkuLadderTo actionLadder = null;
        for (SkuLadderTo ladder : ladders) {
            if (ladder.getFullCount() <= count) {
                actionLadder = ladder;
                continue;
            }
            break;
        }
        return actionLadder;
    }

    /**
     * 仅存在满减优惠
     */
    private void calculateOnlyFullReduction() {
        SkuFullReductionTo activeReduction = getSatisfyReduction();
        if (activeReduction != null) {
            discount = activeReduction.getReducePrice();
            totalPrice = originalTotalPrice.subtract(discount);
        }
    }

    /**
     * 获取满足条件的最大满减优惠
     *
     * @return
     */
    private SkuFullReductionTo getSatisfyReduction() {
        SkuFullReductionTo activeReduction = null;
        for (SkuFullReductionTo fullReduction : fullReductions) {
            if (fullReduction.getFullPrice().compareTo(originalTotalPrice) != 1) {
                activeReduction = fullReduction;
                continue;
            }
            break;
        }
        return activeReduction;
    }

    /**
     * 获取满足条件的最大满减优惠
     * @param price
     * @return
     */
    private SkuFullReductionTo getSatisfyReduction(BigDecimal price){
        SkuFullReductionTo activeReduction = null;
        for (SkuFullReductionTo fullReduction : fullReductions) {
            if (fullReduction.getFullPrice().compareTo(price) != 1) {
                activeReduction = fullReduction;
                continue;
            }
            break;
        }
        return activeReduction;
    }
}
