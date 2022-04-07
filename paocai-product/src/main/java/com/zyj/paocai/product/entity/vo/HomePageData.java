package com.zyj.paocai.product.entity.vo;

import com.zyj.paocai.common.entity.vo.OrderStatusNumsVo;
import com.zyj.paocai.product.entity.CategoryEntity;
import com.zyj.paocai.product.entity.PromoteEntity;
import lombok.Data;

import java.util.List;

/**
 * @author lulx
 * @date 2022-04-07 11:21
 **/
@Data
public class HomePageData {

    /** 三级分类 */
    private List<CategoryEntity> category;

    /** 用户订单状态信息 */
    private OrderStatusNumsVo orderStatusInfo;

    /** 搜索热词 */
    private List<String> hotWords;

    /** 首页顶部大图推广资源 */
    private List<PromoteEntity> promoteCarousel;

    /** 首页顶部小图推广资源 */
    private List<SmallPromote> smallPromoteCarousel;

    /** 公告 */
//    private List<> notices;

    /** 猜你喜欢、热销商品，第一次加载只加载第一页内容20个 */
    private HotSale hotSale;
}