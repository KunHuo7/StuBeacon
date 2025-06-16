<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  major: {
    type: String,
    required: true
  }
});

const majorIntroductions = {
  '电气工程及其自动化': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的电气工程基础知识，良好的数理分析能力，较强的工程实践和创新能力，能够系统地分析和解决电气工程领域的复杂问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及电力系统、电力电子技术、电气自动化、新能源技术、智能电网、电机驱动与控制等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事电气系统设计、自动化控制、电力运维等工作，需要具备专业技能和创新思维，能够运用现代工程工具解决实际问题，确保电气系统安全可靠运行。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在电力、工业等领域担任工程师、技术主管等岗位的专业人才，具备电气工程设计、调试、运维的综合能力，能够适应电气工程领域的技术发展。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '电力公司、电气设备制造企业、工业自动化企业、新能源企业、设计院所等，可从事电气工程设计、自动化系统开发、电力系统运维、电气设备研发等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着国家能源转型和智能电网建设的推进，电气工程人才需求稳定且持续增长。新能源、智能电网等领域的快速发展为电气专业毕业生提供了广阔的就业空间和发展机会。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '电路理论、电机学、电力电子技术、自动控制原理、电力系统分析、高电压技术、电气控制与PLC、电气设备及系统、微电网技术、电力系统继电保护、新能源发电技术、智能电网技术'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习电气工程新技术，掌握行业发展趋势，具备终身学习的意识和能力，能够跟踪行业前沿并应用于工程实践。'
      },
      {
        title: '学会做事',
        content: '能够独立完成电气工程项目，具备实践创新能力，能够运用专业知识解决复杂工程问题，具有质量、安全和效率意识。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够有效沟通和表达，理解工程团队中不同角色的职责，能够在多学科团队中发挥专业作用。'
      },
      {
        title: '学会做人',
        content: '具备工程伦理和职业道德，注重安全和环保，理解工程决策与社会责任的关系，具有可持续发展意识。'
      }
    ]
  },
  '自动化': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的自动控制理论基础，良好的系统分析能力，较强的工程实践和创新能力，能够熟练运用现代控制技术解决工业和生产自动化问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及工业自动化、智能控制、过程控制、机器人技术、运动控制、生产线自动化、工业互联网等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事自动化系统设计、智能控制、工业机器人等工作，需要具备跨学科知识和系统思维，能够将控制理论应用于实际系统，实现工业生产的自动化和智能化。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在工业自动化、智能制造等领域担任控制工程师、系统集成师等岗位的复合型人才，具备自动化系统全生命周期的设计、实施和优化能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '自动化设备制造企业、工业企业、系统集成商、智能制造企业等，可从事自动化系统设计、控制系统开发、工业机器人应用、生产线优化等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着智能制造和工业4.0的推进，自动化技术在制造业的应用不断深入，自动化专业人才需求旺盛。工业机器人、工业互联网等新兴领域的发展为专业人才提供了广阔的发展空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：162学分\n专业必修课：82学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '自动控制原理、过程控制、运动控制系统、检测与转换技术、PLC与组态控制、工业机器人技术、现代控制理论、计算机控制技术、智能控制系统、嵌入式系统、工业通信网络'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习自动化新技术，掌握人工智能与自动控制融合趋势，追踪国际前沿技术发展，建立系统化的专业知识结构，具备跨学科学习能力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成自动化系统设计，具备工程实践和创新能力，掌握现代工程工具，能够应对复杂控制系统的分析、设计和优化任务。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够跨专业沟通和协作，理解多学科团队的工作方式，在系统集成和项目实施中协调各方资源。'
      },
      {
        title: '学会做人',
        content: '具备工程伦理和职业道德，注重系统安全和可靠性，理解自动化技术对生产效率和社会发展的影响，关注可持续发展问题。'
      }
    ]
  },
  '电子信息工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的电子技术基础，良好的系统分析能力，较强的创新实践能力，能够应用现代电子信息技术解决工程问题，具有信息系统的设计与开发能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及集成电路、信息处理、嵌入式系统、物联网、通信技术、智能感知、消费电子等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事电子产品设计、系统开发、技术研发等工作，需要具备专业知识和创新能力，能够应对电子信息产业快速迭代的技术挑战，实现从概念到产品的转化。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在电子信息领域担任研发工程师、技术专家等岗位的创新人才，具备电子系统和信息系统的设计、开发和维护能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '电子设备制造企业、通信企业、集成电路企业、消费电子企业等，可从事电子产品研发、嵌入式系统开发、集成电路设计、通信系统开发等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '电子信息产业是国家重点发展的战略性产业，市场需求持续增长。随着5G、物联网、人工智能等技术的发展，电子信息工程专业人才就业前景广阔，薪资水平较高。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '模拟电子技术、数字电子技术、信号与系统、通信原理、数字信号处理、嵌入式系统、集成电路设计、微波技术、电磁场与电磁波、物联网技术、电子系统设计'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习电子信息新技术，掌握行业前沿动态，具备自主学习和创新思维能力，能够在技术快速更新的环境中保持专业竞争力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成电子系统设计，具备工程实践能力，掌握电子设计自动化工具，能够将理论知识转化为产品和解决方案。'
      },
      {
        title: '学会共处',
        content: '具备团队合作精神，能够跨专业协作，理解产品设计中的不同环节和角色，具有有效的技术沟通和表达能力。'
      },
      {
        title: '学会做人',
        content: '具备创新精神和职业操守，注重知识产权，关注电子产品的环保和安全问题，理解技术创新对社会发展的影响。'
      }
    ]
  },
  '通信工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的通信理论基础，良好的系统分析能力，较强的工程实践能力，能够设计、优化和维护各类通信系统，适应通信技术的快速发展。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及移动通信、光通信、物联网、无线网络、卫星通信、通信信号处理、通信安全等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事通信系统设计、网络规划、技术研发等工作，需要具备专业技能和创新思维，能够应对5G、6G等新一代通信技术的挑战，确保通信系统的性能和可靠性。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在通信行业担任技术工程师、项目经理等岗位的专业人才，具备通信系统规划、设计、实施和优化的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '通信运营商、通信设备制造商、系统集成商、互联网企业等，可从事通信系统设计、网络优化、通信设备研发、系统集成等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着5G商用和6G研发的推进，通信行业进入新一轮发展周期，人才需求旺盛。物联网、工业互联网等新兴领域的发展也为通信工程专业毕业生提供了广阔的就业空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '通信原理、信号与系统、数字信号处理、移动通信、光纤通信、无线通信网络、天线与电波传播、通信电子电路、通信网络与协议、通信系统仿真、物联网技术'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习通信新技术，掌握行业发展方向，跟踪国际前沿技术，构建系统化的通信专业知识体系，具备技术预见性和创新思维。'
      },
      {
        title: '学会做事',
        content: '能够独立完成通信系统设计，具备工程实践能力，熟练运用通信系统分析和设计工具，能够有效解决通信工程中的实际问题。'
      },
      {
        title: '学会共处',
        content: '具备团队协作能力，能够跨部门沟通，理解通信项目中不同角色的职责，能够在多元化团队中有效工作和表达专业意见。'
      },
      {
        title: '学会做人',
        content: '具备职业道德和责任意识，注重通信安全，理解信息传输的隐私和保密要求，关注通信技术对社会发展的影响。'
      }
    ]
  },
  '机器人工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的机器人技术基础，良好的系统设计能力，较强的创新实践能力，能够综合运用机械、电子、控制、计算机等多学科知识解决机器人工程问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及工业机器人、服务机器人、智能控制、人机交互、机器视觉、机器人导航、协作机器人等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事机器人研发、系统集成、智能控制等工作，需要具备跨学科知识和创新能力，能够设计和开发适应不同应用场景的机器人系统，推动机器人技术的产业化应用。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在机器人领域担任研发工程师、项目经理等岗位的创新人才，具备机器人系统设计、开发、集成和应用的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '机器人制造企业、智能装备企业、自动化系统集成商、科研院所等，可从事机器人设计、控制系统开发、算法研究、应用开发等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着人口老龄化和劳动力成本上升，机器人应用需求持续增长。国家政策大力支持智能制造和机器人产业发展，机器人工程专业人才就业前景广阔，发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '机器人学导论、机械设计、机器人控制系统、传感器技术、机器视觉、人工智能、机器人运动学与动力学、机器人操作系统、嵌入式系统设计、人机交互技术'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习机器人新技术，掌握人工智能发展趋势，构建跨学科知识体系，关注机器人领域的国际前沿研究，具备持续创新能力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成机器人系统开发，具备创新实践能力，熟练运用设计工具和开发平台，能够将机器人技术应用于解决实际问题。'
      },
      {
        title: '学会共处',
        content: '具备团队协作能力，能够跨学科合作，理解机器人系统集成中的不同专业角色，具有有效的项目协调和沟通能力。'
      },
      {
        title: '学会做人',
        content: '具备创新精神和职业操守，注重技术伦理，关注机器人技术对就业和社会的影响，推动机器人技术的负责任发展和应用。'
      }
    ]
  }
};

// 获取不同技术图标的方法
const getTechIcon = (key) => {
  switch (key) {
    case 'basicQualities':
      return 'fas fa-bolt';
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
      return 'fas fa-plug';
  }
};
</script>

<template>
  <div class="electrical-major-intro">
    <template v-if="majorIntroductions[major]">
      <!-- 电气学院标题 -->
      <div class="electrical-header">
        <div class="circuit-decoration left"></div>
        <h2 class="major-title">{{ major }}</h2>
        <div class="circuit-decoration right"></div>
      </div>

      <!-- 基本信息部分 - 电气卡片布局 -->
      <div class="electrical-grid">
        <div v-for="(section, key) in ['basicQualities', 'professionalField', 'careerFeatures', 'talentPositioning', 'employmentDirection', 'industryProspects', 'creditRequirements', 'mainCourses']" 
             :key="key"
             :class="['electrical-card', `card-${key % 4}`]">
          <div class="card-header">
            <div class="tech-icon">
              <i :class="getTechIcon(section)"></i>
            </div>
            <h3 class="section-title">{{ majorIntroductions[major][section].title }}</h3>
          </div>
          <div class="card-body">
            <p class="section-content">{{ majorIntroductions[major][section].content }}</p>
          </div>
          <div class="card-glow"></div>
        </div>
      </div>

      <!-- 发展目标部分 - 电路板效果 -->
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
.electrical-major-intro {
  @apply space-y-8 relative overflow-hidden;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f0fd 100%);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 100, 255, 0.1);
}

.dark .electrical-major-intro {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

/* 电气学院标题样式 */
.electrical-header {
  @apply flex items-center justify-center mb-8 relative;
}

.major-title {
  @apply text-3xl font-bold text-blue-700 dark:text-blue-400 px-8 py-2 relative z-10;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.circuit-decoration {
  @apply h-0.5 flex-grow relative overflow-hidden;
  background: linear-gradient(90deg, transparent, #4285f4, transparent);
}

.dark .circuit-decoration {
  background: linear-gradient(90deg, transparent, #60a5fa, transparent);
}

.circuit-decoration::before {
  content: '';
  @apply absolute top-0 h-full w-full;
  background: repeating-linear-gradient(90deg, transparent, transparent 10px, #4285f4 10px, #4285f4 12px);
  animation: circuit-pulse 3s infinite linear;
}

.dark .circuit-decoration::before {
  background: repeating-linear-gradient(90deg, transparent, transparent 10px, #60a5fa 10px, #60a5fa 12px);
}

.circuit-decoration.left::before {
  animation-delay: 0.5s;
}

/* 电路板布局样式 */
.electrical-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%234285f420' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%234285f410' stroke-width='0.5'/%3E%3C/svg%3E");
  padding: 1rem;
}

.dark .electrical-grid {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%2360a5fa20' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%2360a5fa10' stroke-width='0.5'/%3E%3C/svg%3E");
}

.electrical-card {
  @apply bg-white dark:bg-gray-800 rounded-xl shadow-md overflow-hidden transition-all duration-300;
  border: 1px solid rgba(66, 133, 244, 0.2);
  transform: translateY(0);
}

.dark .electrical-card {
  border: 1px solid rgba(96, 165, 250, 0.2);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.electrical-card:hover {
  @apply shadow-lg;
  transform: translateY(-5px);
  border-color: rgba(66, 133, 244, 0.5);
}

.dark .electrical-card:hover {
  border-color: rgba(96, 165, 250, 0.5);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

.card-header {
  @apply flex items-center p-4 bg-gradient-to-r from-blue-500 to-blue-600 dark:from-cyan-900 dark:to-blue-950 text-white relative;
}

.tech-icon {
  @apply p-2 bg-white dark:bg-gray-700 rounded-full text-blue-600 dark:text-blue-400 mr-3;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.section-title {
  @apply text-lg font-bold;
}

.card-body {
  @apply p-4;
}

.section-content {
  @apply text-gray-700 dark:text-gray-300 leading-relaxed;
}

/* 发展目标 - 电能流动效果 */
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
@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(66, 133, 244, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(66, 133, 244, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(66, 133, 244, 0);
  }
}

@keyframes circuit-pulse {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}
</style>