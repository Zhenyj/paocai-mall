package com.zyj.paocai.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author llx
 * @date 2022-04-04 13:38
 * <p>
 * 二级分类vo
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catalog2Vo {
    /*一级父分类id*/
    private String catalogId;
    /*三级子分类*/
    private List<Catalog3Vo> catalog3List;
    /*当前节点的id*/
    private String id;
    /*当前节点的name*/
    private String name;


    /**
     * 三级分类vo
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Catalog3Vo {
        /*二级父分类id*/
        private String catalog2Id;
        /*当前节点的id*/
        private String id;
        /*当前节点的name*/
        private String name;
    }
}
