package com.zyj.paocai.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发票信息
 * @author lulx
 * @date 2022-04-29 10:17
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillType {
    private String typeName;
    private int typeValue;
}
