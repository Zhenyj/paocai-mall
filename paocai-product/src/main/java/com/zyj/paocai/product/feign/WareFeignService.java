package com.zyj.paocai.product.feign;

import com.zyj.paocai.common.entity.to.SkuHasStockVo;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 22:41
 **/
@FeignClient(name = "paocai-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/hasstock")
    R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
