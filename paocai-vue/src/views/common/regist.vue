<template>
  <log-reg :title="title">
    <div
      class="regist"
      v-if="!registStatus"
    >
      <div class="regist-form-wrap">
        <el-form
          class="regist-form"
          status-icon
          :model="registForm"
          ref="registForm"
          :rules="rules"
          label-position="top"
        >
          <el-form-item
            label="用户名"
            prop="username"
          >
            <div class="fm-field">
              <div class="fm-field-input">
                <el-input
                  v-model="registForm.username"
                  placeholder="您的用户名或登录名"
                  size="large"
                  maxlength="11"
                  style="width:100%;"
                ></el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item
            label="设置密码"
            prop="password"
          >
            <div class="fm-field">
              <div class="fm-field-input">
                <el-input
                  v-model="registForm.password"
                  placeholder="建议两种或以上的字符组合"
                  size="large"
                  type="password"
                  show-password
                ></el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item
            label="确认密码"
            prop="confirmPassword"
          >
            <div class="fm-field">
              <div class="fm-field-input">
                <el-input
                  v-model="registForm.confirmPassword"
                  placeholder="请再次输入密码"
                  size="large"
                  type="text"
                  show-password
                ></el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item
            label="手机号"
            prop="phone"
          >
            <div class="fm-field">
              <div class="fm-field-input">
                <el-input
                  v-model="registForm.phone"
                  placeholder="请输入手机号"
                  size="large"
                  type="text"
                >
                </el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item
            label="邮箱"
            prop="email"
          >
            <div class="fm-field">
              <div class="fm-field-input">
                <el-input
                  v-model="registForm.email"
                  placeholder="请输入邮箱"
                  size="large"
                  type="text"
                >
                </el-input>
              </div>
            </div>
          </el-form-item>
        </el-form>
        <button
          class="submit"
          @click="regist('registForm')"
        >注册</button>
        <div class="fm-agreement">
          <el-checkbox v-model="agreement"></el-checkbox>
          <label
            class="fm-agreement-text"
            @click="agreement = !agreement"
          >
            已阅读并同意以下协议
            <a
              href="#"
              target="_blank"
            >泡菜平台服务协议</a>
            、
            <a
              href="#"
              target="_blank"
            >隐私权政策、法律声明</a>
            、
            <a
              href="#"
              target="_blank"
            >法律声明</a>
            、
          </label>
        </div>
      </div>
      <div class="links">
        <router-link
          to="/login"
          class="links-op"
        >立即登录</router-link>
        <router-view></router-view>
      </div>
    </div>
    <div
      class="regit-success"
      v-else
    ></div>
  </log-reg>
</template>

<script>
import LogReg from '@/components/login-regist.vue';
export default {
  components: { LogReg },
  data () {
    // 校验确认密码
    let validateConfirmPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
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
      title: '欢迎注册',
      registStatus: false,
      registForm: {
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        email: ''
      },
      rules: {
        username: [
          { required: true, message: '必须填写用户名', trigger: 'blur' },
          { min: 2, max: 16, message: '用户名长度在 2 到 16 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '必须填写密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度在 6 到 18 个字符', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateConfirmPwd, trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '必须填写手机号', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '必须填写用邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
      },
      agreement: false
    };
  },
  methods: {
    regist (formName) {
      // 表单校验
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 是否同意协议
          if (!this.agreement) {
            this.$confirm('为保障你的合法权益，请阅读并同意用户协议和隐私政策', '用户协议及隐私保护', {
              confirmButtonText: '同意并登录',
              cancelButtonText: '不同意',
              type: 'info'
            }).then(() => {
              this.agreement = true;
              this.regist(formName);
            }).catch(() => {
              return;
            });
          }
          this.doRegist();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    async doRegist () {
      const res = await this.$request({
        url: 'auth/regist',
        method: 'POST',
        data: this.registForm
      })
      if (res.code !== 200) {
        this.$message({
          type: 'info',
          message: res.msg ? res.msg : '注册失败'
        });
        return;
      }
      this.$router.push({ name: 'home' });
    }
  }
};
</script>

<style lang="less" scoped>
.regist {
  .regist-form-wrap {
    .regist-form {
      .fm-field {
        width: 100%;
        display: flex;

        .fm-label-icon {
          width: 40px;
          height: 40px;
          line-height: 40px;
          text-align: center;
          color: #fff;
          background-color: #ccc;

          .iconfont {
            font-size: 20px;
            color: #fff;
          }
        }

        .fm-field-input {
          flex: 1;

          .el-input {
            /deep/ .el-input__inner {
              border-radius: 10px;
            }
          }
        }

        .fm-field-right {
          height: 40px;
          line-height: 40px;
          font-size: 13px;
          float: right;

          span {
            cursor: pointer;
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
    border: none;
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

  .fm-agreement {
    margin-top: 10px;

    &:deep(.el-checkbox) {
      margin-right: 0px;
    }

    .fm-agreement-text {
      text-align: left;
      margin-left: 5px;

      a {
        text-decoration: none;
        color: #fd6020;
      }
    }
  }
}
</style>

<style>
.el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #fd6020;
  border-color: #fd6020;
}

.el-checkbox__input.is-checked + .el-checkbox__label {
  color: #fd6020;
}

.el-checkbox.is-bordered.is-checked {
  border-color: #fd6020;
}

.el-checkbox__input.is-focus .el-checkbox__inner {
  border-color: #fd6020;
}

.el-checkbox__input .el-checkbox__inner:hover {
  border-color: #fd6020;
}

.el-form-item .el-form-item__label {
  padding: 0px;
  line-height: 20px;
}
</style>
