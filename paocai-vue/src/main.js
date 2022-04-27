import Vue from 'vue'
import App from './App.vue'
import '@/assets/css/index.css'
import '@/assets/css/iconfont.css'
import store from '@/store'
import VueCookie from 'vue-cookie'
import '@/element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import router from '@/router'
import { request } from '@/utils/request'
import { getLoginInfo, handleResponseMessage, hasText } from './utils/index'


Vue.use(VueCookie)

Vue.prototype.$request = request;
// 获取用户信息
Vue.prototype.$getLoginInfo = getLoginInfo;
Vue.prototype.$handleResponseMessage = handleResponseMessage;
Vue.prototype.hasText = hasText;

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {
    App
  }
})
