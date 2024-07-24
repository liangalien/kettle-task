<template>
  <el-image-viewer v-if="value" :url-list="[url]" :z-index="20000" :on-close="imageClose"/>
</template>

<script>
  import ElImageViewer from "element-ui/packages/image/src/image-viewer";

  export default {
    name: "RepoPreview",
    props: {
      value: Boolean,
      url: String,
    },
    components: {ElImageViewer},
    methods: {
      imageClose() {
        this.$emit('input', false);
      }
    }
  };
</script>

<style scoped>

</style>
