<template>
  <div>
    <ep-table
      ref="table"
      :columns="columns"
      :request="request"
      search-field="keyword"
      size="small"
    >
      <template #topLeftBefore>
        <div>
          <el-button type="primary" size="small" @click="onAdd">新建</el-button>
        </div>
      </template>
    </ep-table>
    <edit :visible.sync="editShow" :data="editData" @success="$refs.table.refresh()"/>
    <var-drawer :visible.sync="varShow" :project-id="varProject"/>
  </div>
</template>

<script>
  import Http from "@utils/http";
  import EpLink from "@components/link/link";
  import Edit from "./edit";
  import VarDrawer from "./var";

  export default {
    name: "ProjectList",
    components: {Edit, VarDrawer},
    data() {
      return {
        editShow: false,
        editData: {},

        varShow: false,
        varProject: null,

        search: {
          keyword: null
        },
        request(option) {
          return new Promise(resolve => {
            Http.easyPost("/api/project/list", {...option}, resp => {
              resolve(resp.body);
            });
          });
        },
        columns: [
          {
            prop: "id",
            label: "ID",
            width: 70,
            sortable: true,
            align: "center",
          }, {
            prop: "key",
            label: "KEY",
            sortable: true,
            width: 200,
            ellipsis: true
          }, {
            prop: "name",
            label: "名称",
            sortable: true,
            ellipsis: true
          },  {
            prop: "create_time",
            label: "创建时间",
            width: 200,
            sortable: true,
            ellipsis: true,
            show: false
          }, {
            prop: "update_time",
            label: "更新时间",
            width: 200,
            sortable: true,
            ellipsis: true
          }, {
            prop: "update_by_name",
            label: "更新人",
            width: 120,
            sortable: true,
            ellipsis: true,
          }, {
            label: "操作",
            width: 150,
            align: "center",
            render: (h, {row, value, scope}) => {
              return <div>
                <EpLink onClick={() => {
                  this.varProject = row.id;
                  this.varShow = true;
                }}>变量</EpLink>
                <EpLink style="margin: 0 5px" onClick={() => {
                  this.editData = row;
                  this.editShow = true;
                }}>编辑</EpLink>
                <el-popover
                  width='160'
                  ref={'deletePop' + scope.$index}
                >
                  <p>确定删除当前项目？</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                      scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                    }}>取消
                    </el-button>
                    <el-button type='primary' size='mini' onClick={() => {
                         Http.easyDelete("/api/project/remove?id=" + row.id, null, resp => {
                           this.$message.success("删除成功");
                           scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                           this.$refs.table.refresh();
                         });}}>
                      确定
                    </el-button>
                  </div>
                  <EpLink style="margin-left: 5px" slot="reference">删除</EpLink>
                </el-popover>
              </div>;
            }
          }
        ]
      }
    },
    methods: {
      onAdd() {
        this.editData = {};
        this.editShow = true;
      },
    }
  }
</script>

<style scoped>

</style>
