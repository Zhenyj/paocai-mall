/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [{
  path: '/404',
  component: _import('common/404'),
  name: '404',
  meta: { title: '404未找到' }
}, {
  path: '/login',
  component: _import('common/login'),
  name: 'login',
  meta: { title: '登录-泡菜商城' }
}, {
  path: '/regist',
  component: _import('common/regist'),
  name: 'regist',
  meta: { title: '注册-泡菜商城' }
}, {
  path: '/search',
  component: _import('search/search'),
  name: 'search',
  meta: { title: '搜索' }
}, {
  path: '/product',
  component: _import('product/product'),
  name: 'product',
  meta: { title: '商品' }
}, {
  path: '/cart',
  component: _import('cart/cart'),
  name: 'cart',
  meta: { title: '泡菜商城-我的购物车' }
}, {
  path: '/order/order-confirm',
  component: _import('order/order-confirm'),
  name: 'orderConfirm',
  meta: { title: '确认订单-泡菜商城' }
}, {
  path: '/order/pay',
  component: _import('order/pay'),
  name: 'pay',
  meta: { title: '订单支付-泡菜商城' }
}, {
  path: '/member/my-paocai',
  component: _import('member/my-paocai'),
  name: 'myPaocai',
  meta: { title: '我的泡菜-泡菜商城' }
}, {
  path: '/member/address',
  component: _import('member/address/address'),
  name: 'address',
  meta: { title: '收货地址-泡菜商城' }
}, {
  path: '/member/user/user-info',
  component: _import('member/user/user-info'),
  name: 'userInfo',
  meta: { title: '账户信息-泡菜商城' }
}, {
  path: '/member/user/pwd-edit',
  component: _import('member/user/pwd-edit'),
  name: 'pwdEdit',
  meta: { title: '修改登录密码-泡菜商城' }
}, {
  path: '/member/order/order-list',
  component: _import('member/order/order-list'),
  name: 'orderList',
  meta: { title: '我的订单-泡菜商城' }
}]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('home'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
    {
      path: '/home',
      component: _import('home'),
      name: 'home',
      meta: { title: '泡菜商城' }
    }
  ]
}

const router = new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: globalRoutes.concat(mainRoutes)
})

export default router
