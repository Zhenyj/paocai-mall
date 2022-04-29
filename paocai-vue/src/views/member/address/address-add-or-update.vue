<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px"
    >
      <el-form-item
        label="收货人"
        prop="name"
      >
        <el-input
          v-model="dataForm.name"
          placeholder="收货人"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="默认地址"
        prop="defaultStatus"
      >
        <el-switch
          v-model="dataForm.defaultStatus"
          active-color="#13ce66"
          inactive-color="#ff4949"
          :active-value="1"
          :inactive-value="0"
        >
        </el-switch>
      </el-form-item>
      <el-form-item
        label="所在地区"
        prop="area"
        ref="form-area"
      >
        <dl class="meta-panel">
          <dd class="meta-content">
            <div class="postage">
              <span
                class="to-addr"
                @click="showAddSelector = !showAddSelector"
                v-if="!dataForm.province||dataForm.province===''"
              >
                <span>--请选择--</span>
                <i class="el-icon-arrow-down"></i>
              </span>
              <span
                class="to-addr"
                @click="showAddSelector = !showAddSelector"
                v-else
              >
                <span class="province">{{dataForm.province}}</span>
                <span class="city">{{dataForm.city}}</span>
                <span class="region">{{dataForm.region}}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </div>
          </dd>
          <div
            class="address-selector"
            v-show="showAddSelector"
            @mouseleave="showAddSelector = false"
          >
            <div class="address-dialog">
              <el-tabs
                type="border-card"
                v-model="attrTabActiveName"
              >
                <el-tab-pane name="province">
                  <span slot="label">{{dataForm.province}}<i
                      class="el-icon-arrow-down"
                    ></i></span>
                  <div class="list">
                    <ul>
                      <li
                        v-for="(province,index) in provinces"
                        :key="index"
                        @click="handleProvinceChange(province,index)"
                      ><span>{{province}}</span></li>
                    </ul>
                  </div>
                </el-tab-pane>
                <el-tab-pane name="city">
                  <span slot="label">{{dataForm.city}}<i
                      class="el-icon-arrow-down"
                    ></i></span>
                  <div class="list">
                    <ul>
                      <li
                        v-for="(city,index) in cities"
                        :key="index"
                        @click="handleCityChange(city,index)"
                      ><span>{{city}}</span></li>
                    </ul>
                  </div>
                </el-tab-pane>
                <el-tab-pane
                  name="region"
                  v-if="dataForm.region !=''"
                ><span
                    slot="label"
                    v-if="dataForm.region !=''"
                  >{{dataForm.region}}<i class="el-icon-arrow-down"></i></span>
                  <div class="list">
                    <ul>
                      <li
                        v-for="(region,index) in regions"
                        :key="index"
                        @click="handleRegionChange(region,index)"
                      ><span>{{region}}</span></li>
                    </ul>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </dl>
        <div
          v-if="!formAreaValidate"
          class="el-form-item__error"
        >请选择所在地区（省、市、区/县）</div>
      </el-form-item>
      <el-form-item
        label="详细地址"
        prop="detailAddress"
      >
        <el-input
          v-model="dataForm.detailAddress"
          placeholder="详细地址"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="手机号"
        prop="phone"
      >
        <el-input
          v-model="dataForm.phone"
          placeholder="手机号"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="区域编码"
        prop="areacode"
      >
        <el-input
          v-model="dataForm.areacode"
          placeholder="区域编码"
        ></el-input>
      </el-form-item>
    </el-form>
    <span
      slot="footer"
      class="dialog-footer"
    >
      <el-button @click="visible = false">取消</el-button>
      <el-button
        type="primary"
        @click="dataFormSubmit()"
      >确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'addressAddOrUpdate',
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
      visible: true,
      dataForm: {
        id: 0,
      },
      dataRule: {
        name: [
          { required: true, message: '收货人不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ],
        detailAddress: [
          { required: true, message: '详细地址不能为空', trigger: 'blur' }
        ]
      },
      addressData: {},
      provinces: [],
      cities: [],
      regions: [],
      addrForm: {
        province: {
          index: 0,
          name: '北京市'
        },
        city: {
          index: 0,
          name: '北京市'
        },
        region: {
          index: 0,
          name: '东城区'
        }
      },
      attrTabActiveName: 'province',
      showAddSelector: false,
      // 所在地区校验结果
      formAreaValidate: true
    }
  },
  methods: {
    // 父类调用该方法进行初始化
    init (id) {
      this.visible = true;
      this.dataForm.id = id || 0;
      if (!id || id == '') {
        this.dataForm = {};
        return;
      }
      // 获取指定id的收货地址信息
      this.getAddress(id);
    },
    async getAddress (id) {
      const res = await this.$request({
        url: 'member/memberreceiveaddress/info/' + id,
        method: 'GET'
      });
      this.$handleResponseMessage(res, '', '地址数据异常');
      if (res.code !== 200) {
        return;
      }
      this.dataForm = res.data;
    },
    // 提交新增/修改
    dataFormSubmit () {
      if (!this.validateArea()) {
        return;
      }
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          // 提交完成，让父组件重新获取地址信息
          const res = await this.$request({
            url: `member/memberreceiveaddress/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'POST',
            data: this.dataForm
          });
          this.$handleResponseMessage(res, !this.dataForm.id ? '保存成功' : '修改成功', !this.dataForm.id ? '收货地址保存失败' : '收货地址修改失败');
          this.visible = false;
          this.$emit('refreshDataList');
        }
      });
    },
    // 初始化地址数据
    initAddrData () {
      const addressData = require('@/assets/city.json');
      this.addressData = addressData;
      this.initAddrList(addressData);
      this.initAddrForm(addressData);
    },
    // 初始化地址选择列表数据
    initAddrList (addressData) {
      if (addressData.length === 0) {
        return;
      }
      let provinces = [];
      let cities = [];
      let regions = [];
      addressData.forEach((item, index) => {
        provinces.push(item.name);
      });
      addressData[0].city.forEach((item, index) => {
        cities.push(item.name);
      });
      addressData[0].city[0].region.forEach((item, index) => {
        regions.push(item.name);
      });
      this.provinces = provinces;
      this.cities = cities;
      this.regions = regions;
    },
    // 初始化用户地址
    initAddrForm (addressData) {
      // 匹配用户默认收货地址
      if (!this.defaultReceiveAddr || !this.defaultReceiveAddr.province || this.defaultReceiveAddr.province === '' || !this.defaultReceiveAddr.city || this.defaultReceiveAddr.city === '' || !this.defaultReceiveAddr.region || this.defaultReceiveAddr.region === '') {
        return;
      }
      let provinceObj = {};
      let cityObj = {};
      let regionObj = {};
      if (addressData.length > 0) {
        // 省
        addressData.forEach((province, index1) => {
          if (province.name === this.defaultReceiveAddr.province) {
            provinceObj.index = index1;
            provinceObj.name = province.name;
            // 市
            province.city.forEach((city, index2) => {
              if (city.name === this.defaultReceiveAddr.city) {
                cityObj.index = index2;
                cityObj.name = city.name;
                // 区
                city.region.forEach((region, index3) => {
                  if (region.name === this.defaultReceiveAddr.region) {
                    regionObj.index = index3;
                    regionObj.name = region.name;
                    this.addrForm.province = provinceObj;
                    this.addrForm.city = cityObj;
                    this.addrForm.region = regionObj;
                    this.dataForm.province = provinceObj.name;
                    this.dataForm.city = cityObj.name;
                    this.dataForm.region = provinceObj.name;
                  }
                })
              }
            })
          }
        })
      }
      this.validateArea();
    },
    // 更改收货地址省
    handleProvinceChange (province, index) {
      this.addrForm.province.name = province;
      this.addrForm.province.index = index;
      this.dataForm.province = province;
      let cities = [];
      this.addressData[index].city.forEach((item, index) => {
        cities.push(item.name);
      });
      this.cities = cities;
      this.addrForm.city.index = 0;
      this.addrForm.city.name = cities[0];
      this.addrForm.region.index = 0;
      this.addrForm.region.name = '';
      this.regions = [];
      this.dataForm.city = cities[0];
      this.dataForm.region = '';
      this.attrTabActiveName = 'city';
      this.validateArea();
    },
    // 更改收货地址市
    handleCityChange (city, index) {
      this.addrForm.city.name = city;
      this.addrForm.city.index = index;
      let regions = [];
      this.addressData[this.addrForm.province.index].city[0].region.forEach((item, index) => {
        regions.push(item.name);
      });
      this.regions = regions;
      this.addrForm.region.index = 0;
      this.addrForm.region.name = regions[0];
      this.dataForm.region = regions[0];
      this.attrTabActiveName = 'region';
      this.validateArea();
    },
    // 更改收货地址区
    handleRegionChange (region, index) {
      this.addrForm.region.name = region;
      this.addrForm.region.index = index;
      this.dataForm.region = region;
      this.showAddSelector = false;
      this.validateArea();
    },
    // 校验
    validateArea () {
      const dataForm = this.dataForm;
      // 省、市、区/县是否为空
      const b = dataForm.region && dataForm.region.trim() !== '' && dataForm.city && dataForm.city.trim() !== '' && dataForm.province && dataForm.province.trim() !== '';
      this.formAreaValidate = b;
      return b;
    }
  },
  mounted () {
    this.initAddrData();
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
.el-dialog__wrapper {
  z-index: 11111 !important;
}
.meta-panel {
  line-height: 36px;
  color: #333;
  position: relative;
  .meta-tit {
    color: #999;
    font-size: 12px;
    text-align: left;
    float: left;
    width: 50px;
    margin: 0 10px;
  }
}
.meta-panel {
  .postage {
    line-height: 18px;
    padding: 9px 0;
    position: relative;
    .from-addr {
      margin: 0 6px 0 0;
    }
    .to-addr {
      border: 1px solid #ddd;
      padding: 3px 5px;
      cursor: pointer;

      span {
        color: #333;
        margin-right: -12px;
        padding-right: 12px;
        background: 0 0;
        border: none;
        position: relative;
        z-index: 2;
      }
    }
  }
  .address-selector {
    position: absolute;
    left: 0px;
    top: 32px;
    z-index: 1000;
    .address-dialog {
      width: 500px;
      position: relative;
      background: rgb(255, 255, 255);
      .list {
        ul {
          display: flex;
          flex-direction: row;
          justify-content: start;
          flex-wrap: wrap;

          li {
            width: 100px;
            margin-right: 11px;
            span {
              cursor: pointer;
            }
            span:hover {
              color: #f40;
            }
          }
        }
      }
      /deep/
        .el-tabs--border-card
        > .el-tabs__header
        .el-tabs__item:not(.is-disabled):hover {
        color: #f40;
      }
      /deep/ .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active {
        color: #f40;
        background-color: #fff;
        border-right-color: #dcdfe6;
        border-left-color: #dcdfe6;
      }
    }
  }
}
</style>