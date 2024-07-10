<template>
  <div>
    <div class="ep-transfer">
      <el-card style="width: 50%">
        <div slot="header" class="ep-transfer-header ep-transfer-left-header">
          <span class="title">{{ titles[0] }}</span>
          <el-button round type="info" size="mini" icon="el-icon-arrow-right" @click="leftToRight" />
        </div>

        <ep-list
          ref="left"
          :check-when-click="true"
          :list-data="leftData"
          :request="leftRequest"
          :response-format="leftResponseFormat"
          :search-field="leftSearchField"
        >
          <div slot-scope="{item, index}" class="ep-list-item">
            <div>
              <el-link
                target="_blank"
                :underline="false"
                type="primary"
                :href="`http://jira.caih.local/browse/${item.key}`"
              >{{ item.key }}</el-link> - {{ item.summary }}
            </div>
          </div>
        </ep-list>

      </el-card>
      <el-card style="width: 50%">
        <div slot="header" class="ep-transfer-header ep-transfer-right-header">
          <el-button round type="info" size="mini" icon="el-icon-arrow-left" @click="rightToLeft" />
          <span class="title">{{ titles[1] }}</span>
        </div>

        <ep-list
          ref="right"
          :check-when-click="false"
          :list-data="rightData"
          :request="rightRequest"
          :response-format="rightResponseFormat"
          :search-field="rightSearchField"
        >
          <div slot-scope="{item, index}" class="ep-list-item">
            <div>
              <el-link
                target="_blank"
                :underline="false"
                type="primary"
                :href="`http://jira.caih.local/browse/${item.key}`"
              >{{ item.key }}</el-link> - {{ item.summary }}
            </div>
          </div>
        </ep-list>
      </el-card>
    </div>
  </div>
</template>

<script>
import Http from '@utils/http';
import Common from '@utils/common';
import EpList from '@components/list/index';

export default {
  components: { EpList },
  props: {
    titles: {
      type: Array,
      default() {
        return ['左标题', '右标题'];
      }
    },
    leftRequest: Function,
    rightRequest: Function,
    leftResponseFormat: Function,
    rightResponseFormat: Function,
    leftData: {
      type: Array,
      default() {
        return [];
      }
    },
    rightData: {
      type: Array,
      default() {
        return [];
      }
    },
    leftSearchField: {
      type: String,
      default() {
        return 'search';
      }
    },
    rightSearchField: {
      type: String,
      default() {
        return 'search';
      }
    }
  },
  data() {
    return {
    };
  },
  methods: {
    leftToRight() {
      const data = this.$refs.left.getCheckedData();
      this.$refs.right.addData(data, 'start');
      this.$refs.left.removeCheckedData();
    },
    rightToLeft() {
      const data = this.$refs.right.getCheckedData();
      this.$refs.left.addData(data, 'start');
      this.$refs.right.removeCheckedData();
    },
    leftRefresh() {
      this.$refs.left.fetch();
    },
    rightRefresh() {
      this.$refs.right.fetch();
    },
    getSelectedData() {
      return this.$refs.right.data;
    }
  }
};
</script>

<style lang="less">

.ep-transfer {
    display: flex;
    gap: 20px;

    .el-card__header {
        padding: 6px 30px;
    }

    .el-card__body {
        padding: 0 30px;
    }

    .ep-transfer-header {
        text-align: center;
        display: inline-flex;
        width: 100%;

        .title {
            width: 100%;
            font-size: 16px;
        }

        .num {
            color: #7e7e7e;
            font-size: 13px;
        }

        button {
            padding: 5px 14px;
            i {
                font-size: 18px;
            }
        }
    }

    .ep-list-item {
        display: flex;
        justify-content: space-between;
        flex: 1;
    }
}

</style>
