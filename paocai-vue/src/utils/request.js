import Vue from 'vue'
import axios from 'axios'
import router from '@/router'

const baseUrl = process.env.NODE_ENV === 'development' ? 'http://paocai.mall.com/api/' : 'http://paocai.mall.com/api/'

const instance = axios.create({
  timeout: 1000 * 30,
  withCredentials: true,
  headers: { 'Content-Type': 'application/json; charset=utf-8' },
  baseURL: baseUrl
})

/**
 * 请求拦截
 */
instance.interceptors.request.use(config => {
  config.headers['token'] = Vue.cookie.get('token') // 请求头带上token
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
instance.interceptors.response.use(response => {
  if (response.data && response.data.code === 401) { // 401, token失效
    router.push({ name: 'login' })
  }
  return response
}, error => {
  return Promise.reject(error)
})

export const request = options => {
  return new Promise((resolve, reject) => {
    instance({
        ...options,
        url: options.url
      }).then(res => {
        resolve(res.data)
      })
      .catch(err => {
        reject(err.data)
      }).then(() => {
        // finally
      })
  })
}
