<template>
  <div id="address-list-page">
    <common-header
      :loginInfo="loginInfo"
      @navToByRouterName="handleNavTo($event)"
    ></common-header>
    <!-- 头部 -->
    <member-header></member-header>
    <div
      class="content"
      v-loading="loading"
      v-loading.body="loading"
      v-loading.lock="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <!-- 内容 -->
      <div class="left-menu">
        <!-- 侧边菜单 -->
        <member-set-menu
          :bgc="'#f0f3ef'"
          :default-active="activeMenu"
        ></member-set-menu>
      </div>
      <div class="main">
        <div class="address-list">
          <div class="mt">
            <el-button
              type="success"
              size="medium"
              round
              @click="addOrUpdateHandle()"
            >新增收货地址</el-button>
            <span class="ftx">{{'您已创建 '+addressList.length+' 个收货地址，最多可创建 25 个'}}
            </span>
          </div>
          <div class="mc">
            <div
              class="add-item"
              v-for="(v,i) in addressList"
              :key="i"
            >
              <el-descriptions
                class="margin-top"
                title="甄英俊"
                :column="1"
                :size="'small'"
              >
                <el-descriptions-item label="收货人">{{v.name}}
                </el-descriptions-item>
                <el-descriptions-item label="所在地区">
                  {{v.province+v.city+v.region}}
                </el-descriptions-item>
                <el-descriptions-item label="地址">
                  {{v.province+v.city+v.region+v.detailAddress}}
                </el-descriptions-item>
                <el-descriptions-item label="手机号">{{v.phone | showPhone}}
                </el-descriptions-item>
                <el-descriptions-item label="邮政编码">
                  {{v.postCode && v.postCode.trim()!==''?v.postCode:''}}
                </el-descriptions-item>
              </el-descriptions>
              <div class="extra">
                <a
                  v-if="v.defaultStatus === 0"
                  @click="setDefault(v.id)"
                >设为默认</a>
                <a @click="addOrUpdateHandle(v.id)">编辑</a>
              </div>
              <div
                class="delete"
                @click="deleteAddr(v.id)"
              >
                <i class="el-icon-close"></i>
              </div>
            </div>
          </div>
          <div
            class="mt"
            v-if="addressList.length>=3"
          >
            <el-button
              type="success"
              size="medium"
              round
              @click="addOrUpdateHandle()"
            >新增收货地址</el-button>
            <span class="ftx">{{'您已创建 '+addressList.length+' 个收货地址，最多可创建 25 个'}}
            </span>
          </div>
        </div>
      </div>
    </div>
    <common-footer></common-footer>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdate"
      @refreshDataList="getAddress"
    ></add-or-update>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
import MemberHeader from '../member-header'
import MemberSetMenu from '../member-set-menu.vue'
import AddOrUpdate from './address-add-or-update'
export default {
  name: 'addressList',
  components: { CommonHeader, CommonFooter, MemberHeader, MemberSetMenu, AddOrUpdate },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      activeMenu: [0, 2],
      addressList: [],
      addOrUpdateVisible: false,
      loading: true
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
        this.$alert('请先登录再进行此操作', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            this.$router.push({ name: 'login' });
          }
        });
      }
    },
    async getAddress () {
      this.loading = true;
      try {
        const res = await this.$request({
          url: 'member/memberreceiveaddress/my_address/list',
          method: 'GET'
        });
        this.$handleResponseMessage(res, '', '地址数据异常');
        if (res.code !== 200) {
          return;
        }
        this.addressList = res.data;
      } finally {
        this.loading = false;
      }
    },
    setDefault (id) {
      this.$confirm('是否确认将该收获地址设为默认, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.doSetDefault(id);
      }).catch(() => {
      });
    },
    async doSetDefault (id) {
      try {
        this.loading = true;
        const res = await this.$request({
          url: 'member/memberreceiveaddress/update',
          method: 'POST',
          data: {
            id,
            defaultStatus: 1
          }
        });
        this.$handleResponseMessage(res, '地址设为默认成功', '地址设为默认失败');
        if (res.code === 200) {
          this.getAddress();
        }
      } finally {
        this.loading = false;
      }
    },
    deleteAddr (id) {
      this.$confirm('是否将删除此收获地址, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.doDelete(id);
      }).catch(() => {
      });
    },
    async doDelete (id) {
      try {
        this.loading = true;
        const res = await this.$request({
          url: 'member/memberreceiveaddress/delete/' + id,
          method: 'POST'
        });
        this.$handleResponseMessage(res, '删除成功', '删除失败');
        if (res.code === 200) {
          this.getAddress();
        }
      } finally {
        this.loading = false;
      }
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    handleNavTo (routerName) {
      this.$router.push({ name: routerName });
    }
  },
  created () {
    document.title = '收货地址-泡菜商城';
    this.getAddress();
    this.getLoginInfo();
  },
  filters: {
    showPhone (phone) {
      if (phone.length === 11) {
        return phone.slice(0, 3) + '****' + phone.slice(7, 11);
      } else {
        let len = phone.length;
        return phone.slice(0, len - 4) + '****';
      }
    }
  }
}
</script>

<style lang="less" scoped>
#address-list-page {
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
  min-height: 400px;
}
.main {
  flex: 1;
  margin-left: 15px;
  margin-top: 15px;
}
.address-list {
  padding: 10px 20px 20px;
  background-color: #fff;
  margin-bottom: 20px;
  .mt {
    padding: 10px 0;
    .ftx {
      margin-left: 15px;
      color: #999;
    }
  }
  .mc {
    .add-item {
      position: relative;
      border: 2px solid #e6e6e6;
      padding: 10px;
      margin-bottom: 15px;
      font: 12px/150% tahoma, arial, Microsoft YaHei, Hiragino Sans GB,
        "\u5b8b\u4f53", sans-serif;
      /deep/ .el-descriptions__header {
        margin-bottom: 15px;
      }
      /deep/ .el-descriptions__title {
        font-size: 14px;
        font-weight: 700;
        color: #666;
      }
      /deep/ .el-descriptions-item__label {
        color: #999;
        width: 70px;
        text-align: right;
      }
      /deep/ .el-descriptions-item__content {
        color: #555;
      }
      .extra {
        position: absolute;
        right: 30px;
        bottom: 15px;
        a {
          color: #005ea7;
          margin-left: 10px;
        }
      }
      .delete {
        position: absolute;
        right: 10px;
        top: 10px;
        font-size: 20px;
        color: #cccccc;
        cursor: pointer;
      }
    }
  }
}
</style>