<script setup>
import { defineProps, defineEmits, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { jsonpost } from '@/net/index.js';
import CustomConfirmDialog from '../../../../components/zuJian/TanChuang/CustomConfirmDialog.vue';

const props = defineProps({
  activity: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['reviewCompleted']);

// 弹窗状态
const approveDialogVisible = ref(false);
const rejectDialogVisible = ref(false);

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
};

// 审核通过
const approveActivity = () => {
  approveDialogVisible.value = true;
};

// 审核拒绝
const rejectActivity = () => {
  rejectDialogVisible.value = true;
};

// 确认审核通过
const confirmApproveActivity = () => {
  jsonpost('/api/activity/review', {
    id: props.activity.id,
    status: 1 // 通过
  }, 
  (message, data) => {
    ElMessage.success('活动审核已通过');
    emit('reviewCompleted', props.activity.id, 1);
  },
  (error) => {
    ElMessage.error('操作失败：' + error);
  });
};

// 确认审核拒绝
const confirmRejectActivity = () => {
  jsonpost('/api/activity/review', {
    id: props.activity.id,
    status: 2 // 拒绝
  }, 
  (message, data) => {
    ElMessage.success('已拒绝该活动');
    emit('reviewCompleted', props.activity.id, 2);
  },
  (error) => {
    ElMessage.error('操作失败：' + error);
  });
};
</script>

<template>
  <div class="review-card bg-white dark:bg-gray-800 rounded-xl shadow-md p-6 mb-6">
    <div class="flex items-start">
      <!-- 活动图片 -->
      <div class="w-1/4 mr-6">
        <img 
          v-if="activity.image"
          :src="`http://localhost:8080/${activity.image}`"
          :alt="activity.activityName" 
          class="w-full h-32 object-cover rounded-lg"
        />
        <div v-else class="w-full h-32 bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center rounded-lg">
          <span class="text-white text-lg font-bold">{{ activity.activityName }}</span>
        </div>
      </div>
      
      <!-- 活动详情 -->
      <div class="w-3/4">
        <h3 class="text-xl font-bold mb-2">{{ activity.activityName }}</h3>
        
        <div class="grid grid-cols-2 gap-x-4 gap-y-2 mb-4">
          <div class="flex items-center text-sm text-gray-600 dark:text-gray-400">
            <i class="fas fa-map-marker-alt mr-2 text-red-500"></i>
            <span>{{ activity.location }}</span>
          </div>
          
          <div class="flex items-center text-sm text-gray-600 dark:text-gray-400">
            <i class="fas fa-calendar mr-2 text-green-500"></i>
            <span>{{ formatDate(activity.startTime) }}</span>
          </div>
          
          <div class="flex items-center text-sm text-gray-600 dark:text-gray-400">
            <i class="fas fa-users mr-2 text-blue-500"></i>
            <span>{{ activity.maxParticipants }}人</span>
          </div>
          
          <div class="flex items-center text-sm text-gray-600 dark:text-gray-400">
            <i class="fas fa-building mr-2 text-purple-500"></i>
            <span>{{ activity.organizer }}</span>
          </div>
        </div>
        
        <p class="text-gray-700 dark:text-gray-300 mb-4 line-clamp-2">{{ activity.description }}</p>
        
        <div class="flex justify-end mt-4">
          <button @click="rejectActivity" class="reject-btn">拒绝</button>
          <button @click="approveActivity" class="approve-btn">通过</button>
        </div>
      </div>
    </div>
    
    <!-- 通过活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="approveDialogVisible"
      title="审核确认"
      content="确定通过该活动审核吗？通过后活动将公开展示"
      type="success"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="confirmApproveActivity"
    />
    
    <!-- 拒绝活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="rejectDialogVisible"
      title="审核确认"
      content="确定拒绝该活动吗？该操作将通知活动发布者"
      type="warning"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="confirmRejectActivity"
    />
  </div>
</template>

<style scoped>
.review-card {
  transition: all 0.3s ease;
}

.review-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.reject-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 0.375rem;
  margin-right: 1rem;
  @apply bg-red-100 text-red-700 hover:bg-red-200;
}

.approve-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 0.375rem;
  @apply bg-green-100 text-green-700 hover:bg-green-200;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
 