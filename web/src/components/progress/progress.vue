<template>
  <div class="ep-progress" :style="{height: `${height}px`, gap: `${gap}px`, width: width ? `${width}px` : undefined}">
    <div
      v-for="d in data"
      v-if="getPercent(d.value)"
      class="ep-progress-inner"
      :class="`is-${d.type}`"
      :style="{width: `${getPercent(d.value)}%`, backgroundColor: d.color || undefined}"
    >
      <div class="ep-progress-label" :style="{lineHeight: `${height}px`, fontSize: `${labelHeight}px`}">{{ d.label }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EpProgress',
  props: {
    data: Array,
    height: {
      type: Number,
      default() {
        return 10;
      }
    },
    labelHeight: {
      type: Number,
      default() {
        return this.height / 2;
      }
    },
    width: Number,
    gap: {
      type: Number,
      default() {
        return 0;
      }
    }
  },
  data() {
    return {
    };
  },
  updated() {

  },
  methods: {
    getPercent(curValue) {
      let total = 0;
      this.data.map(d => { total += d.value || 0; });
      return total ? curValue / total * 100 : 0;
    }
  }
};
</script>

<style lang="less">
    .ep-progress {
        width: 100%;
        background-color: #ebeef5;
        border-radius: 100px;
        display: inline-flex;

        .ep-progress-label {
            font-size: 10px;
            line-height: 10px;
            text-align: center;
            color: #fff;
        }

        .ep-progress-inner.is-success {
            background-color: #67c23a;
        }

        .ep-progress-inner.is-warning {
            background-color: #e6a23c;
        }

        .ep-progress-inner.is-error {
            background-color: #f56c6c;
        }

        .ep-progress-inner.is-info {
            background-color: #909399;
        }

        .ep-progress-inner.is-normal {
            background-color: #409eff;
        }

        .ep-progress-inner.is-empty {
            background-color: transparent;
        }

        .ep-progress-inner {
            height: 100%;
        }

        .ep-progress-inner:first-child {
            border-top-left-radius: 100px;
            border-bottom-left-radius: 100px;
        }

        .ep-progress-inner:last-child {
            border-top-right-radius: 100px;
            border-bottom-right-radius: 100px;
        }
    }
</style>
