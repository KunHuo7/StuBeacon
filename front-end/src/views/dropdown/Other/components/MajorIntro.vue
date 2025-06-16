<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  major: {
    type: String,
    required: true
  }
});

// 专业介绍数据
const majorIntroductions = {
  '计算机科学与技术': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的数学基础和计算机理论知识，良好的逻辑思维能力和创新意识，较强的编程能力和问题解决能力，对新技术有较强的学习和适应能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及计算机系统与结构、软件开发、人工智能、数据科学、网络技术、信息安全、嵌入式系统、云计算与分布式系统等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事软件开发、系统架构、算法研究、技术管理等工作，需要持续学习新技术，具备团队协作能力，能够应对快速变化的技术环境和复杂的项目需求。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在IT行业担任核心技术人员、项目经理、技术架构师、研发工程师等岗位的高素质人才，具备扎实的理论基础和实践能力，能够解决计算机领域的复杂问题和技术挑战。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '互联网公司、IT企业、金融机构、科研院所、政府部门等，可从事软件开发、系统维护、技术支持、算法工程师、数据分析师、安全工程师等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着人工智能、大数据、云计算等技术的发展，计算机科学与技术专业人才需求旺盛，就业前景广阔。该专业毕业生在国内外IT领域就业竞争力强，薪资水平较高，有较大的职业发展空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '程序设计基础、数据结构、计算机组成原理、操作系统、计算机网络、数据库系统、软件工程、算法设计与分析、人工智能导论、编译原理、计算机图形学、信息安全技术'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '具备持续学习的能力，能够跟踪计算机领域的最新发展，掌握新技术和新工具，保持对前沿技术的敏感性和探索精神。'
      },
      {
        title: '学会做事',
        content: '能够独立完成软件项目的设计、开发和维护，具备问题分析和解决能力，能够应用所学知识解决实际工程问题，具备创新思维和实践能力。'
      },
      {
        title: '学会共处',
        content: '具备良好的团队协作能力，能够有效沟通和表达，参与团队项目开发，理解团队协作的价值，能够在多元文化环境中有效工作。'
      },
      {
        title: '学会做人',
        content: '具备职业道德和社会责任感，关注技术对社会的影响，遵守职业规范，秉持诚信、尊重知识产权，关注技术发展的伦理问题。'
      }
    ]
  },
  '软件工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备系统的软件工程理论知识，良好的项目管理能力，较强的软件设计和开发能力，以及团队协作和沟通能力，对软件质量和用户体验有较高的重视。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及软件开发方法学、软件项目管理、软件质量保证与测试、软件架构设计、DevOps、移动应用开发、Web全栈开发等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事软件项目开发、项目管理、质量保证、技术咨询等工作，需要具备全面的软件工程知识，能够应对复杂项目的挑战，同时关注用户需求和市场变化。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在软件行业担任高级开发工程师、项目经理、质量管理师、架构师等岗位的专业人才，具备从需求分析到系统交付的全流程能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '软件公司、互联网企业、IT服务机构、大型企事业单位的IT部门等，可从事软件开发、测试、项目管理、技术支持、软件咨询等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着数字化转型的深入，各行各业对软件系统的需求持续增长，软件工程专业人才需求旺盛。国家战略对软件产业的重视也为该专业毕业生提供了良好的发展环境和政策支持。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：158学分\n专业必修课：78学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '程序设计、面向对象编程、软件工程导论、需求分析与设计、软件测试、软件体系结构、项目管理、数据库系统、软件质量保证、软件过程改进、Web应用开发、移动应用开发'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '掌握软件工程最新理论和实践，具备技术创新能力，能够追踪行业发展趋势，不断更新知识结构，适应软件技术的快速迭代。'
      },
      {
        title: '学会做事',
        content: '能够独立承担软件项目的分析、设计和实现，具备项目管理能力，能够应用适当的方法和工具解决复杂工程问题，关注产品质量和用户体验。'
      },
      {
        title: '学会共处',
        content: '具备团队领导能力，能够协调各方资源，推进项目完成，理解跨职能团队合作的重要性，具有有效的沟通能力和冲突解决能力。'
      },
      {
        title: '学会做人',
        content: '具备职业素养和责任意识，注重用户体验和社会价值，尊重知识产权，遵守行业规范，考虑软件系统对社会、环境的影响。'
      }
    ]
  },
  '网络工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的网络技术基础，良好的网络规划和管理能力，较强的问题诊断能力，以及安全意识和系统思维能力，对新型网络技术有较强的学习能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及网络规划与设计、网络安全、云计算、物联网、SDN/NFV、边缘计算、5G通信等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事网络工程师、安全工程师、运维工程师等工作，需要具备实践经验和故障排除能力，能够设计和维护复杂网络环境，保障网络的安全性、可靠性和性能。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在IT领域担任网络架构师、安全专家、运维经理、云计算工程师等岗位的技术人才，具备网络系统全生命周期的规划、实施和优化能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '通信运营商、IT服务企业、互联网企业、系统集成商、大型企事业单位的网络部门等，可从事网络规划、安全管理、系统运维、技术支持等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着云计算、5G、物联网等技术的发展，网络基础设施建设需求巨大，网络工程专业人才市场需求旺盛。网络安全领域的快速发展也为专业人才提供了广阔的就业空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：155学分\n专业必修课：75学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '计算机网络、网络协议分析、网络安全技术、网络工程设计、云计算技术、物联网技术、网络编程、路由交换技术、网络管理与维护、无线网络、网络存储技术'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习网络新技术和安全知识，具备技术钻研能力，掌握行业发展趋势，对新兴网络技术有敏锐的洞察力和学习热情。'
      },
      {
        title: '学会做事',
        content: '能够独立完成网络规划、部署和运维，具备问题解决能力，在复杂环境中快速定位和解决网络故障，确保网络系统的稳定运行。'
      },
      {
        title: '学会共处',
        content: '具备良好的服务意识和沟通能力，能够配合团队工作，有效对接业务需求和技术实现，在多部门协作中发挥桥梁作用。'
      },
      {
        title: '学会做人',
        content: '具备安全意识和职业操守，保护用户数据和隐私，遵循网络伦理规范，担负网络安全责任，推动互联网健康发展。'
      }
    ]
  },
  '人工智能': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的数学基础(包括线性代数、概率论、统计学)和机器学习理论，较强的算法设计和实现能力，以及良好的计算思维和创新能力，对AI伦理有深刻认识。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及机器学习、深度学习、计算机视觉、自然语言处理、知识图谱、智能机器人、强化学习、生成式AI等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事算法工程师、研究员、AI产品经理等工作，需要具备创新思维和实践能力，能够将先进算法应用到实际场景，解决复杂问题，推动AI技术落地。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在AI领域担任算法专家、研究主管、技术总监等岗位的创新人才，具备理论研究与工程实现并重的能力，能够引领AI技术创新。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '互联网巨头、AI创新企业、科技研究机构、高校、智能制造企业等，可从事算法研发、产品开发、技术研究、科学研究等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '人工智能作为新一轮科技革命和产业变革的核心驱动力，市场规模快速增长，国家战略重点支持，人才需求旺盛。AI技术正在各行各业广泛渗透，为专业人才提供了广阔的发展空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：162学分\n专业必修课：82学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '高等数学、线性代数、概率论与数理统计、机器学习、深度学习、计算机视觉、自然语言处理、智能系统、数据挖掘、人工智能伦理、强化学习、生成式AI'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续跟踪AI前沿技术，具备科研创新能力，保持对学术和技术前沿的敏感性，熟悉前沿论文和开源项目，具备自主学习和钻研的能力。'
      },
      {
        title: '学会做事',
        content: '能够独立开展算法研究和应用开发，具备工程实践能力，能将理论模型转化为实际系统，解决实际问题，推动AI技术落地。'
      },
      {
        title: '学会共处',
        content: '具备跨学科合作能力，能够与不同领域专家协作，理解业务需求与技术实现的结合点，促进AI技术与各行业的深度融合。'
      },
      {
        title: '学会做人',
        content: '具备科研道德和社会责任感，关注AI伦理问题，考虑技术发展的社会影响，推动AI技术向善，为人类社会创造价值。'
      }
    ]
  },
  '数据科学与大数据技术': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的统计学和数据分析能力，良好的数据处理和可视化能力，以及跨学科思维和业务理解能力，对数据价值有深刻认识。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及数据挖掘、数据分析、商业智能、数据可视化、数据工程、大数据架构、机器学习应用等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事数据分析师、数据工程师、商业分析师等工作，需要具备业务理解和数据洞察能力，能够从海量数据中提取有价值的信息，支持决策制定。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在数据领域担任数据科学家、分析主管、决策顾问等岗位的复合型人才，具备数据处理、分析建模和业务应用的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '互联网企业、金融机构、咨询公司、电子商务、医疗健康、政府部门等，可从事数据分析、数据挖掘、商业智能、决策支持等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着数据量的爆炸性增长和数据驱动决策理念的普及，数据科学人才需求持续增长。各行各业对数据分析和大数据应用的重视程度不断提高，为专业人才提供了广阔的就业前景。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '概率与统计、数据结构与算法、数据库系统、数据挖掘、机器学习、大数据处理技术、数据可视化、统计分析、商业智能、Python数据分析、分布式计算、数据仓库'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习数据科学新方法和工具，具备数据分析能力，关注行业新技术和方法论的发展，保持对数据领域前沿的敏感性。'
      },
      {
        title: '学会做事',
        content: '能够独立完成数据分析项目，具备问题解决能力，能够设计合适的数据分析流程，从原始数据到有价值的洞察，支持业务决策。'
      },
      {
        title: '学会共处',
        content: '具备团队协作能力，能够与业务部门有效沟通，理解业务需求，将数据分析结果转化为业务价值，在跨部门合作中发挥桥梁作用。'
      },
      {
        title: '学会做人',
        content: '具备数据伦理意识，保护数据安全和隐私，践行负责任的数据使用原则，考虑数据分析结果的社会影响，推动数据的合理应用。'
      }
    ]
  }
};

// 获取不同技术图标的方法
const getTechIcon = (key) => {
  switch (key) {
    case 'basicQualities':
      return 'fas fa-brain';
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
      return 'fas fa-star';
  }
};
</script>

<template>
  <div class="major-intro">
    <template v-if="majorIntroductions[major]">
      <!-- 计算机学院标题 -->
      <div class="tech-header">
        <div class="circuit-decoration left">
          <svg class="circuit-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M6,16A2,2 0 0,1 4,14C4,13.62 4.1,13.27 4.29,12.97L6.11,15.81C6.07,15.87 6.05,15.93 6,16M18,16A2,2 0 0,0 20,14C20,13.62 19.9,13.27 19.71,12.97L17.89,15.81C17.93,15.87 17.95,15.93 18,16M4,8H8V4H20V16.17C20.5,16.34 20.92,16.63 21.25,17L22,17V3C22,2.45 21.55,2 21,2H3C2.45,2 2,2.45 2,3V17L2.75,17C3.08,16.63 3.5,16.34 4,16.17V8M10,4H16V8H10V4M10,10H16V14H10V10M16,22C16.56,22 17,21.55 17,21C17,20.45 16.56,20 16,20C15.44,20 15,20.45 15,21C15,21.55 15.44,22 16,22M8,22C8.56,22 9,21.55 9,21C9,20.45 8.56,20 8,20C7.44,20 7,20.45 7,21C7,21.55 7.44,22 8,22M12,22C12.56,22 13,21.55 13,21C13,20.45 12.56,20 12,20C11.44,20 11,20.45 11,21C11,21.55 11.44,22 12,22Z"/>
          </svg>
        </div>
        <h2 class="major-title">{{ major }}</h2>
        <div class="circuit-decoration right">
          <svg class="circuit-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M17,7V9H22V7H17M22,11V13H17V11H22M17,15V17H22V15H17M2,7H15V9H2V7M2,11H15V13H2V11M2,15H15V17H2V15Z"/>
          </svg>
        </div>
      </div>

      <!-- 基本信息部分 - 科技卡片布局 -->
      <div class="tech-grid">
        <div v-for="(section, key) in ['basicQualities', 'professionalField', 'careerFeatures', 'talentPositioning', 'employmentDirection', 'industryProspects', 'creditRequirements', 'mainCourses']" 
             :key="key"
             :class="['tech-card', `card-${key % 4}`]">
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

      <!-- 发展目标部分 - 数字矩阵效果 -->
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
.major-intro {
  @apply space-y-8 relative overflow-hidden;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f7ff 100%);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 118, 255, 0.1);
}

.dark .major-intro {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

/* 计算机学院标题样式 */
.tech-header {
  @apply flex items-center justify-center mb-8 relative;
}

.major-title {
  @apply text-3xl font-bold text-blue-700 dark:text-blue-400 px-8 py-2 relative z-10;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dark .major-title {
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.circuit-decoration {
  @apply relative h-12 w-12 flex-shrink-0;
}

.circuit-icon {
  @apply h-full w-full;
  fill: #0070f3;
  opacity: 0.8;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  transition: all 0.3s ease;
}

.dark .circuit-icon {
  fill: #063975;
  opacity: 0.9;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.circuit-decoration:hover .circuit-icon {
  fill: #3291ff;
  transform: scale(1.1);
}

.dark .circuit-decoration:hover .circuit-icon {
  fill: #60a5fa;
}

/* 科技卡片布局样式 */
.tech-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%230070f320' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%230070f310' stroke-width='0.5'/%3E%3C/svg%3E");
  padding: 1rem;
}

.dark .tech-grid {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%233291ff20' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%233291ff10' stroke-width='0.5'/%3E%3C/svg%3E");
}

.tech-card {
  @apply bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden transition-all duration-300 relative;
  border: 1px solid rgba(0, 112, 243, 0.2);
  transform: translateY(0);
}

.dark .tech-card {
  border: 1px solid rgba(50, 145, 255, 0.2);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.tech-card:hover {
  @apply shadow-lg;
  transform: translateY(-5px);
  border-color: rgba(0, 112, 243, 0.5);
}

.dark .tech-card:hover {
  border-color: rgba(50, 145, 255, 0.5);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

.card-header {
  @apply flex items-center p-4 bg-gradient-to-r from-blue-600 to-blue-700 dark:from-blue-950 dark:to-blue-900 text-white relative;
}

.tech-icon {
  @apply p-2 bg-white dark:bg-gray-700 rounded-full text-blue-600 dark:text-blue-400 mr-3;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.dark .tech-icon {
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
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

.card-glow {
  @apply absolute bottom-0 left-0 w-full h-1;
  background: linear-gradient(90deg, #0070f3, #00c6ff, #0070f3);
  background-size: 200% 100%;
  animation: glow 3s infinite linear;
}

.dark .card-glow {
  background: linear-gradient(90deg, #3291ff, #38bdf8, #3291ff);
  opacity: 0.8;
}

/* 发展目标 - 数字矩阵效果 */
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
@keyframes glow {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}
</style>