<script setup>
import { ref, computed } from 'vue';
import { format, isToday, isYesterday, isThisWeek, isThisYear } from 'date-fns';

const props = defineProps({
  conversations: {
    type: Array,
    default: () => []
  },
  currentId: {
    type: [String, Number],
    default: null
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  error: {
    type: String,
    default: null
  }
});

const emit = defineEmits(['select', 'delete']);

// 处理删除确认
const confirmDelete = ref(null);

// 选择对话
const selectConversation = (id) => {
  emit('select', id);
};

// 格式化日期
const formatDate = (dateString) => {
  try {
    const date = new Date(dateString);
    
    if (isToday(date)) {
      return format(date, 'HH:mm');
    } else if (isYesterday(date)) {
      return '昨天';
    } else if (isThisWeek(date)) {
      const day = date.getDay();
      const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      return days[day];
    } else if (isThisYear(date)) {
      return format(date, 'MM月dd日');
    } else {
      return format(date, 'yyyy年MM月dd日');
    }
  } catch (error) {
    return '';
  }
};

// 确认删除
const confirmDeleteConversation = (id, event) => {
  event.stopPropagation();
  
  // 如果已经有确认删除对话，先取消
  if (confirmDelete.value !== null) {
    confirmDelete.value = null;
  }
  
  // 设置当前确认删除的对话
  confirmDelete.value = id;
  
  // 5秒后自动取消确认
  setTimeout(() => {
    if (confirmDelete.value === id) {
      confirmDelete.value = null;
    }
  }, 5000);
};

// 执行删除
const deleteConversation = (id, event) => {
  event.stopPropagation();
  emit('delete', id);
  confirmDelete.value = null;
};

// 取消删除
const cancelDelete = (event) => {
  event.stopPropagation();
  confirmDelete.value = null;
};

// 对话排序 - 按日期倒序
const sortedConversations = computed(() => {
  return [...props.conversations].sort((a, b) => {
    return new Date(b.date) - new Date(a.date);
  });
});

// 获取对话摘要或最后消息的预览文本
const getPreviewText = (conversation) => {
  // 优先显示摘要，如果没有则显示最后一条消息
  if (conversation.summary && conversation.summary.trim() !== '') {
    return conversation.summary;
  } else {
    return conversation.lastMessage || '';
  }
};
</script>

<template>
  <div>
    <!-- 加载状态 -->
    <div v-if="isLoading" class="flex flex-col items-center justify-center py-6 text-gray-500 dark:text-gray-400">
      <svg class="animate-spin h-8 w-8 mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <span>加载对话历史...</span>
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="bg-red-50 dark:bg-red-900/20 text-red-600 dark:text-red-400 p-4 rounded-lg text-center">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 mx-auto mb-2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
      </svg>
      <p>{{ error }}</p>
      <button class="mt-2 px-3 py-1 bg-red-100 dark:bg-red-800/30 rounded-md hover:bg-red-200 dark:hover:bg-red-800/50 transition-colors">
        重试
      </button>
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="conversations.length === 0" class="flex flex-col items-center justify-center py-8 text-gray-500 dark:text-gray-400 text-center">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-10 h-10 mb-3 text-gray-400 dark:text-gray-500">
        <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 13.5h3.86a2.25 2.25 0 012.012 1.244l.256.512a2.25 2.25 0 002.013 1.244h3.218a2.25 2.25 0 002.013-1.244l.256-.512a2.25 2.25 0 012.013-1.244h3.859m-19.5.338V18a2.25 2.25 0 002.25 2.25h15A2.25 2.25 0 0021.75 18v-4.162c0-.224-.034-.447-.1-.661L19.24 5.338a2.25 2.25 0 00-2.15-1.588H6.911a2.25 2.25 0 00-2.15 1.588L2.35 13.177a2.25 2.25 0 00-.1.661z" />
      </svg>
      <p>暂无对话历史</p>
      <p class="mt-1 text-sm">开始一个新对话吧</p>
    </div>
    
    <!-- 对话列表 -->
    <div v-else class="space-y-1.5">
      <div 
        v-for="conversation in sortedConversations" 
        :key="conversation.id"
        class="relative rounded-lg overflow-hidden transition-all duration-200 cursor-pointer"
        :class="[
          conversation.id === currentId 
            ? 'bg-blue-100 dark:bg-blue-900/30 text-blue-800 dark:text-blue-100' 
            : 'hover:bg-gray-100 dark:hover:bg-gray-700/50 text-gray-800 dark:text-gray-200'
        ]"
        @click="selectConversation(conversation.id)"
      >
        <!-- 正常状态：显示对话信息 -->
        <div v-if="confirmDelete !== conversation.id" class="flex justify-between p-2">
          <div class="flex-1 min-w-0">
            <div class="font-medium truncate">{{ conversation.title }}</div>
            <div class="text-xs text-gray-500 dark:text-gray-400 flex justify-between mt-1">
              <span class="truncate flex-1">{{ getPreviewText(conversation) }}</span>
              <span class="ml-2 whitespace-nowrap">{{ formatDate(conversation.date) }}</span>
            </div>
          </div>
          
          <!-- 标签列表 -->
          <div v-if="conversation.tags && conversation.tags.length > 0" class="flex gap-1 mt-1 flex-wrap">
            <span 
              v-for="(tag, index) in conversation.tags" 
              :key="index"
              class="text-xs px-1.5 py-0.5 rounded-full bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300"
            >
              {{ tag }}
            </span>
          </div>
          
          <!-- 删除按钮 -->
          <button 
            class="ml-2 p-1.5 self-start rounded-full opacity-0 group-hover:opacity-100 hover:bg-gray-200 dark:hover:bg-gray-600 text-gray-500 dark:text-gray-400 transition-all"
            :class="{ 'opacity-100': conversation.id === currentId }"
            @click="confirmDeleteConversation(conversation.id, $event)"
            aria-label="删除对话"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
            </svg>
          </button>
        </div>
        
        <!-- 确认删除状态 -->
        <div v-else class="flex items-center justify-between p-2 bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-300 animate-pulse">
          <span class="text-sm font-medium">确认删除?</span>
          <div class="flex gap-1">
            <button 
              class="p-1.5 rounded bg-red-200 dark:bg-red-800/50 hover:bg-red-300 dark:hover:bg-red-700 transition-colors"
              @click="deleteConversation(conversation.id, $event)"
              aria-label="确认删除"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5" />
              </svg>
            </button>
            <button 
              class="p-1.5 rounded bg-gray-200 dark:bg-gray-700 hover:bg-gray-300 dark:hover:bg-gray-600 text-gray-700 dark:text-gray-300 transition-colors"
              @click="cancelDelete($event)"
              aria-label="取消删除"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template> 