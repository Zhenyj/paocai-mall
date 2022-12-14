package com.zyj.paocai.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品满减信息
 * 
 * @author lulx
 * @email 
 * @date 2022-03-15 21:22:50
 */
@Data
@TableName("sms_sku_full_reduction")
public class SkuFullReductionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * spu_id
	 */
	private Long skuId;
	/**
	 * 满多少
	 */
	private BigDecimal fullPrice;
	/**
	 * 减多少
	 */
	private BigDecimal reducePrice;
	/**
	 * 是否参与其他优惠
	 */
	private Integer addOther;

	/**
	 * 是否启用【0-关闭，1-启用】
	 */
	private Integer status;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date expireTime;

}
