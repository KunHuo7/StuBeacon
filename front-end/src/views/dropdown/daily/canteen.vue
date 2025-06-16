<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useThemeStore } from "@/stores/theme.js"

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)

// 食堂数据
const canteens = ref([
  {
    name: '陶然餐厅',
    location: '校园西南区',
    floors: '1-4',
    features: ['一楼：快餐区', '二楼：大众餐厅', '三楼：特色餐厅','四楼：教师餐厅'],
    openHours: '早餐: 6:30-8:30, 午餐: 11:00-14:00, 晚餐: 16:00-21:00'
  },
  {
    name: '耘耕餐厅',
    location: '校园西北区',
    floors: '第二',
    features: [ '风味餐厅'],
    openHours: '早餐: 6:30-8:30, 午餐: 11:00-14:00, 晚餐: 16:00-21:00'
  },
  {
    name: '玉兔食堂',
    location: '校园西北区',
    floors: '第三',
    features: [ '风味餐厅'],
    openHours: '早餐: 6:30-8:30, 午餐: 11:00-14:00, 晚餐: 16:00-21:00'
  }
])

// 食堂须知
const notices = ref([
  '请自觉排队取餐，保持秩序',
  '爱惜粮食，按需取餐',
  '保持餐厅环境整洁，餐盘请放置回收处',
  '尊重食堂工作人员，文明用餐',
  '校园卡丢失请及时挂失，以免造成损失'
])

// 当前选中的部分
const activeSection = ref('overview')
const setActiveSection = (section) => {
  activeSection.value = section
}

// 监听滚动，更新当前激活的部分
const handleScroll = () => {
  const sections = ['overview', 'notices', 'payment']
  
  for (const section of sections) {
    const element = document.getElementById(section)
    if (element) {
      const rect = element.getBoundingClientRect()
      if (rect.top <= 100 && rect.bottom >= 100) {
        activeSection.value = section
        break
      }
    }
  }
}

// 页面加载时添加滚动监听
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧导航 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-blue-400 dark:from-blue-400 dark:to-blue-300">校园食堂</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#overview" 
            @click="setActiveSection('overview')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'overview' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            食堂概览
          </a>
          <a href="#notices" 
            @click="setActiveSection('notices')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'notices' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            用餐须知
          </a>
          <a href="#payment" 
            @click="setActiveSection('payment')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'payment' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            支付方式
          </a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold mb-8 relative inline-block">
        <span class="bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-blue-500 dark:from-blue-500 dark:to-blue-300">
          校园食堂
        </span>
        <div class="absolute -bottom-2 left-0 w-full h-1 bg-gradient-to-r from-blue-600 to-blue-400 dark:from-blue-500 dark:to-blue-300 rounded-full"></div>
      </h1>
      
      <section id="overview" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          食堂概览
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-6 bg-white/50 dark:bg-gray-800/50 p-4 rounded-lg border border-gray-100 dark:border-gray-700">
          我校共有三个食堂，分布在校园不同区域，为全校师生提供多样化的餐饮服务。每个食堂都有其特色菜品和就餐环境，满足不同师生的用餐需求。
        </p>
        
        <div class="grid gap-6 md:grid-cols-1 lg:grid-cols-2 xl:grid-cols-3">
          <div v-for="(canteen, index) in canteens" :key="index" 
               class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 overflow-hidden 
                      transition-all duration-300 hover:shadow-lg hover:-translate-y-1 dark:hover:shadow-blue-900/20
                      border border-gray-100 dark:border-gray-700">
            <div class="p-6">
              <div class="flex items-center mb-4">
                <div class="w-10 h-10 rounded-lg flex items-center justify-center bg-blue-100 dark:bg-blue-900/40 
                            text-blue-600 dark:text-blue-300 font-bold mr-3">
                  {{ index + 1 }}
                </div>
                <h3 class="text-xl font-bold text-blue-600 dark:text-blue-300">{{ canteen.name }}</h3>
              </div>
              
              <div class="space-y-3 ml-2">
                <div class="flex items-start">
                  <div class="mr-2 text-blue-500 dark:text-blue-400 mt-1">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                  </div>
                  <div>
                    <p class="font-medium text-gray-800 dark:text-gray-200">位置</p>
                    <p class="text-gray-600 dark:text-gray-400">{{ canteen.location }}</p>
                  </div>
                </div>
                
                <div class="flex items-start">
                  <div class="mr-2 text-blue-500 dark:text-blue-400 mt-1">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                    </svg>
                  </div>
                  <div>
                    <p class="font-medium text-gray-800 dark:text-gray-200">楼层</p>
                    <p class="text-gray-600 dark:text-gray-400">{{ canteen.floors }}层</p>
                  </div>
                </div>
                
                <div class="flex items-start">
                  <div class="mr-2 text-blue-500 dark:text-blue-400 mt-1">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                    </svg>
                  </div>
                  <div>
                    <p class="font-medium text-gray-800 dark:text-gray-200">特色</p>
                    <ul class="text-gray-600 dark:text-gray-400 space-y-1 mt-1">
                      <li v-for="(feature, fIndex) in canteen.features" :key="fIndex" class="flex items-center">
                        <span class="w-1.5 h-1.5 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></span>
                        {{ feature }}
                      </li>
                    </ul>
                  </div>
                </div>
                
                <div class="flex items-start">
                  <div class="mr-2 text-blue-500 dark:text-blue-400 mt-1">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                  <div>
                    <p class="font-medium text-gray-800 dark:text-gray-200">营业时间</p>
                    <p class="text-gray-600 dark:text-gray-400">{{ canteen.openHours }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      
      <section id="notices" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          用餐须知
        </h2>
        <div class="bg-gradient-to-r from-blue-50 to-white dark:from-blue-900/30 dark:to-gray-800/80 border-l-4 border-blue-500 p-6 rounded-lg shadow-md">
          <ul class="space-y-3">
            <li v-for="(notice, index) in notices" :key="index" class="flex items-start">
              <div class="w-6 h-6 rounded-full flex items-center justify-center bg-blue-100 dark:bg-blue-900/50 
                          text-blue-600 dark:text-blue-300 font-bold mr-3 mt-0.5 shrink-0">
                {{ index + 1 }}
              </div>
              <span class="text-gray-700 dark:text-gray-300">{{ notice }}</span>
            </li>
          </ul>
        </div>
      </section>
      
      <section id="payment" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          支付方式
        </h2>
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 p-6 border border-gray-100 dark:border-gray-700">
          <div class="grid md:grid-cols-2 gap-6">
            <div class="border border-gray-200 dark:border-gray-700 rounded-lg p-5 shadow-sm
                        hover:shadow-md hover:border-blue-200 dark:hover:border-blue-900/50 transition-all duration-300
                        bg-gradient-to-br from-white to-blue-50 dark:from-gray-800 dark:to-blue-900/20">
              <div class="flex items-center mb-3">
                <div class="p-2 rounded-lg mr-3 bg-blue-100 dark:bg-blue-900/40 text-blue-500 dark:text-blue-300">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                  </svg>
                </div>
                <h3 class="text-lg font-semibold text-blue-600 dark:text-blue-300">校园卡支付</h3>
              </div>
              <p class="text-gray-700 dark:text-gray-300 ml-12">使用校园卡在食堂内任意窗口消费，支持自助充值和线上充值。</p>
            </div>
            
            <div class="border border-gray-200 dark:border-gray-700 rounded-lg p-5 shadow-sm
                        hover:shadow-md hover:border-blue-200 dark:hover:border-blue-900/50 transition-all duration-300
                        bg-gradient-to-br from-white to-blue-50 dark:from-gray-800 dark:to-blue-900/20">
              <div class="flex items-center mb-3">
                <div class="p-2 rounded-lg mr-3 bg-blue-100 dark:bg-blue-900/40 text-blue-500 dark:text-blue-300">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
                  </svg>
                </div>
                <h3 class="text-lg font-semibold text-blue-600 dark:text-blue-300">移动支付</h3>
              </div>
              <p class="text-gray-700 dark:text-gray-300 ml-12">食堂内支持微信、支付宝等移动支付方式，扫码即可完成支付。</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s, transform 0.3s, box-shadow 0.3s;
}

/* 滚动到锚点时的平滑效果 */
html {
  scroll-behavior: smooth;
}

/* 光亮主题下的卡片悬浮效果 */
@media (prefers-color-scheme: light) {
  .hover\:shadow-lg:hover {
    box-shadow: 0 10px 15px -3px rgba(59, 130, 246, 0.1), 0 4px 6px -2px rgba(59, 130, 246, 0.05);
  }
}

/* 暗黑主题下的卡片悬浮效果 */
.dark .hover\:shadow-blue-900\/20:hover {
  box-shadow: 0 10px 15px -3px rgba(30, 58, 138, 0.2), 0 4px 6px -2px rgba(30, 58, 138, 0.1);
}
</style>