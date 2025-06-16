<script setup>
import { ref } from 'vue'

// 学习指南数据
const studyGuides = ref([
  {
    title: '课程安排',
    content: '大一新生的课程主要包括公共基础课和专业基础课。公共基础课包括思想政治理论课、大学英语、高等数学、计算机基础等；专业基础课则根据不同专业设置不同的课程内容。课程表将在开学初通过教务系统发布，请及时查看。'
  },
  {
    title: '学分制度',
    content: '我校实行学分制管理，学生需修满规定学分方可毕业。各专业的具体学分要求请参考专业培养方案。一般情况下，本科专业需修满160-180学分，其中包括必修课、选修课、实践环节等多个部分。'
  },
  {
    title: '考核方式',
    content: '课程考核分为考试和考查两种形式。考试课程一般采用闭卷或开卷笔试；考查课程则可能采用论文、报告、作品、实验操作等多种形式。平时成绩（包括出勤、作业、课堂表现等）通常占总成绩的60%。'
  },
  {
    title: '学业规划',
    content: '建议新生入学后尽快制定个人学业规划，明确学习目标和发展方向。学校为每位新生配备了学业导师，可以向导师咨询专业发展、课程选择等问题。此外，学校定期举办职业规划讲座，帮助学生更好地规划未来。'
  },
  {
    title: '学习资源',
    content: '学校提供丰富的学习资源，包括图书馆、电子资源库、学习中心等。图书馆开放时间为周一至周日8:00-22:00，提供自习座位、图书借阅、电子资源访问等服务。此外，各学院还设有专业实验室和学习空间，供学生使用。'
  }
])

// 学习技巧
const studyTips = ref([
  '合理规划时间，制定学习计划，避免临时抱佛脚',
  '积极参与课堂讨论，与老师和同学互动交流',
  '养成良好的笔记习惯，课后及时复习巩固',
  '遇到学习困难时，及时向老师、同学或辅导员求助',
  '利用图书馆和网络资源拓展知识面，不局限于课本内容',
  '参与学科竞赛、科研项目等活动，提升专业能力'
])

// 常见问题
const faqs = ref([
  {
    question: '如何选课？',
    answer: '选课通过教务系统进行，一般在每学期开始前一周开放。选课分为预选和正选两个阶段，具体选课时间请关注教务处通知。'
  },
  {
    question: '如何查询成绩？',
    answer: '学期结束后，可通过教务系统查询课程成绩。如对成绩有疑问，可在成绩公布后一周内向任课教师或教务处提出复查申请。'
  },
  {
    question: '挂科了怎么办？',
    answer: '课程不及格需要参加补考或重修。补考一般在下学期开学初进行，重修则需要在下一次课程开设时重新学习。建议及时与辅导员和任课教师沟通，了解具体安排。'
  },
  {
    question: '如何申请转专业？',
    answer: '学校一般在大一下学期接受转专业申请，具体政策和流程请参考教务处发布的转专业通知。转专业需要满足一定的学业条件，并参加接收学院组织的考核。'
  }
])
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">学习指南</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#guides" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">学习指南</a>
          <a href="#tips" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">学习技巧</a>
          <a href="#faq" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">常见问题</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">新生学习指南</h1>
      
      <p class="text-lg text-gray-700 dark:text-gray-300 mb-8">
        欢迎来到安徽信息工程学院！为了帮助新生尽快适应大学学习生活，我们准备了以下学习指南，希望对你有所帮助。
      </p>
      
      <section id="guides" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">学习指南</h2>
        <div class="space-y-6">
          <div v-for="(guide, index) in studyGuides" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-3">{{ guide.title }}</h3>
            <p class="text-gray-700 dark:text-gray-300">{{ guide.content }}</p>
          </div>
        </div>
      </section>
      
      <section id="tips" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">学习技巧</h2>
        <div class="bg-blue-50 dark:bg-blue-900/30 border-l-4 border-blue-500 p-6 rounded">
          <ul class="list-disc list-inside text-gray-700 dark:text-gray-300 space-y-3">
            <li v-for="(tip, index) in studyTips" :key="index">{{ tip }}</li>
          </ul>
        </div>
      </section>
      
      <section id="faq" class="mb-10 scroll-mt-20">
        <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">常见问题</h2>
        <div class="space-y-4">
          <div v-for="(item, index) in faqs" :key="index" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-2">{{ item.question }}</h3>
            <p class="text-gray-700 dark:text-gray-300">{{ item.answer }}</p>
          </div>
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