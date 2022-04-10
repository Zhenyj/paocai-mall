package com.zyj.paocai.search.controller;

import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.search.service.MallSearchService;
import com.zyj.paocai.search.vo.SearchParam;
import com.zyj.paocai.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lulx
 * @date 2022-01-13 14:41
 **/
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/search/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        param.setQueryString(request.getQueryString());
        //根据查询参数检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }

    @ResponseBody
    @GetMapping("/search/test")
    public R<SearchResult> testListPage(SearchParam param, HttpServletRequest request) {
        param.setQueryString(request.getQueryString());
        //根据查询参数检索商品
        SearchResult result = mallSearchService.search(param);
        return R.ok(result);
    }
}

