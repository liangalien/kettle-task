<template>
  <div>
    <el-drawer
      :title="data.id ? '修改集成任务' : '添加集成任务'"
      class="kt-drawer"
      :visible.sync="visible"
      :size="600"
      :before-close="onClose"
      :modal="false"
      :destroy-on-close="true"
    >
      <div class="kt-drawer-body">
        <el-form  size="small" label-width="80px" label-position="right" ref="form" :model="form" :rules="rules">
          <el-form-item prop="repo" label="资源文件">
            <file-select
              v-model="form.repo"
              clearable
            />
          </el-form-item>
          <el-form-item prop="name" label="任务名称">
            <el-input v-model="form.name" clearable/>
          </el-form-item>
          <el-form-item prop="description" label="任务描述">
            <el-input v-model="form.description" clearable type="textarea" rows="3"/>
          </el-form-item>
          <el-form-item prop="logLevel" label="日志等级">
            <el-select v-model="form.logLevel" clearable style="width: 100%">
              <el-option :value="0" label="没有日志"/>
              <el-option :value="1" label="错误日志"/>
              <el-option :value="2" label="最小日志"/>
              <el-option :value="3" label="基本日志"/>
              <el-option :value="4" label="详细日志"/>
              <el-option :value="5" label="调试日志"/>
              <el-option :value="6" label="行级日志（非常详细）"/>
            </el-select>
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
  import Common from '@utils/common';
  import Http from '@utils/http';
  import FileSelect from '@components/select/fileRepo';


  export default {
    components: {FileSelect},
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
          repo: null,
          name: null,
          description: null,
          logLevel: 3
        },
        rules: {
          repo: [{ required: true, message: '不能为空', trigger: 'blur' }],
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
              project_id: 1,
              repo_id: this.form.repo.value,
              name: this.form.name,
              description: this.form.description,
              log_level: this.form.logLevel

            };

            Http.easyPost('/api/task/edit', sendData, resp => {
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
              repo: {value: this.data.repo_id, label: this.data.repo_name},
              name: this.data.name,
              description: this.data.description,
              logLevel: this.data.log_level
            }
          }
        }
      }
    }
  };
</script>

<style lang="less">

</style>
