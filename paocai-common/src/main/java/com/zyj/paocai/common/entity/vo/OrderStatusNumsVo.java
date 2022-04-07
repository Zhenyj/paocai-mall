package com.zyj.paocai.common.entity.vo;

import lombok.Data;

/**
 * 用户订单状态
 *
 * @author lulx
 * @date 2022-04-07 14:20
 **/
@Data
public class OrderStatusNumsVo {
    /** 购物车商品数量 */
    private Integer cartNum;
    /** 待收货订单数量 */
    private Integer waitReceiveNum;
    /** 待发货订单数量 */
    private Integer waitDeliverNum;
    /** 待付款订单数量 */
    private Integer waitPayNum;
    /** 待评论订单数量 */
    private Integer waitCommentNum;
}
