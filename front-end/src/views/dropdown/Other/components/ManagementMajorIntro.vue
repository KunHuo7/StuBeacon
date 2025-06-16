<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  major: {
    type: String,
    required: true
  }
});

// 图标获取方法
// 目标图标获取方法
const majorIntroductions = {
  '工商管理': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的管理学和经济学基础，良好的分析决策能力，较强的组织协调能力，具有战略思维和创新精神，能够应对复杂多变的商业环境。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及企业管理、人力资源管理、市场营销、战略管理、创新创业管理、企业运营优化、组织行为学等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事企业管理、人力资源、市场营销等工作，需要具备综合管理能力和商业敏感性，能够分析和解决企业经营管理中的实际问题，推动组织效能提升。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在各类企事业单位担任管理人员、决策分析师等岗位的复合型人才，具备管理理论与实践相结合的能力，能够适应现代企业管理需求。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '企业管理部门、咨询公司、人力资源服务机构、创业企业等，可从事行政管理、人力资源管理、运营管理、创业管理等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着经济发展和企业转型升级，对管理人才的需求持续增长。特别是具备数字化管理能力和跨文化管理经验的人才更受市场欢迎，就业领域广泛，职业发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：150学分\n专业必修课：70学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '管理学原理、经济学、会计学、市场营销、人力资源管理、组织行为学、战略管理、管理信息系统、运营管理、企业财务管理、创业管理、商业伦理'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习管理理论和实践，保持对商业环境变化的敏感性，掌握管理前沿趋势，构建系统化的管理知识体系，具备终身学习能力。'
      },
      {
        title: '学会做事',
        content: '能够综合运用管理知识解决实际问题，具备决策分析能力，能够有效规划、组织、领导和控制组织资源，推动组织目标实现。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通协调能力，能够理解不同利益相关者的需求，具有团队领导能力和冲突管理能力，能够在多元化环境中有效工作。'
      },
      {
        title: '学会做人',
        content: '具备商业伦理和社会责任感，诚信正直，尊重多元文化，关注企业可持续发展，推动企业履行社会责任，创造共享价值。'
      }
    ]
  },
  '国际经济与贸易': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的经济学和国际贸易理论基础，良好的跨文化沟通能力，较强的国际商务实践能力，能够适应全球化商业环境，具有国际视野。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及国际贸易实务、跨境电商、国际金融、国际市场营销、国际商法、国际物流、区域经济合作等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事国际贸易、外贸业务、跨境电商等工作，需要具备国际商务知识和实践能力，能够应对全球市场变化，开拓国际市场，处理国际商务关系。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在外贸企业、跨国公司担任国际商务人员、外贸专员等岗位的专业人才，具备国际贸易实务操作和国际市场开发的能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '外贸企业、跨国公司、跨境电商平台、国际物流企业等，可从事外贸业务、国际采购、国际市场开发、跨境电商运营等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着全球化深入发展和"一带一路"倡议推进，国际经贸合作不断深化，对国际经贸人才需求持续增长。特别是具备跨文化商务能力和数字贸易技能的人才更具就业竞争力。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：150学分\n专业必修课：70学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '国际经济学、国际贸易理论与实务、国际金融、国际市场营销、国际商法、WTO规则、跨境电子商务、国际结算、外贸函电、国际商务谈判、国际物流管理'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习国际经贸理论和实践，关注全球经济形势和贸易政策变化，掌握国际商务前沿趋势，构建系统化的国际经贸知识体系。'
      },
      {
        title: '学会做事',
        content: '能够独立完成国际贸易业务操作，具备国际市场分析和开发能力，熟悉国际贸易规则和实务流程，能够处理复杂的国际商务问题。'
      },
      {
        title: '学会共处',
        content: '具备跨文化沟通能力，能够在多元文化环境中有效工作，理解不同国家和地区的商业惯例和文化背景，建立良好的国际商务关系。'
      },
      {
        title: '学会做人',
        content: '具备国际商业伦理和责任意识，尊重多元文化，践行公平贸易原则，关注经济全球化背景下的可持续发展和普惠共赢。'
      }
    ]
  },
  '财务管理': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的财务会计理论基础，良好的财务分析能力，较强的资金管理能力，具有财务洞察力和风险意识，能够为组织财务决策提供支持。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及企业财务管理、财务分析与决策、投资理财、风险管理、资本运作、财务战略、管理会计、财务科技等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事财务分析、资金管理、投资决策等工作，需要具备专业知识和职业判断力，能够识别财务风险，优化资源配置，提高企业财务绩效。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在各类企事业单位担任财务主管、财务分析师等岗位的专业人才，具备财务管理、财务分析和投资决策的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '企业财务部门、金融机构、会计师事务所、咨询公司等，可从事财务管理、资金管理、财务分析、投资理财、财务咨询等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '作为企业运营的核心职能，财务管理人才需求稳定且持续增长。随着财务智能化转型和财务共享服务的推广，具备数字化财务能力和战略财务思维的人才更具市场竞争力。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：155学分\n专业必修课：75学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '会计学原理、财务管理、管理会计、财务分析、审计学、税法、投资学、公司金融、风险管理、财务决策、财务信息系统、财务战略'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习财务管理理论和实践，关注财务政策和规则变化，掌握财务科技发展趋势，构建系统化的财务知识体系，具备专业学习能力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成财务分析和资金管理，具备财务决策支持能力，能够运用财务分析工具和方法解决实际问题，提升组织财务绩效。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力，能够与组织各部门有效协作，理解业务与财务的关系，能够将财务信息转化为管理决策参考，在团队中发挥专业支持作用。'
      },
      {
        title: '学会做人',
        content: '具备职业操守和诚信品质，恪守财务伦理，严守财务纪律，关注财务合规与治理，践行负责任的财务管理理念，履行财务受托责任。'
      }
    ]
  },
  '市场营销': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的营销理论基础，良好的市场分析能力，较强的营销策划和执行能力，具有创新思维和客户导向，能够敏锐把握市场机会。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及市场调研、品牌管理、数字营销、消费者行为、营销策划、渠道管理、新媒体营销、服务营销等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事市场调研、品牌推广、产品营销等工作，需要具备市场洞察力和创新能力，能够分析消费者需求，制定营销策略，推动产品和服务的市场表现。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在各类企业担任市场专员、营销经理等岗位的应用型人才，具备市场开发、品牌管理和营销推广的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '企业市场部门、广告公司、咨询公司、电子商务企业等，可从事市场调研、品牌管理、产品营销、新媒体运营、市场策划等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '市场营销是企业经营的核心职能之一，人才需求持续旺盛。随着数字经济发展和消费升级，特别是具备数字营销能力和消费者洞察能力的人才就业前景广阔，职业发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：150学分\n专业必修课：70学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '市场营销学、消费者行为学、市场调研、品牌管理、营销策划、广告学、新媒体营销、服务营销、营销渠道管理、国际市场营销、数字营销、营销心理学'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习营销理论和实践，关注市场动态和消费趋势，掌握数字营销新方法，构建系统化的营销知识体系，具备市场洞察能力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成市场分析和营销策划，具备品牌推广和销售管理能力，能够运用营销工具和方法解决实际问题，提升产品市场竞争力。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够与内外部客户有效互动，理解不同背景人群的需求和期望，在营销团队中发挥创意和协调作用。'
      },
      {
        title: '学会做人',
        content: '具备营销伦理和社会责任感，践行诚信营销，尊重消费者权益，关注营销活动的社会影响，推动可持续消费和绿色营销理念。'
      }
    ]
  }
};

// 获取不同管理图标的方法
const getManagementIcon = (key) => {
  switch (key) {
    case 'basicQualities':
      return 'fas fa-user-tie';
    case 'professionalField':
      return 'fas fa-compass';
    case 'careerFeatures':
      return 'fas fa-briefcase';
    case 'talentPositioning':
      return 'fas fa-user-graduate';
    case 'employmentDirection':
      return 'fas fa-building';
    case 'industryProspects':
      return 'fas fa-chart-line';
    case 'creditRequirements':
      return 'fas fa-graduation-cap';
    case 'mainCourses':
      return 'fas fa-book';
    default:
      return 'fas fa-chart-pie';
  }
};
</script>

<template>
  <div class="management-major-intro">
    <template v-if="majorIntroductions[major]">
      <!-- 管理学院标题 -->
      <div class="management-header">
        <div class="chart-decoration left">
          <svg class="chart-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M5 9.2h3V19H5V9.2zM10.6 5h2.8v14h-2.8V5zm5.6 8H19v6h-2.8v-6z"/>
          </svg>
        </div>
        <h2 class="major-title">{{ major }}</h2>
        <div class="chart-decoration right">
          <svg class="chart-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M3.5 18.49l6-6.01 4 4L22 6.92l-1.41-1.41-7.09 7.97-4-4L2 16.99z"/>
          </svg>
        </div>
      </div>

      <!-- 基本信息部分 - 商务卡片布局 -->
      <div class="management-grid">
        <div v-for="(section, key) in ['basicQualities', 'professionalField', 'careerFeatures', 'talentPositioning', 'employmentDirection', 'industryProspects', 'creditRequirements', 'mainCourses']" 
             :key="key"
             :class="['management-card', `card-${key % 4}`]">
          <div class="card-header">
            <div class="mgmt-icon">
              <i :class="getManagementIcon(section)"></i>
            </div>
            <h3 class="section-title">{{ majorIntroductions[major][section].title }}</h3>
          </div>
          <div class="card-body">
            <p class="section-content">{{ majorIntroductions[major][section].content }}</p>
          </div>
          <div class="card-glow"></div>
        </div>
      </div>

      <!-- 发展目标部分 - 商务报表效果 -->
      <div class="goals-section">
        <div class="canvas-header">
          <div class="canvas-line"></div>
          <h3 class="goals-title">发展目标</h3>
          <div class="canvas-line"></div>
        </div>
        
        <div class="canvas-grid">
          <div v-for="(goal, index) in majorIntroductions[major].developmentGoals" 
               :key="index"
               class="canvas-cell">
            <div class="cell-header">
              <div class="cell-number">0{{ index + 1 }}</div>
              <h4 class="goal-title">{{ goal.title }}</h4>
            </div>
            <p class="goal-content">{{ goal.content }}</p>
            <div class="brush-decoration"></div>
          </div>
        </div>
      </div>
    </template>
    <div v-else class="no-data">
      暂无该专业的详细介绍
    </div>
  </div>
</template>

<style scoped>
.management-major-intro {
  @apply space-y-8 relative overflow-hidden;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.dark .management-major-intro {
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

/* 管理学院标题样式 */
.management-header {
  @apply flex items-center justify-center mb-8 relative;
}

.major-title {
  @apply text-3xl font-bold text-gray-800 dark:text-gray-200 px-8 py-2 relative z-10;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.dark .major-title {
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.chart-decoration {
  @apply relative h-10 w-10 flex-shrink-0;
}

.chart-icon {
  @apply h-full w-full;
  fill: #0ea5e9;
  opacity: 0.8;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.1));
  transition: all 0.3s ease;
}

.dark .chart-icon {
  fill: #38bdf8;
  opacity: 0.9;
  filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.3));
}

.chart-decoration:hover .chart-icon {
  fill: #0284c7;
  transform: scale(1.1);
}

.dark .chart-decoration:hover .chart-icon {
  fill: #7dd3fc;
}

/* 商务卡片布局样式 */
.management-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
}

.management-card {
  @apply bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden transition-all duration-300;
  border: 1px solid rgba(226, 232, 240, 0.8);
  transform: translateY(0);
}

.dark .management-card {
  border: 1px solid rgba(30, 41, 59, 0.8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.management-card:hover {
  @apply shadow-lg;
  transform: translateY(-5px);
  border-color: #0ea5e9;
}

.dark .management-card:hover {
  border-color: #38bdf8;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

.card-header {
  @apply flex items-center p-4 bg-gradient-to-r from-gray-50 to-gray-100 dark:from-gray-800 dark:to-gray-700 relative;
  border-bottom: 1px solid rgba(226, 232, 240, 0.8);
}

.dark .card-header {
  border-bottom: 1px solid rgba(30, 41, 59, 0.8);
}

.mgmt-icon {
  @apply p-2 bg-sky-500 dark:bg-sky-600 rounded-full text-white mr-3;
  box-shadow: 0 2px 5px rgba(14, 165, 233, 0.3);
}

.dark .mgmt-icon {
  box-shadow: 0 2px 8px rgba(14, 165, 233, 0.5);
}

.section-title {
  @apply text-lg font-bold text-gray-700 dark:text-gray-200;
}

.card-body {
  @apply p-4;
}

.section-content {
  @apply text-gray-600 dark:text-gray-300 leading-relaxed;
}

/* 发展目标 - 商务报表效果 */
.goals-section {
  @apply mt-12 relative;
}

.canvas-header {
  @apply flex items-center justify-center mb-8;
}

.goals-title {
  @apply text-2xl font-bold text-purple-700 dark:text-purple-400 px-6;
}

.canvas-line {
  @apply h-1 w-24 relative overflow-hidden;
  background: linear-gradient(90deg, #6a11cb 0%, #2575fc 100%);
  border-radius: 4px;
}

.canvas-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
}

.canvas-cell {
  @apply bg-white dark:bg-gray-800 rounded-lg p-6 shadow-md;
}

.cell-header {
  @apply flex items-center space-x-4 mb-4;
}

.cell-number {
  @apply text-2xl font-bold text-purple-600 dark:text-purple-400 flex-shrink-0;
}

.goal-title {
  @apply text-xl font-semibold text-gray-800 dark:text-gray-200;
}

.goal-content {
  @apply text-gray-600 dark:text-gray-300 leading-relaxed;
}

.brush-decoration {
  @apply mt-4 h-1 w-full relative overflow-hidden;
  background: linear-gradient(90deg, #6a11cb 0%, #2575fc 100%);
  border-radius: 4px;
}

.no-data {
  @apply text-center text-gray-500 dark:text-gray-400 py-8;
}

/* 动画效果 */
@keyframes progress-animation {
  0% {
    width: 0;
  }
  100% {
    width: 85%;
  }
}

</style>