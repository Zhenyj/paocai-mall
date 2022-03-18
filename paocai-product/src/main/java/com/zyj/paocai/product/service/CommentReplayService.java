package com.zyj.paocai.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.common.utils.PageUtils;
import com.zyj.paocai.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:19:43
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

