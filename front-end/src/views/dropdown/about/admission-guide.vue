<script setup>
import { ref } from 'vue'

// 入学须知数据
const admissionSteps = ref([
  {
    title: '报到流程',
    content: '新生需按照录取通知书上的时间和地点进行报到。报到时请携带录取通知书、身份证等必要证件。报到流程包括身份验证、缴费、领取学生证等环节。'
  },
  {
    title: '所需材料',
    content: '1. 录取通知书原件\n2. 身份证原件及复印件\n3. 户口本原件及复印件\n4. 近期免冠照片（蓝底，2寸）6张\n5. 高中毕业证原件\n6. 党团关系材料（如有）'
  },
  {
    title: '缴费说明',
    content: '学费和住宿费可通过以下方式缴纳：\n1. 银行转账：请使用录取通知书上的学号作为转账备注\n2. 现场缴费：报到当天可在指定地点使用现金或刷卡缴纳\n3. 线上支付：通过学校官网或指定APP进行在线支付'
  },
  {
    title: '住宿安排',
    content: '学校为新生提供标准化宿舍，一般为4人间，配备基本生活设施。宿舍分配在开学前在学校官网自主选择宿舍，到校后请按照指引前往相应宿舍楼办理入住手续。'
  },
  {
    title: '开学典礼',
    content: '开学典礼将在报到后第一周内举行，具体时间和地点将通过辅导员通知。请着装得体，准时到场。'
  }
])

// 常见问题
const faqs = ref([
  {
    question: '如何办理学生卡？',
    answer: '学生卡（校园一卡通）将在报到时发放。如有遗失，可前往学生服务中心办理补办手续，补办费用为20元。'
  },
  {
    question: '校内有哪些餐饮选择？',
    answer: '学校共有三个食堂，分布在校园不同区域，提供多样化的餐饮服务。此外，校内还有咖啡厅、便利店等餐饮设施。'
  },
  {
    question: '如何连接校园网？',
    answer: '教学区均覆盖WiFi信号。新生可在报到时申请校园网账号，按月缴费使用。'
  },
  {
    question: '学校周边有哪些生活设施？',
    answer: '学校周边有超市、自助银行、医务室等生活设施，可满足日常生活需求。校内也设有小型超市和生活用品店。'
  }
])
</script>

<template>
  <div class="grid grid-cols-[2fr,9fr] gap-8 font-sans">
    <!-- 左侧内容 -->
    <div class="left-[10%] top-[15%] fixed text-right pr-8 border-r border-gray-200 dark:border-gray-700">
      <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-200 mb-4">入学指南</h2>
      <div class="flex justify-end">
        <div class="flex flex-col space-y-2">
          <a href="#steps" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">入学流程</a>
          <a href="#materials" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">所需材料</a>
          <a href="#payment" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">缴费说明</a>
          <a href="#accommodation" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">住宿安排</a>
          <a href="#ceremony" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">开学典礼</a>
          <a href="#faq" class="px-4 py-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200">常见问题</a>
        </div>
      </div>
    </div>
<div></div>
    <!-- 右侧内容 -->
    <div class="pl-8">
      <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-6">新生入学指南</h1>
      
      <p class="text-lg text-gray-700 dark:text-gray-300 mb-8">
        欢迎加入安徽信息工程学院大家庭！为了帮助新生顺利完成入学手续，我们准备了以下入学指南，请仔细阅读。
      </p>
      
      <div class="space-y-10">
        <section v-for="(step, index) in admissionSteps" :key="index" :id="['steps', 'materials', 'payment', 'accommodation', 'ceremony'][index]" class="scroll-mt-20">
          <h2 class="text-2xl font-semibold text-gray-800 dark:text-gray-200 mb-4">{{ step.title }}</h2>
          <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <p class="text-gray-700 dark:text-gray-300 whitespace-pre-line">{{ step.content }}</p>
          </div>
        </section>
        
        <section id="faq" class="scroll-mt-20">
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