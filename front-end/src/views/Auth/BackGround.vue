<template>
  <div class="fixed inset-0 overflow-hidden select-none">
    <!-- 背景容器 - 放在底层并明确设置为不可交互 -->
    <div id="background-container" class="fixed inset-0 w-full h-full pointer-events-none z-0"></div>

    <!-- 内容遮罩层 -->
    <div class="absolute inset-0 bg-black/30 z-1"></div>

    <!-- 主内容区 - 确保高于背景和遮罩层 -->
    <div class="relative z-20 container mx-auto px-4 py-8 flex flex-col h-full">
      <!-- 顶部导航 -->
      <header class="flex justify-between items-center py-4">
        <div class="flex items-center">
          <span class="text-3xl animate-flap mr-2">🦋</span>
          <h2 class="text-white text-xl font-bold">学子航标</h2>
        </div>
      </header>

      <!-- 主要内容区 -->
      <main class="flex-grow flex flex-col md:flex-row items-center justify-center gap-8">
        <!-- 左侧信息 -->
        <div class="md:w-1/2 text-center md:text-left">
          <h1 class="text-3xl md:text-4xl lg:text-5xl font-bold text-white mb-4 leading-tight">
            学子航标
          </h1>
          <p class="text-base md:text-lg text-white/90 mb-6 max-w-xl">
            为大学新生提供全方位的校园信息服务，让校园生活更加便捷。获取最新资讯、课程信息和校园活动。
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center md:justify-start">
            <Send1
                defaultText="开启旅程"
                sentText="出发->"
                primaryColor="#795548"
                minHeight="90px"
                maxHeight="120px"
                to="/auth"
                :external="true"
                :navigationDelay="1500"
            />
          </div>
        </div>

        <!-- 右侧特性卡片 -->
        <div class="md:w-1/2 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl hover:bg-white/15 transition-all duration-300 transform hover:-translate-y-2 hover:shadow-xl group">
            <div class="text-3xl mb-2 group-hover:scale-110 transition-transform duration-300">🏫</div>
            <h3 class="text-lg font-bold text-white mb-1">学校介绍</h3>
            <p class="text-sm text-white/80">了解学校的历史沿革、办学特色和发展愿景</p>
          </div>

          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl hover:bg-white/15 transition-all duration-300 transform hover:-translate-y-2 hover:shadow-xl group">
            <div class="text-3xl mb-2 group-hover:scale-110 transition-transform duration-300">📝</div>
            <h3 class="text-lg font-bold text-white mb-1">课程规划</h3>
            <p class="text-sm text-white/80">查看专业课程设置、教学计划和学分要求</p>
          </div>

          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl hover:bg-white/15 transition-all duration-300 transform hover:-translate-y-2 hover:shadow-xl group">
            <div class="text-3xl mb-2 group-hover:scale-110 transition-transform duration-300">🧭</div>
            <h3 class="text-lg font-bold text-white mb-1">校园导航</h3>
            <p class="text-sm text-white/80">校园地图、建筑分布和设施位置信息</p>
          </div>

          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl hover:bg-white/15 transition-all duration-300 transform hover:-translate-y-2 hover:shadow-xl group">
            <div class="text-3xl mb-2 group-hover:scale-110 transition-transform duration-300">📢</div>
            <h3 class="text-lg font-bold text-white mb-1">信息通知</h3>
            <p class="text-sm text-white/80">学校公告、活动预告和重要通知</p>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import Send1 from "@/components/zuJian/button/Send1.vue";

const router = useRouter();
let pc = null;
let handleResize = null;

// 确保点击事件正常工作
const navigateToAuth = () => {
  console.log('导航到认证页面');
  router.push('/auth');
};

onMounted(async () => {
  // 禁用页面滚动
  document.body.style.overflow = 'hidden';
  document.documentElement.style.overflow = 'hidden';

  // 先设置容器样式
  const backgroundContainer = document.getElementById('background-container');
  if (backgroundContainer) {
    backgroundContainer.style.pointerEvents = 'none';
  }

  // 确保主内容区域在背景之上
  document.querySelector('main').style.position = 'relative';
  document.querySelector('main').style.zIndex = '30';

  // 确保按钮可点击
  const authButton = document.getElementById('auth-button');
  if (authButton) {
    authButton.style.position = 'relative';
    authButton.style.zIndex = '40';
    authButton.style.cursor = 'pointer';

    // 添加直接的点击事件监听器作为备份
    authButton.addEventListener('click', () => {
      console.log('按钮被点击');
      router.push('/auth');
    });
  }

  // 然后导入并初始化蝴蝶背景
  try {
    const { butterfliesBackground } = await import('https://unpkg.com/threejs-toys@0.0.8/build/threejs-toys.module.cdn.min.js');

    // 添加一个延迟，确保DOM已经完全准备好
    await nextTick();

    pc = butterfliesBackground({
      el: document.getElementById('background-container'),
      eventsEl: null, // 不绑定事件元素，避免捕获点击
      gpgpuSize: 18,
      background: 0x88CEFF,
      material: 'phong',
      lights: [
        { type: 'ambient', params: [0xffffff, 0.5] },
        { type: 'directional', params: [0xffffff, 1], props: { position: [10, 0, 0] } }
      ],
      materialParams: { transparent: true, alphaTest: 0.5 },
      texture: 'https://assets.codepen.io/33787/butterflies.png',
      textureCount: 4,
      wingsScale: [2, 2, 2],
      wingsWidthSegments: 16,
      wingsHeightSegments: 16,
      wingsSpeed: 0.75,
      wingsDisplacementScale: 1.25,
      noiseCoordScale: 0.01,
      noiseTimeCoef: 0.0005,
      noiseIntensity: 0.0025,
      attractionRadius1: 100,
      attractionRadius2: 150,
      maxVelocity: 0.1,
      autoResize: false
    });

    // 再次确保背景容器不捕获事件
    if (backgroundContainer) {
      backgroundContainer.style.pointerEvents = 'none';

      // 获取所有Three.js创建的canvas元素并禁用事件
      const canvases = backgroundContainer.querySelectorAll('canvas');
      canvases.forEach(canvas => {
        canvas.style.pointerEvents = 'none';
      });
    }

    // 处理窗口大小变化
    handleResize = () => {
      if (pc && typeof pc.resize === 'function') {
        pc.resize(window.innerWidth, window.innerHeight, false);
      }
    };

    window.addEventListener('resize', handleResize);
  } catch (error) {
    console.error('加载背景失败:', error);
  }
});

onBeforeUnmount(() => {
  // 恢复页面滚动
  document.body.style.overflow = '';
  document.documentElement.style.overflow = '';

  // 移除按钮监听器
  const authButton = document.getElementById('auth-button');
  if (authButton) {
    authButton.removeEventListener('click', () => {
      router.push('/auth');
    });
  }

  // 销毁Three.js实例
  if (pc && typeof pc.destroy === 'function') {
    try {
      pc.destroy();
    } catch (error) {
      console.error('销毁背景失败:', error);
    }
  }

  // 移除事件监听器
  if (handleResize) {
    window.removeEventListener('resize', handleResize);
  }
});
</script>

<style>
body, html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  font-family: 'Montserrat', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  overflow: hidden !important; /* 确保没有滚动条 */
}

/* 确保所有背景元素不阻止点击 */
#background-container,
#background-container * {
  pointer-events: none !important;
}

/* 确保按钮可点击 */
button,
.md\:w-1\/2 div {
  position: relative;
  z-index: 40;
  pointer-events: auto !important;
}

@keyframes flap {
  0%, 20%, 80%, 100% {
    transform: scaleX(1) rotate(5deg);
  }
  50% {
    transform: scaleX(0.7) rotate(7deg);
  }
}

.animate-flap {
  animation: flap 0.75s infinite ease-in-out;
}

/* 移除所有滚动条相关样式 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  display: none;
}

/* 响应式调整 - 调整内容大小以适应屏幕而不需要滚动 */
@media (max-width: 768px) {
  h1 {
    font-size: 2rem;
  }

  p {
    font-size: 0.9rem;
  }
}

@media (max-width: 640px) {
  .grid {
    @apply grid-cols-1 gap-2;
  }

  .text-3xl {
    font-size: 1.5rem;
  }
}
</style>