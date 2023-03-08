import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

//引入全局样式
import "@/assets/css/global.css";

//引入自定义icon图片
import "@/assets/iconfont/iconfont.css";

//引入font-awesome
import "font-awesome/css/font-awesome.min.css";

//引入Element
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import zhCn from 'element-plus/es/locale/lang/zh-cn'

createApp(App).use(store).use(router).use(ElementPlus, {locale: zhCn}).mount("#app");
