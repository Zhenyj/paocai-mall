package com.zyj.paocai.product.web;

import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.vo.Catalog2Vo;
import com.zyj.paocai.product.entity.vo.HomePageData;
import com.zyj.paocai.product.service.CategoryService;
import com.zyj.paocai.product.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-04-04 13:35
 **/
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    IndexService indexService;

    @RequestMapping("/product/index/data")
    @ResponseBody
    public R<HomePageData> main() throws ExecutionException, InterruptedException {
        HomePageData vo = indexService.getMain();
        return R.ok(vo);
    }

    @RequestMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // 查出所有一级分类
        List<CategoryEntity> categorys = categoryService.getLevel1Categorys();
        model.addAttribute("categorys", categorys);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/json/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        Map<String, List<Catalog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }
}
