<script setup>
import { useThemeStore } from "@/stores/theme.js";
import router from "@/router/index.js";
import { useUserStore } from "@/stores/userStore.js";

const userStore = useUserStore()
const themeStore = useThemeStore()
const toggleThemeHandler = () => {
  themeStore.toggleTheme()
}
</script>

<template>
  <!-- 主题切换按钮 -->
  <button
      class="rounded-full p-2 transition-all duration-300 ease-in-out
           bg-gray-100 dark:bg-gray-800
           hover:bg-gray-200 dark:hover:bg-gray-700
           active:scale-90 active:bg-gray-300 dark:active:bg-gray-600
           focus:outline-none focus:ring-2 focus:ring-blue-400 dark:focus:ring-blue-500
           mr-3"
      @click="toggleThemeHandler()"
  >
    <!-- 太阳图标 -->
    <svg
        v-if="themeStore.currentTheme === 'light'"
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        class="size-6 text-yellow-500 dark:text-gray-200 transition-colors duration-200">
      <circle cx="12" cy="12" r="5" fill="none" stroke="currentColor"/>
      <line x1="12" y1="1" x2="12" y2="3" />
      <line x1="12" y1="21" x2="12" y2="23" />
      <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
      <line x1="18.36" y1="18.36" x2="19.78" y2="19.78" />
      <line x1="1" y1="12" x2="3" y2="12" />
      <line x1="21" y1="12" x2="23" y2="12" />
      <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
      <line x1="18.36" y1="5.64" x2="19.78" y2="4.22" />
    </svg>
    <!-- 月亮图标 -->
    <svg
        v-else
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        class="size-6 text-gray-400 dark:text-yellow-400 transition-colors duration-200">
      <path
          d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"
          fill="none"
          stroke="currentColor"/>
    </svg>
  </button>
  <!-- 话题讨论 -->
  <button
      @click="router.push('/talk')"
      class="flex items-center px-6 py-2 text-lg font-semibold
           rounded-full transition-all duration-300 ease-in-out
           bg-gradient-to-r from-amber-500 to-amber-600 dark:from-amber-600 dark:to-amber-700
           text-white dark:text-gray-100
           hover:from-amber-600 hover:to-amber-700 dark:hover:from-amber-700 dark:hover:to-amber-800
           hover:shadow-lg dark:hover:shadow-amber-700/30
           active:scale-90 active:shadow-md
           focus:outline-none focus:ring-2 focus:ring-amber-400 dark:focus:ring-amber-500
           shadow-md dark:shadow-amber-800/20
           space-x-2">
    <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="w-6 h-6 text-white dark:text-gray-100">
      <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
    </svg>
    <span>话题讨论</span>
  </button>
</template>

<style scoped>
/* Add any additional custom styles here if needed */
</style>