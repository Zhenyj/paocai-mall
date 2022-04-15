package com.zyj.paocai.common.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * sku信息
 *
 * @author lulx
 * @email
 * @date 2022-04-16 00:48:43
 */
@Data
public class SkuDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * skuId
     */
    private Long skuId;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * sku名称
     */
    private String skuName;
    /**
     * sku介绍描述
     */
    private String skuDesc;
    /**
     * 所属分类id
     */
    private Long catalogId;

    /**
     * 分类路径
     */
    private List<CatalogBaseVo> catalogPath;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌名
     */
    private String brandName;

    /**
     * 默认图片
     */
    private String skuDefaultImg;
    /**
     * 标题
     */
    private String skuTitle;
    /**
     * 副标题
     */
    private String skuSubtitle;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 销量
     */
    private Long saleCount;

    /**
     * 重量
     */
    private BigDecimal weight;

}
