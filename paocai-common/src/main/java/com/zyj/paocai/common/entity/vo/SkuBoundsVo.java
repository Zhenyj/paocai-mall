package com.zyj.paocai.common.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品sku积分设置
 * 
 * @author lulx
 * @email 
 * @date 2022-04-15 21:22:50
 */
@Data
public class SkuBoundsVo {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 
	 */
	private Long skuId;
	/**
	 * 成长积分
	 */
	private BigDecimal growBounds;
	/**
	 * 购物积分
	 */
	private BigDecimal buyBounds;
	/**
	 * 优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]
	 */
	private Integer work;

}
