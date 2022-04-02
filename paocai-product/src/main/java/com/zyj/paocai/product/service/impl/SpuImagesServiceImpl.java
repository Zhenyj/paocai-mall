package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.common.utils.Query;
import com.zyj.paocai.product.dao.SpuImagesDao;
import com.zyj.paocai.product.entity.SpuImagesEntity;
import com.zyj.paocai.product.service.SpuImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存spu图片
     * @param spuId
     * @param images
     */
    @Transactional
    @Override
    public void saveImages(Long spuId, List<String> images) {
        if(images == null || images.size() == 0){
            return;
        }
        List<SpuImagesEntity> collect = images.stream().map(image -> {
            SpuImagesEntity entity = new SpuImagesEntity();
            entity.setSpuId(spuId);
            entity.setImgUrl(image);
            return entity;
        }).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(collect)){
            this.saveBatch(collect);
        }
    }

}