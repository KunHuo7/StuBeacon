<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useThemeStore } from "@/stores/theme.js";

const themeStore = useThemeStore();

// 悬浮球状态
const position = ref({ x: 30, y: 30 }); // 初始位置
const size = 60; // 悬浮球大小

// 从本地存储中恢复位置
onMounted(() => {
  const savedPosition = localStorage.getItem('floatingBallPosition');
  if (savedPosition) {
    position.value = JSON.parse(savedPosition);
  }
});

// 仅保留悬浮球显示，不处理任何交互事件
</script>

<template>
  <div class="fixed inset-0 w-full h-full z-[9999] pointer-events-none">
    <!-- 悬浮球 -->
    <div
        class="floating-ball absolute rounded-full flex items-center justify-center text-white shadow-lg select-none backdrop-blur-md border-2 border-white/30 will-change-transform"
        :style="{
        left: `${position.x}px`,
        top: `${position.y}px`,
        width: `${size}px`,
        height: `${size}px`,
        backgroundColor: themeStore.isDark ? '#3B82F6' : '#05c1ec'
      }"
    >
      <div class="w-full h-full flex items-center justify-center">
        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24">
          <path fill="#000000" d="M12 0C5.37 0 0 5.37 0 12c0 6.09 4.53 11.11 10.4 11.9v-2.4a1.59 1.59 0 0 0-1.08-1.53A8.41 8.41 0 0 1 3.6 11.8a8.37 8.37 0 0 1 8.49-8.2a8.4 8.4 0 0 1 8.31 8.71l-.01.07a9 9 0 0 1-.03.38c0 .07-.01.14-.02.2c0 .08-.01.16-.02.23l-.02.1c-1.03 6.78-9.85 10.58-9.9 10.61c.52.07 1.06.1 1.6.1c6.63 0 12-5.37 12-12S18.63 0 12 0"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<style>
/* 悬浮球基本样式 */
.floating-ball {
  /* 移除所有与交互相关的过渡和动画 */
  transition: none;
  cursor: default; /* 更改光标为默认，表示不可交互 */
}

/* 移除所有与交互相关的动画定义 */
@keyframes dialog-appear {
  /* Keep empty if no longer needed */
}

@keyframes snap-to-edge {
  /* Keep empty if no longer needed */
}

/* 移除所有调整大小的样式 */
.cursor-nw-resize,
.cursor-ne-resize,
.cursor-sw-resize,
.cursor-se-resize {
  cursor: default; /* 更改光标为默认 */
  opacity: 0; /* 隐藏调整大小的控制点 */
}
</style>
