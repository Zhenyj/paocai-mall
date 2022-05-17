package com.zyj.paocai.product.web;

import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.entity.vo.HomePageData;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.product.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-04-04 13:35
 **/
@RestController
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    IndexService indexService;

    @RequestMapping("/product/index/data")
    public R<HomePageData> getHomeData() throws ExecutionException, InterruptedException {
        HomePageData vo = indexService.getHomeData();
        return R.ok(vo);
    }
}
