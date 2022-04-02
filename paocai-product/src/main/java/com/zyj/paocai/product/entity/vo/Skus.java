package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.common.entity.to.MemberPrice;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lulx
 * @date 2022-04-01 16:12
 **/
@Data
public class Skus {
    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
