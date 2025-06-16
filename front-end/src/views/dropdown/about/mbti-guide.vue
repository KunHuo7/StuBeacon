<template>
  <div>
    <!-- 触发按钮 -->
    <a-button 
      @click="showModal = true"
      type="primary"
      class="flex items-center"
    >
      <span class="mr-0">MBTI指南</span>
      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clip-rule="evenodd" />
      </svg>
    </a-button>

    <!-- 自定义弹窗 -->
    <teleport to="body">
      <div v-if="showModal" 
           @click="closeModal"
           class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center transition-opacity duration-300">
        
        <!-- 弹窗容器 -->
        <div @click="stopPropagation"
             class="bg-white dark:bg-gray-800 rounded-xl w-[1000px] max-w-[95vw] max-h-[90vh] shadow-2xl
                    border border-gray-200 dark:border-gray-700 
                    transform transition-all duration-300 animate-modal-open overflow-hidden">
          
          <!-- 弹窗头部 -->
          <div class="bg-gradient-to-r from-blue-50 to-indigo-50 dark:from-gray-800 dark:to-gray-700 
                     border-b border-gray-200 dark:border-gray-700 rounded-t-xl py-4 px-6 
                     flex items-center justify-between">
            <h2 class="text-2xl font-bold text-gray-800 dark:text-gray-100">MBTI人格类型指南</h2>
            <button @click="closeModal" 
                    class="p-1.5 rounded-full hover:bg-gray-200 dark:hover:bg-gray-600 
                          text-gray-500 dark:text-gray-400 hover:text-gray-800 dark:hover:text-gray-200
                          transition-colors duration-200">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" 
                   stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <!-- 弹窗内容 -->
          <div class="p-4">
            <div class="max-h-[calc(90vh-120px)] overflow-y-auto pr-2 custom-scrollbar">
              <div class="bg-white dark:bg-gray-800 p-6 rounded-xl">
                <p class="text-gray-700 dark:text-gray-300 mb-8">
                  MBTI(Myers-Briggs Type Indicator，迈尔斯-布里格斯类型指标)是一种基于荣格类型学说的人格类型指标工具，
                  将人格分为16种类型，每种类型由四个维度的偏好组成：外向/内向(E/I)、感觉/直觉(S/N)、思考/情感(T/F)和判断/感知(J/P)。
                </p>

                <!-- 导航按钮 -->
                <div class="flex flex-wrap gap-4 mb-8">
                  <a-button 
                    v-for="(section, index) in sections" 
                    :key="index"
                    @click="currentSection = index"
                    :type="currentSection === index ? 'primary' : 'default'"
                  >
                    {{ section.title }}
                  </a-button>
                </div>

                <!-- 内容区域 -->
                <div v-show="currentSection === 0">
                  <div class="bg-indigo-50 dark:bg-indigo-900 dark:bg-opacity-20 p-4 rounded-lg shadow-md">
                    <h2 class="text-xl font-bold text-indigo-800 dark:text-indigo-300 mb-3">了解MBTI的四个维度</h2>
                    <div class="space-y-4">
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">能量来源：外向(E) vs 内向(I)</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          <strong>外向(E)：</strong>从外部世界获取能量，喜欢社交互动，思考时倾向于说出来。<br>
                          <strong>内向(I)：</strong>从内心世界获取能量，喜欢独处与深思，在表达前会先思考。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">信息收集：感觉(S) vs 直觉(N)</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          <strong>感觉(S)：</strong>关注具体事实和细节，相信经验，注重实际应用。<br>
                          <strong>直觉(N)：</strong>关注模式和可能性，喜欢想象和创新，思考未来。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">决策方式：思考(T) vs 情感(F)</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          <strong>思考(T)：</strong>基于逻辑和客观分析做决定，重视真相和公平。<br>
                          <strong>情感(F)：</strong>基于价值观和人际关系做决定，注重和谐与他人感受。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">生活方式：判断(J) vs 感知(P)</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          <strong>判断(J)：</strong>喜欢计划和组织，追求决定和结束，遵守规则和期限。<br>
                          <strong>感知(P)：</strong>喜欢保持选择余地，追求体验过程，适应性强，随机应变。
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-show="currentSection === 1">
                  <div class="bg-blue-50 dark:bg-blue-900 dark:bg-opacity-20 p-4 rounded-lg shadow-md">
                    <h2 class="text-xl font-bold text-blue-800 dark:text-blue-300 mb-3">分析型人格</h2>
                    <div class="space-y-4">
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">INTJ - 建筑师</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          具有创新思维的战略家，擅长系统思考和战略规划，追求卓越和独立。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">INTP - 逻辑学家</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          擅长逻辑分析的创新者，追求逻辑和理性，善于分析复杂系统和理论。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ENTJ - 指挥官</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          大胆、果断的领导者，务实高效，擅长战略规划和执行，有领导魅力。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ENTP - 辩论家</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          充满创造力和挑战精神的思想家，热爱思想交锋和创新，思维敏捷。
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-show="currentSection === 2">
                  <div class="bg-green-50 dark:bg-green-900 dark:bg-opacity-20 p-4 rounded-lg shadow-md">
                    <h2 class="text-xl font-bold text-green-800 dark:text-green-300 mb-3">外交型人格</h2>
                    <div class="space-y-4">
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">INFJ - 提倡者</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          理想主义者，有强烈的道德感和使命感，关注他人成长和社会公正。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">INFP - 调停者</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          理想主义和和平使者，重视个人价值和内心和谐，富有同情心和创造力。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ENFJ - 主人公</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          天生的领导者和激励者，关注他人成长和团队和谐，富有魅力和感染力。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ENFP - 竞选者</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          热情洋溢，富有同情心和想象力，乐于尝试新事物，善于激励他人。
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-show="currentSection === 3">
                  <div class="bg-purple-50 dark:bg-purple-900 dark:bg-opacity-20 p-4 rounded-lg shadow-md">
                    <h2 class="text-xl font-bold text-purple-800 dark:text-purple-300 mb-3">守卫型人格</h2>
                    <div class="space-y-4">
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ISTJ - 物流师</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          实际、务实的组织者，注重责任和传统，可靠而精确，重视规则和程序。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ISFJ - 守卫者</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          尽职尽责的保护者，重视传统和家庭，注重服务他人，细致而有耐心。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ESTJ - 总经理</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          务实高效的管理者，注重秩序和结构，责任感强，善于组织和实施。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ESFJ - 执政官</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          热心肠的合作者，重视和谐与传统，乐于帮助他人，注重社交和关系。
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-show="currentSection === 4">
                  <div class="bg-amber-50 dark:bg-amber-900 dark:bg-opacity-20 p-4 rounded-lg shadow-md">
                    <h2 class="text-xl font-bold text-amber-800 dark:text-amber-300 mb-3">探险型人格</h2>
                    <div class="space-y-4">
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ISTP - 鉴赏家</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          灵活的问题解决者，擅长分析和实践，注重效率，冷静而适应力强。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ISFP - 探险家</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          自由灵魂的艺术家，感性而和平，享受当下的体验，有审美敏感性。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ESTP - 企业家</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          充满活力的冒险家，灵活而务实，善于解决危机，喜欢行动和挑战。
                        </p>
                      </div>
                      <div class="bg-white dark:bg-gray-700 p-3 rounded-md shadow-sm">
                        <h3 class="font-semibold text-gray-900 dark:text-white">ESFP - 表演者</h3>
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                          自然的表演者和娱乐者，热情洋溢，享受生活，乐观开朗，随性而为。
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div class="mt-10 text-center text-gray-500 dark:text-gray-400 text-sm">
                  <p>注意：MBTI测试提供的是性格偏好的参考，而非科学结论。每个人都是独特的个体，不应被简单标签化。</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </teleport>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const currentSection = ref(0)
const showModal = ref(false)

const sections = [
  { title: 'MBTI维度' },
  { title: '分析型人格' },
  { title: '外交型人格' },
  { title: '守卫型人格' },
  { title: '探险型人格' }
]

// 关闭弹窗
const closeModal = () => {
  showModal.value = false;
};

// 阻止弹窗内容点击冒泡到遮罩层
const stopPropagation = (event) => {
  event.stopPropagation();
};

// 监听ESC键关闭弹窗
watch(showModal, (newValue) => {
  if (newValue) {
    document.addEventListener('keydown', handleEscKey);
  } else {
    document.removeEventListener('keydown', handleEscKey);
  }
});

const handleEscKey = (event) => {
  if (event.key === 'Escape') {
    closeModal();
  }
};
</script>

<style scoped>
/* 自定义滚动条 */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* 暗黑模式滚动条 */
.dark .custom-scrollbar::-webkit-scrollbar-thumb {
  background: #475569;
}

.dark .custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #64748b;
}

/* 弹窗动画 */
@keyframes modalOpen {
  from {
    opacity: 0;
    transform: scale(0.95) translateY(10px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.animate-modal-open {
  animation: modalOpen 0.3s ease-out forwards;
}
</style> 