package com.zyj.paocai.common.constant;

/**
 * @author lulx
 * @date 2022-04-07 17:53
 **/
public class OrderConstant {

    static class OrderStatus {
        // 订单状态【0->待付款；1->待发货；2->待收货；3->待评论；4->已完成；5->已关闭；6->无效订单]
        public static final int WAIT_PAY = 0;
        public static final int WAIT_DELIVER = 1;
        public static final int WAIT_RECEIVE = 2;
        public static final int WAIT_COMMENT = 3;
        public static final int FINISHED = 4;
        public static final int CLOSED = 5;
        public static final int INVALID = 6;
    }
}
