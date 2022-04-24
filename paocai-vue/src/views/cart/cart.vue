<template>
  <div
    id="cart-page"
    class="cart-page"
  >
    <common-header :loginInfo="loginInfo"></common-header>
    <!-- 顶部搜索栏 -->
    <div class="header-wrapper">
      <div class="cart-header">
        <h1 class="cart-logo">
          <a
            href="/"
            target="_blank"
            title="泡菜商城"
          ><img src="../../assets/logo.png"></a>
        </h1>
        <div class="search">
          <div class="search-triggers">
            <ul class="switchable-nav">
              <li
                v-for="(item,index) in searchTypeList"
                :key="index"
                :class="{'selected':index == searchTypeIndex}"
                @click="selectSearchType(index)"
              ><a>{{item.searchTypeName}}</a></li>
            </ul>
            <i class="el-icon-search"></i>
            <i class="el-icon-arrow-down"></i>
          </div>
          <div class="search-panel">
            <div class="search-panel-fields">
              <div class="search-combobox">
                <div class="search-combobox-input-wrap">
                  <input
                    type="text"
                    class="search-combobox-input"
                  >
                </div>
              </div>
            </div>
            <div class="search-button">
              <button class="btn-search">搜索</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 购物车内容 -->
    <div class="container">
      <div class="cart-layout">
        <div
          v-if="!loading && (cart===null || !cart.shops || cart.shops.length ===0 )"
        >
          <el-empty
            description="您的购物车还是空的，赶紧行动吧！"
            image="https://gtd.alicdn.com/tps/i3/T1TvXUXnNjXXXXXXXX-100-100.png"
            :image-size="250"
          ></el-empty>
        </div>
        <div
          v-else
          class="cart"
        >
          <div
            v-if="cart && cart.shops && cart.shops.length > 0"
            v-loading="loading"
            v-loading.body="loading"
            v-loading.lock="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
          >
            <div class="cart-filter-bar">
              <span
                class="switch-cart">购物车（全部{{cart.skuCount?cart.skuCount:0}}）</span>
              <div class="cart-sum">
                <span class="pay-text">已选商品（不含运费）</span>
                <strong class="price">{{cart.totalAmount |showPrice}}</strong>
                <a
                  href=""
                  class="submit-btn"
                  :class="{'submit-btn-disabled':cart.totalAmount <= 0}"
                >结&nbsp;算</a>
              </div>
            </div>
            <div class="cart-main">
              <div class="cart-table-th">
                <div class="th-wp">
                  <div class="th th-chk">
                    <div
                      class="select-all"
                      @click="handleSelectAll(cart.allChecked)"
                    >
                      <i
                        class="iconfont"
                        :class="[cart.allChecked?'icon-checked':'icon-unchecked']"
                      ></i>
                      &nbsp;&nbsp;全选
                    </div>
                  </div>
                  <div class="th th-item">
                    <div class="td-inner">商品信息</div>
                  </div>
                  <div class="th th-info">&nbsp;</div>
                  <div class="th th-price">单价</div>
                  <div class="th th-amount">数量</div>
                  <div class="th th-sum">金额</div>
                  <div class="th th-op">操作</div>
                </div>
              </div>
              <div class="cart-list">
                <div
                  class="shop-item"
                  v-for="(shop,i1) in cart.shops"
                  :key="i1"
                >
                  <div
                    class="shop-body "
                    :class="{'all-select':shop.allChecked}"
                  >
                    <div class="shop-header">
                      <div class="shop-info">
                        <i
                          class="iconfont"
                          :class="[shop.allChecked?'icon-checked':'icon-unchecked']"
                        ></i>
                        &nbsp;&nbsp;店铺：<a>{{shop.brandName}}</a>
                        <i class="iconfont icon-kefu"></i>
                      </div>
                    </div>
                    <div class="shop-content">
                      <div class="item-list">
                        <div
                          class="bundle"
                          v-for="(item,i2) in shop.items"
                          :key="i2"
                        >
                          <div
                            class="bundle-hd"
                            v-if="(item.fullReductions && item.fullReductions.length>0)||(item.ladders && item.ladders.length>0)"
                          >
                            <div class="td-chk"></div>
                            <div class="bd-title">
                              {{item.skuName}}
                            </div>
                            <div class="bd-promos">
                              <div
                                class="bd-has-promo"
                                v-if="item.discount>0"
                              >已享优惠：<span
                                  class="bd-has-promo-content">省￥{{item.discount | showPrice}}</span>&nbsp;&nbsp;
                              </div>
                              <div
                                class="act-promo"
                                v-if="item.fullReductions && item.fullReductions.length > 0"
                              >
                                <div class="act-promo-wrapper">
                                  <ul class="act-promo-hint">
                                    <li
                                      v-for="(reduce,i3) in item.fullReductions"
                                      :key="i3"
                                    >{{'满'+reduce.fullPrice+'减'+reduce.reducePrice}}
                                    </li>
                                  </ul>
                                </div>
                              </div>
                              <div
                                class="
                                    act-promo"
                                v-if="item.ladders && item.ladders.length > 0"
                              >
                                <div class="act-promo-wrapper">
                                  <ul class="act-promo-hint">
                                    <li
                                      v-for="(lad,i3) in item.ladders"
                                      :key="i3"
                                    >{{'满'+lad.fullCount+'打'+lad.discount+'折,折后'+lad.price+'元/件'}}
                                    </li>
                                  </ul>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="bundle-main">
                            <div class="item-body">
                              <div class="item-content">
                                <div class="td td-chk">
                                  <i
                                    class="iconfont"
                                    :class="[item.checked?'icon-checked':'icon-unchecked']"
                                    @click="handleCheckSku(i1,i2)"
                                  ></i>
                                </div>
                                <div class="td td-item">
                                  <div class="item-pic">
                                    <a>
                                      <el-image
                                        :src="'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg'"
                                        @click="navToProduct(item.skuId)"
                                        :title="item.skuTitle"
                                      >
                                        <div
                                          slot="error"
                                          class="image-slot"
                                        >
                                          <i
                                            class="el-icon-picture-outline"></i>
                                        </div>
                                      </el-image>
                                    </a>
                                  </div>
                                  <div class="item-info">
                                    <div class="item-basic-info">
                                      <a
                                        class="item-title"
                                        :href="'/product?skuId='+item.skuId"
                                        :title="item.skuTitle"
                                      >{{item.skuTitle}}</a>
                                    </div>
                                    <div class="item-other-info"></div>
                                  </div>
                                </div>
                                <div class="td td-info">
                                  <div class="item-props">
                                    <p
                                      class="sku-line"
                                      v-for="(attr,i3) in item.attrs"
                                    >{{attr.attrName +'：'+ attr.attrValue}}</p>
                                  </div>
                                </div>
                                <div class="td td-price">
                                  <div
                                    class="price-line"
                                    v-if="hasDiscount(item.discount)"
                                  >
                                    <em
                                      class="price-original">￥{{item.originalPrice|showPrice}}</em>
                                  </div>
                                  <div class="price-line">
                                    <em
                                      class="price-now">￥{{item.originalPrice|showPrice}}</em>
                                  </div>
                                </div>
                                <div class="td td-amount">
                                  <el-input-number
                                    v-model="item.count"
                                    :min="1"
                                  ></el-input-number>
                                </div>
                                <div class="td td-sum">
                                  <div class="price-sum">
                                    ￥{{item.originalTotalPrice|showPrice}}
                                  </div>
                                </div>
                                <div class="td td-op">
                                  <el-button
                                    type="warning"
                                    icon="el-icon-star-off"
                                    circle
                                    title="移入收藏夹"
                                  ></el-button>
                                  <el-button
                                    type="danger"
                                    icon="el-icon-delete"
                                    circle
                                    title="删除"
                                  ></el-button>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="float-bar-holder">
              <div class="float-bar-left">
                <div class="select-all">
                  <span @click="handleSelectAll(cart.allChecked)"><i
                      class="iconfont"
                      :class="[cart.allChecked?'icon-checked':'icon-unchecked']"
                    ></i>&nbsp;全选</span>

                </div>
                <div class="operations">
                  <a class="batch-delete">删除</a>
                  <a class="batch-fav">移入收藏夹</a>
                </div>
              </div>
              <div class="float-bar-right">
                <div class="amount-sum">
                  <span class="txt">已选商品</span>
                  <em>0</em>
                  <span class="txt">件</span>
                </div>
                <div class="price-sum">
                  <span class="txt">合计（不含运费）：</span>
                  <strong class="price">{{cart.totalAmount|showPrice}}</strong>
                </div>
                <div class="btn-area">
                  <a
                    href=""
                    class="submit-btn"
                    :class="{'submit-btn-disabled':cart.totalAmount <= 0}"
                  >结&nbsp;算</a>
                </div>
              </div>
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
  name: 'myCart',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      searchTypeIndex: 0,
      searchTypeList: [{
        searchType: 0,
        searchTypeName: '宝贝'
      }, {
        searchType: 1,
        searchTypeName: '店铺'
      }, {
        searchType: 2,
        searchTypeName: '泡泡'
      }],
      cart: {
        skuCount: 0,
        totalCount: 0,
        allChecked: false
      },
      loading: true
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
    // 选择搜索类型
    selectSearchType (index) {
      let obj = this.searchTypeList[0];
      this.searchTypeList[0] = this.searchTypeList[index];
      this.searchTypeList[index] = obj;
      this.$forceUpdate();
    },
    // 获取用户购物车数据
    async getCart () {
      try {
        const res = await this.$request({
          url: 'cart/my_cart',
          method: 'GET'
        });
        if (res.code !== 200) {
          this.$message({
            type: 'info',
            message: res.msg
          });
          return;
        }
        let cart = res.data;
        if (cart == null) {
          return;
        }
        // 初始化不选中
        cart.allChecked = false;
        cart.shops.forEach((v1, i1) => {
          v1.allChecked = false;
          v1.items.forEach((v2, i2) => {
            v2.checked = false;
          });
        });
        this.cart = cart;
      } finally {
        this.loading = false;
      }
    },
    // 打开对应的sku详情页
    navToProduct (skuId) {
      this.$router.push({ name: 'product', query: { skuId: skuId } });
    },
    // 是否有会员价格优惠
    hasDiscount (item) {
      return parseFloat(item.originalPrice) > parseFloat(item.price);
    },
    // 选中商品
    handleCheckSku (i1, i2) {
      cart.shops[i1].items[i2].checked = !cart.shops[i1].items[i2].checked
    },
    handleSelectAll (isAllChecked) {
      const checked = !isAllChecked;
      let cart = this.cart;
      cart.shops.forEach((v1, i1) => {
        v1.allChecked = checked;
        v1.items.forEach((v2, i2) => {
          v2.checked = checked;
        });
      });
      this.cart = cart;
    }
  },
  created () {
    document.title = '泡菜商城-我的购物车';
    this.getLoginInfo();
    this.loading = true;
    this.getCart();
  },
  filters: {
    showPrice (price) {
      return parseFloat(price).toFixed(2);
    }
  }
}
</script>

<style lang="less" scoped>
#cart-page {
  font: 12px/1.5 tahoma, arial, "Hiragino Sans GB", "\5b8b\4f53", sans-serif;
  background: 0 36px repeat-y
    url("https://img.alicdn.com/imgextra/i3/O1CN01PaQurJ1QgnAICTCgg_!!6000000002006-2-tps-1490-2984.png");
}
.el-empty {
  background-color: #fff;
  border-radius: 24px;
  overflow: hidden;
}
.header-wrapper {
  background-color: #fff;
  margin-bottom: 24px;
  .cart-header {
    width: 1200px;
    padding: 11px 0;
    position: relative;
    height: 80px;
    margin: 0 auto;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    .cart-logo {
      a {
        display: block;
        text-align: center;
        height: 58px;
        img {
          width: auto;
          height: 58px;
        }
      }
    }

    .search {
      border-radius: 21px;
      height: 42px;
      display: flex;
      .search-triggers {
        position: relative;
        z-index: 1000;
        width: 104px;
        border-radius: 21px 0 0 21px;
        overflow: hidden;
        .switchable-nav {
          width: 104px;
          position: absolute;
          left: 0;
          top: 0;
          z-index: 1;
          height: 42px;
          background-color: #fff;
          border-right: 1px solid #f5f5f5;
          border-left: 1px solid #f5f5f5;
          overflow: hidden;
          zoom: 1;

          li {
            position: relative;
            zoom: 1;
            width: 100%;
            cursor: pointer;
            display: none;
          }
          li.selected {
            display: block;
          }
          a {
            display: inline-block;
            height: 100%;
            width: 100%;
            background: #f5f5f5;
            border: 0 none;
            color: #000;
            font-weight: 400;
            font-size: 12px;
            padding: 0;
            text-align: center;
            line-height: 42px;
          }
          a:hover {
            color: #000 !important;
            background-color: #f5f5f5;
            text-decoration: none;
            cursor: auto;
          }
        }
        i {
          position: absolute;
          font-size: 16px;
          font-weight: 700;
          z-index: 2;
        }
        .el-icon-search {
          top: 50%;
          left: 15px;
          transform: translateY(-50%);
          color: #000;
        }
        .el-icon-arrow-down {
          top: 50%;
          right: 15px;
          transform: translateY(-50%);
          color: #ccc;
        }
      }
      .search-panel {
        width: 320px;
        background-color: #ebebeb;
        position: relative;
        border-radius: 0 21px 21px 0;
        zoom: 1;
        overflow: hidden;
        .search-panel-fields {
          height: 42px;
          position: relative;
          zoom: 1;
          background-color: #ebebeb;

          overflow: hidden;
          .search-combobox {
            height: 42px;
            font-size: 12px;
          }
          .search-combobox-input-wrap {
            height: 42px;
            position: relative;
            overflow: hidden;
            vertical-align: middle;
            .search-combobox-input {
              vertical-align: middle;
              color: #000;
              overflow-y: visible;
            }

            input {
              width: 100%;
              outline: 0;
              padding-right: 85px;
              text-indent: 5px;
              font-size: 14px;
              position: absolute;
              height: 42px;
              line-height: 42px;
              background-color: #ebebeb;
              border: 0 none;
              z-index: 2;
            }
          }
        }
        .search-button {
          position: absolute;
          z-index: 2;
          top: 1px;
          right: 1px;
          .btn-search {
            background-image: linear-gradient(
              to right,
              #ff9000 0,
              #ff5000 100%
            );
            background-repeat: repeat-x;
            text-align: center;
            width: 80px;
            height: 40px;
            line-height: 40px;
            -webkit-border-radius: 40px;
            -moz-border-radius: 40px;
            -ms-border-radius: 40px;
            border-radius: 40px;
            color: #fff;
            background-position: 0 0;
            font-size: 18px;
            font-weight: 700;
            background-color: #ff5000;
            cursor: pointer;
            border: none;
            margin-left: 2px;
          }
        }
      }
    }
  }
}

.container {
  width: 1200px;
  margin: 0 auto;
  .cart-layout {
    /deep/ .el-icon-loading {
      font-size: 40px;
    }
    /deep/ .el-loading-text {
      font-size: 24px;
    }
    .cart {
      min-height: 400px;
      background-color: #fff;
      -webkit-border-radius: 24px;
      -moz-border-radius: 24px;
      -ms-border-radius: 24px;
      border-radius: 24px;
      overflow: hidden;
      .cart-filter-bar {
        overflow: hidden;
        font-size: 12px;
        position: relative;
        height: 72px;
        padding: 0 18px;
        border-bottom: 1px solid #e6e6e6;
        .switch-cart {
          overflow: hidden;
          color: #000;
          font-size: 18px;
          font-weight: 600;
          height: 72px;
          line-height: 72px;
        }

        .cart-sum {
          position: absolute;
          right: 18px;
          top: 0;
          height: 72px;
          line-height: 72px;
          font-size: 14px;
          .pay-text {
            position: relative;
            top: -2px;
          }

          .price {
            font-family: Arial, Verdana;
            font-weight: 400;
            margin-right: 12px;
            font-size: 22px;
            color: #ff5000;
          }
        }
      }

      .cart-main {
        min-height: 210px;
        .cart-table-th {
          overflow: hidden;
          height: 50px;
          line-height: 50px;
          color: #3c3c3c;
          font-weight: 700;
          .th-wp {
            display: flex;
            flex-direction: row;
            .th {
              font-size: 13px;
            }
            .th-chk {
              width: 195px;
              padding-left: 10px;
              .select-all {
                width: 80px;
                text-align: center;
                cursor: pointer;

                .iconfont {
                  font-size: 14px;
                }
              }
            }

            .th-item {
              width: 255px;
            }

            .th-info {
              width: 220px;
            }

            .th-price {
              width: 150px;
              padding-left: 15px;
            }

            .th-amount {
              width: 120px;
              padding-left: 28px;
            }

            .th-sum {
              width: 170px;
              padding-left: 15px;
            }

            .th-op {
              flex: 1;
            }
          }
        }

        .cart-list {
          width: 1200px;

          .shop-item {
            height: auto;
            .shop-body {
              margin-bottom: 15px;
              .shop-header {
                position: relative;
                height: 38px;
                background: #fff;
                overflow: hidden;
                .shop-info {
                  position: relative;
                  z-index: 2;
                  line-height: 38px;
                  padding-left: 15px;

                  a:hover {
                    color: #f40;
                    text-decoration: underline;
                  }
                  .icon-kefu {
                    margin-left: 10px;
                    color: #1c9eff;
                    font-weight: 700;
                    cursor: pointer;
                  }
                }
              }

              .shop-content {
                border: 1px solid #f5f5f5;
                background-color: #f5f5f5;
                -webkit-border-radius: 18px;
                -moz-border-radius: 18px;
                -ms-border-radius: 18px;
                border-radius: 18px;
                width: 1140px;
                margin: 0 auto;
                overflow: hidden;
                .item-list {
                  .bundle {
                    overflow: hidden;
                    border-bottom: 1px solid #e8e8e8;
                  }
                  .bundle-hd {
                    height: 41px;
                    padding-top: 9px;
                    padding-bottom: 3px;
                    border-bottom: 1px solid #e8e8e8;
                    display: flex;
                    .td-chk {
                      height: 24px;
                      width: 50px;
                    }
                    .bd-title {
                      height: 22px;
                      line-height: 22px;
                      color: #6c6c6c;
                      margin-right: 15px;
                      padding: 0 8px;
                      background: #e7e7e7;
                      position: relative;
                    }
                    .bd-promos {
                      color: #6c6c6c;
                      line-height: 22px;
                      display: flex;
                      .bd-has-promo {
                        max-width: 800px;
                        height: 20px;
                        overflow: hidden;
                        color: #f40;
                        float: left;
                        font-family: verdana;
                      }
                      .act-promo {
                        margin-right: 20px;
                        .act-promo-wrapper {
                          max-width: 400px;
                          height: 23px;
                          overflow: hidden;
                          li {
                            padding: 0 3px;
                            white-space: nowrap;
                          }
                        }
                      }
                    }
                  }
                  .bundle-main {
                    padding-top: 20px;
                    .item-body {
                      .item-content {
                        display: flex;
                        flex-direction: row;
                        .td-chk {
                          width: 50px;
                          .iconfont {
                            margin-left: 20px;
                          }
                        }

                        .td-item {
                          width: 370px;
                          min-height: 119px;
                          overflow: hidden;
                          padding-right: 15px;
                          display: flex;
                          justify-content: space-between;
                          .item-pic {
                            background: #fff;
                            width: 100px;
                            height: 100px;
                            -webkit-border-radius: 3px;
                            -moz-border-radius: 3px;
                            -ms-border-radius: 3px;
                            border-radius: 3px;
                            border: 1px solid #eee;
                            overflow: hidden;
                            a {
                              display: table-cell;
                              width: 100px;
                              height: 100px;
                              -webkit-border-radius: 3px;
                              -moz-border-radius: 3px;
                              -ms-border-radius: 3px;
                              border-radius: 3px;
                              vertical-align: middle;
                              text-align: center;
                            }
                            img {
                              max-height: 100px;
                              max-width: 100px;
                              vertical-align: middle;
                            }
                          }
                          .item-info {
                            width: 240px;
                            .item-basic-info {
                              min-height: 40px;
                              .item-title {
                                font-size: 12px;
                                display: block;
                                max-height: 36px;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                color: #3c3c3c;
                              }
                              .item-title:hover {
                                color: #f40;
                                text-decoration: underline;
                              }
                            }
                          }
                        }

                        .td-info {
                          width: 220px;
                          .item-props {
                            min-height: 84px;
                            .sku-line {
                              margin: 0 5px 0 15px;
                              color: #9c9c9c;
                              overflow: hidden;
                              text-overflow: ellipsis;
                            }
                          }
                        }

                        .td-price {
                          width: 150px;
                          padding-left: 15px;
                          .price-line {
                            color: #3c3c3c;
                            font-weight: 700;
                            font-family: Verdana, Tahoma, arial;
                          }
                          .icon-money {
                            font-size: 8px;
                          }
                          .price-original {
                            color: #9c9c9c;
                            text-decoration: line-through;
                            font-family: Verdana, Tahoma, arial;
                          }
                          .price-now {
                            color: #3c3c3c;
                            font-weight: 700;
                            font-family: Verdana, Tahoma, arial;
                          }
                        }

                        .td-amount {
                          width: 120px;
                          .el-input-number {
                            width: 80px;
                            line-height: 30px;
                            /deep/ .el-input-number__decrease {
                              height: 28px;
                              width: 20px;
                            }
                            /deep/ .el-input-number__increase {
                              width: 20px;
                              height: 28px;
                            }
                            /deep/ .el-input__inner {
                              padding: 0;
                              height: 30px;
                              line-height: 30px;
                            }
                          }
                        }

                        .td-sum {
                          width: 140px;
                          .price-sum {
                            font-family: Verdana, Tahoma, arial;
                            color: #f40;
                            font-weight: 700;
                            font-size: 14px;
                          }
                        }

                        .td-op {
                          flex: 1;
                          display: flex;
                          flex-direction: column;
                          justify-content: flex-start;
                          align-items: center;
                          margin-top: -10px;

                          .el-button {
                            margin-left: 0;
                            margin-bottom: 5px;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            .all-select {
              .shop-content {
                background: #fff9f6;
                border: 1px solid #ff5000;
              }
            }
          }
        }
      }

      .float-bar-holder {
        height: 72px;
        overflow: hidden;
        margin: 0 24px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: #3c3c3c;
        font-size: 13px;
        .txt {
          line-height: 48px;
          font-size: 13px;
          color: #000;
        }
        .float-bar-left {
          display: flex;
          align-items: center;
          .select-all {
            width: 70px;
            line-height: 72px;
            height: 72px;
          }
          .operations {
            line-height: 72px;
            height: 72px;
            a {
              margin-left: 25px;
            }
            a:hover {
              color: #f40;
              text-decoration: underline;
            }
          }
        }
        .float-bar-right {
          display: flex;
          align-items: center;
          .amount-sum {
            cursor: pointer;
            height: 48px;
            line-height: 48px;
            color: #3c3c3c;
          }
          em {
            font-style: normal;
            padding: 0 5px;
            font-size: 20px;
            color: #f40;
            font-weight: 700;
            font-family: tohoma, arial;
          }
          .price-sum {
            height: 48px;
            line-height: 48px;
            margin-left: 30px;
            .price {
              font-weight: 700;
              font-size: 22px;
              color: #f40;
              font-family: tohoma, arial;
            }
          }
          .btn-area {
            margin-left: 15px;
          }
        }
      }
    }
  }

  .submit-btn {
    display: inline-block;
    width: 74px;
    height: 42px;
    line-height: 42px;
    color: #fff;
    background: #ff5000;
    -webkit-border-radius: 21px;
    -moz-border-radius: 21px;
    -ms-border-radius: 21px;
    border-radius: 21px;
    text-align: center;
    cursor: pointer;
    text-decoration: none;
    position: relative;
    top: 0px;
    font-size: 16px;
  }
  .submit-btn-disabled {
    background: #aaa;
    color: #fff;
  }
  .submit-btn-disabled:hover {
    cursor: not-allowed;
  }

  .icon-checked {
    font-size: 14px;
    font-weight: 700;
    cursor: pointer;
    color: #ff5000;
  }
  .icon-unchecked {
    font-size: 14px;
    cursor: pointer;
  }
}
</style>