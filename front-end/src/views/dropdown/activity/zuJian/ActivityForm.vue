<script setup>
import { ref, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import { post } from '@/net/index.js';

const emit = defineEmits(['formSubmitted', 'formCancelled']);

// 活动表单
const activityForm = ref({
  activityName: '',
  location: '',
  organizer: '',
  startTime: '',
  endTime: '',
  image: '',
  maxParticipants: 50,
  description: ''
});

// 图片上传
const imageUrl = ref('');
const handleImageSuccess = (message, data) => {
  activityForm.value.image = data;
  imageUrl.value = data;
  ElMessage.success('图片上传成功');
};

const handleImageUpload = (file) => {
  const formData = new FormData();
  formData.append('image', file);
  
  // 如果已经有图片，添加旧图片路径给后端删除
  if (activityForm.value.image) {
    formData.append('oldImagePath', activityForm.value.image);
  }
  
  post('/api/uploadImg', formData, 
    (message, data) => {
      handleImageSuccess(message, data);
    },
    (error) => {
      ElMessage.error('图片上传失败');
    }
  );
};

// 发布活动
const publishActivity = () => {
  // 表单验证
  if (!activityForm.value.activityName || !activityForm.value.location || 
      !activityForm.value.organizer || !activityForm.value.startTime || 
      !activityForm.value.endTime || !activityForm.value.maxParticipants) {
    ElMessage.warning('请填写完整的活动信息');
    return;
  }

  // 创建一个新对象，将日期字符串转换为可序列化的格式
  const formData = {
    activityName: activityForm.value.activityName,
    location: activityForm.value.location,
    organizer: activityForm.value.organizer,
    startTime: activityForm.value.startTime,
    endTime: activityForm.value.endTime,
    image: activityForm.value.image,
    maxParticipants: activityForm.value.maxParticipants,
    description: activityForm.value.description
  };
  
  emit('formSubmitted', formData);
};

// 重置表单
const resetForm = () => {
  activityForm.value = {
    activityName: '',
    location: '',
    organizer: '',
    startTime: '',
    endTime: '',
    image: '',
    maxParticipants: 50,
    description: ''
  };
  imageUrl.value = '';
};

// 取消发布
const cancelForm = () => {
  resetForm();
  emit('formCancelled');
};

defineExpose({
  resetForm
});
</script>

<template>
  <div class="bg-white dark:bg-gray-800 rounded-xl p-6 mb-8 shadow-lg transition-all duration-500 animate-fade-in">
    <h2 class="text-2xl font-bold mb-6 text-center">发布新活动</h2>
    
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="form-group">
        <label class="form-label">活动名称</label>
        <input v-model="activityForm.activityName" type="text" class="form-input" placeholder="请输入活动名称" />
      </div>
      
      <div class="form-group">
        <label class="form-label">活动地点</label>
        <input v-model="activityForm.location" type="text" class="form-input" placeholder="请输入活动地点" />
      </div>
      
      <div class="form-group">
        <label class="form-label">举办单位</label>
        <input v-model="activityForm.organizer" type="text" class="form-input" placeholder="请输入举办单位" />
      </div>
      
      <div class="form-group">
        <label class="form-label">人数限制</label>
        <input v-model="activityForm.maxParticipants" type="number" class="form-input" placeholder="请输入最大参与人数" />
      </div>
      
      <div class="form-group">
        <label class="form-label">开始时间</label>
        <input v-model="activityForm.startTime" type="datetime-local" class="form-input" />
      </div>
      
      <div class="form-group">
        <label class="form-label">结束时间</label>
        <input v-model="activityForm.endTime" type="datetime-local" class="form-input" />
      </div>
      
      <div class="form-group md:col-span-2">
        <label class="form-label">活动图片</label>
        <div class="mt-2 flex items-center">
          <input type="file" @change="e => handleImageUpload(e.target.files[0])" accept="image/*" class="hidden" id="activity-image" />
          <label for="activity-image" class="upload-btn">
            选择图片
          </label>
          <span v-if="imageUrl" class="ml-4 text-sm text-gray-500">已选择图片</span>
        </div>
        <div v-if="imageUrl" class="mt-4">
          <img :src="`http://localhost:8080/${imageUrl}`" class="w-40 h-40 object-cover rounded-lg" />
        </div>
      </div>
      
      <div class="form-group md:col-span-2">
        <label class="form-label">活动描述</label>
        <textarea v-model="activityForm.description" class="form-textarea" rows="5" placeholder="请输入活动详细描述"></textarea>
      </div>
    </div>
    
    <div class="flex justify-center mt-8">
      <button @click="resetForm" class="cancel-btn">
        重置
      </button>
      <button @click="cancelForm" class="cancel-btn">
        取消
      </button>
      <button @click="publishActivity" class="submit-btn">
        发布活动
      </button>
    </div>
  </div>
</template>

<style scoped>
.form-group {
  margin-bottom: 1rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  @apply text-gray-700 dark:text-gray-300;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border-radius: 0.375rem;
  border: 1px solid;
  @apply border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-800 dark:text-gray-200;
}

.upload-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  cursor: pointer;
  display: inline-block;
  @apply bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600;
}

.cancel-btn {
  margin-right: 1rem;
  padding: 0.5rem 1.5rem;
  border-radius: 0.375rem;
  @apply bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600;
}

.submit-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 0.375rem;
  @apply bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white;
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style> 