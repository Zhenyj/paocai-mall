package com.zyj.paocai.product.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lulx
 * @date 2022-03-20 17:26
 **/
@Data
public class AttrBaseVo implements Serializable {

    private static final long serialVersionUID = 4290774380558810001L;

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

    /**
     * 值类型【0：单值；1：多值】
     */
    private Integer valueType;

    /** 属性分组名 */
    private String attrGroupName;

    /** 所属分类名 */
    private String catelogName;
}
