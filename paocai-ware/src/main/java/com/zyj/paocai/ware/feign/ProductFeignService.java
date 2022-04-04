package com.zyj.paocai.ware.feign;

import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author lulx
 * @date 2022-04-04 10:08
 **/
@FeignClient("paocai-product")
public interface ProductFeignService {

    @PostMapping("/product/skuinfo/skunameinfos")
    R<Map<Long,String>> getSkuNameInfos(@RequestBody List<Long> skuIds);
}
