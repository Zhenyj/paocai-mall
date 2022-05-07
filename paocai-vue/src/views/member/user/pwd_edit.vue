<template>
  <div
    id="user-info-page"
    class="user-info-page"
  >
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <!-- 头部 -->
    <member-header
      @navToByRouterName="handleNavTo($event)"
      :defaultActive="1"
    ></member-header>
    <div class="content">
      <!-- 内容 -->
      <div class="left-menu">
        <!-- 侧边菜单 -->
        <member-set-menu
          :bgc="'#f0f3ef'"
          :default-active="activeMenu"
        ></member-set-menu>
      </div>
      <div
        class="main"
        v-loading="loading"
        v-loading.body="loading"
        v-loading.lock="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
      >
        <el-form
          :model="ruleForm"
          status-icon
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item
            label="旧密码"
            prop="oldPass"
          >
            <el-input
              type="password"
              v-model="ruleForm.oldPass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="新密码"
            prop="pass"
          >
            <el-input
              type="password"
              v-model="ruleForm.pass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="确认密码"
            prop="checkPass"
          >
            <el-input
              type="password"
              v-model="ruleForm.checkPass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="submitForm('ruleForm')"
            >提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
import MemberHeader from '@/views/member/member-header'
import MemberSetMenu from '@/views/member/member-set-menu.vue'
import { clearLoginInfo } from '@/utils'
export default {
  name: 'addressList',
  components: { CommonHeader, CommonFooter, MemberHeader, MemberSetMenu },
  data () {
    let validateOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入旧密码'));
      } else if (value.length < 6) {
        callback(new Error('密码长度必须在6-18个字符之间'));
      } else {
        callback();
      }
    };
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value === this.ruleForm.oldPass) {
        callback(new Error('新密码与旧密码相同'));
      } else if (value.length < 6) {
        callback(new Error('密码长度必须在6-18个字符之间'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value.length < 6) {
        callback(new Error('密码长度必须在6-18个字符之间'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      loginInfo: {
        id: ''
      },
      activeMenu: [0, 1],
      loading: false,
      ruleForm: {
        oldPass: '',
        pass: '',
        checkPass: ''
      },
      rules: {
        oldPass: [
          { required: true, validator: validateOldPass, trigger: 'blur', }
        ],
        pass: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 获取用户登录信息
    async getLoginInfo () {
      const loginInfo = await this.$getLoginInfo();
      if (loginInfo != null) {
        this.loginInfo = loginInfo;
        let userInfoForm = {
          id: loginInfo.id,
          username: loginInfo.username,
          nickname: loginInfo.nickname,
          header: loginInfo.header,
          gender: loginInfo.gender,
          birth: loginInfo.birth
        };
        this.userInfoForm = userInfoForm;
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
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.updatePwd();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    async updatePwd () {
      try {
        this.loading = true;
        const res = await this.$request({
          url: 'member/member/pwd/update',
          method: 'POST',
          data: this.ruleForm
        });
        this.$handleResponseMessage(res, '密码修改成功', '未知错误，密码修改失败');
        if (res.code === 200) {
          // 从session缓存中删除登录信息
          sessionStorage.removeItem('loginInfo');
          // 删除cookie
          clearLoginInfo();
          // 跳转登录页
          this.$router.replace({ name: 'login' });
        }
      } finally {
        this.loading = false;
      }
    }
  },
  created () {
    document.title = '账户信息';
    this.getLoginInfo();
  }
}
</script>

<style lang="less" scoped>
#user-info-page {
  background-color: #f0f3ef;
}
.content {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  /deep/ .el-icon-loading {
    font-size: 40px;
  }
  /deep/ .el-loading-text {
    font-size: 24px;
  }
  .main {
    padding-top: 30px;
    padding-left: 25%;
    .el-input--mini {
      /deep/ .el-icon-loading {
        font-size: 10px;
      }
    }

    /deep/ .el-avatar {
      cursor: pointer;
      overflow: hidden;
      position: relative;
    }
    .el-avatar:hover .header-edit {
      display: block;
    }
    .header-edit:hover {
      display: block;
    }
  }
}
.left-menu {
  width: 170px;
  min-height: 500px;
}
.main {
  flex: 1;
  margin-left: 15px;
  margin-top: 15px;
  background-color: #fff;
  padding: 0 10px;
  min-height: 500px;
  .tip {
    color: #999;
    font-size: 10px;
  }
  .check-name {
    font-size: 10px;
    color: #999;
    line-height: 15px;
  }
}
</style>
<style>
.user-info-page .el-form-item .el-input {
  max-width: 205px;
}
.user-info-page .el-form-item .el-input__inner {
  max-width: 200px;
  padding-right: 20px;
}
</style>