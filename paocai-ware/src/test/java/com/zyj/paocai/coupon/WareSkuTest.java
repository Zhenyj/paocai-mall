package com.zyj.paocai.coupon;

import com.zyj.paocai.common.entity.vo.SkuIdCountVo;
import com.zyj.paocai.ware.PaocaiWareApplication;
import com.zyj.paocai.ware.dao.WareSkuDao;
import com.zyj.paocai.ware.entity.vo.SkuWareHasStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lulx
 * @date 2022-05-12 16:13
 **/
@SpringBootTest(classes = {PaocaiWareApplication.class})
@DisplayName("商品库存测试")
public class WareSkuTest {

    @Autowired
    WareSkuDao wareSkuDao;

    /**
     * 测试批量获取sku哪些仓库有库存
     */
    @Test
    void testListWareIdHasSkuStockBatch(){
        List<SkuIdCountVo> skuIdCountVos = new ArrayList<>();
        SkuIdCountVo skuIdCountVo = new SkuIdCountVo(1701L,"", 1);
        skuIdCountVos.add(skuIdCountVo);
        List<SkuWareHasStock> list = wareSkuDao.listWareIdHasSkuStockBatch(skuIdCountVos);
        System.out.println(list);
    }
}
