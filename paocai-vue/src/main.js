import { createApp } from 'vue'
import App from './App.vue'
import './assets/css/index.css'
import './assets/css/icon-font.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@element-plus/icons-vue'
import router from './router'
// import VueCookies from 'vue-cookies'

const app = createApp(App)

app.use(ElementPlus)
app.use(router)
// app.use(VueCookies)

app.mount('#app')