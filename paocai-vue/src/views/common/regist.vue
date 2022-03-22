<template>
	<log-reg :title="title">
		<div class="regist" v-if="!registStatus">
			<div class="regist-form-wrap">
				<el-form class="regist-form" :model="registForm" label-position="top">
					<el-form-item label="用户名">
						<div class="fm-field">
							<div class="fm-field-input"><el-input v-model="registForm.mobile" placeholder="您的用户名或登录名" size="large" maxlength="11" style="width:100%;"></el-input></div>
						</div>
					</el-form-item>
					<el-form-item label="设置密码">
						<div class="fm-field">
							<div class="fm-field-input">
								<el-input v-model="registForm.password" placeholder="建议两种或以上的字符组合" size="large" type="password" show-password></el-input>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="确认密码">
						<div class="fm-field">
							<div class="fm-field-input"><el-input v-model="registForm.confirmPassword" placeholder="请再次输入密码" size="large" type="password" show-password></el-input></div>
						</div>
					</el-form-item>
					<el-form-item label="邮箱">
						<div class="fm-field">
							<div class="fm-field-input"><el-input v-model="registForm.email" placeholder="请输入邮箱" size="large" type="password" show-password></el-input></div>
						</div>
					</el-form-item>
				</el-form>
			</div>
			<div class="submit" @click="regist">注册</div>
			<div class="fm-agreement">
				<el-checkbox v-model="agreement"></el-checkbox>
				<label class="fm-agreement-text" @click="agreement = !agreement">
					已阅读并同意以下协议
					<a href="#" target="_blank">泡菜平台服务协议</a>
					、
					<a href="#" target="_blank">隐私权政策、法律声明</a>
					、
					<a href="#" target="_blank">法律声明</a>
					、
				</label>
			</div>
			<div class="links">
				<router-link to="/login" class="links-op">立即登录</router-link>
				<router-view></router-view>
			</div>
		</div>
		<div class="regit-success" v-else></div>
	</log-reg>
</template>

<script>
import LogReg from 'components/login-regist.vue';
export default {
	components: { LogReg },
	data() {
		return {
			title: '欢迎注册',
			registStatus: false,
			registForm: {
				mobile: '',
				password: '',
				confirmPassword: '',
				email: ''
			},
			agreement: false
		};
	},
	methods: {
		regist() {
			console.log('regist...');
			registStatus = true;
		}
	}
};
</script>

<style lang="less" scoped>
.regist {
	.regist-form-wrap {
		.regist-form {
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
							border-radius: 10px;
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
	.fm-agreement {
		margin-top: 10px;
		&:deep(.el-checkbox) {
			margin-right: 0px;
		}
		.fm-agreement-text {
			text-align: left;
			margin-left: 5px;
			a {
				text-decoration: none;
				color: #fd6020;
			}
		}
	}
}
</style>

<style>
.el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner{
  background-color:#fd6020;
  border-color: #fd6020;
}
.el-checkbox__input.is-checked + .el-checkbox__label {
  color: #fd6020;
}
.el-checkbox.is-bordered.is-checked{
  border-color: #fd6020;
}
.el-checkbox__input.is-focus .el-checkbox__inner{
  border-color:  #fd6020;
}

.el-checkbox__input .el-checkbox__inner:hover{
	border-color: #fd6020;
}
</style>

