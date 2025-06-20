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

const showToolTip = ref('');

// 自动调整文本框高度
const adjustTextareaHeight = () => {
  if (!inputRef.value) return;
  
  // 重置高度以便于计算
  inputRef.value.style.height = 'auto';
  
  // 计算新高度 (最小32px，最大80px)
  const newHeight = Math.min(Math.max(inputRef.value.scrollHeight, 32), 80);
  inputRef.value.style.height = `${newHeight}px`;
};

// 发送消息
const sendMessage = () => {
  if (props.isLoading || !message.value.trim()) return;
  
  // 发送消息并清空输入框
  emit('send', message.value);
  message.value = '';
  
  // 重置文本框高度
  nextTick(() => {
    if (inputRef.value) {
      inputRef.value.style.height = '32px';
    }
  });
};

// 处理回车键发送消息
const onKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
};

// 添加表情符号
const addEmoji = (emoji) => {
  message.value += emoji;
  showEmojiPicker.value = false;
  inputRef.value.focus();
};


</script>

<template>
  <div class="relative">
    <!-- 工具提示 -->
    <div v-if="showToolTip" class="absolute -top-10 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white px-3 py-1.5 rounded-lg text-sm shadow-lg z-10">
      {{ showToolTip }}
      <div class="absolute -bottom-1.5 left-1/2 transform -translate-x-1/2 w-3 h-3 bg-gray-800 rotate-45"></div>
    </div>
  
    <div class="flex flex-col rounded-2xl border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 focus-within:border-blue-500 dark:focus-within:border-blue-400 focus-within:ring-1
                h-16 focus-within:ring-blue-500 dark:focus-within:ring-blue-400 transition-all duration-200 overflow-hidden">
      <div class="flex items-center h-full">
        <!-- 文本输入区域 -->
        <textarea
          ref="inputRef"
          v-model="message"
          @input="adjustTextareaHeight"
          @keydown="onKeydown"
          class="flex-1 h-full outline-none py-3 px-4 resize-none bg-transparent text-gray-700 dark:text-gray-200 placeholder-gray-500 dark:placeholder-gray-400 min-h-[32px] max-h-[80px]"
          :class="{ 'opacity-70 cursor-not-allowed': isLoading }"
          placeholder="请输入您的问题..."
          :disabled="isLoading"
          rows="1"
        ></textarea>
        
        <!-- 发送按钮 -->
        <button
          class="p-2 mx-2 rounded-xl bg-blue-600 hover:bg-blue-700 dark:bg-blue-600 dark:hover:bg-blue-700 text-white disabled:opacity-50 disabled:cursor-not-allowed disabled:bg-blue-400 dark:disabled:bg-blue-500 transition-colors duration-200"
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
          <svg v-else xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 14 14"><g fill="none">
            <path fill="#d7e0ff" d="m5.808 11l2.178 2.169a1.1 1.1 0 0 0 1.049.3a1.12 1.12 0 0 0 .809-.74l3.577-10.72A1.12 1.12 0 0 0 11.982.57L1.262 4.147a1.12 1.12 0 0 0-.74.859a1.1 1.1 0 0 0 .3 1L3.56 8.742l-.09 3.466z"/>
            <path stroke="#4147d5" stroke-linecap="round" stroke-linejoin="round" d="m5.808 11l2.178 2.169a1.1 1.1 0 0 0 1.049.3a1.12 1.12 0 0 0 .809-.74l3.577-10.72A1.12 1.12 0 0 0 11.982.57L1.262 4.147a1.12 1.12 0 0 0-.74.859a1.1 1.1 0 0 0 .3 1L3.56 8.742l-.09 3.466zM13.101.79L3.56 8.743"/>
          </g>
          </svg>
        </button>
      </div>
    </div>
    
    <!-- 帮助提示 -->
    <h5 class="mt-2 px-1 text-xs text-gray-500 dark:text-gray-400 text-center">
      提示：模型训练还不够成熟，请谨慎鉴别内容。
    </h5>
  </div>
</template> 

<style scoped>
.textarea-container {
  position: relative;
}

textarea:focus {
  box-shadow: none !important;
  border-color: transparent !important;
}
</style> 