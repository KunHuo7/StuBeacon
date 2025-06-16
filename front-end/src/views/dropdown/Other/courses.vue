<script setup>
import { ref, reactive, onMounted } from 'vue';
import MajorIntro from './components/MajorIntro.vue';
import ElectricalMajorIntro from './components/ElectricalMajorIntro.vue';
import MechanicalMajorIntro from './components/MechanicalMajorIntro.vue';
import ManagementMajorIntro from './components/ManagementMajorIntro.vue';
import ArtMajorIntro from './components/ArtMajorIntro.vue';
import CreditRequirements from './components/CreditRequirements.vue';
import MainCourses from './components/MainCourses.vue';
import { getAllColleges, getMajorsByCollegeId, getMajorFullInfo, getCoursesByMajorId, uploadMajorImage, deleteMajorImage, updateMajorContent } from '@/net/courseApi.js';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/userStore.js';
import { baseURL } from '@/main.js';
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

// 用户状态
const userStore = useUserStore();

// 学院和专业数据
const colleges = reactive([]);
const majors = reactive([]);

// 选择状态
const selectedCollege = ref(null);
const selectedMajor = ref(null);
const currentMajorId = ref(null);
const selectedContentType = ref('intro');

// 主要数据对象
const majorData = reactive({
  creditRequirements: '',
  mainCourses: '',
  creditRequirementImages: [],
  mainCourseImages: [],
  courseStructureImages: []
});

// 加载状态
const loading = ref(false);

// 图片上传相关
const showUploadModal = ref(false);
const imageType = ref('');
const imageFile = ref(null);
const imageDescription = ref('');
const imageUploadLoading = ref(false);

// 图片删除相关
const showDeleteConfirm = ref(false);
const imageToDelete = ref(null);

// 图片查看相关
const showImageViewer = ref(false);
const currentViewImage = ref(null);

// 获取所有学院信息
const fetchColleges = () => {
  loading.value = true;
  getAllColleges(
    (message, data) => {
      loading.value = false;
      if (data) {
        colleges.length = 0;
        data.forEach(college => {
          colleges.push({
            id: college.id,
            name: college.name,
            majors: []
          });
        });
      } else {
        ElMessage.error(message || '获取学院信息失败');
      }
    },
    (error) => {
      loading.value = false;
      ElMessage.error('获取学院信息失败：' + error);
    }
  );
};

// 选择学院
const selectCollege = (college) => {
  selectedCollege.value = college;
  selectedMajor.value = null;
  currentMajorId.value = null;
  
  // 如果该学院的专业列表为空，则获取专业信息
  if (college.majors.length === 0) {
    fetchMajorsByCollege(college.id);
  }
};

// 根据学院ID获取专业列表
const fetchMajorsByCollege = (collegeId) => {
  loading.value = true;
  getMajorsByCollegeId(
    collegeId,
    (message, data) => {
      loading.value = false;
      if (data) {
        // 找到对应学院，更新专业列表
        const college = colleges.find(c => c.id === collegeId);
        if (college) {
          college.majors = data.map(major => major.name);
          majors.push(...data);
        }
      } else {
        ElMessage.error(message || '获取专业信息失败');
      }
    },
    (error) => {
      loading.value = false;
      ElMessage.error('获取专业信息失败：' + error);
    }
  );
};

// 选择专业
const selectMajor = (majorName) => {
  selectedMajor.value = majorName;
  
  // 根据专业名称查找专业ID
  const major = majors.find(m => m.name === majorName);
  if (major) {
    currentMajorId.value = major.id;
    fetchMajorFullInfo(major.id);
  } else {
    ElMessage.warning('未找到该专业信息');
  }
};

// 获取专业完整信息（包含图片）
const fetchMajorFullInfo = (majorId) => {
  loading.value = true;
  getMajorFullInfo(
    majorId,
    (message, data) => {
      loading.value = false;
      if (data) {
        // 更新专业数据
        const major = data.major || {};
        majorData.creditRequirements = major.creditRequirements || '';
        majorData.mainCourses = major.mainCourses || '';
        majorData.creditRequirementImages = data.creditRequirementImages || [];
        majorData.mainCourseImages = data.mainCourseImages || [];
        majorData.courseStructureImages = data.structureImages || [];
        
        console.log('获取专业信息成功:', data);
      } else {
        ElMessage.error(message || '获取专业详细信息失败');
      }
    },
    (error) => {
      loading.value = false;
      ElMessage.error('获取专业详细信息失败：' + error);
    }
  );
};

// 判断是否为管理员
const isAdmin = () => {
  return userStore.user?.manager === 1;
};

// 图片上传相关处理
const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    // 检查文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件');
      return;
    }
    
    // 检查文件大小（限制为5MB）
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过5MB');
      return;
    }
    
    imageFile.value = file;
  }
};

// 上传图片
const uploadImage = () => {
  if (!imageFile.value) {
    ElMessage.warning('请选择图片文件');
    return;
  }
  
  if (!currentMajorId.value) {
    ElMessage.warning('未选择专业，无法上传图片');
    return;
  }
  
  imageUploadLoading.value = true;
  
  const formData = new FormData();
  formData.append('image', imageFile.value);
  formData.append('imageType', imageType.value);
  formData.append('description', imageDescription.value || '');
  
  uploadMajorImage(
    currentMajorId.value,
    imageType.value,
    formData,
    (message, data) => {
      imageUploadLoading.value = false;
      if (data) {
        ElMessage.success('图片上传成功');
        // 重新加载专业信息以更新图片列表
        fetchMajorFullInfo(currentMajorId.value);
        // 关闭上传模态框
        closeUploadModal();
      } else {
        ElMessage.error(message || '图片上传失败');
      }
    },
    (error) => {
      imageUploadLoading.value = false;
      ElMessage.error('图片上传失败：' + error);
    }
  );
};

// 打开图片上传模态框
const handleImageUpload = (type) => {
  imageType.value = type;
  imageDescription.value = '';
  imageFile.value = null;
  showUploadModal.value = true;
};

// 关闭上传模态框
const closeUploadModal = () => {
  showUploadModal.value = false;
  imageType.value = '';
  imageDescription.value = '';
  imageFile.value = null;
};

// 显示删除确认框
const showDeleteConfirmDialog = (image) => {
  imageToDelete.value = image;
  showDeleteConfirm.value = true;
};

// 确认删除
const confirmDelete = () => {
  if (imageToDelete.value) {
    deleteMajorImage(
      imageToDelete.value,
      (message, data) => {
        if (data) {
          ElMessage.success('图片删除成功');
          // 重新加载专业信息以更新图片列表
          fetchMajorFullInfo(currentMajorId.value);
        } else {
          ElMessage.error(message || '图片删除失败');
        }
      },
      (error) => {
        ElMessage.error('图片删除失败：' + error);
      }
    );
    showDeleteConfirm.value = false;
    imageToDelete.value = null;
  }
};

// 取消删除
const cancelDelete = () => {
  showDeleteConfirm.value = false;
  imageToDelete.value = null;
};

// 根据学院ID获取对应的专业介绍组件
const getMajorIntroComponent = (collegeId) => {
  switch (collegeId) {
    case 1: return MajorIntro;
    case 2: return ElectricalMajorIntro;
    case 3: return MechanicalMajorIntro;
    case 4: return ManagementMajorIntro;
    case 5: return ArtMajorIntro;
    default: return MajorIntro;
  }
};

// 处理图片查看
const handleImageView = (imageUrl, description) => {
  currentViewImage.value = {
    url: baseURL + (imageUrl.startsWith('/') ? imageUrl.substring(1) : imageUrl),
    description: description || ''
  };
  showImageViewer.value = true;
};

// 关闭图片查看器
const closeImageViewer = () => {
  showImageViewer.value = false;
  currentViewImage.value = null;
};

// 加载初始数据
onMounted(() => {
  fetchColleges();
});
</script>

<template>
  <div class="courses-container theme-transition">
    <!-- 左侧固定学院选择区域 -->
    <div class="sidebar-container">
      <div class="sidebar-content">
        <h2 class="sidebar-title">学院选择</h2>
        <div v-if="loading && colleges.length === 0" class="loading-indicator">
          <span>加载中...</span>
        </div>
        <div v-else class="college-list">
          <button
            v-for="college in colleges"
            :key="college.id"
            @click="selectCollege(college)"
            :class="[
              'college-btn',
              selectedCollege === college ? 'college-btn-active' : ''
            ]"
          >
            {{ college.name }}
          </button>
        </div>
      </div>
    </div>

    <!-- 右侧内容区域 -->
    <div class="main-content">
      <!-- 专业选择区域 -->
      <div v-if="selectedCollege" class="major-selection">
        <h2 class="text-xl font-bold mb-4">{{ selectedCollege.name }}专业</h2>
        <div v-if="loading && selectedCollege.majors.length === 0" class="loading-indicator">
          <span>加载中...</span>
        </div>
        <div v-else class="major-buttons">
          <button
            v-for="major in selectedCollege.majors"
            :key="major"
            @click="selectMajor(major)"
            :class="[
              'major-btn',
              selectedMajor === major ? 'major-btn-active' : ''
            ]"
          >
            {{ major }}
          </button>
        </div>
      </div>

      <!-- 内容展示区域 -->
      <div v-if="selectedMajor" class="content-display">
        <!-- 内容类型选择标签 -->
        <div class="content-tabs">
          <button
            v-for="(label, type) in {
              intro: '专业介绍',
              credits: '学分要求',
              courses: '主要课程',
              structure: '课程关系图'
            }"
            :key="type"
            @click="selectedContentType = type"
            :class="[
              'tab-btn',
              selectedContentType === type ? 'tab-btn-active' : ''
            ]"
          >
            {{ label }}
          </button>
        </div>

        <!-- 内容显示区域 -->
        <div class="content-body">
          <!-- 加载指示器 -->
          <div v-if="loading" class="loading-state">
            <div class="loading-spinner"></div>
            <p class="loading-text">加载数据中...</p>
          </div>
          
          <!-- 专业介绍 -->
          <div v-else-if="selectedContentType === 'intro'" class="space-y-4">
            <h3 class="content-title">{{ selectedMajor }}专业介绍</h3>
            <div class="content-panel">
              <component 
                :is="getMajorIntroComponent(selectedCollege.id)" 
                :major="selectedMajor" 
              />
            </div>
          </div>

          <!-- 学分要求 -->
          <CreditRequirements 
            v-else-if="selectedContentType === 'credits'"
            :majorId="currentMajorId"
            :majorData="majorData"
            :isAdmin="isAdmin()"
            @image-upload="handleImageUpload"
            @image-delete="showDeleteConfirmDialog"
            @image-view="handleImageView"
          />

          <!-- 主要课程 -->
          <MainCourses 
            v-else-if="selectedContentType === 'courses'"
            :majorId="currentMajorId"
            :majorData="majorData"
            :isAdmin="isAdmin()"
            @image-upload="handleImageUpload"
            @image-delete="showDeleteConfirmDialog"
            @image-view="handleImageView"
          />

          <!-- 课程关系图 -->
          <div v-else-if="selectedContentType === 'structure'" class="space-y-4">
            <div class="flex justify-between items-center">
              <h3 class="content-title">课程关系图</h3>
              <div class="flex items-center space-x-2">
                <!-- 管理员上传图片按钮 -->
                <button 
                  v-if="isAdmin()" 
                  @click="handleImageUpload('COURSE_STRUCTURE')" 
                  class="admin-btn"
                >
                  上传课程关系图
                </button>
              </div>
            </div>
            <div class="content-panel">
              <!-- 显示课程结构图片 -->
              <div v-if="majorData.courseStructureImages && majorData.courseStructureImages.length > 0" class="image-gallery">
                <div v-for="(image, index) in majorData.courseStructureImages" :key="index" class="image-item">
                  <div class="image-wrapper">
                    <img 
                      :src="baseURL + (image.imageUrl.startsWith('/') ? image.imageUrl.substring(1) : image.imageUrl)" 
                      :alt="image.description || '课程关系图'" 
                      class="gallery-image" 
                      @click="handleImageView(image.imageUrl, image.description)"
                    />
                    <div class="image-overlay">
                      <button 
                        v-if="isAdmin()"
                        @click.stop="showDeleteConfirmDialog(image)" 
                        class="image-delete-btn" 
                        title="删除图片"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
                          <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                        </svg>
                      </button>
                    </div>
                  </div>
                  <p v-if="image.description" class="image-description">{{ image.description }}</p>
                </div>
              </div>
              
              <p v-else class="placeholder-text">暂无课程关系图</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 未选择专业时显示提示 -->
      <div v-else class="empty-state">
        <div class="empty-state-content">
          <div class="icon-container">
            <!-- 更换为学校/教育相关图标 -->
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="empty-state-icon">
              <path stroke-linecap="round" stroke-linejoin="round" d="M4.26 10.147a60.438 60.438 0 0 0-.491 6.347A48.62 48.62 0 0 1 12 20.904a48.62 48.62 0 0 1 8.232-4.41 60.46 60.46 0 0 0-.491-6.347m-15.482 0a50.636 50.636 0 0 0-2.658-.813A59.906 59.906 0 0 1 12 3.493a59.903 59.903 0 0 1 10.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.717 50.717 0 0 1 12 13.489a50.702 50.702 0 0 1 7.74-3.342M6.75 15a.75.75 0 1 0 0-1.5.75.75 0 0 0 0 1.5Zm0 0v-3.675A55.378 55.378 0 0 1 12 8.443m-7.007 11.55A5.981 5.981 0 0 0 6.75 15.75v-1.5" />
            </svg>
            <!-- 添加装饰元素 -->
            <div class="decoration-dot dot-1"></div>
            <div class="decoration-dot dot-2"></div>
            <div class="decoration-dot dot-3"></div>
          </div>
          <h3 class="empty-state-title">请选择学院与专业</h3>
          <p class="empty-state-message">您可以通过左侧边栏选择学院，然后选择感兴趣的专业，探索各种专业详情、课程信息和学分要求</p>
          
          <!-- 添加指引动画箭头 -->

          <div class="guide-arrow">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5 3 12m0 0 7.5-7.5M3 12h18" />
            </svg>
            <span class="guide-text">通过左侧选择学院</span>
          </div>
          <div class="guide-arrow vertical">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 10.5 12 3m0 0 7.5 7.5M12 3v18" />
            </svg>
            <span class="guide-text">通过上侧选择专业</span>
          </div>
          
          <!-- 添加学院专业数量信息 -->
          <div class="stats-container">
            <div class="stat-item">
              <div class="stat-value">{{ colleges.length }}</div>
              <div class="stat-label">个学院</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ majors.length }}</div>
              <div class="stat-label">个专业</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图片上传模态框 -->
    <div v-if="showUploadModal" class="modal-overlay">
      <div class="modal-container">
        <div class="modal-header">
          <h3 class="modal-title">上传图片</h3>
          <button @click="showUploadModal = false" class="modal-close-btn">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="image-file" class="form-label">选择图片文件</label>
            <input 
              type="file" 
              id="image-file" 
              ref="fileInputRef"
              @change="handleFileChange" 
              accept="image/*" 
              class="form-input"
            />
            <div v-if="imageFile" class="mt-2 text-sm text-gray-600 dark:text-gray-400">
              已选择: {{ imageFile.name }}
            </div>
          </div>
          <div class="form-group">
            <label for="image-description" class="form-label">图片描述 (可选)</label>
            <input 
              type="text" 
              id="image-description" 
              v-model="imageDescription" 
              class="form-input" 
              placeholder="请输入图片描述"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button @click="showUploadModal = false" class="cancel-btn">取消</button>
          <button @click="uploadImage" class="submit-btn" :disabled="imageUploadLoading">
            <span v-if="!imageUploadLoading">上传</span>
            <span v-else class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              上传中...
            </span>
          </button>
        </div>
      </div>
    </div>

    <!-- 替换自定义弹窗为CustomConfirmDialog -->
    <CustomConfirmDialog
      v-model:visible="showDeleteConfirm"
      title="确认删除"
      content="您确定要删除这张图片吗？此操作无法撤销。"
      type="danger"
      confirm-text="确认删除"
      cancel-text="取消"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />

    <!-- 图片查看器 -->
    <div v-if="showImageViewer" class="image-viewer-overlay " @click="closeImageViewer">
      <div class="image-viewer-container" @click.stop>
        <button @click="closeImageViewer" class="image-viewer-close">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        <div class="image-viewer-content">
          <img 
            v-if="currentViewImage" 
            :src="currentViewImage.url" 
            :alt="currentViewImage.description" 
            class="image-viewer-img" 
          />
        </div>
        <div v-if="currentViewImage && currentViewImage.description" class="image-viewer-description-container">
          <p class="image-viewer-description">
            {{ currentViewImage.description }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.courses-container {
  @apply flex min-h-screen w-full;
  background-color: #f8fafc;
}

.dark .courses-container {
  background-color: #0f172a;
}

/* 左侧固定侧边栏样式 */
.sidebar-container {
  @apply w-64 flex-shrink-0 fixed h-full overflow-auto;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  border-right: 1px solid rgba(226, 232, 240, 0.8);
  z-index: 10;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
}

.dark .sidebar-container {
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  border-right: 1px solid rgba(30, 41, 59, 0.8);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sidebar-content {
  @apply p-6 space-y-6;
}

.sidebar-title {
  @apply text-xl font-bold text-gray-800 dark:text-gray-200 mb-4;
  text-align: center;
  position: relative;
}

.sidebar-title::after {
  content: "";
  position: absolute;
  bottom: -8px;
  left: 50%;
  width: 40px;
  height: 2px;
  background-color: #3b82f6;
  transform: translateX(-50%);
}

.college-list {
  @apply flex flex-col space-y-3 mt-6;
}

.college-btn {
  @apply px-4 py-3 rounded-lg font-medium text-left transition-all duration-300;
  background-color: rgba(255, 255, 255, 0.7);
  color: #334155;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  border-left: 3px solid transparent;
}

.dark .college-btn {
  background-color: rgba(30, 41, 59, 0.7);
  color: #e2e8f0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.college-btn:hover {
  @apply transform translate-x-1;
  background-color: rgba(40, 155, 225, 0.9);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dark .college-btn:hover {
  background-color: rgba(51, 65, 85, 0.9);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
}

.college-btn-active {
  background-color: #3b82f6;
  color: white;
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);
  /* border-left: 3px solid #1d4ed8; */
}

.dark .college-btn-active {
  background-color: #1d3b9e;
  box-shadow: 0 2px 4px rgba(37, 99, 235, 0.3);
  /* border-left: 3px solid #3b82f6; */
}

/* 右侧主内容区域样式 */
.main-content {
  @apply flex-grow p-6 ml-64;
  max-width: calc(100% - 16rem);
}

/* 专业选择区域样式 */
.major-selection {
  @apply mb-8 bg-white dark:bg-gray-800 rounded-2xl shadow-sm p-6;
  border: 1px solid rgba(226, 232, 240, 0.8);
  transition: all 0.3s ease;
}

.dark .major-selection {
  border: 1px solid rgba(30, 41, 59, 0.8);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.major-buttons {
  @apply flex flex-wrap gap-3 mt-4;
}

.major-btn {
  @apply px-4 py-2 rounded-full transition-all duration-300;
  background-color: #f8fafc;
  color: #475569;
}

.dark .major-btn {
  background-color: #1e293b;
  color: #cbd5e1;
}

.major-btn:hover {
  @apply transform scale-105;
  background-color: #f1f5f9;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.dark .major-btn:hover {
  background-color: #334155;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.major-btn-active {
  background-color: #dbeafe;
  color: #2563eb;
  font-weight: 500;
  box-shadow: 0 1px 2px rgba(37, 99, 235, 0.1);
}

.dark .major-btn-active {
  background-color: #1e40af;
  color: #bfdbfe;
  box-shadow: 0 1px 2px rgba(37, 99, 235, 0.15);
}

/* 内容展示区域样式 */
.content-display {
  @apply bg-white dark:bg-gray-800 rounded-2xl shadow-sm overflow-hidden;
  border: 1px solid rgba(226, 232, 240, 0.8);
  transition: all 0.3s ease;
}

.dark .content-display {
  border: 1px solid rgba(30, 41, 59, 0.8);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.content-display:hover {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
}

.dark .content-display:hover {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
}

.content-tabs {
  @apply flex border-b border-gray-200 dark:border-gray-700;
}

.tab-btn {
  @apply flex-1 py-4 text-center font-medium transition-all duration-300;
  color: #64748b;
  position: relative;
}

.dark .tab-btn {
  color: #94a3b8;
}

.tab-btn:hover {
  color: #334155;
  background-color: rgba(241, 245, 249, 0.8);
}

.dark .tab-btn:hover {
  color: #e2e8f0;
  background-color: rgba(30, 41, 59, 0.8);
}

.tab-btn-active {
  color: #2563eb;
  background-color: rgba(219, 234, 254, 0.5);
  position: relative;
}

.tab-btn-active::after {
  content: "";
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #2563eb;
}

.dark .tab-btn-active {
  color: #3b82f6;
  background-color: rgba(30, 58, 138, 0.3);
}

.dark .tab-btn-active::after {
  background-color: #3b82f6;
}

.content-body {
  @apply p-6;
}

.content-title {
  @apply text-xl font-bold mb-5;
  color: #1e293b;
}

.dark .content-title {
  color: #f1f5f9;
}

.content-panel {
  @apply p-5 rounded-xl shadow-sm;
  background-color: #f8fafc;
  border: 1px solid rgba(226, 232, 240, 0.8);
}

.dark .content-panel {
  background-color: #1e293b;
  border: 1px solid rgba(30, 41, 59, 0.8);
}

.placeholder-text {
  @apply text-gray-500 dark:text-gray-400 text-center py-10 italic;
}

/* 主题过渡动画 */
.theme-transition {
  transition: all 0.3s ease;
}

/* 加载状态样式 */
.loading-indicator {
  @apply flex justify-center items-center p-4 text-blue-600 dark:text-blue-400;
}

.loading-state {
  @apply flex flex-col items-center justify-center p-8;
}

.loading-spinner {
  @apply w-10 h-10 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin;
}

.dark .loading-spinner {
  @apply border-gray-700 border-t-blue-500;
}

.loading-text {
  @apply mt-4 text-gray-600 dark:text-gray-400;
}

/* 空状态样式 */
.empty-state {
  @apply flex items-center justify-center h-96 bg-white dark:bg-gray-800 rounded-2xl shadow-sm;
  border: 1px solid rgba(226, 232, 240, 0.8);
  transition: all 0.3s ease;
  background-image: radial-gradient(circle at 10% 90%, rgba(59, 130, 246, 0.03) 0%, transparent 30%),
                    radial-gradient(circle at 90% 10%, rgba(59, 130, 246, 0.03) 0%, transparent 30%);
}

.dark .empty-state {
  border: 1px solid rgba(30, 41, 59, 0.8);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  background-image: radial-gradient(circle at 10% 90%, rgba(59, 130, 246, 0.05) 0%, transparent 30%),
                    radial-gradient(circle at 90% 10%, rgba(59, 130, 246, 0.05) 0%, transparent 30%);
}

.empty-state-content {
  @apply flex flex-col items-center justify-center text-center p-8 max-w-lg;
}

.icon-container {
  @apply relative mb-6;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.empty-state-icon {
  @apply w-24 h-24 text-blue-500 dark:text-blue-400;
  filter: drop-shadow(0px 2px 4px rgba(59, 130, 246, 0.15));
}

.decoration-dot {
  @apply absolute rounded-full opacity-75;
  animation: pulse 2s ease-in-out infinite;
}

.dot-1 {
  @apply w-3 h-3 bg-yellow-400 dark:bg-yellow-500;
  top: -5px;
  right: -5px;
  animation-delay: 0s;
}

.dot-2 {
  @apply w-2 h-2 bg-green-400 dark:bg-green-500;
  bottom: 10px;
  right: -8px;
  animation-delay: 0.5s;
}

.dot-3 {
  @apply w-2 h-2 bg-red-400 dark:bg-red-500;
  top: 15px;
  left: -5px;
  animation-delay: 1s;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.75; }
  50% { transform: scale(1.1); opacity: 0.6; }
}

.empty-state-title {
  @apply text-2xl font-bold mb-3 text-gray-800 dark:text-gray-200;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.dark .empty-state-title {
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.empty-state-message {
  @apply text-gray-600 dark:text-gray-400 max-w-md mb-6;
  line-height: 1.5;
}

.guide-arrow {
  @apply flex items-center mb-4 text-blue-500 dark:text-blue-400 font-medium;
  animation: point-left 1.5s ease-in-out infinite;
}

@keyframes point-left {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(-5px); }
}

.arrow-icon {
  @apply w-5 h-5 mr-2;
}

.guide-text {
  @apply text-sm;
}

.stats-container {
  @apply flex items-center justify-center mt-4 p-3 bg-blue-50 dark:bg-blue-900 dark:bg-opacity-20 rounded-xl;
  min-width: 200px;
}

.stat-item {
  @apply flex flex-col items-center;
}

.stat-value {
  @apply text-2xl font-bold text-blue-600 dark:text-blue-400;
}

.stat-label {
  @apply text-sm text-gray-600 dark:text-gray-400 mt-1;
}

.stat-divider {
  @apply h-8 w-px bg-gray-300 dark:bg-gray-600 mx-5;
}

/* 图片画廊样式 */
.image-gallery {
  @apply grid grid-cols-1 md:grid-cols-2 gap-5 mt-5;
}

.image-item {
  @apply bg-white dark:bg-gray-900 rounded-xl overflow-hidden shadow-sm transition-all duration-300 hover:shadow cursor-pointer;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.image-item:hover {
  transform: translateY(-3px);
}

.image-wrapper {
  @apply relative overflow-hidden;
}

.gallery-image {
  @apply w-full h-auto object-contain max-h-64 transition-transform duration-300;
}

.image-wrapper:hover .gallery-image {
  transform: scale(1.02);
}

.image-overlay {
  @apply absolute top-3 right-3 opacity-0 transition-opacity duration-300;
}

.image-wrapper:hover .image-overlay {
  @apply opacity-100;
}

.image-description {
  @apply p-3 text-sm text-gray-700 dark:text-gray-300 bg-gray-50 dark:bg-gray-800 border-t border-gray-100 dark:border-gray-700;
}

/* 管理员按钮样式 */
.admin-btn {
  @apply flex items-center gap-2 px-4 py-2 rounded-full text-white font-medium shadow-sm hover:shadow transition-all duration-300 bg-blue-500 hover:bg-blue-600 border border-blue-400 transform hover:-translate-y-0.5 active:translate-y-0;
}

.admin-btn::before {
  content: '';
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="white" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" /></svg>');
  @apply w-5 h-5 inline-block bg-no-repeat bg-center bg-contain;
}

.dark .admin-btn {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  @apply border-blue-700 text-white font-medium;
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);
}

.dark .admin-btn:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  box-shadow: 0 3px 6px rgba(37, 99, 235, 0.2);
}

/* 删除按钮样式 */
.image-delete-btn {
  @apply p-2 rounded-full bg-red-500 bg-opacity-80 text-white hover:bg-red-600 transition-colors duration-200 shadow-sm hover:shadow transform scale-100 hover:scale-110;
}

.dark .image-delete-btn {
  @apply bg-red-600 hover:bg-red-700;
}

/* 模态框样式 */
.modal-overlay {
  @apply fixed inset-0 flex items-center justify-center z-50;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(2px);
}

.modal-container {
  @apply bg-white dark:bg-gray-800 rounded-xl shadow-lg max-w-md w-full mx-4 transform transition-all duration-300;
  animation: modal-in 0.3s ease-out;
}

@keyframes modal-in {
  0% {
    opacity: 0;
    transform: scale(0.95);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.modal-header {
  @apply flex justify-between items-center p-4 border-b border-gray-200 dark:border-gray-700;
}

.modal-title {
  @apply text-lg font-bold text-gray-800 dark:text-gray-200;
}

.modal-close-btn {
  @apply text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 transition-colors duration-200;
}

.modal-body {
  @apply p-5;
}

.modal-footer {
  @apply flex justify-end p-4 border-t border-gray-200 dark:border-gray-700 space-x-3;
}

.form-group {
  @apply mb-4;
}

.form-label {
  @apply block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1;
}

.form-input {
  @apply w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-xl shadow-sm bg-white dark:bg-gray-700 text-gray-800 dark:text-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all duration-200;
}

.cancel-btn {
  @apply px-4 py-2 rounded-full bg-gray-200 hover:bg-gray-300 text-gray-700 dark:bg-gray-700 dark:hover:bg-gray-600 dark:text-gray-300 text-sm font-medium transition-all duration-200;
}

.submit-btn {
  @apply px-4 py-2 rounded-full bg-blue-500 hover:bg-blue-600 text-white text-sm font-medium transition-all duration-200 shadow-sm hover:shadow disabled:opacity-50 disabled:cursor-not-allowed;
}

/* 图片查看器样式 */
.image-viewer-overlay {
  @apply fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50;
  backdrop-filter: blur(5px);
}

.image-viewer-container {
  @apply relative w-full h-full flex flex-col items-center justify-center;
  max-width: 95vw;
  max-height: 95vh;
}

.image-viewer-close {
  @apply absolute top-4 right-4 p-2 rounded-full bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200 shadow-md z-10 hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors;
}

.image-viewer-content {
  @apply flex items-center justify-center w-full h-full;
  height: calc(100% - 80px);
}

.image-viewer-img {
  @apply object-contain rounded-xl transition-all duration-300;
  max-width: 95vw;
  max-height: 85vh;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.image-viewer-description-container {
  @apply fixed bottom-0 left-0 right-0 pb-6 flex justify-center;
}

.image-viewer-description {
  @apply px-6 py-3 bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200 rounded-full shadow-md text-center;
  max-width: 800px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar-container {
    @apply w-16;
  }
  
  .sidebar-title, .college-btn {
    @apply text-center;
  }
  
  .college-btn {
    @apply p-2;
  }
  
  .main-content {
    @apply ml-16;
    max-width: calc(100% - 4rem);
  }
  
  .image-gallery {
    @apply grid-cols-1;
  }
}

/* 添加上下浮动动画用于第二个指引箭头 */
.vertical {
  animation: float 2s ease-in-out infinite;
}
</style>
