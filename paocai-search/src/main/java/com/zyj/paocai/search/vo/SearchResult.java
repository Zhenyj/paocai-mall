package com.zyj.paocai.search.vo;

import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.entity.vo.CatalogBaseVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索返回结果
 *
 * @author lulx
 * @date 2022-04-16 15:50
 **/
@Data
public class SearchResult {
    /** 查询到的商品信息 */
    private List<SkuEsModel> products;

    /** 当前页数 */
    private Integer pageNum;

    /** 总记录数 */
    private Long total;

    /** 总页码 */
    private Integer totalPages;

    /** 相关品牌信息 */
    private List<BrandVo> brands;

    /** 涉及的所有属性 */
    private List<AttrVo> attrs;

    /** 涉及到的分类 */
    private List<CatalogBaseVo> catalogs;

    /** 同级分类（三级分类） */
    private List<CatalogBaseVo> catalogThreeList;

    /** 便于判断当前id是否被使用 */
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
