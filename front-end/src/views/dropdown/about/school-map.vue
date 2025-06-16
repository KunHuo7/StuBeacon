<script setup>
import { ref } from 'vue'

// 校园区域数据
const campusAreas = ref([
  {
    name: '教学区',
    buildings: [
      { name: '教学楼', location: '图书馆东南', features: ['多媒体教室', '智慧教室', '小型报告厅'] },
      { name: '实验楼', location: '校园东南区', features: ['专业实验室', '计算机教室', '语音室'] },
      { name: '图书馆', location: '校园中心区域', features: ['阅览室', '电子阅览室', '自习区', '学术交流区'] }
    ]
  },
  {
    name: '生活区',
    buildings: [
      { name: '学生公寓A17-A20号楼', location: '校园南区', features: ['4-6人间', '独立卫浴', '空调', '网络'] },
      { name: '学生公寓A21，A33-A36号楼', location: '校园西区', features: ['4-6人间', '独立卫浴', '空调', '网络'] },
      { name: '陶然餐厅', location: '校园南区', features: ['三层', '多种风味餐饮', '教师餐厅'] },
      { name: '玉兔，云耕餐厅', location: '校园西北区', features: ['快餐区', '风味餐厅'] }
    ]
  },
  {
    name: '运动区',
    buildings: [
      { name: '体育馆', location: 'A38健体馆', features: ['篮球场', '排球场', '羽毛球场', '乒乓球室'] },
      { name: '田径场', location: 'A17公寓楼东', features: ['400米标准跑道', '足球场', '看台'] },
      { name: '网球场', location: 'A18,A19公寓楼北', features: ['4片标准网球场', '照明设施'] },
      { name: '篮球场', location: '网球场北', features: ['8片室外篮球场', '照明设施'] }
    ]
  },
  {
    name: '行政区',
    buildings: [
      { name: '行政楼', location: '校园正门内', features: ['校长办公室', '各职能部门办公室', '会议室'] },
      { name: '学院楼', location: '校园各区域', features: ['院系办公室', '教师办公室', '会议室'] },
      { name: '科研楼', location: '校园东区', features: ['实验室', '研究中心', '创新空间'] }
    ]
  }
])

// 交通信息
const transportInfo = ref({
  busLines: ['1路公交车：市中心 - 学校（约30分钟）', '15路公交车：火车站 - 学校（约45分钟）', '22路公交车：汽车站 - 学校（约40分钟）'],
  schoolBus: ['工作日：学校 - 市中心（7:30, 12:00, 17:30）', '周末：学校 - 市中心（9:00, 16:00）'],
  taxi: '从市中心打车到学校约25元'
})

// 校园导航提示
const navigationTips = ref([
  '校园内设有指示牌和电子导航屏，方便查找目的地',
  '新生入学时会发放纸质校园地图',
  '学校官方APP内置校园导航功能',
  '校园内主要道路均有路灯照明，方便夜间出行',
  '校园内设有多个自行车停放点，方便骑行交通'
])
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">校园地图</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#campus-areas" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">校园区域</a>
          <a href="#transport" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">交通信息</a>
          <a href="#navigation" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">导航提示</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">校园地图导览</h1>
      
      <p class="text-lg text-gray-700 dark:text-gray-300 mb-8">
        安徽信息工程学院校园占地1200余亩，环境优美，设施完善。校园分为教学区、生活区、运动区和行政区四大功能区域，布局合理，交通便利。
      </p>
      
      <section id="campus-areas" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">校园区域</h2>
        <div class="space-y-8">
          <div v-for="(area, areaIndex) in campusAreas" :key="areaIndex" class="bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden">
            <div class="bg-blue-50 dark:bg-blue-900/30 p-4 border-l-4 border-blue-500">
              <h3 class="text-xl font-semibold text-gray-900 dark:text-white">{{ area.name }}</h3>
            </div>
            <div class="p-6">
              <div class="grid gap-4 md:grid-cols-2">
                <div v-for="(building, buildingIndex) in area.buildings" :key="buildingIndex" class="border border-gray-200 dark:border-gray-700 rounded-lg p-4">
                  <h4 class="text-lg font-medium text-gray-800 dark:text-gray-200 mb-2">{{ building.name }}</h4>
                  <p class="text-gray-600 dark:text-gray-400 mb-2"><span class="font-medium">位置：</span>{{ building.location }}</p>
                  <div>
                    <p class="font-medium text-gray-600 dark:text-gray-400 mb-1">设施：</p>
                    <ul class="list-disc list-inside text-gray-600 dark:text-gray-400 ml-2">
                      <li v-for="(feature, featureIndex) in building.features" :key="featureIndex">{{ feature }}</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      
      <section id="transport" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">交通信息</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <div class="mb-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">公交线路</h3>
            <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-2 ml-2">
              <li v-for="(bus, busIndex) in transportInfo.busLines" :key="busIndex">{{ bus }}</li>
            </ul>
          </div>
          <div class="mb-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">校车服务</h3>
            <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-2 ml-2">
              <li v-for="(schoolBus, schoolBusIndex) in transportInfo.schoolBus" :key="schoolBusIndex">{{ schoolBus }}</li>
            </ul>
          </div>
          <div>
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">出租车</h3>
            <p class="text-gray-700 dark:text-gray-300 ml-2">{{ transportInfo.taxi }}</p>
          </div>
        </div>
      </section>
      
      <section id="navigation" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">导航提示</h2>
        <div class="bg-green-50 dark:bg-green-900/30 border-l-4 border-green-500 p-6 rounded">
          <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-3">
            <li v-for="(tip, tipIndex) in navigationTips" :key="tipIndex">{{ tip }}</li>
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