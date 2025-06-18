<script setup>
import { ref, nextTick } from 'vue';

const props = defineProps({
  isLoading: {
    type: Boolean,
    default: false
  },
  conversationId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['send']);

const message = ref('');
const inputRef = ref(null);

// 自动调整文本框高度
const adjustTextareaHeight = () => {
  if (!inputRef.value) return;
  
  // 重置高度以便于计算
  inputRef.value.style.height = 'auto';
  
  // 计算新高度 (最小38px，最大150px)
  const newHeight = Math.min(Math.max(inputRef.value.scrollHeight, 38), 150);
  inputRef.value.style.height = `${newHeight}px`;
};

// 发送消息
const sendMessage = () => {
  if (props.isLoading || !message.value.trim()) return;
  
  emit('send', message.value);
  message.value = '';
  
  // 重置文本框高度
  nextTick(() => {
    if (inputRef.value) {
      inputRef.value.style.height = '38px';
    }
  });
};

// 处理回车键发送消息
const onKeydown = (e) => {
  // 如果按下Shift+Enter，允许换行
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
};
</script>

<template>
  <div class="relative">
    <div class="flex items-end rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 focus-within:border-blue-500 dark:focus-within:border-blue-400 focus-within:ring-1 focus-within:ring-blue-500 dark:focus-within:ring-blue-400 transition-all duration-200">
      <!-- 文本输入区域 -->
      <textarea
        ref="inputRef"
        v-model="message"
        @input="adjustTextareaHeight"
        @keydown="onKeydown"
        class="flex-1 outline-none py-2 px-3 resize-none bg-transparent text-gray-700 dark:text-gray-200 placeholder-gray-500 dark:placeholder-gray-400 min-h-[38px] max-h-[150px]"
        :class="{ 'opacity-70 cursor-not-allowed': isLoading }"
        placeholder="发送消息..."
        :disabled="isLoading"
        rows="1"
      ></textarea>
      
      <!-- 发送按钮 -->
      <button
        class="p-2 mr-1 mb-1 rounded-lg text-blue-600 dark:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/30 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
        :disabled="isLoading || !message.trim()"
        @click="sendMessage"
        type="button"
        aria-label="发送消息"
      >
        <!-- 加载中图标 -->
        <svg v-if="isLoading" class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        
        <!-- 发送图标 -->
        <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
        </svg>
      </button>
    </div>
  </div>
</template> 