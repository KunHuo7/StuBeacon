<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { get, post, del } from '../../net/index';
import ChatMessage from './components/ChatMessage.vue';
import ChatInput from './components/ChatInput.vue';
import ConversationList from './components/ConversationList.vue';
import TypingIndicator from './components/TypingIndicator.vue';

const router = useRouter();
const route = useRoute();

// 对话状态
const conversations = ref([]);
const currentConversationId = ref(null);
const showConversationsList = ref(false);
const isLoading = ref(false);
const loadingError = ref(null);

// 当前对话的消息
const messages = ref([]);
const isSending = ref(false);
const isThinking = ref(false);

// API端点
const API_BASE = '/api/chat';

// 获取所有对话历史
const fetchConversations = async () => {
  try {
    isLoading.value = true;
    loadingError.value = null;
    
    get(`${API_BASE}/conversations`, null, 
      (message, data) => {
        conversations.value = data;
        isLoading.value = false;
        
        // 如果存在路由参数中的对话ID，加载该对话
        if (route.params.id) {
          currentConversationId.value = parseInt(route.params.id);
        }
      },
      (errorMsg) => {
        loadingError.value = errorMsg || '无法加载对话历史';
        isLoading.value = false;
      }
    );
  } catch (error) {
    loadingError.value = '无法加载对话历史';
    isLoading.value = false;
    console.error('加载对话历史失败:', error);
  }
};

// 获取特定对话的消息
const fetchMessages = async (conversationId) => {
  if (!conversationId) {
    messages.value = [];
    return;
  }
  
  try {
    isThinking.value = true;
    
    get(`${API_BASE}/conversations/${conversationId}/messages`, null,
      (message, data) => {
        messages.value = data;
        isThinking.value = false;
      },
      (errorMsg) => {
        console.error('加载消息失败:', errorMsg);
        isThinking.value = false;
      }
    );
  } catch (error) {
    console.error('加载消息失败:', error);
    isThinking.value = false;
  }
};

// 创建新对话
const createNewConversation = () => {
  currentConversationId.value = null;
  messages.value = [];
};

// 删除对话
const deleteConversation = async (conversationId) => {
  try {
    del(`${API_BASE}/conversations/${conversationId}`, null,
      (message) => {
        // 删除成功，从列表中移除
        const index = conversations.value.findIndex(conv => conv.id === conversationId);
        if (index !== -1) {
          conversations.value.splice(index, 1);
        }
        
        // 如果删除的是当前对话，清空消息
        if (currentConversationId.value === conversationId) {
          currentConversationId.value = null;
          messages.value = [];
        }
      },
      (errorMsg) => {
        console.error('删除对话失败:', errorMsg);
      }
    );
  } catch (error) {
    console.error('删除对话失败:', error);
  }
};

// 发送消息
const sendMessage = async (content) => {
  if (!content.trim()) return;
  
  try {
    isSending.value = true;
    
    // 创建用户消息并添加到消息列表（立即显示）
    const tempUserMessage = {
      id: 'temp-' + Date.now().toString(),
      role: 'user',
      content,
      timestamp: new Date().toISOString()
    };
    
    // 添加到消息列表
    messages.value.push(tempUserMessage);
    
    // 如果是新对话，先创建对话
    if (!currentConversationId.value) {
      // 创建新对话
      const title = content.length > 30 
        ? content.substring(0, 30) + '...' 
        : content;
      
      post(`${API_BASE}/conversations`, { title },
        (message, conversationId) => {
          // 创建成功，设置当前对话ID
          currentConversationId.value = conversationId;
          
          // 发送消息到这个新对话
          sendMessageToConversation(conversationId, content, tempUserMessage);
          
          // 添加到对话列表
          const newConversation = {
            id: conversationId,
            title,
            date: new Date().toISOString(),
            lastMessage: content
          };
          
          conversations.value.unshift(newConversation);
        },
        (errorMsg) => {
          console.error('创建对话失败:', errorMsg);
          isSending.value = false;
        }
      );
    } else {
      // 直接发送消息到现有对话
      sendMessageToConversation(currentConversationId.value, content, tempUserMessage);
    }
  } catch (error) {
    console.error('发送消息失败:', error);
    isSending.value = false;
  }
};

// 发送消息到指定对话
const sendMessageToConversation = (conversationId, content, tempUserMessage) => {
  // 临时显示思考状态
  isThinking.value = true;
  
  post(`${API_BASE}/conversations/${conversationId}/messages`, { content },
    (message, aiResponse) => {
      // 获取AI回复成功
      
      // 更新对话列表中的最后消息
      const conversation = conversations.value.find(c => c.id === conversationId);
      if (conversation) {
        conversation.lastMessage = aiResponse.content;
        conversation.date = new Date().toISOString();
      }
      
      // 添加AI回复到消息列表
      messages.value.push(aiResponse);
      
      isSending.value = false;
      isThinking.value = false;
    },
    (errorMsg) => {
      console.error('获取AI回复失败:', errorMsg);
      isSending.value = false;
      isThinking.value = false;
    }
  );
};

// 生成对话摘要
const generateSummary = (conversationId) => {
  if (!conversationId) return;
  
  post(`${API_BASE}/conversations/${conversationId}/summary`, null,
    (message, summary) => {
      // 更新对话摘要
      const conversation = conversations.value.find(c => c.id === conversationId);
      if (conversation) {
        conversation.summary = summary;
      }
    },
    (errorMsg) => {
      console.error('生成摘要失败:', errorMsg);
    }
  );
};

// 监听当前对话ID变化，加载相应消息
watch(currentConversationId, (newId) => {
  if (newId) {
    fetchMessages(newId);
  } else {
    messages.value = [];
  }
});

// 计算当前对话标题
const currentConversationTitle = computed(() => {
  if (!currentConversationId.value) return '新对话';
  const conversation = conversations.value.find(c => c.id === currentConversationId.value);
  return conversation ? conversation.title : '对话';
});

// 组件挂载时获取对话列表
onMounted(() => {
  fetchConversations();
  
  // 如果直接从URL访问特定对话
  if (route.params.id) {
    currentConversationId.value = parseInt(route.params.id);
  }
});

// 跳转到完整的聊天页面
const navigateToFullChat = () => {
  // 如果当前有对话，传递对话ID；否则创建新对话
  if (currentConversationId.value) {
    router.push({
      name: 'chatFullPage',
      params: { id: currentConversationId.value }
    });
  } else {
    router.push({ name: 'chatFullPage' });
  }
};
</script>

<template>
  <div class="flex h-screen w-full font-inter antialiased text-gray-800 dark:text-gray-100 bg-gray-100 dark:bg-gray-950">
    <!-- 侧边栏：对话历史 -->
    <div
        class="w-72 bg-gray-50 dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 transform transition-transform duration-300 ease-in-out overflow-hidden flex flex-col shadow-lg md:shadow-none"
        :class="{ 'translate-x-0': showConversationsList, '-translate-x-full absolute h-full md:translate-x-0 md:relative': !showConversationsList }"
    >
      <div class="p-1 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-white dark:bg-gray-900 rounded-tl-lg">
        <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100 px-3 py-2">对话历史</h2>
        <button
            class="p-2 rounded-full hover:bg-gray-200 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 md:hidden focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-600"
            @click="showConversationsList = false"
            aria-label="关闭对话历史"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <button
          class="mx-4 mt-6 mb-4 flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-800 focus:ring-blue-500 dark:focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-gray-50 dark:focus:ring-offset-gray-800 text-white py-3 px-4 rounded-xl shadow-md hover:shadow-lg transition-all duration-200 text-base font-semibold focus:outline-none"
          @click="createNewConversation"
          aria-label="创建新对话"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
        </svg>
        新对话
      </button>

      <div class="flex-1 overflow-y-auto p-2 scrollbar-thin scrollbar-thumb-gray-300 dark:scrollbar-thumb-gray-600 scrollbar-track-gray-100 dark:scrollbar-track-gray-800">
        <ConversationList
            :conversations="conversations"
            :current-id="currentConversationId"
            :is-loading="isLoading"
            :error="loadingError"
            @select="currentConversationId = $event; showConversationsList = false"
            @delete="deleteConversation"
        />
      </div>
    </div>

    <!-- 主内容区：聊天界面 -->
    <div class="flex-1 flex flex-col h-full bg-white dark:bg-gray-900 relative rounded-lg shadow-xl overflow-hidden">
      <!-- 头部 -->
      <div class="px-4 py-3 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-gray-50 dark:bg-gray-800 shadow-sm">
        <div class="flex items-center">
          <button
              class="p-2 mr-3 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 md:hidden focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-600"
              @click="showConversationsList = true"
              aria-label="显示对话历史"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
            </svg>
          </button>
          <h1 class="text-xl font-bold text-gray-800 dark:text-gray-100 truncate max-w-[calc(100vw-150px)] md:max-w-none">{{ currentConversationTitle }}</h1>
        </div>
        <!-- You can add more actions here if needed -->
      </div>

      <!-- 消息区域 -->
      <div class="flex-1 overflow-y-auto p-6 space-y-5 bg-gray-50 dark:bg-gray-900">
        <div v-if="messages.length === 0" class="h-full flex flex-col items-center justify-center text-center p-6 bg-gray-100 dark:bg-gray-800 rounded-lg shadow-inner">
          <div class="w-20 h-20 mb-6 rounded-full bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center text-blue-600 dark:text-blue-400 ring-4 ring-blue-200 dark:ring-blue-800">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10">
              <path stroke-linecap="round" stroke-linejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" />
            </svg>
          </div>
          <h3 class="text-2xl font-extrabold text-gray-900 dark:text-gray-50 mb-3">开启智慧对话</h3>
          <p class="text-gray-600 dark:text-gray-400 max-w-lg leading-relaxed">
            在这里，您可以畅所欲言，提出任何问题或想法。我将尽力为您提供准确、有用的信息。您也可以随时从左侧导航栏选择历史对话，继续您之前的讨论。
          </p>
        </div>

        <template v-else>
          <ChatMessage
              v-for="message in messages"
              :key="message.id"
              :message="message"
          />
        </template>

        <div v-if="isThinking" class="py-2">
          <TypingIndicator />
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="border-t border-gray-200 dark:border-gray-700 p-4 bg-gray-50 dark:bg-gray-800 shadow-md">
        <ChatInput
            :is-loading="isSending"
            :conversation-id="currentConversationId"
            @send="sendMessage"
        />
      </div>
    </div>
  </div>

</template>

<style scoped>
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

/* 自定义滚动条样式 */
.scrollbar-thin::-webkit-scrollbar {
  width: 6px;
}

.scrollbar-thin::-webkit-scrollbar-track {
  background: var(--scrollbar-track, #f1f1f1);
}

.scrollbar-thin::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb, #888);
  border-radius: 10px;
}

.scrollbar-thin::-webkit-scrollbar-thumb:hover {
  background: var(--scrollbar-thumb-hover, #666);
}

/* 暗色模式下的滚动条样式 */
.dark .scrollbar-thin::-webkit-scrollbar-track {
  --scrollbar-track: #2d3748;
}

.dark .scrollbar-thin::-webkit-scrollbar-thumb {
  --scrollbar-thumb: #4a5568;
}

.dark .scrollbar-thin::-webkit-scrollbar-thumb:hover {
  --scrollbar-thumb-hover: #718096;
}
</style>