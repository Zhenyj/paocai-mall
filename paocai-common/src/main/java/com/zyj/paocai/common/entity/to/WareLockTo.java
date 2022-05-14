package com.zyj.paocai.common.entity.to;

import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-05-10 15:57
 **/
@Data
public class WareLockTo {
    /** 订单编号 */
    private String orderSn;
    /** sku数量*/
    private List<SkuIdCountVo> skuIdCountVos;
}
