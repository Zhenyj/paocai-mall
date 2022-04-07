package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.product.entity.SkuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * 热销、猜你喜欢
 *
 * @author lulx
 * @date 2022-04-07 14:56
 **/
@Data
public class HotSale {
    /** 当前页数 */
    private Integer page = 1;
    /** 总页数 */
    private Integer totalPage = 3;
    /** 每页数据量 */
    private Integer pageSize = 20;
    /** 总数据量 */
    private Integer total;
    /** 热销、猜你喜欢商品 */
    private List<SkuInfoEntity> hotSales;
}
