package com.zyj.paocai.search.constant;

/**
 * @author lulx
 * @date 2022-04-01 22:21
 **/
public class EsConstant {

    /**
     * sku数据在es中的索引
     */
    public static final String PRODUCT_INDEX = "paocai_product";
    /**
     * 有库存
     */
    public static final Integer HAS_STOCK = 1;
    /**
     * 没有库存
     */
    public static final Integer NO_STOCK = 0;

    public static final Integer PAGE_SIZE = 30;


}
