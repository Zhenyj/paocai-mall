package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:24:53
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

