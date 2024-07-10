<template>
  <div class="codemirror-wrap">
    <textarea ref="textarea" />
  </div>
</template>

<script>
import codemirror from 'codemirror';
import 'codemirror/mode/sql/sql';

import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/idea.css'

const defaultOptions = {
  tabSize: 4,
  indentUnit: 4,
  theme: 'idea',
  line: true,
  lineNumbers: true, // 启用行号
  mode: 'sql'
};

export default {
  props: {
    value: String,
    options: Object
  },
  data() {
    return {
      cminstance: null
    };
  },
  mounted() {
    this.cminstance = codemirror.fromTextArea(this.$refs.textarea, { ...defaultOptions, ...this.options });
    if (this.value) this.cminstance.setValue(this.value);
    this.cminstance.on('change', cm => {
      this.$emit('input', cm.getValue());
    });
  },
  beforeDestroy() {
    const element = this.cminstance.doc.cm.getWrapperElement();
    element && element.remove && element.remove();
  },
  watch: {
    value(val) {
      if (this.cminstance) {
        this.cminstance.setValue(val);
      }
    }
  }
};
</script>

<style lang="less">
    .codemirror-wrap {
        font-size: 16px
    }

    .el-form-item__content:has(.codemirror-wrap) {
        line-height: inherit;
    }
</style>
