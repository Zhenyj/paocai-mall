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
        <el-tabs v-model="activeName">
          <el-tab-pane
            label="基本信息"
            name="baseInfoTab"
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
                label="用户名"
                prop="username"
              >
                <el-input
                  v-model="ruleForm.username"
                  autocomplete="off"
                  size="mini"
                ></el-input>
                <span class="tip">可用于登录，请牢记哦~</span>
              </el-form-item>
              <el-form-item
                label="昵称"
                prop="nickname"
              >
                <el-input
                  v-model="ruleForm.nickname"
                  autocomplete="off"
                  size="mini"
                ></el-input>
                <div
                  class="check-name"
                  v-show="isCheckNameUnique"
                >昵称唯一性验证中，请稍等...</div>
              </el-form-item>
              <el-form-item
                label="头像"
                prop="header"
              >
                <el-avatar
                  :size="80"
                  :src="ruleForm.header"
                >
                  <img
                    src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                  />
                </el-avatar>
              </el-form-item>
              <el-form-item
                label="性别"
                prop="gender"
              >
                <el-radio-group
                  v-model="ruleForm.gender"
                  size="small"
                >
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="0">女</el-radio>
                  <el-radio :label="2">保密</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item
                label="生日"
                prop="birth"
              >
                <el-form-item prop="birth">
                  <el-date-picker
                    type="date"
                    placeholder="生日"
                    v-model="ruleForm.birth"
                    format="yyyy 年 MM 月 dd 日"
                    value-format="yyyy-MM-dd"
                    size="mini"
                  ></el-date-picker>
                  <span class="tip">填生日有惊喜哦~</span>
                </el-form-item>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitForm('ruleForm')"
                >提交</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane
            label="头像照片"
            name="headImgTab"
          >头像照片</el-tab-pane>
        </el-tabs>
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
export default {
  name: 'addressList',
  components: { CommonHeader, CommonFooter, MemberHeader, MemberSetMenu },
  data () {
    let checkNameUnique = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入用户名'));
      }
      this.isCheckNameUnique = true;
      setTimeout(() => {
        this.isCheckNameUnique = false;
      }, 1000);
    };
    return {
      loginInfo: {
        id: ''
      },
      activeMenu: [0, 0],
      activeName: 'baseInfoTab',
      loading: false,
      ruleForm: {
        username: 'zyj',
        nickname: 'zyj',
        header: 'https://i.jd.com/commons/img/no-img_mid_.jpg',
        gender: 2,
        birth: '1999-01-01'
      },
      rules: {
        username: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
        nickname: [{ validator: checkNameUnique, trigger: 'blur' }],
      },
      isCheckNameUnique: false
    }
  },
  methods: {
    // 获取用户登录信息
    async getLoginInfo () {
      const loginInfo = await this.$getLoginInfo();
      if (loginInfo != null) {
        this.loginInfo = loginInfo;
      } else {
        // TODO
        // this.$alert('请先登录再进行此操作', '提示', {
        //   confirmButtonText: '确定',
        //   callback: action => {
        //     this.$router.push({ name: 'login' });
        //   }
        // });
      }
    },
    handleNavTo (routerName) {
      this.$router.push({ name: routerName });
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
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
.user-info-page .el-form-item {
  margin-bottom: 15px;
}
.user-info-page .el-form-item .el-input {
  max-width: 205px;
}
.user-info-page .el-form-item .el-input__inner {
  max-width: 200px;
  padding-right: 20px;
}
</style>