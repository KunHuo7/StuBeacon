<script setup>
import { defineProps, ref, computed } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { useUserStore } from '@/stores/userStore.js';

const props = defineProps({
  typeId: {
    type: Number,
    required: true
  },
  notifications: {
    type: Array,
    required: true
  },
  isLoading: {
    type: Boolean,
    default: false
  }
});

const userStore = useUserStore();
const { token } = storeToRefs(userStore);

// 根据通知类型过滤通知
const filteredNotifications = computed(() => {
  return props.notifications.filter(item => item.type === props.typeId);
});

// 获取时间差
const getTimeDiff = (timestamp) => {
  const currentTime = new Date().getTime();
  const messageTime = new Date(timestamp).getTime();
  const diff = currentTime - messageTime;
  
  const seconds = Math.floor(diff / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  
  if (days > 0) {
    return `${days}天前`;
  } else if (hours > 0) {
    return `${hours}小时前`;
  } else if (minutes > 0) {
    return `${minutes}分钟前`;
  } else {
    return "刚刚";
  }
};

// 将未读通知标记为已读
const markAsRead = async (notificationId) => {
  try {
    const result = await axios.post(
      `http://43.138.75.58:8080/api/notification/read`,
      { notificationId: notificationId },
      {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      }
    );
    if (result.data.code === 200) {
      // 成功标记为已读
      const notification = props.notifications.find(n => n.id === notificationId);
      if (notification) {
        notification.isRead = 1;
      }
    } else {
      message.error(result.data.message || '操作失败');
    }
  } catch (error) {
    console.error('标记已读失败', error);
    message.error('标记已读失败');
  }
};
</script>

<template>
  <!-- 加载状态 -->
  <div v-if="isLoading" class="flex justify-center items-center p-10">
    <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
  </div>
  
  <!-- 通知列表为空 -->
  <div v-else-if="filteredNotifications.length === 0" 
       class="p-10 flex flex-col items-center justify-center text-gray-400 dark:text-gray-500">
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-16 h-16 mb-4 opacity-50">
      <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
    </svg>
    <span class="text-lg">暂无{{ typeId === 0 ? '点赞' : '评论' }}消息</span>
  </div>
  
  <!-- 通知列表 -->
  <div v-else class="divide-y divide-gray-100 dark:divide-gray-700">
    <div v-for="notification in filteredNotifications" 
         :key="notification.id"
         :class="[
           'p-4 flex hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors duration-200 relative',
           notification.isRead === 0 ? 'bg-blue-50 dark:bg-blue-900/10' : ''
         ]">
      <!-- 未读标记 -->
      <div v-if="notification.isRead === 0" 
           class="absolute top-4 left-0 w-2 h-2 bg-blue-500 rounded-full transform -translate-x-1/2">
      </div>
      
      <!-- 头像 -->
      <img :src="notification.senderAvatar" 
           class="w-12 h-12 rounded-full object-cover mr-4 border border-gray-200 dark:border-gray-700" 
           alt="用户头像">
      
      <div class="flex-1">
        <div class="flex justify-between items-start mb-1">
          <!-- 发送者姓名 -->
          <h4 class="font-medium text-gray-800 dark:text-gray-200">
            {{ notification.senderName }}
            <span class="ml-2 text-sm font-normal text-gray-500 dark:text-gray-400">
              {{ notification.type === 0 ? '赞了你的帖子' : '评论了你的帖子' }}
            </span>
          </h4>
          
          <!-- 时间 -->
          <span class="text-xs text-gray-500 dark:text-gray-400 whitespace-nowrap">
            {{ getTimeDiff(notification.time) }}
          </span>
        </div>
        
        <!-- 帖子内容预览 -->
        <p class="text-gray-600 dark:text-gray-300 text-sm line-clamp-2 mb-2">
          {{ notification.postContent }}
        </p>
        
        <!-- 评论内容 (仅评论类型显示) -->
        <div v-if="notification.type === 2" 
             class="bg-gray-50 dark:bg-gray-800 p-2 rounded text-sm text-gray-700 dark:text-gray-300 mt-1 border-l-2 border-blue-400 dark:border-blue-500">
          {{ notification.commentContent }}
        </div>
        
        <!-- 查看详情和标记已读按钮 -->
        <div class="flex justify-between items-center mt-2">
          <a :href="`/detail/${notification.postId}`" 
             class="text-sm text-blue-500 hover:text-blue-700 dark:hover:text-blue-400 font-medium">
            查看详情
          </a>
          
          <button v-if="notification.isRead === 0"
                  @click="markAsRead(notification.id)"
                  class="text-xs px-2 py-1 bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 rounded hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors">
            标记为已读
          </button>
        </div>
      </div>
    </div>
  </div>
</template> 