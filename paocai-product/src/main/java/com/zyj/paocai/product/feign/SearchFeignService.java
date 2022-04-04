package com.zyj.paocai.product.feign;

import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 22:54
 **/
@FeignClient("paocai-search")
public interface SearchFeignService {

    /**
     * 上架商品
     * @param skuEsModels
     * @return
     */
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
