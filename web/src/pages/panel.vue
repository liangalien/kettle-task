<template>
  <div>
    <el-menu
      router
      style="width: 100%"
      :default-active="defaultMenu"
      class="kt-menu"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      @select="onSelect"
    >
      <el-menu-item disabled class="kt-user">
        <el-dropdown @command="onUserMenu">
          <span class="el-dropdown-link">
            <i class="el-icon-user-solid"></i>
            <span>{{ $store.state.nickname }}</span>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">退出账号</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>
      <el-menu-item class="kt-project" disabled>
        <project-select v-model="project" size="small"/>
      </el-menu-item>
      <el-menu-item
        v-for="(menu, index) in menus"
        v-if="menu.hide !== true"
        :key="index"
        :index="menu.name"
        :route="menu.path"
      >
        {{ menu.name }}
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
  import ProjectSelect from "@components/select/project";
  import System from "@/config/system";
  import Common from "@utils/common";

  const projectId = Common.getProjectId() || 1;
  const projectName = Common.getProjectName() || "TEST";


  export default {
    name: "Panel",
    components: {EpLink, EpScrollbar, ProjectSelect},
    data() {
      return {
        project: {value: projectId, label: projectName},
        menus: System.routes,
        defaultMenu: null,
        breadcrumbs: []
      }
    },
    methods: {
      onSelect(index) {
        this.setNav(index);
        this.$nextTick(() => {
          this.setBre();
        });
      },
      onUserMenu(command) {
        if (command === 'logout') {
          this.$router.push('/login');
        }
      },
      setNav(curMenu=null) {
        this.defaultMenu = curMenu || this.breadcrumbs[0].name;
      },
      setBre() {
        this.breadcrumbs = this.$route.matched.map((r, index) => {
          return {
            name: r.name,
            path: r.path
          }
        });
      }
    },
    watch: {
      project(val) {
        Common.setProject(val.value, val.label);
        this.$router.go(0);
      }
    },
    created() {
      this.setBre();
      this.setNav();
    }
  }
</script>
