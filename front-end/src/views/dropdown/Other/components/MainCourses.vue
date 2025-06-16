<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { updateMajorContent } from '@/net/courseApi.js';
import { baseURL } from '@/main.js';

const props = defineProps({
  majorId: Number,
  majorData: Object,
  isAdmin: Boolean
});

const emit = defineEmits(['image-upload', 'image-delete', 'image-view']);

// 内容编辑相关
const isEditingCourses = ref(false);
const contentUpdateLoading = ref(false);
const newCourseInput = ref('');

// 课程标签数组
const courseTags = ref([]);

// 课程分类
const courseCategories = ref([
  { name: '必修课程', color: 'blue' },
  { name: '选修课程', color: 'green' },
  { name: '实践课程', color: 'purple' },
  { name: '公共课程', color: 'orange' }
]);
const selectedCategory = ref(courseCategories.value[0]);

// 解析文本为标签
const parseCoursesToTags = (text) => {
  if (!text) {
    courseTags.value = [];
    return;
  }
  
  const tags = [];
  const lines = text.split('\n');
  
  for (const line of lines) {
    if (line.trim() === '') continue;
    
    // 尝试解析类别和课程名
    const categoryMatch = line.match(/^(.+?)：(.+)$/);
    if (categoryMatch && categoryMatch[1] && categoryMatch[2]) {
      const category = categoryMatch[1].trim();
      const courseName = categoryMatch[2].trim();
      
      // 确定分类颜色
      let color = 'gray';
      const foundCategory = courseCategories.value.find(cat => cat.name === category);
      if (foundCategory) {
        color = foundCategory.color;
      }
      
      tags.push({
        category: category,
        name: courseName,
        color: color
      });
    } else {
      // 如果没有特定格式，则作为默认分类
      tags.push({
        category: '课程',
        name: line.trim(),
        color: 'gray'
      });
    }
  }
  
  courseTags.value = tags;
};

// 将标签转换为文本
const convertTagsToText = () => {
  // 按分类将课程分组
  const coursesByCategory = {};
  
  for (const tag of courseTags.value) {
    if (!coursesByCategory[tag.category]) {
      coursesByCategory[tag.category] = [];
    }
    coursesByCategory[tag.category].push(tag.name);
  }
  
  // 转换为文本格式
  let result = '';
  for (const category in coursesByCategory) {
    for (const course of coursesByCategory[category]) {
      result += `${category}：${course}\n`;
    }
  }
  
  return result.trim();
};

// 开始编辑主要课程内容
const startEditingCourses = () => {
  parseCoursesToTags(props.majorData.mainCourses);
  isEditingCourses.value = true;
};

// 取消编辑主要课程内容
const cancelEditingCourses = () => {
  isEditingCourses.value = false;
  courseTags.value = [];
  newCourseInput.value = '';
};

// 添加新课程
const addNewCourse = () => {
  if (!newCourseInput.value.trim()) {
    ElMessage.warning('请输入课程名称');
    return;
  }
  
  courseTags.value.push({
    category: selectedCategory.value.name,
    name: newCourseInput.value.trim(),
    color: selectedCategory.value.color
  });
  
  newCourseInput.value = '';
};

// 添加课程按回车键
const handleKeyDown = (event) => {
  if (event.key === 'Enter') {
    event.preventDefault();
    addNewCourse();
  }
};

// 删除课程标签
const removeTag = (index) => {
  courseTags.value.splice(index, 1);
};

// 保存主要课程内容
const saveEditedCourses = () => {
  if (!props.majorId) {
    ElMessage.warning('未选择专业，无法保存内容');
    return;
  }
  
  contentUpdateLoading.value = true;
  
  const coursesText = convertTagsToText();
  
  const data = {
    creditRequirements: props.majorData.creditRequirements,
    mainCourses: coursesText
  };
  
  updateMajorContent(
    props.majorId,
    data,
    (message, data) => {
      contentUpdateLoading.value = false;
      if (data) {
        ElMessage.success('主要课程内容更新成功');
        props.majorData.mainCourses = coursesText;
        isEditingCourses.value = false;
        
        console.log('主要课程更新成功:', coursesText);
      } else {
        ElMessage.error(message || '主要课程内容更新失败');
      }
    },
    (error) => {
      contentUpdateLoading.value = false;
      ElMessage.error('主要课程内容更新失败：' + error);
    }
  );
};

// 计算按类别分组的标签
const groupedTags = computed(() => {
  const grouped = {};
  
  for (const tag of courseTags.value) {
    if (!grouped[tag.category]) {
      grouped[tag.category] = [];
    }
    grouped[tag.category].push(tag);
  }
  
  return grouped;
});

// 调试图片URL的方法
const debugImageUrl = (url) => {
  return url;
};

// 打开图片上传模态框
const openUploadModal = () => {
  emit('image-upload', 'MAIN_COURSE');
};

// 删除图片
const deleteImage = (imageId) => {
  emit('image-delete', imageId);
};

// 打开图片查看器
const openImageViewer = (imageUrl, description) => {
  emit('image-view', imageUrl, description);
};

// 初始化分类选择和解析现有课程数据
onMounted(() => {
  if (courseCategories.value.length > 0) {
    selectedCategory.value = courseCategories.value[0];
  }
  
  // 初始解析课程数据
  if (props.majorData && props.majorData.mainCourses) {
    parseCoursesToTags(props.majorData.mainCourses);
  }
});

// 监听课程数据变化，自动解析标签
watch(() => props.majorData.mainCourses, (newValue) => {
  if (newValue) {
    parseCoursesToTags(newValue);
  }
}, { immediate: true });

// 添加调试输出
console.log('MainCourses - isAdmin属性:', props.isAdmin);
</script>

<template>
  <div class="space-y-4">
    <div class="flex justify-between items-center">
      <h3 class="content-title">主要课程</h3>
      <div class="flex items-center space-x-2">
        <!-- 管理员编辑文本按钮 -->
        <button 
          v-if="props.isAdmin && !isEditingCourses" 
          @click="startEditingCourses" 
          class="admin-btn"
        >
          编辑主要课程
        </button>
        <!-- 管理员上传图片按钮 -->
        <button 
          v-if="props.isAdmin" 
          @click="openUploadModal" 
          class="admin-btn"
        >
          上传主要课程图片
        </button>
      </div>
    </div>
    <div class="content-panel">
      <!-- 显示主要课程（非编辑模式） -->
      <div v-if="majorData.mainCourses && !isEditingCourses" class="main-courses">
        <!-- 显示课程标签 -->
        <div v-if="Object.keys(groupedTags).length > 0" class="courses-display">
          <div v-for="(category, index) in Object.keys(groupedTags)" :key="index" class="course-category-section">
            <h4 class="course-category-title">{{ category }}</h4>
            <div class="course-tags-display">
              <div 
                v-for="(tag, tagIndex) in groupedTags[category]" 
                :key="tagIndex"
                class="course-tag-display" 
                :class="`tag-${tag.color}`"
              >
                {{ tag.name }}
              </div>
            </div>
          </div>
        </div>
        <!-- 如果没有解析出标签 -->
        <pre v-else class="whitespace-pre-wrap">{{ majorData.mainCourses }}</pre>
      </div>
      
      <!-- 主要课程编辑表单 -->
      <div v-if="isEditingCourses" class="edit-form">
        <div class="tag-editor-container">
          <!-- 分类选择 -->
          <div class="category-selector">
            <label class="form-label">课程分类:</label>
            <div class="category-buttons">
              <button 
                v-for="category in courseCategories" 
                :key="category.name"
                @click="selectedCategory = category"
                :class="[
                  'category-button', 
                  `category-${category.color}`,
                  selectedCategory.name === category.name ? 'category-active' : ''
                ]"
              >
                {{ category.name }}
              </button>
            </div>
          </div>
          
          <!-- 输入框和添加按钮 -->
          <div class="tag-input-container">
            <input
              v-model="newCourseInput"
              type="text"
              class="tag-input"
              placeholder="输入课程名称"
              @keydown="handleKeyDown"
            />
            <button @click="addNewCourse" class="add-tag-btn">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              添加
            </button>
          </div>
          
          <!-- 标签展示区 -->
          <div class="tags-container">
            <div v-for="(category, index) in Object.keys(groupedTags)" :key="index" class="tag-category">
              <h4 class="tag-category-title">{{ category }}</h4>
              <div class="tags-list">
                <div 
                  v-for="(tag, tagIndex) in groupedTags[category]" 
                  :key="tagIndex"
                  class="course-tag"
                  :class="`tag-${tag.color}`"
                >
                  <span class="tag-text">{{ tag.name }}</span>
                  <button @click="removeTag(courseTags.indexOf(tag))" class="tag-delete-btn">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
            
            <div v-if="!courseTags.length" class="empty-tags-message">
              请添加主要课程
            </div>
          </div>
        </div>
        
        <div class="edit-form-buttons">
          <button 
            @click="cancelEditingCourses" 
            class="cancel-edit-btn" 
            :disabled="contentUpdateLoading"
          >
            取消
          </button>
          <button 
            @click="saveEditedCourses" 
            class="save-edit-btn" 
            :disabled="contentUpdateLoading"
          >
            <span v-if="!contentUpdateLoading">保存</span>
            <span v-else class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              保存中...
            </span>
          </button>
        </div>
      </div>
      
      <!-- 显示主要课程图片 -->
      <div v-if="majorData.mainCourseImages && majorData.mainCourseImages.length > 0" class="image-gallery mt-4">
        <div v-for="(image, index) in majorData.mainCourseImages" :key="index" class="image-item">
          <div class="image-wrapper">
            <img 
              :src="debugImageUrl(baseURL + (image.imageUrl.startsWith('/') ? image.imageUrl.substring(1) : image.imageUrl))" 
              :alt="image.description || '主要课程'" 
              class="gallery-image" 
              @click="openImageViewer(image.imageUrl, image.description)"
            />
            <div class="image-overlay">
              <button 
                v-if="props.isAdmin"
                @click.stop="deleteImage(image.id)" 
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
      
      <p v-else-if="!majorData.mainCourses && !isEditingCourses" class="placeholder-text">暂无主要课程信息</p>
    </div>
  </div>
</template>

<style scoped>
/* 主要课程样式 */
.main-courses {
  @apply p-5 bg-white dark:bg-gray-900 rounded-lg text-gray-800 dark:text-gray-200 leading-relaxed shadow-sm;
  line-height: 1.75;
}

/* 编辑表单样式 */
.edit-form {
  @apply bg-white dark:bg-gray-800 rounded-lg p-5 shadow-md;
}

.edit-form-buttons {
  @apply flex justify-end space-x-3 mt-6;
}

.cancel-edit-btn {
  @apply px-5 py-2 rounded-md text-gray-700 bg-gray-200 hover:bg-gray-300 dark:text-gray-300 dark:bg-gray-700 dark:hover:bg-gray-600 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 font-medium;
}

.save-edit-btn {
  @apply px-5 py-2 rounded-md text-white bg-blue-500 hover:bg-blue-600 dark:bg-blue-600 dark:hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center shadow-sm hover:shadow-md transition-all duration-200 font-medium;
}

/* 标签编辑器样式 */
.tag-editor-container {
  @apply flex flex-col space-y-5;
}

.category-selector {
  @apply mb-4;
}

.form-label {
  @apply block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2;
}

.category-buttons {
  @apply flex flex-wrap gap-2;
}

.category-button {
  @apply px-3 py-1.5 rounded-md text-sm font-medium shadow-sm transition-all duration-200;
}

.category-blue {
  @apply bg-blue-100 text-blue-800 border border-blue-200 hover:bg-blue-200;
}

.category-green {
  @apply bg-green-100 text-green-800 border border-green-200 hover:bg-green-200;
}

.category-purple {
  @apply bg-purple-100 text-purple-800 border border-purple-200 hover:bg-purple-200;
}

.category-orange {
  @apply bg-orange-100 text-orange-800 border border-orange-200 hover:bg-orange-200;
}

.category-active {
  @apply ring-2 ring-offset-2 dark:ring-offset-gray-800;
}

.category-blue.category-active {
  @apply ring-blue-500;
}

.category-green.category-active {
  @apply ring-green-500;
}

.category-purple.category-active {
  @apply ring-purple-500;
}

.category-orange.category-active {
  @apply ring-orange-500;
}

.dark .category-blue {
  @apply bg-blue-900 bg-opacity-60 text-blue-200 border-blue-700 hover:bg-blue-800;
}

.dark .category-green {
  @apply bg-green-900 bg-opacity-60 text-green-200 border-green-700 hover:bg-green-800;
}

.dark .category-purple {
  @apply bg-purple-900 bg-opacity-60 text-purple-200 border-purple-700 hover:bg-purple-800;
}

.dark .category-orange {
  @apply bg-orange-900 bg-opacity-60 text-orange-200 border-orange-700 hover:bg-orange-800;
}

.tag-input-container {
  @apply flex gap-2;
}

.tag-input {
  @apply flex-1 px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white dark:bg-gray-700 text-gray-800 dark:text-gray-200 outline-none transition-all duration-200;
}

.add-tag-btn {
  @apply flex items-center px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition-colors duration-200 shadow-sm hover:shadow gap-1 font-medium;
}

.dark .add-tag-btn {
  @apply bg-blue-600 hover:bg-blue-700;
}

.tags-container {
  @apply mt-5 border border-gray-200 dark:border-gray-700 rounded-lg p-4 min-h-[200px] max-h-[400px] overflow-y-auto;
  background-color: rgba(241, 245, 249, 0.3);
}

.dark .tags-container {
  background-color: rgba(30, 41, 59, 0.3);
}

.tag-category {
  @apply mb-5 last:mb-0;
}

.tag-category-title {
  @apply text-sm font-semibold text-gray-600 dark:text-gray-400 mb-2 border-b border-gray-200 dark:border-gray-700 pb-1;
}

.tags-list {
  @apply flex flex-wrap gap-2;
}

.empty-tags-message {
  @apply text-center text-gray-500 dark:text-gray-400 py-8 italic;
}

/* 标签样式 */
.course-tag {
  @apply inline-flex items-center gap-1 px-3 py-1.5 rounded-full text-sm font-medium border transition-all duration-200 max-w-full;
}

.course-tag .tag-text {
  @apply truncate;
}

.tag-delete-btn {
  @apply text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200 rounded-full p-0.5 transition-colors duration-200 flex-shrink-0;
}

.tag-blue {
  @apply bg-blue-50 text-blue-700 border-blue-200 hover:bg-blue-100;
}

.tag-green {
  @apply bg-green-50 text-green-700 border-green-200 hover:bg-green-100;
}

.tag-purple {
  @apply bg-purple-50 text-purple-700 border-purple-200 hover:bg-purple-100;
}

.tag-orange {
  @apply bg-orange-50 text-orange-700 border-orange-200 hover:bg-orange-100;
}

.tag-gray {
  @apply bg-gray-50 text-gray-700 border-gray-200 hover:bg-gray-100;
}

.dark .tag-blue {
  @apply bg-blue-900 bg-opacity-30 text-blue-300 border-blue-700 hover:bg-opacity-40;
}

.dark .tag-green {
  @apply bg-green-900 bg-opacity-30 text-green-300 border-green-700 hover:bg-opacity-40;
}

.dark .tag-purple {
  @apply bg-purple-900 bg-opacity-30 text-purple-300 border-purple-700 hover:bg-opacity-40;
}

.dark .tag-orange {
  @apply bg-orange-900 bg-opacity-30 text-orange-300 border-orange-700 hover:bg-opacity-40;
}

.dark .tag-gray {
  @apply bg-gray-800 bg-opacity-50 text-gray-300 border-gray-700 hover:bg-opacity-60;
}

/* 只读标签样式 */
.course-tag-readonly {
  @apply inline-block px-3 py-1.5 rounded-full text-sm font-medium m-1 transition-transform duration-200 hover:scale-105;
}

/* 课程类别区域 */
.course-category-section {
  @apply mb-5 last:mb-0;
}

.course-category-title {
  @apply text-base font-semibold text-gray-700 dark:text-gray-300 mb-2 border-b border-gray-200 dark:border-gray-700 pb-1;
}

.course-tags {
  @apply flex flex-wrap -m-1;
}

/* 图片画廊样式 */
.image-gallery {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 mt-6;
}

.image-item {
  @apply bg-white dark:bg-gray-900 rounded-lg overflow-hidden shadow-lg transition-all duration-300 hover:shadow-xl cursor-pointer;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.image-item:hover {
  transform: translateY(-5px);
}

.image-wrapper {
  @apply relative overflow-hidden;
}

.gallery-image {
  @apply w-full h-auto object-contain max-h-64 transition-transform duration-300;
}

.image-wrapper:hover .gallery-image {
  transform: scale(1.03);
}

.image-overlay {
  @apply absolute top-3 right-3 opacity-0 transition-opacity duration-300;
}

.image-wrapper:hover .image-overlay {
  @apply opacity-100;
}

.image-description {
  @apply p-4 text-sm text-gray-700 dark:text-gray-300 bg-gray-50 dark:bg-gray-800 border-t border-gray-100 dark:border-gray-700;
}

.image-delete-btn {
  @apply p-2 rounded-full bg-red-500 bg-opacity-80 text-white hover:bg-red-600 transition-colors duration-200 shadow-sm hover:shadow-md transform scale-100 hover:scale-110;
}

.dark .image-delete-btn {
  @apply bg-red-600 hover:bg-red-700;
}

.content-title {
  @apply text-xl font-bold mb-4;
  color: #1e293b;
}

.dark .content-title {
  color: #f1f5f9;
}

.content-panel {
  @apply p-5 rounded-lg shadow-sm border border-gray-100 dark:border-gray-700;
  background-color: #f8fafc;
}

.dark .content-panel {
  background-color: #1e293b;
}

.placeholder-text {
  @apply text-gray-500 dark:text-gray-400 text-center py-10 italic;
}

.admin-btn {
  @apply flex items-center gap-2 px-4 py-2 rounded-lg text-white font-medium shadow-sm hover:shadow-md transition-all duration-300 bg-blue-500 hover:bg-blue-600 border border-blue-400 transform hover:-translate-y-0.5 active:translate-y-0;
}

.admin-btn::before {
  content: '';
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="white" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" /></svg>');
  @apply w-5 h-5 inline-block bg-no-repeat bg-center bg-contain;
}

.dark .admin-btn {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  @apply border-blue-700 text-white font-medium;
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3), 0 2px 4px -1px rgba(59, 130, 246, 0.2);
}

.dark .admin-btn:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  box-shadow: 0 8px 12px -3px rgba(37, 99, 235, 0.3), 0 4px 6px -2px rgba(37, 99, 235, 0.2);
}

/* 课程显示样式 */
.courses-display {
  @apply bg-white dark:bg-gray-900 rounded-lg p-5 space-y-6;
}

.course-tags-display {
  @apply flex flex-wrap gap-2 mt-3;
}

.course-tag-display {
  @apply px-4 py-2 rounded-lg text-sm font-medium transition-all duration-200 shadow-sm;
  min-width: 120px;
  text-align: center;
  transform: translateY(0);
}

.course-tag-display:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.dark .course-tag-display:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

.tag-blue.course-tag-display {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  color: #1e40af;
  border: 1px solid #93c5fd;
}

.tag-green.course-tag-display {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
  border: 1px solid #6ee7b7;
}

.tag-purple.course-tag-display {
  background: linear-gradient(135deg, #ede9fe 0%, #ddd6fe 100%);
  color: #5b21b6;
  border: 1px solid #c4b5fd;
}

.tag-orange.course-tag-display {
  background: linear-gradient(135deg, #ffedd5 0%, #fed7aa 100%);
  color: #9a3412;
  border: 1px solid #fdba74;
}

.tag-gray.course-tag-display {
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  color: #1f2937;
  border: 1px solid #d1d5db;
}

.dark .tag-blue.course-tag-display {
  background: linear-gradient(135deg, #1e3a8a 0%, #1e40af 100%);
  color: #bfdbfe;
  border: 1px solid #1e40af;
}

.dark .tag-green.course-tag-display {
  background: linear-gradient(135deg, #064e3b 0%, #065f46 100%);
  color: #a7f3d0;
  border: 1px solid #065f46;
}

.dark .tag-purple.course-tag-display {
  background: linear-gradient(135deg, #4c1d95 0%, #5b21b6 100%);
  color: #ddd6fe;
  border: 1px solid #5b21b6;
}

.dark .tag-orange.course-tag-display {
  background: linear-gradient(135deg, #7c2d12 0%, #9a3412 100%);
  color: #fed7aa;
  border: 1px solid #9a3412;
}

.dark .tag-gray.course-tag-display {
  background: linear-gradient(135deg, #111827 0%, #1f2937 100%);
  color: #e5e7eb;
  border: 1px solid #374151;
}
</style> 