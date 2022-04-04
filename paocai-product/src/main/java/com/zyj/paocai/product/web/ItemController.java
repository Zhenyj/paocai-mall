package com.zyj.paocai.product.web;

import com.zyj.paocai.product.entity.vo.SkuItemVo;
import com.zyj.paocai.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-01-17 19:35
 **/
@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.getItem(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }
}
