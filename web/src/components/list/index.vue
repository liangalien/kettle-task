<template>
  <div class="ep-list">
    <el-input
      v-if="search"
      v-model="searchText"
      clearable
      :placeholder="searchPlaceholder"
      @keyup.native.enter="onSearch"
    >
      <el-button slot="append" icon="el-icon-search" @click="onSearch" />
    </el-input>

    <ep-scrollbar v-if="data && data.length" ref="scrollbar" v-loading="loading" view-class="ep-list" @scroll-bottom="isScrollBottom">
      <el-checkbox-group v-model="checked" @change="change">
        <div v-for="(item, idx) in data" v-if="item.show != false" :key="idx" class="ep-checkbox-warp" @click="$emit('click-item', item)">
          <el-checkbox v-if="checkbox" :label="item[valueKey]" :checked="item.checked || false">
            <span />
          </el-checkbox>
          <div class="ep-list-item-warp" @click="onClickItem(item[valueKey])">
            <slot :item="item" :index="idx">{{ item.label || item[valueKey] }}</slot>
          </div>
        </div>
      </el-checkbox-group>
    </ep-scrollbar>

    <el-empty v-else :description="loading ? '数据加载中' : '暂无数据'" />

  </div>
</template>

<script>

import EpScrollbar from '@components/scrollbar/index';

export default {
  components: { EpScrollbar },
  props: {
    listData: {
      type: Array,
      default() {
        return [];
      }
    },
    request: Function,
    responseFormat: Function,
    pageSize: {
      type: Number,
      default() {
        return 30;
      }
    },
    loadingText: {
      type: String,
      default() {
        return '加载中';
      }
    },
    search: {
      type: Boolean,
      default() {
        return true;
      }
    },
    searchPlaceholder: {
      type: String,
      default() {
        return '输入关键字';
      }
    },
    searchField: {
      type: String,
      default() {
        return 'search';
      }
    },
    checkWhenClick: {
      type: Boolean,
      default() {
        return true;
      }
    },
    valueKey: {
      type: String,
      default() {
        return 'value';
      }
    },
    checkbox: {
      type: Boolean,
      default() {
        return true;
      }
    },
    filterHandler: Function
  },
  data() {
    return {
      data: this.listData,
      checked: [],
      searchText: null,
      pageNo: 1,
      pageTotal: 0,
      loading: false
    };
  },
  watch: {
    listData(d) {
      this.data = d;
    }
  },
  mounted() {
    this.fetch();
  },
  methods: {
    fetch() {
      if (this.request) {
        this.loading = true;
        this.request(this.getOption()).then(resp => {
          if (!this.responseFormat) {
            var rows = resp.rows;
            var total = resp.total == undefined && resp.rows.length || resp.total || 0;
          } else {
            var { rows, total } = this.responseFormat(resp);
          }

          if (this.pageNo === 1) { this.data = rows; } else { this.data = this.data.concat(rows); }

          this.pageTotal = total;
        }).finally(() => this.loading = false);
      } else {
        this.data = this.listData;
      }
    },
    change(val) {
      this.$emit('checked', val);
    },
    scrollToTop() {
      this.$nextTick(() => this.$refs.scrollbar.scrollTop());
    },
    scrollToBottom() {
      this.$nextTick(() => this.$refs.scrollbar.scrollBottom());
    },
    isScrollBottom() {
      if (this.request && this.data.length >= this.pageTotal || this.loading) return;

      this.pageNo++;
      this.fetch();
    },
    getOption: function() {
      const options = {
        pageNo: this.pageNo,
        pageSize: this.pageSize
      };
      if (this.search && this.searchText) {
        options[this.searchField] = this.searchText;
      }
      return options;
    },
    onSearch() {
      if (this.request) {
        this.pageNo = 1;
        this.pageTotal = 0;
        this.fetch();
      } else {
        this.data = this.data.map(item => {
          if (this.filterHandler) { return this.filterHandler(item); } else if (this.searchField) {
            item.show = this.searchText && item[this.searchField].indexOf(this.searchText) == -1 ? false : undefined;
          } else { item.show = undefined; }
          return item;
        });
      }
      this.$refs.scrollbar.scrollTop();
    },
    onClickItem(key) {
      if (!this.checkWhenClick) return;

      if (this.checked.indexOf(key) == -1) { this.checked.push(key); } else { this.checked = this.checked.filter(c => c != key); }
    },
    addData(data, position = 'end') {
      const existKeys = this.data.map(d => d[this.valueKey]);
      const notExistData = data.filter(d => existKeys.indexOf(d[this.valueKey]) == -1);
      if (position == 'start') {
        this.data = notExistData.concat(this.data);
        this.scrollToTop();
      } else {
        this.data = this.data.concat(notExistData);
        this.scrollToBottom();
      }
    },
    removeData(keys) {
      this.data = this.data.filter(d => keys.indexOf(d[this.valueKey]) == -1);
    },
    removeCheckedData() {
      this.removeData(this.checked);
      this.checked = [];
    },
    getCheckedData() {
      return this.data.filter(d => this.checked.indexOf(d[this.valueKey]) != -1);
    },
    setChecked(keys, replace = true) {
      return this.data.map(d => {
        if (keys.indexOf(d[this.valueKey]) != -1) { d.checked = true; } else if (replace) { d.checked = false; }
      });
    }
  }
};
</script>

<style lang="less">
.ep-list {
    margin: 10px 0;

    .el-scrollbar__wrap {
        overflow-x: hidden;
        height: 400px;
    }

    .ep-checkbox-warp {
        display: flex;
        padding: 12px 0;
        margin: 0;
        border-bottom: 1px solid #ebeef5;
        font-size: 14px;
    }

    .ep-list-item-warp {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        cursor: pointer;
    }

    .ep-list-item {
        display: flex;
        justify-content: space-between;
        flex: 1;
    }

    .ep-checkbox-label.ep-checkbox-option {
        display: flex;
        justify-content: space-between;
        flex: 1;
    }

    .ep-list-item-warp:hover {
        white-space: normal;
    }
}

</style>
