<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { get, jsonpost } from '@/net/index.js';
import { useUserStore } from '@/stores/userStore.js';

// 导入组件
import ActivityForm from './zuJian/ActivityForm.vue';
import ActivityTabs from './zuJian/ActivityTabs.vue';
import ActivityList from './zuJian/ActivityList.vue';
import ActivityReview from './zuJian/ActivityReview.vue';
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const currentUser = computed(() => userStore.user);
const isManager = computed(() => userStore.user?.manager === 1);

// 活动数据
const activities = ref([]);
const myActivities = ref([]);
const joinedActivities = ref([]);
const pendingActivities = ref([]);
const loading = ref(true);
const pendingLoading = ref(false);
const searchQuery = ref('');
const isManagePage = ref(false);
const adminTab = ref('all');

// 活动标签页
const activeTab = ref('all');

// 表单控制
const showForm = ref(false);
const activityFormRef = ref(null);

// 活动状态筛选逻辑
const allActivities = ref([]);
const filterStatus = ref('all');
const filterDialogVisible = ref(false);

// 获取活动列表
const getActivities = () => {
  loading.value = true;
  get('/api/activity/list', {}, 
    (message, data) => {
      allActivities.value = data.filter(activity => activity.status === 1);
      applyFilters(); // 应用筛选后再赋值给activities
      loading.value = false;
    },
    (error) => {
      ElMessage.error('获取活动列表失败');
      loading.value = false;
    }
  );
};

// 应用筛选器
const applyFilters = () => {
  let filtered = [...allActivities.value];
  
  // 先应用活动状态筛选
  if (isManagePage.value) {
    // 管理页面 - 按审核状态筛选
    if (adminTab.value === 'pending') {
      filtered = filtered.filter(a => a.status === 0);
    } else if (adminTab.value === 'approved') {
      filtered = filtered.filter(a => a.status === 1);
    } else if (adminTab.value === 'rejected') {
      filtered = filtered.filter(a => a.status === 2);
    }
  } else {
    // 只显示已审核通过的活动
    filtered = filtered.filter(a => a.status === 1);
    
    // 应用时间筛选
    if (filterStatus.value === 'ongoing') {
      const now = new Date();
      filtered = filtered.filter(activity => {
        const startTime = new Date(activity.startTime);
        const endTime = new Date(activity.endTime);
        return now >= startTime && now <= endTime;
      });
    } else if (filterStatus.value === 'expired') {
      const now = new Date();
      filtered = filtered.filter(activity => {
        const endTime = new Date(activity.endTime);
        return now > endTime;
      });
    } else if (filterStatus.value === 'upcoming') {
      const now = new Date();
      filtered = filtered.filter(activity => {
        const startTime = new Date(activity.startTime);
        return now < startTime;
      });
    }
  }
  
  // 应用搜索筛选
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase().trim();
    filtered = filtered.filter(a => 
      a.activityName.toLowerCase().includes(query) || 
      a.location.toLowerCase().includes(query) ||
      a.organizer.toLowerCase().includes(query) ||
      (a.description && a.description.toLowerCase().includes(query))
    );
  }
  
  activities.value = filtered;
};

// 更改筛选状态
const changeFilterStatus = (status) => {
  filterStatus.value = status;
  applyFilters();
};

// 显示筛选确认弹窗
const showFilterDialog = () => {
  filterDialogVisible.value = true;
};

// 确认应用筛选
const confirmFilter = () => {
  applyFilters();
  filterDialogVisible.value = false;
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
};

// 判断活动状态
const getActivityStatus = (activity) => {
  const now = new Date();
  const startTime = new Date(activity.startTime);
  const endTime = new Date(activity.endTime);
  
  if (now < startTime) {
    return { status: 'upcoming', text: '即将开始', class: 'bg-blue-100 text-blue-800' };
  } else if (now >= startTime && now <= endTime) {
    return { status: 'ongoing', text: '进行中', class: 'bg-green-100 text-green-800' };
  } else {
    return { status: 'expired', text: '已结束', class: 'bg-red-100 text-red-800' };
  }
};

// 获取我的活动
const getMyActivities = () => {
  get('/api/activity/my', {}, 
    (message, data) => {
      myActivities.value = data;
    },
    (error) => {
      ElMessage.error('获取我的活动失败');
    }
  );
};

// 获取我参加的活动
const getJoinedActivities = () => {
  get('/api/activity/joined', {}, 
    (message, data) => {
      joinedActivities.value = data;
    },
    (error) => {
      ElMessage.error('获取参加的活动失败');
    }
  );
};

// 获取待审核的活动（管理员）
const getPendingActivities = () => {
  if (!isManager.value) return;
  
  pendingLoading.value = true;
  get('/api/activity/pending', {}, 
    (message, data) => {
      pendingActivities.value = data;
      pendingLoading.value = false;
    },
    (error) => {
      ElMessage.error('获取待审核活动失败');
      pendingLoading.value = false;
    }
  );
};

// 处理活动审核完成
const handleReviewCompleted = (activityId, status) => {
  // 从待审核列表中移除
  pendingActivities.value = pendingActivities.value.filter(activity => activity.id !== activityId);
  
  // 更新活动列表
  getActivities();
};

// 发布活动
const handleFormSubmit = (formData) => {
  jsonpost('/api/activity/publish', formData, 
    (message, data) => {
      ElMessage.success('活动发布成功，等待审核');
      showForm.value = false;
      getActivities();
      if (currentUser.value) {
        getMyActivities();
      }
      if (isManager.value) {
        getPendingActivities();
      }
    },
    (error) => {
      ElMessage.error('活动发布失败：' + error);
    }
  );
};

// 取消发布表单
const handleFormCancel = () => {
  showForm.value = false;
};

// 切换标签页
const handleTabChange = (tab) => {
  activeTab.value = tab;
  
  // 如果切换到待审核标签，获取待审核活动
  if (tab === 'pending' && isManager.value) {
    getPendingActivities();
  }
};

// 发布按钮点击
const handlePublishClick = () => {
  showForm.value = !showForm.value;
  if (!showForm.value && activityFormRef.value) {
    activityFormRef.value.resetForm();
  }
};

// 查看活动详情
const viewActivity = (id) => {
  router.push({
    name: 'activityDetail',
    params: { id: id }
  });
};

// 在活动详情页面参加活动后，需要刷新"我参加的"活动列表
const refreshJoinedActivities = () => {
  if (currentUser.value) {
    getJoinedActivities();
  }
};

// onMounted钩子
onMounted(() => {
  // 检查是否是管理页面
  isManagePage.value = route.path.includes('manage');
  
  // 根据页面类型设置选项卡
  if (isManagePage.value) {
    adminTab.value = 'pending';
  }
  
  // 获取活动列表
  getActivities();
  
  // 如果是已登录用户，获取用户活动信息
  if (userStore.user) {
    getMyActivities();
    getJoinedActivities();
  }
  
  // 如果是管理员，获取待审核活动
  if (isManager.value) {
    getPendingActivities();
  }
  
  // 添加全局事件监听，监听参加活动成功事件
  window.addEventListener('activity-joined', refreshJoinedActivities);
});

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('activity-joined', refreshJoinedActivities);
});
</script>

<template>
  <div class="activity-container mx-auto px-4 py-8">
    <!-- 页面标题 -->
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold mb-4 text-gradient">校园活动中心</h1>
      <p class="text-gray-600 dark:text-gray-300 max-w-2xl mx-auto">
        发现精彩校园活动，结交志同道合的朋友，丰富你的大学生活
      </p>
    </div>
    
    <!-- 活动标签页 -->
    <ActivityTabs 
      :activeTab="activeTab"
      :isLoggedIn="!!currentUser"
      :isManager="isManager"
      @tab-change="handleTabChange"
      @publish-click="handlePublishClick"
    />
    
    <!-- 活动发布表单 -->
    <ActivityForm 
      v-if="showForm"
      ref="activityFormRef"
      @form-submitted="handleFormSubmit"
      @form-cancelled="handleFormCancel"
    />
    
    <!-- 活动筛选按钮（非管理页面且在"全部活动"标签页） -->
    <div v-if="!isManagePage && activeTab === 'all'" class="flex mb-8 overflow-x-auto space-x-4 py-2">
      <button 
        @click="changeFilterStatus('all')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'all'}"
      >
        全部活动
      </button>
      <button 
        @click="changeFilterStatus('upcoming')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'upcoming'}"
      >
        即将开始
      </button>
      <button 
        @click="changeFilterStatus('ongoing')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'ongoing'}"
      >
        正在进行
      </button>
      <button 
        @click="changeFilterStatus('expired')" 
        class="filter-btn"
        :class="{'filter-btn-active': filterStatus === 'expired'}"
      >
        已结束
      </button>
    </div>
    
    <!-- 全部活动列表 -->
    <div v-if="activeTab === 'all'">
    <ActivityList
      :activities="activities"
      :loading="loading"
        :emptyText="'暂无符合条件的活动'"
      @view-activity="viewActivity"
    />
    </div>
    
    <!-- 我发布的活动列表 -->
    <div v-if="activeTab === 'my'">
    <ActivityList
      :activities="myActivities"
      :loading="false"
      :showStatus="true"
      :emptyText="'您还没有发布过活动'"
      @view-activity="viewActivity"
    />
    </div>
    
    <!-- 我参加的活动列表 -->
    <div v-if="activeTab === 'joined'">
    <ActivityList
      :activities="joinedActivities"
      :loading="false"
      :emptyText="'您还没有参加任何活动'"
      @view-activity="viewActivity"
    />
    </div>
    
    <!-- 待审核活动列表（管理员） -->
    <div v-if="activeTab === 'pending' && isManager">
      <div v-if="pendingLoading" class="flex justify-center items-center py-12">
        <div class="loader"></div>
      </div>
      
      <div v-else>
        <div v-if="pendingActivities.length > 0" class="pending-activities">
          <h2 class="text-xl font-bold mb-6 text-yellow-600 dark:text-yellow-400">
            <i class="fas fa-clock mr-2"></i>待审核活动 ({{ pendingActivities.length }})
          </h2>
          
          <ActivityReview 
            v-for="activity in pendingActivities" 
            :key="activity.id"
            :activity="activity"
            @review-completed="handleReviewCompleted"
          />
        </div>
        
        <div v-else class="text-center py-12">
          <p class="text-gray-500 dark:text-gray-400">暂无待审核活动</p>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 筛选确认弹窗 -->
  <CustomConfirmDialog
    v-model:visible="filterDialogVisible"
    title="筛选活动"
    content="确定要应用筛选条件吗？"
    type="primary"
    confirm-text="应用筛选"
    cancel-text="取消"
    @confirm="confirmFilter"
  />
</template>

<style scoped>
.activity-container {
  max-width: 1200px;
}

.text-gradient {
  background: linear-gradient(to right, #3b82f6, #10b981);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

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

.pending-activities {
  background-color: rgba(255, 244, 229, 0.5);
  border-radius: 1rem;
  padding: 1.5rem;
  @apply dark:bg-gray-800 dark:bg-opacity-60;
}

.filter-btn {
  @apply px-4 py-2 rounded-full bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600 transition-all duration-300 font-medium whitespace-nowrap;
}

.filter-btn-active {
  @apply bg-teal-500 text-white hover:bg-teal-600;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>