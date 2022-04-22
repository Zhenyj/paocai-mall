package com.zyj.paocai.common.entity.vo;

import com.zyj.paocai.common.entity.to.SkuFullReductionTo;
import com.zyj.paocai.common.entity.to.SkuLadderTo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车商品项
 * @author lulx
 * @date 2022-04-21 17:27
 **/
@Data
public class CartSkuItem {
    /**id*/
    private Long skuId;
    /**商品名*/
    private String skuName;
    /**标题*/
    private String skuTitle;
    /**副标题*/
    private String skuSubtitle;
    /**默认图片*/
    private String skuDefaultImg;
    /**单价*/
    private BigDecimal price;
    /**数量*/
    private Integer count;
    /**总价*/
    private BigDecimal totalPrice;
    /**优惠价格*/
    private BigDecimal discount;
    /**销售属性*/
    private List<AttrBaseVo> attrs;
    /**满减信息*/
    private List<SkuFullReductionTo> fullReductions;
    /**商品阶梯价格（满几件几折）*/
    private List<SkuLadderTo> ladders;
}
