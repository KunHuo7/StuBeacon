<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { get, post, jsonpost } from '@/net/index.js';
import { useUserStore } from '@/stores/userStore.js';
import {baseURL} from "@/main.js";
import CustomConfirmDialog from '../../../components/zuJian/TanChuang/CustomConfirmDialog.vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const currentUser = computed(() => userStore.user);

const activityId = computed(() => route.params.id);
const activity = ref(null);
const loading = ref(true);
const hasJoined = ref(false);
const isCreator = ref(false);
const canManage = computed(() => 
  currentUser.value && 
  (isCreator.value || (currentUser.value.manager && currentUser.value.manager === 1))
);

// 活动发布者信息
const creator = ref(null);
// 活动参与者列表
const participants = ref([]);

// 弹窗状态
const quitDialogVisible = ref(false);
const deleteDialogVisible = ref(false);
const approveDialogVisible = ref(false);
const rejectDialogVisible = ref(false);
const participantsDialogVisible = ref(false);

// 获取活动详情
const getActivityDetail = () => {
  loading.value = true;
  get(`/api/activity/detail`, { id: activityId.value }, 
    (message, data) => {
      activity.value = data;
      loading.value = false;
      
      // 检查是否是活动创建者
      if (currentUser.value && data.userId === currentUser.value.id) {
        isCreator.value = true;
      }
      
      // 获取发布者信息
      getActivityCreator();
      
      // 检查用户是否已参加
      checkJoinStatus();
    },
    (error) => {
      ElMessage.error('获取活动详情失败');
      loading.value = false;
    }
  );
};

// 获取活动发布者信息
const getActivityCreator = () => {
  get(`/api/activity/creator`, { id: activityId.value }, 
    (message, data) => {
      creator.value = data;
    },
    (error) => {
      console.error('获取发布者信息失败:', error);
    }
  );
};

// 获取活动参与者列表
const getActivityParticipants = () => {
  get(`/api/activity/participants`, { id: activityId.value }, 
    (message, data) => {
      participants.value = data;
    },
    (error) => {
      console.error('获取参与者列表失败:', error);
    }
  );
};

// 打开参与者列表弹窗
const showParticipants = () => {
  getActivityParticipants();
  participantsDialogVisible.value = true;
};

// 检查用户是否已参加活动
const checkJoinStatus = () => {
  if (!currentUser.value) return;
  
  get(`/api/activity/check-joined`, { id: activityId.value }, 
    (message, data) => {
      hasJoined.value = data;
    }
  );
};

// 参加活动
const joinActivity = () => {
  if (!currentUser.value) {
    ElMessage.warning('请先登录');
    return;
  }
  
  jsonpost(`/api/activity/join`, { id: activityId.value }, 
    (message) => {
      ElMessage.success('成功参加活动');
      hasJoined.value = true;
      getActivityDetail(); // 刷新活动信息
      
      // 触发活动参加成功事件，通知其他组件刷新数据
      window.dispatchEvent(new Event('activity-joined'));
      console.log("已触发activity-joined事件");
    },
    (error) => {
      ElMessage.error(error || '参加活动失败');
    }
  );
};

// 退出活动
const quitActivity = () => {
  if (!currentUser.value) return;
  quitDialogVisible.value = true;
};

// 确认退出活动
const confirmQuitActivity = () => {
  jsonpost(`/api/activity/quit`, { id: activityId.value }, 
    (message) => {
      ElMessage.success('成功退出活动');
      hasJoined.value = false;
      getActivityDetail(); // 刷新活动信息
      
      // 触发活动退出事件，通知其他组件刷新数据
      window.dispatchEvent(new Event('activity-joined'));
      console.log("已触发activity-joined事件（退出活动）");
    },
    (error) => {
      ElMessage.error(error || '退出活动失败');
    }
  );
};

// 删除活动
const deleteActivity = () => {
  if (!canManage.value) return;
  deleteDialogVisible.value = true;
};

// 确认删除活动
const confirmDeleteActivity = () => {
  jsonpost(`/api/activity/delete`, { id: activityId.value }, 
    (message) => {
      ElMessage.success('活动已成功删除');
      router.push('/activity');
    },
    (error) => {
      ElMessage.error(error || '删除活动失败');
    }
  );
};

// 审核活动 (仅管理员)
const reviewActivity = (status) => {
  if (!currentUser.value || !currentUser.value.manager || currentUser.value.manager !== 1) return;
  
  if (status === 1) {
    approveDialogVisible.value = true;
  } else {
    rejectDialogVisible.value = true;
  }
};

// 确认审核活动
const confirmReviewActivity = (status) => {
  jsonpost(`/api/activity/review`, { id: activityId.value, status }, 
    (message) => {
      const statusText = status === 1 ? '通过' : '拒绝';
      ElMessage.success(`已${statusText}此活动`);
      getActivityDetail(); // 刷新活动信息
    },
    (error) => {
      ElMessage.error(error || '审核活动失败');
    }
  );
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
};

// 计算剩余名额
const remainingSlots = computed(() => {
  if (!activity.value) return 0;
  return activity.value.maxParticipants - activity.value.currentParticipants;
});

// 计算活动状态
const activityStatus = computed(() => {
  if (!activity.value) return { status: '', text: '', class: '' };
  
  const now = new Date();
  const startTime = new Date(activity.value.startTime);
  const endTime = new Date(activity.value.endTime);
  
  if (now < startTime) {
    return { status: 'upcoming', text: '即将开始', class: 'bg-blue-100 text-blue-800 dark:bg-blue-900/50 dark:text-blue-300' };
  } else if (now >= startTime && now <= endTime) {
    return { status: 'ongoing', text: '进行中', class: 'bg-green-100 text-green-800 dark:bg-green-900/50 dark:text-green-300' };
  } else {
    return { status: 'expired', text: '已结束', class: 'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-300' };
  }
});

onMounted(() => {
  getActivityDetail();
});
</script>

<template>
  <div class="activity-detail-container mx-auto px-4 py-8">
    <div v-if="loading" class="flex justify-center items-center py-32">
      <div class="loader"></div>
    </div>
    
    <div v-else-if="!activity" class="py-32 text-center">
      <h2 class="text-2xl font-bold text-gray-500 dark:text-gray-400">活动不存在或已被删除</h2>
      <button @click="router.push('/activity')" class="mt-8 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all">
        返回活动列表
      </button>
    </div>
    
    <div v-else class="max-w-4xl mx-auto">
      <!-- 返回按钮 -->
      <div class="mb-6">
        <button @click="router.push('/activity')" class="flex items-center text-blue-500 hover:text-blue-600 font-medium">
          <i class="fas fa-arrow-left mr-2"></i>
          返回活动列表
        </button>
      </div>
      
      <!-- 活动头部 -->
      <div class="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-lg mb-8">
        <div class="relative">
          <!-- 活动状态标签 -->
          <div v-if="activity.status !== 1" class="absolute top-4 right-4 px-3 py-1 rounded-full text-xs font-medium"
            :class="{
              'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/50 dark:text-yellow-300': activity.status === 0,
              'bg-red-100 text-red-800 dark:bg-red-900/50 dark:text-red-300': activity.status === 2
            }">
            {{ activity.status === 0 ? '审核中' : '已拒绝' }}
          </div>
          
          <!-- 时间状态标签 -->
          <div v-if="activity.status === 1" class="absolute top-4 right-4 z-10 px-3 py-1 rounded-full text-xs font-medium"
            :class="activityStatus.class">
            {{ activityStatus.text }}
          </div>
          
          <!-- 活动图片 -->
          <div class="h-64 md:h-80 bg-gray-200 dark:bg-gray-700 relative overflow-hidden">
            <img 
              v-if="activity.image" 
              :src="baseURL+activity.image "
              :alt="activity.activityName" 
              class="w-full h-full object-cover"
            />
            <div v-else class="w-full h-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center">
              <span class="text-white text-3xl font-bold">{{ activity.activityName }}</span>
            </div>
          </div>
          
          <!-- 活动标题和描述 -->
          <div class="p-6 md:p-8">
            <h1 class="text-3xl font-bold mb-4">{{ activity.activityName }}</h1>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
              <div class="flex items-center">
                <i class="fas fa-map-marker-alt text-red-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">活动地点</p>
                  <p class="text-lg font-medium">{{ activity.location }}</p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-users text-blue-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">活动人数</p>
                  <p class="text-lg font-medium">
                    <span>{{ activity.currentParticipants }}/{{ activity.maxParticipants }}人</span>
                    <span class="ml-2 text-sm" :class="{'text-green-500': remainingSlots > 0, 'text-red-500': remainingSlots <= 0}">
                      {{ remainingSlots > 0 ? `(还剩${remainingSlots}个名额)` : '(已满)' }}
                    </span>
                  </p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-calendar-alt text-green-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">开始时间</p>
                  <p class="text-lg font-medium">{{ formatDate(activity.startTime) }}</p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-calendar-check text-purple-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">结束时间</p>
                  <p class="text-lg font-medium">{{ formatDate(activity.endTime) }}</p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-building text-indigo-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">举办单位</p>
                  <p class="text-lg font-medium">{{ activity.organizer }}</p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-clock text-amber-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">发布时间</p>
                  <p class="text-lg font-medium">{{ formatDate(activity.createTime) }}</p>
                </div>
              </div>
              
              <div class="flex items-center">
                <i class="fas fa-user text-purple-500 text-xl mr-3"></i>
                <div>
                  <p class="text-sm text-gray-500 dark:text-gray-400">发布者</p>
                  <p v-if="creator" class="text-lg font-medium">{{ creator.username }}</p>
                  <p v-else class="text-lg font-medium text-gray-400">加载中...</p>
                </div>
              </div>
            </div>
            
            <!-- 活动描述 -->
            <div class="mt-8">
              <h3 class="text-xl font-bold mb-4">活动详情</h3>
              <div class="prose dark:prose-invert max-w-none">
                <p class="whitespace-pre-line">{{ activity.description || '暂无详细描述' }}</p>
              </div>
            </div>
            
            <!-- 活动操作按钮 -->
            <div class="mt-8 flex flex-wrap gap-4">
              <!-- 普通用户操作 -->
              <div v-if="currentUser && activity.status === 1">
                <button 
                  v-if="activityStatus.status === 'expired'"
                  class="expired-btn"
                  disabled
                >
                  活动已过期
                </button>
                <template v-else>
                  <button 
                    v-if="!hasJoined" 
                    @click="joinActivity" 
                    class="join-btn"
                    :disabled="remainingSlots <= 0"
                  >
                    {{ remainingSlots > 0 ? '立即参加' : '名额已满' }}
                  </button>
                  <button 
                    v-else 
                    @click="quitActivity" 
                    class="quit-btn"
                  >
                    退出活动
                  </button>
                </template>
              </div>
              
              <!-- 管理员和发布者操作 -->
              <div v-if="canManage" class="flex flex-wrap gap-4">
                <button 
                  v-if="currentUser && currentUser.manager === 1 && activity.status === 0"
                  @click="reviewActivity(1)" 
                  class="approve-btn"
                >
                  通过审核
                </button>
                
                <button 
                  v-if="currentUser && currentUser.manager === 1 && activity.status === 0"
                  @click="reviewActivity(2)" 
                  class="reject-btn"
                >
                  拒绝活动
                </button>
                
                <button 
                  @click="deleteActivity" 
                  class="delete-btn"
                >
                  删除活动
                </button>
              </div>
            </div>
            
            <!-- 添加查看参与者按钮 -->
            <div class="mt-4 flex justify-end">
              <button 
                @click="showParticipants" 
                class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all flex items-center"
              >
                <i class="fas fa-users mr-2"></i>
                查看参与者名单
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 自定义弹窗组件 -->
    <!-- 退出活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="quitDialogVisible"
      title="退出活动"
      content="确定要退出此活动吗?"
      type="warning"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="confirmQuitActivity"
    />
    
    <!-- 删除活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="deleteDialogVisible"
      title="删除活动"
      content="确定要删除此活动吗? 此操作不可逆!"
      type="danger"
      confirm-text="确定删除"
      cancel-text="取消"
      @confirm="confirmDeleteActivity"
    />
    
    <!-- 通过活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="approveDialogVisible"
      title="审核活动"
      content="确定要通过此活动吗?"
      type="success"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="() => confirmReviewActivity(1)"
    />
    
    <!-- 拒绝活动弹窗 -->
    <CustomConfirmDialog
      v-model:visible="rejectDialogVisible"
      title="审核活动"
      content="确定要拒绝此活动吗?"
      type="warning"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="() => confirmReviewActivity(2)"
    />
    
    <!-- 参与者列表弹窗 -->
    <el-dialog
      v-model="participantsDialogVisible"
      title="活动参与者"
      width="30%"
    >
      <div v-if="participants.length === 0" class="text-center py-4">
        <p class="text-gray-500">暂无参与者</p>
      </div>
      <el-table v-else :data="participants" style="width: 100%">
        <el-table-column label="用户名" prop="username"></el-table-column>
        <el-table-column label="头像">
          <template #default="scope">
            <el-avatar v-if="scope.row.avatar" :src="baseURL + scope.row.avatar" size="small"></el-avatar>
            <el-avatar v-else icon="el-icon-user" size="small"></el-avatar>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="participantsDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.activity-detail-container {
  max-width: 1200px;
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

.join-btn {
  padding: 0.5rem 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white;
}

.join-btn:disabled {
  @apply opacity-50 cursor-not-allowed;
}

.quit-btn {
  padding: 0.5rem 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-orange-500 to-red-500 hover:from-orange-600 hover:to-red-600 text-white;
}

.approve-btn {
  padding: 0.5rem 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white;
}

.reject-btn {
  padding: 0.5rem 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-amber-500 to-orange-600 hover:from-amber-600 hover:to-orange-700 text-white;
}

.delete-btn {
  padding: 0.5rem 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-red-500 to-pink-600 hover:from-red-600 hover:to-pink-700 text-white;
}

.expired-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gray-300 text-gray-600 cursor-not-allowed;
}
</style> 