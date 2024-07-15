<template>
  <div>
    <el-drawer
      title="项目变量"
      class="kt-drawer"
      :visible.sync="visible"
      :size="600"
      :before-close="onClose"
      :modal="false"
      :destroy-on-close="true"
    >
      <div class="kt-drawer-body">
          <div  class="kt-var-list" v-for="(v, index) in vars" :key="index" :value="v">
            <el-input v-model="v.name" size="small" placeholder="变量名称" auto-complete="off" clearable/>
            <el-input v-model="v.value" size="small" placeholder="变量值" auto-complete="off" clearable>
              <ep-switch slot="prefix" :value="v.isEncode" @change="val => v.isEncode=val"  active-text="密文" inactive-text="明文"/>
            </el-input>

            <div style="display: flex">
              <el-button type="primary" icon="el-icon-plus" circle size="small" @click="insert(index)"/>
              <el-button :disabled="vars.length <= 1" type="danger" icon="el-icon-minus" circle size="small" @click="remove(index)"/>
            </div>
          </div>
      </div>
      <div class="kt-drawer-footer">
        <el-button size="small" type="primary" :loading="loading" @click="onSave">保存</el-button>
        <el-button size="small" @click="onClose">取消</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import Http from '@utils/http';
  import EpSwitch from '@components/switch/index'

  export default {
    components: {EpSwitch},
    props: {
      projectId: Number,
      visible: Boolean
    },
    data() {
      return {
        loading: false,
        vars: []
      };
    },
    methods: {
      get() {
        Http.easyGet('/api/project/var/get', {project_id: this.projectId}, resp => {
          this.vars = resp.body.map(v => {
            return {
              ...v,
              isEncode: v.is_encode === 1 ? true : false
            }
          });
          if (this.vars.length === 0) {
            this.vars.push(this.newVar());
          }
        });
      },
      newVar() {
        return {
          name: null,
          value: null,
          isEncode: false,
        };
      },
      insert(index) {
        this.vars.splice(index + 1, 0, this.newVar());
      },
      remove(index) {
        this.vars.splice(index, 1);
      },
      onClose() {
        this.$emit('update:visible', false);
      },
      onSave() {
        let data = this.vars.filter(v => v.name && v.value).map(v => {
          return {
            ...v,
            isEncode: v.isEncode ? 1 : 0
          }
        });

        if (data.length === 0) {
          this.$message.error('无任何有效变量');
          return;
        }

        this.loading = true;
        Http.easyPost('/api/project/var/edit', {
          project_id: this.projectId,
          data
        }, resp => {
          this.$message.success('保存成功');
        }, null, null, () => this.loading = false);
      }
    },
    watch: {
      visible(val) {
        if (val) {
          this.get();
        }
      }
    }
  };
</script>

<style lang="less">

</style>
