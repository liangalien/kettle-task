<template>
  <div>
    <ep-table
      ref="table"
      size="small"
      :columns="columns"
      :request="request"
      :search-field="false"
      :auto-loading="false"
    >
      <template #topLeftBefore>
        <div>
          <el-select v-model="search.status" style="width: 200px" size="small" placeholder="状态查询" clearable>
            <el-option
              v-for="(item, index) in statusList"
              :key="index"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
          <el-select v-model="search.repo_type" style="width: 200px" size="small" clearable placeholder="类型查询">
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
    <log-drawer :visible.sync="logShow" :data="logData"/>
    <repo-preview v-model="previewShow" :url="previewUrl"/>
  </div>
</template>

<script>
  import Http from "@utils/http";
  import Task from "@utils/task";
  import Common from "@utils/common";
  import EpLink from "@components/link/link";
  import RepoPreview from "@pages/repo/preview";
  import LogDrawer from "./log";
  import dayjs from 'dayjs';

  export default {
    name: "RunnerList",
    components: {EpLink, LogDrawer, RepoPreview},
    data() {
      return {
        logShow: false,
        logData: {},

        previewShow: false,
        previewUrl: null,

        statusList: Task.statusList,
        search: {
          project_id: Common.getProjectId(),
          task_id: Common.getUrlParam('task_id') || undefined
        },
        request(option) {
          return new Promise(resolve => {
            Http.easyPost("/api/runner/list", {...option}, resp => {
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
            prop: "task_name",
            label: "名称",
            sortable: true,
            ellipsis: true,
            render: (h, {value, row}) => {
              return <el-tooltip content={`文件名称：${row.repo_name}`}>
                <span>
                  <el-tag
                    size="small"
                    effect="plain"
                    type={row.repo_type === 'trans' ? 'info' : 'success'}
                    style="margin-right: 10px"
                  >{row.repo_type === 'trans' ? '转换' : '作业'}
                  </el-tag>
                  {value}
                </span>
              </el-tooltip>
            }
          }, {
            prop: "task_dir",
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
            prop: "start_time",
            label: "开始时间",
            width: 200,
            sortable: true,
            ellipsis: true
          }, {
            prop: "end_time",
            label: "结束时间",
            width: 200,
            sortable: true,
            ellipsis: true
          }, {
            prop: "durations",
            label: "耗时",
            width: 100,
            ellipsis: true,
            render: (h, {value, row}) => {
              return row.start_time && row.end_time ? value + '秒' : '-';
            }
          }, {
            prop: "create_by_name",
            label: "执行人",
            width: 120,
            sortable: true,
            ellipsis: true,
            render: (h, {value, row}) => {
              return row.trigger == 2 ? '定时任务' : value;
            }
          }, {
            label: "操作",
            width: 150,
            align: "center",
            render: (h, {row, value, scope}) => {
              return <div>
                <EpLink onClick={() => {
                  this.logData = row;
                  this.logShow = true;
                }}>日志
                </EpLink>

                <EpLink  style="margin: 0 5px" onClick={() => {
                  this.previewUrl = row.repo_img;
                  this.previewShow = true;
                }}>图像
                </EpLink>

                {[1, 2, 3].indexOf(row.status) !== -1 && <el-popover
                  width='160'
                  ref={'deletePop' + scope.$index}
                >
                  <p>确定停止当前任务？</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                      scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                    }}>取消
                    </el-button>
                    <el-button type='primary' size='mini' onClick={() => {
                         Http.easyDelete("/api/task/stop?id=" + row.task_id, null, resp => {
                           this.$message.success("操作成功");
                           scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                           this.$refs.table.refresh();
                         });}}>
                      确定
                    </el-button>
                  </div>
                  <EpLink slot="reference">停止</EpLink>
                </el-popover> ||

                <el-popover
                  width='160'
                  ref={'restartPop' + scope.$index}
                >
                  <p>确定重跑这个任务吗？</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                      scope._self.$refs['restartPop' + scope.$index].showPopper = false;
                    }}>取消
                    </el-button>
                    <el-button type='primary' size='mini'
                               onClick={() => {
                                 Http.easyPost("/api/task/start", {id: row.task_id}, () => {
                                   this.$message.success("重跑任务已添加，请稍后刷新当前页面查看");
                                   this.$refs.table.refresh();
                                   scope._self.$refs['restartPop' + scope.$index].showPopper = false;
                                 });
                               }}>确定
                    </el-button>
                  </div>
                  <EpLink slot="reference">重跑</EpLink>
                </el-popover>}
              </div>;
            }
          }
        ]
      }
    },
    methods: {
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
        Http.easyDelete("/api/task/sched/cancel?quartz_name=" + this.cronName, null, resp => {
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
          ...this.search,
          task_id: this.search.task_id || undefined,
          repo_type: this.search.repo_type || undefined,
          status: Common.isBlank(this.search.status) ? undefined : this.search.status,
          keyword: this.search.keyword || undefined,
          start,
          end
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
