<script setup>
import {ref, onMounted, computed, defineProps} from 'vue';
import router from "@/router/index.js";
import Card from "@/components/tooltip/Card.vue";
import Carousel from "@/components/zuJian/Carousel.vue";
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { get } from '@/net/index.js';
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

const router2 = useRouter();

const handleMenuItemClick = (item) => {
  activeItem.value = item;
  router.push(item.path);
};

// 检查当前路由是否匹配菜单项
const isActiveRoute = (path) => {
  return router2.currentRoute.value.path === path;
};

// 处理菜单项悬停
const handleMenuItemHover = (item) => {
  if (!isMenuExpanded.value) {
    activeItem.value = item;
  }
};

// 活动列表
const activities = ref([]);
const allActivities = ref([]); // 存储所有活动，用于筛选
const loading = ref(true);
// 活动作者信息
const activityCreators = ref({});

// 筛选状态
const filterStatus = ref('all'); // 'all', 'ongoing', 'expired'

// 获取活动列表
const getActivities = () => {
  loading.value = true;
  get('/api/activity/list', {}, 
    (message, data) => {
      // 仅显示已审核通过的活动
      allActivities.value = data.filter(activity => activity.status === 1);
      filterActivities(); // 应用筛选
      // 获取每个活动的发布者信息
      allActivities.value.forEach(activity => {
        getActivityCreator(activity.id);
      });
      loading.value = false;
    },
    (error) => {
      ElMessage.error('获取活动列表失败');
      loading.value = false;
    }
  );
};

// 筛选活动
const filterActivities = () => {
  if (filterStatus.value === 'all') {
    activities.value = [...allActivities.value];
  } else if (filterStatus.value === 'ongoing') {
    const now = new Date();
    activities.value = allActivities.value.filter(activity => {
      const startTime = new Date(activity.startTime);
      const endTime = new Date(activity.endTime);
      return now >= startTime && now <= endTime;
    });
  } else if (filterStatus.value === 'expired') {
    const now = new Date();
    activities.value = allActivities.value.filter(activity => {
      const endTime = new Date(activity.endTime);
      return now > endTime;
    });
  } else if (filterStatus.value === 'upcoming') {
    const now = new Date();
    activities.value = allActivities.value.filter(activity => {
      const startTime = new Date(activity.startTime);
      return now < startTime;
    });
  }
};

// 更改筛选状态
const changeFilter = (status) => {
  filterStatus.value = status;
  filterActivities();
};

// 获取活动发布者信息
const getActivityCreator = (activityId) => {
  get(`/api/activity/creator`, { id: activityId }, 
    (message, data) => {
      // 使用Vue的响应式API更新对象
      activityCreators.value = { ...activityCreators.value, [activityId]: data };
    },
    (error) => {
      console.error(`获取活动 ${activityId} 的发布者信息失败:`, error);
    }
  );
};

// 查看活动详情
const viewActivity = (id) => {
  router.push({
    name: 'activityDetail',
    params: { id: id }
  });
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
};

onMounted(() => {
  getActivities();
});


const props = defineProps({
  activeTab: {
    type: String,
    required: true
  },
  isLoggedIn: {
    type: Boolean,
    default: false
  },
  isManager: {
    type: Boolean,
    default: false
  }
});

const handlePublishClick = () => {
  emit('publishClick');
};
</script>

<template>
  <div class="container mx-auto px-4 py-12 animate-on-scroll" id="activities-section">
    <div class="flex justify-between items-center mb-10">
      <div class="text-3xl font-bold gradient-text-alt inline-block px-4 py-2 rounded-lg">
        校园活动
        <div class="h-1 w-24 bg-gradient-to-r from-green-500 to-teal-500 mt-2 rounded-full"></div>
      </div>
      <div class="flex flex-none items-center">
      <div >
        <button @click="router.push('/activity')" class="hover:underline text-teal-500 hover:text-teal-600 font-medium flex items-center transition-all duration-300">
          <span>查看详情</span>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
    </div>

    <!-- 活动筛选按钮 -->
    <div class="flex mb-8 overflow-x-auto space-x-4 py-2">
      <button 
        @click="changeFilter('all')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'all'}"
      >
        全部活动
      </button>
      <button 
        @click="changeFilter('upcoming')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'upcoming'}"
      >
        即将开始
      </button>
      <button 
        @click="changeFilter('ongoing')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'ongoing'}"
      >
        正在进行
      </button>
      <button 
        @click="changeFilter('expired')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'expired'}"
      >
        已结束
      </button>
    </div>

    <div v-if="loading" class="flex justify-center items-center py-12">
      <div class="loader"></div>
    </div>

    <div v-else-if="activities.length === 0" class="text-center py-12">
      <p class="text-gray-500 dark:text-gray-400">暂无符合条件的活动</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <div
          v-for="(activity, index) in activities.slice(0, 3)"
          :key="activity.id"
          class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-md rounded-xl overflow-hidden shadow-md hover:shadow-xl
          transition-all duration-500 transform hover:-translate-y-2 border border-gray-200 dark:border-gray-700 relative"
          @click="viewActivity(activity.id)"
      >
        <!-- 活动状态标签 -->
        <div 
          class="absolute top-3 right-3 z-10 px-3 py-1 rounded-full text-xs font-semibold"
          :class="{
            'bg-green-100 text-green-800': new Date() >= new Date(activity.startTime) && new Date() <= new Date(activity.endTime),
            'bg-red-100 text-red-800': new Date() > new Date(activity.endTime),
            'bg-blue-100 text-blue-800': new Date() < new Date(activity.startTime)
          }"
        >
          {{ new Date() >= new Date(activity.startTime) && new Date() <= new Date(activity.endTime) ? '进行中' : 
             new Date() > new Date(activity.endTime) ? '已结束' : '即将开始' }}
        </div>

        <div class="h-48 bg-gray-200 dark:bg-gray-700 relative overflow-hidden">
          <img 
            v-if="activity.image"
            :src="`http://localhost:8080/${activity.image}`"
            :alt="activity.activityName" 
            class="w-full h-full object-cover"
          />
          <div v-else class="select-none absolute inset-0 bg-gradient-to-br from-green-400/30 to-blue-500/30 flex items-center justify-center text-white text-2xl font-bold">
            {{ activity.activityName }}
          </div>
        </div>
        <div class="p-6 select-none">
          <h3 class="text-xl font-bold mb-3 line-clamp-1">{{ activity.activityName }}</h3>
          <div class="flex items-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2h5z" />
            </svg>
            <span class="text-gray-600 dark:text-gray-300">{{ formatDate(activity.startTime) }}</span>
          </div>
          <div class="flex items-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <span class="text-gray-600 dark:text-gray-300">{{ activity.location }}</span>
          </div>
          <div class="flex items-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span class="text-gray-600 dark:text-gray-300">{{ activity.currentParticipants }}/{{ activity.maxParticipants }} 人</span>
          </div>
          <div class="flex items-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <span class="text-gray-600 dark:text-gray-300">
              发布者: {{ activityCreators[activity.id]?.username || '加载中...' }}
            </span>
          </div>
          <button class="w-full py-2 rounded-lg bg-gradient-to-r from-green-500 to-teal-500 text-white font-medium hover:from-green-600 hover:to-teal-600 transition-all duration-300 transform hover:scale-105">
            查看详情
          </button>
        </div>
      </div>
    </div>
    
  </div>
</template>

<style scoped>
.loader {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left-color: #3b82f6;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.filter-btn {
  @apply px-4 py-2 rounded-full bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600 transition-all duration-300 font-medium whitespace-nowrap;
}

.filter-btn-active {
  @apply bg-teal-500 text-white hover:bg-teal-600;
}
</style>