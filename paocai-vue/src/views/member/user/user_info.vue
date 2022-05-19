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
              :model="userInfoForm"
              status-icon
              :rules="rules"
              ref="userInfoForm"
              label-width="100px"
              class="demo-userInfoForm"
            >
              <el-form-item
                label="用户名"
                prop="username"
              >
                <el-input
                  v-model="userInfoForm.username"
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
                  v-model="userInfoForm.nickname"
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
                  :src="userInfoForm.header"
                >
                  <img
                    src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                  />
                </el-avatar>
                <div style="display:inline-block; margin-left:30px;">
                  <single-upload
                    v-model="userInfoForm.header"
                    :isShowFileList="showFileList"
                  ></single-upload>
                </div>
              </el-form-item>
              <el-form-item
                label="性别"
                prop="gender"
              >
                <el-radio-group
                  v-model="userInfoForm.gender"
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
                    v-model="userInfoForm.birth"
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
                  @click="submitForm('userInfoForm')"
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
import SingleUpload from '@/components/upload/singleUpload'
export default {
  name: 'addressList',
  components: { CommonHeader, CommonFooter, MemberHeader, MemberSetMenu, SingleUpload },
  data () {
    let checkNameUnique = async (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入用户名'));
      }
      if (value == this.loginInfo.username) {
        return callback();
      }
      this.isCheckNameUnique = true;
      try {
        const res = await this.$request({
          url: `member/member/checkUsernameUnique?username=${value}`
        });
        if (res.code !== 200) {
          return callback(new Error(res.msg));
        }
      } catch (e) {
        return callback(new Error('用户名已存在'));
      } finally {
        this.isCheckNameUnique = false;
      }
    };
    return {
      loginInfo: {
        id: ''
      },
      activeMenu: [0, 0],
      activeName: 'baseInfoTab',
      loading: false,
      userInfoForm: {
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
      isCheckNameUnique: false,
      showFileList: false
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
          if (this.hasDifferent()) {
            this.updateUserInfo();
          } else {
            this.$message('对不起，您没有修改任何个人信息');
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    hasDifferent () {
      const loginInfo = this.loginInfo;
      const userInfoForm = this.userInfoForm;
      if (loginInfo.username != userInfoForm.username) {
        return true;
      }
      if (loginInfo.nickname != userInfoForm.nickname) {
        return true;
      }
      if (loginInfo.header != userInfoForm.header) {
        return true;
      }
      if (loginInfo.gender != userInfoForm.gender) {
        return true;
      }
      if (loginInfo.birth != userInfoForm.birth) {
        return true;
      }
      return false;
    },
    async updateUserInfo () {
      const res = await this.$request({
        url: 'member/member/update',
        method: 'POST',
        data: this.userInfoForm
      });
      this.$handleResponseMessage(res, '基本信息修改成功', '未知错误，删除失败', false);
      if (res.code === 200) {
        const loginInfo = res.data;
        // 存入session缓存中
        sessionStorage.setItem('loginInfo', JSON.stringify(loginInfo));
      }
    }
  },
  created () {
    document.title = '账户信息-泡菜商城';
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
  padding: 0 20px;
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