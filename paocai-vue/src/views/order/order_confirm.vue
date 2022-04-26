<template>
  <div id="order-confirm-page">
    <common-header :loginInfo="loginInfo"></common-header>
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
            :active="1"
            align-center
            finish-status="success"
          >
            <el-step title="我的购物车"></el-step>
            <el-step title="填写核对订单信息"></el-step>
            <el-step title="成功提交订单"></el-step>
          </el-steps>
        </div>
      </div>
      <!-- 收获地址 -->
      <div class="address-wrapper">
        <div class="order-address">
          <div class="address-header">
            <h2 class="header-title">选择收货地址</h2>
          </div>
          <div class="address-list">
            <div class="address-item-wrapper addr-selected addr-default">
              <div class="inner-infos">
                <div class="addr-hd">
                  <span>福建</span>
                  <span>厦门</span>
                  <span>（甄英俊收）</span>
                </div>
                <div class="addr-bd">
                  <span>集美</span>
                  <span>后溪</span>
                  <span>港头三里17号</span>
                  <span>12345678910</span>
                </div>
                <a
                  title="修改地址"
                  class="modify-operation"
                >修改</a>
              </div>
              <div
                class="set-default"
                title="设置当前地址为默认"
              >设为默认</div>
              <div class="default-tip">默认地址</div>
            </div>
            <div
              class="address-item-wrapper"
              v-for="i in 3"
            >
              <div class="inner-infos">
                <div class="addr-hd">
                  <span>福建</span>
                  <span>厦门</span>
                  <span>（甄英俊收）</span>
                </div>
                <div class="addr-bd">
                  <span>集美</span>
                  <span>后溪</span>
                  <span>港头三里17号</span>
                  <span>12345678910</span>
                </div>
                <a
                  title="修改地址"
                  class="modify-operation"
                  v-if="true"
                >修改</a>
              </div>
              <div
                class="set-default"
                title="设置当前地址为默认"
              >设为默认</div>
              <div class="default-tip">默认地址</div>
            </div>
          </div>
          <div class="operations">
            <a href="">显示全部地址</a>
            <a href="">管理收货地址</a>
          </div>
        </div>
      </div>
      <!-- 订单头部标题 -->
      <div class="order-desc">
        <div class="item-headers">
          <div class="desc-wrapper ">
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
          v-for="i in 2"
        >
          <div class="shop-info">
            <span class="shop-name">店铺:&nbsp;</span>
            <a
              target="_blank"
              class="order-link"
            >旗舰店</a>
          </div>
          <div
            class="item-info"
            v-for="j in 2"
          >
            <div class="item-row">
              <div class="shop-item-info">
                <div class="info-detail info-cell">
                  <a
                    href=""
                    target="_blank"
                    class="info-cell"
                  ><img
                      class="info-img"
                      src="https://img.alicdn.com/imgextra/O1CN01agWcnU1Eb2sdiwP7K_!!6000000000369-0-remus.jpg_100x100q90.jpg"
                      alt=""
                    ></a>
                  <div class="info-cell info-msg">
                    <a
                      href=""
                      target="_blank"
                      class="info-title order-link"
                    >【8+128G低至1899】realme真我GTNeo2高通骁龙87065W闪充E4旗舰屏学生智能手机拍照游戏官方正品gtneo2</a>
                  </div>
                </div>
                <div class="info-sku info-cell">
                  <p><span class="hd">机身颜色：</span><span class="bd">龙珠定制版</span>
                  </p>
                  <p><span class="hd">套餐类型：</span><span class="bd">官方标配</span>
                  </p>
                  <p><span class="hd">存储容量：</span><span
                      class="bd">12GB+256GB</span></p>
                  <p><span class="hd">版本类型：</span><span class="bd">中国大陆</span>
                  </p>
                </div>
                <div class="info-price info-cell">2799.00</div>
              </div>
              <div class="order-quantity">
                <div class="quantity-inner">
                  <p>1</p>
                </div>
              </div>
              <div class="item-row__select">
                <p class="item-row__text">无优惠</p>
              </div>
              <div class="item-row__price">
                <div class="label item-row__price-item"><span
                    style="font-weight: bold; font-style: normal; text-decoration: none; color: rgb(255, 0, 54); font-size: 14px; min-width: 100px;"
                  >2999.00</span></div>
              </div>
            </div>
          </div>
          <div class="order-ext-wrapper">
            <div class="order-ext">
              <div class="ext-border">
                <div class="ext-top-grid">
                  <div class="ext-border ext-grid ext-grid-left">
                    <div class="ext-item">
                      <div class="invoice">
                        <el-checkbox v-model="checked">开具发票</el-checkbox>
                        <div class="invoice-operate">
                          <div class="invoice-op-item invoice-type">
                            <label class="title-label">发票类型：</label>
                            <span>增值税电子普通发票</span>
                          </div>
                          <div class="invoice-op-item invoice-info">
                            <label class="title-label">发票信息：</label>
                            <el-select
                              v-model="value"
                              placeholder="请选择"
                              size="mini"
                            >
                              <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                              >
                              </el-option>
                            </el-select>
                          </div>
                          <div class="invoice-op-item invoice-info">
                            <label class="title-label">发票抬头类型：</label>
                            <el-radio-group v-model="radio">
                              <el-radio :label="3">个人</el-radio>
                              <el-radio :label="6">企业</el-radio>
                            </el-radio-group>
                          </div>
                          <div class="invoice-op-item invoice-header">
                            <label class="title-label">发票抬头：</label>
                            <span>
                              <span>个人</span>
                              <a class="edit-header">修改</a>
                              <span class="header-box">
                                <el-tooltip
                                  effect="dark"
                                  manual
                                  content="请填写发票抬头"
                                  placement="bottom-start"
                                  v-model="visible"
                                  popper-class="tps"
                                >
                                  <el-input
                                    size="mini"
                                    v-model="input"
                                    placeholder="根据最新增值税管理办法，如需企业抬头发票，请填写有效税号信息"
                                  ></el-input>
                                </el-tooltip>
                              </span>
                            </span>
                          </div>
                          <!-- 纳税人编号，发票抬头选择企业时才显示 -->
                          <div
                            class="invoice-op-item invoice-tax-no"
                            v-if="true"
                          >
                            <label class="title-label">纳税人识别号：</label>
                            <span class="hide">
                              <span>123123</span>
                              <a class="edit-header">修改</a>
                            </span>
                            <span class="header-box">
                              <el-tooltip
                                effect="dark"
                                manual
                                content="根据最新增值税管理办法，如需企业抬头发票，请填写有效税号信息"
                                placement="bottom-start"
                                v-model="visible"
                                popper-class="tps"
                              >
                                <el-input
                                  size="mini"
                                  v-model="input"
                                  placeholder="根据最新增值税管理办法，如需企业抬头发票，请填写有效税号信息"
                                ></el-input>
                              </el-tooltip>
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="ext-item">
                      <div class="textarea">
                        <label class="textarea-title">
                          <div>给卖家留言：</div>
                        </label>
                        <div class="textarea-wrapper">
                          <el-input
                            type="textarea"
                            placeholder="选填，请先和商家协商一致，付款后商家可见"
                            v-model="textarea"
                            maxlength="200"
                            show-word-limit
                          >
                          </el-input>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="ext-border ext-grid ext-grid-right">
                    <!-- 店铺优惠,有优惠才显示 -->
                    <div
                      class="ext-item"
                      v-if="true"
                    >
                      <div class="select-container">
                        <div class="select-wrap">
                          <label class="select-label">店铺优惠<span>：</span></label>
                          <div class="select-inline">
                            <el-select
                              v-model="value"
                              placeholder="请选择"
                              size="mini"
                            >
                              <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                              >
                              </el-option>
                            </el-select>
                          </div>
                          <div
                            class="label-select-wrap"
                            style="float: right;"
                          >
                            <div class="label label-shop-price">
                              <span>-1000.00</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- 配送方式 -->
                    <div class="ext-item">
                      <div class="delivery-method">
                        <div class="delivery-select"><span
                            class="delivery-title"
                          >运送方式：</span>
                          <div class="delivery-box">
                            <span class="single-method"><label
                                class="title-text"
                              >普通配送</label><label
                                class="delivery-type"></label><span>快递&nbsp;免邮</span></span>
                            <div class="appoint-container"></div>
                          </div>
                        </div><span
                          class="select-price"
                          style="color: rgb(255, 0, 54);"
                        >0.00</span>
                      </div>
                    </div>
                    <!-- 运费险 -->
                    <div class="ext-item">
                      <div class="order-postageInsurance">
                        <div class="user-title">运费险：</div>
                        <div class="user-operate">
                          <label
                            class="next-checkbox-wrapper ins-checkbox checked disabled "
                          >
                            <span class="next-checkbox">
                              <span class="next-checkbox-inner">
                                <i class="el-icon-check"></i>
                              </span>
                              <input
                                disabled
                                type="checkbox"
                                class="next-checkbox-input"
                                checked
                              >
                            </span>
                            <span class="next-checkbox-label">
                              <span class="trigger">运费险</span>
                              <div class="user-content">卖家赠送，退换货可赔</div>
                            </span>
                          </label>
                        </div>
                        <div
                          class="user-price"
                          style="color: rgb(255, 0, 54);"
                        >0.00</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="ext-border">
                <div class="shop-sum">
                  <div class="label">
                    <span class="label-header">店铺合计(含运费)</span>
                    <span class="shop-sum-price">￥2999.00</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 订单总信息 -->
          <div class="realpay">
            <div class="pay-info box">
              <div class="box__wrapper">
                <div class="box__shadow">
                  <div>
                    <span class="realpay-title">实付款：</span>
                    <span class="realpay-price-symbol">¥</span>
                    <span class="realpay-price">6367.81</span>
                  </div>
                  <div class="order-confirmAddr">
                    <div class="confirmAddr-addr">
                      <span class="confirmAddr-title">寄送至：</span>
                      <span class="confirmAddr-addr-bd"> 福建 厦门 集美 后溪
                        港头三里17号</span>
                    </div>
                    <div class="confirmAddr-addr-user">
                      <span class="confirmAddr-title">收货人：</span>
                      <span class="confirmAddr-addr-bd">卢
                        12345678910</span>
                    </div>
                  </div>
                  <div class="order-confirm-eticket"></div>
                </div>
              </div>
            </div>
          </div>
          <!--  -->
          <div class="submit-order-container">
            <div class="wrapper">
              <a
                class="go-back"
                target="_self"
                role="button"
                title="返回购物车"
                href="/cart"
              ><i class="iconfont icon-fanhui"></i>返回购物车</a>
              <a
                role="button"
                title="提交订单"
                class="go-btn"
              >提交订单</a>
            </div>
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
  name: 'orderConfirm',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      visible: true
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
  },
  created () {
    document.title = "确认订单-泡菜商城";
    this.getLoginInfo();
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
  .item-row__select {
    display: inline-block;
    width: 180px;
    vertical-align: top;
    height: 75px;
    text-align: center;
    .item-row__text {
      padding: 10px;
    }
  }
  .item-row__price {
    display: inline-block;
    width: 130px;
    text-align: right;
    padding-right: 5px;
    .item-row__price-item {
      display: inline-block;
      padding: 10px 0 5px;
    }
  }
  .order-ext-wrapper {
    .order-ext {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: stretch;
      margin: 0px;
      padding: 0px;
      background-color: rgb(242, 247, 255);
      border-bottom: 1px dotted rgb(128, 178, 255);
    }
    .ext-border {
      padding: 0px;
      border-right: 1px solid rgb(255, 255, 255);
      border-top: 1px solid rgb(255, 255, 255);
    }
    .ext-top-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      margin: 0px;
      .ext-grid {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: stretch;
        margin: -5px 0px;
        padding: 0px;
      }
      .ext-grid-left {
        font-family: verdana, -apple-system, BlinkMacSystemFont, Helvetica Neue,
          Helvetica, Tahoma, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
          sans-serif;
        font-size: 12px;
        color: #333;
        /deep/ .el-input__inner:focus {
          border: 1px solid #cd9b7a;
          outline: 0;
        }
      }
    }

    .ext-item {
      padding: 5px;
      border-top: 1px solid rgb(255, 255, 255);
    }
    .delivery-method {
      position: relative;
      padding-top: 11px;
      padding-bottom: 10px;
      .delivery-title {
        display: inline-block;
        padding-right: 15px;
        min-width: 95px;
        text-align: right;
        font-size: 12px;
      }
      .delivery-select {
        display: inline;
      }
      .delivery-box {
        display: inline;
      }
      .delivery-type {
        margin-right: 4px;
      }
      .select-price {
        position: absolute;
        top: 10px;
        right: 0;
        font-weight: 700;
        font-size: 12px;
      }
    }
    .order-postageInsurance {
      padding: 10px 0;
      line-height: 19px;
      text-align: left;
      .user-title {
        width: 95px;
        padding-right: 15px;
        text-align: right;
        display: inline-block;
        vertical-align: top;
      }
      .user-operate {
        display: inline-block;
        vertical-align: top;
      }
      .next-checkbox-wrapper {
        .next-checkbox {
          display: inline-block;
          position: relative;
          line-height: 1;
          vertical-align: middle;
        }
        .next-checkbox-inner {
          display: block;
          width: 16px;
          height: 16px;
          background: #fff;
          border-radius: 3px;
          border: 1px solid #c4c6cf;
          transition: all 0.1s linear;
          text-align: left;
          box-shadow: none;
          text-align: center;
          line-height: 13px;
        }
        .next-checkbox-input {
          opacity: 0;
          position: absolute;
          top: 0;
          left: 0;
          width: 16px;
          height: 16px;
          margin: 0;
          cursor: pointer;
          padding: 0;
        }
      }
      .next-checkbox-inner {
        display: block;
        width: 16px;
        height: 16px;
        background: #fff;
        border-radius: 3px;
        border: 1px solid #c4c6cf;
        transition: all 0.1s linear;
        text-align: left;
        box-shadow: none;
        .el-icon-check {
          color: #ccc;
          font-weight: 700;
          opacity: 1;
        }
      }
      .next-checkbox-wrapper.disabled {
        .next-checkbox-inner {
          border-color: #e6e7eb;
          background: #f7f8fa;
        }

        .next-checkbox-input {
          cursor: not-allowed;
        }
      }

      .next-checkbox-label {
        font-size: 12px;
        color: #333;
        vertical-align: middle;
        margin: 0 0 0 4px;
        line-height: 1;
      }
      .trigger {
        position: relative;
        background: #36ab67;
        color: #fff;
        cursor: pointer;
        padding: 1px;
        margin-right: 5px;
      }
      .user-content {
        max-width: 245px;
        display: inline-block;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        vertical-align: middle;
      }
      .user-price {
        padding-top: 2px;
        float: right;
        text-align: right;
        color: #3c3c3c;
        font-weight: 700;
        font-size: 12px;
      }
    }
    .select-container {
      width: 100%;
      display: inline-block;
      .select-wrap {
        padding: 10px 0 5px;
        width: 100%;
        .select-label {
          display: inline-block;
          min-width: 95px;
          padding-right: 15px;
          text-align: right;
          vertical-align: top;
          line-height: 26px;
        }
        .label-select-wrap {
          display: inline-block;
          vertical-align: top;
          line-height: 26px;
          margin-top: -3px;
        }
        .select-inline {
          display: inline-block;
        }

        .label-shop-price {
          padding: 5px 0 5px 5px;
          span {
            font-weight: bold;
            font-style: normal;
            text-decoration: none;
            color: rgb(255, 0, 54);
            font-size: 14px;
            min-width: 100px;
          }
        }
      }
    }
    .invoice {
      padding: 10px;

      /deep/ .el-checkbox__label {
        font-size: 12px;
        color: #333;
      }
    }
    .invoice-operate {
      padding-top: 5px;
      .invoice-op-item {
        padding: 4px 0;
        vertical-align: top;
      }
      .title-label {
        display: inline-block;
        min-width: 85px;
      }
      .invoice-type {
      }

      .invoice-info {
        /deep/ .el-select {
          width: 120px;
        }
        /deep/ .el-radio__label {
          font-size: 12px;
          color: #333;
        }
      }
      .edit-header {
        margin-left: 10px;
      }
      a {
        color: #cc9977;
      }
      a:hover {
        color: #ff0036;
      }
    }
    .invoice-tax-no {
      height: auto;
    }
    .header-box {
      display: inline-block;
      width: 240px;
    }
    .textarea {
      position: relative;
      padding: 10px;
      .textarea-title {
        vertical-align: top;
        display: inline-block;
      }
      .textarea-wrapper {
        width: 330px;
        display: inline-block;
      }
    }
    .shop-sum {
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      align-items: flex-end;
      margin: 0px;
      padding: 10px 0px;
      font-family: verdana, -apple-system, BlinkMacSystemFont, Helvetica Neue,
        Helvetica, Tahoma, PingFang SC, Hiragino Sans GB, Microsoft YaHei,
        sans-serif;
      .label-header {
        font-weight: normal;
        font-style: normal;
        text-decoration: none;
        font-size: 14px;
        min-width: 100px;
        margin-right: 10px;
        display: inline-block;
        width: inherit;
        text-align: right;
      }
      .shop-sum-price {
        font-weight: bold;
        font-style: normal;
        text-decoration: none;
        color: rgb(255, 0, 54);
        font-size: 14px;
        min-width: 100px;
      }
    }
    .label {
      display: inline-block;
      padding: 5px;
    }
  }
  .realpay {
    .pay-info {
      text-align: right;
      margin-top: 15px;
    }
    .box__wrapper {
      display: inline-block;
      border: 1px solid #ff0036;
      .box__shadow {
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
</style>
<style >
.tps.el-tooltip__popper {
  color: #ff0036;
  border: 1px solid #f88578;
  background-color: #ffefed;
  max-width: 240px;
}

.el-tooltip__popper[x-placement^="bottom"] .popper__arrow,
.el-tooltip__popper[x-placement^="bottom"] .popper__arrow::after {
  border-bottom-color: #f88578;
}
.el-tooltip__popper[x-placement^="bottom"] .popper__arrow {
  left: 10px !important;
}
.el-tooltip__popper[x-placement^="bottom"] .popper__arrow::after {
  left: 0px !important;
}
</style>