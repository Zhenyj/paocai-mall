package com.zyj.paocai.common.exception;

/**
 * @author zyj
 * @create 2022-04-01 1:15
 * <p>
 * 错误码和错误信息定义类
 * 1。错误码定义规则为5为数字
 * 2，前两位表示业务场景，最后三位表示错误码。例如:100001。10:通用 001:系统未知异常
 * 3．维护错误码后需要维护错误描述,将他们定义为枚举形式
 * 错误码列表:
 * 10:通用
 * 001:参数格式校验
 * 11:商品
 * 12:订单
 * 13:购物车
 * 14:物流
 */

public enum BizCodeEnum {
    // 通用
    INTERNAL_ERROR(500, "服务器内部错误"),
    UNKNOWN_EXCEPTION(1000, "系统未知异常"),
    VALID_EXCEPTION(1001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(1002, "验证码获取频率太高，请稍后再试"),
    TOO_MANY_REQUEST(1003, "请求流量过大,请稍后再试"),
    DEGRADE(1004, "服务降级"),
    PARAM_FLOW(1005, "参数限流"),
    SYSTEM_BLOCK(1006, "系统负载异常"),
    AUTHORITY(1007, "授权异常"),
    BODY_NOT_MATCH(1008, "请求的数据格式不符!"),
    // 订单服务
    ORDER_SERVICE_EXCEPTION(9000, "订单服务异常"),
    ORDER_NOT_EXIST_SHOP_EXCEPTION(9001, "订单信息缺失相关店铺数据"),
    ORDER_NOT_EXIST_PRODUCT_EXCEPTION(9002, "订单信息缺失相关商品数据"),
    ORDER_TIME_OUT_EXCEPTION(9003, "订单已过期"),
    ORDER_PRICE_ERROR_EXCEPTION(9004, "订单商品价格发生变化，请确认后再次提交"),
    ORDER_STOCK_LOCKED_EXCEPTION(9005, "库存锁定失败，库存商品不足"),
    ORDER_CLOSE_EXCEPTION(9006, "订单关闭失败"),
    // 商品服务
    PRODUCT_SERVICE_EXCEPTION(10000, "商品服务异常"),
    PRODUCT_UP_EXCEPTION(10001, "商品上架异常"),
    PRODUCT_CATEGORY_EXCEPTION(10002, "商品分类异常"),
    PRODUCT_WARE_EXCEPTION(10003, "商品库存异常"),
    PRODUCT_NO_EXIST_EXCEPTION(10004, "商品不存在或已下架"),
    PRODUCT_SPU_NO_EXIST_EXCEPTION(10005, "spu信息异常"),
    PRODUCT_BRAND_EXCEPTION(10006, "品牌信息异常"),
    PRODUCT_ATTR_EXCEPTION(10007, "属性数据异常"),
    PRODUCT_ATTR_GROUP_RELATION_EXCEPTION(10007, "属性&属性分组关联"),
    PRODUCT_ATTR_GROUP_EXCEPTION(10008, "属性分组关联异常"),
    PRODUCT_CATEGORY_NO_EXIST_EXCEPTION(10009, "分类不存在"),
    PRODUCT_OF_CART_EXCEPTION(10010,"获取购物车商品信息详情失败"),
    // 购物车服务
    CART_SERVICE_EXCEPTION(16000, "购物车服务异常"),
    CART_PRODUCT_INFO_EXCEPTION(16001, "购物车商品信息缺失、不完整"),
    CART_RELEASE_ORDER_ITEM_EXCEPTION(16002, "删除购物车已提交订单的商品失败"),
    // 会员服务、认证服务
    MEMBER_SERVICE_EXCEPTION(15000, "会员服务异常"),
    USER_EXIST_EXCEPTION(15001, "用户名已存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已存在"),
    EMAIL_EXIST_EXCEPTION(15003, "邮箱已存在"),
    LOGIN_ACCT_PASSWORD_INVALID_EXCEPTION(15003, "账号密码错误"),
    TOKEN_EXCEPTION(15004, "token无效"),
    PLEASE_LOGIN(15005, "对不起，请先登录后再进行此操作！"),
    ADDRESS_ADD_EXCEPTION(15006, "收货地址添加异常"),
    ADDRESS_DELETE_EXCEPTION(15007, "收货地址删除异常"),
    ADDRESS_UPDATE_EXCEPTION(15008, "收货地址修改异常"),
    PASSWORD_DIFFERENT_EXCEPTION(15009, "两次密码不一致"),
    PASSWORD_ERROR_EXCEPTION(15009, "密码错误"),
    // 库存服务
    WARE_SERVICE_EXCEPTION(8000, "库存服务异常"),
    NO_STOCK_EXCEPTION(8001, "商品库存不足"),
    CREATE_ORDER_TASK_EXCEPTION(8002, "创建库存工作单异常"),
    CREATE_ORDER_TASK_DETAIL_EXCEPTION(8003, "创建库存工作详单异常"),
    GET_ORDER_TASK_EXCEPTION(8004, "获取库存工作单异常"),
    GET_ORDER_TASK_DETAIL_EXCEPTION(8005, "获取库存工作详单单异常"),
    UNLOCK_STOCK_EXCEPTION(8006, "库存解锁失败"),
    // 优惠服务
    COUPON_SERVICE_EXCEPTION(14000, "优惠服务异常"),
    COUPON_OF_PRODUCT_EXCEPTION(14001, "商品优惠信息异常"),
    // 搜索服务
    SEARCH_SERVICE_EXCEPTION(13000, "搜索服务异常");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
