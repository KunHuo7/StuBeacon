<script setup>
import studentlogo from '@/assets/images/studentlogo.png'
import ThemeSwitch from "@/components/ThemeSwitch.vue";
import schoolLogo from '@/assets/images/校徽.png'
import router from "@/router/index.js";
import { onMounted, ref, onUnmounted } from "vue";
import { get } from "@/net/index.js";
import Tooltip from "@/components/tooltip/Tooltip.vue";
import Student from "@/components/tooltip/Student.vue";
import {useUserStore} from "@/stores/userStore.js";
import {useThemeStore} from "@/stores/theme.js";

import { useRouter } from 'vue-router'
import {baseURL} from "@/main.js";
const userStore = useUserStore();
const themeStore = useThemeStore();

// 待审核用户数量
const pendingUsersCount = ref(0);

// Menu data structure
const menuItems = ref([
  {
    title: '关于我们',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '学校简介', path: '/about/school-profile' },
      { name: '学校地图', path: '/about/school-map' },
      { name: '入学指南', path: '/about/admission-guide' },
    ]
  },
  {
    title: '入学须知',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '军训', path: '/admission/military-training' },
      { name: '学习', path: '/admission/study-guide' },
      { name: '奖励办法', path: '/admission/reward-policy' }
    ]
  },
  {
    title: '日常生活',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '食堂', path: '/daily/canteen' },
      { name: '医务室', path: '/daily/infirmary' },
      { name: '营业厅', path: '/daily/service-hall' }
    ]
  },
  {
    title: '课程规划',
    type: 'courses',
    path: '/courses'
  },
  // {
  //   title: '用户管理',
  //   type: 'manager',
  //   path: '/manager'
  // },

])


const isHidden = ref(false)
let lastScrollY = 0


const handleScroll = () => {
  const currentScrollY = window.scrollY
  isHidden.value = currentScrollY > lastScrollY && currentScrollY > 100
  
  // 当页面下滑时，关闭所有打开的下拉菜单
  if (currentScrollY > lastScrollY && currentScrollY > 100) {
    menuItems.value = menuItems.value.map(item => ({
      ...item,
      isOpen: false
    }))
  }
  
  lastScrollY = currentScrollY
}

const toggleDropdown = (index) => {
  menuItems.value = menuItems.value.map((item, i) => ({
    ...item,
    isOpen: i === index ? !item.isOpen : false
  }))
}

const navigateTo = (path) => {
  if (path.startsWith('http')) {
    window.location.href = path
  } else {
    router.push(path)
  }
}

const handleClickOutside = (event) => {
  const dropdowns = document.querySelectorAll('.dropdown-container')
  let clickedOutside = true
  dropdowns.forEach((dropdown) => {
    if (dropdown.contains(event.target)) {
      clickedOutside = false
    }
  })
  if (clickedOutside) {
    menuItems.value = menuItems.value.map(item => ({
      ...item,
      isOpen: false
    }))
  }
}

// 返回顶部功能
const isVisible = ref(false)

// 滚动到顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth', // 平滑滚动
  })
}

// 监听页面滚动
const handleBackTopScroll = () => {
  // 如果页面滚动超过100px，显示按钮
  isVisible.value = window.scrollY > 100
}

// 获取待审核用户数量
const loadPendingUsersCount = () => {
  // 检查是否是管理员
  if (userStore.user && userStore.user.manager === 1) {
    get('api/user/admin/pending', {}, (message, data) => {
      pendingUsersCount.value = data ? data.length : 0;
    });
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
  initUserData()
  window.addEventListener('scroll', handleBackTopScroll)
  // 加载待审核用户数量
  loadPendingUsersCount()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
  // 移除返回顶部的滚动监听
  window.removeEventListener('scroll', handleBackTopScroll)
})

// Fetch user data
const initUserData = () => {
  get('api/user/myinfo', {}, (message, data) => {
    useUserStore().login(data)
    // 用户数据加载后再获取待审核用户数量（确保获取到了manager）
    loadPendingUsersCount()
  })
}

// 获取当前路径是否为活跃路径
const isActivePath = (path) => {
  return router.currentRoute.value.path === path
}

// 获取当前路径是否在子项中
const isActiveInSubItems = (subItems) => {
  const currentPath = router.currentRoute.value.path
  return subItems.some(item => item.path === currentPath)
}

</script>

<template>
  <header class="basic-color fixed top-0 left-0 right-0 shadow-md z-50 dark:shadow-blue-950">
    <div>
    <div class="container mx-auto px-4 py-2 flex justify-between items-center">
      <!-- Left: Website logo -->
      <div class="flex items-center flex-none rounded-xl">
        <div class="mx-3">
          <button
              @click="router.push('/')"
              class="flex items-center justify-center px-4 py-2 text-gray-400 rounded-lg shadow-md transition-all duration-300 ease-in-out cursor-pointer active:scale-95 focus:outline-none focus:ring-2 hover:brightness-75 dark:text-white dark:bg-gray-800 dark:hover:bg-gray-950 dark:shadow-gray-800 dark:focus:ring-gray-600">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 mr-2">
              <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"/>
            </svg>
            <span class="text-base font-medium">主页</span>
          </button>
        </div>

        <img :src="schoolLogo" alt="Logo" class="h-8" />
<!--        <h3 class="mx-4 font-bold my-0.5 text-[20px]">安徽信息工程学院新生入学指引</h3>-->
      </div>

      <div class="flex items-center gap-2">
        <ThemeSwitch/>
        <div class="mx-3 h-[45px] w-[45px] rounded-full cursor-pointer scale-95"
             @click="router.push('/personal')">
          <img :src="userStore.user?.avatar ? baseURL+userStore.user.avatar : 'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="用户头像" class="w-10 h-10 rounded-xl">
        </div>
      </div>
    </div>
  </div>
    <!-- 顶部菜单栏 -->
    <div>
    <div class="fixed w-full h-[60px] left-0 z-100 transition-transform duration-300 bg-white dark:bg-gray-950 shadow-md dark:shadow-gray-900/30"
         :class="{ '-translate-y-full': isHidden }"
         v-if="$route.path !== '/map'">
      <nav class="container mx-auto px-4">
        <div class="flex justify-between items-center">
          <div class="text-xl font-bold text-gray-800 dark:text-white pl-2">
            校园导航
          </div>
          <div class="flex space-x-2 mx-4">

            <div v-for="(item, index) in menuItems" :key="index" class="dropdown-container relative">
              <!-- 主菜单按钮 -->
              <button
                  @click="item.type === 'dropdown' ? toggleDropdown(index) : navigateTo(item.path)"
                  class="px-4 py-2 rounded-md transition-colors duration-200 hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none"
                  :class="{
                    'bg-blue-200 text-blue-800 dark:bg-blue-800 dark:text-blue-200': 
                      (item.type === 'dropdown' && isActiveInSubItems(item.subItems)) || 
                      (item.type !== 'dropdown' && isActivePath(item.path)),
                    'text-gray-700 dark:text-gray-200': 
                      !(item.type === 'dropdown' && isActiveInSubItems(item.subItems)) && 
                      !(item.type !== 'dropdown' && isActivePath(item.path))
                  }">
                <div class="flex items-center space-x-1">
                  <span>{{ item.title }}</span>

                  <svg v-if="item.type === 'dropdown'" class="w-4 h-4 transition-transform duration-200"
                       :class="{ 'rotate-180': item.isOpen }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
                  </svg>
                </div>
              </button>

              <!-- 下拉菜单 -->
              <div v-if="item.type === 'dropdown' && item.isOpen" 
                   class="absolute top-full left-0 mt-2 w-48 rounded-lg shadow-lg overflow-hidden
                         bg-white dark:bg-gray-800 ring-1 ring-black/5 dark:ring-white/10
                         transform origin-top transition-all duration-200 animate-dropdown">
                <div class="py-1">
                  <a v-for="(subItem, subIndex) in item.subItems" :key="subIndex" 
                     @click="navigateTo(subItem.path)"
                     class="block px-4 py-2 text-sm cursor-pointer transition-colors duration-150"
                     :class="{
                       'bg-blue-100 text-blue-800 dark:bg-blue-900/60 dark:text-blue-200': isActivePath(subItem.path),
                       'text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700': !isActivePath(subItem.path)
                     }">
                    {{ subItem.name }}
                  </a>
                </div>
              </div>
            </div>
<!--            用户管理按钮-->
            <div class="flex flex-none">
            <button v-if="userStore.user?.manager === 1"
                    @click="router.push('/manager')"
                    class="flex flex-none px-4 py-2 rounded-md transition-colors duration-200 hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none">
              用户管理
              <span
                  v-if="pendingUsersCount > 0 && userStore.user && userStore.user.manager === 1"
                    class="ml-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                    {{ pendingUsersCount > 99 ? '99+' : pendingUsersCount }}
              </span>
            </button>
            </div>
          </div>
        </div>
      </nav>
    </div>
    </div>
  </header>
<!--返回顶部按钮-->
  <div>
    <button v-if="isVisible" @click="scrollToTop"
            class="fixed bottom-4 right-4 z-10 p-3 rounded-2xl text-white return_top
              hover:active:scale-95 hover:brightness-75 transition-all duration-300"
            :class="{'bg-blue-500 hover:bg-blue-600 shadow-lg shadow-blue-300/50': !themeStore.isDark, 
                     'bg-blue-700 hover:bg-blue-800 shadow-lg shadow-blue-900/30': themeStore.isDark}"
            title="返回顶部">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
      </svg>
    </button>
  </div>


  <main class="basic-color mt-16">
    <router-view />
  </main>
</template>

<style scoped>
.dropdown-container {
  display: inline-block;
}

@keyframes dropdown {
  from {
    opacity: 0;
    transform: scaleY(0.95) translateY(-10px);
  }
  to {
    opacity: 1;
    transform: scaleY(1) translateY(0);
  }
}

.animate-dropdown {
  animation: dropdown 0.2s ease-out forwards;
}
</style>
