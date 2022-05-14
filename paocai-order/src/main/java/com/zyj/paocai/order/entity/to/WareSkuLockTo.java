package com.zyj.paocai.order.entity.to;

import com.zyj.paocai.common.entity.vo.CartSkuItem;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-01-29 20:27
 **/
@Data
public class WareSkuLockTo {
    private String orderSn;
    private List<CartSkuItem> locks;
}
