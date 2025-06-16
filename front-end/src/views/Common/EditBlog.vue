<script setup>
import {reactive, ref, onMounted} from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {useThemeStore} from "@/stores/theme.js";
import {post} from "@/net/index.js";
import {message} from "ant-design-vue";
import {useUserStore} from "@/stores/userStore.js";
import {useRouter, useRoute} from "vue-router";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const [messageApi, contextHolder] = message.useMessage();
const themeStore = useThemeStore();
const blogForm = reactive({
  title:'',
  content:'',
  id: null,
  isPublic: 1 // 默认为公开，1-公开 0-私密
})

// 检查用户权限
const checkPermission = () => {
  if (!userStore.user) {
    messageApi.error('请先登录');
    router.push('/login');
    return;
  }
};

// 在组件挂载时检查权限
onMounted(() => {
  checkPermission();
  
  // 如果有query参数，说明是编辑模式
  if (route.query.id) {
    blogForm.id = route.query.id;
    blogForm.title = route.query.title || '';
    blogForm.content = route.query.content || '';
    // 确保isPublic是数字类型
    blogForm.isPublic = route.query.isPublic !== undefined ? 
      (typeof route.query.isPublic === 'string' ? Number(route.query.isPublic) : route.query.isPublic) : 1;
    
    console.log("编辑模式初始化 isPublic:", route.query.isPublic, "转换后:", blogForm.isPublic, "类型:", typeof blogForm.isPublic);
  }
});

// 工具栏配置 - 排除不需要的工具
const excludedTools = ['github', 'mermaid', 'katex'];

const submitHandler = () => {
  // 再次检查权限
  if (!userStore.user) {
    messageApi.error('请先登录');
    return;
  }
  
  // 表单验证
  if(!blogForm.title.trim()) {
    messageApi.warning('请输入标题');
    return;
  }
  
  // 确保isPublic是数字类型
  const isPublicValue = Number(blogForm.isPublic);
  console.log("提交前isPublic:", blogForm.isPublic, "转换后:", isPublicValue);
  
  // 根据是否有ID判断是新增还是修改
  if(blogForm.id) {
    // 编辑现有文章
    post('/api/blog/update', {
      id: blogForm.id,
      title: blogForm.title,
      content: blogForm.content,
      isPublic: isPublicValue
    }, (message) => {
      messageApi.success(message);
      // 跳转到查看页
      router.push('/view/' + blogForm.id);
    }, (message) => {
      messageApi.warning(message);
    }, (message) => {
      messageApi.error(message);
    });
  } else {
    // 发布新文章
    post('/api/blog/addNew', {
      title: blogForm.title,
      content: blogForm.content,
      isPublic: isPublicValue
    }, (message) => {
      messageApi.success(message);
      // 清空表单
      blogForm.title = '';
      blogForm.content = '';
      blogForm.isPublic = 1;
      // 可以选择跳转到首页
      router.push('/');
    }, (message) => {
      messageApi.warning(message);
    }, (message) => {
      messageApi.error(message);
    });
  }
}

</script>

<template>
  <div class="container mx-auto max-w-5xl p-5">
    <div class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-md">
      <h1 class="text-2xl font-bold mb-6 text-gray-800 dark:text-gray-100">
        {{ blogForm.id ? '编辑文章' : '发布新文章' }}
      </h1>
      
      <div class="mb-5">
        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">标题</label>
        <input 
          v-model="blogForm.title" 
          type="text" 
          class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white"
          placeholder="请输入文章标题"
        >
      </div>
      
      <div class="mb-5">
        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">内容</label>
        <MdEditor
          v-model="blogForm.content"
          :theme="themeStore.isDark ? 'dark' : 'light'"
          :excludeToolbars="excludedTools"
          class="rounded-lg overflow-hidden"
          previewTheme="github"
        />
      </div>
      
      <!-- 新增公开/私密设置 -->
      <div class="mb-5">
        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">文章设置</label>
        <div class="flex items-center space-x-2">
          <a-switch
            :checked="Number(blogForm.isPublic) === 1"
            @change="(checked) => { blogForm.isPublic = checked ? 1 : 0; console.log('切换后isPublic:', blogForm.isPublic); }"
          />
          <span class="text-gray-600 dark:text-gray-300">{{ Number(blogForm.isPublic) === 1 ? '公开' : '私密' }}</span>
          <span class="text-xs text-gray-500 dark:text-gray-400 ml-2">
            {{ Number(blogForm.isPublic) === 1 ? '所有用户可见' : '仅管理员和您可见' }}
          </span>
        </div>
      </div>
      
      <div class="flex justify-end gap-4 mt-6">
        <button 
          @click="router.back()" 
          class="px-5 py-2 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-300 dark:focus:ring-gray-600 transition-colors duration-300"
        >
          取消
        </button>
        <button 
          @click="submitHandler" 
          class="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-800 transition-colors duration-300"
        >
          {{ blogForm.id ? '更新' : '发布' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 确保编辑器的高度足够 */
:deep(.md-editor) {
  min-height: 400px;
}

/* 主题过渡动画 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}
</style>