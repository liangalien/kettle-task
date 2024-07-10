<template>
  <div id="app">
    <el-menu
      :default-active="defaultMenu"
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-menu-item v-for="(menu, index) in menus" :key="index" :index="menu.name">
        <ep-link :href="menu.path">{{ menu.name }}</ep-link>
      </el-menu-item>
    </el-menu>

    <div class="kt-body">
      <div class="kt-breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbs"
            :key="index"
            :to="{path: item.path}"
          >
            {{ item.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="kt-panel">
        <router-view/>
      </div>
    </div>
  </div>
</template>
<script>
  import EpScrollbar from "@components/scrollbar/index";
  import EpLink from "@components/link/link";
  import System from "@/config/system";

  export default {
    name: "Main",
    components: {EpLink, EpScrollbar},
    data() {
      return {
        menus: System.routes,
        defaultMenu: null,
        breadcrumbs: []
      }
    },
    created() {
      this.breadcrumbs = this.$route.matched.map((r, index) => {
        return {
          name: r.name,
          path: r.path
        }
      });
      this.defaultMenu = this.breadcrumbs.length ? this.breadcrumbs[0].name : '首页';

      console.log(this.$router)
    }
  }
</script>
<style>

  body {
    margin: 0;
    background-color: #f7f7f7;
  }

  .el-tabs__item {
    padding: 0 20px 12px 20px;
    height: auto;
    line-height: normal;
  }


  .el-tabs__header {
    margin: 0 0 4px !important;
  }

  .el-tabs__nav-wrap::after {
    height: 1px;
    background-color: rgba(0, 0, 0, .06);
  }

  .hljs {
    background-color: transparent;
  }

  .el-table th:not(.el-descriptions-item__cell) {
    padding: 4px 0 !important;
  }

  .el-table th .cell {
    line-height: 34px !important;
  }

  .el-table th.is-sortable:hover {
    background: #f2f2f2 !important;
  }

  .el-table .caret-wrapper {
    float: right;
  }

  .ep-table .table-top {
    margin-bottom: 0 !important;
  }

  .el-tree-node__content {
    line-height: 34px;
    height: 34px;
  }

  .el-tag:not(.el-tag--plain) {
    border-color: transparent !important;
  }


  .el-icon-arrow-down {
    font-size: 12px;
  }

  .el-link {
    vertical-align: baseline !important;
  }


  .el-drawer__footer {
    width: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 10px 16px;
    text-align: center;
  }

  .el-textarea__inner {
    font-family: element-icons;
  }


</style>
