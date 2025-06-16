<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  major: {
    type: String,
    required: true
  }
});

const majorIntroductions = {
  '产品设计': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的产品设计理论基础，良好的创意思维和审美能力，较强的造型能力和工程实现能力，能够综合运用设计思维解决实际问题。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及工业产品设计、交互设计、用户体验设计、智能产品设计、可持续设计、文创产品设计、服务设计等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事产品设计、创新设计、用户研究等工作，需要具备设计思维和创新能力，能够理解用户需求，优化产品体验，推动产品创新和价值提升。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在设计公司、制造企业担任产品设计师、创新设计师等岗位的创新型人才，具备产品创意、设计表达和工程实现的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '设计公司、制造企业、创新企业、科技公司等，可从事产品设计、交互设计、用户体验设计、创新设计等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着消费升级和创新驱动发展战略的实施，产品设计人才需求持续增长。特别是在智能产品、健康医疗、文创产业等领域，对高素质产品设计人才的需求尤为旺盛。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '设计素描、设计原理、人机工程学、产品设计方法、产品造型设计、计算机辅助设计、材料与工艺、产品开发设计、交互设计、用户研究、服务设计、创新设计思维'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习设计理论和方法，关注设计趋势和新技术应用，掌握跨学科知识，构建系统化的设计知识体系，具备设计研究能力和创新思维。'
      },
      {
        title: '学会做事',
        content: '能够独立完成产品设计项目，具备从需求分析到方案落地的设计能力，熟练运用设计工具和表达方法，能够将创意转化为可行的产品方案。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够与工程师、营销人员等多学科团队成员有效合作，在设计团队中发挥创意和协调作用。'
      },
      {
        title: '学会做人',
        content: '具备设计伦理和社会责任感，关注可持续设计和包容性设计，尊重知识产权，推动设计为人类生活创造价值，促进社会进步。'
      }
    ]
  },
  '环境设计': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的空间设计理论基础，良好的空间构思能力和表现技能，较强的环境整合能力，能够综合考虑功能、美学和生态需求，创造和谐的人居环境。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及室内设计、景观设计、建筑设计、公共空间设计、展览设计、城市规划与设计、生态设计等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事空间设计、环境规划、景观设计等工作，需要具备空间思维和创新能力，能够协调各种环境因素，创造功能完善、美观舒适、可持续发展的人居环境。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在设计公司、工程企业担任环境设计师、空间设计师等岗位的应用型人才，具备环境空间规划、设计和实施的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '建筑设计公司、室内设计公司、景观设计公司、规划设计机构等，可从事室内设计、景观设计、展览设计、城市设计等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着城镇化进程推进和人居环境品质提升需求增长，环境设计人才市场需求旺盛。特别是在城市更新、文化空间、健康环境等领域，对高素质环境设计人才的需求持续增长。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '设计素描、空间构成、室内设计原理、建筑设计基础、景观设计、人体工程学、环境心理学、装饰材料与工艺、空间表现技法、环境艺术设计、可持续设计、数字化设计'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习环境设计理论和方法，关注设计趋势和技术应用，掌握跨学科知识，构建系统化的环境设计知识体系，具备环境研究能力和创新思维。'
      },
      {
        title: '学会做事',
        content: '能够独立完成环境设计项目，具备从概念构思到方案实施的设计能力，熟练运用设计工具和表达方法，能够将创意转化为可行的空间方案。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够与建筑师、工程师等多学科团队成员有效合作，理解和尊重各方需求，在设计团队中发挥创意和协调作用。'
      },
      {
        title: '学会做人',
        content: '具备设计伦理和社会责任感，关注可持续设计和生态环保，尊重地域文化和历史传承，推动环境设计为人类创造和谐、健康、美好的生活空间。'
      }
    ]
  },
  '动画': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的动画基础理论，良好的叙事能力和视觉表现力，较强的创意思维和艺术感知力，能够综合运用各种媒介手段进行动画创作。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及二维动画、三维动画、停格动画、动作设计、角色设计、场景设计、特效制作、动画导演等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事动画创作、角色设计、特效制作等工作，需要具备艺术创作能力和技术应用能力，能够运用动画语言讲述故事，创造生动的视觉体验。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在影视公司、游戏企业担任动画师、视觉艺术家等岗位的创作型人才，具备动画创意、角色设计和动画制作的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '动画制作公司、影视后期公司、游戏公司、广告公司等，可从事动画设计、角色设计、场景设计、特效制作等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着文化创意产业发展和数字娱乐需求增长，动画人才市场需求旺盛。特别是在影视动画、游戏动画、广告动画等领域，对高素质动画创作人才的需求持续增长。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '动画基础、动画原理、角色设计、场景设计、故事板创作、二维动画、三维动画、动画特效、动画导演、后期合成、动画剧本创作、动画音效设计'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习动画理论和技术，关注动画艺术发展趋势，掌握跨学科知识，构建系统化的动画创作知识体系，具备动画研究能力和创新思维。'
      },
      {
        title: '学会做事',
        content: '能够独立完成动画创作项目，具备从故事构思到作品呈现的全流程能力，熟练运用动画软件和创作工具，能够将创意转化为生动的动画作品。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够在动画制作团队中有效合作，理解不同岗位的工作需求，在创作团队中发挥艺术创意和技术支持作用。'
      },
      {
        title: '学会做人',
        content: '具备艺术伦理和社会责任感，尊重知识产权，传播积极健康的价值观，推动动画艺术的创新发展，为社会创造文化价值和精神享受。'
      }
    ]
  },
  '视觉传达设计': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的视觉设计理论基础，良好的创意思维和审美能力，较强的视觉表现力和沟通能力，能够通过视觉语言传递信息和情感。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及平面设计、品牌设计、广告设计、包装设计、UI设计、信息设计、编辑设计、展示设计等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事视觉设计、品牌设计、UI设计等工作，需要具备视觉创意能力和沟通表达能力，能够理解客户需求，通过视觉设计有效传递信息和价值。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在设计公司、广告公司担任视觉设计师、创意总监等岗位的应用型人才，具备视觉创意、设计表达和项目实施的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '设计公司、广告公司、企业品牌部门、出版机构等，可从事平面设计、品牌设计、UI设计、包装设计等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着品牌经济发展和视觉传播需求增长，视觉传达设计人才市场需求旺盛。特别是具备数字媒体设计能力和品牌设计能力的人才就业前景广阔，职业发展空间大。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '设计素描、色彩构成、平面构成、字体设计、标志设计、版式设计、广告设计、包装设计、品牌设计、UI设计、信息可视化设计、设计心理学'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习视觉设计理论和方法，关注设计趋势和技术应用，掌握跨学科知识，构建系统化的视觉设计知识体系，具备设计研究能力和创新思维。'
      },
      {
        title: '学会做事',
        content: '能够独立完成视觉设计项目，具备从需求分析到方案实施的设计能力，熟练运用设计软件和表现技法，能够将创意转化为有效的视觉传达方案。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够理解客户需求，与不同专业背景的团队成员有效合作，在设计团队中发挥创意和协调作用。'
      },
      {
        title: '学会做人',
        content: '具备设计伦理和社会责任感，尊重知识产权，推崇原创设计，传播积极健康的视觉文化，推动视觉设计为社会创造文化价值和视觉体验。'
      }
    ]
  },
  '数字媒体技术': {
    basicQualities: {
      title: '基本素质要求',
      content: '具备扎实的数字媒体技术基础，良好的设计思维和艺术感知力，较强的技术应用能力，能够融合艺术与技术，创造优质数字内容和交互体验。'
    },
    professionalField: {
      title: '专业领域方向',
      content: '主要涉及交互设计、网页设计、移动应用设计、数字娱乐、虚拟现实、增强现实、游戏设计、数字影像等领域。'
    },
    careerFeatures: {
      title: '职业特征',
      content: '从事交互设计、UI设计、数字内容创作等工作，需要具备艺术创意能力和技术实现能力，能够创造优质的数字体验和沉浸式内容。'
    },
    talentPositioning: {
      title: '人才定位',
      content: '培养能够在互联网企业、游戏公司担任交互设计师、UI设计师等岗位的复合型人才，具备数字媒体内容创意、交互设计和技术实现的综合能力。'
    },
    employmentDirection: {
      title: '就业方向',
      content: '互联网企业、游戏公司、数字媒体机构、广告公司等，可从事交互设计、UI设计、UX设计、数字内容创作等职位。'
    },
    industryProspects: {
      title: '行业前景',
      content: '随着数字经济发展和数字媒体技术创新，数字媒体技术人才市场需求旺盛。特别是在虚拟现实、增强现实、元宇宙等新兴领域，对高素质数字媒体技术人才的需求持续增长。'
    },
    creditRequirements: {
      title: '学分要求',
      content: '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分'
    },
    mainCourses: {
      title: '主要课程',
      content: '数字媒体概论、交互设计、UI设计、数字图像处理、网页设计与制作、移动应用设计、数字影像创作、数字音频处理、三维建模与动画、虚拟现实技术、游戏设计基础'
    },
    developmentGoals: [
      {
        title: '学会求知',
        content: '持续学习数字媒体理论和技术，关注数字技术发展趋势，掌握跨学科知识，构建系统化的数字媒体知识体系，具备数字创新能力和技术敏感性。'
      },
      {
        title: '学会做事',
        content: '能够独立完成数字媒体项目，具备从创意构思到技术实现的全流程能力，熟练运用数字媒体设计和开发工具，能够创造高质量的数字体验和内容。'
      },
      {
        title: '学会共处',
        content: '具备良好的沟通能力和团队协作精神，能够与设计师、工程师等多学科团队成员有效合作，在数字创意团队中发挥艺术创意和技术支持作用。'
      },
      {
        title: '学会做人',
        content: '具备数字伦理和社会责任感，关注数字内容的健康性和包容性，尊重知识产权，推动数字媒体技术为人类创造积极的数字体验和文化价值。'
      }
    ]
  }
};

// 获取不同艺术图标的方法
const getArtIcon = (key) => {
  switch (key) {
    case 'basicQualities':
      return 'fas fa-palette';
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
      return 'fas fa-paint-brush';
  }
};
</script>

<template>
  <div class="art-major-intro">
    <template v-if="majorIntroductions[major]">
      <!-- 艺术学院标题 -->
      <div class="art-header">
        <div class="palette-decoration left">
          <svg class="palette" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <path d="M50 10c-22.1 0-40 17.9-40 40s17.9 40 40 40c3.3 0 6-2.7 6-6 0-1.5-0.6-2.9-1.5-4-0.9-1.1-1.5-2.5-1.5-4 0-3.3 2.7-6 6-6h7c12.1 0 22-9.9 22-22 0-20.9-16.3-38-38-38zm-26 40c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm12-16c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm28 0c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm12 16c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6z"/>
          </svg>
        </div>
        <h2 class="major-title">{{ major }}</h2>
        <div class="palette-decoration right">
          <svg class="palette" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <path d="M50 10c-22.1 0-40 17.9-40 40s17.9 40 40 40c3.3 0 6-2.7 6-6 0-1.5-0.6-2.9-1.5-4-0.9-1.1-1.5-2.5-1.5-4 0-3.3 2.7-6 6-6h7c12.1 0 22-9.9 22-22 0-20.9-16.3-38-38-38zm-26 40c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm12-16c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm28 0c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6zm12 16c-3.3 0-6-2.7-6-6s2.7-6 6-6 6 2.7 6 6-2.7 6-6 6z"/>
          </svg>
        </div>
      </div>

      <!-- 基本信息部分 - 创意卡片布局 -->
      <div class="art-grid">
        <div v-for="(section, key) in ['basicQualities', 'professionalField', 'careerFeatures', 'talentPositioning', 'employmentDirection', 'industryProspects', 'creditRequirements', 'mainCourses']" 
             :key="key"
             :class="['art-card', `card-${key % 4}`]">
          <div class="card-header">
            <div class="art-icon">
              <i :class="getArtIcon(section)"></i>
            </div>
            <h3 class="section-title">{{ majorIntroductions[major][section].title }}</h3>
          </div>
          <div class="card-body">
            <p class="section-content">{{ majorIntroductions[major][section].content }}</p>
          </div>
          <div class="card-glow"></div>
        </div>
      </div>

      <!-- 发展目标部分 - 创意画板效果 -->
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
.art-major-intro {
  @apply space-y-8 relative overflow-hidden;
  background: linear-gradient(135deg, #fff8f8 0%, #fdf2ff 100%);
  border-radius: 1rem;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(186, 104, 200, 0.15);
}

.dark .art-major-intro {
  background: linear-gradient(135deg, #1d2f47 0%, #0b1635 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
}

/* 艺术学院标题样式 */
.art-header {
  @apply flex items-center justify-center mb-8 relative;
}

.major-title {
  @apply text-3xl font-bold text-purple-700 dark:text-purple-400 px-8 py-2 relative z-10;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dark .major-title {
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.palette-decoration {
  @apply relative h-14 w-14 flex-shrink-0;
}

.palette {
  @apply h-full w-full;
  fill: #9c27b0;
  opacity: 0.8;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  transition: all 0.5s ease;
}

.dark .palette {
  fill: #bb86fc;
  opacity: 0.9;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.palette-decoration:hover .palette {
  fill: #e91e63;
  transform: rotate(15deg);
}

.dark .palette-decoration:hover .palette {
  fill: #cf6dce;
  transform: rotate(15deg);
}

/* 创意卡片布局样式 */
.art-grid {
  @apply grid grid-cols-1 md:grid-cols-2 gap-6 relative;
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%239C27B020' fill-opacity='0.1'/%3E%3C/svg%3E");
}

.dark .art-grid {
  background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23bb86fc20' fill-opacity='0.1'/%3E%3C/svg%3E");
}

.art-card {
  @apply bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden transition-all duration-300;
  border: 2px solid transparent;
  transform: rotate(0);
}

.dark .art-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.art-card:hover {
  @apply shadow-lg;
  transform: translateY(-5px);
}

.dark .art-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
}

.card-0 { border-color: #e91e63; }
.card-1 { border-color: #9c27b0; }
.card-2 { border-color: #3f51b5; }
.card-3 { border-color: #009688; }

.dark .card-0 { border-color: #cf6679; }
.dark .card-1 { border-color: #bb86fc; }
.dark .card-2 { border-color: #82b1ff; }
.dark .card-3 { border-color: #03dac6; }

.card-header {
  @apply flex items-center p-4 relative;
  background: linear-gradient(135deg, rgba(255,255,255,0.8) 0%, rgba(240,240,240,0.8) 100%);
}

.dark .card-header {
  background: linear-gradient(135deg, rgba(40,35,50,0.8) 0%, rgba(30,25,40,0.8) 100%);
}

.art-icon {
  @apply p-2 rounded-full text-white mr-3;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.dark .art-icon {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.card-0 .art-icon { background: #e91e63; }
.card-1 .art-icon { background: #9c27b0; }
.card-2 .art-icon { background: #3f51b5; }
.card-3 .art-icon { background: #009688; }

.dark .card-0 .art-icon { background: #cf6679; }
.dark .card-1 .art-icon { background: #bb86fc; }
.dark .card-2 .art-icon { background: #82b1ff; }
.dark .card-3 .art-icon { background: #03dac6; }

.section-title {
  @apply text-lg font-bold text-gray-800 dark:text-gray-200;
}

.card-body {
  @apply p-4;
}

.section-content {
  @apply text-gray-700 dark:text-gray-300 leading-relaxed;
}

/* 发展目标 - 创意画板效果 */
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
  background: linear-gradient(90deg, #e91e63, #9c27b0, #3f51b5, #009688);
  border-radius: 4px;
}

.dark .canvas-line {
  background: linear-gradient(90deg, #cf6679, #bb86fc, #82b1ff, #03dac6);
  opacity: 0.9;
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
  background: linear-gradient(90deg, #e91e63, #9c27b0, #3f51b5, #009688);
  border-radius: 4px;
}

.dark .brush-decoration {
  background: linear-gradient(90deg, #cf6679, #bb86fc, #82b1ff, #03dac6);
  opacity: 0.9;
}

.no-data {
  @apply text-center text-gray-500 dark:text-gray-400 py-8;
}

</style>