package com.zyj.paocai.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 商品检索条件vo
 *
 * @author lulx
 * @date 2022-04-16 15:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchParam {

    /** 匹配关键字 */
    private String keyword;

    /** 三级分类id */
    private Long catalog3Id;

    /**
     * 排序条件
     * sort=saleCount_asc/desc
     * sort=skuPrice_asc/desc
     * sort=hotScore_asc/desc
     */
    private String sort;

    /**
     * 价格区间
     * skuPrice=1_100/_100/100_
     */
    private String skuPrice;

    /** 品牌id */
    private List<Long> brandId;

    /**
     * 商品属性
     * attrs=属性id_属性值
     */
    private List<SearchResult.AttrVo> attrs;

    /** 是否只显示有货 */
    private Boolean hasStock;

    /** 泡菜物流 */
    private Boolean paocaiLogistics;

    /** 货到付款 */
    private Boolean payOnDeliver;

    /** 新品 */
    private Boolean newProduct;

    /** 会员专享 */
    private Boolean vip;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数据量 */
    private Integer pageSize;
}
