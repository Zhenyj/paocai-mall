<template>
  <div>
    <common-header></common-header>
    <div class="product-content">

    </div>
    <common-footer></common-footer>
  </div>

</template>

<script>
  import CommonHeader from '@/components/common/header.vue'
  import CommonFooter from '@/components/common/footer.vue'
  export default {
    name: 'product',
    components: { CommonHeader, CommonFooter },
    data () {
      return {

      }
    },
    created() {
      
    }
  }
</script>

<style lang="less" scoped>

</style>
