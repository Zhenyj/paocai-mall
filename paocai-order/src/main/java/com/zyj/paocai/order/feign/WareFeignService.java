package com.zyj.paocai.order.feign;

import com.zyj.paocai.common.entity.vo.SkuHasStockVo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-29 17:13
 **/
@FeignClient(name = "paocai-ware")
public interface WareFeignService {

    /**
     * 批量获取sku是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock/batch")
    R<List<SkuHasStockVo>> getSkuHasStockBatch(@RequestBody List<Long> skuIds);

    /**
     * 获取sku是否有库存
     * @param skuId
     * @return
     */
    @PostMapping("/ware/waresku/hasstock/{skuId}")
    R<SkuHasStockVo> getSkuHasStock(@PathVariable("skuId") Long skuId);
}
