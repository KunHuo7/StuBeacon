<script setup>
import { computed } from 'vue';
import { format } from 'date-fns';
import { marked } from 'marked';
import DOMPurify from 'dompurify';

const props = defineProps({
  message: {
    type: Object,
    required: true
  }
});

// 格式化消息时间
const formattedTime = computed(() => {
  try {
    return format(new Date(props.message.timestamp), 'HH:mm');
  } catch (error) {
    return '';
  }
});

// 处理markdown内容
const renderContent = computed(() => {
  if (!props.message.content) return '';
  
  // 使用marked解析markdown
  const rawHtml = marked(props.message.content, {
    breaks: true,
    gfm: true
  });
  
  // 使用DOMPurify清理HTML以防止XSS攻击
  return DOMPurify.sanitize(rawHtml);
});

// 确定消息类型
const isUser = computed(() => props.message.role === 'user');

// 头像URL
const avatarUrl = computed(() => {
  return isUser.value 
    ? '/src/assets/images/user-avatar.png' // 默认用户头像（需要替换为实际路径）
    : '/src/assets/images/ai-avatar.png'; // 默认AI头像（需要替换为实际路径）
});

// 头像备选方案（如果图片加载失败）
const avatarFallback = computed(() => {
  return isUser.value ? 'U' : 'AI';
});

// 根据消息长度决定气泡最大宽度
const bubbleMaxWidth = computed(() => {
  const contentLength = props.message.content?.length || 0;
  if (contentLength < 50) return 'max-w-xs';
  if (contentLength < 200) return 'max-w-sm';
  if (contentLength < 500) return 'max-w-md';
  return 'max-w-lg';
});
</script>

<template>
  <div 
    class="flex items-start gap-4 group animate-fade-in"
    :class="{ 
      'justify-end': isUser
    }"
  >
    <!-- 头像 - 当不是用户消息(AI)时显示在左侧 -->
    <div 
      v-if="!isUser"
      class="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center text-lg font-semibold shadow-sm bg-purple-100 text-purple-600 dark:bg-purple-900 dark:text-purple-300"
    >
      <!-- 替代文本（当图片无法加载时显示） -->
      {{ avatarFallback }}
    </div>

    <div class="flex flex-col gap-1">
      <!-- 消息气泡 -->
      <div 
        :class="[
          bubbleMaxWidth,
          'rounded-2xl p-4 shadow-sm', 
          isUser 
            ? 'bg-blue-600 text-white rounded-xl border border-blue-600'
            : 'bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200 rounded-xl border border-gray-200 dark:border-gray-700'
        ]"
      >
        <div class="prose dark:prose-invert prose-sm max-w-none break-words" v-html="renderContent">
        </div>
      </div>

      <div 
        class="text-xs opacity-0 group-hover:opacity-70 transition-opacity px-2"
        :class="[
          isUser ? 'text-right' : 'text-left',
          'text-gray-500 dark:text-gray-400'
        ]"
      >
        {{ formattedTime }}
      </div>
    </div>
    
    <!-- 头像 - 用户消息时显示在右侧 -->
    <div 
      v-if="isUser"
      class="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center text-lg font-semibold shadow-sm bg-blue-100 text-blue-600 dark:bg-blue-900 dark:text-blue-300"
    >
      <!-- 替代文本（当图片无法加载时显示） -->
      {{ avatarFallback }}
    </div>
  </div>
</template>

<style scoped>
/* 添加样式以适应 markdown 内容 */
:deep(.prose pre) {
  @apply bg-gray-900 dark:bg-gray-950 text-gray-100 p-3 rounded-md overflow-x-auto;
}

:deep(.prose code:not(pre code)) {
  @apply bg-gray-200 dark:bg-gray-700 px-1 py-0.5 rounded text-gray-800 dark:text-gray-200 text-sm;
}

:deep(.prose blockquote) {
  @apply border-l-4 border-gray-300 dark:border-gray-600 pl-4 italic;
}

:deep(.prose ul) {
  @apply list-disc pl-5;
}

:deep(.prose ol) {
  @apply list-decimal pl-5;
}

:deep(.prose a) {
  @apply text-blue-500 dark:text-blue-400 hover:underline;
}

:deep(.prose h1), :deep(.prose h2), :deep(.prose h3), :deep(.prose h4) {
  @apply font-bold mb-2 mt-4;
}

:deep(.prose h1) {
  @apply text-xl;
}

:deep(.prose h2) {
  @apply text-lg;
}

:deep(.prose h3) {
  @apply text-base;
}

:deep(.prose h4) {
  @apply text-sm;
}

:deep(.prose p:not(:last-child)) {
  @apply mb-3;
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