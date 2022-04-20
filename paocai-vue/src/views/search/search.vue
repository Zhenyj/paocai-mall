<template>
  <div id="searach-page">
    <common-header :login-info="loginInfo"></common-header>
    <div class="search-content">
      <!-- 顶部搜索栏 -->
      <div class="top">
        <div class="top-wrap">
          <div class="logo">
            <a href="/">
              <img src="../../assets/logo.png">
            </a>
          </div>
          <div class="search">
            <div class="search-wrap">
              <!-- 搜索框 -->
              <div class="search-bd">
                <div class="search-tab-header">
                  <div class="search-tab-list">
                    <ul class="ks-switchable-nav">
                      <li
                        v-for="(item,index) in searchTypeList"
                        :key="index"
                        class="search-tab-item"
                        :class="{'selected':index == searchTypeIndex}"
                        @click="selectSearchType(index)"
                      >
                        {{item.searchTypeName}}
                      </li>
                    </ul>
                    <div class="search-tab-icon">
                      <i class="iconfont icon-arrow-down"></i>
                    </div>
                  </div>
                </div>
                <div class="search-panel">
                  <div class="search-button">
                    <button
                      type="button"
                      class="btn-search"
                      @click="resetSearch"
                    >搜索</button>
                  </div>
                  <div class="search-panel-fields">
                    <div class="search-combobox">
                      <div class="search-combobox-input-wrap">
                        <input
                          class="search-combobox-input"
                          id="keyword"
                          v-model="searchParam.keyword"
                          placeholder=""
                          @keyup.enter="resetSearch"
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
      <!-- 面包屑导航和已筛选属性 -->
      <div class="crumbs-bar-wrap">
        <div class="crumbs-bar clearfix">
          <div
            class="crumbs-nav clearfix"
            v-if="searchParam.catalog3Id != '' && catalogs && catalogs.length > 0"
          >
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item class="cate-level-one">
                {{catalogs[0].catalogName}}
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="catalogs.length > 1">
                {{catalogs[1].catalogName}}
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="catalogs.length>2">
                <span
                  class=""
                  @mouseenter.self="addMenuOpen"
                  @mouseleave.self="removeMenuOpen"
                >
                  <span class="menu-drop">
                    <span
                      class="menu-selected">{{catalogs[2].catalogName}}</span><i
                      class="iconfont icon-arrow-down"
                    ></i>
                  </span>
                  <div
                    class="menu-drop-main"
                    v-if="catalogThreeList && catalogThreeList.length >0"
                  >
                    <ul class="menu-drop-list">
                      <li
                        v-for="(item,index) in catalogThreeList"
                        :key="index"
                      >
                        <a
                          :href="'/search?catalog3Id='+item.catalogId">{{item.catalogName}}</a>
                      </li>
                    </ul>
                  </div>
                </span>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <div class="attr-filter-wrap">
              <ul class="clearfix">
                <li v-show="brandList.length > 0">
                  <span
                    class="crumb-select-item"
                    :title="brandNames.join('、')"
                  >
                    <b>品牌：</b>
                    <em>{{brandNames.join('、')}}</em>
                    <i
                      class="el-icon-close"
                      @click="removeBrandFilter"
                    ></i>
                  </span>
                </li>
                <li
                  v-show="searchParam.skuPrice && searchParam.skuPrice.trim() != '' && searchParam.skuPrice.trim() != '_'"
                >
                  <span
                    class="crumb-select-item"
                    :title="searchParamSkuPrice"
                  >
                    <b>价格：</b>
                    <em>{{searchParamSkuPrice}}</em>
                    <i
                      class="el-icon-close"
                      @click="removePriceFilter"
                    ></i>
                  </span>
                </li>
                <li
                  v-for="(v,i) in attrList"
                  :key="i"
                >
                  <span
                    class="crumb-select-item"
                    :title="v.attrValue.join('、')"
                  >
                    <b>{{v.attrName+'：'}}</b>
                    <em>{{v.attrValue.join('、')}}</em>
                    <i
                      class="el-icon-close"
                      @click="removeAttrFilter(v.attrId)"
                    ></i>
                  </span>
                </li>
              </ul>
            </div>
          </div>
          <div
            class="crumbs-nav clearfix"
            v-else
          >
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item>
                全部结果
              </el-breadcrumb-item>
              <el-breadcrumb-item>
                <b>{{(searchParam.keyword && searchParam.keyword!='')?'"'+searchParam.keyword+'"':'""'}}</b>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <div class="attr-filter-wrap clearfix">
              <ul>
                <li v-show="brandList.length > 0">
                  <span
                    class="crumb-select-item"
                    :title="brandNames.join('、')"
                  >
                    <b>品牌：</b>
                    <em>{{brandNames.join('、')}}</em>
                    <i
                      class="el-icon-close"
                      @click="removeBrandFilter"
                    ></i>
                  </span>
                </li>
                <li
                  v-show="searchParam.skuPrice && searchParam.skuPrice.trim() != '' && searchParam.skuPrice.trim() != '_'"
                >
                  <span
                    class="crumb-select-item"
                    :title="searchParamSkuPrice"
                  >
                    <b>价格：</b>
                    <em>{{searchParamSkuPrice}}</em>
                    <i
                      class="el-icon-close"
                      @click="removePriceFilter"
                    ></i>
                  </span>
                </li>
                <li
                  v-for="(v,i) in attrList"
                  :key="i"
                >
                  <span
                    class="crumb-select-item"
                    :title="v.attrValue.join('、')"
                  >
                    <b>{{v.attrName+'：'}}</b>
                    <em>{{v.attrValue.join('、')}}</em>
                    <i
                      class="el-icon-close"
                      @click="removeAttrFilter(v.attrId)"
                    ></i>
                  </span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="search-wrap-container">
        <div class="container">
          <!-- 属性筛选条件 -->
          <div class="selector">
            <!-- 品牌 -->
            <div
              class="selector-line s-brand"
              v-show="brands.length > 0"
            >
              <div class="sl-wrap">
                <div class="sl-key">
                  <strong>品牌</strong>
                </div>
                <div class="sl-value">
                  <div class="sl-v-logos">
                    <ul class="value-list">
                      <li
                        v-for="(item,index) in brands"
                        :key="index"
                        :class="{'active':brandId.indexOf(item.brandId) != -1}"
                        @click="filterBrand(item)"
                      >
                        <a><span class="brand-img"><img
                              :src="item.brandImg"
                              :alt="item.bandName"
                            /></span><span
                            class="brand-name">{{item.brandName}}</span></a>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="sl-ext">
                  <div class="sl-e-more">
                    更多<i class="iconfont icon-arrow-down"></i>
                  </div>
                  <div class="sl-e-multiple">
                    多选<i class="iconfont icon-add"></i>
                  </div>
                </div>
              </div>
            </div>
            <!-- 主要筛选属性 -->
            <div
              class="selector-line"
              v-for="(v1,i1) in attrs"
              :key="i1"
              v-if="i1 < 4"
            >
              <div class="sl-wrap">
                <div class="sl-key">
                  <strong>{{v1.attrName}}</strong>
                </div>
                <div class="sl-value">
                  <div class="sl-v-list">
                    <ul class="value-list">
                      <li
                        v-for="(v2,i2) in v1.attrValue"
                        :key="i2"
                        @click="filterAttrs(v1.attrId,v1.attrName,v2)"
                      ><a>{{v2}}</a></li>
                    </ul>
                  </div>
                </div>
                <div class="sl-ext">
                  <div class="sl-e-more">
                    更多<i class="iconfont icon-arrow-down"></i>
                  </div>
                  <div class="sl-e-multiple">
                    <i class="iconfont icon-add"></i>多选
                  </div>
                </div>
              </div>
            </div>
            <!-- 更多属性选择 -->
            <div
              class="selector-line s-senior"
              v-if="attrs.length>4"
            >
              <div class="sl-wrap">
                <div class="sl-key">
                  <span>高级选项</span>
                </div>
                <div class="sl-value">
                  <div class="sl-v-tab">
                    <div class="sl-tab-trigger">
                      <div
                        class="trig-item"
                        ref="trig-item"
                        v-for="(v1,i1) in attrs"
                        :key="i1"
                        v-if="i1 >= 4"
                        @mouseenter.self="addTrigCurr(i1)"
                        @mouseleave="removeTrigCurr(i1)"
                      >
                        <span>{{v1.attrName}}</span>
                        <i class="iconfont icon-arrow-down"></i>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="sl-tab-cont">
                  <div
                    class="sl-tab-cont-item"
                    ref="sl-tab-cont-item"
                    v-for="(v1,i1) in attrs"
                    :key="i1"
                    style="display:none"
                    @mouseenter.self="addTrigCurr(i1)"
                    @mouseleave="removeTrigCurr(i1)"
                  >
                    <div class="sl-v-list">
                      <ul class="value-list">
                        <li
                          v-for="(v2,i2) in v1.attrValue"
                          key="i2"
                        >
                          <a href="">{{v2}}</a>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 商品 -->
          <div class="main">
            <div class="m-list">
              <div class="ml-wrap">
                <!-- 过滤选项 -->
                <div class="filter">
                  <div class="f-line f-line-top">
                    <!-- 排序 -->
                    <div class="f-sort">
                      <a
                        :class="{'curr':sortField==='hotScore'}"
                        ref="f-sort-type"
                        @click="changeSort('hotScore','desc')"
                      >
                        <span class="fs-tit">综合</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a
                        :class="{'curr':sortField==='saleCount'}"
                        ref="f-sort-type"
                        @click="changeSort('saleCount','desc')"
                      >
                        <span class="fs-tit">销量</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a
                        :class="{'curr':sortField==='commentCount'}"
                        ref="f-sort-type"
                        @click="changeSort('commentCount','desc')"
                      >
                        <span class="fs-tit">评论数</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a
                        :class="{'currSkuPrice':sortField==='skuPrice'}"
                        ref="f-sort-type"
                      >
                        <span class="fs-tit">价格</span>
                        <div class="fs-price">
                          <i
                            class="el-icon-caret-top"
                            :class="{'active':sortField==='skuPrice' && sortType === 'asc'}"
                            @click="changeSort('skuPrice','asc')"
                          ></i>
                          <i
                            class="el-icon-caret-bottom"
                            :class="{'active':sortField==='skuPrice' && sortType === 'desc'}"
                            @click="changeSort('skuPrice','desc')"
                          ></i>
                        </div>
                      </a>
                    </div>
                    <!-- 价格范围 -->
                    <div
                      class="price-scope"
                      ref="price-scope"
                    >
                      <div
                        class="f-price-set"
                        @mouseenter="addPriceFocus"
                      >
                        <div class="price-input price-min">
                          <el-input
                            placeholder="￥"
                            v-model="min"
                            min="0"
                          >
                          </el-input>
                        </div>
                        <span>-</span>
                        <div class="price-input price-max">
                          <el-input
                            placeholder="￥"
                            v-model="max"
                          >
                          </el-input>
                        </div>
                      </div>
                      <div
                        class="f-price-edit"
                        @mouseleave="removePriceFocus"
                      >
                        <el-button
                          type="danger"
                          plain
                          @click="clearPriceScope"
                        >清空</el-button>
                        <el-button
                          type="primary"
                          plain
                          @click="changePriceScope"
                        >确认</el-button>
                      </div>
                    </div>
                  </div>
                  <div class="f-line">
                    <div class="f-feature">
                      <ul>
                        <li>
                          <el-checkbox v-model="paocaiLogistics">泡菜物流
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="payOnDeliver">货到付款
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="hasStock">仅显示有货
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="newProduct">新品
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="vip">会员专享</el-checkbox>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div
                  id="product-list"
                  class="product-list"
                  v-loading="loading"
                  v-loading.body="loading"
                  v-loading.lock="loading"
                  element-loading-text="拼命加载中"
                  element-loading-spinner="el-icon-loading"
                >
                  <div
                    class="item"
                    v-for="(item,index) in products"
                    :key="index"
                    @click="toDetail(item.skuId)"
                  >
                    <a class="hotsale-item">
                      <div class="img-wrapper">
                        <img :src="item.skuImg">
                      </div>
                      <h4 v-html="item.skuTitle"></h4>
                      <p class="info">
                        <span class="price">
                          <i class="iconfont icon-money"></i>
                          {{item.skuPrice | priceFilter}}
                        </span>
                      </p>
                      <div class="comment">
                        <span
                          class="comment-num">{{item.commentCount}}</span>条评价
                      </div>
                      <div class="store">
                        <a class="store-name">泡菜官方自营店</a>
                        <b class="iconfont icon-kefu"></b>
                      </div>
                    </a>
                  </div>
                </div>
                <div class="page">
                  <div class="block">
                    <el-pagination
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      background
                      :current-page="pageNum"
                      :page-sizes="pageSizeList"
                      :page-size="pageSize"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="total"
                    >
                    </el-pagination>
                  </div>
                </div>
              </div>
            </div>
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
export default {
  name: 'search',
  components: { CommonHeader, CommonFooter },
  data () {
    return {
      loginInfo: {
        id: ''
      },
      // 请求参数
      searchParam: {
        keyword: '',
        catalog3Id: ''
      },
      searchTypeIndex: 0,
      searchTypeList: [{
        searchType: 0,
        searchTypeName: '宝贝'
      }, {
        searchType: 1,
        searchTypeName: '店铺'
      }],
      pageSizeList: [20, 30, 50, 100],
      pageSize: 20,
      pageNum: 1,
      total: 0,
      totalPages: 0,
      // 商品
      products: [],
      // 筛选属性
      attrs: [],
      // 已选择
      attrList: [],
      // 已选择的属性id
      attrIds: [],
      // 相关分类路径的其他分类
      catalogs: [],
      // 相关品牌信息
      brands: [],
      // 已选择的品牌
      brandList: [],
      // 已选择的品牌id
      brandId: [],
      // 已选择的品牌名
      brandNames: [],
      min: '',
      max: '',
      // 泡菜物流
      paocaiLogistics: false,
      // 货到付款
      payOnDeliver: false,
      // 仅显示有货
      hasStock: false,
      // 新品
      newProduct: false,
      // 会员专享
      vip: false,
      sortField: 'hotScore',
      sortType: 'desc',
      loading: true,
      timer: null
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
    // 选择搜索类型
    selectSearchType (index) {
      let obj = this.searchTypeList[0];
      this.searchTypeList[0] = this.searchTypeList[index];
      this.searchTypeList[index] = obj;
      this.$forceUpdate();
    },
    // 构造请求参数
    buildSearchParam () {
      let searchParam = this.$route.query;
      searchParam = {
        ...searchParam,
        brandId: this.brandId,
        attrs: this.attrList,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        hasStock: this.hasStock,
        paocaiLogistics: this.paocaiLogistics,
        payOnDeliver: this.payOnDeliver,
        newProduct: this.newProduct,
        vip: this.vip
      }
      // 价格区间
      let skuPrice = '_';
      if (this.min != '') {
        skuPrice = this.min + skuPrice;
      }
      if (this.max != '') {
        skuPrice = skuPrice + this.max;
      }
      if (skuPrice != '_') {
        searchParam.skuPrice = skuPrice;
      }
      // 排序
      if (this.sortField != '' && this.sortType != '') {
        searchParam.sort = this.sortField + '_' + this.sortType
      }
      this.searchParam = searchParam;
      this.search();
    },
    // 搜索
    async search () {
      if (this.searchParam.keyword == '' && (!this.searchParam.catalog3Id || this.searchParam.catalog3Id == '')) {
        return;
      }
      this.loading = true;
      try {
        let res = await this.$request({
          url: 'search/item',
          data: this.searchParam,
          method: 'POST'
        });
        let data = res.data;
        this.products = data.products;
        this.pageNum = data.pageNum;
        this.total = data.total;
        this.totalPages = data.totalPages;
        this.brands = data.brands;
        this.catalogs = data.catalogs;
        this.catalogThreeList = data.catalogThreeList;
        let attrs = data.attrs;
        let attrIds = data.attrIds;
        if (attrIds.length > 0) {
          // 过滤掉已选择的属性
          attrs = attrs.filter(attr => {
            return attrIds.indexOf(attr.attrId) === -1;
          });
        }
        this.attrs = attrs;
        this.attrIds = attrIds;
      } finally {
        setTimeout(() => {
          this.loading = false;
        }, 300);
      }
    },
    resetSearch () {
      window.location.href = "/search?keyword=" + this.searchParam.keyword;
    },
    // 过滤品牌
    filterBrand (brand) {
      let brandList = this.brandList;
      let index = -1;
      brandList.forEach((v1, i1) => {
        if (v1.brandId === brand.brandId) {
          index = i1;
        }
      })
      if (index === -1) {
        brandList.push(brand);
      } else {
        // 截去品牌id
        brandList = brandList.slice(0, index).concat(brandList.slice(index + 1, brandList.length));
      }
      this.brandList = brandList;
      let brandId = [];
      let brandNames = [];
      // 品牌
      if (brandList.length > 0) {
        this.brandList.forEach((v, i) => {
          brandId.push(v.brandId);
          brandNames.push(v.brandName);
        });
      }
      this.brandId = brandId;
      this.searchParam.brandId = brandId;
      this.brandNames = brandNames;
      this.search();
    },
    removeBrandFilter () {
      this.brandList = [];
      this.brandId = [];
      this.searchParam.brandId = [];
      this.search();
    },
    // 筛选属性
    filterAttrs (attrId, attrName, attrValue) {
      // 单选
      let attr = {};
      attr.attrId = attrId;
      attr.attrName = attrName;
      attr.attrValue = [attrValue];
      let attrList = this.attrList;
      attrList.push(attr);
      this.attrList = attrList;
      this.searchParam.attrs = attrList;
      this.search();
    },
    removeAttrFilter (attrId) {
      let attrList = this.attrList;
      attrList = attrList.filter(item => {
        return item.attrId != attrId;
      })
      this.attrList = attrList;
      this.searchParam.attrs = attrList;
      this.search();
    },
    removePriceFilter () {
      this.min = '';
      this.max = '';
      this.searchParam.skuPrice = '';
      this.search();
    },
    changeSort (sortField, sortType) {
      this.sortField = sortField;
      this.sortType = sortType;
      this.searchParam.sort = sortField + '_' + sortType;
      this.search();
    },
    handleSizeChange (val) {
      this.pageSize = val;
      this.searchParam.pageSize = val;
      this.search();
    },
    handleCurrentChange (val) {
      this.pageNum = val;
      this.searchParam.pageNum = val;
      this.search();
    },
    // 分类路径选择显示
    addMenuOpen (e) {
      e.target.classList.add("menu-drop-open");
    },
    // 分类选择隐藏
    removeMenuOpen (e) {
      e.target.classList.remove("menu-drop-open");
    },
    // 显示选择高级选项
    addTrigCurr (index) {
      this.$refs['trig-item'][index].classList.add("trig-curr");
      let items = this.$refs['sl-tab-cont-item']
      for (let item of items) {
        item.style.display = "none";
      }
      items[index].style.display = "block"
    },
    removeTrigCurr (index) {
      this.$refs['trig-item'][index].classList.remove('trig-curr');
      this.$refs['sl-tab-cont-item'][index].style.display = "none";
    },
    toDetail (skuId) {
      if (skuId && skuId != '') {
        this.$router.push({ name: 'product', query: { skuId: skuId } });
      }
    },
    addPriceFocus () {
      this.$refs['price-scope'].classList.add('f-price-focus');
    },
    removePriceFocus () {
      this.$refs['price-scope'].classList.remove('f-price-focus');
    },
    // 确认筛选价格变动
    changePriceScope () {
      let searchParam = this.searchParam;
      let skuPrice = '_';
      if (this.min !== '') {
        skuPrice = this.min + skuPrice;
      }
      if (this.max !== '') {
        skuPrice = skuPrice + this.max;
      }
      if (skuPrice !== '_' && skuPrice !== searchParam.skuPrice) {
        searchParam.skuPrice = skuPrice;
        this.searchParam = searchParam;
        this.search();
      }
    },
    // 移除价格筛选
    clearPriceScope () {
      this.min = '';
      this.max = '';
      let searchParam = this.searchParam;
      if (searchParam.skuPrice && searchParam.skuPrice.trim() !== '') {
        searchParam.skuPrice = '';
        this.searchParam = searchParam;
        this.search();
      }
    }
  },
  created () {
    this.getLoginInfo();
    this.buildSearchParam();
  },
  computed: {
    searchParamSkuPrice () {
      if (!this.searchParam.skuPrice) {
        return "";
      }
      let skuPrice = this.searchParam.skuPrice.trim();
      if (skuPrice === '' || skuPrice === '_') {
        return "";
      }
      let s = skuPrice.split('_');
      if (s[0] === '') {
        return '0-' + s[1];
      } else if (s[1] === '') {
        return s[0] + '以上'
      } else {
        return s[0] + '-' + s[1];
      }
    }
  },
  watch: {
    hasStock (val) {
      this.searchParam.hasStock = val;
      this.search();
    },
    'searchParam.skuPrice' () {
      this.search();
    },
    min (val) {
      if (this.timer != '') {
        clearTimeout(this.timer);
      }
      this.timer = setTimeout(() => {
        if (val < 0) {
          console.log(val);
          this.min = 0;
        }
      }, 500);
    }
  },
  filters: {
    priceFilter (price) {
      return Math.floor(price * 100) / 100;
    }
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

          .search-tab-header {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 200;
            width: 75px;
            height: 40px;
            background-color: #fff;
            border-top-left-radius: 20px;
            border-bottom-left-radius: 20px;

            .search-tab-list {
              li {
                line-height: 40px;
                height: 40px;
                color: #3c3c3c;
                width: 75px;
                background-color: #fff;
                text-align: center;
                cursor: pointer;
                display: none;
              }

              .selected {
                display: block;
                color: #333;
                background-image: none;
                border-top-left-radius: 20px;
                border-bottom-left-radius: 20px;
              }

              li:hover {
                color: #ff5000;
                font-weight: 600;
                font-size: 13px;
                display: block;
              }

              .selected:hover {
                color: #333;
                font-size: 12px;
                font-weight: 400;
                background-image: none;
              }
            }

            .search-tab-list:hover li {
              display: block;
            }

            .search-tab-list:hover .selected {
              border-bottom-left-radius: 0;
            }

            .search-tab-icon {
              position: absolute;
              top: 12px;
              right: 3px;

              i {
                font-size: 12px;
              }
            }
          }

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
                  margin-left: 75px;
                  border-left: 1px solid #f5f5f5;

                  .search-combobox-input {
                    text-indent: 10px;
                    height: 40px;
                    line-height: 40px;
                    width: 624px;
                    border: none;
                    border-radius: 0 20px 20px 0;
                    padding-right: 90px;
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

.search-fixed {
  width: 100%;
  position: fixed;
  left: 0;
  top: 0;
  height: 60px;
  padding: 10px 0;
  background-color: #fff;
  border-bottom: solid 1px #ff5000;
  overflow: visible;
  z-index: 9999;

  .top-wrap {
    width: 1200px;
    height: 40px;
    margin: 0 auto;
    position: relative;
    display: flex;

    .logo {
      height: 30px;
      margin-top: 5px;

      a {
        display: block;
        height: 30px;
        margin: 0 25px;

        img {
          height: 30px;
        }
      }
    }

    .search {
      margin-left: 180px;

      .search-wrap {
        .search-bd {
          position: relative;
          background: #fff;
          border-radius: 20px;
          border: 1px solid #ff5000;
          z-index: 30;

          .search-tab-header {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 200;
            width: 75px;
            height: 40px;
            background-color: #fff;
            border-top-left-radius: 20px;
            border-bottom-left-radius: 20px;

            .search-tab-list {
              li {
                line-height: 40px;
                height: 40px;
                color: #3c3c3c;
                width: 75px;
                background-color: #fff;
                text-align: center;
                cursor: pointer;
                display: none;
              }

              .selected {
                color: #333;
                background-image: none;
                border-top-left-radius: 20px;
                border-bottom-left-radius: 20px;
                display: block;
              }

              li:hover {
                color: #ff5000;
                font-weight: 600;
                font-size: 13px;
              }

              .selected:hover {
                color: #333;
                font-size: 12px;
                font-weight: 400;
                background-image: none;
              }

              .search-tab-icon {
                position: absolute;
                top: 12px;
                right: 3px;
                cursor: pointer;

                i {
                  font-size: 12px;
                }
              }
            }

            .search-tab-list:hover li {
              display: block;
            }

            .search-tab-list:hover .selected {
              border-bottom-left-radius: 0;
            }
          }

          .search-panel {
            position: relative;

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
                  margin-left: 75px;
                  border-left: 1px solid #f5f5f5;

                  .search-combobox-input {
                    text-indent: 10px;
                    height: 40px;
                    line-height: 40px;
                    width: 460px;
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
          display: none;

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

.crumbs-bar-wrap {
  width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  .crumbs-bar {
    position: relative;
    padding-top: 15px;
    margin-bottom: 5px;
    padding-left: 15px;
    z-index: 6;
    .crumbs-nav {
      padding-top: 5px;
      font-family: "microsoft yahei";
      // display: flex;
      // flex-direction: row;
      // justify-content: flex-start;
      // flex-wrap: wrap;
      /deep/ .el-breadcrumb {
        height: 35px;
        line-height: 35px;
        float: left;
        margin-right: 15px;
      }
      .cate-level-one {
        font-size: 18px;
        font-weight: 700;
        cursor: pointer;
      }
      /deep/ .cate-level-one:hover .el-breadcrumb__inner {
        color: #f40;
      }

      .menu-drop {
        border: 1px solid #ddd;
        padding: 3px 5px;
        font-size: 12px;
        cursor: pointer;
        position: relative;
      }
      .menu-selected:hover {
        color: #f40;
      }

      .el-breadcrumb__item {
        position: relative;
      }
      .menu-drop-main {
        position: absolute;
        top: 29px;
        left: 0;
        width: 340px;
        display: none;
        background-color: #fff;
        border: 1px solid #f40;
        padding: 10px 0 10px 8px;
        .menu-drop-list {
          display: flex;
          flex-direction: row;
          flex-wrap: wrap;

          li {
            width: 100px;
            margin-right: 10px;
            a {
              display: block;
              height: 25px;
              line-height: 25px;
              font-size: 12px;
              cursor: pointer;
            }
            a:hover {
              color: #f40;
            }
          }
        }
      }
      .menu-drop-open {
        .menu-drop {
          z-index: 99;
          border-color: #f40;
          border-bottom: 1px solid #fff;
          padding: 3px 5px;
          font-size: 12px;
          cursor: pointer;
          position: relative;
          background-color: #fff;
        }
        .iconfont::before {
          color: #f40;
          content: "\e745";
        }
        .menu-drop-main {
          z-index: 9;
          display: block;
        }
      }
      .el-breadcrumb__inner:hover .menu-drop-main {
        z-index: 9;
        display: block;
      }
      .menu-drop-main:hover {
        z-index: 9;
        display: block;
      }
      /deep/ .el-icon-arrow-right {
        font-size: 14px;
      }
      .attr-filter-wrap {
        ul {
          li {
            float: left;
            height: 35px;
            display: flex;
            align-items: center;
          }
          .crumb-select-item {
            float: left;
            position: relative;
            border: 1px solid #ddd;
            font-size: 12px;
            cursor: pointer;
            background: #f3f3f3;
            padding-right: 25px;
            padding-left: 5px;
            margin-right: 10px;
            height: 24px;
            line-height: 24px;

            span {
              height: 24px;
              line-height: 24px;
            }

            b {
              font-weight: 400;
            }
            em {
              color: #e4393c;
              font-style: normal;
            }
            .el-icon-close {
              position: absolute;
              right: -1px;
              top: -1px;
              height: 24px;
              width: 24px;
              line-height: 24px;
              text-align: center;
              font-size: 14px;
            }
          }
          .crumb-select-item:hover {
            border-color: #e4393c;
            background: #fff;
          }

          .crumb-select-item:hover .el-icon-close {
            background-color: #e4393c;
            color: #fff;
          }
        }
      }
    }
  }
}

.search-wrap-container {
  width: 1200px;
  margin: 0 auto;
  .container {
    .selector {
      border-top: 1px solid #ddd;
      background: #fff;
      margin-bottom: 10px;
      font: 12px/150% tahoma, arial, Microsoft YaHei, Hiragino Sans GB,
        "\u5b8b\u4f53", sans-serif;
      .selector-line {
        .sl-wrap {
          line-height: 34px;
          border-bottom: 1px solid #ddd;
          background: #f3f3f3;
          position: relative;
          display: flex;
          .sl-key {
            width: 100px;
            padding-left: 10px;
            white-space: nowrap;
            overflow: hidden;
          }

          .sl-value {
            flex: 1;
            background-color: #fff;
            padding-left: 10px;
            padding-right: 150px;
            overflow: hidden;
            padding-top: 4px;
            .sl-v-logos {
              .value-list {
                display: flex;
                flex-wrap: wrap;
                min-height: 50px;
                max-height: 95px;
                margin: 10px 0;
                overflow: hidden;
                padding-top: 1px;
                li {
                  height: 48px;
                  padding: 0;
                  border: 1px solid #ddd;
                  margin: -1px -1px 0 0;
                  background: #fff;
                  text-align: center;
                  overflow: hidden;
                  width: 114px;

                  a {
                    display: block;
                    height: 46px;
                    width: 114px;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    color: #005aa0;
                    line-height: 48px;
                    overflow: unset;
                    span {
                      display: inline-block;
                      height: 46px;
                      width: 114px;
                      img {
                        height: 34px;
                        max-width: 100px;
                        margin: 5px 6px;
                        vertical-align: top;
                      }
                    }
                  }
                  a:hover {
                    text-decoration: none;
                    color: #f40;
                  }
                }
                li:hover {
                  position: relative;
                  z-index: 5;
                  border-color: #f40;
                  box-shadow: 2px 2px 3px rgb(0 0 0 / 12%);
                }
                li:hover a {
                  transform: translateX(-100%);
                }
                .active {
                  z-index: 5;
                  border-color: #f40;
                }
              }
            }
            .sl-v-list {
              .value-list {
                display: flex;
                height: 30px;
                li {
                  margin-right: 50px;
                  margin-bottom: 4px;
                  height: 26px;
                  line-height: 26px;
                  a {
                    white-space: nowrap;
                    zoom: 1;
                    color: #005aa0;
                  }
                  a:hover {
                    color: #f40;
                  }
                }
              }
            }
            .sl-v-tab {
              height: 31px;
              .sl-tab-trigger {
                height: 30px;
                display: flex;
                overflow: hidden;
                .trig-item {
                  cursor: pointer;
                  color: #005aa0;
                  height: 25px;
                  line-height: 22px;
                  padding: 0 3px 0 6px;
                  border: 1px solid #ddd;
                  background: #fff;
                  margin-right: 15px;
                }
                .trig-curr {
                  border-color: #999;
                  border-bottom: medium none;
                  text-decoration: none;
                  position: relative;
                  z-index: 99;
                }
              }
            }
          }
          .sl-ext {
            background-color: #fff;
            position: absolute;
            top: 5px;
            right: 10px;
            height: auto;
            line-height: 22px;
            overflow: hidden;
            zoom: 1;
            i {
              color: #ccc;
            }
            .sl-e-more {
              cursor: pointer;
              float: left;
              margin-right: 10px;
              position: relative;
              width: 47px;
              height: 25px;
              line-height: 25px;
              padding: 0 7px 0 4px;
              background: #fff;
              visibility: visible;
              color: #333;
              border: 1px solid #ddd;
              i {
                position: absolute;
                top: 0;
                right: 2px;
                display: block;
                background-position: 4px 7px;
              }
            }
            .sl-e-more:hover {
              color: #f40;
            }
            .sl-e-more:hover i {
              color: #f40;
            }
            .sl-e-multiple {
              cursor: pointer;
              float: left;
              height: 25px;
              line-height: 25px;
              border: 1px solid #ddd;
              padding: 0 3px 0 18px;
              position: relative;
              background: #fff;
              color: #333;
              i {
                font-size: 10px;
                position: absolute;
                display: block;
                font-style: normal;
                left: 3px;
                top: 0;
                background-position: 0 -63px;
              }
            }
            .sl-e-multiple:hover {
              color: #f40;
            }
            .sl-e-multiple:hover i {
              color: #f40;
            }
          }
          .sl-tab-cont {
            width: 1200px;
            .sl-tab-cont-item {
              position: absolute;
              left: 0;
              top: 28px;
              width: 1200px;
              border: 1px solid #999;
              padding: 10px;
              background: #fff;
              box-shadow: 1px 1p x 1px rgb(153 153 153 / 30%);
              z-index: 9;
              // display: none;
              .sl-v-list {
                overflow: hidden;
                zoom: 1;
                padding-top: 4px;
                .value-list {
                  height: auto;
                  overflow: hidden;
                  zoom: 1;
                  position: relative;
                  display: flex;
                  li {
                    margin-right: 50px;
                    margin-bottom: 4px;
                    height: 26px;
                    line-height: 26px;
                    a {
                      white-space: nowrap;
                      zoom: 1;
                      color: #005aa0;
                    }
                    a:hover {
                      color: #f40;
                    }
                  }
                }
              }
            }
          }
        }
      }
      .s-brand {
      }
      .s-senior {
        .sl-wrap {
          display: block;
          .sl-key {
            float: left;
          }
        }
      }
    }
    .main {
      margin-bottom: 10px;
      .m-list {
        float: right;
        width: 100%;
        .ml-wrap {
          position: relative;
          padding-bottom: 30px;
          .filter {
            position: relative;
            z-index: 4;
            border-top: 1px solid #ddd;
            margin-bottom: 5px;
            .f-line {
              height: auto;
              padding: 6px 8px;
              border-bottom: 1px solid #e7e3e7;
              background: #f9f9f9;
              zoom: 1;
              overflow: hidden;
            }
            .f-line-top {
              background: #f1f1f1;
              display: flex;
              z-index: 9999;
            }
            .f-sort {
              margin-right: 13px;
              display: flex;
              justify-content: flex-start;

              a {
                display: inline-block;
                padding: 0 9px;
                height: 25px;
                line-height: 25px;
                border: 1px solid #ccc;
                margin-right: -1px;
                background: #fff;
                color: #333;
                .icon-xiajiang {
                  font-size: 8px;
                }
              }
              a.curr {
                border-color: #f40;
                background: #f40;
                color: #fff;
              }

              a:hover {
                border-color: #f40;
              }

              a:hover > .fs-tit {
                color: #f40;
              }

              a:hover > .icon-xiajiang {
                color: #f40;
              }

              a.curr:hover > .fs-tit {
                color: #fff !important;
              }
              a.curr:hover > .icon-xiajiang {
                color: #fff !important;
              }

              a.currSkuPrice {
                border-color: #f40;
              }

              .fs-price {
                float: right;
                height: 23px;
                display: flex;
                flex-direction: column;
                justify-content: center;
                margin-left: 3px;

                i {
                  font-size: 12px;
                  font-weight: 700;
                  height: 10px;
                  width: 10px;
                }
                i:hover {
                  color: #f40;
                }
                .active {
                  color: #f40;
                }
              }
            }

            .price-scope {
              margin-left: 20px;
              position: relative;
              height: 25px;
              .f-price-set {
                display: flex;
                width: 136px;
                height: 25px;
                position: relative;
                z-index: 99;
                .price-input {
                  /deep/ .el-input__inner {
                    display: inline;
                    height: 25px;
                    width: 60px;
                    border-color: #ccc;
                    height: 25px;
                    line-height: 25px;
                    border: 1px solid #ccc;
                    padding: 3px;
                  }
                }
                span {
                  display: inline-block;
                  height: 25px;
                  width: 5px;
                  line-height: 25px;
                  margin: 0 5px;
                }
              }
              .f-price-edit {
                display: none;
                z-index: 9;
                position: relative;
                top: -31px;
                left: -12px;
                background-color: #fff;
                width: 270px;
                height: 37px;
                padding: 5px 10px 0 160px;
                border: 1px solid #999;
                box-shadow: 0px 3px 3px rgb(0 0 0 / 10%);
                zoom: 1;

                .el-button {
                  height: 25px;
                  width: 40px;
                  font-size: 10px;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                }
                .el-button--danger {
                  float: left;
                }
                .el-button--primary {
                  float: right;
                }
              }
            }

            .f-price-focus {
              .f-price-edit {
                display: block;
              }
            }

            .f-feature {
              height: 25px;
              ul {
                display: flex;
                .el-checkbox:hover {
                  color: #f40;
                }
                /deep/ .el-checkbox__label {
                  font-size: 6px;
                  padding-left: 5px;
                  margin-right: 10px;
                }
                /deep/ .el-checkbox__inner:hover {
                  border-color: #f40;
                }
                /deep/ .is-checked .el-checkbox__inner {
                  background-color: #f40;
                  border-color: #f40;
                }
                /deep/ .is-checked .el-checkbox__label {
                  color: #f40;
                }
                /deep/ li:hover .el-checkbox__inner {
                  border-color: #f40;
                }
              }
            }
          }

          .product-list {
            width: 100%;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            min-height: 200px;
            .item {
              border-radius: 12px;
              padding: 7px 7px 15px 7px;
              margin: 0 0 2px 7px;
              color: #6c6c6c;
              width: 230px;
              position: relative;

              .hotsale-item {
                display: block;

                .img-wrapper {
                  width: 215px;
                  height: 215px;
                  border-radius: 12px;
                  background-color: rgba(0, 0, 0, 0.2);
                  overflow: hidden;

                  img {
                    display: block;
                    border-radius: 12px;
                    width: 100%;
                    height: 100%;
                    transition: opacity 0.2s;
                    background: #fff;
                    opacity: 0.9;
                  }
                }

                h4 {
                  margin-top: 9px;
                  line-height: 22px;
                  height: 44px;
                  font-size: 16px;
                  color: #111111;
                  font-weight: normal;
                  transition: color 0.3s;
                  overflow: hidden;
                }

                .info {
                  margin-top: 10px;
                  height: 22px;
                  line-height: 22px;
                  vertical-align: bottom;
                  border-radius: 0 0 12px 12px;

                  .price {
                    float: left;
                    color: #f40;
                    font-size: 20px;
                    height: 22px;
                    line-height: 22px;

                    .icon-money {
                      font-size: 14px;
                    }
                  }
                }
                .comment {
                  margin-top: 5px;
                  color: #a7a7a7;
                  font-weight: 400;
                  .comment-num {
                    color: #646fb0;
                    font-weight: 700;
                  }
                }
                .store {
                  margin-top: 5px;
                  font-size: 12px;
                  color: #999;

                  .store-name {
                    display: inline-block;
                    max-width: 140px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                  }
                  .icon-kefu {
                    font-size: 12px;
                    vertical-align: top;
                    color: #f40;
                    margin-left: 3px;
                  }
                }
              }
            }
            .item:hover {
              transition: all 0.5s;
              box-shadow: 0 19px 39px 0 rgb(0 0 0 / 12%);
            }

            .item:hover h4 {
              color: #ff5500;
            }

            /deep/ .el-icon-loading {
              font-size: 40px;
            }
            /deep/ .el-loading-text {
              font-size: 24px;
            }
          }
          .page {
            width: 100%;
            margin-top: 30px;
            text-align: center;
          }
        }
      }
    }
  }
}
</style>
