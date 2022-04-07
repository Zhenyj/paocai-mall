package com.zyj.paocai.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页推广
 *
 * @author lulx
 * @date 2022-04-07 16:40
 **/
@Data
@TableName(value = "pms_promote")
public class PromoteEntity implements Serializable {
    /**
     * 推广id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 推广名
     */
    private String promoteName;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片名
     */
    private String imgName;

    /**
     * 推广类型
     */
    private Integer type;

    /**
     * 点击跳转的链接
     */
    private String promoteUrl;

    /**
     * 是否显示
     */
    private Integer showStatus;
}