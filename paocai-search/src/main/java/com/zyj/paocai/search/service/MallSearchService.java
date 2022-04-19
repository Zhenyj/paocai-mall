package com.zyj.paocai.search.service;

import com.zyj.paocai.search.vo.SearchParam;
import com.zyj.paocai.search.vo.SearchResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author lulx
 * @date 2022-01-13 17:27
 **/
public interface MallSearchService {
    /**
     * 检索商品
     * @param param 检索参数
     * @return 检索结果
     */
    SearchResult search(SearchParam param) throws IOException, ExecutionException, InterruptedException, Exception;
}
