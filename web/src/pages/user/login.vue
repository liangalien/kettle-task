<template>
  <div>
    <el-card style="width: 350px; height: 250px; margin: 100px auto;">
      <div style="text-align: center; font-size: 22px; margin: 20px 0">欢迎使用</div>
      <el-form form="form" size="small" label-width="50px">
        <el-form-item label="账号">
          <el-input v-model="form.username" clearable @keydown.native.enter="login"></el-input>
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" clearable show-password @keydown.native.enter="login"></el-input>
        </el-form-item>

        <div style="text-align: center">
          <el-button size="small" @click="regShow = true">注册</el-button>
          <el-button :loading="loading" size="small" type="primary" @click="login">登录</el-button>
        </div>
      </el-form>
    </el-card>

    <register :visible.sync="regShow"/>

  </div>
</template>

<script>
  import Http from "@utils/http";
  import Common from "@utils/common";
  import Register from "./register";

  export default {
    name: "Login",
    components: {Register},
    data() {
      return {
        loading: false,
        regShow: false,
        form: {
          username: null,
          password: null
        }
      }
    },
    methods: {
      login() {
        if (!this.form.username || !this.form.password) {
          this.$message.error('请输入用户和密码');
          return;
        }

        this.loading = true;
        Http.easyPost('/api/user/login', this.form, resp => {
          let user = resp.body;
          Common.setStorage('token', user.token);
          Common.setStorage("username", user.username);
          Common.setStorage("nickname", user.nickname);
          this.$store.commit('setUsername', user.username);
          this.$store.commit('setNickname', user.nickname);

          let redirect = Common.getUrlParam('redirect');
          this.$router.push(redirect ? decodeURIComponent(redirect) : '/project');
        }, null, null, () => this.loading = false);
      }
    },
    created() {
      Common.removeStorage('token');
      Common.removeStorage("username");
      Common.removeStorage("nickname");
      this.$store.commit('setUsername', null);
      this.$store.commit('setNickname', null);
    }
  };
</script>

<style lang="less" scoped>
  .el-form {
    padding-right: 10px;
  }
</style>
