<template>
  <div id="searach-page">
    <common-header :login-info="loginInfo"></common-header>
    <div class="search-content">
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
                          placeholder=""
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
      <!-- 面包屑导航 -->
      <div class="crumbs-bar-wrap">
        <div class="crumbs-bar">
          <div
            class="crumbs-nav"
            v-if="catePath.length>0"
          >
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item class="cate-level-one">{{catePath[0].name}}
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="catePath.length > 1">
                <span
                  class=""
                  @mouseenter.self="addMenuOpen"
                  @mouseleave.self="removeMenuOpen"
                >
                  <span class="menu-drop">
                    <span class="menu-selected">{{catePath[1].name}}</span><i
                      class="iconfont icon-arrow-down"
                    ></i>
                  </span>
                  <div
                    class="menu-drop-main"
                    v-if="catalogs.catalogTwo && catalogs.catalogTwo.length >0"
                  >
                    <ul class="menu-drop-list">
                      <li
                        v-for="(item,index) in catalogs.catalogTwo"
                        :key="index"
                      >
                        <span>{{item.name}}</span>
                      </li>
                    </ul>
                  </div>
                </span>
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="catePath.length>2">
                <span
                  class=""
                  @mouseenter.self="addMenuOpen"
                  @mouseleave.self="removeMenuOpen"
                >
                  <span class="menu-drop">
                    <span class="menu-selected">{{catePath[2].name}}</span><i
                      class="iconfont icon-arrow-down"
                    ></i>
                  </span>
                  <div
                    class="menu-drop-main"
                    v-if="catalogs.catalogThree && catalogs.catalogThree.length >0"
                  >
                    <ul class="menu-drop-list">
                      <li
                        v-for="(item,index) in catalogs.catalogThree"
                        :key="index"
                      >
                        <span>{{item.name}}</span>
                      </li>
                    </ul>
                  </div>
                </span>
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
      </div>
      <div class="search-wrap-container">
        <div class="container">
          <!-- 属性筛选条件 -->
          <div class="selector">
            <!-- 品牌 -->
            <div class="selector-line s-brand">
              <div class="sl-wrap">
                <div class="sl-key">
                  <strong>品牌</strong>
                </div>
                <div class="sl-value">
                  <div class="sl-v-logos">
                    <ul class="value-list">
                      <li v-for="(item,index) in 16">
                        <a href=""><img
                            src="https://img30.360buyimg.com/popshop/jfs/t1/156223/15/22684/3178/61764659Ebfccc8c1/e84d202cc34091a5.jpg"
                            alt=""
                          />小米（MI）</a>
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
            <div class="selector-line">
              <div class="sl-wrap">
                <div class="sl-key">
                  <strong>CPU</strong>
                </div>
                <div class="sl-value">
                  <div class="sl-v-list">
                    <ul class="value-list">
                      <li><a href="">骁龙888</a></li>
                      <li><a href="">骁龙8 Gen 1</a></li>
                      <li><a href="">骁龙870</a></li>
                      <li><a href="">天玑8100</a></li>
                      <li><a href="">天玑9000</a></li>
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
            <div class="selector-line s-senior">
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
                        v-for="(item,index) in 6"
                        :key="index"
                        :class="{'':index === 0}"
                        @mouseenter.self="addTrigCurr(index)"
                        @mouseleave="removeTrigCurr(index)"
                      >
                        <span>充电功率</span>
                        <i class="iconfont icon-arrow-down"></i>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="sl-tab-cont">
                  <div
                    class="sl-tab-cont-item"
                    ref="sl-tab-cont-item"
                    v-for="(item,index) in 6"
                    :key="index"
                    style="display:none"
                    @mouseenter.self="addTrigCurr(index)"
                    @mouseleave="removeTrigCurr(index)"
                  >
                    <div class="sl-v-list">
                      <ul class="value-list">
                        <li>
                          <a href="">100W以上</a>
                        </li>
                        <li>
                          <a href="">50-100W</a>
                        </li>
                        <li>
                          <a href="">50W一下</a>
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
                    <div class="f-sort">
                      <a
                        href=""
                        class="curr"
                      >
                        <span class="fs-tit">综合</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a href="">
                        <span class="fs-tit">销量</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a href="">
                        <span class="fs-tit">评论数</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a href="">
                        <span class="fs-tit">新品</span>
                        <i class="iconfont icon-xiajiang"></i>
                      </a>
                      <a href="">
                        <span class="fs-tit">价格</span>
                        <div class="fs-price">
                          <i class="el-icon-caret-top"></i>
                          <i class="el-icon-caret-bottom"></i>
                        </div>
                      </a>
                    </div>
                  </div>
                  <div class="f-line">
                    <div class="f-feature">
                      <ul>
                        <li>
                          <el-checkbox v-model="paocaiDeliverChecked">泡菜物流
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="payOnDeliverChecked">货到付款
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="hasStockChecked">仅显示有货
                          </el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="newChecked">新品</el-checkbox>
                        </li>
                        <li>
                          <el-checkbox v-model="vipChecked">会员专享</el-checkbox>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
                <div
                  class="product-list"
                  v-show="products.length>0"
                >
                  <div
                    class="item"
                    v-for="(item,index) in products"
                    :key="index"
                  >
                    <a
                      href=""
                      class="hotsale-item"
                    >
                      <div class="img-wrapper">
                        <img :src="item.skuImg">
                      </div>
                      <h4>{{item.skuTitle}}</h4>
                      <p class="info">
                        <span class="price">
                          <i class="iconfont icon-money"></i>
                          {{item.skuPrice}}
                        </span>
                      </p>
                      <div class="comment">
                        <span class="comment-num">5万+</span>条评价
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
                      :total="400"
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
      keyword: '',
      searchTypeIndex: 0,
      searchTypeList: [{
        searchType: 0,
        searchTypeName: '宝贝'
      }, {
        searchType: 1,
        searchTypeName: '店铺'
      }],
      pageSizeList: [20, 30, 40, 50, 100],
      pageSize: 20,
      pageNum: 1,
      total: 400,
      totalPages: 0,
      // 商品果
      products: [],
      // 筛选属性
      attrs: [],
      // 相关分类路径的其他分类
      catalogs: {
        catId: 2,
        catLevel: 1,
        name: '手机',
        parentCid: 0,
        productCount: 0,
        showStatus: 1,
        sort: 0,
        catalogTwo: [
          {
            catId: 34,
            catLevel: 2,
            name: '手机通讯',
            parentCid: 2,
            productCount: 0,
            showStatus: 1,
            sort: 0
          },
          {
            catId: 35,
            catLevel: 2,
            name: '运营商',
            parentCid: 2,
            productCount: 0,
            showStatus: 1,
            sort: 0
          },
          {
            catId: 36,
            catLevel: 2,
            name: '手机配件',
            parentCid: 2,
            productCount: 0,
            showStatus: 1,
            sort: 0
          }
        ],
        catalogThree: [
          {
            catId: 225,
            catLevel: 3,
            name: '手机',
            parentCid: 34,
            productCount: 0,
            showStatus: 1,
            sort: 0
          },
          {
            catId: 226,
            catLevel: 3,
            name: '对讲机',
            parentCid: 34,
            productCount: 0,
            showStatus: 1,
            sort: 0
          }
        ]
      },
      // 已选择的分类路径
      catePath: [{
        catId: 2,
        catLevel: 1,
        name: '手机',
        parentCid: 0,
        productCount: 0,
        showStatus: 1,
        sort: 0
      }, {
        catId: 34,
        catLevel: 2,
        name: '手机通讯',
        parentCid: 2,
        productCount: 0,
        showStatus: 1,
        sort: 0
      }, {
        catId: 225,
        catLevel: 3,
        name: '手机',
        parentCid: 34,
        productCount: 0,
        showStatus: 1,
        sort: 0
      }],
      attrIds: [],
      paocaiDeliverChecked: false,
      payOnDeliverChecked: false,
      hasStockChecked: false,
      newChecked: false,
      vipChecked: false
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
    // 保存滚动值，这是兼容的写法
    handleScroll () {
      this.scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
    },
    // 搜索
    search () {
      console.log('search...')
    },
    handleSizeChange (val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`);
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
      console.log(items);
      for (let item of items) {
        item.style.display = "none";
      }
      items[index].style.display = "block"
    },
    removeTrigCurr (index) {
      this.$refs['trig-item'][index].classList.remove('trig-curr');
      this.$refs['sl-tab-cont-item'][index].style.display = "none";
    }
  },
  created () {
    this.getLoginInfo();
    const searchResult = require('../../assets/search_result.json');
    this.products = searchResult.products
    this.pageNum = searchResult.pageNum
    this.total = searchResult.total
    this.totalPages = searchResult.totalPages
    this.brands = searchResult.brands
    this.attrs = searchResult.attrs
    this.attrIds = searchResult.attrIds
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
      height: 35px;
      padding-top: 5px;
      font-family: "microsoft yahei";
      /deep/ .el-breadcrumb {
        display: flex;
        justify-content: flex-start;
        flex-direction: row;
        align-items: center;
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
        top: 20px;
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
            span {
              display: block;
              height: 25px;
              line-height: 25px;
              font-size: 12px;
              cursor: pointer;
            }
            span:hover {
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
                    border: 1px solid #fff;
                    height: 46px;
                    width: 114px;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    color: #005aa0;
                    line-height: 48px;
                    overflow: unset;
                    img {
                      margin: 5px 6px;
                      vertical-align: top;
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
                li:hover img {
                  margin-left: -114px;
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
              height: 37px;
              padding: 6px 8px;
              border-bottom: 1px solid #e7e3e7;
              background: #f9f9f9;
              zoom: 1;
            }
            .f-line-top {
              background: #f1f1f1;
            }
            .f-sort {
              float: left;
              margin-right: 13px;
              a {
                float: left;
                padding: 0 9px;
                height: 23px;
                border: 1px solid #ccc;
                line-height: 19px;
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

              .fs-price {
                float: right;
                height: 23px;
                display: flex;
                flex-direction: column;
                justify-content: flex-start;
                margin-left: 3px;
                i {
                  font-weight: 700;
                  height: 8px;
                }
                i:hover {
                  color: #f40;
                }
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
                  min-height: 22px;
                  max-height: 44px;
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
