<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

// 菜单数据结构 - 修改为地图功能相关选项
const menuItems = ref([
  {
    title: '地图控件',
    icon: 'controls',
    type: 'controls',
    description: '控制地图显示选项'
  },
  {
    title: '校园位置',
    icon: 'location',
    type: 'locations',
    description: '查看校园重要地点'
  },
  {
    title: '路线规划',
    icon: 'route',
    type: 'route',
    description: '规划校园内路线'
  }
]);


// 控制菜单展开/收起状态
const isMenuExpanded = ref(false);
const activeItem = ref(null);

// 处理菜单展开/收起
const toggleMenu = () => {
  isMenuExpanded.value = !isMenuExpanded.value;
};

// 处理菜单项点击 - 修改为发出事件而不是路由跳转
const handleMenuItemClick = (item) => {
  activeItem.value = item;
  // 触发自定义事件，传递选中的菜单类型
  emit('menu-select', item.type);
};

// 定义组件事件
const emit = defineEmits(['menu-select']);

// 处理菜单项悬停
const handleMenuItemHover = (item) => {
  if (!isMenuExpanded.value) {
    activeItem.value = item;
  }
};

// 控制导航栏可见性
const isHidden = ref(false);




// 图标组件
</script>

<template>
  <!-- 菜单栏 -->
  <div
    class=" z-50 transition-all duration-300 ease-in-out h-full"
    :class="{
      'w-20': !isMenuExpanded,
      'w-64': isMenuExpanded,
      '-translate-y-full': isHidden
    }"
  >
    <!-- 菜单背景 -->
    <div class="h-full bg-gradient-to-b from-blue-300 to-indigo-300 dark:from-gray-800 dark:to-gray-900 rounded-r-2xl shadow-xl overflow-hidden flex flex-col">
      <!-- 菜单头部 -->
      <div class="p-4 flex items-center justify-between">
        <div class="flex items-center space-x-3">
          <h1 class="text-white select-none font-bold text-xl transition-opacity duration-200" :class="{ 'opacity-0 w-0': !isMenuExpanded, 'opacity-100': isMenuExpanded }">菜单</h1>
        </div>
        <button
          @click="toggleMenu"
          class="w-9 h-9 rounded-full bg-blue-400/30 dark:bg-gray-700/50 flex items-center justify-center text-white hover:bg-blue-400/50 dark:hover:bg-gray-700/80 transition-all duration-200"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6 transition-transform duration-300"
            :class="{ 'rotate-180': isMenuExpanded }"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>

      <!-- 菜单项列表 -->
      <div class="flex-1 overflow-y-auto py-4 px-3">
        <ul class="space-y-2">
          <li v-for="(item, index) in menuItems" :key="index">
            <a
              @click="handleMenuItemClick(item)"
              @mouseenter="handleMenuItemHover(item)"
              class="flex items-center p-3 rounded-xl transition-all duration-200 group cursor-pointer"
              :class="{
                'justify-center': !isMenuExpanded,
                'bg-white/10 dark:bg-gray-700/30': activeItem === item,
                'hover:bg-white/10 dark:hover:bg-gray-700/30': activeItem !== item
              }"
            >
              <!-- 图标 -->
              <div
                class="flex items-center justify-center w-10 h-10 rounded-lg transition-all duration-300"
                :class="{
                  'bg-white dark:bg-gray-700 text-blue-600 dark:text-blue-400 shadow-lg transform scale-110': activeItem === item,
                  'text-white group-hover:bg-white/20 dark:group-hover:bg-gray-700/40 hover:scale-105': activeItem !== item
                }"
              >
                <!-- 地图控件图标 -->
                <svg v-if="item.icon === 'controls'" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <!-- 校园位置图标 -->
                <svg v-else-if="item.icon === 'location'" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <!-- 路线规划图标 -->
                <svg v-else-if="item.icon === 'route'" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
                </svg>
              </div>

              <!-- 文本和描述 (仅在展开时显示) -->
              <button
                v-if="isMenuExpanded"
                class="ml-3 flex flex-col transition-all duration-200"
                :title=item.title
              >
                <span class="text-white font-medium text-base">{{ item.title }}</span>
                <span class="text-blue-100 dark:text-gray-300 text-xs mt-1">{{ item.description }}</span>
              </button>

<!--             <p class="font-medium">{{ item.title }}</p>-->
            </a>
          </li>
        </ul>
      </div>

      <!-- 菜单底部 -->
      <div class="p-4 border-t border-blue-400/30 dark:border-gray-700/50">
        <!-- 底部空间保留，但移除了退出登录按钮 -->
      </div>
    </div>
  </div>

  <!-- 移动端菜单触发按钮 -->
  <div class="fixed bottom-6 right-6 md:hidden z-50">
    <button
      @click="toggleMenu"
      class="w-14 h-14 rounded-full bg-gradient-to-r from-blue-600 to-indigo-700 dark:from-gray-700 dark:to-gray-900 flex items-center justify-center shadow-lg hover:shadow-xl transition-all duration-300 ease-in-out transform hover:scale-105 active:scale-95"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
      </svg>
    </button>
  </div>
</template>

<style scoped>
@keyframes fadeIn {
  from { opacity: 0; transform: translateX(-10px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes slideDown {
  from { opacity: 0; transform: scaleY(0.8); }
  to { opacity: 1; transform: scaleY(1); }
}

/* 添加点击波纹效果 */
@keyframes ripple {
  0% { transform: scale(0); opacity: 1; }
  100% { transform: scale(2.5); opacity: 0; }
}
</style>