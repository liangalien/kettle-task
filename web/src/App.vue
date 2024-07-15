<template>
  <div id="app">
    <login v-if="$route.path === '/login'"/>
    <panel v-else />
  </div>
</template>
<script>
  import Login from '@pages/user/login';
  import Panel from '@pages/panel';

  export default {
    components: {Login, Panel}
  }
</script>
