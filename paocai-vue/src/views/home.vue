<template>
  <div id="home-page">
    <!-- 头部组件 -->
    <common-header
      :login-info="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <!-- 主要内容 -->
    <div class="home-content">
      <!-- 顶部搜索栏 -->
      <div :class="[scrollTop>=250?'search-fixed':'top']">
        <div class="top-wrap">
          <div class="logo">
            <a href="#">
              <img src="../assets/logo.png">
            </a>
          </div>
          <div class="search">
            <div class="search-wrap">
              <!-- 搜索框 -->
              <div class="search-bd">
                <div class="search-tab-header">
                  <div class="search-tab-list">
                    <ul class="ks-switchable-nav">
                      <li
                        v-for="(item,index) in searchTypeList"
                        :key="index"
                        class="search-tab-item"
                        :class="{'selected':index == searchTypeIndex}"
                        @click="selectSearchType(index)"
                      >
                        {{item.searchTypeName}}
                      </li>
                    </ul>
                    <div class="search-tab-icon">
                      <i class="iconfont icon-arrow-down"></i>
                    </div>
                  </div>
                </div>
                <div class="search-panel">
                  <div class="search-button">
                    <a
                      class="btn-search"
                      :href="'/search?keyword='+keyword"
                    >搜索</a>
                  </div>
                  <div class="search-panel-fields">
                    <div class="search-combobox">
                      <div class="search-combobox-input-wrap">
                        <input
                          class="search-combobox-input"
                          id="keyword"
                          v-model="keyword"
                          placeholder=""
                          @keyup.enter="search"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 搜索热词 -->
              <div class="search-ft">
                <div class="hotword">
                  <div class="search-hots-lines">
                    <a
                      class="hots-item"
                      v-for="(item,index) in hotWords"
                      :key="index"
                      :href="'/search?keyword='+item"
                    >
                      {{item}}
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="pc-nav">
        <div class="nav">
          <h2>分类市场</h2>
          <ul class="nav-hd">
            <li style="color:#FF0036"><a href="#">泡泡</a></li>
            <li style="color:#FF0036"><a href="#">百亿补贴</a></li>
            <li style="color:#33c900"><a href="#">泡泡超市</a></li>
          </ul>
          <ul class="nav-bd">
            <li class="pipe">|</li>
            <li><a href="#">司法拍卖</a></li>
            <li><a href="#">八戒旅行</a></li>
            <li><a href="#">天天特卖</a></li>
          </ul>
          <ul class="nav-bd">
            <li class="pipe">|</li>
            <li><a href="#">造点新货</a></li>
            <li><a href="#">泡泡易购</a></li>
            <li><a href="#">泡菜心选</a></li>
            <li><a href="#">智能生活</a></li>
            <li><a href="#">泡菜直播</a></li>
          </ul>
        </div>
      </div>
      <div class="screen-outer">
        <div class="main">
          <div class="main-inner">
            <div
              class="category"
              @mouseleave="outCateMenu"
            >
              <ul class="category-bd">
                <li
                  class="cate-menu-item"
                  :class="{'cate-menu-item-on':cateIndex==index}"
                  v-for="(item,index) in category"
                  :key="index"
                  @mouseenter="selectCateMenu(index)"
                >{{item.name}}</li>
              </ul>
              <div
                class="cate-pop"
                v-show="cateIndex >= 0"
              >
                <div
                  class="cate-part"
                  v-for="(item1,index1) in category"
                  :key="index1"
                  v-show="cateIndex===index1"
                >
                  <div class="cate-detail">
                    <dl
                      class="cate-detail-item"
                      v-for="(item2,index2) in item1.children"
                      :key="index2"
                    >
                      <dt class="cate-detail-tit">
                        <a
                          :href="'search?catalog3Id='+item2.catId"
                          target="_blank"
                        >{{item2.name}}</a>
                        <i class="iconfont icon-arrow-right"></i>
                      </dt>
                      <dd class="cate-detail-con">
                        <a
                          :href="'search?catalog3Id='+item3.catId"
                          class="cate-detail-con-lk"
                          v-for="(item3,index3) in item2.children"
                          :key="index3"
                        >{{item3.name}}</a>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>
            <div class="core">
              <div class="promo">
                <div class="promo-bd">
                  <el-carousel
                    :interval="5000"
                    arrow="always"
                    indicator-position="none"
                  >
                    <el-carousel-item
                      v-for="(item,index) in promoteCarousel"
                      :key="index"
                    >
                      <h3><a
                          href="#"
                          target="_blank"
                        ><img
                            :src="item.imgUrl"
                            :title="item.imgName"
                          ></a></h3>
                    </el-carousel-item>
                  </el-carousel>
                </div>
              </div>
              <div class="tmall">
                <div class="tmall-bd">
                  <el-carousel
                    :interval="5000"
                    arrow="always"
                    indicator-position="none"
                  >
                    <el-carousel-item
                      v-for="(item,index) in smallPromoteCarousel"
                      :key="index"
                    >
                      <a
                        href="#"
                        target="_blank"
                      ><img
                          :src="item.left.imgUrl"
                          :title="item.left.imgName"
                        ></a>
                      <a
                        href="#"
                        target="_blank"
                      ><img
                          :src="item.right.imgUrl"
                          :title="item.right.imgName"
                        ></a>
                    </el-carousel-item>
                  </el-carousel>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-right">
          <!-- 用户信息 -->
          <div class="member">
            <div class="member-bd">
              <div class="avatar-wrapper">
                <a
                  href="#"
                  class="member-home"
                >
                  <img
                    class="member-avatar"
                    v-if="loginInfo.id != '' && loginInfo.header &&  loginInfo.header != ''"
                    :src="loginInfo.header"
                  >
                  <img
                    class="member-avatar"
                    src="../assets/images/default_user.png"
                    v-else
                  ></a>
              </div>
              <a
                class="member-nickurl"
                href="#"
              >
                <span class="member-nick-info">
                  Hi
                  <strong class="member-nick">
                    {{loginInfo.id==''?'你好':!loginInfo.nickname|| loginInfo.nickname==''?loginInfo.username:loginInfo.nickname}}
                  </strong>
                </span>
              </a>
            </div>
            <div class="member-ft">
              <!-- 未登录-->
              <div
                class="member-logout"
                v-if="loginInfo.id == ''"
              >
                <a
                  @click="toLogin"
                  class="member-logout-op"
                >登录</a>
                <a
                  @click="toRegist"
                  class="member-logout-op"
                >注册</a>
                <a
                  href="#"
                  class="member-logout-op"
                >开店</a>
              </div>
              <!-- 已登录-->
              <div
                class="member-login"
                v-else
              >
                <div class="order-status-info">
                  <a
                    href="#"><strong>{{orderStatusInfo.cartNum}}</strong>购物车</a><a
                    href="#"
                  ><strong>{{orderStatusInfo.waitReceiveNum}}</strong>待收货</a><a
                    href="#"
                  ><strong>{{orderStatusInfo.waitDeliverNum}}</strong>待发货</a><a
                    href="#"
                  ><strong>{{orderStatusInfo.waitPayNum}}</strong>待付款</a><a
                    href="#"
                  ><strong>{{orderStatusInfo.waitCommentNum}}</strong>待评论</a>
                </div>
              </div>
            </div>
          </div>
          <div class="tip">
            <div class="tip-mod">
              <a
                href="#"
                target="_blank"
              >
                <img src="../assets/images/10001.png">
              </a>
            </div>
          </div>
          <div class="notice">
            <div class="notice-top">
              <span class="notice-title">公告</span>
              <span class="notice-desc">新鲜事儿都在这里～</span>
            </div>
            <div class="notice-content">
              <a href="#">
                <div class="notice-tip">规则</div>
              </a>
              <a href="#">
                <p>奥运知识产权规则解读</p>
              </a>
            </div>
            <div class="notice-content">
              <a href="#">
                <div class="notice-tip">热点</div>
              </a>
              <a href="#">
                <p>关于推进“清朗”账号乱象专项整治的通知</p>
              </a>
            </div>
            <div class="notice-content">
              <a href="#">
                <div class="notice-tip">热点</div>
              </a>
              <a href="#">
                <p>泡菜平台打击流量造假、黑公关、网络水军公告</p>
              </a>
            </div>
          </div>
          <div class="mypao">
            <div class="mypao-content">
              <a href="#">
                <span class="iconfont icon-collection"></span>
                <p>收藏的宝贝</p>
              </a>
            </div>
            <div class="mypao-content">
              <a href="#">
                <span class="iconfont icon-package"></span>
                <p>买过的店铺</p>
              </a>
            </div>
            <div class="mypao-content">
              <a href="#">
                <span class="iconfont icon-shop"></span>
                <p>收藏的店铺</p>
              </a>
            </div>
            <div class="mypao-content">
              <a href="#">
                <span class="iconfont icon-history"></span>
                <p>我的足迹</p>
              </a>
            </div>
          </div>
        </div>
      </div>
      <!-- 秒杀商品 -->
      <div
        id="seckill-container"
        class="seckill-container"
      >
        <div class="seckill-inner">
          <a
            href="#"
            class="seckill-countdown"
          >
            <div class="countdown-title">泡菜秒杀</div>
            <div class="countdown-icon">
              <i class="iconfont icon-shandian"></i>
            </div>
            <div>
              <div class="countdown-desc">
                <strong>20:00</strong>点场 距结束
              </div>
              <span class="timmer countdown-main">
                <span class="timmer-unit timmer-unit-hour">
                  01
                </span><span class="timmer-unit timmer-unit-minute">
                  02
                </span><span class="timmer-unit timmer-unit-second">
                  03
                </span>
              </span>
            </div>
          </a>
          <div class="seckill-list">
            <div class="slider-wrapper">
              <el-carousel
                :interval="3000"
                arrow="always"
                height="280px"
                indicator-position="none"
              >
                <el-carousel-item
                  v-for="(item,index) in promoteCarousel"
                  :key="index"
                >
                  <a
                    href="#"
                    v-for="i in 4"
                    :key="i"
                  >
                    <div class="seckill-item">
                      <div class="seckill-item-image">
                        <img :src="item.imgUrl">
                      </div>
                      <h6 class="seckill-item-name">商品名称</h6>
                      <div class="seckill-item-price">
                        <span class="price-origin">
                          <i class="iconfont icon-money"></i>
                          <span>
                            199.00
                          </span>
                        </span>
                      </div>
                    </div>
                  </a>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
          <div class="seckill-brand">
            <div class="seckill-brand-slider">
              <el-carousel
                :interval="3000"
                arrow="none"
                height="260px"
              >
                <el-carousel-item
                  v-for="item in 2"
                  :key="item"
                >
                  <a href="#">
                    <div class="item-image">
                      <img src="../assets/images/login_bg.png">
                    </div>
                    <div class="item-info">
                      <div class="item-info-title">小米秒杀专场</div>
                      <div class="item-info-promo">品质好物，点击抢购</div>
                      <div class="item-info-action">
                        品牌秒杀<i class="iconfont icon-arrow-right"></i>
                      </div>
                    </div>
                  </a>
                </el-carousel-item>
              </el-carousel>
            </div>
          </div>
        </div>
      </div>
      <div
        id="hotsale-container"
        class="hotsale-container"
      >
        <div class="hotsale-hd">
          <h3><span class="hotsale-title">
              猜你喜欢
            </span><span class="hotsale-icon">
              个性推荐
            </span></h3>
        </div>
        <div class="list">
          <ul>
            <li
              class="item"
              v-for="(item,index) in hotSale.hotSales"
              :key="index"
            >
              <a
                href=""
                class="hotsale-item"
              >
                <div class="img-wrapper">
                  <img :src="item.skuDefaultImg">
                </div>
                <h4>{{item.skuTitle}}</h4>
                <p class="info">
                  <span class="price">
                    <i class="iconfont icon-money"></i>
                    {{item.price}}
                  </span>
                </p>
              </a>
            </li>
          </ul>
        </div>
        <div></div>
        <div
          class="hotsale-ft"
          v-show="loading"
        >
          <i class="el-icon-loading"></i>
        </div>
        <div
          class="hotsale-ft"
          v-show="noMore"
        >
          <i class="hotsale-end">END</i>
        </div>
      </div>
    </div>
    <!-- 侧边工具 -->
    <div
      class="fixedtool"
      :style="[{position:(scrollTop>530?'fixed':'absolute')},{top:(scrollTop>530?'75px':'600px')}]"
    >
      <div class="fixedtool-list">
        <div
          class="fixedtool-item goods"
          :class="{'on':scrollTop<=835}"
          @click="goAnchor(555)"
        >泡菜秒杀</div>
        <div
          class="fixedtool-item hotsale"
          :class="{'on':scrollTop>835}"
          @click="goAnchor(840)"
        >猜你喜欢</div>
        <div class="fixedtool-item">反馈</div>
        <el-backtop
          :visibility-height="400"
          :right="60"
          class="fixedtool-item"
        >
        </el-backtop>
      </div>
    </div>
    <!-- 底部组件 -->
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
import { getLoginInfo } from '../utils';
export default {
  name: 'home',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      orderStatusInfo: {
        cartNum: 3,
        waitReceiveNum: 2,
        waitDeliverNum: 1,
        waitPayNum: 0,
        waitCommentNum: 5
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
      keyword: '',
      hotWords: ['新款连衣裙', '四件套', '潮流T恤', '时尚女鞋', '短裤', '半身裙', '男士外套', '墙纸', '行车记录仪', '新款男鞋', '耳机', '时尚女包', '沙发'],
      scrollTop: 0, //屏幕高度
      category: [],
      cateIndex: -1,
      promoteCarousel: [],
      smallPromoteCarousel: [],
      maxPage: 5,
      hotSale: {
        page: 1,
        totalPage: 3,
        pageSize: 20,
        total: 20,
        hotSales: []
      },
      loading: false
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
    // 获取首页需要的数据
    async getIndexData () {
      let res = await this.$request({
        url: 'product/index/data',
        method: 'POST'
      });
      this.$handleResponseMessage(res, '', '未知错误，无法获取首页资源');
      const data = res.data;
      this.category = data.category;
      this.hotWords = data.hotWords;
      this.orderStatusInfo = data.orderStatusInfo;
      this.promoteCarousel = data.promoteCarousel;
      this.smallPromoteCarousel = data.smallPromoteCarousel;
      this.hotSale = data.hotSale;
    },
    // 选择搜索类型
    selectSearchType (index) {
      let obj = this.searchTypeList[0]
      this.searchTypeList[0] = this.searchTypeList[index]
      this.searchTypeList[index] = obj
      this.$forceUpdate();
    },
    // 热词搜索
    searchHot (hotword) {
      console.log('热词:' + hotword)
    },
    // 搜索
    search () {
      if (this.keyword.trim() === '') {
        return;
      }
      this.$router.push({ name: 'search', query: { keyword: this.keyword } })
    },
    // 保存滚动值，这是兼容的写法
    handleScroll () {
      console.log(1);
      this.scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      var wScrollY = window.scrollY; //当前滚动条位置
      var wInnerH = window.innerHeight; //设备窗口的高度（不会变）
      var bscrollH = document.body.scrollHeight; //滚动条总高度
      if (!this.loading && !this.noMore && wScrollY + wInnerH >= bscrollH - 10) {
        //你需要做的动作
        this.loadHotSale();
      }
    },
    selectCateMenu (index) {
      this.cateIndex = index;
    },
    outCateMenu () {
      this.cateIndex = -1;
    },
    goAnchor (height) {
      document.documentElement.scrollTop = height
    },
    toLogin () {
      this.$router.push({ name: 'login' })
    },
    toRegist () {
      this.$router.push({ name: 'regist' })
    },
    async loadHotSale () {
      this.loading = true;
      const page = this.hotSale.page + 1;
      const pageSize = this.hotSale.pageSize;
      try {
        const res = await this.$request({
          url: 'product/skuinfo/hotsale?page=' + page + '&pageSize=' + pageSize,
          method: 'GET'
        });
        if (res.code !== 200) {
          this.$message({
            type: 'error',
            message: res.msg ? res.msg : '未知错误，无法获取首页资源'
          });
          return;
        }
        this.hotSale.page = page;
        this.hotSale.hotSales = this.hotSale.hotSales.concat(res.data);
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 2000);
      }
    },
    handleNavTo (routerName) {
      console.log(routerName);
      this.$router.push({ name: routerName });
    }
  },
  async created () {
    document.title = "泡菜商城"
    this.getLoginInfo();
    this.getIndexData();
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll, true)
  },
  destroyed () {
    // 离开该页面需要移除这个监听的事件，不然会报错
    window.removeEventListener('scroll', this.handleScroll)
  },
  computed: {
    noMore () {
      return this.hotSale.page >= this.maxPage;
    },
    disabled () {
      return this.loading || this.noMore()
    }
  }
}
</script>

<style lang="less" scoped>
#home-page {
  background: 0 36px repeat url(../assets/images/bg.png) !important;
}
.top {
  padding-top: 25px;
  height: 125px;
  position: relative;
  z-index: 9999;

  .top-wrap {
    width: 1200px;
    height: 100px;
    margin: 0 auto;
    position: relative;
    display: flex;

    .logo {
      height: 60px;
      margin-top: 10px;

      a {
        display: block;
        height: 60px;
        margin: 0 25px;

        img {
          height: 60px;
        }
      }
    }

    .search {
      margin-left: 100px;

      .search-wrap {
        position: relative;

        .search-bd {
          position: relative;
          background: #fff;
          border-radius: 20px;
          margin-top: 12px;
          border: 1px solid #ff5000;
          z-index: 30;

          .search-tab-header {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 200;
            width: 75px;
            height: 40px;
            background-color: #fff;
            border-top-left-radius: 20px;
            border-bottom-left-radius: 20px;

            .search-tab-list {
              li {
                line-height: 40px;
                height: 40px;
                color: #3c3c3c;
                width: 75px;
                background-color: #fff;
                text-align: center;
                cursor: pointer;
                display: none;
              }

              .selected {
                display: block;
                color: #333;
                background-image: none;
                border-top-left-radius: 20px;
                border-bottom-left-radius: 20px;
              }

              li:hover {
                color: #ff5000;
                font-weight: 600;
                font-size: 13px;
                display: block;
              }

              .selected:hover {
                color: #333;
                font-size: 12px;
                font-weight: 400;
                background-image: none;
              }
            }

            .search-tab-list:hover li {
              display: block;
            }

            .search-tab-list:hover .selected {
              border-bottom-left-radius: 0;
            }

            .search-tab-icon {
              position: absolute;
              top: 12px;
              right: 3px;

              i {
                font-size: 12px;
              }
            }
          }

          .search-panel {
            position: relative;

            .search-button {
              position: absolute;
              width: 75px;
              height: 36px;
              border-radius: 20px;
              right: 3px;
              top: 50%;
              transform: translateY(-50%);
              overflow: hidden;

              .btn-search {
                display: block;
                font: 12px/1.5 tahoma, arial, "Hiragino Sans GB", "\5b8b\4f53",
                  sans-serif;
                font-size: 18px;
                font-weight: 700;
                text-align: center;
                color: #fff;
                cursor: pointer;
                height: 36px;
                line-height: 36px;
                border: none;
                width: 100%;
                background-image: linear-gradient(
                  to right,
                  #ff9000 0,
                  #ff5000 100%
                );
              }
            }

            .search-panel-fields {
              width: 630px;
              height: 40px;

              .search-combobox {
                font-size: 12px;

                .search-combobox-input-wrap {
                  margin-left: 75px;
                  border-left: 1px solid #f5f5f5;

                  .search-combobox-input {
                    text-indent: 10px;
                    height: 40px;
                    line-height: 40px;
                    width: 554px;
                    padding-right: 90px;
                    border: none;
                    border-radius: 0 20px 20px 0;
                    outline: 0;
                    background: #fff;
                    color: #000;
                  }
                }
              }
            }
          }
        }

        .search-ft {
          margin-top: 5px;

          .hotword {
            .search-hots-lines {
              display: flex;
              justify-content: flex-start;

              .hots-item {
                font-size: 12px;
                margin-right: 5px;
                cursor: pointer;
                color: #3c3c3c;
              }

              .hots-item:hover {
                color: #ff5000;
              }
            }
          }
        }
      }
    }
  }
}

.search-fixed {
  width: 100%;
  position: fixed;
  left: 0;
  top: 0;
  height: 60px;
  padding: 10px 0;
  background-color: #fff;
  border-bottom: solid 1px #ff5000;
  overflow: visible;
  z-index: 9999;

  .top-wrap {
    width: 1200px;
    height: 40px;
    margin: 0 auto;
    position: relative;
    display: flex;

    .logo {
      height: 30px;
      margin-top: 5px;

      a {
        display: block;
        height: 30px;
        margin: 0 25px;

        img {
          height: 30px;
        }
      }
    }

    .search {
      margin-left: 180px;

      .search-wrap {
        .search-bd {
          position: relative;
          background: #fff;
          border-radius: 20px;
          border: 1px solid #ff5000;
          z-index: 30;

          .search-tab-header {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 200;
            width: 75px;
            height: 40px;
            background-color: #fff;
            border-top-left-radius: 20px;
            border-bottom-left-radius: 20px;

            .search-tab-list {
              li {
                line-height: 40px;
                height: 40px;
                color: #3c3c3c;
                width: 75px;
                background-color: #fff;
                text-align: center;
                cursor: pointer;
                display: none;
              }

              .selected {
                color: #333;
                background-image: none;
                border-top-left-radius: 20px;
                border-bottom-left-radius: 20px;
                display: block;
              }

              li:hover {
                color: #ff5000;
                font-weight: 600;
                font-size: 13px;
              }

              .selected:hover {
                color: #333;
                font-size: 12px;
                font-weight: 400;
                background-image: none;
              }

              .search-tab-icon {
                position: absolute;
                top: 12px;
                right: 3px;
                cursor: pointer;

                i {
                  font-size: 12px;
                }
              }
            }

            .search-tab-list:hover li {
              display: block;
            }

            .search-tab-list:hover .selected {
              border-bottom-left-radius: 0;
            }
          }

          .search-panel {
            position: relative;

            .search-button {
              position: absolute;
              width: 75px;
              height: 36px;
              border-radius: 20px;
              right: 3px;
              top: 50%;
              transform: translateY(-50%);
              overflow: hidden;

              .btn-search {
                display: block;
                font: 12px/1.5 tahoma, arial, "Hiragino Sans GB", "\5b8b\4f53",
                  sans-serif;
                font-size: 18px;
                font-weight: 700;
                text-align: center;
                color: #fff;
                cursor: pointer;
                height: 36px;
                line-height: 36px;
                border: none;
                width: 100%;
                background-image: linear-gradient(
                  to right,
                  #ff9000 0,
                  #ff5000 100%
                );
              }
            }

            .search-panel-fields {
              width: 630px;
              height: 40px;

              .search-combobox {
                font-size: 12px;

                .search-combobox-input-wrap {
                  margin-left: 75px;
                  border-left: 1px solid #f5f5f5;

                  .search-combobox-input {
                    text-indent: 10px;
                    height: 40px;
                    line-height: 40px;
                    width: 554px;
                    border: none;
                    border-radius: 0 20px 20px 0;
                    outline: 0;
                    background: #fff;
                    color: #000;
                  }
                }
              }
            }
          }
        }

        .search-ft {
          display: none;

          .hotword {
            .search-hots-lines {
              display: flex;
              justify-content: flex-start;

              .hots-item {
                font-size: 12px;
                margin-right: 5px;
                cursor: pointer;
                color: #3c3c3c;
              }

              .hots-item:hover {
                color: #ff5000;
              }
            }
          }
        }
      }
    }
  }
}

.pc-nav {
  border-radius: 20px 20px 0 0;
  width: 1200px;
  padding-top: 6px;
  margin: 0 auto;
  background-color: #fff;

  .nav {
    height: 30px;
    line-height: 30px;
    display: flex;

    h2 {
      float: left;
      width: 190px;
      text-align: center;
      font-size: 16px;
      color: #ff6200;
      margin-right: 52px;
    }

    a {
      color: inherit;
      text-decoration: none;
    }

    a:hover {
      color: #ff6200;
    }

    .nav-hd {
      font-size: 16px;
      font-weight: 700;
      display: flex;

      li {
        margin: 0 3px;
        text-align: center;
        padding: 0 4px;
      }
    }

    .nav-bd {
      font-size: 16px;
      color: #333;
      display: flex;

      li {
        margin-left: 6px;
        text-align: center;
        padding: 0 4px;
      }
    }
  }
}

.screen-outer {
  background-color: #fff;
  width: 1200px;
  margin: 0 auto;
  height: 525px;
  display: flex;
  justify-content: space-between;

  .main {
    width: 770px;

    .main-inner {
      display: flex;

      .category {
        width: 210px;
        font-size: 14px;
        position: relative;

        .category-bd {
          height: 525px;
          width: 100%;
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          letter-spacing: 2px;
          padding: 6px 0 3px 0;

          .cate-menu-item {
            flex: 1;
            font-size: 14px;
            display: flex;
            align-items: center;
            padding-left: 20px;
            line-height: 100%;
            cursor: pointer;
          }

          .cate-menu-item:hover {
            color: #ff5500;
            background-color: #d9d9d9;
          }

          .cate-menu-item-on {
            background-color: #d9d9d9;
          }
        }

        .cate-pop {
          position: absolute;
          z-index: 999;
          top: 0;
          left: 210px;
          width: 990px;
          height: 525px;
          background-color: #fff;
          border: 1px solid #f7f7f7;
          box-shadow: 2px 0 5px rgb(0 0 0 / 30%);

          .cate-part {
            padding: 10px 0;

            a {
              text-decoration: none;
              color: #6c6c6c;
              font-size: 12px;
              letter-spacing: 1px;
            }

            .cate-detail {
              padding: 0 10px;

              a:hover {
                color: #ff5500;
              }

              .cate-detail-item {
                display: flex;

                .cate-detail-tit {
                  width: 90px;
                  text-align: right;
                  font-weight: 700;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                }

                .cate-detail-con {
                  flex: 1;

                  .cate-detail-con-lk {
                    margin: 3px 0;
                    padding: 0 7px;
                    height: 16px;
                    line-height: 16px;
                    white-space: nowrap;
                  }
                }
              }
            }
          }
        }
      }

      .core {
        flex: 1;
        height: 515px;
        margin-top: 10px;
        margin-left: 20px;

        .promo {
          position: relative;
          width: 540px;
          height: 280px;
          border-radius: 12px;
          overflow: hidden;

          .promo-bd {
            position: relative;
            border-radius: 12px;
            height: 280px;

            .el-carousel {
              /deep/ .el-carousel__container {
                height: 280px;
              }

              .el-carousel__indicators {
                .is-active {
                  /deep/ .el-carousel__button {
                    background-color: #ff5500;
                  }
                }
              }
            }

            img {
              width: 540px;
              height: 280px;
            }
          }
        }

        .tmall {
          width: 540px;
          height: 220px;
          margin-top: 15px;

          .tmall-bd {
            background-color: #f6f6f6;
            border-radius: 12px;

            .el-carousel {
              /deep/ .el-carousel__container {
                width: 520px;
                height: 200px;
                margin: 10px;

                .el-carousel__item {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;

                  a {
                    display: inline-block;

                    img {
                      height: 200px;
                      width: 255px;
                      border-radius: 12px;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  .col-right {
    flex: 1;
    height: 515px;
    border-radius: 12px;
    margin: 10px 20px 0 20px;
    margin-right: 18px;
    margin-top: 10px;
    background-color: #f3f3f3;

    .member {
      background-color: #f3f3f3;

      .member-bd {
        position: relative;

        a {
          color: #3c3c3c;
        }

        .avatar-wrapper {
          display: block;
          margin: 20px auto 0;
          width: 50px;
          height: 50px;
          border-radius: 50%;
          background-clip: padding-box;
          background-color: #ff6f06;

          .member-home {
            width: 50px;
            height: 50px;
            line-height: 50px;
            overflow: hidden;

            .member-avatar {
              width: 50px;
              height: 50px;
              border-radius: 50%;
              background-clip: padding-box;
            }
          }
        }

        .member-nickurl {
          .member-nick-info {
            font-size: 14px;
            margin-top: 4px;
            display: block;
            line-height: 17px;
            height: 17px;
            text-align: center;
          }

          .member-nick-info:hover {
            color: #f40;
          }
        }
      }

      .member-ft {
        height: 42px;
        overflow: hidden;
        margin-top: 11px;
        padding: 0 14px;

        .member-logout {
          display: flex;
          justify-content: space-between;
          align-items: flex-end;

          .member-logout-op {
            display: block;
            width: 115px;
            height: 32px;
            line-height: 32px;
            border-radius: 5px;
            font-size: 16px;
            background-image: linear-gradient(
              to right,
              #ff5000 0,
              #ff6f06 100%
            );
            background-repeat: repeat-x;
            text-align: center;
            color: #fff;
          }
        }

        .member-login {
          .order-status-info {
            display: flex;
            flex-direction: row;
            justify-content: space-around;
            align-items: center;

            a {
              width: 66px;
              text-align: center;
              color: #333;

              strong {
                display: block;
                font-size: 14px;
                color: #f40;
              }
            }

            a:hover {
              color: #f40;
            }
          }
        }
      }
    }

    .tip {
      margin: 0 auto;
      margin-top: 16px;
      width: 356px;
      height: 141px;
      border-radius: 6px;
      overflow: hidden;
      position: relative;

      .tip-mod {
        a {
          display: block;

          img {
            width: 356px;
            height: 141px;
          }
        }
      }
    }

    .notice {
      background-color: #f3f3f3;
      margin-left: 14px;

      .notice-top {
        margin: 17px 0 14px;
        height: 16px;
        line-height: 16px;

        .notice-title {
          height: 16px;
          font-size: 12px;
          line-height: 16px;
          font-weight: 700;
        }

        .notice-desc {
          margin-left: 6px;
          height: 16px;
          font-size: 12px;
          line-height: 16px;
        }
      }

      .notice-content {
        margin-bottom: 8px;
        line-height: 18px;
        height: 18px;
        position: relative;
        overflow: hidden;

        .notice-tip {
          height: 18px;
          width: 32px;
          line-height: 18px;
          border-radius: 2px;
          background-color: rgba(255, 174, 174, 0.2);
          opacity: 0.8;
          text-align: center;
          font-size: 12px;
          color: #ff5000;
          font-weight: 700;
          float: left;
        }

        p {
          font-size: 12px;
          color: #666;
          margin-left: 8px;
          float: left;
          width: 295px;
          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }
      }
    }

    .mypao {
      position: relative;
      margin: 20px 14px 0 14px;
      overflow: hidden;
      display: flex;
      justify-content: space-around;

      .mypao-content {
        a {
          color: #333;
          display: block;
        }

        p {
          text-align: center;
        }

        .iconfont {
          display: block;
          width: 60px;
          font-size: 24px;
          text-align: center;
        }
      }

      .mypao-content:hover .iconfont {
        color: #f40;
      }
    }
  }
}

/* 秒杀 */
.seckill-container {
  width: 1200px;
  height: 300px;
  margin: 0 auto;
  padding-top: 20px;
  background-color: #fff;

  .seckill-inner {
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;

    .seckill-countdown {
      background-image: linear-gradient(to bottom, #ff9000 0, #ff5000 100%);
      display: block;
      width: 190px;
      height: 100%;
      color: #fff;

      .countdown-title {
        width: 100%;
        text-align: center;
        font-size: 30px;
        font-weight: 700;
        margin-top: 31px;
      }

      .countdown-icon {
        width: 100%;
        height: 80px;
        line-height: 80px;
        text-align: center;

        .icon-shandian {
          font-size: 40px;
        }
      }

      .countdown-desc {
        margin-top: 10px;
        font-size: 14px;
        text-align: center;

        strong {
          font-size: 18px;
          padding-right: 2px;
          vertical-align: middle;
          display: inline-block;
          margin-top: -1px;
        }
      }

      .timmer {
        .timmer-unit {
          position: relative;
          float: left;
          width: 30px;
          height: 30px;
          text-align: center;
          background-color: #2f3430;
          margin-right: 20px;
          color: white;
          font-size: 20px;
        }

        .timmer-unit::after {
          content: ":";
          display: block;
          position: absolute;
          right: -20px;
          font-weight: bolder;
          font-size: 18px;
          width: 20px;
          height: 100%;
          top: 0;
        }

        .timmer-unit:last-child::after {
          content: "";
        }

        .timmer-unit-hour {
        }

        .timmer-unit-minute {
        }

        .timmer-unit-second {
          margin-right: 0;
          content: "";
        }
      }

      .countdown-main {
        margin-left: auto;
        margin-right: auto;
        width: 130px;
        height: 30px;
        margin-top: 10px;
        display: block;
      }
    }

    .seckill-list {
      width: 800px;
      height: 280px;
      background-color: #fff;

      .slider-wrapper {
        width: 800px;
        height: 100%;

        .el-carousel__item {
          display: flex;

          a {
            display: block;
            width: 200px;
            height: 280px;
            padding: 4px;
            background: linear-gradient(
              180deg,
              rgba(240, 244, 248, 0.5),
              rgba(255, 255, 255, 0.5)
            );

            .seckill-item {
              margin: 0 4px;
              background-color: #fff;

              .seckill-item-image {
                background-color: #fff;
                width: 184px;
                padding: 7px;

                img {
                  width: 170px;
                  height: 170px;
                  border-radius: 5px;
                }
              }

              .seckill-item-name {
                width: 180px;
                height: 35px;
                line-height: 35px;
                padding: 0 10px;
                text-align: center;
                font-size: 12px;
                font-weight: 400;
                color: #333;
                transition: color 0.2s ease;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                background-color: #fff;
              }

              .seckill-item-price {
                width: 180px;
                height: 45px;
                text-align: center;
                padding: 0 10px;
                background-color: #fff;

                .price-origin {
                  height: 100%;
                  width: 66px;
                  background: #fff;
                  color: #e1251b;
                  font-size: 14px;
                  font-weight: 700;
                  line-height: 22px;
                  box-sizing: border-box;
                  vertical-align: top;
                  vertical-align: bottom;
                  text-align: center;

                  .icon-money {
                    font-size: 12px;
                  }
                }
              }
            }
          }
        }
      }
    }

    .seckill-brand {
      flex: 1;
      height: 280px;
      padding: 10px;

      .seckill-brand-slider {
        .item-image {
          width: 120px;
          height: 120px;
          margin-left: auto;
          margin-right: auto;
          margin-top: 20px;

          img {
            width: 100%;
            height: 100%;
          }
        }

        .item-info {
          background: linear-gradient(
            180deg,
            rgba(255, 255, 255, 0.5),
            rgba(220, 224, 236, 0.5)
          );
          text-align: center;
          font-size: 14px;
          position: relative;
          width: 100%;
          height: 110px;
          margin-top: 10px;
          border-radius: 10px;

          .item-info-title {
            color: #666666;
          }

          .item-info-promo {
            color: #333;
            font-weight: 700;
          }

          .item-info-action {
            color: #f40;
            border-radius: 14px;
            width: 82px;
            height: 24px;
            box-sizing: border-box;
            text-align: center;
            display: inline-block;
            line-height: 22px;
            font-weight: 700;
            padding-left: 4px;
            font-size: 12px;
            border: 1px solid #f40;
            margin-top: 4px;
          }

          .item-info-action:hover {
            background-color: #f40;
            color: #fff;
          }
        }
      }
    }
  }
}

.hotsale-container {
  width: 1200px;
  margin: 0 auto;
  background-color: #fff;

  .hotsale-hd {
    display: block;
    height: 48px;
    position: relative;
    padding-left: 20px;
    line-height: 48px;
    font-size: 20px;

    .hotsale-title {
      font-size: 24px;
      color: #111;
      font-weight: bold;
      line-height: 24px;
    }

    .hotsale-icon {
      background-image: linear-gradient(to bottom, #ff9000 0, #ff5000 100%);
      width: 70px;
      height: 20px;
      line-height: 20px;
      text-align: center;
      display: inline-block;
      font-size: 13px;
      color: #fff;
      border-radius: 5px;
    }
  }

  .list {
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    min-height: 652px;
    padding-bottom: 10px;
    ul {
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
    }
    .item {
      border-radius: 12px;
      padding: 7px 7px 0 7px;
      margin: 0 0 2px 7px;
      color: #6c6c6c;
      width: 230px;
      height: 325px;
      position: relative;

      .hotsale-item {
        display: block;

        .img-wrapper {
          width: 215px;
          height: 215px;
          border-radius: 12px;
          background-color: rgba(0, 0, 0, 0.2);
          overflow: hidden;

          img {
            display: block;
            border-radius: 12px;
            width: 100%;
            height: 100%;
            transition: opacity 0.2s;
            background: #fff;
            opacity: 0.9;
          }
        }

        h4 {
          width: 216px;
          margin-top: 9px;
          line-height: 22px;
          height: 44px;
          font-size: 16px;
          color: #111111;
          transition: color 0.3s;
          font-weight: normal;
          overflow: hidden;
        }

        .info {
          margin-top: 11px;
          height: 22px;
          line-height: 22px;
          vertical-align: bottom;
          border-radius: 0 0 12px 12px;

          .price {
            float: left;
            color: #f40;
            font-size: 20px;
            height: 22px;
            line-height: 22px;

            .icon-money {
              font-size: 14px;
            }
          }
        }
      }
    }

    .item:hover {
      transition: all 0.5s;
      box-shadow: 0 19px 39px 0 rgb(0 0 0 / 12%);
    }

    .item:hover h4 {
      color: #ff5500;
    }
  }

  .hotsale-ft {
    height: 80px;
    background: #fff;
    position: relative;
    text-align: center;

    .hotsale-end {
      position: absolute;
      text-align: center;
      color: #ccccd0;
      left: 50%;
      width: 84px;
      line-height: 40px;
      font-size: 18px;
      margin-left: -42px;
      font-style: normal;
      top: 20px;
    }

    .hotsale-end::before {
      left: -70px;
    }

    .hotsale-end::after {
      right: -70px;
    }

    .hotsale-end::before,
    .hotsale-end::after {
      content: " ";
      position: absolute;
      height: 0;
      line-height: 0;
      border-top: 1px solid #e6e6e6;
      width: 60px;
      top: 50%;
    }

    .el-icon-loading {
      font-size: 30px;
      color: #ccccd0;
    }
  }
}

.fixedtool {
  right: 60px;

  .fixedtool-list {
    .fixedtool-item {
      width: 50px;
      height: 50px;
      padding: 10px;
      font-size: 12px;
      text-align: center;
      border-radius: 50%;
      background-color: #ffffff;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 10px;
      cursor: pointer;
    }

    .goods {
      color: #ff4400;
    }

    .hotsale {
      color: #ff4400;
    }

    .fixedtool-item:hover {
      background-image: linear-gradient(135deg, #ff971b, #ff5000);
      color: #ffffff;
    }

    .on {
      background-image: linear-gradient(135deg, #ff971b, #ff5000);
      color: #ffffff;
    }

    .el-backtop {
      width: 50px;
      height: 50px;
      background-color: #ffffff;
      border-radius: 50%;
      font-size: 20px;
    }
  }
}
</style>