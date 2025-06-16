<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';
import {useUserStore} from "@/stores/userStore.js";

const userStore = useUserStore();
const formData = reactive({
  userid: userStore.user.id || '',
  sex: null,
  age: null,
  hobby: '',
  major: '',
  type: '',
  introduction: '',
  contact: ''
});

const rules = {
  userid: [{ required: true, message: '请输入学生ID', trigger: 'blur' }],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  major: [
    { required: true, message: '请输入专业', trigger: 'blur' },
    { max: 15, message: '专业最多输入15个字符', trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择MBTI类型', trigger: 'blur' }],
  hobby: [{ max: 20, message: '爱好最多输入20个字符', trigger: 'blur' }],
  introduction: [{ max: 100, message: '个人简介最多输入100个字符', trigger: 'blur' }],
  contact: [{ max: 20, message: '联系方式最多输入20个字符', trigger: 'blur' }]
};

const formRef = ref(null);
const loading = ref(false);
const operationType = ref('add');

onMounted(() => {
  if (userStore.user?.id) {
    formData.userid = userStore.user.id;
    getPersonalInfo(userStore.user.id);
  }
});

const getPersonalInfo = async (userid) => {
  try {
    loading.value = true;
    const response = await axios.get('/api/personal/get', {
      headers: {
        'Authorization': localStorage.getItem('authToken'),
        'UserId': userid
      },
      withCredentials: true
    });
    if (response.data && response.data.success) {
      const data = response.data.data;
      Object.assign(formData, data);
      operationType.value = 'update';
      ElMessage.success(response.data.message);
    } else {
      ElMessage.warning(response.data?.message || '获取个人信息失败');
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取个人信息失败');
  } finally {
    loading.value = false;
  }
};

const submitForm = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        const submitData = { ...formData };

        const url = operationType.value === 'add' ? '/api/personal/add' : '/api/personal/update';
        const method = operationType.value === 'add' ? 'post' : 'put';

        const response = await axios[method](url, submitData, {
          headers: { 
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('authToken')
          },
          withCredentials: true
        });

        if (response.data && response.data.success) {
          ElMessage.success(response.data.message);
          resetForm();
          isModalVisible.value = false; // 关闭弹窗
        } else {
          ElMessage.warning(response.data?.message || '操作失败');
        }
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '操作失败');
      } finally {
        loading.value = false;
      }
    }
  });
};

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  Object.keys(formData).forEach(key => {
    if (key === 'sex' || key === 'age') {
      formData[key] = null;
    } else {
      formData[key] = '';
    }
  });
  operationType.value = 'add';
};

// 弹窗状态控制
const isModalVisible = ref(false);
const showModal = () => {
  isModalVisible.value = true;
};
const closeModal = () => {
  isModalVisible.value = false;
};

// 监听ESC键关闭弹窗
watch(isModalVisible, (newValue) => {
  if (newValue) {
    document.addEventListener('keydown', handleEscKey);
  } else {
    document.removeEventListener('keydown', handleEscKey);
  }
});

const handleEscKey = (event) => {
  if (event.key === 'Escape') {
    closeModal();
  }
};

// 阻止弹窗内容点击冒泡到遮罩层
const stopPropagation = (event) => {
  event.stopPropagation();
};

// 计算字符数和剩余字符
const hobbyLength = computed(() => formData.hobby ? formData.hobby.length : 0);
const majorLength = computed(() => formData.major ? formData.major.length : 0);
const introductionLength = computed(() => formData.introduction ? formData.introduction.length : 0);
const contactLength = computed(() => formData.contact ? formData.contact.length : 0);
</script>

<template>
  <el-button @click="showModal" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg transition-colors duration-200
                                       font-bold dark:bg-blue-600 hover:dark:bg-blue-700 dark:text-white focus:outline-none">
    修改信息
  </el-button>

  <!-- 自定义弹窗 -->
  <teleport to="body">
    <div v-if="isModalVisible" 
         @click="closeModal"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center transition-opacity duration-300">
      
      <!-- 弹窗容器 -->
      <div @click="stopPropagation"
           class="bg-white dark:bg-gray-800 rounded-xl w-[640px] max-w-[95vw] max-h-[90vh] shadow-2xl
                  border border-gray-200 dark:border-gray-700 
                  transform transition-all duration-300 animate-modal-open overflow-hidden">
        
        <div class="p-6 space-y-6 overflow-y-auto max-h-[80vh] custom-scrollbar">
          <div class="flex justify-between items-center border-b border-gray-200 dark:border-gray-700 pb-4">
            <h2 class="text-2xl font-bold text-gray-900 dark:text-white">学生个人信息管理</h2>
            <button @click="closeModal" class="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200 focus:outline-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-500 dark:text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 rounded-lg p-4 flex items-center space-x-4 shadow-sm border border-gray-200 dark:border-gray-600">
            <a-avatar shape="square" class="w-16 h-16 rounded-lg flex-shrink-0 shadow-md" :src="userStore.user?.avatar ? `http://localhost:8080/${userStore.user.avatar}` : 'https://img95.699pic.com/element/40109/0194.png_300.png'" />
            <div>
              <h3 class="text-lg font-semibold bg-gradient-to-r from-blue-500 via-purple-500 to-pink-500 bg-clip-text text-transparent">
                {{ userStore.user?.username }}
              </h3>
              <p class="text-gray-500 dark:text-gray-300">ID: {{ userStore.user?.id }}</p>
            </div>
          </div>

          <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px" class="space-y-4">

            <el-form-item label="性别" prop="sex" class="mb-4">
              <el-radio-group v-model="formData.sex">
                <el-radio :label="1" class="mr-4 dark:text-gray-300">男</el-radio>
                <el-radio :label="0" class="dark:text-gray-300">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="年龄" prop="age" class="mb-4">
              <el-input-number v-model="formData.age" :min="1" :max="100" class="w-32 dark:border-gray-600" />
            </el-form-item>

            <el-form-item label="爱好" prop="hobby" class="mb-4">
              <el-input v-model="formData.hobby" placeholder="请输入爱好" class="w-full dark:border-gray-600" maxlength="20" show-word-limit />
              <div class="text-xs text-right text-gray-500 dark:text-gray-400 mt-1">
                {{ hobbyLength }}/20
              </div>
            </el-form-item>

            <el-form-item label="专业" prop="major" class="mb-4">
              <el-input v-model="formData.major" placeholder="请输入专业" class="w-full dark:border-gray-600" maxlength="15" show-word-limit />
              <div class="text-xs text-right text-gray-500 dark:text-gray-400 mt-1">
                {{ majorLength }}/15
              </div>
            </el-form-item>

            <el-form-item label="MBTI类型" prop="type" class="mb-4">
              <el-select v-model="formData.type" placeholder="请选择MBTI类型" class="w-full dark:border-gray-600">
                <el-option label="INTJ - 建筑师" value="INTJ" />
                <el-option label="INTP - 逻辑学家" value="INTP" />
                <el-option label="ENTJ - 指挥官" value="ENTJ" />
                <el-option label="ENTP - 辩论家" value="ENTP" />
                <el-option label="INFJ - 提倡者" value="INFJ" />
                <el-option label="INFP - 调停者" value="INFP" />
                <el-option label="ENFJ - 主人公" value="ENFJ" />
                <el-option label="ENFP - 竞选者" value="ENFP" />
                <el-option label="ISTJ - 物流师" value="ISTJ" />
                <el-option label="ISFJ - 守卫者" value="ISFJ" />
                <el-option label="ESTJ - 总经理" value="ESTJ" />
                <el-option label="ESFJ - 执政官" value="ESFJ" />
                <el-option label="ISTP - 鉴赏家" value="ISTP" />
                <el-option label="ISFP - 探险家" value="ISFP" />
                <el-option label="ESTP - 企业家" value="ESTP" />
                <el-option label="ESFP - 表演者" value="ESFP" />
              </el-select>
            </el-form-item>

            <el-form-item label="个人简介" prop="introduction" class="mb-4">
              <el-input
                v-model="formData.introduction"
                type="textarea"
                :rows="3"
                placeholder="请输入个人简介"
                class="w-full dark:border-gray-600"
                maxlength="100"
                show-word-limit
              />
              <div class="text-xs text-right text-gray-500 dark:text-gray-400 mt-1">
                {{ introductionLength }}/100
              </div>
            </el-form-item>

            <el-form-item label="联系方式" prop="contact" class="mb-4">
              <el-input v-model="formData.contact" placeholder="请输入联系方式" class="w-full dark:border-gray-600" maxlength="20" show-word-limit />
              <div class="text-xs text-right text-gray-500 dark:text-gray-400 mt-1">
                {{ contactLength }}/20
              </div>
            </el-form-item>

            <el-form-item class="flex justify-end space-x-4 mt-8 pt-4 border-t border-gray-200 dark:border-gray-700">
              <el-button type="primary" @click="submitForm" :loading="loading" class="bg-blue-500 hover:bg-blue-600 text-white dark:bg-blue-600 dark:hover:bg-blue-700 shadow-sm">
                {{ operationType === 'add' ? '添加' : '更新' }}
              </el-button>
              <el-button @click="resetForm" class="border border-gray-300 dark:border-gray-600 dark:text-gray-300 dark:hover:bg-gray-700">
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </teleport>
</template>

<style scoped>
/* Element Plus 组件暗色模式适配 */
:deep(.el-input__wrapper) {
  background-color: transparent !important;
}

:deep(.el-input__inner) {
  color: inherit !important;
}

:deep(.el-select__popper.el-popper) {
  background-color: #1f2937 !important;
  border-color: #374151 !important;
}

:deep(.el-select-dropdown__item) {
  color: #e5e7eb !important;
}

:deep(.el-select-dropdown__item.hover),
:deep(.el-select-dropdown__item:hover) {
  background-color: #374151 !important;
}

:deep(.el-select-dropdown__item.selected) {
  background-color: #3b82f6 !important;
  color: #ffffff !important;
}

/* 输入框暗色模式样式 */
:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #3b82f6 !important;
}

/* 数字输入框暗色模式样式 */
:deep(.el-input-number.is-controls-right .el-input-number__decrease),
:deep(.el-input-number.is-controls-right .el-input-number__increase) {
  background-color: #374151 !important;
  border-color: #4b5563 !important;
  color: #e5e7eb !important;
}

/* 单选框暗色模式样式 */
:deep(.el-radio__input.is-checked .el-radio__inner) {
  background-color: #3b82f6 !important;
  border-color: #3b82f6 !important;
}

:deep(.el-radio__label) {
  color: inherit !important;
}

/* 按钮暗色模式样式 */
:deep(.el-button) {
  background-color: transparent !important;
  border-color: #4b5563 !important;
  color: inherit !important;
}

:deep(.el-button--primary) {
  background-color: #3b82f6 !important;
  border-color: #3b82f6 !important;
  color: #ffffff !important;
}

:deep(.el-button--primary:hover) {
  background-color: #2563eb !important;
  border-color: #2563eb !important;
}

/* 表单项标签暗色模式样式 */
:deep(.el-form-item__label) {
  color: inherit !important;
}

/* 下拉选择框暗色模式样式 */
:deep(.el-select .el-input__wrapper) {
  background-color: transparent !important;
  box-shadow: 0 0 0 1px #4b5563 !important;
}

:deep(.el-select .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #3b82f6 !important;
}

/* 文本域暗色模式样式 */
:deep(.el-textarea__inner) {
  background-color: transparent !important;
  border-color: #4b5563 !important;
  color: inherit !important;
  white-space: pre-wrap !important;
  word-wrap: break-word !important;
  overflow-wrap: break-word !important;
}

:deep(.el-textarea__inner:focus) {
  border-color: #3b82f6 !important;
}

/* 文本内容样式 */
:deep(.el-form-item__content),
:deep(.el-input__wrapper),
:deep(.el-input__inner) {
  white-space: pre-wrap !important;
  word-wrap: break-word !important;
  overflow-wrap: break-word !important;
  max-width: 100% !important;
}

/* 确保所有文本容器都能正确换行 */
.text-content {
  white-space: pre-wrap !important;
  word-wrap: break-word !important;
  overflow-wrap: break-word !important;
  max-width: 100% !important;
}

:deep(.el-form-item__content) {
  width: 100% !important;
}

:deep(.el-input),
:deep(.el-textarea) {
  width: 100% !important;
  max-width: 100% !important;
}

/* 弹窗动画 */
@keyframes modalOpen {
  from {
    opacity: 0;
    transform: scale(0.95) translateY(10px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.animate-modal-open {
  animation: modalOpen 0.3s ease-out forwards;
}

/* 自定义滚动条 */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  @apply bg-gray-300 dark:bg-gray-600 rounded-full hover:bg-blue-400 dark:hover:bg-blue-500;
}
</style>