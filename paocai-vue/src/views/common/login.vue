<template>
	<log-reg :title="title">
		<div class="login-slot">
			<!-- 切换登录方式 -->
			<div class="login-switch-tab">
				<div class="switch-tab-item" :class="{ check: loginType == 0 }" @click="loginType = 0">密码登录</div>
				<div class="switch-tab-item" :class="{ check: loginType == 1 }" @click="loginType = 1">短信登录</div>
			</div>
			<!-- 密码登录 -->
			<div class="login-form-wrap" v-show="loginType == 0">
				<el-form class="login-form" :model="loginForm">
					<el-form-item>
						<div class="fm-field">
							<div class="fm-label-icon"><i class="iconfont icon-member" title="用户名"></i></div>
							<div class="fm-field-input"><el-input v-model="loginForm.loginId" placeholder="会员名/邮箱/手机号" size="large" style="width:100%;"></el-input></div>
						</div>
					</el-form-item>
					<el-form-item>
						<div class="fm-field">
							<div class="fm-label-icon"><i class="iconfont icon-password" title="密码"></i></div>
							<div class="fm-field-input"><el-input v-model="loginForm.password" placeholder="请输入登录密码" size="large" type="password"></el-input></div>
						</div>
					</el-form-item>
				</el-form>
			</div>
			<!-- 短信登录 -->
			<div class="login-form-wrap" v-show="loginType == 1">
				<el-form class="login-form" :model="loginForm">
					<el-form-item>
						<div class="fm-field">
							<div class="fm-label-icon"><i class="iconfont icon-member" title="手机号"></i></div>
							<div class="fm-field-input"><el-input v-model="loginForm.mobile" placeholder="请输入手机号" size="large" style="width:100%;" :maxlength="11"></el-input></div>
						</div>
					</el-form-item>
					<el-form-item>
						<div class="fm-field">
							<div class="fm-label-icon"><i class="iconfont icon-phone" title="验证码"></i></div>
							<div class="fm-field-input">
								<el-input v-model="loginForm.code" placeholder="请输入验证码" size="large" :maxlength="6">
									<template #suffix>
										<div class="fm-field-right"><span @click="getSmsCode">获取验证码</span></div>
									</template>
								</el-input>
							</div>
						</div>
					</el-form-item>
				</el-form>
			</div>
			<div class="submit" @click="login">登录</div>
			<div class="sns-links">
				<a href="#">
					<i class="iconfont icon-qq-login" style="color: #30a5dd;"></i>
					QQ
				</a>
				<a href="#">
					<i class="iconfont icon-wx-login" style="color: #28c445;"></i>
					微信
				</a>
				<a href="#">
					<i class="iconfont icon-zfb-login" style="color: #5a9ef7;"></i>
					支付宝
				</a>
				<a href="#">
					<i class="iconfont icon-gitee-login" style="color: #bb2124;"></i>
					Gitee
				</a>
			</div>
			<div class="links">
				<a href="#" class="links-op" v-show="loginType == 0">忘记密码</a>
				<a href="#" class="links-op" v-show="loginType == 0">忘记用户名</a>
				<router-link to="/regist" class="links-op">立即注册</router-link>
				<router-view></router-view>
			</div>
		</div>
	</log-reg>
</template>

<script>
import LogReg from 'components/login-regist.vue';
export default {
	components: { LogReg },
	data() {
		return {
			title: '欢迎登录',
			loginType: 0,
			loginForm: {
				loginId: '',
				password: '',
				mobile: '',
				code: ''
			}
		};
	},
	methods: {
		login() {
			console.log('login...');
		},
		getSmsCode() {
			console.log('getSmsCode...');
		}
	}
};
</script>

<style lang="less" scoped>
.login-slot {
	.login-switch-tab {
		display: flex;
		flex-direction: row;
		margin: 4px 8px 20px 8px;
		.switch-tab-item {
			font-size: 16px;
			color: #3c3c3c;
			height: 18px;
			line-height: 5px;
			margin: 9px 10px 0 0;
			font-weight: 700;
			cursor: pointer;
		}
		.check {
			border-bottom: 2px solid #000;
		}
	}
	.login-form-wrap {
		.login-form {
			.fm-field {
				width: 100%;
				display: flex;

				.fm-label-icon {
					width: 40px;
					height: 40px;
					line-height: 40px;
					text-align: center;
					color: #fff;
					background-color: #ccc;
					.iconfont {
						font-size: 20px;
						color: #fff;
					}
				}

				.fm-field-input {
					flex: 1;
					.el-input {
						&:deep(.el-input__inner) {
							border-radius: 0 10px 10px 0 !important;
						}
					}
				}

				.fm-field-right {
					height: 40px;
					line-height: 40px;
					font-size: 13px;
					float: right;
					span {
						cursor: pointer;
					}
				}
			}
		}
	}

	.submit {
		width: 100%;
		height: 40px;
		line-height: 40px;
		text-align: center;
		font-size: 18px;
		border-radius: 15px;
		background-color: #ff4400;
		color: #fff;
		cursor: pointer;
	}
	.sns-links {
		a {
			margin-top: 15px;
			color: #000;
			display: inline-block;
			text-decoration: none;
			outline: none;
			margin-right: 5px;
		}

		.iconfont {
			font-size: 16px;
			margin-right: 3px;
		}
	}

	.links {
		margin-top: 15px;
		display: flex;
		flex-direction: row;
		justify-content: end;
		.links-op {
			color: #000;
			text-decoration: none;
			margin-left: 10px;
		}
	}
}
</style>
