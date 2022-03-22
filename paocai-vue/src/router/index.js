import { createRouter, createWebHashHistory } from 'vue-router'
import Home from 'views/home.vue'
import Login from 'views/common/login.vue'
import Regist from 'views/common/regist.vue'
import Error4 from 'views/common/404.vue'

// 全局路由
const globalRoutes = [
  {
    path: '/404',
    component: Error4,
    name: '404',
    meta: {
      title: '404未找到'
    }
  },
  {
    path: '/login',
    component: Login,
    name: 'login',
    meta: {
      title: '登录'
    }
  },
  {
    path: '/regist',
    component: Regist,
    name: 'regist',
    meta: {
      title: '注册'
    }
  }
]

// 主入口路由
const mainRoutes = {
  path: '/',
  redirect: '/home',
  meta: {
    title: '主入口整体布局'
  },
  children: [
    {
      path: '/home',
      component: Home,
      name: 'home',
      meta: {
        title: '首页'
      }
    }
  ]
}

const router = createRouter({
  mode: 'history',
  history: createWebHashHistory(),
  routes: globalRoutes.concat(mainRoutes)
});

export default router;