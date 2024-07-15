<template>
  <div>
    <el-drawer
      title="用户注册"
      class="kt-drawer"
      :visible.sync="visible"
      :size="480"
      :before-close="onClose"
      :modal="false"
      :destroy-on-close="true"
    >
      <div class="kt-drawer-body">
        <el-form  size="small" label-width="60px" label-position="right" ref="form" :model="form" :rules="rules">
          <el-form-item prop="username" label="账号">
            <el-input v-model="form.username" clearable auto-complete="off"/>
          </el-form-item>
          <el-form-item prop="nickname" label="昵称">
            <el-input v-model="form.nickname" clearable auto-complete="off"/>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input v-model="form.password" clearable show-password auto-complete="new-password"/>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="form.email" clearable/>
          </el-form-item>
          <el-form-item prop="phone" label="手机">
            <el-input v-model="form.phone" clearable/>
          </el-form-item>
        </el-form>
      </div>
      <div class="kt-drawer-footer">
        <el-button size="small" type="primary" @click="onSave">注册</el-button>
        <el-button size="small" @click="onClose">取消</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import Http from '@utils/http';


  export default {
    name: 'Register',
    props: {
      data: {
        type: Object,
        default() {
          return {}
        }
      },
      visible: Boolean
    },
    data() {
      return {
        treeData: [],
        form: {
        },
        rules: {
          username: [{ required: true, message: '不能为空', trigger: 'blur' }],
          nickname: [{ required: true, message: '不能为空', trigger: 'blur' }],
          password: [{ required: true, message: '不能为空', trigger: 'blur' }],
        }
      };
    },
    methods: {
      onClose() {
        this.$emit('update:visible', false);
      },
      onSave() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            Http.easyPost('/api/user/register', this.form, resp => {
              this.$message.success(`账号：${this.form.username}，注册成功`);
              this.onClose();
            });
          }
        });
      }
    }
  };
</script>

<style lang="less">

</style>
