<script setup>
import { ref, reactive, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { updateMajorContent } from '@/net/courseApi.js';
import { baseURL } from '@/main.js';

const props = defineProps({
  majorId: Number,
  majorData: Object,
  isAdmin: Boolean
});

const emit = defineEmits(['image-upload', 'image-delete', 'image-view']);

// 表格形式的学分要求数据
const creditTable = reactive({
  rows: []
});

// 内容编辑相关
const isEditingCredit = ref(false);
const contentUpdateLoading = ref(false);

// 表格编辑相关
const editedCreditTable = reactive({
  rows: []
});

// 解析文本形式的学分要求为表格数据
const parseTextToTable = (text) => {
  if (!text) return;
  
  const rows = [];
  // 分割文本为行
  const lines = text.split('\n');
  
  // 处理总学分行
  if (lines.length > 0) {
    const totalMatch = lines[0].match(/总学分要求：(\d+)学分/);
    if (totalMatch && totalMatch[1]) {
      rows.push({
        category: '总学分要求',
        credits: totalMatch[1],
        notes: ''
      });
    }
  }
  
  // 处理其他学分类别
  for (let i = 1; i < lines.length; i++) {
    const line = lines[i];
    const match = line.match(/(.+)：(\d+)学分/);
    if (match && match[1] && match[2]) {
      rows.push({
        category: match[1],
        credits: match[2],
        notes: ''
      });
    }
  }
  
  // 如果没有解析出数据，添加默认行
  if (rows.length === 0) {
    rows.push(
      { category: '总学分要求', credits: '', notes: '' },
      { category: '专业必修课', credits: '', notes: '' },
      { category: '专业选修课', credits: '', notes: '' },
      { category: '公共基础课', credits: '', notes: '' }
    );
  }
  
  creditTable.rows = rows;
};

// 将表格数据转换为文本
const convertTableToText = () => {
  let text = '';
  
  // 遍历表格行
  for (const row of editedCreditTable.rows) {
    if (row.category && row.credits) {
      if (row.category === '总学分要求') {
        text += `总学分要求：${row.credits}学分\n`;
      } else {
        text += `${row.category}：${row.credits}学分\n`;
      }
    }
  }
  
  return text.trim();
};

// 开始编辑学分要求内容
const startEditingCredit = () => {
  // 深拷贝当前表格数据以供编辑
  editedCreditTable.rows = JSON.parse(JSON.stringify(creditTable.rows));
  
  // 如果表格为空，添加默认行
  if (editedCreditTable.rows.length === 0) {
    editedCreditTable.rows = [
      { category: '总学分要求', credits: '', notes: '' },
      { category: '专业必修课', credits: '', notes: '' },
      { category: '专业选修课', credits: '', notes: '' },
      { category: '公共基础课', credits: '', notes: '' }
    ];
  }
  
  isEditingCredit.value = true;
};

// 添加新的学分类别行
const addCreditRow = () => {
  editedCreditTable.rows.push({
    category: '',
    credits: '',
    notes: ''
  });
};

// 删除学分类别行
const removeCreditRow = (index) => {
  // 不允许删除总学分行
  if (index === 0 && editedCreditTable.rows[0].category === '总学分要求') {
    ElMessage.warning('不能删除总学分要求行');
    return;
  }
  
  editedCreditTable.rows.splice(index, 1);
};

// 取消编辑学分要求内容
const cancelEditingCredit = () => {
  isEditingCredit.value = false;
  // 清空编辑状态
  editedCreditTable.rows = [];
};

// 保存学分要求内容
const saveEditedCredit = () => {
  if (!props.majorId) {
    ElMessage.warning('未选择专业，无法保存内容');
    return;
  }
  
  // 将表格转换为文本
  const textContent = convertTableToText();
  if (!textContent) {
    ElMessage.warning('请至少填写总学分要求');
    return;
  }
  
  contentUpdateLoading.value = true;
  
  const data = {
    creditRequirements: textContent,
    mainCourses: props.majorData.mainCourses
  };
  
  updateMajorContent(
    props.majorId,
    data,
    (message, data) => {
      contentUpdateLoading.value = false;
      if (data) {
        ElMessage.success('学分要求内容更新成功');
        // 直接更新父组件的数据
        props.majorData.creditRequirements = textContent;
        // 更新表格显示
        parseTextToTable(textContent);
        isEditingCredit.value = false;
        
        console.log('学分要求更新成功:', textContent);
      } else {
        ElMessage.error(message || '学分要求内容更新失败');
      }
    },
    (error) => {
      contentUpdateLoading.value = false;
      ElMessage.error('学分要求内容更新失败：' + error);
    }
  );
};

// 调试图片URL的方法
const debugImageUrl = (url) => {
  return url;
};

// 打开图片上传模态框
const openUploadModal = () => {
  emit('image-upload', 'CREDIT_REQUIREMENT');
};

// 删除图片
const deleteImage = (imageId) => {
  emit('image-delete', imageId);
};

// 打开图片查看器
const openImageViewer = (imageUrl, description) => {
  emit('image-view', imageUrl, description);
};

// 监听majorData的变化
watch(() => props.majorData.creditRequirements, (newVal) => {
  parseTextToTable(newVal);
}, { immediate: true });

// 添加调试输出
console.log('CreditRequirements - isAdmin属性:', props.isAdmin);
</script>

<template>
  <div class="space-y-4">
    <div class="flex justify-between items-center">
      <h3 class="content-title">学分要求</h3>
      <div class="flex items-center space-x-2">
        <!-- 管理员编辑文本按钮 -->
        <button 
          v-if="props.isAdmin && !isEditingCredit" 
          @click="startEditingCredit" 
          class="admin-btn"
        >
          编辑学分要求
        </button>
        <!-- 管理员上传图片按钮 -->
        <button 
          v-if="props.isAdmin" 
          @click="openUploadModal" 
          class="admin-btn"
        >
          上传学分要求图片
        </button>
      </div>
    </div>
    <div class="content-panel">
      <!-- 表格形式显示学分要求 -->
      <div v-if="creditTable.rows.length > 0 && !isEditingCredit" class="credit-table-container">
        <table class="credit-table">
          <thead>
            <tr>
              <th>类别</th>
              <th>学分</th>
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in creditTable.rows" :key="index">
              <td>{{ row.category }}</td>
              <td>{{ row.credits }} 学分</td>
              <td>{{ row.notes }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 学分要求编辑表格 -->
      <div v-if="isEditingCredit" class="edit-form">
        <div class="edit-table-container">
          <table class="edit-credit-table">
            <thead>
              <tr>
                <th>类别</th>
                <th>学分</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in editedCreditTable.rows" :key="index">
                <td>
                  <input 
                    v-model="row.category" 
                    type="text" 
                    class="table-input" 
                    placeholder="类别名称"
                    :disabled="index === 0 && row.category === '总学分要求'"
                  />
                </td>
                <td>
                  <input 
                    v-model="row.credits" 
                    type="number" 
                    class="table-input" 
                    placeholder="学分数" 
                  />
                </td>
                <td>
                  <input 
                    v-model="row.notes" 
                    type="text" 
                    class="table-input" 
                    placeholder="备注信息" 
                  />
                </td>
                <td>
                  <button 
                    @click="removeCreditRow(index)" 
                    class="table-action-btn text-red-500"
                    :disabled="index === 0 && row.category === '总学分要求'"
                  >
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div class="edit-table-actions">
          <button @click="addCreditRow" class="add-row-btn">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            添加学分类别
          </button>
        </div>
        
        <div class="edit-form-buttons mt-4">
          <button 
            @click="cancelEditingCredit" 
            class="cancel-edit-btn" 
            :disabled="contentUpdateLoading"
          >
            取消
          </button>
          <button 
            @click="saveEditedCredit" 
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
      
      <!-- 显示学分要求图片 -->
      <div v-if="majorData.creditRequirementImages && majorData.creditRequirementImages.length > 0" class="image-gallery mt-4">
        <div v-for="(image, index) in majorData.creditRequirementImages" :key="index" class="image-item">
          <div class="image-wrapper">
            <img 
              :src="debugImageUrl(baseURL + (image.imageUrl.startsWith('/') ? image.imageUrl.substring(1) : image.imageUrl))" 
              :alt="image.description || '学分要求'" 
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
      
      <p v-else-if="!majorData.creditRequirements && !isEditingCredit && creditTable.rows.length === 0" class="placeholder-text">暂无学分要求信息</p>
    </div>
  </div>
</template>

<style scoped>
/* 学分要求表格样式 */
.credit-table-container {
  @apply overflow-x-auto rounded-lg shadow-sm;
  transition: all 0.3s ease;
}

.credit-table {
  @apply w-full text-left border-collapse;
}

.credit-table th {
  @apply bg-blue-50 dark:bg-blue-900 dark:bg-opacity-30 text-gray-700 dark:text-gray-300 font-semibold py-3 px-4 border-b border-gray-200 dark:border-gray-700;
}

.credit-table td {
  @apply py-3 px-4 border-b border-gray-200 dark:border-gray-700 text-gray-800 dark:text-gray-200;
}

.credit-table tr:hover {
  @apply bg-blue-50 dark:bg-blue-900 dark:bg-opacity-20;
}

.credit-table tr:last-child td {
  @apply border-b-0;
}

/* 编辑表格样式 */
.edit-table-container {
  @apply overflow-x-auto border rounded-lg shadow-md;
  border-color: #e2e8f0;
}

.dark .edit-table-container {
  border-color: #334155;
}

.edit-credit-table {
  @apply w-full text-left border-collapse;
}

.edit-credit-table th {
  @apply bg-blue-50 dark:bg-blue-900 dark:bg-opacity-30 text-gray-700 dark:text-gray-300 font-semibold py-3 px-4 border-b border-gray-200 dark:border-gray-700;
}

.edit-credit-table td {
  @apply py-2 px-4 border-b border-gray-200 dark:border-gray-700;
}

.table-input {
  @apply w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all duration-200;
  border-color: #cbd5e1;
  background-color: white;
}

.dark .table-input {
  @apply bg-gray-700 border-gray-600 text-gray-200;
}

.table-input:disabled {
  @apply bg-gray-100 dark:bg-gray-800 cursor-not-allowed;
}

.table-action-btn {
  @apply px-2 py-1 rounded-md text-sm transition-colors duration-200;
}

.table-action-btn:hover:not(:disabled) {
  @apply bg-red-50 dark:bg-red-900 dark:bg-opacity-20;
}

.table-action-btn:disabled {
  @apply opacity-50 cursor-not-allowed;
}

.edit-table-actions {
  @apply mt-4 flex justify-end;
}

.add-row-btn {
  @apply flex items-center px-3 py-1.5 bg-green-500 hover:bg-green-600 text-white rounded-md transition-all duration-200 text-sm font-medium shadow-sm hover:shadow-md transform hover:-translate-y-0.5 active:translate-y-0;
}

/* 编辑表单样式 */
.edit-form {
  @apply bg-white dark:bg-gray-800 rounded-lg p-4 shadow-md;
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
</style> 