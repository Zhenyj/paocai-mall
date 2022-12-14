package com.zyj.paocai.common.entity.vo;


import com.zyj.paocai.common.utils.BigDecimalUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 订单店铺项
 *
 * @author lulx
 * @date 2022-04-29 10:15
 **/
@Data
public class ShopItem {

    /** 店铺id */
    private Long brandId;
    /** 店铺名 */
    private String brandName;
    /** 商品 */
    private LinkedList<CartSkuItem> items = new LinkedList<>();
    /** 店铺优惠金额 */
    private BigDecimal shopDiscount;
    /** 运费 */
    private BigDecimal freightAmount;
    /** 是否有运费险 */
    private Boolean freightInsurance = true;
    /** 运费险金额 */
    private BigDecimal freightInsurancePrice = new BigDecimal(0);
    /** 订单金额 */
    private BigDecimal totalAmount;
    /** 应付总额 */
    private BigDecimal payAmount;
    /** 备注 */
    private String note;
    /** 物流公司(配送方式) */
    private String deliveryCompany;
    /** 促销优惠金额（促销价、满减、阶梯价） */
    private BigDecimal promotionAmount;
    /** 积分抵扣金额 */
    private BigDecimal integrationAmount;
    /** 使用的优惠券 */
    private Long couponId;
    /** 优惠券金额 */
    private BigDecimal couponAmount;
    /** 后台调整订单使用的折扣金额 */
    private BigDecimal discountAmount;
    /** 下单可得积分 */
    private BigDecimal integration;
    /** 下单可得会员成长值 */
    private BigDecimal growth;
    /** 店铺可选优惠项 */
    private List<ShopDiscountOptionVo> shopDiscountOptions;
    /** 默认优惠项、已选优惠项 */
    private ShopDiscountOptionVo discountOption;
    /** 是否开发票 */
    private Boolean isInvoice;
    /** 发票信息类型 */
    private List<BillType> billTypeList;
    /** 发票类型 */
    private Integer billType = 2;
    /** 发票抬头 */
    private String billHeader;

    /**
     * 计算店铺价格及优惠
     */
    public void calculate() {
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal payAmount = new BigDecimal(0);
        BigDecimal promotionAmount = new BigDecimal(0);
        BigDecimal integration = new BigDecimal(0);
        BigDecimal growth = new BigDecimal(0);
        BigDecimal freightAmount = new BigDecimal(0);
        BigDecimal integrationAmount = new BigDecimal(0);
        BigDecimal couponAmount = new BigDecimal(0);
        BigDecimal discountAmount = new BigDecimal(0);
        String discountTitle = "";
        for (CartSkuItem item : items) {
            item.calculate();
            totalAmount = totalAmount.add(item.getTotalAmount());
            payAmount = payAmount.add(item.getPayAmount());
            integration = integration.add(item.getIntegration());
            growth = growth.add(item.getGrowth());
            if (item.getPromotionAmount().compareTo(BigDecimal.ZERO) > 0) {
                // 有优惠需添加选项
                if (promotionAmount.compareTo(BigDecimal.ZERO) > 0) {
                    discountTitle = "组合优惠";
                } else {
                    discountTitle = item.getSkuName();
                }
                promotionAmount = promotionAmount.add(item.getPromotionAmount());
            }
        }
        if (promotionAmount.compareTo(BigDecimal.ZERO) > 0) {
            shopDiscountOptions = new ArrayList<>(2);
            ShopDiscountOptionVo maxDiscountOption =
                    new ShopDiscountOptionVo(discountTitle + ":" + BigDecimalUtils.formatToNumber(promotionAmount), promotionAmount);
            // 默认选中最大优惠
            discountOption = maxDiscountOption;
            // 添加可选优惠选项
            shopDiscountOptions.add(maxDiscountOption);
            // 不需要优惠选项
            shopDiscountOptions.add(ShopDiscountOptionVo.noUseDiscount());
        }
        this.promotionAmount = promotionAmount;
        this.totalAmount = totalAmount;
        this.payAmount = payAmount;
        this.integration = integration;
        this.growth = growth;
        this.freightAmount = freightAmount;
        this.integrationAmount = integrationAmount;
        this.couponAmount = couponAmount;
        this.discountAmount = discountAmount;
    }

    public CartSkuItem getItemBySkuId(Long skuId) {
        if (items != null && items.size() > 0) {
            for (CartSkuItem item : items) {
                if (skuId.equals(item.getSkuId())) {
                    return item;
                }
            }
        }
        return null;
    }

    public void addItem(CartSkuItem cartSkuItem){
        items.add(cartSkuItem);
    }
}
