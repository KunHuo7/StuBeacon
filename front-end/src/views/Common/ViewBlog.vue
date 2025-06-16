<script setup>
import {onBeforeMount, onMounted, reactive, ref} from 'vue';
import { MdPreview, MdCatalog } from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import {useThemeStore} from "@/stores/theme.js";
import {useRoute, useRouter} from "vue-router";
import {get} from "@/net/index.js";
import {message} from "ant-design-vue";
const [messageApi, contextHolder] = message.useMessage();
import {formatDate} from "@/util/DateFormattor.js";
import {post} from "@/net/index.js";
import {useUserStore} from "@/stores/userStore.js";
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const pid = route.params.id
const themeStore = useThemeStore()
const id = 'preview-only';

const options = reactive({
  data: {
    blog:{
      content:'123'
    }
  }
})

// 添加删除确认对话框的状态
const deleteDialogVisible = ref(false);

const fetchBlogContent = () => {
  get('api/blog/getBlog',
      {id:pid},
      (message,data) => {
        options.data = data
        console.log("获取到的博客数据:", options.data)
        console.log("isPublic类型:", typeof options.data.blog.isPublic, "值:", options.data.blog.isPublic)
      })
}

onBeforeMount(() => {
  fetchBlogContent();
})

const scrollElement = document.documentElement;

const likeHandler = () => {
  if (options.data.isLiked === true) {
    post('/api/blog/unlike', {
      id: pid
    }, (message) => {
      options.data.blog.likes--;
      options.data.isLiked = false;
      messageApi.warning(message);
    }, (message) => {
      messageApi.warning(message);
    })
  } else {
    post('/api/blog/like', {
      id: pid
    }, (message) => {
      options.data.blog.likes++;
      options.data.isLiked = true;
      messageApi.success(message);
    }, (message) => {
      messageApi.warning(message);
    })
  }
}

// 检查当前用户是否是文章作者
const isAuthor = () => {
  return userStore.user && options.data.blog && userStore.user.id === options.data.blog.authorId;
}

// 编辑博客处理函数
const editHandler = () => {
  if (!isAuthor()) {
    messageApi.warning('您没有权限编辑此博客');
    return;
  }
  
  // 确保获取正确的isPublic值
  const currentIsPublic = options.data.blog.isPublic;
  console.log("编辑前isPublic值:", currentIsPublic, "类型:", typeof currentIsPublic);
  
  router.push({
    path: '/edit-blog',
    query: { 
      id: pid,
      title: options.data.blog.title,
      content: options.data.blog.content,
      isPublic: currentIsPublic
    }
  });
}

// 显示删除确认对话框
const deleteHandler = () => {
  if (!isAuthor()) {
    messageApi.warning('您没有权限删除此博客');
    return;
  }
  
  deleteDialogVisible.value = true;
};

// 确认删除操作
const confirmDelete = () => {
  post('/api/blog/delete', {
    id: pid
  }, (message) => {
    messageApi.success(message);
    router.push('/');
  }, (message) => {
    messageApi.warning(message);
  });
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 py-6 px-4 sm:px-4 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- Blog Header -->
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-4 mb-8 transition-all duration-300">
        <h1 class="text-4xl font-bold text-gray-900 dark:text-white mb-4 text-center transition-colors duration-300">
          {{ options.data.blog.title }}
        </h1>

        <div class="flex items-center justify-between border-b border-gray-200 dark:border-gray-700 pb-4">
          <div class="flex items-center space-x-4">
            <span class="text-lg font-medium text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 cursor-pointer transition-colors duration-200">
              {{ options.data.username }}
            </span>
            <div class="text-sm text-gray-500 dark:text-gray-400">
              最后修改日期:{{ formatDate(options.data.blog.updateDate) }}
            </div>
          </div>

          <div class="flex items-center space-x-3">
            <!-- 作者操作按钮 -->
            <div v-if="isAuthor()" class="flex space-x-2">
              <button 
                @click="editHandler" 
                class="px-3 py-1.5 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition-colors duration-300 flex items-center text-sm"
              >
                <i class="ri-edit-line mr-1"></i> 编辑
              </button>
              <button 
                @click="deleteHandler" 
                class="px-3 py-1.5 bg-red-500 hover:bg-red-600 text-white rounded-lg transition-colors duration-300 flex items-center text-sm"
              >
                <i class="ri-delete-bin-line mr-1"></i> 删除
              </button>
            </div>

            <div class="flex items-center px-4 py-2 rounded-full"
                 :class="[options.data.blog.isPublic === 1 || options.data.blog.isPublic === '1' ? 'bg-green-100 dark:bg-green-900' : 'bg-red-100 dark:bg-red-900']">
              <span :class="[options.data.blog.isPublic === 1 || options.data.blog.isPublic === '1' ? 'text-green-700 dark:text-green-300' : 'text-red-700 dark:text-red-300']"
                    class="font-medium flex items-center">
                {{ (options.data.blog.isPublic === 1 || options.data.blog.isPublic === '1') ? '公开' : '私密' }}
                <svg v-if="options.data.blog.isPublic === 1 || options.data.blog.isPublic === '1'" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.5 10.5V6.75a4.5 4.5 0 1 1 9 0v3.75M3.75 21.75h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H3.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z" />
                </svg>
              </span>
            </div>
          </div>
        </div>

        <div class="flex items-center space-x-6 mt-4">
          <button @click="likeHandler"
                  class="flex items-center space-x-2 px-4 py-2 rounded-lg transition-all duration-200 hover:bg-gray-100 dark:hover:bg-gray-700"
                  :class="{'text-red-500': options.data.isLiked}">
            <svg xmlns="http://www.w3.org/2000/svg" :fill="options.data.isLiked ? 'currentColor' : 'none'"
                 viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z" />
            </svg>
            <span class="font-medium">{{ options.data.blog.likes }}</span>
          </button>

          <div class="flex items-center space-x-2 px-4 py-2 rounded-lg text-gray-600 dark:text-gray-300">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
            </svg>
            <span class="font-medium">{{ options.data.blog.watches }}</span>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-[1fr,3fr] gap-6">
        <!-- Sidebar -->
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-4 h-fit sticky top-[80px] transition-all duration-300 max-w-[400px]">
          <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-4">目录</h2>
          <MdCatalog
              :theme="themeStore.currentTheme"
              :editorId="id"
              :scrollElement="scrollElement"
              class="text-gray-600 dark:text-gray-300" />
        </div>

        <!-- Main Content -->
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-4 transition-all duration-300">
          <MdPreview
              :theme="themeStore.currentTheme"
              :editorId="id"
              :modelValue="options.data.blog.content"
              class="prose dark:prose-invert max-w-none" />
        </div>
      </div>
    </div>
  </div>

  <!-- 删除确认对话框 -->
  <CustomConfirmDialog
    v-model:visible="deleteDialogVisible"
    title="确认删除"
    content="确定要删除这篇博客吗？此操作不可撤销。"
    type="danger"
    confirm-text="确认删除"
    cancel-text="取消"
    @confirm="confirmDelete"
  />
</template>

<style scoped>
.prose {
  @apply max-w-none;
}

/* 自定义滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.dark ::-webkit-scrollbar-thumb {
  background: #475569;
}

/* 平滑过渡效果 */
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

/* 响应式布局调整 */
@media (max-width: 1024px) {
  .container {
    @apply px-2;
  }
  
  main {
    @apply max-w-full;
  }
}
</style>