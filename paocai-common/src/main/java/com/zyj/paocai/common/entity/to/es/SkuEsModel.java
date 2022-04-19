package com.zyj.paocai.common.entity.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-03 12:53
 **/
@Data
public class SkuEsModel {

    /**
     * "skuId": {
     *      "type": "long"
     * }
     */
    private Long skuId;

    /**
     * "spuId": {
     *      "type": "keyword"
     * }
     */
    private Long spuId;

    /**
     * "skuTitle": {
     *      "type": "text",
     *      "analyzer": "ik_smart"
     * },
     */
    private String skuTitle;

    /**
     * "skuPrice": {
     * "type": "keyword"
     * },
     */
    private BigDecimal skuPrice;

    /**
     * "skuImg": {
     * "type": "keyword",
     * "index": false,
     * "doc_values": false
     * },
     */
    private String skuImg;

    /**
     * 销量
     * "saleCount": {
     * "type": "long"
     * },
     */
    private Long saleCount;

    /**
     * 是否有库存
     * "hasStock": {
     *      "type": "boolean"
     *  },
     */
    private Boolean hasStock;

    /**
     * 评分
     * "hotScore": {
     *      "type": "long"
     *  },
     */
    private Long hotScore;

    /**
     * 评论数
     * "commentNum": {
     *      "type": "long"
     * },
     */
    private Long commentCount;

    /**
     * "brandId": {
     *      "type": "long"
     *  },
     */
    private Long brandId;

    /**
     * "catalogId": {
     *      "type": "long"
     *  },
     */
    private Long catalogId;

    /**
     * "brandName": {
     *      "type": "keyword",
     *      "index": false,
     *      "doc_values": false
     * },
     */
    private String brandName;

    /**
     * "brandImg": {
     *      "type": "keyword",
     *      "index": false,
     *      "doc_values": false
     *  },
     */
    private String brandImg;

    /**
     * "catalogName": {
     *      "type": "keyword",
     *      "index": false,
     *      "doc_values": false
     *  },
     */
    private String catalogName;

    /**
     * 相关可搜索属性
     */
    private List<Attrs> attrs;


    /**
     * "attrs": {
     *      "type": "nested",
     *      "properties": {
     *          "attrId": {
     *            "type": "long"
     *          },
     *          "attrName": {
     *            "type": "keyword",
     *            "index": false,
     *            "doc_values": false
     *          },
     *          "attrValue": {
     *            "type": "keyword"
     *          }
     *      }
     *  }
     */
    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
