<script setup>
import { ref, onMounted } from 'vue'
import router from "@/router/index.js";
import Card from "@/components/tooltip/Card.vue";
import Carousel from "@/components/zuJian/Carousel.vue";
import { useRouter } from 'vue-router';
import ModernMenu from "@/components/zuJian/ModernMenu.vue";
import GongGao from "@/components/zuJian/main/GongGao.vue";
import Activity from "@/views/dropdown/activity/Activity.vue";

const router2 = useRouter();

// 菜单数据结构
const menuItems = ref([
  {
    title: '校园导览',
    icon: 'map',
    path: '/map',
    description: '探索校园各个区域'
  },
  {
    title: '学习资源',
    icon: 'book',
    path: '/admission/study-guide',
    description: '获取学习材料和资源'
  },
  {
    title: '校园活动',
    icon: 'calendar',
    path: '/index',
    description: '了解最新校园活动'
  },
  {
    title: '个人中心',
    icon: 'user',
    path: '/personal',
    description: '管理个人信息和设置'
  }
]);

// 控制菜单展开/收起状态
const isMenuExpanded = ref(false);
const activeItem = ref(null);


// 处理菜单项点击
const handleMenuItemClick = (item) => {
  activeItem.value = item;
  router.push(item.path);
};

// 检查当前路由是否匹配菜单项
const isActiveRoute = (path) => {
  return router2.currentRoute.value.path === path;
};

// 处理菜单项悬停
const handleMenuItemHover = (item) => {
  if (!isMenuExpanded.value) {
    activeItem.value = item;
  }
};

// 控制导航栏可见性
const isHidden = ref(false);

// 消息


// 校园活动
const campusActivities = ref([
  {
    title: '迎新晚会',
    date: '2026-09-10',
    location: '大礼堂',
    image: '/images/welcome-party.jpg'
  },
  {
    title: '社团招新',
    date: '2026-09-15',
    location: '学生活动中心',
    image: '/images/club-fair.jpg'
  },
  {
    title: '学术讲座',
    date: '2026-09-20',
    location: '图书馆报告厅',
    image: '/images/lecture.jpg'
  }
])

// 初始化各部分为可见状态
onMounted(() => {
  // 页面加载时默认显示所有部分
  document.querySelectorAll('.animate-on-scroll').forEach(section => {
    section.style.opacity = '1';
    section.style.transform = 'translateY(0)';
  });
})
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800 relative overflow-hidden transition-colors duration-500">
    <!-- 轮播图部分 -->
    <div class="relative overflow-hidden">
      <!-- AI字样 -->
      <div class="ai-letter">AI</div>
<!--      <Carousel class="z-10 relative"/>-->
      <!-- IT字样 -->
      <div class="it-letter">IT</div>
    </div>

    <!-- 快速通道 -->
    <div class="p-6 animate-on-scroll" id="quick-access">
      <div class="container mx-auto p-4 bg-white/90 dark:bg-gray-800/90 backdrop-blur-md rounded-2xl shadow-lg hover:shadow-xl
      transition-all duration-300 transform  border border-gray-200 dark:border-gray-700">
        <h2 class="text-3xl font-bold mb-8 px-4 py-2 inline-block rounded-lg gradient-text">
          推荐服务
        </h2>
        <Card/>
      </div>
    </div>

    <!-- 最新公告 -->
    <GongGao/>

    <!-- 校园活动 -->
    <Activity/>

    <!-- 入学指南 -->
    <div class="container mx-auto px-4 py-12 animate-on-scroll" id="guide-section">
      <div class="text-3xl font-bold mb-10 gradient-text-alt inline-block px-4 py-2 rounded-lg">
        入学指南
        <div class="h-1 w-24 bg-gradient-to-r from-yellow-500 to-orange-500 mt-2 rounded-full"></div>
      </div>

      <div class="bg-white/90 dark:bg-gray-800/90 backdrop-blur-md rounded-xl shadow-lg p-8 border border-gray-200 dark:border-gray-700 hover:shadow-xl transition-all duration-300">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
          <div class="guide-card">
            <div class="flex items-center mb-6">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-yellow-400 to-orange-500 flex items-center justify-center text-white mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
              </div>
              <h3 class="text-2xl font-semibold text-gray-800 dark:text-white">
                入学准备
              </h3>
            </div>
            <ul class="space-y-4 text-gray-600 dark:text-gray-300 pl-4">
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-600 dark:text-yellow-300 flex items-center justify-center mr-3 mt-0.5">•</span>
                <span>准备身份证、录取通知书等证件</span>
              </li>
              <li class="flex items-start">
                <span class="w-6 h-6 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-600 dark:text-yellow-300 flex items-center justify-center mr-3 mt-0.5">•</span>
                <span>按要求准备入学所需物品</span>
              </li>
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-600 dark:text-yellow-300 flex items-center justify-center mr-3 mt-0.5">•</span>
                <span>提前了解校园地图和周边环境</span>
              </li>
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-600 dark:text-yellow-300 flex items-center justify-center mr-3 mt-0.5">•</span>
                <span>关注学校官方公众号获取最新信息</span>
              </li>
            </ul>
          </div>

          <div class="guide-card">
            <div class="flex items-center mb-6">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center text-white mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <h3 class="text-2xl font-semibold text-gray-800 dark:text-white">
                报到流程
              </h3>
            </div>
            <ul class="space-y-4 text-gray-600 dark:text-gray-300 pl-4">
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-orange-100 dark:bg-orange-900 text-orange-600 dark:text-orange-300 flex items-center justify-center mr-3 mt-0.5">1</span>
                <span>到达学校后先办理报到手续</span>
              </li>
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-orange-100 dark:bg-orange-900 text-orange-600 dark:text-orange-300 flex items-center justify-center mr-3 mt-0.5">2</span>
                <span>缴纳学费和住宿费</span>
              </li>
              <li class="flex items-start">
                <span class=" w-6 h-6 rounded-full bg-orange-100 dark:bg-orange-900 text-orange-600 dark:text-orange-300 flex items-center justify-center mr-3 mt-0.5">3</span>
                <span>领取学生证和相关材料</span>
              </li>
              <li class="flex items-start">
                <span class="w-6 h-6 rounded-full bg-orange-100 dark:bg-orange-900 text-orange-600 dark:text-orange-300 flex items-center justify-center mr-3 mt-0.5">4</span>
                <span>办理入住手续</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
/* AI和IT字样样式 */
.ai-letter, .it-letter {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16rem;
  font-weight: 600;
  z-index: 10;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  opacity: 0.8;
  transition: all 0.3s ease;
  letter-spacing: -5px;
  font-family: 'Arial', sans-serif;
}

.ai-letter {
  left: 0;
  background-image: linear-gradient(135deg, #00c6ff, #0072ff);
  -webkit-background-clip: text;
  color: transparent;
  transform: translateY(-50%) perspective(500px) rotateY(40deg);
}

.it-letter {
  right: 2px;
  background-image: linear-gradient(135deg, #ff0080, #7928ca);
  -webkit-background-clip: text;
  color: transparent;
  transform: translateY(-50%) perspective(500px) rotateY(-40deg);
}

.ai-letter:hover, .it-letter:hover {
  opacity: 1;
  text-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
  transform: translateY(-50%) scale(1.05) perspective(500px) rotateY(0);
}

/* 渐变文字样式 */
.gradient-text {
  background-image: linear-gradient(45deg, #48ff00, #ff00dd, green, cyan, blue, magenta);
  -webkit-background-clip: text;
  color: transparent;
  animation: hue-rotate 6s infinite linear;
}

.gradient-text-alt {
  background-image: linear-gradient(45deg, #3b82f6, #8b5cf6);
  -webkit-background-clip: text;
  color: transparent;
}

@keyframes hue-rotate {
  from {
    filter: hue-rotate(0deg);
  }
  to {
    filter: hue-rotate(360deg);
  }
}
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动动画 */
.animate-on-scroll {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.8s ease, transform 0.8s ease;
}

#quick-access.animate-on-scroll {
  transition-delay: 0.2s;
}

#activities-section.animate-on-scroll {
  transition-delay: 0.6s;
}

#guide-section.animate-on-scroll {
  transition-delay: 0.8s;
}

/* 当元素进入视口时应用的样式 */
#quick-access, #news-section, #activities-section, #guide-section {
  opacity: 1;
  transform: translateY(0);
}

/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}

/* 滚动到锚点时的平滑效果 */
html {
  scroll-behavior: smooth;
}
</style>