<template>
  <div id="my-paocai-page">
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <!-- 头部 -->
    <member-header @navToByRouterName="handleNavTo($event)"></member-header>
    <div class="content">
      <!-- 内容 -->
      <div class="left-menu">
        <!-- 侧边菜单 -->
        <member-menu></member-menu>
      </div>
      <div class="main">
        <div class="box-flex box-0">
          <div class="m m-user">
            <div class="mc">
              <span class="rank">银牌</span>
              <a
                href="/member/user/info"
                target="_blank"
                class="avatar"
              >
                <el-avatar
                  :size="64"
                  src="https://empty"
                >
                  <img
                    src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                  />
                </el-avatar>
              </a>
              <div class="user">
                <a
                  href="/member/user/info"
                  class="user-name"
                  target="_blank"
                >甄英俊</a>
              </div>
              <div class="score">
                <a
                  class="score-vip"
                  href=""
                  target="_blank"
                >京享值 2743</a>
              </div>
            </div>
            <div class="mb">
              <div class="text"><em>开通PLUS会员尊享</em><span>全年360元运费券礼包</span>
              </div><a class="button">立即开通</a>
            </div>
          </div>
          <div class="m m-block m-wallet">
            <div class="mt">
              <h3>我的钱包</h3>
              <button class="icon-sprite icon-wallet-show">
                <i
                  class="iconfont"
                  :class="[true?'icon-eye-open':'icon-eye-closed']"
                  title="隐藏金额"
                ></i>
              </button>
            </div>
            <div class="mc">
              <ul>
                <li>
                  <div class="num">
                    <a
                      href="/"
                      target="_blank"
                    >0</a>
                  </div>
                  <div class="name">优惠券</div>
                  <div class="extra"></div>
                  <div class="operate">
                    <a
                      href="/"
                      target="_blank"
                    >领券</a>
                  </div>
                </li>
                <li>
                  <div class="num">
                    <a
                      href="/"
                      target="_blank"
                    >0</a>
                  </div>
                  <div class="name">积分</div>
                  <div class="extra"></div>
                  <div class="operate">
                    <a
                      href="/search"
                      target="_blank"
                    >赚积分</a>
                  </div>
                </li>
                <li>
                  <div class="num">
                    <a
                      href="/"
                      target="_blank"
                    >{{0|showPrice}}</a>
                  </div>
                  <div class="name">余额</div>
                  <div class="extra"></div>
                  <div class="operate">
                    <a
                      href="/"
                      target="_blank"
                    >充值</a>
                  </div>
                </li>
                <li>
                  <div class="num">
                    <a
                      href="/"
                      target="_blank"
                    >{{0|showPrice}}</a>
                  </div>
                  <div class="name">泡呗本月待还</div>
                  <div class="extra"></div>
                  <div class="operate">
                    <a
                      href="/"
                      target="_blank"
                    >查看</a>
                  </div>
                </li>
                <li>
                  <div class="num">
                    <a
                      href="/"
                      target="_blank"
                    >{{0|showPrice}}</a>
                  </div>
                  <div class="name">小金库昨日收益</div>
                  <div class="extra"></div>
                  <div class="operate">
                    <a
                      href="/"
                      target="_blank"
                    >转入小金库，马上赚钱</a>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="box-flex flex-between">
          <div class="box-1">
            <div class="m m-block m-order">
              <div class="mt">
                <h3>我的订单</h3>
              </div>
              <div class="mc">
                <div class="nav box-flex">
                  <a
                    href="/order/list"
                    target="_blank"
                    class="item"
                  >
                    <div class="iconfont icon-daifukuan"></div>
                    <span class="name">待付款</span>
                  </a>
                  <a
                    href="/order/list"
                    target="_blank"
                    class="item"
                  >
                    <div class="iconfont icon-daishouhuo"></div>
                    <span class="name">待收货</span>
                  </a>
                  <a
                    href="/order/list"
                    target="_blank"
                    class="item"
                  >
                    <div class="iconfont icon-daipingjia"></div>
                    <span class="name">待评价</span>
                  </a>
                  <a
                    href="/order/list"
                    target="_blank"
                    class="item"
                  >
                    <div class="iconfont icon-shouhou"></div>
                    <span class="name">退换/售后</span>
                  </a>
                  <a
                    href="/order/list"
                    target="_blank"
                    class="item"
                  >
                    <div class="iconfont icon-dingdan"></div>
                    <span class="name">全部订单</span>
                  </a>
                </div>
                <div class="empty">
                  <el-empty
                    image="https://gtd.alicdn.com/tps/i3/T1TvXUXnNjXXXXXXXX-100-100.png"
                    :image-size="150"
                    description="您买的东西太少了，这里都空空的，快去挑选合适的商品吧！"
                  ></el-empty>
                </div>

              </div>
            </div>
          </div>
          <div class="box-2">
            <div class="m m-block m-follow">
              <div class="mt">
                <h3>我的关注</h3>
              </div>
              <div class="mc flex-around">
                <a
                  href="/member/follow/prdocut"
                  target="_blank"
                >
                  <div class="number">4</div>
                  <div class="text">商品关注</div>
                </a>
                <a
                  href="/member/follow/shop"
                  target="_blank"
                >
                  <div class="number">4</div>
                  <div class="text">店铺关注</div>
                </a>
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
import MemberMenu from '@/components/member/member-menu'
import MemberHeader from '@/components/member/member-header'
export default {
  name: 'myPaocai',
  components: { CommonHeader, CommonFooter, MemberMenu, MemberHeader },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      isCollapse: false
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
    }
  },
  created () {
    document.title = '我的泡菜-泡菜商城';
    this.getLoginInfo();
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
#my-paocai-page {
  background-color: #f0f3ef;
  padding-bottom: 50px;
}
.content {
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
  margin-left: 15px;
  .box-flex {
    display: flex;
  }
  .box-0 {
    padding: 20px 0;
    height: 283px;
    display: flex;
    font: 12px/150% tahoma, arial, Microsoft YaHei, Hiragino Sans GB,
      "\u5b8b\u4f53", sans-serif;
  }
  .m-user {
    width: 250px;
    height: 243px;
    box-shadow: 2px 2px 10px rgb(0 0 0 / 30%);
    background-color: #e2231a;
    .mc {
      position: relative;
      height: 167px;
      text-align: center;
      background-color: #e2231a;
      .rank {
        position: absolute;
        top: 55px;
        left: 50%;
        transform: translateX(-50%);
        z-index: 3;
        width: 40px;
        height: 18px;
        line-height: 18px;
        text-align: center;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 2px 4px 4px 0 rgb(41 41 41 / 50%);
        color: #343434;
      }
      .avatar {
        height: 72px;
        width: 72px;
        margin: 20px auto;
        display: block;
        border-radius: 100%;
        border: 4px solid #fff;
      }
      .user {
        display: inline-block;
        margin-bottom: 10px;
        height: 20px;
        overflow: hidden;
        .user-name {
          font: 700 18px/20px Arial;
          color: #fff;
        }
      }
      .score {
        a {
          padding: 0 10px;
          border: 1px solid #fff;
          color: #fff;
          display: inline-block;
          margin: 0 5px;
          height: 22px;
          line-height: 22px;
          border-radius: 12px;
        }
        a:hover {
          background-color: #fff;
          color: #c81623;
        }
      }
    }
    .mb {
      padding: 10px 20px;
      height: 56px;
      background-color: #232331;
      overflow: hidden;
      zoom: 1;
      .text {
        float: left;
        margin-right: 10px;
        color: #f4ca3a;
        em {
          font-size: 13px;
          font-weight: 700;
          display: block;
          font-style: normal;
        }
        span {
          display: block;
        }
      }
      .button {
        float: right;
        padding: 0 10px;
        margin-top: 5px;
        height: 26px;
        line-height: 26px;
        border-radius: 14px;
        background-color: #f4ca3a;
        color: #232331;
        font-weight: 700;
        text-align: center;
      }
    }
  }
  .m-block {
    margin-bottom: 20px;
    background-color: #fff;
    .mt {
      padding: 0 20px;
      height: 55px;
      line-height: 55px;
      border-bottom: 1px solid #f0f3ef;
      overflow: visible;
      h3 {
        font-size: 16px;
        color: #333;
        float: left;
        font-weight: 400;
      }
    }
  }

  .m-wallet {
    flex: 1;
    margin: 9px 0;
    overflow: visible;
    background-color: #fff;
    zoom: 1;
    .mypc-popup {
      display: none;
      position: absolute;
      z-index: 11;
    }
    button {
      background-color: transparent;
      border: none;
      cursor: pointer;
      float: right;
      .iconfont {
        display: block;
        font-size: 22px;
        margin-top: 10px;
      }
    }
    .icon-wallet-show {
      background-position: -200px -80px;
    }
    .mc {
      overflow: hidden;
      zoom: 1;
      ul {
        display: flex;
        justify-content: space-between;
        padding: 20px;
        height: 169px;
        overflow: hidden;
        background-color: #fff;
      }
      li {
        color: #333;
      }
      a:hover {
        color: #c81623;
      }
      .num,
      .extra {
        margin-bottom: 10px;
      }
      .num {
        box-sizing: content-box;
        padding-top: 10px;
        height: 24px;
        line-height: 24px;
        font-size: 22px;
        font-weight: 700;
      }
      .name,
      .extra,
      .operate {
        height: 20px;
        line-height: 20px;
        overflow: hidden;
      }
      .operate {
        a {
          color: #4184e8;
        }
      }
    }
  }
  .box-1 {
    width: 694px;
  }
  .m-order {
    .mc {
      overflow: visible;
    }
    .nav {
      padding: 15px 20px;
      display: flex;
      justify-content: space-around;
      align-items: center;
      .item {
        box-sizing: border-box;
        padding: 13px 0 0;
        width: 110px;
        height: 90px;
        text-align: center;
        color: #333;
        transition: all 0.4s ease;
        .iconfont {
          color: #333;
          margin: 5px auto;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 35px;
          font-size: 40px;
        }
        .iconfont::before {
          line-height: 45px;
        }
      }
      .item:hover {
        border-radius: 5px;
        box-shadow: 0 0 16px 0 rgb(0 0 0 / 10%);
      }
      .item:hover .iconfont {
        color: #ff0036;
      }
      .name {
        box-sizing: content-box;
        padding-top: 10px;
        height: 16px;
        line-height: 16px;
        position: relative;
        display: block;
      }
    }
  }
  .box-2 {
    width: 290px;
  }
  .m-follow {
    .mc {
      padding: 20px;
      height: 100px;
      a {
        display: block;
        height: 60px;
        line-height: 30px;
        font-size: 16px;
        color: #333;
        text-align: center;
      }
      a:hover .number {
        color: #ff0036;
      }
      .text {
        color: #666666;
        font-size: 12px;
      }
    }
  }
}
</style>