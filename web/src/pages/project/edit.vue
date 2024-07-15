<template>
  <div>
    <el-drawer
      :title="data.id ? '修改项目' : '新建项目'"
      class="kt-drawer"
      :visible.sync="visible"
      :size="600"
      :before-close="onClose"
      :modal="false"
      :destroy-on-close="true"
    >
      <div class="kt-drawer-body">
        <el-form  size="small" label-width="80px" label-position="right" ref="form" :model="form" :rules="rules">
          <el-form-item prop="key" label="项目KEY">
            <el-input :disabled="form.id ? true : false" v-model="form.key" placeholder="项目唯一标识符，创建后不允许修改" clearable />
          </el-form-item>
          <el-form-item prop="name" label="项目名">
            <el-input v-model="form.name" clearable/>
          </el-form-item>
        </el-form>
      </div>
      <div class="kt-drawer-footer">
        <el-button size="small" type="primary" @click="onSave">保存</el-button>
        <el-button size="small" @click="onClose">取消</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import Http from '@utils/http';


  export default {
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
          key: null,
          name: null,
        },
        rules: {
          key: [{ required: true, message: '不能为空', trigger: 'blur' }],
          name: [{ required: true, message: '不能为空', trigger: 'blur' }],
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
            let sendData = {
              id: this.form.id,
              key: this.form.key,
              name: this.form.name
            };

            Http.easyPost('/api/project/edit', sendData, resp => {
              this.$emit('success');
              this.onClose();
            });
          }
        });
      }
    },
    watch: {
      visible(val) {
        if (val) {
          if (this.data.id) {
            this.form = {
              id: this.data.id,
              key:  this.data.key,
              name: this.data.name
            }
          } else {
            this.form = {};
          }
        }
      }
    }
  };
</script>

<style lang="less">

</style>
