import Vue from 'vue'
import router from '@/router'
import store from '@/store'
import cookies from 'vue-cookie'
import { request } from './request'


/**
 * 获取用户信息
 * @returns 用户信息
 */
export const getLoginInfo = async () => {
  // 从session中获取
  let loginInfo = sessionStorage.getItem('loginInfo')
  if (loginInfo != null && loginInfo != '') {
    return JSON.parse(loginInfo);
  }
  if (cookies.get('token') != null && cookies.get('token') != '') {
    let res = await request({
      url: 'auth/user/cookie',
      method: 'POST'
    })
    if (res.code !== 200) {
      return null;
    }
    loginInfo = res.data;
    // 放入session,JSON字符串
    sessionStorage.setItem('loginInfo', JSON.stringify(loginInfo))
    return loginInfo;
  }
  return null;
};

/**
 * 获取uuid
 */
export function getUUID () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

/**
 * 清除登录信息
 */
export function clearLoginInfo () {
  Vue.cookie.delete('token')
  store.commit('resetStore')
  router.options.isAddDynamicMenuRoutes = false
}
