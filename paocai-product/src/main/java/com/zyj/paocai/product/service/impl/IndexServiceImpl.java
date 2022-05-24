package com.zyj.paocai.product.service.impl;

import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.RRException;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.PromoteEntity;
import com.zyj.paocai.product.entity.SkuInfoEntity;
import com.zyj.paocai.product.entity.vo.HomePageData;
import com.zyj.paocai.product.entity.vo.HotSale;
import com.zyj.paocai.product.entity.vo.SmallPromote;
import com.zyj.paocai.product.feign.OrderFeignService;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.product.service.IndexService;
import com.zyj.paocai.product.service.PromoteService;
import com.zyj.paocai.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 主页业务实现类
 *
 * @author lulx
 * @date 2022-04-07 12:20
 **/
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    private static final int BIG_TYPE = 0;
    private static final int SMALL_TYPE = 1;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    PromoteService promoteService;

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public HomePageData getHomeData() throws ExecutionException, InterruptedException {
        HomePageData vo = new HomePageData();
        // 获取三级分裂
        CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(() -> {
            List<CategoryEntity> category = categoryService.listTree();
            vo.setCategory(category);
        }, executor);

        // 获取搜索热词
        CompletableFuture<Void> hotWordsFuture = CompletableFuture.runAsync(() -> {
            // TODO 暂时使用固定数据
            String[] strings = new String[]{"新款连衣裙", "四件套", "潮流T恤", "时尚女鞋", "短裤", "半身裙",
                    "男士外套", "墙纸", "行车记录仪", "新款男鞋", "耳机", "时尚女包", "沙发"};
            ArrayList<String> hotWords = new ArrayList<>(Arrays.asList(strings));
            vo.setHotWords(hotWords);
        }, executor);

        // 获取首页推广资源
        CompletableFuture<Void> promoteFuture = CompletableFuture.runAsync(() -> {
            List<PromoteEntity> promotes = promoteService.getShowPromote();
            if (CollectionUtils.isEmpty(promotes)) {
                log.warn("没有首页推广资源");
                throw new RRException("没有首页推广资源", BizCodeEnum.PRODUCT_SERVICE_EXCEPTION.getCode());
            }
            // 首页顶部大图推广资源
            List<PromoteEntity> promoteCarousel = new ArrayList<>(6);
            List<PromoteEntity> smalls = new ArrayList<>(10);
            for (PromoteEntity promote : promotes) {
                if (promote.getType().equals(BIG_TYPE)) {
                    promoteCarousel.add(promote);
                } else {
                    smalls.add(promote);
                }
            }
            // 首页顶部小图推广资源
            List<SmallPromote> smallPromoteCarousel = new ArrayList<>(5);
            if (smalls.size() > 0) {
                for (int i = 1; i < smalls.size(); i = i + 2) {
                    SmallPromote smallPromote = new SmallPromote();
                    smallPromote.setLeft(smalls.get(i - 1));
                    smallPromote.setRight(smalls.get(i));
                    smallPromoteCarousel.add(smallPromote);
                }
            }
            vo.setPromoteCarousel(promoteCarousel);
            vo.setSmallPromoteCarousel(smallPromoteCarousel);
        }, executor);

        // 获取猜你喜欢、热销商品数据
        CompletableFuture<Void> hotSaleFuture = CompletableFuture.runAsync(() -> {
            HotSale hotSale = new HotSale();
            List<SkuInfoEntity> hotSales = skuInfoService.getHotSales(hotSale.getPageSize(), hotSale.getPage());
            hotSale.setTotal(hotSales.size());
            hotSale.setHotSales(hotSales);
            vo.setHotSale(hotSale);
        }, executor);

        CompletableFuture.allOf(categoryFuture, hotWordsFuture, promoteFuture, hotSaleFuture).get();

        return vo;
    }
}
