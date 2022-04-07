package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.product.entity.PromoteEntity;

import java.util.List;

/**
 *
 */
public interface PromoteService extends IService<PromoteEntity> {

    /**
     * 获取首页推广资源
     *
     * @return
     */
    List<PromoteEntity> getShowPromote();
}
