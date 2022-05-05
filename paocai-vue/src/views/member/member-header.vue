<template>
  <div id="member-header">
    <div class="header-wrapper">
      <div class="header">
        <h1 class="logo">
          <a
            href="/"
            target="_blank"
            title="泡菜商城"
          ><img src="../../assets/logo.png"></a>

        </h1>
      </div>
      <div class="title">我的泡菜</div>
      <div class="nav-tab">
        <el-dropdown
          :show-timeout="150"
          :hide-timeout="150"
          v-for="(v1,i1) in menus"
          :key="i1"
        >
          <span
            class="el-dropdown-link"
            :class="{'active':i1==defaultActive}"
            @click="navToByRouterName(v1.routerName)"
          >
            {{v1.title}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu
            slot="dropdown"
            class="tab-list"
          >
            <el-dropdown-item
              v-for="(v2,i2) in v1.items"
              :key="i2"
              @click.native="navToByRouterName(v2.routerName)"
            >{{v2.name}}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'memberHeader',
  props: {
    defaultActive: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      menus: [{
        title: '首页',
        routerName: 'myPaocai',
        items: [{
          name: '订单中心',
          routerName: ''
        }, {
          name: '我的钱包',
          routerName: ''
        }, {
          name: '我的关注',
          routerName: ''
        }, {
          name: '客户服务',
          routerName: ''
        }]
      }, {
        title: '账户设置',
        routerName: 'userInfo',
        items: [{
          name: '个人信息',
          routerName: 'userInfo'
        }, {
          name: '账户安全',
          routerName: ''
        }, {
          name: '收货地址',
          routerName: 'address'
        }, {
          name: '我的银行卡',
          routerName: ''
        }, {
          name: '账号绑定',
          routerName: ''
        }]
      }],
    }
  },
  methods: {
    navToByRouterName (routerName) {
      if (routerName.trim() !== '') {
        this.$emit('navToByRouterName', routerName);
      }
    }
  },
  mounted () {

  }
}
</script>

<style lang="less" scoped>
#member-header {
  background-color: #fff;
}
.header-wrapper {
  background-color: #fff;
  margin-bottom: 24px;
  display: flex;
  width: 1200px;
  margin: 0 auto;
  .header {
    position: relative;
    height: 64px;
    margin-top: 20px;
    margin-bottom: 15px;
    .logo {
      display: inline-block;
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
  .title {
    font: 12px/150% tahoma, arial, Microsoft YaHei, Hiragino Sans GB,
      "\u5b8b\u4f53", sans-serif;
    -webkit-font-smoothing: antialiased;
    font-size: 22px;
    padding: 50px 0 0 25px;
  }
}
.nav-tab {
  width: 250px;
  margin-left: 100px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  .el-dropdown-link {
    cursor: pointer;
    font-size: 18px;
  }
  .el-dropdown-link:hover {
    color: #ff0036;
  }
  .el-dropdown-link.active {
    color: #ff0036;
  }
}
</style>
<style>
.tab-list .el-dropdown-menu__item:focus,
.tab-list .el-dropdown-menu__item:not(.is-disabled):hover {
  color: #ff0036;
  background-color: #fff;
}
</style>