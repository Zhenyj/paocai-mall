<template>
  <div>
    <common-header :login-info="loginInfo"></common-header>
    <div class="product-content">
      <!-- 顶部搜索栏 -->
      <div class="top">
        <div class="top-wrap">
          <div class="logo">
            <a href="#">
              <img src="../../assets/logo.png">
            </a>
          </div>
          <div class="search">
            <div class="search-wrap">
              <!-- 搜索框 -->
              <div class="search-bd">
                <div class="search-panel">
                  <div class="search-button">
                    <button
                      type="button"
                      class="btn-search"
                      @click="search"
                    >搜索</button>
                  </div>
                  <div class="search-panel-fields">
                    <div class="search-combobox">
                      <div class="search-combobox-input-wrap">
                        <input
                          class="search-combobox-input"
                          id="keyword"
                          v-model="keyword"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="!productInfo.skuId">
        <el-empty
          description="暂无商品数据或商品已下架"
          :image-size="400"
        ></el-empty>
      </div>
      <div v-else>
        <!-- 面包屑导航 -->
        <div class="crumb-wrap">
          <div class="w">
            <div class="crumb">
              <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item>手机通讯</el-breadcrumb-item>
                <el-breadcrumb-item>手机</el-breadcrumb-item>
                <el-breadcrumb-item>手机</el-breadcrumb-item>
                <el-breadcrumb-item>小米（MI）</el-breadcrumb-item>
                <el-breadcrumb-item>红米（Redmi） K50</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
            <div class="contact">
              <div class="contact-item">
                <a href="">小米官方自营店</a>
              </div>
              <div class="contact-item">
                <a href=""><i class="iconfont icon-kefu"></i>联系客服</a>
              </div>
              <div class="contact-item">
                <a href=""><i class="iconfont icon-collection-fill"></i>关注店铺</a>
              </div>
            </div>
          </div>
        </div>
        <!-- 商品 -->
        <div class="product-intro">
          <div class="preview-wrap">
            <div
              class="preview"
              @mouseenter="carouselStop"
              @mouseleave="carouselStart"
            >
              <div class="main-img">
                <el-image :src="this.productInfo.images[carouselIndex]">
                  <div
                    slot="error"
                    class="image-slot"
                  >
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
              <div class="spec-list">
                <div class=" spec-forward">
                  <i
                    class="spec-icon el-icon-arrow-left"
                    @click="specForward"
                  ></i>
                </div>
                <div class="spec-items">
                  <ul
                    class="lh"
                    ref="spec-lh"
                    style="left: 0px;"
                  >
                    <li
                      v-for="(item,index) in productInfo.images"
                      :key="index"
                      :class="{'img-hover':index===carouselIndex}"
                      @mouseenter="specImageHover(index)"
                      ref="spec-img"
                    >
                      <el-image :src="item">
                        <div
                          slot="error"
                          class="image-slot"
                        >
                          <i class="el-icon-picture-outline"></i>
                        </div>
                      </el-image>
                    </li>
                  </ul>
                </div>
                <div class=" spec-backward">
                  <i
                    class="spec-icon el-icon-arrow-right"
                    @click="specBackForward"
                  ></i>
                </div>
              </div>
            </div>
          </div>
          <div class="info-wrap">
            <div class="sku-name">
              {{productInfo.skuName}}
            </div>
            <div class="news">
              <div class="item">
                【品质好物】天玑8100年度旗舰处理器，三星2K直屏，支持光学防抖【点击抢购】<a href="#">查看<i
                    class="el-icon-arrow-right"
                  ></i></a>
              </div>
            </div>
            <div class="price-wrap">
              <dl class="price-panel">
                <dt class="tit">价格</dt>
                <dd>
                  <div class="price">
                    <i class="iconfont icon-money"></i>
                    <span class="sku-price">{{productInfo.skuPrice}}</span>
                  </div>
                </dd>
              </dl>
            </div>
            <!-- 地址、运费 -->
            <div class="meta meta-freight">
              <dl class="meta-panel">
                <dt class="meta-tit">
                  运费
                </dt>
                <dd class="meta-content">
                  <div class="postage">
                    <span class="from-addr">北京</span>至
                    <span
                      class="to-addr"
                      @click="showAddSelector = !showAddSelector"
                    >
                      <span class="province">{{addrForm.province.name}}</span>
                      <span class="city">{{addrForm.city.name}}</span>
                      <span class="region">{{addrForm.region.name}}</span>
                      <i class="el-icon-arrow-down"></i>
                    </span>
                    <span class="postage-info">快递包邮</span>
                  </div>
                </dd>
                <div
                  class="address-selector"
                  v-show="showAddSelector"
                >
                  <div class="address-dialog">
                    <el-tabs
                      type="border-card"
                      v-model="attrTabActiveName"
                    >
                      <el-tab-pane name="province">
                        <span slot="label">{{addrForm.province.name}}<i
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
                        <span slot="label">{{addrForm.city.name}}<i
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
                        v-if="addrForm.region.name !=''"
                      ><span
                          slot="label"
                          v-if="addrForm.region.name !=''"
                        >{{addrForm.region.name}}<i
                            class="el-icon-arrow-down"></i></span>
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
            </div>
            <!-- 重量 -->
            <div class="meta meta-weight">
              <dl class="meta-panel">
                <dt class="meta-tit">
                  重量
                </dt>
                <dd class="meta-content">
                  {{productInfo.weight}}kg
                </dd>
              </dl>
            </div>
            <!-- 积分信息 -->
            <div class="bounds-wrap">
              <div class="bounds-item grow-bounds">送成长积分<span>250</span></div>
              <div class="bounds-item buy-bounds">送购物积分<span>250</span></div>
            </div>
            <div class="summary-line"></div>
            <!-- 属性选择 -->
            <div class="choose-attrs">
              <div class="attr-group">
                <div class="dt">选择颜色</div>
                <div class="dd">
                  <div
                    class="attr-item"
                    title="墨羽"
                  >墨羽</div>
                  <div
                    class="attr-item"
                    title="银迹"
                  >银迹</div>
                  <div
                    class="attr-item"
                    title="幽芒"
                  >幽芒</div>
                  <div
                    class="attr-item selected"
                    title="幻境"
                  >幻境</div>
                </div>
              </div>
              <div class="attr-group">
                <div class="dt">选择版本</div>
                <div class="dd">
                  <div
                    class="attr-item"
                    title="8+128"
                  >8+128</div>
                  <div
                    class="attr-item"
                    title="8+256"
                  >8+256</div>
                  <div
                    class="attr-item"
                    title="12+256"
                  >12+256</div>
                  <div
                    class="attr-item selected"
                    title="12+512"
                  >12+512</div>
                </div>
              </div>
            </div>
            <div class="summary-line"></div>
            <div class="choose-btns">
              <div class="choose-num">
                <div class="dt">数量</div>
                <div class="dd">
                  <el-input-number
                    v-model="buyNum"
                    @change="handleChangeBuyNum"
                    :min="1"
                    :max="99"
                    :step="1"
                    controls-position="right"
                    step-strictly
                  ></el-input-number>
                  <span class="num-unit">件</span>
                  <span class="stock">{{productInfo.hasStock?'有货':'缺货'}}</span>
                </div>
              </div>
              <div class="choose-action">
                <div class="action-item action-buy">立即购买</div>
                <div class="action-item action-basket"><i
                    class="iconfont icon-cart"
                  ></i>加入购物车
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="detail-wrap">
          <div class="aside">
            <div class="m-aside">
              <div class="mt">
                <h3>小米官方自营店</h3>
                <i class="iconfont icon-kefu"></i>
              </div>
              <div class="mc">
                <div class="pop-score-summary">
                  <div class="btns">
                    <div class="btn-item enter-shop"><i
                        class="el-icon-s-goods"></i>进入店铺</div>
                    <div class="btn-item follow-shop"><i
                        class="el-icon-star-on"></i>关注店铺</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="detail">
            <product-detail :descImages="productInfo.descImages">
            </product-detail>
          </div>
        </div>
      </div>
    </div>
    <common-footer></common-footer>
  </div>
</template>

<script>
import CommonHeader from '@/components/common/header.vue'
import CommonFooter from '@/components/common/footer.vue'
import ProductDetail from './product-detail.vue'
export default {
  name: 'product',
  components: { CommonHeader, CommonFooter, ProductDetail },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      keyword: '',
      productInfo: {
        skuId: 1690,
        spuId: 1671,
        skuName: 'Redmi K50 墨羽 12GB+256GB',
        skuPrice: '2399.00',
        skuImg: 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg',
        images: ['https://img14.360buyimg.com/n1/s450x450_jfs/t1/105178/10/26230/268355/6250f118E52fcee7b/c2462e1856cdcf0b.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img12.360buyimg.com/n1/s450x450_jfs/t1/199208/12/22008/274510/6250f15dE910ae355/1870874a6394fe4f.jpg', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/105178/10/26230/268355/6250f118E52fcee7b/c2462e1856cdcf0b.jpg', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/105178/10/26230/268355/6250f118E52fcee7b/c2462e1856cdcf0b.jpg', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/105178/10/26230/268355/6250f118E52fcee7b/c2462e1856cdcf0b.jpg', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/105178/10/26230/268355/6250f118E52fcee7b/c2462e1856cdcf0b.jpg'],
        descImages: ['https://img30.360buyimg.com/sku/jfs/t1/219151/20/15066/215160/62331980E84bad091/6e2c0a1fa012f009.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/86181/39/22556/233158/62331980Eb2b73503/ccb0de162521a60a.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/116489/7/22678/197566/62331980E2e397502/f12f7a2b34cce02e.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/126774/16/22581/242468/62331980E32ca5223/55a65a8a4244423e.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/117436/2/21571/210196/62331980E63152806/86da06857bd941a0.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/213173/5/14991/105272/62331980Eb1e34009/21e514957140b8ed.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/102195/8/25800/125036/62331980Ee25d766b/d5217b686cf24063.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/191928/25/21570/81977/62331980Ee58d3486/bc380ff04f62b18f.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/115555/31/21890/283461/62331980Eab5294c9/3ed64550c231fd49.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/110338/36/24866/40538/62331980E78555416/5cefd8fc19cc0b57.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/145404/4/25201/108158/62331980Eeb0ff926/8e13dfb746e0308e.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/109832/5/26077/179494/62331980E1b5aefaf/b7ebc96d9adcdd3c.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/102534/5/26071/131879/62331980E921cb07c/c9a9f9d0cf70c7c6.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/109060/28/27344/187444/6250f057E88892a72/bbcc94ad68837671.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/117832/38/22039/106595/62331980E80a9b463/ac93ceaf1dd9fd9e.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/218362/8/14799/138393/62331980E6ce8cf08/b9944df59f172cc0.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/108520/31/25457/221817/62331980E51471709/515855b50577abae.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/97382/19/26288/217420/62331980E5a22e6af/830ca44f7ce96c44.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/88690/6/24872/243920/62331980Eaec8524d/3f1d2f37ad36678c.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/219465/37/15066/168879/62331980E859d45a0/c5c6443e870e0e98.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/214597/4/15046/58798/62331980E65ba4d64/8bfd525e8d9388e1.jpg', 'https://img30.360buyimg.com/sku/jfs/t1/113952/34/22208/232990/62331980E1b912e48/1d65be176e68e183.jpg'],
        saleCount: 0,
        hasStock: true,
        hotScore: 0,
        brandId: 2,
        catalogId: '225',
        brandName: '红米',
        brandImg: null,
        weight: 0.201,
        catalogName: '手机',
        attrs: [
          {
            attrId: 1292,
            attrName: 'CPU',
            attrValue: '天玑8100'
          },
          {
            attrId: 1281,
            attrName: '上市年份',
            attrValue: '2022'
          },
          {
            attrId: 1282,
            attrName: '品牌',
            attrValue: '红米'
          },
          {
            attrId: 1290,
            attrName: '屏幕刷新率',
            attrValue: '120Hz'
          }
        ]
      },
      // 选购数量
      buyNum: 1,
      //
      carouselIndex: 0,
      // 轮播时间
      carouselInterval: 4000,
      // 用于定时器
      timer: '',
      defaultReceiveAddr: {
        province: '福建省',
        city: '龙岩市',
        region: '新罗区',
        detail_address: '',
        defaultStatus: 1
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
      showAddSelector: false
    }
  },
  methods: {
    // 获取用户登录信息
    async getLoginInfo () {
      const loginInfo = await this.$getLoginInfo();
      if (loginInfo != null) {
        this.loginInfo = loginInfo;
      }
    },
    // 搜索
    search () {

    },
    // 商品轮播图往前
    specForward () {
      let node = this.$refs['spec-lh'];
      const imageLen = this.productInfo.images ? this.productInfo.images.length : 0;
      if (imageLen <= 5) {
        node.style.left = '0px'
        return;
      }
      let left = parseInt(node.style.left);
      // 最后一页的位移距离
      const minLeft = 0 - (76 * (imageLen - 5));
      // 跳最后一页
      if (left === 0) {
        left = minLeft;
        node.style.left = left + 'px';
        return;
      }
      //计算移动距离
      if (left <= -380) {
        left += 380;
      } else {
        left = 0;
      }
      node.style.left = left + 'px';
    },
    // 商品轮播图往后
    specBackForward () {
      let node = this.$refs['spec-lh'];
      const imageLen = this.productInfo.images.length;
      if (imageLen <= 5) {
        node.style.left = '0px'
        return;
      }
      let left = parseInt(node.style.left);
      // 最后一页的位移距离
      const minLeft = 0 - (76 * (imageLen - 5));
      // 已经最后一页,跳转第一页
      if (left <= minLeft) {
        left = 0;
        node.style.left = left + 'px';
        return;
      }
      //计算移动距离
      if (left - minLeft >= 380) {
        left -= 380;
      } else {
        left = minLeft;
      }
      node.style.left = left + 'px';
    },
    //
    specImageHover (index) {
      this.carouselIndex = index;
    },
    // 切换大图
    nextSpecImage () {
      let imageLen = this.productInfo.images ? this.productInfo.images.length : 0;
      if (imageLen === 0) {
        this.carouselIndex = -1;
        return;
      }
      let carouselIndex = this.carouselIndex;
      carouselIndex = (carouselIndex + 1) % imageLen;
      this.carouselIndex = carouselIndex;
      this.$forceUpdate();
      if (carouselIndex === 0) {
        this.$refs['spec-lh'].style.left = '0px';
        return;
      }
      let left = parseInt(this.$refs['spec-lh'].style.left);
      if (((carouselIndex + 1) * 76) + left > 380) {
        this.specBackForward();
      }
    },
    // 开启轮播
    carouselStart () {
      this.timer = setInterval(this.nextSpecImage, this.carouselInterval);
    },
    // 暂停轮播
    carouselStop () {
      clearInterval(this.timer);
    },
    // 变更选购数量
    handleChangeBuyNum (value) {
      console.log(value);
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
                    return;
                  }
                })
                return;
              }
            })
            return;
          }
        })
        return;
      }
    },
    // 更改收货地址省
    handleProvinceChange (province, index) {
      this.addrForm.province.name = province;
      this.addrForm.province.index = index;
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
      this.attrTabActiveName = 'city';
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
      this.attrTabActiveName = 'region';
    },
    // 更改收货地址区
    handleRegionChange (region, index) {
      this.addrForm.region.name = region;
      this.addrForm.region.index = index;
      this.showAddSelector = false;
    }
  },
  created () {
    this.getLoginInfo();

    this.initAddrData();
  },
  mounted () {
    this.carouselStart();
  },
  beforeDestroy () {
    this.carouselStop();
  }
}
</script>

<style lang="less" scoped>
.top {
  padding-top: 25px;
  height: 125px;
  position: relative;
  z-index: 9999;

  .top-wrap {
    width: 1200px;
    height: 100px;
    margin: 0 auto;
    position: relative;
    display: flex;

    .logo {
      height: 60px;
      margin-top: 10px;

      a {
        display: block;
        height: 60px;
        margin: 0 25px;

        img {
          height: 60px;
        }
      }
    }

    .search {
      margin-left: 100px;

      .search-wrap {
        position: relative;

        .search-bd {
          position: relative;
          background: #fff;
          border-radius: 20px;
          margin-top: 12px;
          border: 1px solid #ff5000;
          z-index: 30;

          .search-panel {
            position: relative;
            width: 700px;

            .search-button {
              position: absolute;
              width: 75px;
              height: 36px;
              border-radius: 20px;
              right: 3px;
              top: 50%;
              transform: translateY(-50%);
              overflow: hidden;

              .btn-search {
                font: 12px/1.5 tahoma, arial, "Hiragino Sans GB", "\5b8b\4f53",
                  sans-serif;
                font-size: 18px;
                font-weight: 700;
                color: #fff;
                cursor: pointer;
                height: 100%;
                border: none;
                width: 100%;
                background-image: linear-gradient(
                  to right,
                  #ff9000 0,
                  #ff5000 100%
                );
              }
            }

            .search-panel-fields {
              width: 630px;
              height: 40px;

              .search-combobox {
                font-size: 12px;

                .search-combobox-input-wrap {
                  .search-combobox-input {
                    border-radius: 20px;
                    padding-left: 15px;
                    text-indent: 10px;
                    height: 40px;
                    line-height: 40px;
                    width: 600px;
                    border: none;
                    outline: 0;
                    background: #fff;
                    color: #000;
                  }
                }
              }
            }
          }
        }

        .search-ft {
          margin-top: 5px;

          .hotword {
            .search-hots-lines {
              display: flex;
              justify-content: flex-start;

              .hots-item {
                font-size: 12px;
                margin-right: 5px;
                cursor: pointer;
                color: #3c3c3c;
              }

              .hots-item:hover {
                color: #ff5000;
              }
            }
          }
        }
      }
    }
  }
}
.crumb-wrap {
  background: #f2f2f2;
  width: 100%;
  height: 44px;
  .w {
    width: 1200px;
    margin: 0 auto;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .crumb {
      padding-top: 15px;
      font-size: 12px;
    }
    .contact {
      height: 44px;
      line-height: 44px;
      display: flex;
      justify-content: flex-end;
      .contact-item {
        margin-right: 15px;
        a {
          color: #666;
        }
        a:hover {
          color: #f40;
        }
        .iconfont {
          margin-right: 3px;
          font-size: 16px;
        }
        .icon-kefu {
          font-weight: 700;
          color: #0099ff;
        }
        .icon-collection-fill {
          color: #f40;
        }
      }
    }
  }
}
.product-intro {
  width: 1200px;
  margin: 10px auto;
  position: relative;
  display: flex;
  justify-content: space-between;
  .preview-wrap {
    width: 452px;

    .preview {
      .main-img {
        border: 1px solid #eee;
        margin-bottom: 20px;
        height: 452px;
        .el-image {
          width: 450px;
          height: 450px;
          img {
          }
          /deep/ .image-slot {
            height: 100%;
            width: 100%;
            text-align: center;
            line-height: 450px;
            .el-icon-picture-outline {
              font-size: 50px;
              color: #ccc;
            }
          }
        }
      }

      .spec-list {
        margin-bottom: 18px;
        position: relative;
        width: 450px;
        height: 58px;
        overflow: hidden;
        display: flex;
        justify-content: space-between;
        .spec-forward {
        }
        .spec-backward {
        }
        .spec-icon {
          font-size: 35px;
          line-height: 58px;
          color: #dfdfdf;
          font-weight: 1000;
          cursor: pointer;
        }
        .spec-icon:hover {
          color: #999999;
        }

        .spec-items {
          position: relative;
          width: 380px;
          height: 58px;
          overflow: hidden;
          .lh {
            height: 58px;
            position: absolute;
            top: 0px;
            overflow: hidden;
            zoom: 1;
            transition: left 0.5s ease;

            li {
              width: 58px;
              height: 58px;
              margin: 0 9px;
              cursor: pointer;
              float: left;

              .el-image {
                /deep/ .el-image__inner {
                  border: 2px solid #fff;
                }
              }

              /deep/ .image-slot {
                height: 100%;
                width: 100%;
                text-align: center;
                line-height: 58px;
                .el-icon-picture-outline {
                  font-size: 20px;
                  color: #ccc;
                }
              }

              img {
                width: 58px;
                height: 58px;
                border: 2px solid #fff;
                vertical-align: middle;
              }
            }
            .img-hover {
              img {
                border: 2px solid #f40;
              }
              .el-image {
                /deep/ .el-image__inner {
                  border: 2px solid #f40;
                }
              }
            }
          }
        }
      }
    }
  }
  .info-wrap {
    flex: 1;
    margin-left: 20px;
    .sku-name {
      font: 700 16px Arial, "microsoft yahei";
      color: #000;
      padding-top: 10px;
      line-height: 28px;
      margin-bottom: 5px;
    }

    .news {
      color: #e4393c;
      margin-bottom: 5px;
      .item {
        width: 100%;
        margin-bottom: 5px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        max-height: 1.5em;
        line-height: 1.5em;
        a {
          color: #5e69ad;
          text-decoration: underline;
          i {
            text-decoration: underline;
          }
        }
      }
    }

    .price-wrap {
      background: #f3f3f3;
      height: 42px;
      margin-bottom: 10px;
      .price-panel {
        .tit {
          color: #999;
          font-size: 12px;
          text-align: left;
          float: left;
          width: 69px;
          margin-left: 10px;
          line-height: 42px;
        }
        dd {
          margin-left: 70px;
        }
        .price {
          height: 42px;
          padding: 3px 0;
          line-height: 42px;
          .icon-money {
            color: #ff0036;
            font-size: 14px;
            font-family: Arial;
            -webkit-font-smoothing: antialiased;
          }
          .sku-price {
            font-size: 30px;
            color: #ff0036;
            font-weight: bolder;
            font-family: Arial;
            -webkit-font-smoothing: antialiased;
          }
        }
      }
    }
    .meta {
      margin: 5px 0;
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
        .meta-content {
          margin-left: 70px;
        }
      }
    }
    .meta-freight {
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
            .city {
            }
            .region {
            }
          }
          .postage-info {
          }
        }
        .address-selector {
          position: absolute;
          left: 70px;
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
            /deep/
              .el-tabs--border-card
              > .el-tabs__header
              .el-tabs__item.is-active {
              color: #f40;
              background-color: #fff;
              border-right-color: #dcdfe6;
              border-left-color: #dcdfe6;
            }
          }
        }
      }
    }
    .meta-weight {
      .meta-panel {
      }
    }
    .bounds-wrap {
      height: 40px;
      border-top: 1px dotted #dfdfdf;
      display: flex;
      overflow: hidden;
      padding: 10px 0;
      .bounds-item {
        flex: 1;
        color: #999999;
        text-align: center;
        span {
          color: #228800;
          margin-left: 3px;
          font-weight: 700;
        }
      }
      .grow-bounds {
        border-right: 1px dotted #ccc;
      }
    }

    .choose-attrs {
      .attr-group {
        line-height: 32px;
        margin-bottom: 3px;
        .dt {
          float: left;
          padding-left: 10px;
          font-family: simsun;
          color: #999;
        }
        .dd {
          margin-left: 70px;
          .attr-item {
            float: left;
            color: #666;
            background-color: #f7f7f7;
            margin-right: 7px;
            margin-bottom: 4px;
            border: 1px solid #ccc;
            padding: 0 13px;
            white-space: nowrap;
            cursor: pointer;
          }

          .attr-item:hover {
            border: 1px solid #f40;
          }

          .selected {
            border: 1px solid #f40;
          }
        }
      }
      .attr-group::after {
        content: "";
        display: block;
        height: 0;
        clear: both;
        visibility: hidden;
      }
    }

    .choose-btns {
      .choose-num {
        line-height: 40px;
        .dt {
          float: left;
          padding-left: 10px;
          font-family: simsun;
          color: #999;
        }
        .dd {
          margin-left: 70px;
          .attr-item {
            float: left;
            color: #666;
            background-color: #f7f7f7;
            margin-right: 7px;
            margin-bottom: 4px;
            border: 1px solid #ccc;
            padding: 0 13px;
            white-space: nowrap;
            cursor: pointer;
          }

          .attr-item:hover {
            border: 1px solid #f40;
          }

          .selected {
            border: 1px solid #f40;
          }
          span {
            font-family: arial;
            color: #878787;
          }
          .num-unit {
            margin-left: 5px;
          }
          .stock {
            margin-left: 10px;
          }
        }
        /deep/ .el-input-number {
          width: 100px;
        }
        /deep/ .el-input__inner:focus {
          border-color: #dcdfe6;
        }
        /deep/ .el-input-number__increase:hover {
          color: #f40;
        }
        /deep/ .el-input-number__decrease:hover {
          color: #f40;
        }
        /deep/
          .el-input-number__increase:hover:not(.is-disabled)
          ~ .el-input
          .el-input__inner:not(.is-disabled) {
          border-color: #f40;
        }
        /deep/
          .el-input-number__decrease:hover:not(.is-disabled)
          ~ .el-input
          .el-input__inner:not(.is-disabled) {
          border-color: #f40;
        }
      }
      .choose-action {
        padding: 10px 0 0 66px;
        margin: 10px 0 0;
        display: flex;

        .action-item {
          height: 38px;
          line-height: 38px;
          text-align: center;
          font-size: 16px;
        }
        .action-buy {
          margin-right: 10px;
          float: left;
          overflow: hidden;
          position: relative;
          width: 178px;
          background-color: #ffeded;
          border: 1px solid #ff0036;
          color: #ff0036;
          font-family: "Microsoft Yahei";
        }
        .action-basket {
          margin-right: 0;
          float: left;
          overflow: hidden;
          position: relative;
          width: 178px;
          background-color: #ff0036;
          border: 1px solid #ff0036;
          color: #fff;
          font-family: "Microsoft Yahei";
        }
        .iconfont {
          margin-right: 5px;
        }
      }
    }
  }
}
.summary-line {
  height: 0;
  overflow: hidden;
  border-bottom: 1px dotted #dfdfdf;
  margin-bottom: 15px;
}

.detail-wrap {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  .aside {
    width: 210px;
    .m-aside {
      .mt {
        padding: 10px;
        background-color: #f7f7f7;
        border: 1px solid #eee;
        overflow: hidden;
        zoom: 1;
        h3 {
          font: 700 14px "microsoft yahei";
          cursor: pointer;
          float: left;
          max-width: 150px;
          height: 18px;
          overflow: hidden;
        }
        h3:hover {
          color: #f40;
        }

        i {
          margin-left: 5px;
          float: left;
          color: #0099ff;
          font-weight: 700;
          font-size: 15px;
          display: block;
          height: 18px;
        }
      }
      .mc {
        padding: 0 10px;
        border: 1px solid #eee;
        border-top: none;
        background-color: #fff;
        .pop-score-summary {
          .btns {
            padding: 10px 0;
            display: flex;
            justify-content: space-between;
            .btn-item {
              width: 88px;
              height: 34px;
              line-height: 34px;
              text-align: center;
              padding: 0;
              font-size: 12px;
              border: 1px solid #ddd;
              background-color: #f8f8f8;
              color: #666;
              cursor: pointer;
              font-family: Microsoft YaHei;
              i {
                font-size: 20px;
                color: #ff0036;
                vertical-align: baseline;
              }
            }
          }
        }
      }
    }
  }
  .detail {
    width: 970px;
  }
}
</style>
