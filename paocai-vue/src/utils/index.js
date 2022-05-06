import Vue from 'vue'
import router from '@/router'
import store from '@/store'
import cookies from 'vue-cookie'
import { request } from './request'
import { Message } from 'element-ui';

/**
 * 获取uuid
 */
export function getUUID () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

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
 * 处理请求结果并提示消息
 */
export const handleResponseMessage = (res, successDefaultMsg = '', errorDefaultMsg = '', errorInterrupt = true) => {
  if (res.code === 200) {
    if (successDefaultMsg.trim() !== '') {
      Message.success(res.msg && res.msg.trim() != 'success' ? res.msg : successDefaultMsg);
    }
  } else {
    if (errorDefaultMsg.trim() !== '') {
      Message.error(res.msg && res.msg.trim() != '' ? res.msg : errorDefaultMsg);
    }
    return errorInterrupt;
  }
  return false;
}

export const hasText = (s) => {
  return s != null && s.trim().length > 0;
}
