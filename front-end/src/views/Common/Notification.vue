<script setup>
import { onMounted, reactive } from "vue";
import { get, post } from "@/net/index.js";
import { formatDate } from "@/util/DateFormattor.js";
import { message } from "ant-design-vue";
import { useUserStore } from '@/stores/userStore.js';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();
const [messageApi, contextHolder] = message.useMessage();
const clearNotification = (type) => {
  post('/api/user/clearNotifications', {
    type: type
  }, (message) => {
    messageApi.success(message);
    fetchNotificationsByType(type);
  });
};

const readNotification = (id) => {
  post('/api/user/readNotification', {
    id: id
  }, (message) => {
    messageApi.success(message);
  });
};
/*生命周期*/
onMounted(() => {
  fetchNotificationsByType(0)
});

/* 对数组进行操作 */
const options = reactive({
  selected: {
    notificationType: 2
  },
  currentNotifications: []
});

const buttonClickHandler = (buttonId) => {
  options.selected.notificationType = buttonId;
  fetchNotificationsByType(buttonId - 1);
};

const fetchNotificationsByType = (type) => {
  get('/api/user/getNotifications', {
        type: type
      },
      (message, data) => {
        options.currentNotifications = data;
      });
};

const sideBarObjects = [
  {
    typeId: 1,
    name: "我的",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
</svg>
`,
    id: 2
  },
  {
    typeId: 0,
    name: "点赞",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M6.633 10.25c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75a.75.75 0 0 1 .75-.75 2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282m0 0h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904m10.598-9.75H14.25M5.904 18.5c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 9.953 4.167 9.5 5 9.5h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z" /></svg>`,
    id: 1
  },
  {
    typeId: 2,
    name: "关注",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M18 18.72a9.094 9.094 0 0 0 3.741-.479 3 3 0 0 0-4.682-2.72m.94 3.198.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0 1 12 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 0 1 6 18.719m12 0a5.971 5.971 0 0 0-.941-3.197m0 0A5.995 5.995 0 0 0 12 12.75a5.995 5.995 0 0 0-5.058 2.772m0 0a3 3 0 0 0-4.681 2.72 8.986 8.986 0 0 0 3.74.477m.94-3.197a5.971 5.971 0 0 0-.94 3.197M15 6.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Zm6 3a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Zm-13.5 0a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Z" /></svg>`,
    id: 3
  },
  {
    typeId: 3,
    name: "评论",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z" /></svg>`,
    id: 4
  },

];

// 提取话题ID的函数
const extractTalkId = (content) => {
  if (!content) return null;
  
  // 从通知内容中提取话题ID
  // 假设通知内容格式为："您的话题: \"标题\" 刚刚被用户uid:123点赞了" 或 "您的话题: \"标题\" 收到了新评论"
  // 需要从数据库获取话题ID，这里暂时提取第一个数字作为示例
  
  const match = content.match(/话题.*?\"(.*?)\".*?(收到|点赞)/);
  if (match && match[1]) {
    // 使用标题查询ID - 实际项目中应该有更好的方式
    return match[1];
  }
  return null;
};

// 跳转到话题详情页
const goToTalkDetail = (title) => {
  if (!title) return;
  
  // 查询话题ID
  get('/api/talk/searchByTitle', { title: title }, (message, data) => {
    if (data && data.id) {
      router.push(`/talk/detail/${data.id}`);
    } else {
      messageApi.warning('找不到对应的话题');
    }
  }, (error) => {
    messageApi.error('跳转失败: ' + error);
  });
};
</script>
<template>
  <contextHolder />
  <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800 p-6">
    <div class="max-w-7xl mx-auto grid grid-cols-[280px,1fr] gap-6">
      <!-- Enhanced Sidebar -->
      <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-lg backdrop-blur-sm backdrop-filter p-5 h-fit sticky top-6">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600 dark:from-blue-400 dark:to-indigo-400">
            消息中心
          </h2>
          <button @click="clearNotification(options.selected.notificationType - 1)"
                  class="p-2 rounded-xl hover:bg-gray-100 dark:hover:bg-gray-700 transition-all duration-300"
                  title="一键清空">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>

        <div class="space-y-2">
          <div v-for="sbo in sideBarObjects"
               :key="sbo.id"
               @click="buttonClickHandler(sbo.id)"
               :class="[
                 'flex items-center p-3 rounded-xl cursor-pointer transition-all duration-300',
                 options.selected.notificationType === sbo.id
                   ? 'bg-blue-50 dark:bg-gray-700 shadow-sm'
                   : 'hover:bg-gray-50 dark:hover:bg-gray-700'
               ]">
            <div v-html="sbo.svg"
                 :class="[
                   'w-8 h-8',
                   options.selected.notificationType === sbo.id
                     ? 'text-blue-600 dark:text-blue-400'
                     : 'text-gray-600 dark:text-gray-400'
                 ]"></div>
            <span :class="[
                    'ml-3 font-medium',
                    options.selected.notificationType === sbo.id
                      ? 'text-blue-600 dark:text-blue-400'
                      : 'text-gray-700 dark:text-gray-300'
                  ]">{{ sbo.name }}</span>
          </div>
        </div>
      </div>

      <!-- 主内容 -->
      <div class="space-y-4">
        <!-- 我的信息 -->
        <div v-if="options.selected.notificationType === 2" class="space-y-4">
          <div class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-sm hover:shadow-md transition-all duration-300">
            <div class="flex items-center space-x-4 mb-6">
              <img :src="userStore.user?.avatar ? `http://localhost:8080/${userStore.user.avatar}` : 'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="用户头像" class="w-20 h-20 rounded-full border-4 border-blue-100 dark:border-blue-900">
              <div>
                <h3 class="text-xl font-bold text-gray-900 dark:text-white">{{ userStore.user?.username }}</h3>
                <p class="text-gray-500 dark:text-gray-400">{{ userStore.user?.email }}</p>
              </div>
            </div>
            
            <div class="grid grid-cols-3 gap-4 mb-6">
              <div class="bg-blue-50 dark:bg-gray-700 p-4 rounded-lg text-center">
                <h4 class="text-lg font-semibold text-blue-600 dark:text-blue-400">{{ userStore.user?.follows || 2 }}</h4>
                <p class="text-sm text-gray-600 dark:text-gray-300">关注</p>
              </div>
              <div class="bg-blue-50 dark:bg-gray-700 p-4 rounded-lg text-center">
                <h4 class="text-lg font-semibold text-blue-600 dark:text-blue-400">{{ userStore.user?.fans || 2 }}</h4>
                <p class="text-sm text-gray-600 dark:text-gray-300">粉丝</p>
              </div>
              <div class="bg-blue-50 dark:bg-gray-700 p-4 rounded-lg text-center">
                <h4 class="text-lg font-semibold text-blue-600 dark:text-blue-400">{{ userStore.user?.articles || 4 }}</h4>
                <p class="text-sm text-gray-600 dark:text-gray-300">文章</p>
              </div>
            </div>

            <div class="border-t dark:border-gray-700 pt-6">
              <h4 class="text-lg font-semibold text-gray-900 dark:text-white mb-4">最近动态</h4>
              <div class="space-y-4">
                <div class="flex items-center justify-between p-4 bg-gray-50 dark:bg-gray-700 rounded-lg">
                  <div class="flex items-center space-x-3">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                    </svg>
                    <span class="text-gray-700 dark:text-gray-300">发布了文章</span>
                  </div>
                  <span class="text-sm text-gray-500 dark:text-gray-400">两个月前</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 点赞通知 -->
        <div v-if="options.selected.notificationType === 1" class="space-y-4">
          <div v-for="noti in options.currentNotifications"
               :key="noti.id"
               class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-sm hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-3">
              <div class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-red-100 dark:bg-red-900 rounded-full flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-lg font-semibold text-gray-900 dark:text-white">{{ noti.title }}</h3>
                  <span class="text-sm text-gray-500 dark:text-gray-400">{{ formatDate(noti.createTime) }}</span>
                </div>
              </div>
              <div v-if="!noti.isRead"
                   @click="readNotification(noti.id); noti.isRead = true"
                   class="inline-flex items-center text-sm text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 cursor-pointer transition-colors duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                标记已读
              </div>
            </div>
            <p class="text-gray-600 dark:text-gray-300 mt-2">{{ noti.content }}</p>
            
            <!-- 添加查看详情按钮 -->
            <div class="mt-4 flex justify-end">
              <button 
                @click="goToTalkDetail(extractTalkId(noti.content))"
                class="px-3 py-1 bg-blue-100 dark:bg-blue-900 text-blue-600 dark:text-blue-300 hover:bg-blue-200 dark:hover:bg-blue-800 rounded-md text-sm flex items-center transition-colors duration-300"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                查看详情
              </button>
            </div>
          </div>
        </div>

        <!-- 关注通知 -->
        <div v-if="options.selected.notificationType === 3" class="space-y-4">
          <div v-for="(noti, index) in ['用户mfy关注了你', '用户admin开始关注你了']"
               :key="index"
               class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-sm hover:shadow-md transition-all duration-300">
            <div class="flex items-center space-x-4">
              <div class="flex-shrink-0">
                <div class="w-12 h-12 bg-blue-100 dark:bg-blue-900 rounded-full flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <h3 class="text-lg font-semibold text-gray-900 dark:text-white">新的关注</h3>
                <p class="text-gray-600 dark:text-gray-300">{{ noti }}</p>
                <span class="text-sm text-gray-500 dark:text-gray-400 mt-2 block">三个月前</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 评论通知 -->
        <div v-if="options.selected.notificationType === 4" class="space-y-4">
          <div v-for="noti in options.currentNotifications"
               :key="noti.id"
               class="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-sm hover:shadow-md transition-all duration-300">
            <div class="flex items-center justify-between mb-3">
              <div class="flex items-center space-x-3">
                <div class="w-10 h-10 bg-green-100 dark:bg-green-900 rounded-full flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-lg font-semibold text-gray-900 dark:text-white">{{ noti.title }}</h3>
                  <span class="text-sm text-gray-500 dark:text-gray-400">{{ formatDate(noti.createTime) }}</span>
                </div>
              </div>
              <div v-if="!noti.isRead"
                   @click="readNotification(noti.id); noti.isRead = true"
                   class="inline-flex items-center text-sm text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 cursor-pointer transition-colors duration-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                标记已读
              </div>
            </div>
            <p class="text-gray-600 dark:text-gray-300 mt-2">{{ noti.content }}</p>
            
            <!-- 添加查看详情按钮 -->
            <div class="mt-4 flex justify-end">
              <button 
                @click="goToTalkDetail(extractTalkId(noti.content))"
                class="px-3 py-1 bg-green-100 dark:bg-green-900 text-green-600 dark:text-green-300 hover:bg-green-200 dark:hover:bg-green-800 rounded-md text-sm flex items-center transition-colors duration-300"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                </svg>
                查看评论
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Smooth scrolling */
html {
  scroll-behavior: smooth;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Dark mode scrollbar */
.dark ::-webkit-scrollbar-thumb {
  background: #475569;
}

.dark ::-webkit-scrollbar-thumb:hover {
  background: #64748b;
}
</style>
