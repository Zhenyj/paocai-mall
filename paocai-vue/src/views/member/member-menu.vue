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
        v-for="(v2,i2) in v1.items"
        :key="i2"
        @click="navToByRouterName(v2.routerName)"
      >{{v2.name}}</dd>
    </dl>
  </div>
</template>

<script>
export default {
  name: 'memberMenu',
  props: {
    bgc: {
      type: String,
      default: '#ffffff'
    }
  },
  data () {
    return {
      menus: [{
        title: '订单中心',
        items: [{
          name: '我的订单',
          routerName: ''
        }]
      }, {
        title: '我的钱包',
        items: [{
          name: '优惠券',
          routerName: ''
        }, {
          name: '红包',
          routerName: ''
        }, {
          name: '银行卡',
          routerName: ''
        }, {
          name: '余额',
          routerName: ''
        }]
      }, {
        title: '我的关注',
        items: [{
          name: '关注的店铺',
          routerName: ''
        }, {
          name: '关注的商品',
          routerName: ''
        }]
      }, {
        title: '客户服务',
        items: [{
          name: '返修退换货',
          routerName: ''
        }, {
          name: '价格保护',
          routerName: ''
        }, {
          name: '我的发票',
          routerName: ''
        }, {
          name: '购买咨询',
          routerName: ''
        }, {
          name: '泡菜维修',
          routerName: ''
        }, {
          name: '上门服务预约',
          routerName: ''
        }, {
          name: '举报中心',
          routerName: ''
        }, {
          name: '意见建议',
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
}
</style>