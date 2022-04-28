package com.zyj.paocai.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyj.paocai.common.constant.Constant;
import com.zyj.paocai.product.dao.PromoteDao;
import com.zyj.paocai.product.entity.PromoteEntity;
import com.zyj.paocai.product.service.PromoteService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class PromoteServiceImpl extends ServiceImpl<PromoteDao, PromoteEntity> implements PromoteService {

    @Override
    public List<PromoteEntity> getShowPromote() {
        return baseMapper.selectList(new QueryWrapper<PromoteEntity>().eq("show_status", Constant.ONE));

    }
}
