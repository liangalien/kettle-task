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
          <el-button type="primary" size="small" @click="$refs.upload.value = null; $refs.upload.click()">上传</el-button>
          <input v-show="false" ref="upload" type="file" @change="onUpload" accept=".ktr,.kjb" multiple/>
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
  </div>
</template>

<script>
  import Http from "@utils/http";
  import EpLink from "@components/link/link";

  export default {
    name: "RepoList",
    data() {
      return {
        uploading: false,
        search: {
          project_id: 1,
          keyword: null
        },
        request(option) {
          return new Promise(resolve => {
            Http.easyPost("/api/repo/file/list", {...option}, resp => {
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
            prop: "update_by",
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
            project_id: 1
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
      setSearch: function () {
        this.$refs.table.search = {
          search: {
            keyword: this.search.keyword
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
