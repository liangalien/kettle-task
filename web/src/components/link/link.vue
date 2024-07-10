<template>
  <router-link v-if="route" :to="href" tag="span">
    <el-link
      v-bind="{href: undefined, target: undefined, type, underline, ...$attrs}"
    ><slot /></el-link>
  </router-link>
  <el-link
    v-else
    v-bind="{type, underline, href, ...$attrs}"
    v-on="$listeners"
  ><slot /></el-link>
</template>

<script>

export default {
  name: 'EPLink',
  props: {
    href: String,
    route: Boolean,
    type: {
      type: String,
      default() {
        return 'primary';
      }
    },
    underline: {
      type: Boolean,
      default() {
        return false;
      }
    }
  }
};
</script>
