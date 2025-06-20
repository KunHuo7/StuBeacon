<script setup>
import {onMounted, reactive, ref, computed} from 'vue';
import {useUserStore} from '@/stores/userStore.js';
import router from '@/router/index.js';
import {get} from "@/net/index.js";
const userStore = useUserStore();




onMounted(() => {
  initPageData();
  updateGreeting();
  fetchUserFavouritesCount();
  setInterval(updateGreeting, 60000);

  // 每分钟自动刷新一次数据，确保显示最新状态
  setInterval(refreshData, 60000);
});



const greetingMessage = ref('');

const updateGreeting = () => {
  const hour = new Date().getHours();
  if (hour < 12 && hour >= 6) {
    greetingMessage.value = '早上好';
  } else if (hour >= 12 && hour < 18) {
    greetingMessage.value = '下午好';
  } else if (hour >= 18 && hour < 22) {
    greetingMessage.value = '晚上好';
  } else {
    greetingMessage.value = '夜深了,早点休息';
  }
};

/* 选择显示的文章 */
reactive({
  selected: {
    notificationType: 1
  },
  currentNotifications: []
});

const searchQuery = ref('');

// 计算用户所有博客的总点赞量
const getTotalLikes = () => {
  if (!userStore.user || !userStore.user.username) {
    return 0;
  }

  // 筛选当前用户的博客，并累加它们的点赞数
  return options.allRawBlogs
      .filter(blog => blog.username === userStore.user.username)
      .reduce((total, blog) => total + (blog.blog.likes || 0), 0);
};

// 计算用户所有博客的总收藏量
const getTotalFavourites = () => {
  if (!userStore.user || !userStore.user.username) {
    return 0;
  }

  // 筛选当前用户的博客，并累加它们的收藏数
  return options.allRawBlogs
      .filter(blog => blog.username === userStore.user.username)
      .reduce((total, blog) => total + (blog.blog.favourites || 0), 0);
};

const filteredBlogs = computed(() => {
  let blogsToDisplay = options.blogs;

  if (searchQuery.value.trim() !== '') {
    const searchTerm = searchQuery.value.trim().toLowerCase();
    return blogsToDisplay.filter(blogEntry =>
        blogEntry.blog.title.toLowerCase().includes(searchTerm)
    );
  }
  return blogsToDisplay;
});

// 获取用户收藏的文章总数
const userFavouritesCount = ref(0);

// 获取用户收藏的文章数量
const fetchUserFavouritesCount = () => {
  if (!userStore.user || !userStore.user.id) return;

  get('/api/user/getUserFavouritesCount', { userId: userStore.user.id }, (msg, data) => {
    userFavouritesCount.value = data || 0;
  });
};

// 格式化互动数据显示
const formatInteractionCount = (count) => {
  if (count === undefined || count === null) return 0;
  return count;
};

// 安全获取数字，处理undefined、null等情况
const safeGetNumber = (value) => {
  if (value === undefined || value === null) return 0;
  const num = Number(value);
  return isNaN(num) ? 0 : num;
};

// 格式化大数字显示
const formatBigNumber = (num) => {
  num = safeGetNumber(num);
  if (num < 1000) return num.toString();
  if (num < 10000) return (num / 1000).toFixed(1) + 'K';
  return (num / 10000).toFixed(1) + 'W';
};
</script>

<template>
    <div>
      <div class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-md hover:shadow-lg transition-all duration-300 w-full border border-gray-100 dark:border-gray-700">
        <div class="flex flex-col mb-5">
          <!-- 头像与问候语 -->
          <div class="flex items-center gap-4 mb-4">
            <div class="relative">
              <img
                  class="w-16 h-16 rounded-xl object-cover border-2 border-blue-100 dark:border-blue-900 shadow-sm"
                  :src="userStore.user?.avatar || getUserAvatar(userStore.user?.username)"
                  alt="用户头像"
              />
              <div class="absolute -bottom-1 -right-1 h-5 w-5 bg-green-400 rounded-full border-2 border-white dark:border-gray-800"></div>
            </div>
            <div class="flex-1">
              <div class="flex items-center">
                <span class="text-lg font-medium text-gray-700 dark:text-gray-200">{{ greetingMessage }}</span>
                <span class="ml-1 text-lg">👋</span>
              </div>
              <h2 class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 via-purple-500 to-pink-500 dark:from-blue-400 dark:via-purple-400 dark:to-pink-400">
                {{ userStore.user?.username }}
              </h2>
            </div>
          </div>

          <!-- 用户统计信息 -->
          <div class="grid grid-cols-3 gap-3 mt-2">
            <div class=" dark:bg-gray-700 p-3 rounded-lg text-center border border-blue-100 dark:border-gray-600">
              <div class="text-lg font-bold text-gray-700 dark:text-gray-100">{{ options.myBlogsCount }}</div>
              <div class="text-xs font-medium text-gray-500 dark:text-gray-400">文章</div>
            </div>

            <div class="bg-blue-150 dark:bg-gray-700 p-3 rounded-lg text-center border border-blue-100 dark:border-gray-600">
              <div class="text-lg font-bold text-gray-700 dark:text-gray-100">{{ getTotalLikes() }}</div>
              <div class="text-xs font-medium text-gray-500 dark:text-gray-400">点赞</div>
            </div>

            <div class="bg-blue-150 dark:bg-gray-700 p-3 rounded-lg text-center border border-blue-100 dark:border-gray-600">
              <div class="text-lg font-bold text-gray-700 dark:text-gray-100">{{ userFavouritesCount }}</div>
              <div class="text-xs font-medium text-gray-500 dark:text-gray-400">收藏</div>
            </div>
          </div>
        </div>

        <!-- 创建新文章按钮 -->
        <button
            @click="router.push('/new')"
            class="w-full flex items-center justify-center py-2.5 px-4 rounded-lg bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white font-medium shadow-sm hover:shadow transition-all duration-300 mt-2">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          创建新文章
        </button>
      </div>
    </div>
</template>

<style scoped>

</style>