<template>
	<div class="common-header">
		<div class="site-nav">
			<div class="site-nav-bd">
				<ul class="site-nav-bd-left">
					<li class="side-nav-menu site-nav-multi-menu site-nav-switch">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top">
									{{currentRegionName}}
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd site-nav-menu-list">
							<ul class="site-nav-region-list site-nav-menu-bd-panel">
								<li v-for="(item,index) in regionList" :key="index" class="site-nav-region-item"
									@click="selectRegion(index)">
									{{item.regionName}}
								</li>
							</ul>
						</div>
					</li>
					<li class="side-nav-menu" v-if="!isLogin">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top" style="color: #FD6020;">
									亲，请登录
								</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu" v-if="!isLogin">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top">
									免费注册
								</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu site-nav-multi-menu" v-if="isLogin">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top" id="site-nav-login-info-nick"
									class="site-nav-login-info-nick">
									{{(!loginInfo.nickname || loginInfo.nickname == '') ?loginInfo.username:loginInfo.nickname}}
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd">
							<div class="site-nav-menu-bd-login-info">
								<div class="login-op-wrap">
									<a href="#" class="login-op-item">账号管理</a>
									<span style="margin: 0 3px;">|</span>
									<a href="#" class="login-op-item">退出</a>
								</div>
								<div class="login-info-wrap">
									<div class="login-info-left">
										<div class="user-head-image">
											<img v-if="!loginInfo.header && loginInfo.header != ''"
												:src="loginInfo.header">
											<img v-else src="../../assets/images/default_user.png">
										</div>
									</div>
									<div class="login-info-right">
										<div class="user-info">
											<ul>
												<li class="info-item">泡泡值：{{loginInfo.growth}}</li>
												<li class="info-item">普通会员</li>
											</ul>
										</div>
									</div>
								</div>
								<div class="login-bottom">
									<a href="#">查看你的专属权益</a>
								</div>
							</div>
						</div>
					</li>
				</ul>
				<ul class="site-nav-bd-right">
					<li class="side-nav-menu site-nav-multi-menu">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top">
									我的淘宝
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd site-nav-menu-list">
							<div class="site-nav-menu-bd-panel">
								<a href="#" target="_top">已买到的宝贝</a>
								<a href="#" target="_top">我的足迹</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#" target="_top">
									<i class="iconfont icon-cart"></i>购物车
								</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu">
						<div class="site-nav-menu-hd site-nav-multi-menu">
							<div class="site-nav-user">
								<a href="#" target="_top">
									<i class="iconfont icon-collection-fill"></i>收藏夹
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd site-nav-menu-list">
							<div class="site-nav-menu-bd-panel">
								<a href="#" target="_top">收藏的宝贝</a>
								<a href="#" target="_top">收藏的店铺</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#">
									商品分类
								</a>
							</div>
						</div>
					</li>
					<li class="site-nav-pipe">
						|
					</li>
					<li class="side-nav-menu site-nav-multi-menu">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#">
									泡菜卖家中心
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd site-nav-menu-list">
							<div class="site-nav-menu-bd-panel">
								<a href="#" target="_top">开店入驻</a>
								<a href="#" target="_top">已卖出的宝贝</a>
								<a href="#" target="_top">出售中的宝贝</a>
								<a href="#" target="_top">卖家服务市场</a>
								<a href="#" target="_top">卖家培训中心</a>
								<a href="#" target="_top">体验中心</a>
								<a href="#" target="_top">电商学习中心</a>
							</div>
						</div>
					</li>
					<li class="side-nav-menu site-nav-multi-menu">
						<div class="site-nav-menu-hd">
							<div class="site-nav-user">
								<a href="#">
									联系客服
								</a>
								<i class="iconfont icon-arrow-down"></i>
							</div>
						</div>
						<div class="site-nav-menu-bd site-nav-menu-list">
							<div class="site-nav-menu-bd-panel">
								<a href="#" target="_top">消费者客服</a>
								<a href="#" target="_top">卖家客服</a>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data () {
			return {
				isLogin: true,
				loginInfo: {
					id: 1,
					levelId: 1,
					username: '甄英俊',
					nickname: '',
					header: '',
					growth: 999
				},
				currentRegionId: 0,
				currentRegionName: '中国大陆',
				regionList: [{
					regionId: 0,
					regionName: '中国香港'
				}, {
					regionId: 1,
					regionName: '中国澳门'
				}, {
					regionId: 2,
					regionName: '中国台湾'
				}, {
					regionId: 3,
					regionName: '全球'
				}, {
					regionId: 4,
					regionName: '马来西亚'
				}, {
					regionId: 5,
					regionName: '澳大利亚'
				}, {
					regionId: 6,
					regionName: '新加坡'
				}, {
					regionId: 7,
					regionName: '新西兰'
				}, {
					regionId: 8,
					regionName: '加拿大'
				}, {
					regionId: 9,
					regionName: '日本'
				}, {
					regionId: 10,
					regionName: '越南'
				}, {
					regionId: 11,
					regionName: '泰国'
				}, {
					regionId: 12,
					regionName: '菲律宾'
				}, {
					regionId: 13,
					regionName: '韩国'
				}, ]
			}
		},
		methods: {
			selectRegion (index) {
				if (index >= 0 && index < this.regionList.length) {
					this.currentRegionName = this.regionList[index].regionName
					this.currentRegionId = this.regionList[index].regionId
				}
			}
		},
		created () {}
	}
</script>

<style lang="less" scoped>
	.common-header {
		.site-nav {
			z-index: 10000;
			width: 100%;
			background-color: #f5f5f5;
			border-bottom: 1px solid #eee;

			.site-nav-bd {
				width: 1200px;
				height: 35px;
				margin: 0 auto;
				background-color: #f5f5f5;
				-webkit-backface-visibility: hidden;
				display: flex;
				justify-content: space-between;
				font-size: 12px;

				ul {
					list-style-type: none;
				}

				.site-nav-bd-left {
					display: flex;
					justify-content: flex-start;
					align-items: center;
				}

				.site-nav-bd-right {
					display: flex;
					justify-content: flex-end;
					align-items: center;
				}
			}

			.side-nav-menu {
				.site-nav-menu-hd {
					z-index: 10002;
					position: relative;
					padding: 0 6px;
					height: 35px;
					line-height: 35px;
					cursor: pointer;
					vertical-align: middle;

					a {
						text-decoration: none;
						color: #6C6C6C;
						margin-right: 7px;
					}

					.site-nav-login-info-nick {
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
						max-width: 98px;
					}

					.iconfont {
						font-size: 12px;
					}

					.icon-collection-fill {
						font-size: 13px;
						margin-right: 5px;
					}

					.icon-cart {
						font-size: 14px;
						color: #FD6020;
						margin-right: 5px;
					}
				}

				.site-nav-menu-hd:hover>.site-nav-user>a {
					color: #FD6020;
				}

				.site-nav-menu-hd:hover+.site-nav-menu-bd {
					display: block;
				}

				.site-nav-menu-bd:hover {
					display: block;
				}

				.site-nav-menu-bd {
					position: relative;
					z-index: 10001;
					display: none;
					margin: -1px;

					.site-nav-menu-bd-panel {
						position: absolute;
						left: 0;
						top: 0;
						line-height: normal;
						background-color: #FFFFFF;

						a {
							display: block;
							text-decoration: none;
							padding: 0 5px;
							line-height: 28px;
							white-space: nowrap;
							color: #6C6C6C;
						}

						.site-nav-region-item {
							width: 242px;
							height: 29px;
							line-height: 29px;
							padding-left: 8px;
							cursor: pointer;
						}

						.site-nav-region-item:hover {
							background-color: #F5F5F5;
						}

						a:hover {
							background-color: #F5F5F5;
						}
					}

					.site-nav-menu-bd-login-info {
						width: 300px;
						position: absolute;
						top: 0;
						left: 0;
						background-color: #FFFFFF;
						padding: 10px;

						.login-op-wrap {
							width: 100%;
							text-align: right;

							.login-op-item {
								display: inline-block;
								text-decoration: none;
								color: #6C6C6C;
								margin: 0 3px;
							}

							.login-op-item:hover {
								color: #FD6020;
							}
						}

						.login-info-wrap {
							margin-top: 15px;
							display: flex;

							.login-info-left {
								margin: 0 20px;

								.user-head-image {
									img {
										width: 70px;
										height: 70px;
										border-radius: 50%;
									}
								}
							}

							.login-info-right {
								flex: 1;
								font-size: 13px;
								padding-left: 10px;
								display: flex;
								flex-direction: column;
								justify-content: center;

								.info-item {
									height: 20px;
									line-height: 20px;
								}
							}
						}

						.login-bottom{
							margin-top: 15px;
							a{
								display: block;
								width: 100%;
								height: 30px;
								line-height: 30px;
								text-align: center;
								text-decoration: none;
								font-size: 13px;
								color: #6C6C6C;
								background-color: #FFF0E8;
								border-radius: 10px;
							}
							a:hover{
								color: #FD6020;
							}
						}
					}
				}

				.site-nav-menu-list {
					.site-nav-menu-bd-panel {
						padding: 8px 0;
					}
				}

				.site-nav-region-list {
					max-height: 270px;
					overflow-y: scroll;
				}
			}

			.site-nav-multi-menu:hover {
				background-color: #FFFFFF;
			}

			.site-nav-pipe {
				height: 35px;
				line-height: 35px;
				padding: 0 5px;
				color: #DDD;
			}
		}
	}
</style>
