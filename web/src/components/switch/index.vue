<template>
  <div
    class="el-switch ep-switch"
    :class="{ 'is-disabled': switchDisabled, 'is-checked': checked }"
    role="switch"
    :aria-checked="checked"
    :aria-disabled="switchDisabled"
    @click.prevent="switchValue"
  >
    <input
      :id="id"
      ref="input"
      class="el-switch__input"
      type="checkbox"
      :name="name"
      :true-value="activeValue"
      :false-value="inactiveValue"
      :disabled="switchDisabled"
      @change="handleChange"
      @keydown.enter="switchValue"
    >
    <span ref="core" class="el-switch__core">
      <span v-if="activeText && value" class="ep-switch__label ep-switch__label__active">{{ activeText }}</span>
      <span v-if="inactiveText && !value" class="ep-switch__label ep-switch__label__inactive">{{ inactiveText }}</span>
    </span>
  </div>
</template>

<script>
import ElSwitch from 'element-ui/packages/switch/src/component';

export default {
  extends: ElSwitch
};
</script>

<style lang="less">
    .ep-switch {
        .el-switch__core:has(.ep-switch__label) {
            display: flex;
            width: 100%;
        }
        .ep-switch__label {
            color: #fff;
            line-height: 18px;
            font-size: 13px;
        }
        .ep-switch__label__active {
            padding: 0 20px 0 10px;
        }
        .ep-switch__label__inactive {
            padding: 0 10px 0 20px;
        }
    }
</style>
