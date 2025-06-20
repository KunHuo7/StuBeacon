<script setup>
import axios from 'axios';
import {onMounted, reactive} from "vue";
import { useUserStore } from '@/stores/userStore.js';
import MbtiRadarCharts from "@/components/zuJian/MbtiRadarCharts.vue";
import { message } from 'ant-design-vue';
import MbtiGuide from "@/views/dropdown/about/mbti-guide.vue";

const userStore = useUserStore();
const [messageApi, contextHolder] = message.useMessage();

const personalInfo = reactive({
  sex: null,
  age: null,
  hobby: '',
  major: '',
  type: '',
  introduction: '',
  contact: ''
});

const fetchPersonalInfo = async () => {
  try {
    if (userStore.user?.id) {
      const response = await axios.get('/api/personal/get', {
        headers: {
          'Authorization': localStorage.getItem('authToken'),
          'UserId': userStore.user.id
        },
        withCredentials: true
      });
      if (response.data && response.data.success) {
        const data = response.data.data;
        Object.assign(personalInfo, data);
        messageApi.success(response.data.message);
      } else {
        messageApi.warning(response.data?.message || '获取个人信息失败');
      }
    }
  } catch (error) {
    console.error('获取个人信息错误:', error);
    messageApi.error(error.response?.data?.message || '获取个人信息失败');
  }
};

onMounted(() => {
  fetchPersonalInfo();
});
</script>

<template>
  <contextHolder />
  <div class="bg-white dark:bg-gray-800 rounded-xl shadow-xl p-6 space-y-6">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-900 dark:text-white">个人信息</h2>
      <div class="flex items-center space-x-2">
        <div>
          <mbti-guide/>
        </div>
        <div>
          <div class="text-sm font-medium text-gray-900 dark:text-white">{{ userStore.user?.username }}</div>
          <div class="text-sm text-gray-500 dark:text-gray-400">ID: {{ userStore.user?.id }}</div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <div class="h-14"></div>
        <div class="grid grid-cols-2 gap-8">
          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">性别</div>
            <div class="font-medium text-gray-900 dark:text-white">{{ personalInfo.sex === 1 ? '男' : personalInfo.sex === 0 ? '女' : '未设置' }}</div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">年龄</div>
            <div class="font-medium text-gray-900 dark:text-white">{{ personalInfo.age || '未设置' }}</div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">爱好</div>
            <div class="font-medium text-gray-900 dark:text-white">{{ personalInfo.hobby || '未设置' }}</div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">专业班级</div>
            <div class="font-medium text-gray-900 dark:text-white">{{ personalInfo.major || '未设置' }}</div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">MBTI</div>
            <div class="font-medium text-gray-900 dark:text-white text-center">{{ personalInfo.type || '未设置' }}</div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 transition-all duration-300 hover:shadow-md">
            <div class="text-sm text-gray-500 dark:text-gray-400 mb-1">联系方式</div>
            <div class="font-medium text-gray-900 dark:text-white">{{ personalInfo.contact || '未设置' }}</div>
          </div>
        </div>

      </div>

      <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4">
        <MbtiRadarCharts :mbtiType="personalInfo.type"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>