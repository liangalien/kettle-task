import Vue from 'vue';
import Vuex from 'vuex';
import Common from "@utils/common";

Vue.use(Vuex);


export default new Vuex.Store({
  // 数据，相当于data
  state: {
    username: Common.getStorage('username') || null,
    nickname: Common.getStorage('nickname') || null
  },
  getters: {

  },
  // 里面定义方法，操作state方发
  mutations: {
    setUsername(state, username) {
      state.username = username;
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
  },
  // 操作异步操作mutation
  actions: {

  },
  modules: {

  }
});
