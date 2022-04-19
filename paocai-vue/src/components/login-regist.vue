<template>
  <div>
    <!-- 头部 -->
    <div class="header">
      <div class="logo">
        <div class="header-left">
          <a href=""><img
              src="../assets/logo.png"
              alt="泡菜买菜"
            /></a>
          <b>{{ title }}</b>
        </div>
        <div class="header-right"></div>
      </div>
    </div>
    <!-- 表单部分 -->
    <div class="login-content">
      <div class="content-bg">
        <div class="login-box">
          <div class="login-box-warp">
            <div class="login-container">
              <!-- 表单插槽 -->
              <slot></slot>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 底部 -->
    <div class="login-footer"></div>
  </div>
</template>

<script>
export default {
  name: 'LogReg',
  props: ['title'],
  data () {
    return {
      loginType: 0,
      loginId: '',
      password: '',
      mobile: '',
      code: ''
    };
  },
  methods: {
    onSubmit () { }
  }
};
</script>

<style lang="less" scoped>
.header {
  padding: 22px 0;
  height: 95px;
  .logo {
    .header-left {
      width: 1200px;
      margin: auto;
      overflow: hidden;
      a {
        display: inline-block;
      }
      img {
        height: 55px;
        width: auto;
        margin-right: 20px;
      }
      b {
        font-size: 25px;
      }
    }
  }
}
.login-content {
  width: 100%;
  position: relative;
  .content-bg {
    background-image: url("../assets/images/login_bg.png");
    width: 100%;
    height: 600px;
    z-index: 9;
    position: absolute;
    top: 0;
    left: 0;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: 50%;
    .login-box {
      width: 1200px;
      height: 600px;
      position: relative;
      overflow: hidden;
      z-index: 99;
      margin: auto;
      .login-box-warp {
        position: absolute;
        top: 50%;
        right: 60px;
        transform: translateY(-50%);
        width: 350px;
        .login-container {
          background-color: #fff;
          width: 350px;
          color: #6c6c6c;
          margin: 0 auto;
          padding: 25px 25px 23px;
          .login-switch-tab {
            display: flex;
            flex-direction: row;
            margin: 4px 8px 20px 8px;
            .switch-tab-item {
              font-size: 16px;
              color: #3c3c3c;
              height: 18px;
              line-height: 5px;
              margin: 9px 10px 0 0;
              font-weight: 700;
              cursor: pointer;
            }
            .check {
              border-bottom: 2px solid #000;
            }
          }
        }
      }
    }
  }
}
.login-footer {
}
</style>
