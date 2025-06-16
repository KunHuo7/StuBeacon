<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useThemeStore } from "@/stores/theme.js"

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)

// 学校基本信息
const schoolInfo = ref({
  name: '安徽信息工程学院',
  foundYear: '2003年',
  location: '安徽省芜湖市',
  area: '1200余亩',
  buildingArea: '37万余平方米',
  motto: '明德、博学、求是、创新'
})

// 学校历史沿革
const history = ref([
  {
    year: '2003年',
    event: '学校正式成立，前身为安徽工程大学机电学院'
  },
  {
    year: '2012年',
    event: '科大讯飞股份有限公司主导办学，开启校企合作新模式'
  },
  {
    year: '2016年',
    event: '获批成为应用型本科高校'
  },
  {
    year: '2020年',
    event: '获批"国家智慧教育平台试点学校"'
  }
])

// 学校特色
const features = ref([
  {
    title: '产教融合',
    content: '与科大讯飞等知名企业深度合作，实现校企共建专业、共建课程、共建实验室、共建师资队伍。'
  },
  {
    title: '智能特色',
    content: '依托科大讯飞人工智能技术优势，打造智能化校园环境，推进智能教育教学改革。'
  },
  {
    title: '应用导向',
    content: '注重培养学生实践能力和创新精神，强化应用型人才培养模式。'
  },
  {
    title: '国际交流',
    content: '与多个国家和地区的高校建立了合作关系，开展学生交换、教师互访、科研合作等国际交流活动。'
  }
])

// 学校荣誉
const honors = ref([
  '安徽省高等教育教学成果奖34项',
  '安徽省一流本科专业建设点10个',
  '安徽省特色专业6个',
  '安徽省产教融合示范基地',
  '国家智慧教育平台试点学校'
])

// 当前选中的部分
const activeSection = ref('overview')
const setActiveSection = (section) => {
  activeSection.value = section
}

// 监听滚动，更新当前激活的部分
const handleScroll = () => {
  const sections = ['overview', 'history', 'features', 'honors']
  
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
      <h2 class="text-2xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-blue-400 dark:from-blue-400 dark:to-blue-300">学校简介</h2>
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
            基本信息
          </a>
          <a href="#history" 
            @click="setActiveSection('history')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'history' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            历史沿革
          </a>
          <a href="#features" 
            @click="setActiveSection('features')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'features' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            学校特色
          </a>
          <a href="#honors" 
            @click="setActiveSection('honors')"
            :class="[
              'px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium',
              activeSection === 'honors' 
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/40 dark:text-blue-300 transform scale-105' 
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800/60'
            ]">
            学校荣誉
          </a>
        </div>
      </div>
    </div>
    <div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold mb-8 relative inline-block">
        <span class="bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-blue-500 dark:from-blue-500 dark:to-blue-300">
          {{ schoolInfo.name }}
        </span>
        <div class="absolute -bottom-2 left-0 w-full h-1 bg-gradient-to-r from-blue-600 to-blue-400 dark:from-blue-500 dark:to-blue-300 rounded-full"></div>
      </h1>

      <section id="overview" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          基本信息
        </h2>
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 p-6 border border-gray-100 dark:border-gray-700 transform transition-all duration-300 hover:shadow-lg dark:hover:shadow-blue-900/20">
          <div class="grid md:grid-cols-2 gap-6 mb-4">
            <div class="flex items-start">
              <div class="mr-3 text-blue-500 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 p-2 rounded-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-800 dark:text-gray-200">创建时间</p>
                <p class="text-gray-600 dark:text-gray-400">{{ schoolInfo.foundYear }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <div class="mr-3 text-blue-500 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 p-2 rounded-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-800 dark:text-gray-200">地理位置</p>
                <p class="text-gray-600 dark:text-gray-400">{{ schoolInfo.location }}</p>
              </div>
            </div>
          </div>
          <div class="grid md:grid-cols-2 gap-6 mb-4">
            <div class="flex items-start">
              <div class="mr-3 text-blue-500 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 p-2 rounded-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-800 dark:text-gray-200">占地面积</p>
                <p class="text-gray-600 dark:text-gray-400">{{ schoolInfo.area }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <div class="mr-3 text-blue-500 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 p-2 rounded-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-800 dark:text-gray-200">建筑面积</p>
                <p class="text-gray-600 dark:text-gray-400">{{ schoolInfo.buildingArea }}</p>
              </div>
            </div>
          </div>
          <div class="flex items-start">
            <div class="mr-3 text-blue-500 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 p-2 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
            </div>
            <div>
              <p class="font-medium text-gray-800 dark:text-gray-200">校训</p>
              <p class="text-gray-600 dark:text-gray-400">{{ schoolInfo.motto }}</p>
            </div>
          </div>
        </div>
      </section>

      <section id="history" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          历史沿革
        </h2>
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 p-6 border border-gray-100 dark:border-gray-700 transform transition-all duration-300 hover:shadow-lg dark:hover:shadow-blue-900/20">
          <ol class="relative border-l-2 border-blue-200 dark:border-blue-900 ml-3">
            <li v-for="(item, index) in history" :key="index" class="mb-8 ml-6">
              <div class="absolute flex items-center justify-center w-8 h-8 bg-blue-100 dark:bg-blue-900 rounded-full -left-4 ring-4 ring-white dark:ring-gray-800 shadow-md">
                <span class="text-blue-800 dark:text-blue-300 text-sm font-medium">{{ index + 1 }}</span>
              </div>
              <div class="p-4 bg-blue-50 dark:bg-blue-900/20 rounded-lg shadow-sm">
                <h3 class="flex items-center mb-1 text-lg font-semibold text-blue-600 dark:text-blue-300">{{ item.year }}</h3>
                <p class="text-base font-normal text-gray-700 dark:text-gray-300">{{ item.event }}</p>
              </div>
            </li>
          </ol>
        </div>
      </section>

      <section id="features" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          学校特色
        </h2>
        <div class="grid md:grid-cols-2 gap-6">
          <div v-for="(feature, index) in features" :key="index" 
               class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 p-6 border border-gray-100 dark:border-gray-700 transform transition-all duration-300 hover:shadow-lg hover:-translate-y-1 dark:hover:shadow-blue-900/20">
            <div class="flex mb-3">
              <div class="w-8 h-8 rounded-lg flex items-center justify-center bg-blue-100 dark:bg-blue-900/40 text-blue-600 dark:text-blue-300 font-bold mr-2">
                {{ index + 1 }}
              </div>
              <h3 class="text-xl font-medium text-blue-600 dark:text-blue-300">{{ feature.title }}</h3>
            </div>
            <p class="text-gray-700 dark:text-gray-300 ml-10">{{ feature.content }}</p>
          </div>
        </div>
      </section>

      <section id="honors" class="mb-12 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4 flex items-center">
          <div class="w-1.5 h-6 bg-blue-500 dark:bg-blue-400 rounded-full mr-2"></div>
          学校荣誉
        </h2>
        <div class="bg-white dark:bg-gray-800 rounded-xl shadow-md dark:shadow-blue-900/10 p-6 border border-gray-100 dark:border-gray-700 transform transition-all duration-300 hover:shadow-lg dark:hover:shadow-blue-900/20">
          <ul class="space-y-4">
            <li v-for="(honor, index) in honors" :key="index" 
                class="flex items-start bg-blue-50 dark:bg-blue-900/20 p-3 rounded-lg">
              <div class="w-6 h-6 rounded-full flex items-center justify-center bg-blue-500 dark:bg-blue-600 text-white text-xs font-bold mr-3 mt-0.5">
                {{ index + 1 }}
              </div>
              <span class="text-gray-700 dark:text-gray-300">{{ honor }}</span>
            </li>
          </ul>
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
