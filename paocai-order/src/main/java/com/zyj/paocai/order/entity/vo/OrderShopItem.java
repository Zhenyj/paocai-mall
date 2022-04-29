package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.common.entity.vo.CartSkuItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单店铺项
 * @author lulx
 * @date 2022-04-29 10:15
 **/
public class OrderShopItem {
    /**店铺id*/
    private Long brandId;
    /**店铺名*/
    private String brandName;
    /**商品*/
    private List<CartSkuItem> items;
    /**店铺优惠金额*/
    private BigDecimal shopDiscount;
    /**运费*/
    private BigDecimal freightAmount;
    /**是否有运费险*/
    private Boolean freightInsurance = true;
    /**运费险金额*/
    private BigDecimal freightInsurancePrice = new BigDecimal(0);
    /**是否开发票*/
    private Boolean isInvoice;
    /**发票信息类型*/
    private List<BillType> billTypeList;
    /**发票类型*/
    private Integer billType = 2;
    /**发票抬头*/
    private String billHeader;
    /**备注*/
    private String note;
    /**物流公司(配送方式)*/
    private String deliveryCompany;
    /**订单金额*/
    private BigDecimal totalAmount;
    /**应付总额*/
    private BigDecimal payAmount;
    /**促销优惠金额（促销价、满减、阶梯价）*/
    private BigDecimal promotionAmount;
    /**积分抵扣金额*/
    private BigDecimal integrationAmount;
    /**使用的优惠券*/
    private Long couponId;
    /**优惠券金额*/
    private BigDecimal couponAmount;
    /**后台调整订单使用的折扣金额*/
    private BigDecimal discountAmount;
    /**下单可得积分*/
    private BigDecimal integration;
    /**下单可得会员成长值*/
    private BigDecimal growth;
    /**店铺可选优惠项*/
    private List<ShopDiscountOptionVo> shopDiscountOptions;
    /**默认优惠项、已选优惠项*/
    private ShopDiscountOptionVo discountOption;
}
