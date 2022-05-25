package com.zyj.paocai.product.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zyj.paocai.product.entity.vo.HomePageData;

import java.util.concurrent.ExecutionException;

/**
 * 主页业务
 * @author lulx
 * @date 2022-04-07 12:20
 **/
public interface IndexService {
    /**
     * 获取主页数据
     * @return
     */
    HomePageData getHomeData() throws ExecutionException, InterruptedException, BlockException;
}
