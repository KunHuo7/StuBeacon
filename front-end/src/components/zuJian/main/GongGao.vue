<script setup>
import { ref, onMounted, reactive, watch } from "vue";
import { get } from "@/net/index.js";
import router from "@/router/index.js";
import {useUserStore} from "@/stores/userStore.js";
const userStore = useUserStore();
const visible = ref(false);
const loading = ref(true);
const loadError = ref(false);
const options = reactive({
  blogs: []
});

const initData = () => {
  loading.value = true;
  loadError.value = false;
  
  get('/api/blog/getInroduce', {}, 
    (message, data) => {
      // 确保data是数组类型
      if (Array.isArray(data)) {
    options.blogs = data;
      } else {
        console.error('返回的博客数据格式不正确:', data);
        options.blogs = []; // 设置为空数组
        loadError.value = true;
      }
      loading.value = false;
    },
    (error) => {
      console.error('获取博客列表失败:', error);
      options.blogs = []; // 错误时设置为空数组
      loading.value = false;
      loadError.value = true;
    }
  );
};

onMounted(() => {
  initData();
});

const showAllBlogs = () => {
  visible.value = true;
};

// 关闭弹窗
const closeModal = () => {
  visible.value = false;
};

// 监听ESC键关闭弹窗
watch(visible, (newValue) => {
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
</script>

<template>
  <div class="container mx-auto px-4 py-12 animate-on-scroll" id="news-section">
    <div class=" mb-10  px-4 py-2 rounded-lg flex items-center justify-between relative">
      <div class="text-3xl font-bold gradient-text-alt">
      最新公告
      <div class="h-1 w-24 bg-gradient-to-r from-purple-500 to-pink-500 mt-2 rounded-full"></div>
      </div>

      <div class="flex items-center gap-4">
        <div v-if="userStore.user?.manager === 1"
             @click="router.push('/new')"
             class="px-6 py-2.5 cursor-pointer rounded-lg mx-2 bg-gradient-to-r from-sky-500 to-gray-500
                    dark:from-sky-600 dark:to-gray-600 hover:from-sky-600 hover:to-gray-600
                    dark:hover:from-sky-800 dark:hover:to-gray-800 text-white font-medium active:scale-90
                    shadow-md hover:shadow-lg transform transition-all duration-300">
          发布公告
        </div>
        
        <button
            v-if="options.blogs.length > 3"
            @click="showAllBlogs"
            class="px-4 py-2 hover:underline hover:text-blue-600 font-medium transition-all duration-300 flex items-center gap-2  text-blue-700
                   dark:text-gray-300  dark:hover:text-white">
          <span>查看更多</span>
          <span class="transform group-hover:translate-x-1 transition-transform">></span>
        </button>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="loading" class="py-16 flex justify-center items-center">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="loadError" class="py-16 text-center">
      <div class="text-red-500 text-xl mb-4">
        <i class="fas fa-exclamation-circle mr-2"></i>
        加载公告失败
      </div>
      <button @click="initData" class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-all">
        重新加载
      </button>
    </div>

    <!-- 无数据状态 -->
    <div v-else-if="options.blogs.length === 0" class="py-16 text-center">
      <p class="text-gray-500 dark:text-gray-400">暂无公告</p>
    </div>

    <!-- 正常显示数据 -->
    <div v-else class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <div
        v-for="(blog, index) in options.blogs.slice(0, 3)"
        :key="index"
        class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-md rounded-xl overflow-hidden
        shadow-md hover:shadow-xl transition-all duration-500 transform hover:-translate-y-2
        border border-gray-200 dark:border-gray-700 news-card flex flex-col min-h-[250px]"
        :class="`news-card-${index}`"
        @click="router.push('/view/'+blog.blog.id)"
      >
        <div class="p-6 flex flex-col flex-grow">
          <div class="flex-grow">
            <h3 class="text-xl font-semibold text-gray-800 dark:text-white mb-3">
              {{ blog.blog.title }}
            </h3>
            <p class="text-gray-600 dark:text-gray-300 mb-4 line-clamp-3">
              {{ blog.blog.content }}
            </p>
          </div>
          <div class="flex justify-between items-center mt-auto pt-4 border-t border-gray-200 dark:border-gray-700">
            <span class="text-blue-500 select-none">{{ blog.username }}</span>
            <button
              class="px-4 py-2 rounded-lg bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-medium hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 transform hover:scale-105"
            >
              查看详情
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 自定义弹窗 -->
    <teleport to="body">
      <!-- 更新弹窗遮罩和过渡效果，与 CustomConfirmDialog 保持一致 -->
      <transition 
        enter-active-class="transition ease-out duration-300"
        enter-from-class="opacity-0 scale-95"
        enter-to-class="opacity-100 scale-100"
        leave-active-class="transition ease-in duration-200"
        leave-from-class="opacity-100 scale-100"
        leave-to-class="opacity-0 scale-95"
      >
        <div v-if="visible" 
             @click="closeModal"
             class="fixed inset-0 bg-black/40 backdrop-blur-sm z-50 flex items-center justify-center transition-opacity duration-300">
          
          <!-- 弹窗容器样式与 CustomConfirmDialog 保持一致的视觉风格 -->
          <div @click="stopPropagation"
               class="bg-white dark:bg-gray-800 rounded-2xl shadow-2xl 
                      border border-gray-100 dark:border-gray-700 
                      transform transition-all duration-300 
                      w-[800px] max-w-[95vw] max-h-[90vh] overflow-hidden">
            
            <!-- 弹窗头部 -->
            <div class="px-6 py-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center
                        bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400">
              <h3 class="text-lg font-bold flex items-center">
                <span class="mr-2">
                  <i class="fas fa-circle-info"></i>
                </span>
                公告列表
              </h3>
              <button @click="closeModal" 
                      class="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 
                             w-8 h-8 flex items-center justify-center rounded-full 
                             hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors">
                <i class="fas fa-xmark"></i>
              </button>
            </div>
            
            <!-- 弹窗内容 -->
            <div class="p-4">
              <div class="max-h-[calc(90vh-120px)] overflow-y-auto pr-2 custom-scrollbar">
                <div
                  v-for="blog in options.blogs"
                  :key="blog.blog.id"
                  class="p-5 mb-3 border border-gray-200 dark:border-gray-700 hover:bg-white dark:hover:bg-gray-700
                  transition-all duration-300 cursor-pointer bg-gray-50 dark:bg-gray-800/90 rounded-xl
                  shadow-sm hover:shadow-md transform hover:scale-[1.01]"
                  @click="router.push('/view/'+blog.blog.id)"
                >
                  <h3 class="text-lg font-semibold text-gray-800 dark:text-gray-100 mb-2">
                    {{ blog.blog.title }}
                  </h3>
                  <p class="text-gray-600 dark:text-gray-300 mb-2 line-clamp-2">
                    {{ blog.blog.content }}
                  </p>
                  <span class="text-blue-500 dark:text-blue-400 font-medium">{{ blog.username }}</span>
                </div>
                <div class="text-center py-4 text-gray-500 dark:text-gray-400 text-sm font-medium animate-pulse hover:text-gray-600 dark:hover:text-gray-300 transition-colors duration-300">
                  -已经到底了-
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </teleport>
  </div>
</template>

<style scoped>
.gradient-text-alt {
  background-image: linear-gradient(45deg, #3b82f6, #8b5cf6);
  -webkit-background-clip: text;
  color: transparent;
}

/* 卡片动画效果 */
.news-card {
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
}

.news-card-0 { animation-delay: 0.2s; }
.news-card-1 { animation-delay: 0.4s; }
.news-card-2 { animation-delay: 0.6s; }

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动动画 */
.animate-on-scroll {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease, transform 0.8s ease;
}

#news-section.animate-on-scroll {
  transition-delay: 0.4s;
}

/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
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