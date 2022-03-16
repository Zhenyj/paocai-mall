package com.zyj.paocai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyj.paocai.utils.PageUtils;
import com.zyj.paocai.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 *
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

