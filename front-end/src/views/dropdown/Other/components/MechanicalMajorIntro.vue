<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  major: {
    type: String,
    required: true
  }
});

const majorIntroductions = {
  '机械工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的机械设计基础，良好的空间思维能力，较强的工程实践能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及机械设计、智能制造、工业自动化、3D打印等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事机械设计、产品开发、生产管理等工作，需要具备专业技能和创新思维。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在制造业担任工程师、技术主管等岗位的专业人才。'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习机械工程新技术，掌握智能制造趋势。'
      },
      {
        title: '学会做事',
        content: '能够独立完成机械设计项目，具备工程实践能力。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够跨部门沟通。'
      },
      {
        title: '学会做人',
        content: '具备工程伦理和职业道德，注重产品质量和安全。'
      }
    ]
  },
  '机械设计制造及其自动化': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的机械原理和制造工艺基础，良好的设计能力，较强的实践动手能力，能够系统地分析和解决机械设计与制造中的工程问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及机械设计、制造工艺、数控技术、精密加工、CAD/CAM、工业设计、智能制造等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事机械产品设计、工艺规划、生产管理等工作，需要具备专业知识和实践经验，能够将设计理念转化为实际产品，确保产品的质量和性能。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在机械制造业担任设计工程师、工艺工程师等岗位的应用型人才，具备机械产品全生命周期的设计、制造和维护能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '机械设备制造企业、汽车制造企业、航空航天企业、船舶制造企业等，可从事机械设计、工艺规划、生产管理、产品研发等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '作为国民经济的支柱产业，机械制造业对高素质人才的需求持续稳定。随着智能制造和高端装备制造业的发展，机械专业人才就业前景广阔，职业发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '机械设计基础、工程力学、机械制造技术、机械CAD/CAM、数控技术、机电一体化设计、精密测量技术、特种加工技术、现代设计方法、材料科学基础、机械工程实验'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习先进制造技术，掌握数字化设计与制造趋势，关注行业技术发展，构建系统化的机械工程知识体系，具备持续学习的能力和意愿。'
      },
      {
        title: '学会做事',
        content: '能够独立完成机械产品设计与制造，具备解决实际问题的能力，熟练运用现代设计工具和制造技术，具有工程实践能力和创新精神。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够与不同岗位人员有效沟通，理解产品设计与制造中的不同环节和角色，能够在多元化团队中有效工作。'
      },
      {
        title: '学会做人',
        content: '具备工匠精神和职业道德，注重产品精度和质量，关注生产安全和环境保护，具有责任意识和社会担当，遵守工程伦理。'
      }
    ]
  },
  '机械电子工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备机械与电子交叉知识基础，良好的系统集成能力，较强的创新应用能力，能够综合运用机械、电子、控制、计算机等知识解决机电系统问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及机电一体化、嵌入式系统、智能装备、传感与检测、光机电系统、精密仪器、智能控制等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事机电产品开发、智能设备设计、系统集成等工作，需要具备跨学科知识和综合能力，能够将机械系统与电子系统有机结合，实现产品功能的智能化和自动化。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在高端装备制造业担任机电工程师、系统集成师等岗位的复合型人才，具备机电系统设计、开发和维护的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '智能装备制造企业、精密仪器制造企业、医疗设备企业、自动化设备企业等，可从事机电产品设计、嵌入式系统开发、智能装备研发等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着智能制造和工业4.0的推进，机电一体化技术在各领域的应用不断深入，机械电子工程专业人才需求旺盛。国家战略性新兴产业的发展为专业人才提供了广阔的就业前景。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '机械设计基础、电工电子技术、单片机原理及应用、传感器技术、机电传动控制、嵌入式系统设计、机电系统设计、PLC应用技术、工业机器人技术、智能仪器设计'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习机电融合新技术，掌握智能装备发展趋势，构建跨学科知识体系，关注交叉学科前沿，具备集成创新能力和技术敏感性。'
      },
      {
        title: '学会做事',
        content: '能够独立完成机电系统设计，具备跨学科实践能力，熟练运用现代设计工具和开发平台，能够将机电系统概念转化为实际产品。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够在多学科团队中有效工作，理解不同专业背景人员的思维方式，具有良好的沟通能力和团队意识。'
      },
      {
        title: '学会做人',
        content: '具备创新精神和责任意识，注重系统安全和可靠性，关注机电产品的环保和节能设计，推动技术创新与可持续发展相结合。'
      }
    ]
  },
  '车辆工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的车辆工程基础，良好的系统分析能力，较强的工程应用能力，能够系统地分析和解决汽车设计、制造和测试中的工程问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及汽车设计、新能源汽车、智能网联汽车、车辆测试与评价、汽车电子控制、底盘技术、汽车动力系统等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事汽车设计开发、性能测试、生产管理等工作，需要具备专业知识和创新能力，能够适应汽车产业新能源化、智能化、网联化的发展趋势。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在汽车行业担任研发工程师、测试工程师等岗位的专业人才，具备车辆系统设计、测试和优化的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '汽车整车企业、零部件供应商、汽车研发机构、检测认证机构等，可从事车辆设计、性能开发、试验测试、质量控制等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着汽车产业转型升级和新能源汽车的快速发展，车辆工程专业人才需求持续增长。智能网联汽车、自动驾驶等新技术领域的发展为专业人才提供了广阔的就业前景和发展空间。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '汽车构造、汽车理论、汽车设计、汽车电子控制技术、新能源汽车技术、智能网联汽车、车辆测试技术、汽车CAE、汽车制造工艺、汽车动力学、汽车NVH'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习汽车新技术，掌握新能源与智能化发展趋势，关注行业技术革新，具备跨学科学习能力，保持对汽车产业发展的敏感性。'
      },
      {
        title: '学会做事',
        content: '能够独立完成车辆系统设计，具备工程实践能力，熟练运用现代设计工具和测试方法，能够将理论知识应用于解决实际问题。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够在跨国企业环境中工作，理解全球化背景下的汽车产业特点，具有跨文化沟通能力和国际视野。'
      },
      {
        title: '学会做人',
        content: '具备工程伦理和社会责任感，注重产品安全和环保，关注汽车产业可持续发展，推动绿色、智能、安全的出行方式。'
      }
    ]
  },
  '材料成型及控制工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的材料科学基础，良好的工艺设计能力，较强的过程控制能力，能够系统地分析和解决材料成型及控制中的工程问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及材料成型工艺、模具设计、增材制造(3D打印)、质量控制、先进制造技术、精密成形、塑性加工等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事材料成型工艺设计、模具开发、质量管理等工作，需要具备专业技能和创新思维，能够优化成型工艺，提高产品质量和生产效率。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在制造业担任工艺工程师、模具设计师等岗位的应用型人才，具备材料成型工艺设计、优化和控制的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '模具制造企业、汽车零部件企业、航空航天企业、家电制造企业等，可从事工艺设计、模具开发、质量控制、生产管理等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '制造业是国民经济的重要支柱，材料成型及控制工程专业人才需求稳定。随着先进制造技术的发展和增材制造等新技术的应用，专业人才就业前景广阔，发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '材料科学基础、塑性成形原理、模具设计、铸造工艺学、焊接技术、增材制造技术、材料成型CAD/CAM/CAE、特种成形技术、金属材料热处理、质量控制工程'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习先进成型技术，掌握新材料应用趋势，关注行业技术发展，构建系统化的专业知识体系，具备持续学习和创新能力。'
      },
      {
        title: '学会做事',
        content: '能够独立完成工艺设计与优化，具备解决实际问题的能力，熟练运用现代设计工具和分析方法，能够将理论知识应用于生产实践。'
      },
      {
        title: '学会共处',
        content: '具备团队协作精神，能够与设计、生产部门有效沟通，理解制造流程中不同环节的要求，能够协调各方资源，推动工艺优化。'
      },
      {
        title: '学会做人',
        content: '具备工匠精神和职业道德，注重资源节约和环境保护，关注清洁生产和绿色制造，推动产业转型升级和可持续发展。'
      }
    ]
  },
  '机器人工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的机器人技术基础，良好的系统设计能力，较强的创新实践能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及工业机器人、服务机器人、智能控制、人机交互等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事机器人研发、系统集成、智能控制等工作，需要具备跨学科知识和创新能力。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在机器人领域担任研发工程师、项目经理等岗位的创新人才。'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习机器人新技术，掌握人工智能发展趋势。'
      },
      {
        title: '学会做事',
        content: '能够独立完成机器人系统开发，具备创新实践能力。'
      },
      {
        title: '学会共处',
        content: '具备团队协作能力，能够跨学科合作。'
      },
      {
        title: '学会做人',
        content: '具备创新精神和职业操守，注重技术伦理。'
      }
    ]
  },
  '智能制造工程': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的制造工程基础，良好的系统思维能力，较强的智能化应用能力。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及智能制造、工业物联网、数字孪生、预测性维护等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事智能工厂规划、生产优化、系统集成等工作，需要具备综合技能和创新思维。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在智能制造领域担任技术专家、项目经理等岗位的复合型人才。'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习智能制造新技术，掌握工业4.0发展趋势。'
      },
      {
        title: '学会做事',
        content: '能够独立完成智能制造项目，具备系统集成能力。'
      },
      {
        title: '学会共处',
        content: '具备团队领导能力，能够跨部门协作。'
      },
      {
        title: '学会做人',
        content: '具备工程伦理和创新精神，注重可持续发展。'
      }
    ]
  }
};

// 获取不同机械图标的方法
const getMechIcon = (key) => {
  switch (key) {
    case 'basicQualities':
      return 'fas fa-cogs';
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
      return 'fas fa-wrench';
  }
};
</script>

<template>
  <div class="mechanical-major-intro">
    <template v-if="majorIntroductions[major]">
      <!-- 机械学院标题 -->
      <div class="mechanical-header">
        <div class="gear-decoration left">
          <svg class="gear" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <path d="M50 15c-2.8 0-5 2.2-5 5v5.2c-2.4 0.4-4.7 1.2-6.8 2.2l-3.7-3.7c-2-2-5.1-2-7.1 0s-2 5.1 0 7.1l3.7 3.7c-1 2.1-1.8 4.4-2.2 6.8H24c-2.8 0-5 2.2-5 5s2.2 5 5 5h5.2c0.4 2.4 1.2 4.7 2.2 6.8l-3.7 3.7c-2 2-2 5.1 0 7.1s5.1 2 7.1 0l3.7-3.7c2.1 1 4.4 1.8 6.8 2.2V72c0 2.8 2.2 5 5 5s5-2.2 5-5v-5.2c2.4-0.4 4.7-1.2 6.8-2.2l3.7 3.7c2 2 5.1 2 7.1 0s2-5.1 0-7.1l-3.7-3.7c1-2.1 1.8-4.4 2.2-6.8H76c2.8 0 5-2.2 5-5s-2.2-5-5-5h-5.2c-0.4-2.4-1.2-4.7-2.2-6.8l3.7-3.7c2-2 2-5.1 0-7.1s-5.1-2-7.1 0l-3.7 3.7c-2.1-1-4.4-1.8-6.8-2.2V20c0-2.8-2.2-5-5-5zm0 20c8.3 0 15 6.7 15 15s-6.7 15-15 15-15-6.7-15-15 6.7-15 15-15z"/>
          </svg>
        </div>
        <h2 class="major-title">{{ major }}</h2>
        <div class="gear-decoration right">
          <svg class="gear" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <path d="M50 15c-2.8 0-5 2.2-5 5v5.2c-2.4 0.4-4.7 1.2-6.8 2.2l-3.7-3.7c-2-2-5.1-2-7.1 0s-2 5.1 0 7.1l3.7 3.7c-1 2.1-1.8 4.4-2.2 6.8H24c-2.8 0-5 2.2-5 5s2.2 5 5 5h5.2c0.4 2.4 1.2 4.7 2.2 6.8l-3.7 3.7c-2 2-2 5.1 0 7.1s5.1 2 7.1 0l3.7-3.7c2.1 1 4.4 1.8 6.8 2.2V72c0 2.8 2.2 5 5 5s5-2.2 5-5v-5.2c2.4-0.4 4.7-1.2 6.8-2.2l3.7 3.7c2 2 5.1 2 7.1 0s2-5.1 0-7.1l-3.7-3.7c1-2.1 1.8-4.4 2.2-6.8H76c2.8 0 5-2.2 5-5s-2.2-5-5-5h-5.2c-0.4-2.4-1.2-4.7-2.2-6.8l3.7-3.7c2-2 2-5.1 0-7.1s-5.1-2-7.1 0l-3.7 3.7c-2.1-1-4.4-1.8-6.8-2.2V20c0-2.8-2.2-5-5-5zm0 20c8.3 0 15 6.7 15 15s-6.7 15-15 15-15-6.7-15-15 6.7-15 15-15z"/>
          </svg>
        </div>
      </div>

      <!-- 基本信息部分 - 工业卡片布局 -->
      <div class="mechanical-grid">
        <div v-for="(section, key) in ['basicQualities', 'professionalField', 'careerFeatures', 'talentPositioning', 'employmentDirection', 'industryProspects', 'creditRequirements', 'mainCourses']" 
             :key="key"
             :class="['mechanical-card', `card-${key % 4}`]">
          <div class="card-header">
            <div class="mech-icon">
              <i :class="getMechIcon(section)"></i>
            </div>
            <h3 class="section-title">{{ majorIntroductions[major][section].title }}</h3>
          </div>
          <div class="card-body">
            <p class="section-content">{{ majorIntroductions[major][section].content }}</p>
          </div>
          <div class="card-glow"></div>
        </div>
      </div>

      <!-- 发展目标部分 - 工程图纸效果 -->
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
.mechanical-major-intro {
  @apply space-y-8 relative overflow-hidden;
  background: linear-gradient(135deg, #f5f5f5 0%, #e9e9e9 100%);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.dark .mechanical-major-intro {
  background: linear-gradient(135deg, #1e1e1e 0%, #2d2d2d 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
}

/* 机械学院标题样式 */
.mechanical-header {
  @apply flex items-center justify-center mb-8 relative;
}

.major-title {
  @apply text-3xl font-bold text-gray-800 dark:text-gray-200 px-8 py-2 relative z-10;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.gear-decoration {
  @apply relative h-16 w-16 flex-shrink-0;
}

.gear {
  @apply h-full w-full;
  fill: #ff6b00;
  opacity: 0.8;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.dark .gear {
  fill: #ff8c38;
  opacity: 0.9;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.4));
}

.gear-decoration.left .gear {
  animation: rotate 20s infinite linear;
}

.gear-decoration.right .gear {
  animation: rotate 20s infinite linear reverse;
}

/* 工业卡片布局样式 */
.mechanical-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%23ff6b0020' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%23ff6b0010' stroke-width='0.5'/%3E%3C/svg%3E");
  padding: 1rem;
}

.dark .mechanical-grid {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M10 10h80v80H10z' fill='none' stroke='%23ff8c3820' stroke-width='0.5'/%3E%3Cpath d='M30 10v80M50 10v80M70 10v80M10 30h80M10 50h80M10 70h80' stroke='%23ff8c3810' stroke-width='0.5'/%3E%3C/svg%3E");
}

.mechanical-card {
  @apply bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden transition-all duration-300 relative;
  border: 2px solid #d1d1d1;
  transform: translateY(0);
}

.dark .mechanical-card {
  border: 2px solid #4a4a4a;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.mechanical-card:hover {
  @apply shadow-lg;
  transform: translateY(-5px);
  border-color: #ff6b00;
}

.dark .mechanical-card:hover {
  border-color: #ff8c38;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
}

.card-header {
  @apply flex items-center p-4 bg-gradient-to-r from-gray-700 to-gray-800 dark:from-gray-800 dark:to-gray-900 text-white relative;
}

.mech-icon {
  @apply p-2 bg-orange-500 dark:bg-orange-600 rounded-full text-white mr-3;
  box-shadow: 0 0 10px rgba(255, 107, 0, 0.5);
}

.section-title {
  @apply text-lg font-bold;
}

.card-body {
  @apply p-4 relative;
  background: linear-gradient(135deg, #f8f8f8 0%, #efefef 100%);
}

.dark .card-body {
  background: linear-gradient(135deg, #2d2d2d 0%, #333333 100%);
}

.section-content {
  @apply text-gray-700 dark:text-gray-300 leading-relaxed;
}

/* 发展目标 - 工程图纸效果 */
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
@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

</style>

<script>
export default {
  methods: {
    getMechIcon(key) {
      const icons = {
        basicQualities: 'fas fa-cogs',
        professionalField: 'fas fa-compass',
        careerFeatures: 'fas fa-briefcase',
        talentPositioning: 'fas fa-user-graduate',
        employmentDirection: 'fas fa-building',
        industryProspects: 'fas fa-chart-line',
        creditRequirements: 'fas fa-graduation-cap',
        mainCourses: 'fas fa-book'
      };
      return icons[key] || 'fas fa-wrench';
    }
  }
};
</script>