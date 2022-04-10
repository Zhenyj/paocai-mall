<template>
  <div>
    <common-header :login-info="loginInfo"></common-header>
    <div class="search-content">
      <!-- 顶部搜索栏 -->
      <div :class="[scrollTop>=250?'search-fixed':'top']">
        <div class="top-wrap">
          <div class="logo">
            <a href="#">
              <img src="../assets/logo.png">
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
      //屏幕高度
      scrollTop: 0,
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
  },
  created () {
    const searchResult = require('../assets/search_result.json');
    console.log(searchResult);
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
</style>
