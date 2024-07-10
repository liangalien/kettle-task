import EpSwitch from './switch/index';
import EpList from './list/index';
import EpProgress from './progress/progress';
import EpScrollbar from './scrollbar/index';
import { RouterLink } from 'vue-router';
import EpLink from './link/link';

export default {
  install(Vue) {
    Vue.component('EpSwitch', EpSwitch);
    Vue.component('EpList', EpList);
    Vue.component('EpProgress', EpProgress);
    Vue.component('EpScrollbar', EpScrollbar);
    Vue.component("RouterLink", RouterLink);
    Vue.component('EpLink', EpLink);
  }
};
