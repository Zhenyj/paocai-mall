package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 11:19
 **/
@Data
public class AttrGroupWithAttrsVo {
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
     * 相关基本属性Json字符串
     */
    private String attrString;

    /**
     * 相关基本属性
     */
    private List<AttrEntity> attrs;

}
