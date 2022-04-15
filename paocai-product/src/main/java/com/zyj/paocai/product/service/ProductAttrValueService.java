package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.ProductAttrValueEntity;
import com.zyj.paocai.product.entity.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询spu的所有规格属性
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId);

    /**
     * 获取spu规格
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> listBaseAttrForSpu(Long spuId);

    /**
     * 修改商品规格
     * @param entities
     * @param spuId
     */
    void updateBaseAttrForSpu(List<ProductAttrValueEntity> entities, Long spuId);

    /**
     * 获取spu相关可搜索属性
     * @param spuId
     * @return
     */
    List<SkuEsModel.Attrs> getSearchAttrs( Long spuId);

    /**
     * 获取spu快速展示属性
     * @param spuId
     * @return
     */
    List<ProductAttrValueEntity> getQuickShowAttrBySpuId(Long spuId);
}

