<template>
  <div class="kt-log-drawer">
    <el-drawer
      class="kt-drawer"
      :visible.sync="visible"
      size="80%"
      :before-close="onClose"
      :modal="false"
      :destroy-on-close="true"
    >
      <template #title>
        <span>{{ `查看日志（${data.task_name} | key: ${data.key}）` }}</span>
        <el-button type="text" class="el-drawer__close-btn" icon="el-icon-refresh" @click="getLog"></el-button>
      </template>
      <div class="kt-drawer-body">
        <editor
          v-loading="loading"
          ref="editor"
          :value="content"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import Http from '@utils/http';
  import Editor from '@components/editor/editor';


  export default {
    components: {Editor},
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
        taskKey: null,
        content: null,
        loading: true
      };
    },
    methods: {
      onClose() {
        this.$emit('update:visible', false);
      },
      scrollBottom() {
        this.$refs.editor.$el.querySelector('.CodeMirror-scroll').scrollTop = 999999999;
      },
      getLog() {
        if (this.data.id) {
          this.content = null;
          this.loading = true;
          Http.easyGet('/api/runner/log', {id: this.data.id}, resp => {
            this.content = resp.body;
            this.$nextTick(() => this.scrollBottom());
          }, null, null, () => this.loading = false);
        }
      }
    },
    watch: {
      visible(val) {
        if (val) {
          this.getLog();
        }
      }
    },
  };
</script>

<style lang="less">
  .kt-log-drawer {
    .CodeMirror {
      height: calc(100vh - 100px);
    }
    .el-drawer__header {
      padding: 10px 20px;
      margin-bottom: 0;
    }
  }

</style>
