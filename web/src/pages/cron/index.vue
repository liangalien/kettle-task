<template>
  <div>
    <ep-table
      :columns="columns"
      :data="data"
      :auto-loading="false"
      :search-field="false"
      :extra="false"
      size="small"
    >
    </ep-table>
  </div>
</template>

<script>
  import Http from "@utils/http";
  import EpLink from "@components/link/link";

  export default {
    name: "CronList",
    data() {
      return {
        data: [],
        columns: [
          {
            prop: "key",
            label: "KEY",
            ellipsis: true
          }, {
            prop: "group",
            label: "组",
            ellipsis: true
          }, {
            prop: "name",
            label: "名称",
            ellipsis: true
          },{
            prop: "cron",
            label: "定时策略",
            ellipsis: true,
          },  {
            prop: "next_fire_time",
            label: "下次触发时间",
            ellipsis: true,
            show: false
          }, {
            label: "操作",
            width: 100,
            align: "center",
            render: (h, {row, value, scope}) => {
              return <div>
                <el-popover
                  width='160'
                  ref={'deletePop' + scope.$index}
                >
                  <p>确定删除定时策略</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                      scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                    }}>取消
                    </el-button>
                    <el-button type='primary' size='mini' onClick={() => {
                         Http.easyDelete(`/api/qrtz/remove?name=${row.name}&group=${row.group}`, null, resp => {
                           this.$message.success("操作成功");
                           scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                           this.get();
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
      get() {
        Http.easyGet("/api/qrtz/list", null, resp => {
          this.data = resp.body;
        });
      }
    },
    created() {
      this.get();
    }
  }
</script>

<style scoped>

</style>
