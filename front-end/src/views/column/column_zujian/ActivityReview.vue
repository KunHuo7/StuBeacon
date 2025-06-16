<script setup>
import { ref, onMounted, defineEmits } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { useUserStore } from '@/stores/userStore.js';

const userStore = useUserStore();
const { token } = storeToRefs(userStore);

const emit = defineEmits(['review-completed']);

// 待审核活动列表
const pendingActivities = ref([]);
const isLoadingActivities = ref(false);

// 获取待审核活动
const fetchPendingActivities = async () => {
  isLoadingActivities.value = true;
  try {
    const result = await axios.get(
      'http://43.138.75.58:8080/api/activity/all-applying',
      {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      }
    );
    if (result.data.code === 200) {
      pendingActivities.value = result.data.data;
    } else {
      message.error(result.data.message || '获取待审核活动失败');
    }
  } catch (error) {
    console.error('获取待审核活动失败', error);
    message.error('获取待审核活动失败');
  } finally {
    isLoadingActivities.value = false;
  }
};

// 审核活动
const reviewActivity = async (id, status) => {
  try {
    const result = await axios.post(
      'http://43.138.75.58:8080/api/activity/review',
      {
        activityId: id,
        status: status
      },
      {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      }
    );
    if (result.data.code === 200) {
      message.success(status === 1 ? '已通过活动申请' : '已拒绝活动申请');
      // 刷新活动列表
      fetchPendingActivities();
      // 发送审核完成事件
      emit('review-completed');
    } else {
      message.error(result.data.message || '操作失败');
    }
  } catch (error) {
    console.error('活动审核失败', error);
    message.error('活动审核失败');
  }
};

// 组件挂载时获取待审核活动
onMounted(() => {
  if (userStore.user?.manager === 1) {
    fetchPendingActivities();
  }
});
</script>

<template>
  <!-- 活动审核 UI -->
  <div class="rounded-lg overflow-hidden bg-white dark:bg-gray-800 shadow-md">
    <!-- 加载状态 -->
    <div v-if="isLoadingActivities" class="p-10 flex justify-center items-center">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
    </div>

    <!-- 无待审核活动 -->
    <div v-else-if="pendingActivities.length === 0" 
         class="p-10 flex flex-col items-center justify-center text-gray-400 dark:text-gray-500">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-16 h-16 mb-4 opacity-50">
        <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 002.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 00-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 00.75-.75 2.25 2.25 0 00-.1-.664m-5.8 0A2.251 2.251 0 0113.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25zM6.75 12h.008v.008H6.75V12zm0 3h.008v.008H6.75V15zm0 3h.008v.008H6.75V18z" />
      </svg>
      <span class="text-lg">暂无待审核活动</span>
    </div>

    <!-- 待审核活动列表 -->
    <div v-else class="divide-y divide-gray-100 dark:divide-gray-700">
      <div v-for="activity in pendingActivities" 
           :key="activity.id" 
           class="p-6 hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors">
        <div class="flex justify-between items-start mb-3">
          <h3 class="text-xl font-semibold text-gray-900 dark:text-gray-100">{{ activity.activityName }}</h3>
          <span class="bg-yellow-100 text-yellow-800 text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-yellow-900 dark:text-yellow-300">待审核</span>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
          <div class="flex items-center">
            <span class="text-gray-500 dark:text-gray-400 mr-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5" />
              </svg>
            </span>
            <span class="text-gray-700 dark:text-gray-300">创建时间: {{ new Date(activity.createTime).toLocaleString() }}</span>
          </div>

          <div class="flex items-center">
            <span class="text-gray-500 dark:text-gray-400 mr-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z" />
              </svg>
            </span>
            <span class="text-gray-700 dark:text-gray-300">地点: {{ activity.place }}</span>
          </div>

          <div class="flex items-center">
            <span class="text-gray-500 dark:text-gray-400 mr-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
              </svg>
            </span>
            <span class="text-gray-700 dark:text-gray-300">组织者: {{ activity.organizer }}</span>
          </div>

          <div class="flex items-center">
            <span class="text-gray-500 dark:text-gray-400 mr-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z" />
              </svg>
            </span>
            <span class="text-gray-700 dark:text-gray-300">参与人数限制: {{ activity.maxNum }}</span>
          </div>
        </div>

        <div class="mb-4">
          <h4 class="text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">活动描述:</h4>
          <p class="text-gray-600 dark:text-gray-400 bg-gray-50 dark:bg-gray-700/50 p-3 rounded-lg">
            {{ activity.description }}
          </p>
        </div>

        <div class="flex justify-end space-x-3">
          <button @click="reviewActivity(activity.id, 2)" 
                  class="px-4 py-2 bg-red-100 text-red-700 rounded-md hover:bg-red-200 dark:bg-red-900/30 dark:text-red-300 dark:hover:bg-red-900/50 transition-colors">
            拒绝
          </button>
          <button @click="reviewActivity(activity.id, 1)" 
                  class="px-4 py-2 bg-green-100 text-green-700 rounded-md hover:bg-green-200 dark:bg-green-900/30 dark:text-green-300 dark:hover:bg-green-900/50 transition-colors">
            通过
          </button>
        </div>
      </div>
    </div>
  </div>
</template> 