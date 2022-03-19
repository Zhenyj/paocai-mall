package com.zyj.paocai.product.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-03-19 16:47
 **/
@Data
public class AttrGroupWithCatelogPathVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 分类完整路径
     */
    private List<Long> catelogPath;
}
