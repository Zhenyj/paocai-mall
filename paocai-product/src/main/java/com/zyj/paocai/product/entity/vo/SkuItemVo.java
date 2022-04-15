package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.common.entity.vo.SkuBoundsVo;
import com.zyj.paocai.common.entity.vo.SkuDetailVo;
import com.zyj.paocai.product.entity.SkuImagesEntity;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-04 14:29
 **/
@Data
public class SkuItemVo {
    /** sku基本信息 */
    private SkuDetailVo skuInfo;

    /** 是否有库存 */
    private boolean hasStock = true;

    /** sku图片信息 */
    private List<SkuImagesEntity> images;

    /** spu销售属性组合 */
    private List<SkuItemSaleAttrVo> saleAttrs;

    /** spu描述图片信息 */
    private List<String> descImages;

    /** spu介绍参数(快速展示) */
    private List<SpuBaseAttrVo> introduceParameters;

    /** spu规格参数信息 */
    private List<SpuItemAttrGroupVo> groupAttrs;

    /** 商品spu积分 */
    private SkuBoundsVo bounds;

    /** 秒杀信息 */
    private SeckillInfoVo seckillInfo;

    @Data
    public static class SpuBaseAttrVo {
        private String attrName;

        private String attrValue;
    }

    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> attrs;
    }

    /**
     * sku销售属性
     */
    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;

        private String attrName;

        private List<AttrValueWithSkuIdVo> attrValues;

        @Data
        public static class AttrValueWithSkuIdVo {
            private String skuIds;
            private String attrValue;
        }
    }


}
