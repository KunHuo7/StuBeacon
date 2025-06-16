<script setup>
import { ref } from 'vue'

// 两个运营商的营业厅信息
const operatorInfo = ref([
  {
    name: '中国移动',
    logo: '🔵',
    location: '校园中心区域综合服务楼一楼 101室',
    openHours: '工作日: 9:00-18:00, 周末: 10:00-16:00',
    contactNumber: '10086',
    description: '中国移动校园营业厅提供校园卡办理、手机套餐业务、宽带业务等服务，为学生提供专属优惠资费。'
  },
  {
    name: '中国电信',
    logo: '🔴',
    location: '校园中心区域综合服务楼一楼 102室',
    openHours: '工作日: 9:00-18:00, 周末: 10:00-16:00',
    contactNumber: '10000',
    description: '中国电信校园营业厅提供校园卡办理、宽带业务、手机套餐、校园网业务等服务，为学生提供校园专属优惠。'
  }
])

// 校园卡套餐
const campusCardPlans = ref([
  {
    operator: '中国移动',
    plans: [
      { 
        name: '校园卡套餐', 
        price: '39元/月', 
        data: '60GB/月', 
        voice: '200分钟/月', 
        features: [
          '校园100宽带（双终端）',
          '国内流量60GB',
          '国内其他流量300GB',
          '每月可领权益包',
          '校内流量免费',
          '闲时流量翻倍'
        ] 
      }
    ]
  },
  {
    operator: '中国电信',
    plans: [
      { 
        name: '校园卡套餐', 
        price: '39元/月', 
        data: '60GB/月', 
        voice: '200分钟/月', 
        features: [
          '校园100宽带（双终端）',
          '国内流量60GB',
          '国内其他流量300GB',
          '每月可领权益包',
          '校内WIFI免费',
          '月中可调整套餐'
        ] 
      }
    ]
  }
])

// 宽带服务
ref([
  {
    operator: '中国移动',
    services: [
      { name: '校园基础宽带', speed: '100Mbps', price: '30元/月', features: ['独享带宽', '支持多设备接入', '安装快捷'] },
      { name: '校园高速宽带', speed: '500Mbps', price: '50元/月', features: ['低延迟体验', '适合游戏党', '含IPTV服务'] },
      { name: '校园光纤宽带', speed: '1000Mbps', price: '80元/月', features: ['千兆光纤入户', '超高速体验', '免费提供路由器'] }
    ]
  },
  {
    operator: '中国电信',
    services: [
      { name: '校园基础宽带', speed: '200Mbps', price: '35元/月', features: ['独享带宽', '赠送校内WIFI账号', '专人上门安装'] },
      { name: '校园高速宽带', speed: '500Mbps', price: '55元/月', features: ['稳定高速连接', '电竞级体验', '含网络电视服务'] },
      { name: '校园光纤宽带', speed: '1000Mbps', price: '88元/月', features: ['千兆光纤专线', '24小时客服支持', '免费提供高级路由器'] }
    ]
  }
]);
// 办理流程
const processSteps = ref([
  {
    step: '选择运营商',
    description: '根据个人需求和偏好，选择中国移动或中国电信。'
  },
  {
    step: '准备证件',
    description: '准备好本人身份证、学生证，前往对应的营业厅。'
  },
  {
    step: '咨询选择套餐',
    description: '根据个人需求，选择合适的套餐或宽带服务。'
  },
  {
    step: '填写资料',
    description: '填写入网登记表、实名认证等相关资料。'
  },
  {
    step: '办理激活',
    description: '缴费后工作人员会帮助激活SIM卡或宽带，并指导基本使用。'
  }
])

// 常见问题
const faqs = ref([
  {
    question: '如何选择适合自己的运营商？',
    answer: '可以考虑以下因素：校内信号覆盖情况、家乡使用习惯、套餐价格与内容、宽带服务质量、同学使用反馈等。建议开学初可以先咨询师兄师姐或同学的使用体验。'
  },
  {
    question: '宿舍宽带如何申请？',
    answer: '在营业厅办理校园卡的同时可以一并申请宽带服务，需提供宿舍楼栋、房间号等信息，工作人员会安排专人上门安装。'
  },
  {
    question: '两个运营商有什么区别？',
    answer: '中国移动和中国电信在不同区域的信号覆盖、资费套餐、宽带服务等方面有所不同。建议根据个人需求和使用习惯选择，也可以在营业厅详细咨询比较后再决定。'
  },
  {
    question: '可以网上办理或预约吗？',
    answer: '两家运营商都提供官方APP和微信小程序预约服务，可以提前在线查看套餐并预约办理，减少排队等待时间。'
  },
  {
    question: '毕业后如何处理校园卡？',
    answer: '毕业前可前往营业厅办理套餐变更，将校园卡转为普通套餐继续使用，或办理销户手续。建议提前半个月处理，避免毕业高峰期排队等待。'
  }
])

// 注意事项
const notices = ref([
  '办理业务时必须携带本人身份证原件和学生证',
  '开学初（9月初）和放假前是业务办理高峰期，建议错峰前往',
  '可通过运营商官方APP或微信小程序提前预约，减少等待时间',
  '选择套餐前建议详细了解资费标准和使用条件',
  '校园宽带安装可能需要1-3个工作日，请提前规划',
  '注意保管好个人证件信息，谨防电信诈骗'
])

// 当前选择的运营商标签
const activeTab = ref('中国移动')
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">校园营业厅</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#overview" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">基本信息</a>
          <a href="#campuscard" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">校园卡套餐</a>
          <a href="#broadband" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">宽带服务</a>
          <a href="#process" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">办理流程</a>
          <a href="#faq" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">常见问题</a>
          <a href="#notices" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">注意事项</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">校园营业厅</h1>
      
      <!-- 运营商选择标签 -->
      <div class="mb-8 flex space-x-4 border-b border-gray-200 dark:border-gray-700">
        <button 
          v-for="operator in operatorInfo" 
          :key="operator.name" 
          @click="activeTab = operator.name"
          :class="[
            'py-2 px-4 text-lg font-medium border-b-2 transition-colors duration-200',
            activeTab === operator.name 
              ? 'border-blue-500 text-blue-600 dark:text-blue-400' 
              : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300'
          ]"
        >
          <span class="mr-2">{{ operator.logo }}</span>{{ operator.name }}
        </button>
      </div>
      
      <section id="overview" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">基本信息</h2>
        <div v-for="operator in operatorInfo.filter(op => activeTab === '全部' || op.name === activeTab)" :key="operator.name" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 mb-4">
          <div class="flex items-center mb-4">
            <div class="text-3xl mr-3">{{ operator.logo }}</div>
            <h3 class="text-xl font-bold text-gray-900 dark:text-white">{{ operator.name }}</h3>
          </div>
          
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
                <p class="text-gray-600 dark:text-gray-400">{{ operator.location }}</p>
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
                <p class="text-gray-600 dark:text-gray-400">{{ operator.openHours }}</p>
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
              <p class="font-medium text-gray-700 dark:text-gray-300">联系电话</p>
              <p class="text-gray-600 dark:text-gray-400">{{ operator.contactNumber }}</p>
            </div>
          </div>
          <p class="text-gray-700 dark:text-gray-300">{{ operator.description }}</p>
        </div>
      </section>
      
      <section id="campuscard" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">校园卡套餐</h2>
        <div v-for="operatorPlans in campusCardPlans.filter(op => activeTab === '全部' || op.operator === activeTab)" :key="operatorPlans.operator" class="mb-6">
          <div class="flex justify-center">
            <div v-for="(plan, index) in operatorPlans.plans" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6 border-t-4 max-w-md w-full transform transition-all duration-300 " :class="{'border-blue-500': operatorPlans.operator === '中国移动', 'border-red-500': operatorPlans.operator === '中国电信'}">
              <div class="flex justify-between items-center mb-4">
                <h3 class="text-2xl font-bold text-gray-900 dark:text-white">{{ plan.name }}</h3>
                <div class="text-3xl font-bold text-blue-600 dark:text-blue-400">{{ plan.price }}</div>
              </div>
              <div class="mb-4 flex flex-wrap gap-2">
                <span class="inline-block bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-300 text-sm px-3 py-1.5 rounded-full font-medium">{{ plan.data }}</span>
                <span class="inline-block bg-green-100 dark:bg-green-900 text-green-800 dark:text-green-300 text-sm px-3 py-1.5 rounded-full font-medium">{{ plan.voice }}</span>
              </div>
              <div class="bg-gray-50 dark:bg-gray-700/50 p-4 rounded-lg mb-4">
                <h4 class="text-sm uppercase tracking-wider text-gray-500 dark:text-gray-400 mb-2 font-medium">套餐特点</h4>
                <ul class="text-gray-700 dark:text-gray-300 text-sm space-y-2">
                  <li v-for="(feature, featureIndex) in plan.features" :key="featureIndex" class="flex items-start">
                    <svg class="h-5 w-5 text-green-500 mr-2 mt-0.5 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                    </svg>
                    <span>{{ feature }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>

      
      <section id="process" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">办理流程</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <ol class="relative border-l border-gray-200 dark:border-gray-700 ml-3">
            <li v-for="(process, index) in processSteps" :key="index" class="mb-6 ml-6">
              <span class="absolute flex items-center justify-center w-6 h-6 bg-blue-100 rounded-full -left-3 ring-8 ring-white dark:ring-gray-800 dark:bg-blue-900">
                <span class="text-blue-800 dark:text-blue-300 text-sm font-medium">{{ index + 1 }}</span>
              </span>
              <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">{{ process.step }}</h3>
              <p class="text-base font-normal text-gray-700 dark:text-gray-300">{{ process.description }}</p>
            </li>
          </ol>
        </div>
      </section>
      
      <section id="faq" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">常见问题</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <div v-for="(item, index) in faqs" :key="index" class="mb-4 last:mb-0 border-b border-gray-200 dark:border-gray-700 pb-4 last:border-0 last:pb-0">
            <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-2">{{ item.question }}</h3>
            <p class="text-gray-700 dark:text-gray-300">{{ item.answer }}</p>
          </div>
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

/* 移动运营商标签效果 */
@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

/* 表格响应式设计 */
@media (max-width: 768px) {
  table {
    display: block;
    overflow-x: auto;
  }
}
</style>