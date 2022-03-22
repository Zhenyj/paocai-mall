import Vue from 'vue'
import router from '../router/index.js'
import store from '../store/index.js'



/**
 * 清除登录信息
 */
export function clearLoginInfo () {
  Vue.cookie.delete('token')
  store.commit('resetStore')
  router.options.isAddDynamicMenuRoutes = false
}
