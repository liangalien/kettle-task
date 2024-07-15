<template>
  <div>
    <ep-table
      ref="table"
      size="small"
      :columns="columns"
      :request="request"
      search-field="keyword"
    >
      <template #topLeftBefore>
        <div>
          <el-button type="primary" size="small" @click="$refs.upload.value = null; $refs.upload.click()">上传</el-button>
          <input v-show="false" ref="upload" type="file" @change="onUpload" accept=".ktr,.kjb" multiple/>
        </div>
      </template>
    </ep-table>
  </div>
</template>

<script>
  import Http from "@utils/http";
  import EpLink from "@components/link/link";
  import Common from "@utils/common";

  export default {
    name: "RepoList",
    data() {
      return {
        uploading: false,
        request(option) {
          return new Promise(resolve => {
            Http.easyPost("/api/repo/file/list", {...option, project_id: Common.getProjectId()}, resp => {
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
            prop: "file_name",
            label: "文件名",
            sortable: true,
            ellipsis: true
          }, {
            prop: "create_time",
            label: "上传时间",
            width: 200,
            sortable: true,
            ellipsis: true
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
            width: 100,
            align: "center",
            render: (h, {row, value, scope}) => {
              return <div>
                <el-popover
                  width='160'
                  ref={'deletePop' + scope.$index}
                >
                  <p>确定删除当前文件？</p>
                  <div style='text-align: right; margin: 0'>
                    <el-button size='mini' type='text' onClick={(e) => {
                      scope._self.$refs['deletePop' + scope.$index].showPopper = false;
                    }}>取消
                    </el-button>
                    <el-button type='primary' size='mini' onClick={() => {
                         Http.easyDelete("/api/repo/file/remove?id=" + row.id, null, resp => {
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
      onUpload(event) {
        let files = event.target.files
        if (files.length === 0) {
          return;
        } else if (files.length > 10) {
          this.$message.error('一次最多只能上传10个文件');
          return;
        }

        this.uploading = true;
        let uploaded = [];
        Array.from(files).map(file => {
          Http.easyUpload('/api/repo/file/upload', 'file', file, {
            project_id: Common.getProjectId()
          }, resp => {
            uploaded.push(true);
          }, () => {
            uploaded.push(false);
          });
        });

        let interval = setInterval(() => {
          if (uploaded.length === files.length) {
            clearInterval(interval);
            this.uploading = false;
            this.$message.success('上传完成');
            this.$refs.table.refresh();
          }
        }, 500);

      },
    },
  }
</script>

<style scoped>

</style>
