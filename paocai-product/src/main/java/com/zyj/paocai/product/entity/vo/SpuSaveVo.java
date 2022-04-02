package com.zyj.paocai.product.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 16:09
 **/
@Data
public class SpuSaveVo {
    /**
     * spu名
     */
    @NotNull
    private String spuName;
    /**
     * spu描述
     */
    private String spuDescription;

    /**
     * 分类id
     */
    @NotNull
    private Long catalogId;
    /**
     * 品牌id
     */
    @NotNull
    private Long brandId;
    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 状态
     */
    @NotNull
    private int publishStatus;

    /**
     * 描述
     */
    private List<String> decript;
    /**
     * 图片
     */
    private List<String> images;
    /**
     * 奖励
     * */
    private Bounds bounds;
    /**
     * 相关基本属性
     * */
    private List<BaseAttrs> baseAttrs;
    /**
     * skus
     */
    private List<Skus> skus;
}
