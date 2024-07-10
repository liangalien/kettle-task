import Vue from 'vue';
import VueRouter from 'vue-router';
import store from './store';

import ElementUI from 'element-ui';
import ElSelectTree from 'el-select-tree';
import 'element-ui/lib/theme-chalk/index.css';
import './theme/kt.less';

import Fullscreen from 'vue-fullscreen';

import ElementUIPro from 'element-ui-components-pro';
import Components from '@components/components';

import App from './App.vue';

import System from '@/config/system';


Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.use(ElementUIPro);
Vue.use(ElSelectTree);
Vue.use(Fullscreen);

const originalPush = VueRouter.prototype.push
// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

let myRouter = new VueRouter({
  mode: 'history',
  ignoreDuplicates: true,
  routes: System.routes
});

new Vue({
  el: '#root',
  store,
  render: h => h(App),
  router: myRouter
});
