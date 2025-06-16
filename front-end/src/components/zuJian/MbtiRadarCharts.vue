<template>
  <div>
    <LeiDa6 
      :data="mbtiData.data" 
      :skills="mbtiData.skills" 
      :themeColor="mbtiData.color" 
    />
    <div class="text-center mt-4 text-sm text-gray-500 dark:text-gray-400">
      {{ mbtiData.description }}
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import LeiDa6 from "@/components/zuJian/LeiDa6.vue";

const props = defineProps({
  mbtiType: {
    type: String,
    default: ''
  }
});

const mbtiData = computed(() => {
  switch (props.mbtiType) {
    case 'INTJ':
      return {
        data: [90, 65, 70, 95, 85, 60],
        skills: ['分析能力', '策略思维', '独立性', '逻辑思考', '创新能力', '社交技能'],
        color: '#1E40AF', // 深蓝色
        description: 'INTJ - 建筑师：擅长系统思考和战略规划，追求卓越和独立'
      };
    case 'INTP':
      return {
        data: [95, 60, 65, 90, 80, 55],
        skills: ['逻辑分析', '概念思维', '问题解决', '理论探索', '创新能力', '情感表达'],
        color: '#7C3AED', // 紫色
        description: 'INTP - 逻辑学家：追求逻辑和理性，善于分析复杂系统和理论'
      };
    case 'ENTJ':
      return {
        data: [85, 80, 75, 90, 80, 75],
        skills: ['领导能力', '决策力', '战略思维', '效率规划', '组织能力', '情感共鸣'],
        color: '#B91C1C', // 红色
        description: 'ENTJ - 指挥官：天生的领导者，务实高效，长于战略规划和执行'
      };
    case 'ENTP':
      return {
        data: [80, 85, 70, 90, 95, 75],
        skills: ['创新思维', '辩论能力', '适应性', '逻辑分析', '创意生成', '情感稳定性'],
        color: '#9D174D', // 洋红色
        description: 'ENTP - 辩论家：充满创造力和挑战精神，热爱思想交锋和创新'
      };
    case 'INFJ':
      return {
        data: [75, 80, 85, 70, 85, 90],
        skills: ['洞察力', '同理心', '创造性', '价值判断', '文字表达', '人际理解'],
        color: '#4338CA', // 靛蓝色
        description: 'INFJ - 提倡者：理想主义者，有强烈的道德感和使命感，关注他人成长'
      };
    case 'INFP':
      return {
        data: [70, 75, 80, 65, 90, 85],
        skills: ['创造力', '情感深度', '理想主义', '适应能力', '价值坚守', '写作能力'],
        color: '#065F46', // 绿色
        description: 'INFP - 调停者：理想主义和和平使者，重视个人价值和内心和谐'
      };
    case 'ENFJ':
      return {
        data: [75, 95, 90, 80, 85, 85],
        skills: ['人际影响', '沟通能力', '团队协作', '组织能力', '同理心', '讲故事能力'],
        color: '#047857', // 翠绿色
        description: 'ENFJ - 主人公：天生的领导者和激励者，关注他人成长和团队和谐'
      };
    case 'ENFP':
      return {
        data: [70, 90, 85, 75, 95, 80],
        skills: ['创造力', '社交能力', '情感表达', '灵活性', '激励能力', '适应能力'],
        color: '#F59E0B', // 琥珀色
        description: 'ENFP - 竞选者：热情洋溢，富有同情心和想象力，乐于尝试新事物'
      };
    case 'ISTJ':
      return {
        data: [80, 60, 75, 90, 60, 85],
        skills: ['细节关注', '组织能力', '责任心', '逻辑思考', '规则遵循', '实践能力'],
        color: '#1F2937', // 深灰色
        description: 'ISTJ - 物流师：实际、务实的组织者，注重责任和传统，可靠而精确'
      };
    case 'ISFJ':
      return {
        data: [75, 70, 85, 80, 65, 90],
        skills: ['责任感', '细节关注', '记忆力', '实用性', '同理心', '组织能力'],
        color: '#78350F', // 褐色
        description: 'ISFJ - 守卫者：尽职尽责的保护者，重视传统和家庭，注重服务他人'
      };
    case 'ESTJ':
      return {
        data: [75, 80, 85, 90, 60, 75],
        skills: ['组织能力', '管理技能', '实践性', '决断力', '责任感', '精确性'],
        color: '#064E3B', // 暗绿色
        description: 'ESTJ - 总经理：务实高效的管理者，注重秩序和结构，责任感强'
      };
    case 'ESFJ':
      return {
        data: [70, 90, 95, 75, 65, 80],
        skills: ['社交能力', '合作性', '组织能力', '责任感', '服务精神', '实用性'],
        color: '#5B21B6', // 淡紫色
        description: 'ESFJ - 执政官：热心肠的合作者，重视和谐与传统，乐于帮助他人'
      };
    case 'ISTP':
      return {
        data: [85, 60, 65, 95, 75, 80],
        skills: ['机械能力', '问题解决', '分析技能', '冷静处事', '适应性', '实践能力'],
        color: '#374151', // 石板灰
        description: 'ISTP - 鉴赏家：灵活的问题解决者，擅长分析和实践，注重效率'
      };
    case 'ISFP':
      return {
        data: [65, 70, 75, 60, 95, 80],
        skills: ['艺术感知', '审美能力', '情感敏感度', '和平主义', '适应性', '实用创造力'],
        color: '#BE185D', // 品红色
        description: 'ISFP - 探险家：自由灵魂的艺术家，感性而和平，享受当下的体验'
      };
    case 'ESTP':
      return {
        data: [75, 85, 70, 90, 80, 75],
        skills: ['冒险精神', '社交技能', '实用能力', '问题解决', '适应性', '机智反应'],
        color: '#B45309', // 棕色
        description: 'ESTP - 企业家：充满活力的冒险家，灵活而务实，善于解决危机'
      };
    case 'ESFP':
      return {
        data: [65, 90, 85, 60, 80, 75],
        skills: ['表演能力', '社交技能', '乐观精神', '实用性', '感官体验', '即兴应变'],
        color: '#D97706', // 金色
        description: 'ESFP - 表演者：自然的表演者和娱乐者，热情洋溢，享受生活'
      };
    default:
      return {
        data: [75, 75, 75, 75, 75, 75],
        skills: ['专业知识', '沟通能力', '团队协作', '问题解决', '创新思维', '学习能力'],
        color: '#3B82F6', // 默认蓝色
        description: '请在个人信息中选择您的MBTI类型'
      };
  }
});

// 监听MBTI类型变化
watch(() => props.mbtiType, (newValue) => {
  console.log('MBTI类型已更新:', newValue);
});

onMounted(() => {
  console.log('MbtiRadarCharts组件已挂载，当前MBTI类型:', props.mbtiType);
});
</script>

<style scoped>
</style> 