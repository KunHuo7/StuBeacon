<script setup>
// 简单的打字指示器组件
</script>

<template>
  <div class="flex items-center gap-1 px-3 py-2 rounded-lg bg-gray-100 dark:bg-gray-800 w-fit">
    <div class="typing-dot"></div>
    <div class="typing-dot"></div>
    <div class="typing-dot"></div>
  </div>
</template>

<style scoped>
.typing-dot {
  @apply bg-blue-500 dark:bg-blue-400;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin: 0 2px;
  animation: typing-animation 1.4s infinite ease-in-out both;
}

.typing-dot:nth-child(1) {
  animation-delay: 0s;
}

.typing-dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing-animation {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.6;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}
</style> 