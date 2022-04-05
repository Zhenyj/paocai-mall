package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.product.entity.SkuImagesEntity;
import com.zyj.paocai.product.entity.SkuInfoEntity;
import com.zyj.paocai.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-04 14:29
 **/
@Data
public class SkuItemVo {
    /** sku基本信息 */
    private SkuInfoEntity info;

    /** 是否有库存 */
    private boolean hasStock = true;

    /** sku图片信息 */
    private List<SkuImagesEntity> images;

    /** spu销售属性组合 */
    private List<SkuItemSaleAttrVo> saleAttrs;

    /** spu描述信息 */
    private SpuInfoDescEntity desp;

    /** spu规格参数信息 */
    private List<SpuItemAttrGroupVo> groupAttrs;

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
