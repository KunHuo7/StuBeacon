<script setup>
import { ref, onMounted, computed } from 'vue';
import { get, post, put, del, jsonpost } from '@/net/index.js';
import { useUserStore } from '@/stores/userStore.js';
import { useThemeStore } from '@/stores/theme.js';
import { useRouter } from 'vue-router';
import { ElMessage } from "element-plus";
import { baseURL } from '@/main.js';
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

const router = useRouter();
const userStore = useUserStore();
const themeStore = useThemeStore();

// 检查是否是管理员
const isAdmin = computed(() => {
  return userStore.user && userStore.user.manager === 1;
});

// 如果不是管理员，重定向到首页
onMounted(() => {
  if (!isAdmin.value) {
    ElMessage.error('您没有管理员权限');
    router.push('/');
  } else {
    loadUsers();
    // 加载待审核用户列表
    loadPendingUsers();
  }
});

// 用户列表
const users = ref([]);
const isLoading = ref(false);
const searchKeyword = ref('');
const totalUsers = ref(0);

// 分页
const currentPage = ref(1);
const pageSize = ref(10);

// 模态框控制
const showAddUserModal = ref(false);
const showEditUserModal = ref(false);
const showPasswordModal = ref(false);

// 当前编辑的用户
const currentUser = ref({
  id: null,
  username: '',
  mail: '',
  studentId: '',
  avatar: '',
  manager: 0
});

// 新用户表单
const newUser = ref({
  username: '',
  password: '',
  mail: '',
  studentId: '',
  manager: 0
});

// 密码修改
const passwordForm = ref({
  userId: null,
  newPassword: ''
});

// 表单验证规则
const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应为6-20个字符', trigger: 'blur' }
  ]
};

// 添加新的ref变量控制删除弹窗和存储待删除用户ID
const showDeleteModal = ref(false);
const deleteUserId = ref(null);

// 为确认弹窗添加状态变量
const approveDialogVisible = ref(false);
const rejectDialogVisible = ref(false);
const currentUserId = ref(null);

// 加载用户列表
const loadUsers = () => {
  isLoading.value = true;
  if (searchKeyword.value.trim()) {
    // 搜索用户
    get('api/user/admin/search', { keyword: searchKeyword.value }, (message, data) => {
      users.value = data;
      totalUsers.value = data.length;
      isLoading.value = false;
    }, () => {
      isLoading.value = false;
    });
  } else {
    // 获取所有用户
    get('api/user/admin/list', {}, (message, data) => {
      users.value = data;
      totalUsers.value = data.length;
      isLoading.value = false;
    }, () => {
      isLoading.value = false;
    });
  }
};

// 搜索用户
const searchUsers = () => {
  currentPage.value = 1;
  loadUsers();
};

// 打开添加用户模态框
const openAddUserModal = () => {
  newUser.value = {
    username: '',
    password: '',
    mail: '',
    studentId: '',
    manager: 0
  };
  showAddUserModal.value = true;
};

// 添加新用户
const addUser = () => {
  jsonpost('api/user/admin/add', newUser.value, (message) => {
    showAddUserModal.value = false;
    ElMessage.success('添加用户成功');
    loadUsers();
  }, (error) => {
    ElMessage.error(error || '添加用户失败');
  });
};

// 打开编辑用户模态框
const openEditUserModal = (user) => {
  currentUser.value = { ...user };
  showEditUserModal.value = true;
};

// 更新用户信息
const updateUser = () => {
  put('api/user/admin/update', currentUser.value, (message) => {
    showEditUserModal.value = false;
    ElMessage.success('更新用户信息成功');
    loadUsers();
  });
};

// 打开修改密码模态框
const openPasswordModal = (userId) => {
  passwordForm.value = {
    userId: userId,
    newPassword: ''
  };
  showPasswordModal.value = true;
};

// 更新用户密码
const updatePassword = () => {
  // 注意：后端API使用@RequestParam，所以我们需要使用URLSearchParams方式传递参数
  put(`api/user/admin/update-password?userId=${passwordForm.value.userId}&newPassword=${passwordForm.value.newPassword}`, {}, (message) => {
    showPasswordModal.value = false;
    ElMessage.success('密码修改成功');
  });
};

// 打开删除用户弹窗
const openDeleteModal = (userId) => {
  deleteUserId.value = userId;
  showDeleteModal.value = true;
};

// 删除用户
const deleteUser = () => {
  del(`api/user/admin/delete/${deleteUserId.value}`, {}, (message) => {
    ElMessage.success('删除用户成功');
    loadUsers();
    showDeleteModal.value = false;
  });
};

// 切换用户管理员状态
const toggleAdminStatus = (user) => {
  const newStatus = user.manager === 1 ? 0 : 1;
  // 注意：后端使用@RequestParam，使用URLSearchParams方式传递参数
  post(`api/user/updateManager?userId=${user.id}&manager=${newStatus}`, {}, (message) => {
    ElMessage.success(`已${newStatus === 1 ? '授予' : '撤销'}管理员权限`);
    loadUsers();
  });
};

// 分页显示的用户
const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return users.value.slice(start, end);
});

// 重置搜索
const resetSearch = () => {
  searchKeyword.value = '';
  loadUsers();
};

// =========================== 用户审核相关 ============================

// 选项卡控制（用户管理/用户审核）
const activeTab = ref('users');  // 'users' 或 'pending'

// 待审核用户列表
const pendingUsers = ref([]);
const isPendingLoading = ref(false);

// 加载待审核用户
const loadPendingUsers = () => {
  isPendingLoading.value = true;
  get('api/user/admin/pending', {}, (message, data) => {
    pendingUsers.value = data;
    isPendingLoading.value = false;
  }, () => {
    isPendingLoading.value = false;
  });
};

// 切换到用户审核标签时加载数据
const handleTabChange = (tab) => {
  activeTab.value = tab;
  if (tab === 'pending') {
    loadPendingUsers();
  } else {
    loadUsers();
  }
};

// 审核通过用户
const showApproveDialog = (userId) => {
  currentUserId.value = userId;
  approveDialogVisible.value = true;
};

const confirmApproveUser = () => {
  post(`api/user/admin/approve/${currentUserId.value}`, {}, (message) => {
    ElMessage.success('已通过用户注册申请');
    loadPendingUsers();
  });
};

// 拒绝用户注册
const showRejectDialog = (userId) => {
  currentUserId.value = userId;
  rejectDialogVisible.value = true;
};

const confirmRejectUser = () => {
  post(`api/user/admin/reject/${currentUserId.value}`, {}, (message) => {
    ElMessage.success('已拒绝用户注册申请');
    loadPendingUsers();
  });
};
</script>

<template>
  <div v-if="isAdmin" class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <!-- 标题和选项卡 -->
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900 dark:text-white mb-6">用户管理</h1>
      
      <!-- 选项卡 -->
      <div class="flex border-b border-gray-200 dark:border-gray-700 mb-6">
        <button 
          @click="handleTabChange('users')" 
          class="py-2 px-4 text-center focus:outline-none transition-colors duration-200"
          :class="activeTab === 'users' 
            ? 'border-b-2 border-blue-500 text-blue-600 dark:text-blue-400 font-medium' 
            : 'text-gray-600 dark:text-gray-400 hover:text-gray-800 dark:hover:text-gray-300'"
        >
          用户管理
        </button>
        <button 
          @click="handleTabChange('pending')" 
          class="py-2 px-4 text-center focus:outline-none transition-colors duration-200 relative"
          :class="activeTab === 'pending' 
            ? 'border-b-2 border-blue-500 text-blue-600 dark:text-blue-400 font-medium' 
            : 'text-gray-600 dark:text-gray-400 hover:text-gray-800 dark:hover:text-gray-300'"
        >
          用户审核
          <span 
            v-if="pendingUsers.length > 0" 
            class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center"
          >
            {{ pendingUsers.length > 99 ? '99+' : pendingUsers.length}}
          </span>
        </button>
      </div>
      
      <!-- 用户管理视图 -->
      <div v-if="activeTab === 'users'">
        <div class="flex flex-col sm:flex-row gap-4 justify-between items-start sm:items-center mb-6">
          <!-- 搜索框 -->
          <div class="flex flex-1 max-w-md">
            <div class="relative w-full">
              <input
                v-model="searchKeyword"
                @keyup.enter="searchUsers"
                type="text"
                placeholder="搜索用户名..."
                class="w-full px-4 py-2 border rounded-lg text-gray-700 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500
                      bg-white dark:bg-gray-800 border-gray-300 dark:border-gray-700"
              />
              <button 
                @click="searchUsers"
                class="absolute right-2 top-2 text-gray-500 dark:text-gray-400 hover:text-blue-500 dark:hover:text-blue-400"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </button>
            </div>
            <button 
              v-if="searchKeyword" 
              @click="resetSearch"
              class="ml-2 px-3 py-2 bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-600"
            >
              重置
            </button>
          </div>
          
          <!-- 添加用户按钮 -->
          <button 
            @click="openAddUserModal"
            class="px-4 py-2 bg-blue-600 dark:bg-blue-700 text-white rounded-lg hover:bg-blue-700 dark:hover:bg-blue-800 shadow-md transition duration-200 flex items-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            添加用户
          </button>
        </div>
      </div>
      
      <!-- 用户审核视图 -->
      <div v-if="activeTab === 'pending'" class="mb-6">
        <div class="flex justify-between items-center">
          <h2 class="text-lg font-semibold text-gray-700 dark:text-gray-300">待审核用户列表</h2>
          <button 
            @click="loadPendingUsers"
            class="px-3 py-1 bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-600 text-sm flex items-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            刷新
          </button>
        </div>
      </div>
    </div>
    
    <!-- 用户列表（用户管理视图） -->
    <div v-if="activeTab === 'users'" class="bg-white dark:bg-gray-900 rounded-xl shadow-md overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200 dark:divide-gray-800">
          <thead class="bg-gray-50 dark:bg-gray-800">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">用户名</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">邮箱</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">学号</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">角色</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">状态</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">操作</th>
            </tr>
          </thead>
          <tbody class="bg-white dark:bg-gray-900 divide-y divide-gray-200 dark:divide-gray-800">
            <tr v-if="isLoading" class="animate-pulse">
              <td colspan="7" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                加载中...
              </td>
            </tr>
            <tr v-else-if="users.length === 0" class="hover:bg-gray-50 dark:hover:bg-gray-800">
              <td colspan="7" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                未找到用户
              </td>
            </tr>
            <tr 
              v-for="user in paginatedUsers" 
              :key="user.id"
              class="hover:bg-gray-50 dark:hover:bg-gray-800 transition duration-150"
            >
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-100">{{ user.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-100">
                <div class="flex items-center">
                  <img 
                    :src="user.avatar ? baseURL + user.avatar : 'https://img95.699pic.com/element/40109/0194.png_300.png'" 
                    class="h-8 w-8 rounded-full mr-2 object-cover"
                    alt="用户头像"
                  />
                  {{ user.username }}
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">{{ user.mail || '未设置' }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">{{ user.studentId || '未设置' }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                <span 
                  :class="user.manager === 1 
                    ? 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200' 
                    : 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'"
                  class="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full"
                >
                  {{ user.manager === 1 ? '管理员' : '普通用户' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                <span 
                  :class="{
                    'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200': user.status === 0,
                    'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200': user.status === 1,
                    'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200': user.status === 2
                  }"
                  class="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full"
                >
                  {{ user.status === 0 ? '待审核' : user.status === 1 ? '已通过' : '已拒绝' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                <button 
                  @click="openEditUserModal(user)"
                  class="text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300"
                >
                  编辑
                </button>
                <button 
                  @click="openPasswordModal(user.id)"
                  class="text-yellow-600 dark:text-yellow-400 hover:text-yellow-800 dark:hover:text-yellow-300"
                >
                  重置密码
                </button>
                <button 
                  @click="toggleAdminStatus(user)"
                  :class="user.manager === 1 
                    ? 'text-purple-600 dark:text-purple-400 hover:text-purple-800 dark:hover:text-purple-300'
                    : 'text-green-600 dark:text-green-400 hover:text-green-800 dark:hover:text-green-300'"
                >
                  {{ user.manager === 1 ? '撤销管理员' : '设为管理员' }}
                </button>
                <button 
                  @click="openDeleteModal(user.id)"
                  class="text-red-600 dark:text-red-400 hover:text-red-800 dark:hover:text-red-300"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 分页 -->
      <div class="px-6 py-4 bg-gray-50 dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 flex items-center justify-between">
        <div class="flex-1 flex justify-between sm:hidden">
          <button 
            @click="currentPage--" 
            :disabled="currentPage === 1"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
          >
            上一页
          </button>
          <button 
            @click="currentPage++" 
            :disabled="currentPage * pageSize >= totalUsers"
            class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 dark:border-gray-600 text-sm font-medium rounded-md text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
          >
            下一页
          </button>
        </div>
        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-gray-700 dark:text-gray-300">
              显示第 
              <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span>
              至
              <span class="font-medium">{{ Math.min(currentPage * pageSize, totalUsers) }}</span>
              项，共
              <span class="font-medium">{{ totalUsers }}</span>
              项
            </p>
          </div>
          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button
                @click="currentPage--"
                :disabled="currentPage === 1"
                class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-sm font-medium text-gray-500 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
              >
                <span class="sr-only">上一页</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
              </button>
              <button
                @click="currentPage++"
                :disabled="currentPage * pageSize >= totalUsers"
                class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-sm font-medium text-gray-500 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-600 disabled:opacity-50"
              >
                <span class="sr-only">下一页</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 待审核用户列表（用户审核视图） -->
    <div v-if="activeTab === 'pending'" class="bg-white dark:bg-gray-900 rounded-xl shadow-md overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200 dark:divide-gray-800">
          <thead class="bg-gray-50 dark:bg-gray-800">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">用户名</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">邮箱</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">学号</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">操作</th>
            </tr>
          </thead>
          <tbody class="bg-white dark:bg-gray-900 divide-y divide-gray-200 dark:divide-gray-800">
            <tr v-if="isPendingLoading" class="animate-pulse">
              <td colspan="5" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                加载中...
              </td>
            </tr>
            <tr v-else-if="pendingUsers.length === 0" class="hover:bg-gray-50 dark:hover:bg-gray-800">
              <td colspan="5" class="px-6 py-4 text-center text-gray-500 dark:text-gray-400">
                暂无待审核用户
              </td>
            </tr>
            <tr 
              v-for="user in pendingUsers" 
              :key="user.id"
              class="hover:bg-gray-50 dark:hover:bg-gray-800 transition duration-150"
            >
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-100">{{ user.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-100">{{ user.username }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">{{ user.mail || '未设置' }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">{{ user.studentId || '未设置' }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                  <button 
                    @click="showApproveDialog(user.id)"
                    class="px-3 py-1 bg-green-100 dark:bg-green-900 text-green-800 dark:text-green-200 rounded hover:bg-green-200 dark:hover:bg-green-800 transition-colors duration-200"
                  >
                    通过
                  </button>
                  <button 
                    @click="showRejectDialog(user.id)"
                    class="px-3 py-1 bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200 rounded hover:bg-red-200 dark:hover:bg-red-800 transition-colors duration-200"
                  >
                    拒绝
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- 添加用户模态框 -->
    <div v-if="showAddUserModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4">
        <div class="p-6">
          <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-4">添加新用户</h3>
          <form @submit.prevent="addUser">
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">用户名</label>
                <input 
                  v-model="newUser.username" 
                  type="text" 
                  required
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">密码</label>
                <input 
                  v-model="newUser.password" 
                  type="password" 
                  required
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">邮箱</label>
                <input 
                  v-model="newUser.mail" 
                  type="email"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">学号</label>
                <input 
                  v-model="newUser.studentId" 
                  type="text"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">角色</label>
                <select 
                  v-model="newUser.manager"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                >
                  <option :value="0">普通用户</option>
                  <option :value="1">管理员</option>
                </select>
              </div>
            </div>
            <div class="mt-6 flex justify-end space-x-3">
              <button 
                type="button" 
                @click="showAddUserModal = false"
                class="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 focus:outline-none"
              >
                取消
              </button>
              <button 
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 dark:bg-blue-700 hover:bg-blue-700 dark:hover:bg-blue-600 focus:outline-none"
              >
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <!-- 编辑用户模态框 -->
    <div v-if="showEditUserModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4">
        <div class="p-6">
          <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-4">编辑用户</h3>
          <form @submit.prevent="updateUser">
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">用户名</label>
                <input 
                  v-model="currentUser.username" 
                  type="text" 
                  required
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">邮箱</label>
                <input 
                  v-model="currentUser.mail" 
                  type="email"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">学号</label>
                <input 
                  v-model="currentUser.studentId" 
                  type="text"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">角色</label>
                <select 
                  v-model="currentUser.manager"
                  class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
                >
                  <option :value="0">普通用户</option>
                  <option :value="1">管理员</option>
                </select>
              </div>
            </div>
            <div class="mt-6 flex justify-end space-x-3">
              <button 
                type="button" 
                @click="showEditUserModal = false"
                class="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 focus:outline-none"
              >
                取消
              </button>
              <button 
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 dark:bg-blue-700 hover:bg-blue-700 dark:hover:bg-blue-600 focus:outline-none"
              >
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <!-- 修改密码模态框 -->
    <div v-if="showPasswordModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4">
        <div class="p-6">
          <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-4">重置密码</h3>
          <form @submit.prevent="updatePassword">
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">新密码</label>
              <input 
                v-model="passwordForm.newPassword" 
                type="password" 
                required
                class="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
              />
            </div>
            <div class="mt-6 flex justify-end space-x-3">
              <button 
                type="button" 
                @click="showPasswordModal = false"
                class="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 focus:outline-none"
              >
                取消
              </button>
              <button 
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 dark:bg-blue-700 hover:bg-blue-700 dark:hover:bg-blue-600 focus:outline-none"
              >
                确认修改
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 非管理员提示 -->
  <div v-else class="min-h-screen flex items-center justify-center">
    <div class="text-center p-8 bg-white dark:bg-gray-800 rounded-xl shadow-lg max-w-md">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
      </svg>
      <h2 class="mt-4 text-xl font-bold text-gray-900 dark:text-white">权限不足</h2>
      <p class="mt-2 text-gray-600 dark:text-gray-300">您没有管理员权限，无法访问此页面。</p>
      <button 
        @click="router.push('/')"
        class="mt-6 px-4 py-2 bg-blue-600 dark:bg-blue-700 text-white rounded-lg hover:bg-blue-700 dark:hover:bg-blue-800 shadow-md transition duration-200"
      >
        返回首页
      </button>
    </div>
  </div>

  <!-- 删除确认弹窗 -->
  <div v-if="showDeleteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-md w-full mx-4 overflow-hidden">
      <div class="p-6">
        <div class="flex items-center mb-4">
          <div class="bg-red-100 dark:bg-red-900/30 p-2 rounded-full mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600 dark:text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </div>
          <h3 class="text-lg font-medium text-gray-900 dark:text-white">确认删除用户</h3>
        </div>
        <p class="text-gray-600 dark:text-gray-300 mb-6">此操作将永久删除此用户，且不可恢复。您确定要继续吗？</p>
        <div class="flex justify-end space-x-3">
          <button 
            @click="showDeleteModal = false"
            class="px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600 focus:outline-none transition-colors duration-200"
          >
            取消
          </button>
          <button 
            @click="deleteUser()"
            class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 dark:bg-red-700 hover:bg-red-700 dark:hover:bg-red-600 focus:outline-none transition-colors duration-200"
          >
            确认删除
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- 添加确认对话框组件 -->
  <CustomConfirmDialog
    v-model:visible="approveDialogVisible"
    title="用户审核"
    content="确定通过此用户的注册申请？"
    type="success"
    confirm-text="通过"
    cancel-text="取消"
    @confirm="confirmApproveUser"
  />
  
  <CustomConfirmDialog
    v-model:visible="rejectDialogVisible"
    title="用户审核"
    content="确定拒绝此用户的注册申请？"
    type="warning"
    confirm-text="拒绝"
    cancel-text="取消"
    @confirm="confirmRejectUser"
  />
</template>

<style scoped>

</style>