import '@/assets/CSS/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from "axios";
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
// 引入echarts
import Echarts from 'vue-echarts'
import * as echarts from 'echarts'

const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8080/'
// axios.defaults.baseURL1 = 'https://mmffyy.icu'
// const  baseURL1 = 'https://mmffyy.icu/api'

const  baseURL = 'http://localhost:8080/'

axios.defaults.withCredentials = true


// 使用组件
app.component('e-charts', Echarts)
// 全局挂载 echarts
app.config.globalProperties.$echarts = echarts

app.config.globalProperties.$axios = axios

app.use(createPinia())
app.use(router).use(ElementPlus).use(Antd)

app.mount('#app')
// export {baseURL1};
export {baseURL};
