<template>
  <div class="common-footer">
    <div class="footer-content">
      <i class="content">泡菜商城</i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommonFooter',
  data () {
    return {}
  }
}
</script>

<style lang="less" scoped>
.common-footer {
  background-color: #ffffff;
  width: 100%;
  margin-top: 30px;
}
.footer-content {
  height: 80px;
  background: #fff;
  position: relative;
  text-align: center;

  .content {
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

  .content::before {
    left: -70px;
  }

  .content::after {
    right: -70px;
  }

  .content::before,
  .content::after {
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
</style>
