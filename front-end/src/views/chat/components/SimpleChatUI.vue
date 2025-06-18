<script setup>
import { ref, onMounted } from 'vue';
import { post, get } from '../../../net/index';
import ChatMessage from './ChatMessage.vue';
import ChatInput from './ChatInput.vue';
import TypingIndicator from './TypingIndicator.vue';

// API端点
const API_BASE = '/api/chat';

// 当前对话的消息
const messages = ref([]);
const isSending = ref(false);
const isThinking = ref(false);

// 创建临时对话ID
const tempConversationId = ref(null);

// 发送消息
const sendMessage = async (content) => {
  if (!content.trim()) return;
  
  try {
    isSending.value = true;
    
    // 创建用户消息
    const userMessage = {
      id: 'temp-' + Date.now().toString(),
      role: 'user',
      content,
      timestamp: new Date().toISOString()
    };
    
    // 添加到消息列表
    messages.value.push(userMessage);
    
    // 如果没有对话ID，创建一个新对话
    if (!tempConversationId.value) {
      // 创建新对话，使用消息内容的前30个字符作为标题
      const title = content.length > 30 
        ? content.substring(0, 30) + '...' 
        : content;
        
      post(`${API_BASE}/conversations`, { title },
        (message, conversationId) => {
          // 设置对话ID
          tempConversationId.value = conversationId;
          
          // 发送消息到这个新对话
          sendMessageToConversation(conversationId, content);
        },
        (errorMsg) => {
          console.error('创建对话失败:', errorMsg);
        isSending.value = false;
          
          // 添加错误消息
          messages.value.push({
            id: 'error-' + Date.now().toString(),
            role: 'assistant',
            content: '创建对话失败，请重试。',
            timestamp: new Date().toISOString()
          });
        }
      );
    } else {
      // 发送消息到现有对话
      sendMessageToConversation(tempConversationId.value, content);
    }
  } catch (error) {
    console.error('发送消息失败:', error);
    isSending.value = false;
    
    // 添加错误消息
    messages.value.push({
      id: 'error-' + Date.now().toString(),
      role: 'assistant',
      content: '发送消息失败，请检查网络连接。',
      timestamp: new Date().toISOString()
    });
  }
};

// 发送消息到指定对话
const sendMessageToConversation = (conversationId, content) => {
  // 显示思考状态
  isThinking.value = true;
  
  post(`${API_BASE}/conversations/${conversationId}/messages`, { content },
    (message, aiResponse) => {
      // 添加AI回复到消息列表
      messages.value.push(aiResponse);
      
      isSending.value = false;
      isThinking.value = false;
    },
    (errorMsg) => {
      console.error('获取AI回复失败:', errorMsg);
      isSending.value = false;
      isThinking.value = false;
      
      // 添加错误消息
      messages.value.push({
        id: 'error-' + Date.now().toString(),
        role: 'assistant',
        content: '获取AI回复失败: ' + errorMsg,
        timestamp: new Date().toISOString()
      });
    }
  );
};

// 组件挂载时获取一条欢迎消息
onMounted(() => {
  // 添加欢迎消息
  messages.value.push({
    id: 'welcome',
    role: 'assistant',
    content: '您好！我是AI助手，有什么我可以帮您的吗？',
    timestamp: new Date().toISOString()
  });
});
</script>

<template>
  <div class="flex flex-col h-full">
    <!-- 消息区域 -->
    <div class="flex-1 overflow-y-auto p-2 space-y-4 mb-3">
      <ChatMessage 
        v-for="message in messages" 
        :key="message.id" 
        :message="message" 
      />
      
      <div v-if="isThinking" class="py-2">
        <TypingIndicator />
      </div>
      
      <!-- 欢迎消息（当没有消息时显示） -->
      <div v-if="messages.length === 0" class="flex flex-col items-center justify-center text-center p-4">
        <div class="w-12 h-12 mb-3 rounded-full bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center text-blue-600 dark:text-blue-400">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" />
          </svg>
        </div>
        <p class="text-gray-600 dark:text-gray-400">
          开始一个新对话，我会尽力回答您的问题。
        </p>
      </div>
    </div>
    
    <!-- 输入区域 -->
    <ChatInput 
      :is-loading="isSending" 
      :conversation-id="tempConversationId"
      @send="sendMessage"
    />
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