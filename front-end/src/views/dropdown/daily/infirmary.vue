<script setup>
import { ref } from 'vue'

// 医务室信息
const infirmaryInfo = ref({
  location: '校园西南区教室公寓旁',
  openHours: '日常就诊: 8:30-21:00 (中餐时间: 11:40-12:00 ,晚餐时间: 17:30-18:00)',
  emergencyContact: '879-5190',
  description: '校医务室为全校师生提供基本医疗服务，配备专业医护人员，设有内科、外科等基础科室。'
})

// 服务项目
const services = ref([
  {
    title: '日常诊疗',
    items: ['感冒发热', '外伤处理', '常见病治疗', '药品配发']
  },
  {
    title: '健康检查',
    items: ['新生入学体检', '毕业生体检', '常规健康检查']
  },
  {
    title: '预防保健',
    items: ['传染病防控', '健康教育']
  },
  {
    title: '心理咨询',
    items: ['心理健康评估', '个人心理咨询', '心理辅导']
  }
])

// 就医流程
const medicalProcess = ref([
  {
    step: '挂号',
    description: '前往医务室前台出示校园卡进行挂号登记。'
  },
  {
    step: '分诊',
    description: '护士根据症状进行初步评估，引导至相应科室。'
  },
  {
    step: '诊断',
    description: '医生进行诊断，开具处方或进行处置。'
  },
  {
    step: '取药/治疗',
    description: '凭处方到药房取药或在治疗室接受治疗。'
  },
  {
    step: '结算',
    description: '使用校园卡或医保卡进行费用结算。'
  }
])

// 注意事项
const notices = ref([
  '就诊请携带校园卡和医保卡',
  '非紧急情况请避开就诊高峰期（上午9:00-10:30，下午2:00-3:30）',
  '如遇突发疾病，可拨打医务室急诊电话',
  '需要转诊至校外医院的同学，请先到医务室开具转诊单',
  '医务室药品仅限校内使用，不对外售卖'
])
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">医务室</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#overview" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">基本信息</a>
          <a href="#services" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">服务项目</a>
          <a href="#process" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">就医流程</a>
          <a href="#notices" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">注意事项</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">校园医务室</h1>
      
      <section id="overview" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">基本信息</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <div class="grid md:grid-cols-2 gap-4 mb-4">
            <div class="flex items-start">
              <div class="mr-2 text-blue-500 dark:text-blue-400">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-700 dark:text-gray-300">位置</p>
                <p class="text-gray-600 dark:text-gray-400">{{ infirmaryInfo.location }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <div class="mr-2 text-blue-500 dark:text-blue-400">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div>
                <p class="font-medium text-gray-700 dark:text-gray-300">开放时间</p>
                <p class="text-gray-600 dark:text-gray-400">{{ infirmaryInfo.openHours }}</p>
              </div>
            </div>
          </div>
          <div class="flex items-start mb-4">
            <div class="mr-2 text-blue-500 dark:text-blue-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
              </svg>
            </div>
            <div>
              <p class="font-medium text-gray-700 dark:text-gray-300">紧急联系电话</p>
              <p class="text-gray-600 dark:text-gray-400">{{ infirmaryInfo.emergencyContact }}</p>
            </div>
          </div>
          <p class="text-gray-700 dark:text-gray-300">{{ infirmaryInfo.description }}</p>
        </div>
      </section>
      
      <section id="services" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">服务项目</h2>
        <div class="grid md:grid-cols-2 gap-6">
          <div v-for="(service, index) in services" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">{{ service.title }}</h3>
            <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-1">
              <li v-for="(item, itemIndex) in service.items" :key="itemIndex">{{ item }}</li>
            </ul>
          </div>
        </div>
      </section>
      
      <section id="process" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">就医流程</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <ol class="relative border-l border-gray-200 dark:border-gray-700 ml-3">
            <li v-for="(process, index) in medicalProcess" :key="index" class="mb-6 ml-6">
              <span class="absolute flex items-center justify-center w-6 h-6 bg-blue-100 rounded-full -left-3 ring-8 ring-white dark:ring-gray-800 dark:bg-blue-900">
                <span class="text-blue-800 dark:text-blue-300 text-sm font-medium">{{ index + 1 }}</span>
              </span>
              <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">{{ process.step }}</h3>
              <p class="text-base font-normal text-gray-700 dark:text-gray-300">{{ process.description }}</p>
            </li>
          </ol>
        </div>
      </section>
      
      <section id="notices" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">注意事项</h2>
        <div class="bg-yellow-50 dark:bg-yellow-900/30 border-l-4 border-yellow-500 p-4 rounded">
          <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-2">
            <li v-for="(notice, index) in notices" :key="index">{{ notice }}</li>
          </ul>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}

/* 滚动到锚点时的平滑效果 */
html {
  scroll-behavior: smooth;
}
</style>