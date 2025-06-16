<script setup>
import { ref } from "vue";
import { message } from "ant-design-vue";
import { useUserStore } from '@/stores/userStore.js';
import axios from "axios";
import { storeToRefs } from 'pinia';

// 初始化状态和API
const userStore = useUserStore();
const { token } = storeToRefs(userStore);
const avatarInput = ref(null);

// 添加个人简介数据
const props = defineProps({
  personalInfo: {
    type: Object,
    required: true
  }
});

// 头像上传逻辑
const uploadAvatar = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('image', file);
  
  // 如果用户已有头像，添加旧头像路径用于删除
  if (userStore.user?.avatar) {
    formData.append('oldImagePath', userStore.user.avatar);
  }

  try {
    const response = await axios.post(
      '/api/uploadImg',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token.value}`
        }
      }
    );

    if (response.data.success) {
      // 更新用户头像
      const avatarUrl = response.data.data;
      userStore.updateUserAvatar(avatarUrl);
      message.success('头像上传成功');
    } else {
      message.error(response.data.message || '头像上传失败');
    }
  } catch (error) {
    console.error('头像上传错误:', error);
    message.error('头像上传失败');
  }
};

// 处理头像点击事件
const handleAvatarClick = () => {
  avatarInput.value?.click();
};
</script>

<template>
  <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 transition-all duration-300 hover:shadow-lg">
    <div class="flex flex-col md:flex-row items-start md:items-center">
      <div class="flex flex-col md:flex-row items-center mb-4 md:mb-0">
        <!-- 头像上传 -->
        <input type="file" @change="uploadAvatar" accept="image/*" class="hidden" ref="avatarInput" />
        <div class="relative group">
          <div class="w-[130px] h-[130px] rounded-full overflow-hidden ring-4 ring-blue-100 dark:ring-blue-900 transition-all duration-300 group-hover:ring-blue-200 dark:group-hover:ring-blue-800 transform group-hover:scale-105">
            <img
              :src="userStore.user?.avatar ? `http://localhost:8080/${userStore.user.avatar}` : 'https://img95.699pic.com/element/40109/0194.png_300.png'" 
              :alt="userStore.user?.username"
              class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
            />
          </div>
          <div @click="handleAvatarClick"
              class="hover:brightness-75 absolute -bottom-2 left-1/2 cursor-pointer transform -translate-x-1/2 bg-gradient-to-r from-blue-500 to-purple-600 text-white px-4 py-1 rounded-full text-sm font-medium opacity-0 group-hover:opacity-100 transition-all duration-300 whitespace-nowrap">
            修改头像
          </div>
        </div>
        
        <!-- 用户信息 -->
        <div class="md:ml-6 text-center md:text-left">
          <h2 class="text-2xl font-bold text-gray-900 dark:text-white">{{ userStore.user?.username || '用户名' }}</h2>
          <div class="mt-1 flex items-center justify-center md:justify-start">
            <span v-if="userStore.user?.manager === 1" class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-red-900 dark:text-red-300">
              管理员
            </span>
            <span v-else class="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-blue-900 dark:text-blue-300">
              学生
            </span>
          </div>
          
          <!-- 个人简介区域 -->
          <div class="mt-3">
            <div class="bg-white dark:bg-gray-800 rounded-lg p-3 shadow-sm transition-all duration-300 hover:shadow-md border border-gray-100 dark:border-gray-700">
              <div class="flex items-center text-blue-500 dark:text-blue-400 mb-2 border-b border-gray-100 dark:border-gray-700 pb-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="text-sm font-medium">个人简介</span>
              </div>
              <div class="bg-gray-50 dark:bg-gray-700/50 rounded p-2">
                <p class="text-gray-600 dark:text-gray-300 text-sm leading-relaxed">
                  {{ personalInfo.introduction || '这个人很低调，还没有留下简介...' }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="flex-grow"></div>
    </div>
  </div>
</template> 