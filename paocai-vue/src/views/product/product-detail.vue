<template>
  <div class="product-detail">
    <el-tabs v-model="activeName">
      <el-tab-pane name="first"><span slot="label">商品介绍</span>
        <div class="desc-wrap">
          <div class="p-parameter">
            <el-descriptions
              :title="'品牌'+skuInfo.brandName"
              :column="3"
            >
              <el-descriptions-item label="商品名称">{{skuInfo.skuName}}
              </el-descriptions-item>
              <el-descriptions-item label="商品编号">{{skuInfo.skuId}}
              </el-descriptions-item>
              <el-descriptions-item label="商品毛重">{{skuInfo.weight}}kg
              </el-descriptions-item>
              <el-descriptions-item
                v-for="(parameter,index) in introduceParameters"
                :key="index"
                :label="parameter.attrName"
              >{{parameter.attrValue}}</el-descriptions-item>
            </el-descriptions>
            <div class="more-parameter"><span
                @click="activeName='second'">更多参数<i class="el-icon-d-arrow-right
"></i></span></div>
          </div>
          <div class="detail-content">
            <div class="ssd-module-wrap">
              <el-image
                v-for="url in descImages"
                :key="url"
                :src="url"
                lazy
              ></el-image>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane name="second"><span slot="label">规格与包装</span>
        <div class="attr-wrap">
          <div
            class="attr-group"
            v-for="(v1,i1) in groupAttrs"
            :key="i1"
          >
            <div class="tit">{{v1.groupName}}</div>
            <div class="content">
              <dl v-for="(v2,i2) in v1.attrs">
                <dd>{{v2.attrName}}</dd>
                <dt>{{v2.attrValue}}</dt>
              </dl>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane name="third"><span slot="label">售后保障</span>
        <div class="m-content guarantee-wrap">
          <div class="mt">
            <h3>售后保障</h3>
          </div>
          <div class="mc">
            <div class="guarantee-bd">
              <div class="guarantee-item">
                <div class="guarantee-tit"><i
                    class="iconfont icon-baozhang"></i><span>厂家服务</span></div>
                <div class="guarantee-content">
                  本商品质保周期为1年质保，在此时间范围内可提交维修申请，具体请以厂家服务为准。
                  如因质量问题或故障，凭厂商维修中心或特约维修点的质量检测证明，享受7日内退货，15日内换货，15日以上在质保期内享受免费保修等三包服务！
                  <br />
                  (注:如厂家在商品介绍中有售后保障的说明,则此商品按照厂家说明执行售后保障服务。)
                  您可以查询本品牌在各地售后服务中心的联系方式，<a href="">请点击这儿查询......</a>
                  <br /><br />
                  品牌官方网站：<a href="http://www.mi.com/">http://www.mi.com/</a>
                  <br>
                  售后服务电话：400-100-5678
                </div>
              </div>
              <div class="guarantee-item">
                <div class="guarantee-tit"><i
                    class="iconfont icon-baozhang"></i><span>泡菜承诺</span></div>
                <div class="guarantee-content">
                  泡菜平台卖家销售并发货的商品，由平台卖家提供发票和相应的售后服务。请您放心购买！
                  <br />
                  注：因厂家会在没有任何提前通知的情况下更改产品包装、产地或者一些附件，本司不能确保客户收到的货物与商城图片、产地、附件说明完全一致。只能确保为原厂正货！并且保证与当时市场上同样主流新品一致。若本商城没有及时更新，请大家谅解！
                </div>
              </div>
              <div class="guarantee-item">
                <div class="guarantee-tit"><i
                    class="iconfont icon-baozhang"></i><span>正品行货</span></div>
                <div class="guarantee-content">
                  泡菜商城向您保证所售商品均为正品行货，泡菜自营商品开具机打发票或电子发票。
                </div>
              </div>
              <div class="guarantee-item">
                <div class="guarantee-tit"><i
                    class="iconfont icon-baozhang"></i><span>无忧退货</span></div>
                <div class="guarantee-content">
                  客户购买泡菜自营商品7日内（含7日，自客户收到商品之日起计算），在保证商品完好的前提下，可无理由退货。（部分商品除外，详情请见各商品细则）
                </div>
              </div>
            </div>
            <div class="state">
              <div>
                <strong>权利声明：</strong>
                <p>泡菜上的所有商品信息、客户评价、商品咨询、网友讨论等内容，是泡菜重要的经营资源，未经许可，禁止非法转载使用。</p>
                <p>
                  <b>注：</b>本站商品信息均来自于合作方，其真实性、准确性和合法性由信息拥有者（合作方）负责。本站不提供任何保证，并不承担任何法律责任。
                </p>
              </div>
              <br>
              <div>
                <strong>价格说明：</strong>
                <p>
                  <b>泡菜价：</b>泡菜价为商品的销售价，是您最终决定是否购买商品的依据。
                </p>
                <p>
                  <b>划线价：</b>商品展示的划横线价格为参考价，并非原价，该价格可能是品牌专柜标价、商品吊牌价或由品牌供应商提供的正品零售价（如厂商指导价、建议零售价等）或其他真实有依据的价格；由于地区、时间的差异性和市场行情波动，品牌专柜标价、商品吊牌价等可能会与您购物时展示的不一致，该价格仅供您参考。
                </p>
                <p>
                  <b>折扣：</b>如无特殊说明，折扣指销售商在原价、或划线价（如品牌专柜标价、商品吊牌价、厂商指导价、厂商建议零售价）等某一价格基础上计算出的优惠比例或优惠金额；如有疑问，您可在购买前联系销售商进行咨询。
                </p>
                <p>
                  <b>异常问题：</b>商品促销信息以商品详情页“促销”栏中的信息为准；商品的具体售价以订单结算页价格为准；如您发现活动商品售价或促销信息有异常，建议购买前先联系销售商咨询。
                </p>
              </div>
              <br>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane name="fourth"><span slot="label">商品评价(5万+)</span>
        <div class="m-content comment-wrap">
          <div class="mt">
            <h3>商品评价</h3>
          </div>
          <div class="mc">
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'ProductDetail',
  props: {
    skuInfo: {
      type: Object
    },
    descImages: {
      type: Array
    },
    groupAttrs: {
      type: Array
    },
    introduceParameters: {
      type: Array
    }
  },
  data () {
    return {
      activeName: 'first'
    }
  },
  methods: {

  },
  mounted () {
  }
}
</script>

<style lang="less" scoped>
.product-detail {
  .desc-wrap {
    .p-parameter {
      padding-bottom: 10px;
      border-bottom: 1px solid #eee;
      .more-parameter {
        color: #005aa0;
        text-align: right;

        span {
          cursor: pointer;
        }
      }
    }
    .detail-content {
      .ssd-module-wrap {
        position: relative;
        margin: 0 auto;
        width: 750px;
        text-align: left;
        background-color: #fff;
        img {
          width: 750px;
          height: auto;
        }
      }
    }
  }

  .attr-wrap {
    margin: 10px 0;
    .attr-group {
      padding: 12px 0;
      line-height: 220%;
      color: #999;
      font-size: 12px;
      border-bottom: 1px solid #eee;
      display: flex;
      font-family: arial;
      .tit {
        width: 110px;
        font-weight: 400;
        width: 110px;
        text-align: right;
        float: left;
        font-size: 12px;
      }
      .content {
        flex: 1;
        dl {
          display: flex;
          dd {
            width: 160px;
            float: left;
            text-align: right;
            padding-right: 40px;
          }
          dt {
            flex: 1;
          }
        }
      }
    }
  }

  .m-content {
    .mt {
      padding: 10px;
      background-color: #f7f7f7;
      border: 1px solid #eee;
      h3 {
        font: 700 14px "microsoft yahei";
      }
    }
  }
  .guarantee-wrap {
    .mc {
      margin: 10px;
      .guarantee-bd {
        padding: 20px;
        .guarantee-item {
          .guarantee-tit {
            color: #e4393c;
            font-family: "microsoft yahei";

            .iconfont {
              margin-right: 15px;
              font-size: 30px;
              vertical-align: sub;
            }
            span {
              font-size: 16px;
              font-weight: 700;
            }
          }
          .guarantee-content {
            margin-left: 45px;
            display: block;
            padding-bottom: 30px;
            line-height: 18px;
          }
        }
      }
      .state {
        strong {
          color: #e4393c;
          display: inline-block;
          padding: 8px 0 3px;
        }
      }
    }
  }
  /deep/ .el-tabs__item.is-active {
    color: #f40;
  }
  /deep/ .el-tabs__item:hover {
    color: #f40;
    cursor: pointer;
  }
  /deep/ .el-tabs__active-bar {
    background-color: #f40;
  }
}
</style>