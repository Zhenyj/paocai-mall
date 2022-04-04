package com.zyj.paocai.product.dao;

import com.zyj.paocai.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu信息
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    /**
     * 修改spu上架状态
     * @param spuId
     * @param code
     */
    void updateSpuStatus(Long spuId, int code);
}
