<script setup>
import { ref } from 'vue'

// 奖学金数据
const scholarships = ref([
  {
    name: '国家奖学金',
    amount: '8000元/年',
    eligibility: '学习成绩优异，社会实践、创新能力、综合素质等方面特别突出的二年级以上（含二年级）学生。',
    quota: '按教育部下达指标执行，约占学生总数的0.3%'
  },
  {
    name: '国家励志奖学金',
    amount: '5000元/年',
    eligibility: '家庭经济困难，学习成绩优秀的二年级以上（含二年级）学生。',
    quota: '按教育部下达指标执行，约占学生总数的3%'
  },
  {
    name: '学校一等奖学金',
    amount: '3000元/年',
    eligibility: '学习成绩排名专业前5%，无不及格课程，综合表现优秀的学生。',
    quota: '约占学生总数的5%'
  },
  {
    name: '学校二等奖学金',
    amount: '2000元/年',
    eligibility: '学习成绩排名专业前10%，无不及格课程，综合表现良好的学生。',
    quota: '约占学生总数的10%'
  },
  {
    name: '学校三等奖学金',
    amount: '1000元/年',
    eligibility: '学习成绩排名专业前20%，无不及格课程，综合表现良好的学生。',
    quota: '约占学生总数的15%'
  }
])

// 荣誉称号
const honors = ref([
  {
    name: '三好学生',
    criteria: '思想品德优秀，学习成绩优良（平均成绩85分以上），身心健康，积极参加社会实践和公益活动。',
    benefits: '优先推荐参评奖学金，优先推荐就业实习机会'
  },
  {
    name: '优秀学生干部',
    criteria: '担任学生干部职务，工作认真负责，表现突出，学习成绩良好（平均成绩80分以上）。',
    benefits: '优先推荐参评奖学金，优先参加学校组织的领导力培训'
  },
  {
    name: '优秀毕业生',
    criteria: '在校期间学习成绩优秀，获得过校级以上奖励，无违纪记录，综合素质高。',
    benefits: '授予荣誉证书，优先推荐就业，优先推荐研究生'
  }
])

// 申请流程
const applicationProcess = ref([
  {
    step: '发布通知',
    description: '每学年第一学期开学后，学校发布奖学金评选通知。'
  },
  {
    step: '个人申请',
    description: '符合条件的学生向班级提交申请材料，包括申请表、成绩单、获奖证书复印件等。'
  },
  {
    step: '班级评议',
    description: '班级评议小组对申请人进行初审，确定推荐名单。'
  },
  {
    step: '学院审核',
    description: '学院奖学金评审委员会审核班级推荐名单，确定学院推荐人选。'
  },
  {
    step: '学校审批',
    description: '学校奖学金评审委员会审批学院推荐名单，确定最终获奖名单。'
  },
  {
    step: '公示与发放',
    description: '获奖名单公示无异议后，学校发放奖学金。'
  }
])
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">奖励办法</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#scholarships" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">奖学金设置</a>
          <a href="#honors" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">荣誉称号</a>
          <a href="#process" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">申请流程</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">学生奖励办法</h1>
      
      <p class="text-lg text-gray-700 dark:text-gray-300 mb-8">
        为激励学生勤奋学习、全面发展，学校设立了完善的奖励体系，包括各类奖学金和荣誉称号。以下是相关奖励政策的详细介绍。
      </p>
      
      <section id="scholarships" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">奖学金设置</h2>
        <div class="space-y-6">
          <div v-for="(scholarship, index) in scholarships" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">{{ scholarship.name }}</h3>
            <div class="grid md:grid-cols-3 gap-4">
              <div>
                <p class="font-medium text-gray-700 dark:text-gray-300">金额</p>
                <p class="text-gray-600 dark:text-gray-400">{{ scholarship.amount }}</p>
              </div>
              <div>
                <p class="font-medium text-gray-700 dark:text-gray-300">名额</p>
                <p class="text-gray-600 dark:text-gray-400">{{ scholarship.quota }}</p>
              </div>
            </div>
            <div class="mt-4">
              <p class="font-medium text-gray-700 dark:text-gray-300">申请条件</p>
              <p class="text-gray-600 dark:text-gray-400">{{ scholarship.eligibility }}</p>
            </div>
          </div>
        </div>
      </section>
      
      <section id="honors" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">荣誉称号</h2>
        <div class="space-y-6">
          <div v-for="(honor, index) in honors" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">{{ honor.name }}</h3>
            <div class="mb-4">
              <p class="font-medium text-gray-700 dark:text-gray-300">评选标准</p>
              <p class="text-gray-600 dark:text-gray-400">{{ honor.criteria }}</p>
            </div>
            <div>
              <p class="font-medium text-gray-700 dark:text-gray-300">获得权益</p>
              <p class="text-gray-600 dark:text-gray-400">{{ honor.benefits }}</p>
            </div>
          </div>
        </div>
      </section>
      
      <section id="process" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">申请流程</h2>
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
          <ol class="relative border-l border-gray-200 dark:border-gray-700 ml-3">
            <li v-for="(process, index) in applicationProcess" :key="index" class="mb-6 ml-6">
              <span class="absolute flex items-center justify-center w-6 h-6 bg-blue-100 rounded-full -left-3 ring-8 ring-white dark:ring-gray-800 dark:bg-blue-900">
                <span class="text-blue-800 dark:text-blue-300 text-sm font-medium">{{ index + 1 }}</span>
              </span>
              <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">{{ process.step }}</h3>
              <p class="text-base font-normal text-gray-700 dark:text-gray-300">{{ process.description }}</p>
            </li>
          </ol>
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