import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

import axios from "axios";
// 开发环境通过 Vite 代理，无需设置 baseURL
// axios.defaults.baseURL = "http://localhost:8080";

const app = createApp(App)
app.config.globalProperties.$http = axios;
app.use(ElementPlus)
app.use(router)
app.mount('#app')