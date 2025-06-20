<script setup>
// 简单的打字指示器组件
</script>

<template>
  <div class="flex items-start gap-4 animate-fade-in">
    <!-- AI头像 - 显示在左侧 -->
    <div class="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center text-lg font-semibold shadow-sm bg-purple-100 text-purple-600 dark:bg-purple-900 dark:text-purple-300">
      AI
    </div>

    <div class="flex flex-col">
      <!-- 消息气泡 -->
      <div class="bg-white dark:bg-gray-800 rounded-2xl rounded-tl-none p-4 border border-gray-200 dark:border-gray-700 shadow-sm">
        <div class="flex items-center gap-1.5 typing-container">
          <div class="typing-dot"></div>
          <div class="typing-dot"></div>
          <div class="typing-dot"></div>
        </div>
      </div>
      
      <!-- 时间替代品，保持布局一致 -->
      <div class="h-4"></div>
    </div>
  </div>
</template>

<style scoped>
.typing-container {
  padding: 2px;
}

.typing-dot {
  @apply bg-purple-500 dark:bg-purple-400;
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

.animate-fade-in {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 