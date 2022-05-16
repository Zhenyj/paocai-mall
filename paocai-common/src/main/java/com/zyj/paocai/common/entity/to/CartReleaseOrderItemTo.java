package com.zyj.paocai.common.entity.to;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lulx
 * @date 2022-05-15 22:09
 **/
@Data
public class CartReleaseOrderItemTo implements Serializable {
    /** 会员号 */
    private Long memberId;
    /** 要删除的skuId,rabbitmq不能反序列化List所以使用String类型拼接相关skuId */
    private String skuIdStr;
}
