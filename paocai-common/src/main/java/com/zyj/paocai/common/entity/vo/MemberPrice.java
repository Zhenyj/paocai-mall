package com.zyj.paocai.common.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lulx
 * @date 2022-04-01 16:16
 **/
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
