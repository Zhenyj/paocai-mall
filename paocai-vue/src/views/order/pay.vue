<template>
  <div id="pay-page">
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <div class="content">
      <!-- 顶部logo -->
      <div class="header-wrapper">
        <div class="order-header">
          <h1 class="order-logo">
            <a
              href="/"
              target="_blank"
              title="泡菜商城"
            ><img src="../../assets/logo.png"></a>
          </h1>
        </div>
        <!-- 进度条 -->
        <div class="steps-wrapper">
          <el-steps
            :active="2"
            process-status="success"
            align-center
            finish-status="success"
          >
            <el-step title="我的购物车"></el-step>
            <el-step title="填写核对订单信息"></el-step>
            <el-step title="成功提交订单"></el-step>
          </el-steps>
        </div>
      </div>
      <!-- 订单头部标题 -->
      <div class="order-desc">
        <div class="item-headers">
          <div class="desc-wrapper">
            <h2 class="desc-title">确认订单信息</h2>
          </div>
          <div class="item-headers-wrap item-headers-column-6">
            <div class="item-headers-content item-headers-0">店铺宝贝</div>
            <div class="item-headers-content item-headers-1">商品属性</div>
            <div class="item-headers-content item-headers-2">单价</div>
            <div class="item-headers-content item-headers-3">数量</div>
            <div class="item-headers-content item-headers-4">优惠方式</div>
            <div class="item-headers-content item-headers-5">小计</div>
          </div>
        </div>
      </div>
      <!-- 订单详情 -->
      <div class="shop-list">
        <div
          class="shop-item"
          v-for="(shop,i1) in orderInfo.shops"
          :key="i1"
        >
          <div class="shop-info">
            <span class="shop-name">店铺:&nbsp;</span>
            <span>{{shop.brandName}}</span>
          </div>
          <div
            class="item-info"
            v-for="(item,i2) in shop.items"
            :key="i2"
          >
            <div class="item-row">
              <div class="shop-item-info">
                <div class="info-detail info-cell">
                  <span class="info-cell"><img
                      class="info-img"
                      :src="item.skuDefaultImg"
                      alt=""
                    ></span>
                  <div class="info-cell info-msg">
                    <span class="info-title">{{item.skuTitle}}</span>
                  </div>
                </div>
                <div class="info-sku info-cell">
                  <p
                    v-for="(attr,i3) in item.attrs"
                    :key="i3"
                  >
                    <span class="hd">{{attr.attrName+'：'}}</span>
                    <span class="bd">{{attr.attrValue}}</span>
                  </p>
                </div>
                <div class="info-price info-cell">{{item.price | showPrice}}
                </div>
              </div>
              <div class="order-quantity">
                <div class="quantity-inner">
                  <p>{{item.count}}</p>
                </div>
              </div>
              <div class="item-row-select">
                <p class="item-row-text">无优惠</p>
              </div>
              <div class="item-row-price">
                <div class="label item-row-price-item">
                  <span
                    style="font-weight: bold; font-style: normal; text-decoration: none; color: rgb(255, 0, 54); font-size: 14px; min-width: 100px;"
                  >{{(item.price*item.count) | showPrice}}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 订单总信息 -->
        <div class="realpay">
          <div class="pay-info box">
            <div class="box-wrapper">
              <div class="box-shadow">
                <div>
                  <span class="realpay-title">实付款：</span>
                  <span class="realpay-price-symbol">¥</span>
                  <span
                    class="realpay-price">{{total_amount | showPrice}}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="submit-order-container">
          <div class="wrapper">
            <a
              role="button"
              title="支付"
              class="go-btn"
              @click="payOrder"
            >支付</a>
          </div>
        </div>
      </div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
export default {
  name: 'pay',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      orderInfo: {},
      out_trade_no: '',
      total_amount: '',
    }
  },
  methods: {
    // 获取用户登录信息
    async getLoginInfo () {
      const loginInfo = await this.$getLoginInfo();
      if (loginInfo != null) {
        this.loginInfo = loginInfo;
      } else {
        this.$alert('请先登录再进行此操作', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            this.$router.push({ name: 'login' });
          }
        });
      }
    },
    handleNavTo (routerName) {
      this.$router.push({ name: routerName });
    },
    async payOrder () {
      const res = await this.$request({
        url: 'order/payOrder',
        method: 'POST',
        data: {
          out_trade_no: this.out_trade_no,
          total_amount: this.total_amount
        }
      });
      console.log(res);
      if (res.code !== 200) {
        this.$alert(res.msg + "，返回首页", '提示', {
          confirmButtonText: '确定',
          callback: action => {
            this.$router.push({ name: 'home' });
          }
        });
      }
      // 解决支付宝返回的是html代码的问题
      const div = document.createElement('div') // 创建div
      div.innerHTML = res.data // 将返回的form 放入div
      document.body.appendChild(div)
      // 出发支付宝返回的表单跳转支付宝的支付页面
      document.forms[0].submit()
    }
  },
  created () {
    document.title = '订单支付-泡菜商城';
    this.getLoginInfo();
    const payInfo = this.$router.currentRoute.params.payInfo;
    this.payInfo = payInfo;
    this.orderInfo = payInfo.orderInfo;
    this.out_trade_no = payInfo.out_trade_no;
    this.total_amount = payInfo.payAmount;
  },
  filters: {
    showPrice (price) {
      return parseFloat(price).toFixed(2);
    }
  }
}
</script>

<style lang="less" scoped>
body {
  line-height: 1.5;
  color: #333;
}
.content {
  width: 1000px;
  margin: 0 auto;
  /deep/ .el-icon-loading {
    font-size: 40px;
  }
  /deep/ .el-loading-text {
    font-size: 24px;
  }
}
.header-wrapper {
  background-color: #fff;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .order-header {
    position: relative;
    height: 64px;
    width: 112px;
    margin-top: 26px;
    .order-logo {
      a {
        display: block;
        height: 64px;
        width: 112px;
        img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
  .steps-wrapper {
    padding-top: 26px;
    width: 600px;
  }
}
.address-wrapper {
  width: 1000px;
  margin: 0 auto;
  .order-address {
    margin: 30px 0;
    a {
      color: #c97;
    }
    a:hover {
      color: #ff0036;
    }
  }
  .address-header {
    position: relative;
    margin-bottom: 15px;
    .header-title {
      line-height: 25px;
      color: #333;
      font-weight: 700;
      font-size: 14px;
    }
  }
  .address-list {
    width: 1016px;
    .address-item-wrapper {
      display: inline-block;
      vertical-align: top;
      position: relative;
      width: 237px;
      margin: 0 16px 14px 0;
      color: #666;
      cursor: pointer;
      font-family: verdana, -apple-system, BlinkMacSystemFont, Helvetica Neue,
        Helvetica, Tahoma, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
        sans-serif;

      .inner-infos {
        z-index: 2;
        position: relative;
        padding: 11px 15px;
        overflow: hidden;
        background-image: url("../../assets/images/address-bg.png");
        .addr-bd {
          overflow: hidden;
          height: 55px;
          span {
            margin-right: 3px;
            word-break: break-all;
            word-wrap: break-word;
          }
        }
      }
      .inner-infos:hover {
        background-image: url("../../assets/images/address-bg-selected.png");
      }
      .set-default {
        color: #666;
        background: #fff;
        display: none;
        position: absolute;
        top: 0;
        right: 0;
        padding: 0 2px;
        text-decoration: none;
        filter: alpha(opacity=70);
        opacity: 0.7;
        z-index: 3;
      }
      .set-default:hover {
        color: #c97;
      }
      .default-tip {
        background: #ccc;
        color: #fff;
        display: none;
        position: absolute;
        top: 0;
        right: 0;
        padding: 0 2px;
        text-decoration: none;
        filter: alpha(opacity=70);
        opacity: 0.7;
        z-index: 3;
      }
      a {
        display: none;
      }
    }
    .addr-selected:hover:not(.addr-default) .set-default {
      display: block;
    }
    .addr-selected {
      .inner-infos {
        background-image: url("../../assets/images/address-bg-selected.png");
        .addr-bd {
          height: 37px;
        }
      }
      .default-tip {
        display: block;
      }
      a {
        display: inline;
      }
    }

    .addr-hd {
      width: 100%;
      border-bottom: 1px solid #f2f2f2;
      padding: 0 0 5px;
      margin-bottom: 5px;
      line-height: 18px;
      font-weight: 700;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      .span {
        vertical-align: top;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }
  }

  .operations {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
  }
}
.order-desc {
  font-family: verdana, -apple-system, BlinkMacSystemFont, Helvetica Neue,
    Helvetica, Tahoma, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
    sans-serif;
  .item-headers {
    .desc-wrapper {
      position: relative;
      margin-bottom: 15px;
      .desc-title {
        line-height: 25px;
        color: #333;
        font-weight: 700;
        font-size: 14px;
      }
    }

    .item-headers-wrap {
      margin-top: 15px;
      color: #6c6c6c;
      display: flex;
      .item-headers-content {
        height: 24px;
        line-height: 24px;
        text-align: center;
        border-bottom: 3px solid #b2d1ff;
        font-size: 12px;
        margin-left: 1px;
        vertical-align: bottom;
      }
      .item-headers-0 {
        width: 260px;
        margin-left: 0;
      }
      .item-headers-1 {
        width: 185px;
      }

      .item-headers-2 {
        width: 120px;
      }

      .item-headers-3 {
        width: 120px;
      }

      .item-headers-4 {
        width: 180px;
      }

      .item-headers-5 {
        width: 130px;
      }
    }
  }
}
.shop-list {
  .shop-item {
    .order-link {
      color: #3c3c3c;
    }
    .order-link:hover {
      color: #ff0036 !important;
    }
    .shop-info {
      position: relative;
      padding-bottom: 5px;
      margin-top: 25px;
      border-bottom: 1px dotted #80b2ff;
      height: 27px;
    }
  }
  .item-info {
    background-color: rgb(251, 252, 255);
    border-bottom: 1px dotted rgb(221, 221, 221);
    .item-row {
      display: flex;
      .shop-item-info {
        vertical-align: top;
        display: flex;
        .info-cell {
          display: inline-block;
          box-sizing: content-box;
          margin-right: 1px;
        }
        .info-detail {
          width: 250px;
          padding: 10px 0 10px 10px;
          .info-img {
            display: block;
            width: 50px;
            height: 50px;
          }
          .info-msg {
            padding-left: 10px;
            vertical-align: top;
          }
          .info-title {
            color: #3c3c3c;
            width: 180px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            display: block;
          }
        }

        .info-sku {
          color: #6c6c6c;
          width: 185px;
          vertical-align: top;
          padding: 10px 0;
          p {
            word-break: break-all;
          }
        }

        .info-price {
          color: #3c3c3c;
          vertical-align: top;
          width: 120px;
          text-align: center;
          padding: 10px 0;
        }
      }
    }
  }
  .order-quantity {
    display: inline-block;
    vertical-align: top;
    text-align: center;
    width: 120px;
    padding: 10px 0;
    margin-right: 1px;
    .quantity-inner {
      position: relative;
      user-select: none;
      padding: 0 0 5px;
    }
  }
  .item-row-select {
    display: inline-block;
    width: 180px;
    vertical-align: top;
    height: 75px;
    text-align: center;
    .item-row-text {
      padding: 10px;
    }
  }
  .item-row-price {
    display: inline-block;
    width: 130px;
    text-align: right;
    padding-right: 5px;
    .item-row-price-item {
      display: inline-block;
      padding: 10px 0 5px;
    }
  }
  .realpay {
    .pay-info {
      text-align: right;
      margin-top: 15px;
    }
    .box-wrapper {
      display: inline-block;
      border: 1px solid #ff0036;
      .box-shadow {
        border: 3px solid #fff0e8;
        min-width: 230px;
        padding: 10px 5px 10px 20px;
      }
    }
    .realpay-title {
      font-weight: 700;
      color: #333;
    }
    .realpay-price-symbol {
      font-size: 26px;
      margin-right: 4px;
      color: #999;
    }
    .realpay-price {
      color: rgb(255, 0, 54);
      font: 700 26px tahoma;
    }
    .order-confirmAddr {
      line-height: 1.6;
      margin-top: 10px;
      .confirmAddr-title {
        font-weight: 700;
        color: #333;
      }
    }
    .order-confirm-eticket {
      line-height: 1.6;
      margin-top: 10px;
    }
  }
  .submit-order-container {
    text-align: right;
    margin-top: -1px;
    .wrapper {
      display: inline-block;
      padding: 0 0 5px 5px;
    }
    .go-back {
      color: #36c;
      margin-right: 50px;
      line-height: 34px;
    }
    .go-back:hover {
      color: #dd2727;
    }
    .icon-fanhui {
      margin-right: 5px;
    }
    .go-btn {
      display: inline-block;
      width: 182px;
      height: 39px;
      position: relative;
      vertical-align: middle;
      line-height: 39px;
      cursor: pointer;
      text-align: center;
      font-size: 14px;
      font-weight: 700;
      background: #dd2727;
      background-color: rgb(255, 0, 54);
      color: #fff;
    }
  }
}
/deep/ .el-select-dropdown__item.selected {
  color: #333;
  font-weight: normal;
  font-size: 12px;
  font-family: verdana, -apple-system, BlinkMacSystemFont, Helvetica Neue,
    Helvetica, Tahoma, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
    sans-serif;
}
/deep/ .el-select-dropdown__item {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
