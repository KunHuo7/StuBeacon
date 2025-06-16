<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  activity: {
    type: Object,
    required: true
  },
  showStatus: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['cardClick']);

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
};

// 活动状态
const activityStatus = (status) => {
  switch(status) {
    case 0: return '待审核';
    case 1: return '已通过';
    case 2: return '已拒绝';
    default: return '未知';
  }
};

const handleCardClick = () => {
  emit('cardClick', props.activity.id);
};
</script>

<template>
  <div class="activity-card" @click="handleCardClick">
    <div class="relative h-48 overflow-hidden rounded-t-xl">
      <div v-if="showStatus && activity.status !== undefined" 
        class="absolute top-2 right-2 px-2 py-1 rounded text-xs font-medium z-10" 
        :class="{
          'bg-yellow-100 text-yellow-800': activity.status === 0, 
          'bg-green-100 text-green-800': activity.status === 1,
          'bg-red-100 text-red-800': activity.status === 2
        }">
        {{ activityStatus(activity.status) }}
      </div>
      <img 
        v-if="activity.image"
        :src="`http://localhost:8080/${activity.image}`"
        :alt="activity.activityName" 
        class="w-full h-full object-cover"
      />
      <div v-else class="w-full h-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center">
        <span class="text-white text-xl font-bold">{{ activity.activityName }}</span>
      </div>
    </div>
    <div class="p-5">
      <h3 class="text-xl font-bold mb-2 line-clamp-1">{{ activity.activityName }}</h3>
      
      <div class="flex items-center text-sm text-gray-600 dark:text-gray-400 mb-2">
        <i class="fas fa-map-marker-alt mr-2 text-red-500"></i>
        <span>{{ activity.location }}</span>
      </div>
      
      <div class="flex items-center text-sm text-gray-600 dark:text-gray-400 mb-2">
        <i class="fas fa-calendar mr-2 text-green-500"></i>
        <span>{{ formatDate(activity.startTime) }}</span>
      </div>
      
      <div class="flex items-center text-sm text-gray-600 dark:text-gray-400 mb-4">
        <i class="fas fa-users mr-2 text-blue-500"></i>
        <span>{{ activity.currentParticipants }}/{{ activity.maxParticipants }}人</span>
      </div>
      
      <p class="text-gray-700 dark:text-gray-300 mb-4 line-clamp-2">{{ activity.description }}</p>
      
      <button class="view-btn w-full">
        查看详情
      </button>
    </div>
  </div>
</template>

<style scoped>
.activity-card {
  @apply bg-white dark:bg-gray-800 rounded-xl shadow-md hover:shadow-lg transition-all duration-300 overflow-hidden cursor-pointer;
}

.view-btn {
  padding: 0.5rem 0;
  border-radius: 0.375rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 