<template>
  <div>
    <ep-table
      ref="table"
      :columns="columns"
      :request="request"
      :search-field="false"
      :auto-loading="false"
    >
      <template #topLeftBefore>
        <div>
          <el-button type="primary" size="small" @click="add">添加任务</el-button>
        </div>
        <div>
          <el-select v-model="search.status" style="width: 200px" size="small" clearable placeholder="状态查询">
            <el-option
              v-for="(item, index) in statusList"
              :key="index"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
          <el-select v-model="search.type" style="width: 200px" size="small" clearable placeholder="类型查询">
            <el-option value="trans" label="转换" />
            <el-option value="job" label="作业" />
          </el-select>
        </div>
        <div>
          <el-date-picker
            v-model="search.times"
            clearable
            size="small"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </div>
        <div>
          <el-input
            v-model="search.keyword"
            clearable
            style="width: 200px"
            size="small"
            placeholder="关键字查询"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
        </div>
      </template>
    </ep-table>
    <el-dialog
      :visible.sync="cronShow"
      class="kt-dialog"
      title="定时任务"
      width="400px"
    >
      <el-input v-model="cronValue" placeholder="cron表达式，如每天12点执行：0 0 12 * * ?" size="small"/>
      <span slot="footer" class="dialog-footer">
        <el-popover width="160" v-model="cronRemoveShow">
          <p>确定移除定时任务？</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="cronRemoveShow = false">取消</el-button>
            <el-button size="mini" type="primary" @click="removeCron">确定</el-button>
          </div>
          <el-button slot="reference" :disabled="cronValue == null" size="mini">移除</el-button>
        </el-popover>
        <el-button size="mini" type="primary" @click="setCron">确定</el-button>
      </span>
    </el-dialog>

    <edit :visible.sync="editShow" :data="editData" @success="$refs.table.refresh()"/>
  </div>
</template>

<script>
  import Edit from "./edit";
  import EpLink from "@components/link/link";

  import Http from "@utils/http";
  import Task from "@utils/task";
  import dayjs from 'dayjs';
  import Common from "@utils/common";

  export default {
    name: "TaskList",
    components: {Edit, EpLink},
    data() {
      return {
        cornTaskId: null,
        cronName: null,
        cronShow: false,
        cronValue: null,
        cronRemoveShow: false,

        editShow: false,
        editData: {},

        statusList: Task.statusList,
        search: {
          project_id: 1
        },
        request(option) {
          return new Promise(resolve => {
            Http.easyPost("/api/task/list", {...option}, resp => {
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
            prop: "name",
            label: "名称",
            sortable: true,
            ellipsis: true,
            render: (h, {value, row}) => {
              return <el-tooltip content={`文件名称：${row.repo_name}`}>
                  <EpLink onClick={() => {
                    this.editData = row;
                    this.editShow = true;
                  }}><el-tag
                    size="small"
                    type="info"
                    style="margin-right: 10px"
                  >{row.repo_type === 'trans' ? '转换' : '作业'}
                  </el-tag>{value}</EpLink>
              </el-tooltip>
            }
          }, {
            prop: "dir",
            label: "路径",
            width: 100,
            show: false
          }, {
            prop: "status",
            label: "状态",
            width: 100,
            sortable: true,
            ellipsis: true,
            render: (h, {value}) => {
              let status = Task.statusMap[value];
              return <el-tag size="small" effect="dark" color={status.color}>{status.label}</el-tag>
            }
          }, {
            prop: "quartz_cron",
            label: "定时策略",
            width: 200,
            ellipsis: true,
            render: (h, {value, row}) => {
              return <el-tooltip disabled={value === null} content={`下次执行时间：${row.next_run_time}`}>
                <el-link
                  underline={false}
                  type="primary"
                  onClick={() => {
                    this.cornTaskId = row.id;
                    this.cronName = row.key;
                    this.cronValue = value;
                    this.cronShow = true;
                  }}
                >{value || "未开启定时"}</el-link>
              </el-tooltip>
            }
          }, {
            prop: "last_start_time",
            label: "最后执行",
            width: 200,
            sortable: true,
            ellipsis: true,
          }, {
            prop: "create_by",
            label: "创建者",
            width: 200,
            sortable: true,
            ellipsis: true,
          }, {
            label: "操作",
            width: 150,
            align: "center",
            render: (h, {row, value, scope}) => {
              return <div>
                <EpLink onClick={() => {
                  Http.easyPost("/api/task/start", {id: row.id}, () => {
                    this.$message.success("操作成功");
                    this.$refs.table.refresh();
                  });
                }}>执行
                </EpLink>

                <EpLink style="margin-left: 5px" href={"/runner?task_id=" + row.id} target="_blank">记录</EpLink>

                {[1, 2, 3].indexOf(row.status) === -1 &&
                  <el-popover
                    width='160'
                    ref={'deletePop' + scope.$index}
                  >
                    <p>确定删除这条记录吗？</p>
                    <div style='text-align: right; margin: 0'>
                      <el-button size='mini' type='text' onClick={(e) => {
                        scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                      }}>取消
                      </el-button>
                      <el-button type='primary' size='mini'
                                 onClick={() => {
                                   Http.easyDelete("/api/task/remove?id=" + row.id, null, resp => {
                                     this.$message.success("删除成功");
                                     scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                                     this.$refs.table.refresh();
                                   });
                                 }}>确定
                      </el-button>
                    </div>
                    <EpLink style="margin-left: 5px" slot="reference">删除</EpLink>
                  </el-popover> ||

                  <el-popover
                  width='160'
                  ref={'deletePop' + scope.$index}
                  >
                  <p>确定停止当前任务？</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                    scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                  }}>取消
                    </el-button>
                    <el-button type='primary' size='mini'
                    onClick={() => {
                      Http.easyDelete("/api/task/stop?id=" + row.id, null, resp => {
                        this.$message.success("操作成功");
                        scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                        this.$refs.table.refresh();
                      });
                    }}>确定</el-button>
                  </div>
                  <EpLink style="margin-left: 5px" slot="reference">停止</EpLink>
                  </el-popover>
                }
              </div>;
            }
          }
        ]
      }
    },
    methods: {
      add() {
        this.editData = {};
        this.editShow = true;
      },
      remove(row) {

      },
      setCron() {
        Http.easyPost("/api/task/sched", {
          id: this.cornTaskId,
          cron: this.cronValue
        }, resp => {
          this.$message.success("操作成功");
          this.cronShow = false;
          this.$refs.table.refresh();
        });
      },
      removeCron() {
        Http.easyDelete("/api/task/sched/cancel?key=" + this.cronName, null, resp => {
          this.$message.success("操作成功");
          this.cronRemoveShow = false;
          this.cronShow = false;
          this.$refs.table.refresh();
        });
      },
      setSearch: function () {
        let times = this.search.times, start = null, end = null;
        if (times && times.length === 2) {
          start = dayjs(times[0]).format('YYYY-MM-DD HH:mm:ss');
          end = dayjs(times[1]).format('YYYY-MM-DD HH:mm:ss');
        }
        this.$refs.table.search = {
          search: {
            type: this.search.type || undefined,
            status: Common.isBlank(this.search.status) ? undefined : this.search.status,
            keyword: this.search.keyword || undefined,
            start,
            end
          }
        };
        this.$refs.table.refresh();
      },
    },
    watch: {
      search: {
        handler() {
          this.setSearch();
        },
        deep: true
      }
    },
    mounted() {
      this.setSearch();
    }
  }
</script>

<style scoped>

</style>
