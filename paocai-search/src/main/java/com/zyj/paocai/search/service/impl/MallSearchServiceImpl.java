package com.zyj.paocai.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.common.entity.to.es.SkuEsModel;
import com.zyj.paocai.common.entity.vo.CatalogBaseVo;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.search.config.ElasticSearchConfig;
import com.zyj.paocai.search.constant.EsConstant;
import com.zyj.paocai.search.feign.ProductFeignService;
import com.zyj.paocai.search.service.MallSearchService;
import com.zyj.paocai.search.vo.BrandVo;
import com.zyj.paocai.search.vo.SearchParam;
import com.zyj.paocai.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * @author lulx
 * @date 2022-01-13 17:27
 **/
@Slf4j
@Service
public class MallSearchServiceImpl implements MallSearchService {

    private static final String BASE_LINK = "http://search.paocai.com/list.html?";

    @Autowired
    RestHighLevelClient client;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    /**
     * 检索商品
     *
     * @param param 检索参数
     * @return
     */
    @Override
    public SearchResult search(SearchParam param) throws IOException, ExecutionException, InterruptedException {
        // 检索条件
        SearchRequest request = buildSearchRequest(param);
        SearchResult result = null;
        // 检索请求
        SearchResponse response = client.search(request, ElasticSearchConfig.COMMON_OPTIONS);
        // 分析数据封装成特定格式
        result = buildSearchResult(param, response);
        return result;
    }

    /**
     * 封装结果
     *
     * @param response 响应数据
     * @return
     */
    private SearchResult buildSearchResult(SearchParam param, SearchResponse response) throws ExecutionException, InterruptedException {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
        // 总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        // 当前页码
        result.setPageNum(param.getPageNum());
        Integer pageSize = param.getPageSize();
        // 总页数
        int totalPages = (int) total / pageSize;
        if (total % pageSize != 0) {
            totalPages = totalPages + 1;
        }
        result.setTotalPages(totalPages);

        Long catalogId = param.getCatalog3Id();
        boolean catalogFlg = catalogId != null;
        CompletableFuture<Void> catalogsFuture = null;
        CompletableFuture<Void> catalogThreeListFuture = null;
        if (catalogFlg) {
            // 获取分类路径
            catalogsFuture = CompletableFuture.runAsync(() -> {
                R<List<CatalogBaseVo>> r = productFeignService.getCatalogs(catalogId);
                if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                    throw new RRException("获取相关分类路径失败,catalogId:" + catalogId,r.getCode());
                }
                result.setCatalogs(r.getData());
            }, executor);

            // 获取同级分类数据
            catalogThreeListFuture = CompletableFuture.runAsync(() -> {
                R<List<CatalogBaseVo>> r = productFeignService.getSameLevelCategory(catalogId);
                if (!Constant.SUCCESS_CODE.equals(r.getCode())) {
                    throw new RRException("获取相关分类路径失败,catalogId:" + catalogId,r.getCode());
                }
                result.setCatalogThreeList(r.getData());
            }, executor);
        }

        // 相关商品数据
        List<SkuEsModel> products = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(hits.getHits())) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                if (skuTitle != null) {
                    esModel.setSkuTitle(skuTitle.getFragments()[0].toString());
                }
                products.add(esModel);
            }
        }
        result.setProducts(products);

        // 已选择的筛选属性条件
        List<SearchResult.AttrVo> attrs = param.getAttrs();
        if(!CollectionUtils.isEmpty(attrs)){
            List<Long> attrIds = attrs.stream().map(attr -> {
                return attr.getAttrId();
            }).collect(Collectors.toList());
            result.setAttrIds(attrIds);
        }

        Aggregations aggs = response.getAggregations();
        // 商品涉及的属性信息
        ParsedNested attrAgg = aggs.get("attr_agg");
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            long attrId = bucket.getKeyAsNumber().longValue();
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg"))
                    .getBuckets().stream().map(item -> {
                        String keyAsString = item.getKeyAsString();
                        return keyAsString;
                    }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);

        // 商品涉及到的品牌
        ParsedLongTerms brandAgg = aggs.get("brand_agg");
        List<BrandVo> brands = new ArrayList<>();
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            BrandVo brandVo = new BrandVo();
            String keyAsString = bucket.getKeyAsString();
            brandVo.setBrandId(Long.parseLong(keyAsString));

            if (((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().size() > 0) {
                String brandName = ((ParsedStringTerms) bucket.getAggregations()
                        .get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
                brandVo.setBrandName(brandName);
            }
            if (((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().size() > 0) {
                String brandImg = ((ParsedStringTerms) bucket.getAggregations()
                        .get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
                brandVo.setBrandImg(brandImg);
            }

            brands.add(brandVo);
        }
        result.setBrands(brands);



        // 分类聚合解析
//        ParsedLongTerms catalogAgg = aggs.get("catalog_agg");
//        List<CatalogBaseVo> catalogs = new ArrayList<>();
//        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
//            CatalogBaseVo catalogVo = new CatalogBaseVo();
//            String keyAsString = bucket.getKeyAsString();
//            catalogVo.setCatalogId(Long.parseLong(keyAsString));
//
//            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
//            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
//            catalogVo.setCatalogName(catalogName);
//
//            catalogs.add(catalogVo);
//        }
//        result.setCatalogs(catalogs);

        if (catalogFlg) {
            CompletableFuture.allOf(catalogsFuture, catalogThreeListFuture).get();
        }

        return result;
    }

    /**
     * 构造检索DSL
     *
     * @param param 检索参数
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1、查询：模糊匹配，过滤（按照属性、分类、品牌、价格区间、库存等）
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1 must-模糊匹配
        if (StringUtils.hasText(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // 1.2 过滤
        if (param.getCatalog3Id() != null) {
            // 三级分类id
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        if (!CollectionUtils.isEmpty(param.getBrandId())) {
            // 品牌id
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        if (param.getHasStock() != null && param.getHasStock()) {
            // 是否有库存
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock()));
        }
        if (StringUtils.hasText(param.getSkuPrice())) {
            // 价格区间
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] split = param.getSkuPrice().split("_");
            if (split.length == 2) {
                rangeQuery.lte(split[1]);
                if (!param.getSkuPrice().startsWith("_")) {
                    rangeQuery.gte(split[0]);
                }
            } else {
                rangeQuery.gte(split[0]);
            }
            boolQuery.filter(rangeQuery);
        }
        if (!CollectionUtils.isEmpty(param.getAttrs())) {
            // 商品属性过滤
            for (SearchResult.AttrVo attr : param.getAttrs()) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                // 属性id
                String attrId = attr.getAttrId().toString();
                // 属性值
                List<String> attrValues = attr.getAttrValue();
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        sourceBuilder.query(boolQuery);

        // 排序
        if (StringUtils.hasText(param.getSort())) {
            String[] split = param.getSort().split("_");
            if (split.length == 2) {
                if (split[1].equalsIgnoreCase(SortOrder.DESC.toString())) {
                    sourceBuilder.sort(split[0], SortOrder.DESC);
                } else {
                    sourceBuilder.sort(split[0], SortOrder.ASC);
                }
            }
        }
        //分页
        Integer pageSize = param.getPageSize();
        sourceBuilder.from(pageSize * (param.getPageNum() - 1));
        sourceBuilder.size(pageSize);

        //高亮
        if (StringUtils.hasText(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle").preTags("<b style='color: #ff5500;'>").postTags("</b>");
            sourceBuilder.highlighter(builder);
        }

        // 聚合分析
        // 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg")
                .field("brandId").size(50);
        // 品牌聚合的子聚合(品牌名、图片)
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg")
                .field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg")
                .field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        // 分类聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg")
                .field("catalogId").size(20);
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg")
                .field("catalogName").size(1));
        sourceBuilder.aggregation(catalogAgg);

        // 属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        // 聚合所有属性id
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // 聚合分析属性id的所有属性值
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(attrAgg);

        log.info(sourceBuilder.toString());
        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return request;
    }
}
