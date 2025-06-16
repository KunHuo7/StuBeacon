<script setup>
import { defineProps, defineEmits } from 'vue';
import { useUserStore } from '@/stores/userStore.js';

const userStore = useUserStore();
const props = defineProps({
  selectedTab: {
    type: Number,
    required: true
  },
  unreadCounts: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['tab-change']);

// 侧边栏菜单项
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
    name: "评论",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z" /></svg>`,
    id: 3
  },
  {
    typeId: 3,
    name: "关于",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" /></svg>`,
    id: 4
  },
  // 管理员专用：活动审核菜单项
  {
    typeId: 4,
    name: "活动审核",
    adminOnly: true, // 标记为仅管理员可见
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8">
      <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75 11.25 15 15 9.75M21 12c0 1.268-.63 2.39-1.593 3.068a3.745 3.745 0 0 1-1.043 3.296 3.745 3.745 0 0 1-3.296 1.043A3.745 3.745 0 0 1 12 21c-1.268 0-2.39-.63-3.068-1.593a3.746 3.746 0 0 1-3.296-1.043 3.745 3.745 0 0 1-1.043-3.296A3.745 3.745 0 0 1 3 12c0-1.268.63-2.39 1.593-3.068a3.745 3.745 0 0 1 1.043-3.296 3.746 3.746 0 0 1 3.296-1.043A3.746 3.746 0 0 1 12 3c1.268 0 2.39.63 3.068 1.593a3.746 3.746 0 0 1 3.296 1.043 3.746 3.746 0 0 1 1.043 3.296A3.745 3.745 0 0 1 21 12Z" />
    </svg>`,
    id: 5
  },
];

// 处理菜单项点击
const handleClick = (buttonId) => {
  emit('tab-change', buttonId);
};
</script>

<template>
  <!-- 左侧导航 -->
  <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 sticky top-6 h-fit max-h-[calc(100vh-100px)] transition-all duration-300 hover:shadow-lg">
    <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-6 pb-3 border-b border-gray-200 dark:border-gray-700 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
      </svg>
      个人中心
    </h3>
    <div class="space-y-6">
      <div v-for="sbo in sideBarObjects"
           :key="sbo.id"
           v-if="!sbo.adminOnly || (sbo.adminOnly && userStore.user?.manager === 1)"
           @click="handleClick(sbo.id)"
           :class="[
             'flex items-center py-3 px-4 rounded-lg cursor-pointer transition-all duration-200 group relative overflow-hidden',
             selectedTab === sbo.id
               ? 'bg-gradient-to-r from-blue-50 to-blue-100 dark:from-blue-900/30 dark:to-blue-800/20 text-blue-600 dark:text-blue-400 shadow-sm'
               : 'hover:bg-gray-50 dark:hover:bg-gray-700/50 text-gray-700 dark:text-gray-300'
           ]">
        <div v-html="sbo.svg" class="w-5 h-5 mr-3 transition-transform duration-200 group-hover:scale-110" 
             :class="{'text-blue-500 dark:text-blue-400': selectedTab === sbo.id}">
        </div>
        <span class="font-medium text-sm">
          {{ sbo.name }}
          <span v-if="(sbo.id === 1 && unreadCounts.like > 0) || (sbo.id === 3 && unreadCounts.comment > 0)"
                class="ml-2 px-2 py-0.5 text-xs bg-red-100 dark:bg-red-900/50 text-red-600 dark:text-red-300 rounded-full inline-flex items-center">
            {{ sbo.id === 1 ? unreadCounts.like : unreadCounts.comment }}
          </span>
        </span>
        <div v-if="selectedTab === sbo.id" 
             class="absolute right-0 top-0 h-full w-1 bg-blue-500 dark:bg-blue-400">
        </div>
      </div>
    </div>
  </div>
</template> 