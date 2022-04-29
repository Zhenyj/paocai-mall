package com.zyj.paocai.order.entity.vo;

import com.zyj.paocai.common.entity.to.AddressTo;
import lombok.Data;

import java.util.List;

/**
 * 订单确认页，返回结果
 * @author lulx
 * @date 2022-04-29 10:03
 **/
@Data
public class OrderConfirmVo {
    /**用户所有收货地址*/
    private List<AddressTo> addressList ;
    /**订单信息*/
    private OrderInfoVo orderInfo;
}
