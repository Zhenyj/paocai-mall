package com.zyj.paocai.common.entity.vo;

import lombok.Data;

/**
 * 品牌信息
 * @author lulx
 * @date 2022-04-22 0:42
 **/
@Data
public class BrandVo {
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */
    private String name;
    /**
     * 品牌logo地址
     */
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;
    /**
     * 检索首字母
     */
    private String firstLetter;
    /**
     * 排序
     */
    private Integer sort;

}
