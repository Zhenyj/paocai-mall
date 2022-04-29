<template>
  <div
    id="member-menu"
    :style="{'background-color':bgc}"
  >
    <dl
      class="menu-group"
      v-for="(v1,i1) in menus"
      :key="i1"
    >
      <dt class="tit">{{v1.title}}</dt>
      <dd
        class="menu-item"
        :class="{'active':i1===defaultActive[0] && i2 === defaultActive[1]}"
        v-for="(v2,i2) in v1.items"
        :key="i2"
        @click="navToByRouterName(v2.routerName)"
      >{{v2.name}}</dd>
    </dl>
  </div>
</template>

<script>
export default {
  name: 'memberSetMenu',
  props: {
    bgc: {
      type: String,
      default: '#ffffff'
    },
    defaultActive: {
      type: Array,
      default: [0, 0]
    }
  },
  data () {
    return {
      menus: [{
        title: '设置',
        items: [{
          name: '个人信息',
          routerName: ''
        }, {
          name: '账户安全',
          routerName: ''
        }, {
          name: '收货地址',
          routerName: 'myPaocai'
        }, {
          name: '我的银行卡',
          routerName: ''
        }, {
          name: '账号绑定',
          routerName: ''
        }]
      }]
    }
  },
  methods: {
    navToByRouterName (routerName) {
      this.$router.push({ name: routerName });
    }
  },
  created () {
  }
}
</script>

<style lang="less" scoped>
.menu-group {
  padding: 15px 20px;
  border-bottom: 1px solid #f3f3f3;
  .tit {
    font-size: 16px;
    font-weight: 700;
    line-height: 32px;
  }
  .menu-item {
    font-size: 14px;
    line-height: 32px;
    cursor: pointer;
  }
  .menu-item:hover {
    color: #ff0036;
  }
  .active {
    color: #ff0036;
  }
}
</style>