<template>
  <div id="order-list-page">
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <member-header @navToByRouterName="handleNavTo($event)"></member-header>
    <div class="order-container">
      <div class="left-menu">
        <!-- 侧边菜单 -->
        <member-menu
          :bgc="'#f0f3ef'"
          :default-active="activeMenu"
        ></member-menu>
      </div>
      <div class="main">
        <el-tabs
          v-model="status"
          @tab-click="changeOrderStatus"
        >
          <el-tab-pane
            label="全部订单"
            name="-1"
          >
          </el-tab-pane>
          <el-tab-pane name="0">
            <span slot="label">待付款<span
                v-if="orderStatusInfo.waitPayNum && orderStatusInfo.waitPayNum > 0"
                class="tab-label-num"
              >{{orderStatusInfo.waitPayNum}}</span></span>
          </el-tab-pane>
          <el-tab-pane name="1">
            <span slot="label">待发货<span
                v-if="orderStatusInfo.waitDeliverNum && orderStatusInfo.waitDeliverNum > 0"
                class="tab-label-num"
              >{{orderStatusInfo.waitDeliverNum}}</span></span>
          </el-tab-pane>
          <el-tab-pane name="2">
            <span slot="label">待收货<span
                v-if="orderStatusInfo.waitReceiveNum && orderStatusInfo.waitReceiveNum > 0"
                class="tab-label-num"
              >{{orderStatusInfo.waitReceiveNum}}</span></span>
          </el-tab-pane>
          <el-tab-pane name="3">
            <span slot="label">待评价<span
                v-if="orderStatusInfo.waitCommentNum && orderStatusInfo.waitCommentNum > 0"
                class="tab-label-num"
              >{{orderStatusInfo.waitCommentNum}}</span></span>
          </el-tab-pane>
          <div
            v-show="loading"
            class="loading-mock"
            v-loading="loading"
            v-loading.body="loading"
            v-loading.lock="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
          >
          </div>
          <div v-show="!loading && totalCount > 0">
            <div class="order-info-table-header">
              <div class="header-item item-product">宝贝</div>
              <div class="header-item item-price">单价</div>
              <div class="header-item item-num">数量</div>
              <div class="header-item item-product-op">商品操作</div>
              <div class="header-item item-pay-amount">实付款</div>
              <div class="header-item item-order-status">交易状态</div>
              <div class="header-item item-order-op"></div>
            </div>
            <div
              class="top-pagination"
              v-show="totalCount>0"
            >
              <el-button
                type="primary"
                plain
                size="mini"
                :disabled="currPage===1"
              >上一页</el-button>
              <el-button
                type="primary"
                plain
                size="mini"
                :disabled="currPage>=totalPage"
              >下一页</el-button>
            </div>
            <div
              class="order-info-table-body"
              v-if="totalCount > 0"
            >
              <table
                class="order-info-item"
                :class="{'in-progress':v1.status >= 0 &&v1.status<=3}"
                v-for="(v1,i1) in orders"
                :key="i1"
              >
                <tbody>
                  <tr class="order-info-header">
                    <td class="info-header-item item-1">
                      <strong>{{v1.createTime.substring(0,10)}}</strong>
                      <span
                        style="margin-left:10px">{{"订单号："+v1.orderSn}}</span>

                    </td>
                    <td class="info-header-item item-2">
                      <a href="#">
                        {{v1.shopName}}
                      </a>
                    </td>
                    <td class="info-header-item item-3">
                      和我联系
                    </td>
                    <td class="info-header-item item-op">
                      <i
                        class="iconfont icon-shanchu"
                        title="删除订单"
                      ></i>
                    </td>
                  </tr>
                </tbody>
                <tbody class="order-info-body">
                  <tr
                    class="order-item"
                    v-for="(v2,i2) in v1.orderItems"
                    :key="i2"
                  >
                    <td
                      class="order-item-item item-product order-item-border no-br"
                    >
                      <div class="product-info">
                        <div class="info-left">
                          <a :href="'/product?skuId='+v2.skuId">
                            <img :src="v2.skuPic">
                          </a>
                        </div>
                        <div class="info-right">
                          <p class="info-item info-name">
                            <a
                              :href="'/product?skuId='+v2.skuId">{{v2.skuName}}</a>
                          </p>
                          <p class="info-item info-attr">
                            {{v2.skuAttrsVals}}
                          </p>
                        </div>
                      </div>
                    </td>
                    <td
                      class="order-item-item item-price order-item-border no-br"
                    >
                      <p
                        v-if="hasDiscount(v2)"
                        class="origin-price"
                      >￥{{v2.skuPrice | showPrice}}</p>
                      <p class="price">
                        ￥{{v2.realAmount | showPrice}}
                      </p>
                    </td>
                    <td
                      class="order-item-item item-num order-item-border no-br">1
                    </td>
                    <td
                      class="order-item-item item-product-op order-item-border"
                    >
                      <p><a href="#">申请售后</a></p>
                      <p><a href="#">退运保险</a></p>
                      <p><a href="#">投诉商家</a></p>
                    </td>
                    <td
                      class="order-item-item item-pay-amount order-item-border"
                      :class="{'no-bt':i2 > 0}"
                    >
                      <div v-if="i2 === 0">
                        <p><strong>￥{{v1.payAmount | showPrice}}</strong></p>
                        <p><span
                            style="color:#6c6c6c;font-family:verdana;">(含运费：￥{{v2.freightAmount | showPrice}})</span>
                        </p>
                      </div>
                    </td>
                    <td
                      class="order-item-item item-order-status order-item-border"
                      :class="{'no-bt':i2 > 0}"
                    >
                      <div v-if="i2 === 0">
                        <p>交易成功</p>
                        <p><a href="#">订单详情</a></p>
                      </div>
                    </td>
                    <td
                      class="order-item-item item-order-op order-item-border"
                      :class="{'no-bt':i2 > 0}"
                    >
                      <div v-if="i2 === 0">
                        <button class="op-btn">评价</button>
                        <p><a href="#">追加评论</a></p>
                        <p><a href="#">再次购买</a></p>
                        <p><a href="#">申请开票</a></p>
                      </div>
                    </td>
                  </tr>
                  <tr class="order-item">
                    <td
                      class="order-item-item item-product item-insurance order-item-border no-br"
                    >
                      <p><a href="#">保险服务</a></p>
                    </td>
                    <td
                      class="order-item-item item-price order-item-border no-br"
                    >
                      <p><span>￥0.00</span></p>
                    </td>
                    <td
                      class="order-item-item item-num order-item-border no-br">
                    </td>
                    <td
                      class="order-item-item item-product-op order-item-border"
                    ></td>
                    <td
                      class="order-item-item item-pay-amount order-item-border no-bt"
                    ></td>
                    <td
                      class="order-item-item item-order-status order-item-border no-bt"
                    ></td>
                    <td
                      class="order-item-item item-order-op order-item-border no-bt"
                    >
                    </td>
                  </tr>
                </tbody>
              </table>
              <div
                class="pagination-wrapper"
                v-show="totalCount > 0"
              >
                <el-pagination
                  background
                  layout="prev, pager, next"
                  :total="totalCount"
                  :page-count="totalPage"
                  :page-size="pageSize"
                  :current-page="currPage"
                  @size-change="getOrderList"
                  @current-change="getOrderList"
                >
                </el-pagination>
              </div>
            </div>
            <div v-else>
              <el-empty
                image="https://gtd.alicdn.com/tps/i3/T1TvXUXnNjXXXXXXXX-100-100.png"
                :image-size="150"
                description="您暂时还没有相关的订单，这里都空空的，快去挑选合适的商品吧！"
              ></el-empty>
            </div>
          </div>
        </el-tabs>
      </div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
import MemberMenu from '@/components/member/member-menu'
import MemberHeader from '@/components/member/member-header'
export default {
  name: 'orderList',
  components: { CommonHeader, CommonFooter, MemberMenu, MemberHeader },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      activeMenu: [0, 0],
      status: '-1',
      loading: false,
      orderStatusInfo: {
        waitReceiveNum: 1,
        waitDeliverNum: 1,
        waitPayNum: 1,
        waitCommentNum: 1
      },
      totalCount: 0,
      pageSize: 10,
      totalPage: 0,
      currPage: 1,
      key: '',
      orders: []
    }
  },
  methods: {
    // 获取用户登录信息
    async getLoginInfo () {
      const loginInfo = await this.$getLoginInfo();
      if (loginInfo != null) {
        this.loginInfo = loginInfo;
      }
    },
    // 获取订单列表
    async getOrderList () {
      let currPage = this.currPage;
      if (currPage < 1) {
        currPage = 1;
      }
      let pageSize = this.pageSize;
      if (pageSize < 1) {
        pageSize = 10;
      }
      let url = 'order/order/list?page=' + currPage + '&limit=' + pageSize;
      if (this.status && this.status >= 0) {
        url += '&status=' + this.status;
      }
      if (this.key.trim() !== '') {
        url += '&key=' + this.key;
      }
      try {
        this.loading = true;
        const res = await this.$request({
          url: url,
          method: 'GET'
        });
        this.$handleResponseMessage(res, '', '未知错误，获取订单列表失败');
        if (res.code !== 200) {
          return;
        }
        const data = res.data;
        this.totalCount = data.totalCount;
        this.pageSize = data.pageSize;
        this.totalPage = data.totalPage;
        this.currPage = data.currPage;
        let orders = data.list;
        orders.forEach((v1, i1) => {
          v1.orderItems.forEach((v2, i2) => {
            let skuAttrJsonStr = v2.skuAttrsVals;
            let skuAttrObj = JSON.parse(skuAttrJsonStr);
            let skuAttrStr = '';
            skuAttrObj.forEach((v3, i2) => {
              skuAttrStr += v3.attrName + "：" + v3.attrValue + " "
            });
            v2.skuAttrsVals = skuAttrStr;
          });
        });
        this.orders = orders;
        this.loading = false;
      } finally {
        this.loading = false;
      }
    },
    handleNavTo (routerName) {
      this.$router.push({ name: routerName });
    },
    // 是否有会员价格优惠
    hasDiscount (item) {
      return parseFloat(item.skuPrice) > parseFloat(item.realAmount);
    },
    // 变更tab标签页
    changeOrderStatus (tab, event) {
      this.getOrderList();
      return true;
    },
    // 获取用户订单数量信息
    async getOrderStatusInfo () {
      const res = await this.$request({
        url: 'order/order/order_status_num',
        method: 'GET'
      });
      if (res.code === 200) {
        this.orderStatusInfo = res.data;
      }
    }
  },
  created () {
    this.getLoginInfo();
    this.getOrderStatusInfo();
    let param = this.$route.query;
    if (param.status) {
      if (param.status < 0) {
        this.status = '-1';
      } else {
        this.status = param.status;
      }
    }
    this.getOrderList();
  },
  filters: {
    showPrice (price) {
      if (price == null) {
        price = 0;
      }
      return parseFloat(price).toFixed(2);
    }
  }
}
</script>

<style lang="less" scoped>
#order-list-page {
  background-color: #f0f3ef;
  font: 12px/1.5 tahoma, arial, Hiragino Sans GB, \\5b8b\4f53, sans-serif;
}
.order-container {
  width: 1200px;
  margin: 0 auto;
  display: flex;
}
.left-menu {
  width: 170px;
  min-height: 400px;
}
.main {
  flex: 1;
  margin-top: 15px;
  background-color: #fff;
  padding: 0 20px;
  min-height: 500px;
  /deep/ .el-icon-loading {
    font-size: 36px;
  }
  /deep/ .el-loading-text {
    font-size: 20px;
  }
  /deep/ .el-tabs__content {
    min-height: 400px;
  }
  /deep/ .el-loading-spinner {
    top: 150px;
  }
  /deep/ .el-tabs__item.is-active {
    color: #ff0036;
  }
  /deep/ .el-tabs__active-bar {
    background-color: #ff0036;
  }
  /deep/ .el-tabs__item:hover {
    color: #ff0036;
  }
  .tab-label-num {
    color: #ff0036;
    font-size: 16px;
    margin-left: 5px;
  }
  .order-info-table-header {
    margin: 10px 0;
    display: flex;
    background-color: #f5f5f5;
    border: 1px solid #e8e8e8;
    text-align: center;
  }
  .header-item {
    padding: 5px 0;
    line-height: 30px;
  }
  .item-product {
    width: 38%;
  }
  .item-price {
    width: 12%;
  }
  .item-num {
    width: 3%;
  }
  .item-product-op {
    width: 12%;
  }
  .item-pay-amount {
    width: 12%;
  }
  .item-order-status {
    width: 11%;
  }
  .item-order-op {
    width: 12%;
  }
  .item-op {
    flex: 1;
    text-align: right;
    padding-right: 20px !important;
  }
  .order-info-table-body {
    width: 100%;
    .order-info-item {
      border: 1px solid #ececec;
      margin: 10px 0;
      border-collapse: collapse;
      border-spacing: 0;
      color: #3c3c3c;
      table-layout: fixed;
      width: 100%;
      td {
        vertical-align: top;
      }
    }
    .order-info-item:hover {
      border-color: #bfbfbf;
    }

    .order-info-header {
      display: flex;
      background: #f1f1f1;
      .info-header-item {
        padding: 10px 0;
        line-height: 20px;
      }
      .item-1 {
        width: 38%;
        padding-left: 20px;
      }
      .item-2 {
        display: block;
        width: 15%;
        padding-left: 20px;
        padding-right: 20px;
        text-align: center;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        color: #333;
      }
      .item-3 {
        width: 12%;
        text-align: center;
      }
      a:hover {
        color: #ff4200;
      }

      .icon-shanchu {
        font-size: 20px;
        cursor: pointer;
      }
    }
    .order-info-body {
      width: 100%;
    }
    .order-item {
      display: flex;

      .item-insurance {
        text-align: left;
        padding-left: 20px;
      }
    }
    .order-item-item {
      padding: 10px 0;
      text-align: center;
      p {
        margin-top: 8px;
        margin-bottom: 8px;
        line-height: 1;
      }
    }
    .order-item-border {
      border-width: 1px 1px 0 0;
      border-style: solid;
      border-color: #ececec;
    }
    .order-item-border:last-child {
      border-right: none;
    }
    .product-info {
      margin-left: 20px;
      text-align: left;
      display: flex;
    }
    .info-left {
      width: 80px;
      height: 80px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .info-right {
      flex: 1;
      .info-item {
        margin: 8px 0;
      }
      .info-attr {
        color: #9e9e9e;
      }
    }
    .origin-price {
      text-decoration: line-through;
      color: #9c9c9c;
    }
    .item-order-op {
      .op-btn {
        font: 12px/1.5 tahoma, arial, Hiragino Sans GB, \\5b8b\4f53, sans-serif;
        background-color: #fff;
        padding: 0 12px;
        cursor: pointer;
        border: 1px solid #dcdcdc;
        font-weight: bold;
        color: #3c3c3c;
        line-height: 28px;
        border-radius: 5px;
      }
      .op-btn:hover {
        border-color: #ff1d00;
        color: #ff1d00;
      }
    }
    .in-progress {
      border-color: #daf3ff;
      .order-info-header {
        background-color: #eaf8ff;
      }
      .order-item-item {
        border-color: #daf3ff;
      }
    }
    .in-progress:hover {
      border-color: #aed8ff;
    }
    .no-br {
      border-right: none !important;
    }
    .no-bt {
      border-top: none !important;
    }
  }
  .top-pagination {
    text-align: right;
  }
  .pagination-wrapper {
    margin: 20px 0;
  }
  a {
    color: #3c3c3c;
  }
  a:hover {
    color: #ff4200;
  }
}
</style>
