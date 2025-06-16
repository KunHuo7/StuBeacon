<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Menu data structure
const menuItems = ref([
  {
    title: '关于我们',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '学校简介', path: '/about/school-profile' },
      { name: '学校地图', path: '/about/school-map' },
      { name: '入学须知', path: '/about/admission-guide' }
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
    title: '联系我们',
    type: 'link',
    path: '/contact-us'
  },
  {
    title: '学校官网',
    type: 'link',
    path: 'https://www.aiit.edu.cn/'  // 更新为实际官网链接
  }
])

// Control navbar visibility
const isHidden = ref(false)
let lastScrollY = 0

// Handle scroll events
const handleScroll = () => {
  const currentScrollY = window.scrollY
  isHidden.value = currentScrollY > lastScrollY && currentScrollY > 100
  lastScrollY = currentScrollY
}

// Toggle dropdown menu
const toggleDropdown = (index) => {
  menuItems.value = menuItems.value.map((item, i) => ({
    ...item,
    isOpen: i === index ? !item.isOpen : false
  }))
}

// Navigate to path
const navigateTo = (path) => {
  if (path.startsWith('http')) {
    // If it's an external link like the official website
    window.location.href = path
  } else {
    router.push(path)
  }
}

// Handle click outside to close dropdowns
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

// Lifecycle hooks
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <header
      class="fixed w-full top-[61px] left-0 z-100 transition-transform duration-300
           bg-white dark:bg-gray-800 shadow-md dark:shadow-gray-900/30"
      :class="{ '-translate-y-full': isHidden }">
    <nav class="container mx-auto px-4 py-4">
      <div class="flex justify-between items-center">
        <div class="text-xl font-bold text-gray-800 dark:text-white">
          校园导航
        </div>

        <div class="flex space-x-2 mx-4">  <!-- 这里添加了 mx-4 类来调整按钮的间隔 -->
          <div
              v-for="(item, index) in menuItems"
              :key="index"
              class="dropdown-container relative">
            <!-- Menu Item Button -->
            <button
                @click="item.type === 'dropdown' ? toggleDropdown(index) : navigateTo(item.path)"
                class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-200
                     hover:bg-gray-100 dark:hover:bg-gray-700
                     focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400
                     transition-colors duration-200">
              <div class="flex items-center space-x-1">
                <span>{{ item.title }}</span>
                <svg
                    v-if="item.type === 'dropdown'"
                    class="w-4 h-4 transition-transform duration-200"
                    :class="{ 'rotate-180': item.isOpen }"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24">
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M19 9l-7 7-7-7"/>
                </svg>
              </div>
            </button>

            <!-- Dropdown Menu -->
            <div
                v-if="item.type === 'dropdown' && item.isOpen"
                class="absolute top-full left-0 mt-2 w-48 rounded-md shadow-lg
                     bg-white dark:bg-gray-800 ring-1 ring-black ring-opacity-5"
            >
              <div class="py-1">
                <a
                    v-for="(subItem, subIndex) in item.subItems"
                    :key="subIndex"
                    @click="navigateTo(subItem.path)"
                    class="block px-4 py-2 text-sm text-gray-700 dark:text-gray-200
                         hover:bg-gray-100 dark:hover:bg-gray-700
                         cursor-pointer transition-colors duration-150"
                >
                  {{ subItem.name }}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<style scoped>
.dropdown-container {
  display: inline-block;
}
</style>