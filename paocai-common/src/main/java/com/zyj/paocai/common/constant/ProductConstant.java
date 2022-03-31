package com.zyj.paocai.common.constant;

/**
 * @author lulx
 * @date 2022-04-01 0:17
 **/
public class ProductConstant {

    public static final int BASE_ATTR_TYPE = 1;
    public static final int SALE_ATTR_TYPE = 0;

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"), ATTR_TYPE_SALE(0, "销售属性");
        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
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

    public enum StatusEnum {
        SPU_NEW(1, "新建"),
        SPU_UP(0, "上架"),
        SPU_DOWN(0, "下架");
        private int code;
        private String msg;

        StatusEnum(int code, String msg) {
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
}
