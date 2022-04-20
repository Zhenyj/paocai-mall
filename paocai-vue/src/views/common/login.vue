<template>
  <log-reg :title="title">
    <div class="login-slot">
      <!-- 切换登录方式 -->
      <div class="login-switch-tab">
        <div
          class="switch-tab-item"
          :class="{ check: loginType === 0 }"
          @click="loginType = 0"
        >密码登录</div>
        <div
          class="switch-tab-item"
          :class="{ check: loginType === 1 }"
          @click="loginType = 1"
        >短信登录</div>
      </div>
      <!-- 密码登录 -->
      <div
        class="login-form-wrap"
        v-show="loginType === 0"
      >
        <el-form
          class="login-form"
          status-icon
          :model="loginForm"
          ref="loginForm"
          :rules="rules"
        >
          <el-form-item prop="loginAccount">
            <div class="fm-field">
              <div class="fm-field-input">
                <div class="fm-label-icon"><i
                    class="iconfont icon-member"
                    title="用户名"
                  ></i></div>
                <el-input
                  v-model="loginForm.loginAccount"
                  placeholder="会员名/邮箱/手机号"
                  size="large"
                  style="width:100%;"
                >
                </el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="fm-field">
              <div class="fm-label-icon"><i
                  class="iconfont icon-password"
                  title="密码"
                ></i></div>
              <div class="fm-field-input">
                <el-input
                  v-model="loginForm.password"
                  placeholder="请输入登录密码"
                  size="large"
                  type="password"
                ></el-input>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <!-- 短信登录 -->
      <div
        class="login-form-wrap"
        v-show="loginType === 1"
      >
        <el-form
          class="login-form"
          status-icon
          :model="mobileLoginForm"
          ref="mobileLoginForm"
          :rules="mobileRules"
        >
          <el-form-item prop="mobile">
            <div class="fm-field">
              <div class="fm-label-icon"><i
                  class="iconfont icon-member"
                  title="手机号"
                ></i></div>
              <div class="fm-field-input">
                <el-input
                  v-model="mobileLoginForm.mobile"
                  placeholder="请输入手机号"
                  size="large"
                  style="width:100%;"
                  :maxlength="11"
                ></el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="code">
            <div class="fm-field">
              <div class="fm-label-icon"><i
                  class="iconfont icon-phone"
                  title="验证码"
                ></i></div>
              <div class="fm-field-input">
                <el-input
                  v-model="mobileLoginForm.code"
                  placeholder="请输入验证码"
                  size="large"
                  :maxlength="6"
                >
                  <template #suffix>
                    <div class="fm-field-right"><span
                        @click="getSmsCode">获取验证码</span></div>
                  </template>
                </el-input>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div
        class="submit"
        @click="login"
      >登录</div>
      <div class="sns-links">
        <a href="#">
          <i
            class="iconfont icon-qq-login"
            style="color: #30a5dd;"
          ></i>
          QQ
        </a>
        <a href="#">
          <i
            class="iconfont icon-wx-login"
            style="color: #28c445;"
          ></i>
          微信
        </a>
        <a href="#">
          <i
            class="iconfont icon-zfb-login"
            style="color: #5a9ef7;"
          ></i>
          支付宝
        </a>
        <a href="#">
          <i
            class="iconfont icon-gitee-login"
            style="color: #bb2124;"
          ></i>
          Gitee
        </a>
      </div>
      <div class="links">
        <a
          href="#"
          class="links-op"
          v-show="loginType === 0"
        >忘记密码</a>
        <a
          href="#"
          class="links-op"
          v-show="loginType === 0"
        >忘记用户名</a>
        <router-link
          to="/regist"
          class="links-op"
        >立即注册</router-link>
        <router-view></router-view>
      </div>
    </div>
  </log-reg>
</template>

<script>
import LogReg from '@/components/login-regist.vue'
export default {
  name: 'login',
  components: { LogReg },
  data () {
    // 校验手机号
    let validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('必须填写手机号'));
      } else if (!/^1[0-9]{10}$/.test(value)) {
        callback(new Error('手机号格式不正确'));
      } else {
        callback();
      }
    };
    return {
      title: '欢迎登录',
      loginType: 0,
      loginForm: {
        loginAccount: '',
        password: ''
      },
      mobileLoginForm: {
        mobile: '',
        code: ''
      },
      rules: {
        loginAccount: [
          { required: true, message: '必须填写用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '必须填写密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度在 6 到 18 个字符', trigger: 'blur' }
        ]
      },
      mobileRules: {
        mobile: [
          { required: true, message: '必须填写手机号', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请填写验证码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login () {
      let formName = 'loginForm';
      if (this.loginType === 1) {
        formName = 'mobileLoginForm';
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.doLogin();
        } else {
          return false;
        }
      });
    },
    async doLogin () {
      let res;
      if (this.loginType === 0) {
        res = await this.$request({
          url: 'auth/login',
          method: 'POST',
          data: this.loginForm
        });
      } else {
        res = await this.$request({
          url: 'auth/mobile_login',
          method: 'POST',
          data: this.mobileLoginForm
        });
      }
      if (res.code !== 200) {
        this.$message({
          type: 'info',
          message: res.msg
        });
        return;
      }
      // 登录成功
      // 保存用户至当前回话缓存
      let loginInfo = res.data;
      sessionStorage.setItem('loginInfo', JSON.stringify(loginInfo));
      // 跳转首页
      this.$router.push({
        name: 'home'
      })
    },
    getSmsCode () {
      console.log('getSmsCode...')
    }
  }
}
</script>

<style lang="less" scoped>
.login-slot {
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

  .login-form-wrap {
    .login-form {
      .fm-field {
        width: 100%;
        position: relative;

        .fm-label-icon {
          width: 39px;
          height: 39px;
          line-height: 39px;
          text-align: center;
          color: #fff;
          background-color: #f56c6c;
          position: absolute;
          top: 0.9px;
          left: 0.5px;
          z-index: 99;
          border-radius: 4px 0 0 4px;

          .iconfont {
            font-size: 20px;
            color: #fff;
          }
        }

        .fm-field-input {
          flex: 1;

          .el-input {
            /deep/ .el-input__inner {
              padding: 0 15px 0 50px;
            }

            /deep/ .el-input__inner:focus {
              border-color: #ddd;
            }
          }
        }

        .fm-field-right {
          height: 40px;
          line-height: 40px;
          font-size: 13px;
          float: right;
          padding-left: 10px;
          padding-right: 5px;
          border-left: 1px solid #ddd;

          span {
            cursor: pointer;
            color: #333;
          }
        }
      }
    }
  }

  .submit {
    width: 100%;
    height: 40px;
    line-height: 40px;
    text-align: center;
    font-size: 18px;
    border-radius: 15px;
    background-color: #ff4400;
    color: #fff;
    cursor: pointer;
  }

  .sns-links {
    a {
      margin-top: 15px;
      color: #000;
      display: inline-block;
      text-decoration: none;
      outline: none;
      margin-right: 5px;
    }

    .iconfont {
      font-size: 16px;
      margin-right: 3px;
    }
  }

  .links {
    margin-top: 15px;
    display: flex;
    flex-direction: row;
    justify-content: end;

    .links-op {
      color: #000;
      text-decoration: none;
      margin-left: 10px;
    }
  }
}
</style>
