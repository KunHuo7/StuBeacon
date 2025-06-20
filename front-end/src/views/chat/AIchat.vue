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

// 标签管理已禁用

// API端点
const API_BASE = '/api/chat';

// 获取所有对话历史
const fetchConversations = async () => {
  try {
    isLoading.value = true;
    loadingError.value = null;

    get(`${API_BASE}/conversations`, null,
        (message, data) => {
          // 确保数据是数组
          if (Array.isArray(data)) {
            // 确保每个对话都有正确的ID类型（后端返回的可能是字符串）
            conversations.value = data.map(conv => ({
              ...conv,
              id: typeof conv.id === 'string' ? parseInt(conv.id) : conv.id
            }));
          } else {
            conversations.value = [];
            console.error('获取到的对话数据格式不正确:', data);
          }

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

    // 保存当前页面上可能存在的用户消息
    const existingUserMessages = messages.value.filter(msg =>
        msg.role === 'user' && msg.id.startsWith('temp-')
    );

    get(`${API_BASE}/conversations/${conversationId}/messages`, null,
        (message, data) => {
          // 确保数据是数组
          if (Array.isArray(data)) {
            // 确保每个消息都有正确的格式
            const fetchedMessages = data.map(msg => ({
              ...msg,
              id: msg.id || `msg-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
              role: msg.role || 'assistant',
              timestamp: msg.timestamp || new Date().toISOString()
            }));

            // 合并从服务器获取的消息和当前存在的临时用户消息
            // 检查是否存在重复的消息内容
            const combinedMessages = [...fetchedMessages];

            // 添加任何可能遗漏的用户消息
            existingUserMessages.forEach(userMsg => {
              // 检查这条用户消息是否已经在后端数据中
              const isDuplicate = fetchedMessages.some(msg =>
                  msg.role === 'user' && msg.content === userMsg.content
              );

              if (!isDuplicate) {
                combinedMessages.push(userMsg);
              }
            });

            // 按时间戳排序
            combinedMessages.sort((a, b) => {
              return new Date(a.timestamp) - new Date(b.timestamp);
            });

            messages.value = combinedMessages;
          } else {
            // 如果服务器返回无效数据，至少保留现有的用户消息
            if (existingUserMessages.length > 0) {
              messages.value = existingUserMessages;
            } else {
              messages.value = [];
            }
            console.error('获取到的消息数据格式不正确:', data);
          }

          isThinking.value = false;
        },
        (errorMsg) => {
          // 发生错误时，保留现有的用户消息
          if (existingUserMessages.length > 0) {
            messages.value = existingUserMessages;
          }
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

    // 自动滚动到底部
    setTimeout(() => {
      const messagesContainer = document.querySelector('.flex-1.overflow-y-auto');
      if (messagesContainer) {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      }
    }, 50);

    // 如果是新对话，先创建对话
    if (!currentConversationId.value) {
      // 创建新对话
      const title = content.length > 10
          ? content.substring(0, 10) + '...'
          : content;

      post(`${API_BASE}/conversations`, { title: title },
          (message, conversationId) => {
            // 创建成功，设置当前对话ID
            currentConversationId.value = conversationId;

            // 更新URL，但不重新加载页面
            window.history.pushState({}, '', `/chat/${conversationId}`);

            // 注意：此时messages数组中已经包含了用户消息
            // 发送消息到这个新对话，同时传递用户消息对象以便在需要时恢复
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

  // 保存当前用户消息的引用，避免它被fetchMessages覆盖
  const userMessageId = tempUserMessage.id;

  post(`${API_BASE}/conversations/${conversationId}/messages`, { content: content },
      (message, aiResponse) => {
        // 获取AI回复成功

        // 更新对话列表中的最后消息
        const conversation = conversations.value.find(c => c.id === conversationId);
        if (conversation) {
          conversation.lastMessage = aiResponse.content;
          conversation.date = new Date().toISOString();
          // 摘要功能已禁用
        }

        // 检查用户消息是否仍在消息列表中
        const userMessageExists = messages.value.some(msg => msg.id === userMessageId);

        // 如果用户消息不存在（可能被fetchMessages覆盖），则重新添加
        if (!userMessageExists) {
          messages.value.push(tempUserMessage);
        }

        // 添加AI回复到消息列表
        messages.value.push(aiResponse);

        // 自动滚动到底部
        setTimeout(() => {
          const messagesContainer = document.querySelector('.flex-1.overflow-y-auto');
          if (messagesContainer) {
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
          }
        }, 50);

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

// 摘要功能已禁用

// 监听当前对话ID变化，加载相应消息
watch(currentConversationId, (newId, oldId) => {
  if (newId) {
    // 添加延迟，让用户消息有时间渲染
    setTimeout(() => {
      // 如果是首次创建对话，不要立即加载消息，让sendMessageToConversation来处理
      // 这样可以避免覆盖刚刚添加到messages中的用户消息
      const isFirstMessage = messages.value.length === 1 && messages.value[0].role === 'user';
      if (!isFirstMessage) {
        fetchMessages(newId);
      }
    }, 100);
  } else {
    messages.value = [];
  }
});

// 计算当前对话标题
const currentConversationTitle = computed(() => {
  if (!currentConversationId.value) {
    // 如果有消息但还没创建对话，显示用户第一条消息的前10个字
    if (messages.value.length > 0 && messages.value[0].role === 'user') {
      const firstMessageContent = messages.value[0].content;
      return firstMessageContent.length > 10 ? firstMessageContent.substring(0, 10) + '...' : firstMessageContent;
    }
    return '新对话';
  }
  const conversation = conversations.value.find(c => c.id === currentConversationId.value);
  return conversation ? conversation.title : '对话';
});

// 标签功能已禁用

// 组件挂载时获取对话列表
onMounted(() => {
  fetchConversations();

  // 如果直接从URL访问特定对话
  if (route.params.id) {
    try {
      currentConversationId.value = parseInt(route.params.id);
      // 监听路由变化
      window.addEventListener('popstate', () => {
        const pathParts = window.location.pathname.split('/');
        const idParam = pathParts[pathParts.length - 1];
        if (idParam && !isNaN(parseInt(idParam))) {
          currentConversationId.value = parseInt(idParam);
        } else {
          currentConversationId.value = null;
        }
      });
    } catch (e) {
      console.error('解析对话ID失败:', e);
    }
  }
});

// 跳转到完整的聊天页面
const navigateToFullChat = () => {
  // 如果当前有对话，传递对话ID；否则创建新对话
  if (currentConversationId.value) {
    router.push({
      name: 'chatFullPage',
      params: {id: currentConversationId.value}
    });
  } else {
    router.push({name: 'chatFullPage'});
  }
};
</script>

<template>
  <div
      class="flex h-screen w-full font-inter antialiasing text-gray-800 dark:text-gray-100 bg-gradient-to-br from-blue-50 to-gray-100 dark:from-gray-900 dark:to-gray-950">
    <!-- 侧边栏：对话历史 -->
    <div
        class="w-80 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 transform transition-transform duration-300 ease-in-out overflow-hidden flex flex-col shadow-lg md:shadow-none"
        :class="{ 'translate-x-0': showConversationsList, '-translate-x-full absolute h-full md:translate-x-0 md:relative z-99': !showConversationsList }"
    >
      <div
          class="p-2 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-white dark:bg-gray-900">
        <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100">对话历史</h2>
        <button
            class="p-2 rounded-full hover:bg-gray-200 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 md:hidden focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-600"
            @click="showConversationsList = false"
            aria-label="关闭对话历史"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
               stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <button
          class="mx-4 mt-6 mb-4 flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-800 focus:ring-blue-500 dark:focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-gray-50 dark:focus:ring-offset-gray-800 text-white py-3 px-4 rounded-full shadow-md hover:shadow-lg transition-all duration-200 text-base font-semibold focus:outline-none"
          @click="createNewConversation"
          aria-label="创建新对话"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor"
             class="w-5 h-5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
        </svg>
        新对话
      </button>

      <div class="flex-1 overflow-y-auto p-3">
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
    <div class="flex-1 flex flex-col h-full bg-white dark:bg-gray-900 relative overflow-hidden">
      <!-- 头部 -->
      <div
          class="px-6 py-2 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center bg-white dark:bg-gray-900 shadow-sm">
        <div class="flex items-center">
          <button
              class="p-2 mr-3 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 md:hidden focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-600"
              @click="showConversationsList = true"
              aria-label="显示对话历史"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"/>
            </svg>
          </button>
          <div class="flex flex-col">
            <h1 class="text-xl font-bold text-gray-800 dark:text-gray-100 truncate max-w-[calc(100vw-150px)] md:max-w-none">
              {{ currentConversationTitle }}
            </h1>

          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex items-center space-x-3">
          <button
              class="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 text-gray-500 dark:text-gray-400"
              aria-label="清空对话"
              @click="createNewConversation"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"/>
            </svg>
          </button>
        </div>
      </div>

      <!-- 消息区域 -->
      <div class="flex-1 overflow-y-auto py-6 px-4 md:px-8 space-y-6 bg-gray-50 dark:bg-gray-900">
        <div v-if="messages.length === 0"
             class="h-full flex flex-col items-center justify-center text-center p-6 rounded-xl">
          <div
              class="w-24 h-24 mb-8 rounded-full bg-blue-50 dark:bg-blue-900/20 flex items-center justify-center text-blue-500 dark:text-blue-400 ring-8 ring-blue-100 dark:ring-blue-900/10">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="w-12 h-12">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z"/>
            </svg>
          </div>
          <h3 class="text-3xl font-bold text-gray-900 dark:text-gray-50 mb-4">学子航标</h3>
          <p class="text-gray-600 dark:text-gray-400 max-w-lg mx-auto text-lg leading-relaxed">
            在这里，你可以提出关于安徽信息工程学院的问题或想法。我将尽力为你提供准确、有用的信息。你也可以随时从左侧导航栏选择历史对话，继续你之前的讨论。
          </p>
          <div class="mt-8 grid grid-cols-1 md:grid-cols-2 gap-4 max-w-2xl mx-auto">
            <div
                class="p-4 border border-gray-200 dark:border-gray-700 rounded-xl hover:bg-white dark:hover:bg-gray-800 transition-colors">
              <p class="font-medium mb-2 text-gray-900 dark:text-gray-100 items-center">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-6 items-center">
                  <path stroke-linecap="round" stroke-linejoin="round"
                        d="m9 7.5 3 4.5m0 0 3-4.5M12 12v5.25M15 12H9m6 3H9m12-3a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
                </svg>
                奖励办法
              </p>
              <p class="text-sm text-gray-600 dark:text-gray-400">可以给我介绍一下本学校的奖励办法吗？</p>
            </div>
            <div
                class="p-4 border border-gray-200 dark:border-gray-700 rounded-xl hover:bg-white dark:hover:bg-gray-800 transition-colors">
              <p class="font-medium mb-2 text-gray-900 dark:text-gray-100 items-center">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-6">
                  <path stroke-linecap="round" stroke-linejoin="round"
                        d="M12 21v-8.25M15.75 21v-8.25M8.25 21v-8.25M3 9l9-6 9 6m-1.5 12V10.332A48.36 48.36 0 0 0 12 9.75c-2.551 0-5.056.2-7.5.582V21M3 21h18M12 6.75h.008v.008H12V6.75Z"/>
                </svg>
                校园概况
              </p>
              <p class="text-sm text-gray-600 dark:text-gray-400">可以介绍一下该学校的校园概况吗？</p>
            </div>
          </div>
        </div>

        <template v-else>
          <ChatMessage
              v-for="message in messages"
              :key="message.id"
              :message="message"
          />
        </template>

        <div v-if="isThinking" class="py-1">
          <TypingIndicator/>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="border-t border-gray-200 dark:border-gray-700 p-4 md:p-6 bg-white dark:bg-gray-900 shadow-md">
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

</style>