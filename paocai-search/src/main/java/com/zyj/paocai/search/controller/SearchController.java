package com.zyj.paocai.search.controller;

import com.zyj.paocai.common.exception.BizCodeEnum;
import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.search.service.MallSearchService;
import com.zyj.paocai.search.vo.SearchParam;
import com.zyj.paocai.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lulx
 * @date 2022-01-13 14:41
 **/
@RestController
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @PostMapping("/search/item")
    public R<SearchResult> testListPage(@RequestBody SearchParam param) {
        //根据查询参数检索商品
        SearchResult result = null;
        try {
            result = mallSearchService.search(param);
        } catch (Exception e) {
            return R.error(BizCodeEnum.SEARCH_SERVICE_EXCEPTION.getCode(),BizCodeEnum.SEARCH_SERVICE_EXCEPTION.getMsg());
        }
        return R.ok(result);
    }
}

