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
</script>

<template>
  <div 
    class="flex"
    :class="{ 'justify-end': isUser }"
  >
    <div 
      class="max-w-[85%] md:max-w-[75%] rounded-lg p-3 mb-2 relative"
      :class="[
        isUser 
          ? 'bg-blue-600 text-white rounded-tr-none' 
          : 'bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-200 rounded-tl-none'
      ]"
    >
      <!-- 消息内容 -->
      <div 
        class="prose dark:prose-invert prose-sm max-w-none break-words"
        v-html="renderContent"
      ></div>
      
      <!-- 消息时间 -->
      <div 
        class="text-xs mt-1 text-right opacity-70"
      >
        {{ formattedTime }}
      </div>
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
</style> 