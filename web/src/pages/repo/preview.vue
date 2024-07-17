<template>
  <el-image-viewer v-if="show" :url-list="[url]" :z-index="20000" :on-close="imageClose"/>
</template>

<script>
  import ElImageViewer from "element-ui/packages/image/src/image-viewer";
  import Http from "@utils/http";

  export default {
    name: "RepoPreview",
    props: {
      value: Boolean,
      repoId: Number,
    },
    components: {ElImageViewer},
    data() {
      return {
        url: null,
        show: this.value,
      }
    },
    methods: {
      imageClose() {
        this.show = false
      }
    },
    watch: {
      value(val) {
        if (val) {
          let loading = this.$loading({
            lock: true,
            text: '正在努力生成图片'
          });;
          Http.easyGet('/api/repo/file/image', {id: this.repoId}, resp => {
            this.url = resp.body.src;
            this.show = val;
          }, null, null, () => loading.close());
        }
      },
      show(val) {
        this.$emit('input', val);
      }
    }
  };
</script>

<style scoped>

</style>
