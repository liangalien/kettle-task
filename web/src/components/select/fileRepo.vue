<template>
  <ep-select
    :request="request"
    :response-format="format"
    :value="value"
    width="100%"
    value-key="id"
    search-field="keyword"
    v-bind="$attrs"
    @change="change"
    v-on="$listeners"
  />
</template>

<script>
import Common from '@utils/common';
import Http from '@utils/http';

export default {
  props: {
    value: [String, Number, Array, Object]
  },
  data() {
    return {
      request: option => {
        return new Promise(resolve => {
          Http.easyPost('/api/repo/file/list', { ...option, project_id: Common.getProjectId() }, resp => {
            resolve(resp.body);
          });
        });
      }

    };
  },
  methods: {
    format(resp) {
      return {
        rows: resp.rows.map(item => {
          item.value = item.id;
          item.label = item.file_name;
          return {
            value: item,
            label: item.file_name
          };
        }),
        total: resp.total
      };
    },
    change(val) {
      this.$emit('input', val);
    }
  }
};
</script>
