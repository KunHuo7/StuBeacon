<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const menuItems = ref([
  {
    title: '作息时间',
    type: 'dropdown',
    isOpen: false,
  },
  {
    title: '军训活动',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '军事训练'},
      { name: '安全教育'},
      { name: '文体活动'}
    ]
  },
  {
    title: '入党专题',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '入党流程'},
      { name: '申请书'},
      { name: '注意事项'}
    ]
  },
  {
    title: '其他事项',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '物资准备' },
      { name: '饮食安排' }
    ]
  },
  {
    title: '温馨提醒',
    type: 'dropdown',
    isOpen: false,
    subItems: [
      { name: '自我保护' },
      { name: '注意事项' }
    ]
  }
])

const isHidden = ref(false)
let lastScrollY = 0


const handleScroll = () => {
  const currentScrollY = window.scrollY
  isHidden.value = currentScrollY > lastScrollY && currentScrollY > 100
  lastScrollY = currentScrollY
}


const toggleDropdown = (index) => {
  menuItems.value = menuItems.value.map((item, i) => ({
    ...item,
    isOpen: i === index ? !item.isOpen : false
  }))
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

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.removeEventListener('click', handleClickOutside)
})
// 表格数据
const schedule = [
  { content: '起床、洗漱、内务整理', time: '6:00—6:30' },
  { content: '早操', time: '6:30—7:10' },
  { content: '早饭', time: '7:10—8:00' },
  { content: '课前准备', time: '8:00—8:20' },
  { content: '操课', time: '8:20—11:20' },
  { content: '午饭', time: '11:20—12:30' },
  { content: '午休', time: '13:00—14:50' },
  { content: '课前准备', time: '14:50—15:00' },
  { content: '操课', time: '15:00—17:30' },
  { content: '晚饭', time: '17:30—18:40' },
  { content: '自由活动', time: '19:00—21:00' },
  { content: '晚点名', time: '21:00—21:30' },
  { content: '熄灯', time: '22:00' },
];

// 军事训练内容
const militaryTrainings = [
  { name: '军姿训练', description: '培养挺拔有力的站姿，锻炼意志力和耐力' },
  { name: '队列训练', description: '包括齐步走、正步走、跑步等基本动作' },
  { name: '军体拳', description: '锻炼身体协调性和基本格斗技能' },
  { name: '战术训练', description: '包括卧倒、匍匐前进等基本战术动作' }
];

// 思政国防教育内容
const educationContents = [
  { title: '爱国主义教育', content: '通过讲座、影片等形式增强爱国意识' },
  { title: '国防知识', content: '了解国防政策、军事常识和国际形势' },
  { title: '安全教育', content: '校园安全、寝室安全、网络安全等内容' }
];

// 文体活动内容
const activities = [
  { title: '军歌比赛', time: '第一周周末', location: '操场' },
  { title: '内务评比', time: '每周一次', location: '宿舍区' },
  { title: '军训汇演', time: '军训结束前', location: '主会场' }
];

// 防暑降温提示
const healthTips = [
  '及时补充水分，预防中暑',
  '注意休息，保持充足睡眠',
  '如有不适，及时向辅导员或教官报告',
  '适当调整训练强度，循序渐进'
];

// 物资准备清单
const supplies = [
  { category: '服装', items: ['军训服装', '运动鞋', '帽子', '袜子(多双)', '内衣裤(多套)'] },
  { category: '洗漱用品', items: ['毛巾', '牙刷牙膏', '洗发水', '沐浴露', '脸盆'] },
  { category: '医疗物品', items: ['创可贴', '消毒药水', '感冒药', '防晒霜', '清凉油'] },
  { category: '其他', items: ['水杯', '手电筒', '雨伞', '备用眼镜', '少量零钱'] }
];

</script>
<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">军训</h2>
      <div class="theme-toggle mt-4">

      </div>
      <nav class="mt-6">
        <ul class="space-y-3">
          <li v-for="(item, index) in menuItems" :key="index" class="dropdown-container">
            <div @click="toggleDropdown(index)" class="cursor-pointer flex items-center justify-end mb-2">
              <span class="text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400">{{ item.title }}</span>
              <span class="ml-2">{{ item.isOpen ? '▲' : '▼' }}</span>
            </div>
            <ul v-if="item.isOpen" class="space-y-2 text-sm pl-4">
              <li v-for="(subItem, subIndex) in item.subItems" :key="subIndex" class="text-gray-600 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-400 cursor-pointer">
                {{ subItem.name }}
              </li>
            </ul>
          </li>
        </ul>
      </nav>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">军训作息</h1>
      <div class="text-lg text-gray-700 dark:text-gray-300 mb-4">
        军训时间: 8月19日 - 9月8日
      </div>
      <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 mb-6 space-y-2">
        <li>军训期间同时以连队为单位开展始业教育（如开学典礼、体检等）</li>
        <li>各连队见面会安排在8月19日上午，需着军装</li>
        <li>8月23日上午迎接承训官兵，在此之前无其他始业教育安排的时间，原则上由副连带队训练</li>
      </ul>
      <div class="text-sm text-gray-500 dark:text-gray-400">
        由于各团的作息时间可能并不相同，这里仅供参考。
      </div>
      <div class="schedule-container my-6">
        <div class="schedule-table">
          <table>
            <thead>
            <tr>
              <th>内容</th>
              <th>时间</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item, index) in schedule" :key="index" :class="{'bg-gray-50 dark:bg-gray-800': index % 2 === 0}">
              <td>{{ item.content }}</td>
              <td>{{ item.time }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 军事训练内容 -->
      <section class="mb-10">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">军事训练</h1>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div v-for="(training, index) in militaryTrainings" :key="index" class="p-4 border border-gray-200 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800 shadow-sm">
            <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-200 mb-2">{{ training.name }}</h3>
            <p class="text-gray-600 dark:text-gray-400">{{ training.description }}</p>
          </div>
        </div>
      </section>

      <!-- 思政国防安全教育 -->
      <section class="mb-10">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">思政-国防-安全教育</h1>
        <div class="space-y-4">
          <div v-for="(edu, index) in educationContents" :key="index" class="p-4 border-l-4 border-blue-500 bg-blue-50 dark:bg-blue-900/20 dark:border-blue-400 rounded-r-lg">
            <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-200 mb-1">{{ edu.title }}</h3>
            <p class="text-gray-600 dark:text-gray-400">{{ edu.content }}</p>
          </div>
        </div>
      </section>

      <!-- 文体活动 -->
      <section class="mb-10">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">文体活动</h1>
        <div class="overflow-x-auto">
          <table class="min-w-full bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg">
            <thead>
              <tr class="bg-gray-100 dark:bg-gray-700">
                <th class="py-3 px-6 text-left text-gray-800 dark:text-gray-200">活动名称</th>
                <th class="py-3 px-6 text-left text-gray-800 dark:text-gray-200">时间</th>
                <th class="py-3 px-6 text-left text-gray-800 dark:text-gray-200">地点</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(activity, index) in activities" :key="index" class="border-b border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/50">
                <td class="py-3 px-6 text-gray-700 dark:text-gray-300">{{ activity.title }}</td>
                <td class="py-3 px-6 text-gray-700 dark:text-gray-300">{{ activity.time }}</td>
                <td class="py-3 px-6 text-gray-700 dark:text-gray-300">{{ activity.location }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- 物资准备 -->
      <section class="mb-10">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">物资准备</h1>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div v-for="(category, index) in supplies" :key="index" class="p-5 bg-gray-50 dark:bg-gray-800 rounded-lg shadow-sm">
            <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-200 mb-3 border-b border-gray-200 dark:border-gray-700 pb-2">{{ category.category }}</h3>
            <ul class="space-y-1">
              <li v-for="(item, itemIndex) in category.items" :key="itemIndex" class="text-gray-600 dark:text-gray-400 flex items-center">
                <span class="mr-2">•</span>
                {{ item }}
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 温馨提醒 -->
      <section class="mb-10">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-6">温馨提醒</h1>
        <div class="p-6 bg-yellow-50 dark:bg-yellow-900/20 border-l-4 border-yellow-500 dark:border-yellow-600 rounded-r-lg">
          <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-200 mb-4">防暑降温小贴士</h3>
          <ul class="space-y-2">
            <li v-for="(tip, index) in healthTips" :key="index" class="text-gray-700 dark:text-gray-300 flex items-center">
              <span class="text-yellow-500 dark:text-yellow-400 mr-2">★</span>
              {{ tip }}
            </li>
          </ul>
        </div>
      </section>
    </div>
  </div>
</template>
<style scoped>
/* 全局主题变量 */
:root {
  --bg-primary: #ffffff;
  --bg-secondary: #f8f9fa;
  --bg-tertiary: #f1f1f1;
  --text-primary: #333333;
  --text-secondary: #555555;
  --text-tertiary: #777777;
  --border-color: #dddddd;
  --shadow-color: rgba(0, 0, 0, 0.1);
  --accent-color: #4299e1;
  --hover-color: #f1f1f1;
}

:root.dark-mode {
  --bg-primary: #1a1a1a;
  --bg-secondary: #2d2d2d;
  --bg-tertiary: #2a2a2a;
  --text-primary: #e0e0e0;
  --text-secondary: #b0b0b0;
  --text-tertiary: #909090;
  --border-color: #333333;
  --shadow-color: rgba(255, 255, 255, 0.1);
  --accent-color: #63b3ed;
  --hover-color: #2a2a2a;
}

/* 表格样式 */
.schedule-table {
  width: 100%;
  max-width: 600px;
  background-color: var(--bg-primary);
  border-radius: 8px;
  box-shadow: 0 4px 6px var(--shadow-color);
  padding: 20px;
  transition: all 0.3s ease;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
  transition: background-color 0.3s ease;
}

th {
  background-color: var(--bg-secondary);
  font-weight: bold;
  color: var(--text-primary);
}

tr:hover {
  background-color: var(--hover-color);
}

tr:last-child td {
  border-bottom: none;
}

td {
  color: var(--text-secondary);
}

/* 斑马条纹 */
tr:nth-child(even) {
  background-color: var(--bg-secondary);
}

/* 表格圆角 */
table {
  border-radius: 8px;
  overflow: hidden;
}

thead tr:first-child th:first-child {
  border-top-left-radius: 8px;
}

thead tr:first-child th:last-child {
  border-top-right-radius: 8px;
}

tbody tr:last-child td:first-child {
  border-bottom-left-radius: 8px;
}

tbody tr:last-child td:last-child {
  border-bottom-right-radius: 8px;
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

section {
  animation: fadeIn 0.5s ease-in-out;
}

.dark-mode tr:hover {
  background-color: rgba(255, 255, 255, 0.05);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .schedule-table {
    max-width: 100%;
    padding: 10px;
  }
  
  th, td {
    padding: 10px 8px;
    font-size: 0.95rem;
  }
}
</style>