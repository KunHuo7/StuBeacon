<template>
  <div class="tooltip-container">
    <div class="tooltip-trigger" ref="trigger">
      <slot name="trigger"></slot>
    </div>
  </div>
</template>

<script>
export default {
  // 简化组件，移除所有与tooltip显示相关的逻辑
};
</script>

<style scoped>
.tooltip-container {
  position: relative;
  display: inline-block;
}

.tooltip-trigger {
  cursor: pointer;
}
</style>
