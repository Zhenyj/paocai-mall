<template>
  <div id="address-manage-page">
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
export default {
  name: 'addressManage',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
    }
  },
  methods: {
    handleNavTo (routerName) {
      this.$router.push({ name: routerName });
    }
  },
  created () {
    document.title = '收货地址管理-泡菜商城';
  }
}
</script>

<style lang="less" scoped>
</style>