package com.zyj.paocai.search.controller;

import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 22:12
 **/
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    /**
     * 上架商品
     *
     * @param skuEsModels
     * @return
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误:{}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),
                    BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),
                    BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
