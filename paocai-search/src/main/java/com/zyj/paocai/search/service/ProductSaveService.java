package com.zyj.paocai.search.service;

import com.zyj.paocai.common.entity.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 22:14
 **/
public interface ProductSaveService {
    /**
     * 商品上架
     * @param skuEsModels
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
