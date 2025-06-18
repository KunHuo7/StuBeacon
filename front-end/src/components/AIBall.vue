<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useThemeStore } from "@/stores/theme.js";
import { useRouter } from 'vue-router';
import SimpleChatUI from '@/views/chat/components/SimpleChatUI.vue';

const router = useRouter();
const themeStore = useThemeStore();

// 悬浮球状态
const isVisible = ref(true);
const isDragging = ref(false);
const position = ref({ x: 30, y: 30 });
const offset = ref({ x: 0, y: 0 });
const size = 60; // 悬浮球大小
const wasDragged = ref(false); // 标记是否进行了拖动操作

// 关闭按钮显示状态
const showCloseButton = ref(false);

// 弹窗状态
const showDialog = ref(false);
const dialogPosition = ref({ x: 0, y: 0 });
const dialogDragging = ref(false);
const dialogOffset = ref({ x: 0, y: 0 });
const dialogSize = { width: 550, height: 350 }; // 弹窗尺寸

// 处理拖拽开始
const startDrag = (e) => {
  isDragging.value = true;
  wasDragged.value = false; // 重置拖动标记
  showCloseButton.value = false; // 开始拖动时隐藏关闭按钮
  
  // 计算点击位置相对于悬浮球的偏移量
  const rect = e.target.getBoundingClientRect();
  offset.value = {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  };
  
  // 防止触发其他点击事件
  e.preventDefault();
};

// 处理拖拽过程
const onDrag = (e) => {
  if (!isDragging.value && !dialogDragging.value) return;
  
  // 获取事件对象（处理触摸和鼠标事件）
  const eventObj = e.touches ? e.touches[0] : e;
  
  if (isDragging.value) {
    // 标记已经发生了真正的拖动
    wasDragged.value = true;
    
    // 悬浮球拖动逻辑
    position.value = {
      x: eventObj.clientX - offset.value.x,
      y: eventObj.clientY - offset.value.y
    };
    
    // 确保悬浮球不会超出屏幕边界
    const maxX = window.innerWidth - size;
    const maxY = window.innerHeight - size;
    
    position.value.x = Math.max(0, Math.min(position.value.x, maxX));
    position.value.y = Math.max(0, Math.min(position.value.y, maxY));
  } else if (dialogDragging.value) {
    // 弹窗拖动逻辑
    dialogPosition.value = {
      x: eventObj.clientX - dialogOffset.value.x,
      y: eventObj.clientY - dialogOffset.value.y
    };
    
    // 确保弹窗不会超出屏幕边界
    const maxX = window.innerWidth - dialogSize.width;
    const maxY = window.innerHeight - dialogSize.height;
    
    dialogPosition.value.x = Math.max(0, Math.min(dialogPosition.value.x, maxX));
    dialogPosition.value.y = Math.max(0, Math.min(dialogPosition.value.y, maxY));
  }
};

// 处理拖拽结束
const endDrag = () => {
  if (isDragging.value) {
    isDragging.value = false;
    
    // 获取屏幕宽度
    const screenWidth = window.innerWidth;
    
    // 获取悬浮球元素
    const ball = document.querySelector('.floating-ball');
    if (ball) {
      // 添加过渡效果，使移动更加平滑
      ball.style.transition = 'left 0.5s cubic-bezier(0.25, 0.1, 0.25, 1)';
      
      // 根据当前位置决定回到左侧还是右侧
      if (position.value.x < screenWidth / 2) {
        // 靠近左侧，移动到左侧边缘
        position.value.x = 20;
      } else {
        // 靠近右侧，移动到右侧边缘
        position.value.x = screenWidth - size - 20;
      }
      
      // 添加吸附动画效果
      setTimeout(() => {
        ball.classList.add('snap-animation');
        
        // 动画结束后移除类
        setTimeout(() => {
          ball.classList.remove('snap-animation');
        }, 500);
      }, 400); // 等待位置过渡完成后再添加动画
      
      // 延迟移除过渡效果
      setTimeout(() => {
        ball.style.transition = 'transform 0.2s, box-shadow 0.2s, opacity 0.3s';
      }, 600);
    }
    
    // 将位置存储到本地存储中
    localStorage.setItem('floatingBallPosition', JSON.stringify(position.value));
    
    // 显示关闭按钮
    showCloseButton.value = true;
  }
  
  dialogDragging.value = false;
};

// 点击悬浮球显示弹窗
const showDialogWindow = (e) => {
  // 如果正在拖动，不触发点击事件
  if (isDragging.value) return;
  
  // 如果是拖动后的松开，不触发弹窗
  if (wasDragged.value) {
    wasDragged.value = false; // 重置拖动标记
    return;
  }
  
  e.stopPropagation(); // 阻止事件冒泡
  
  // 隐藏悬浮球
  isVisible.value = false;
  
  // 计算弹窗位置
  const screenWidth = window.innerWidth;
  const screenCenter = screenWidth / 2;
  
  // 根据悬浮球位置决定弹窗位置
  if (position.value.x < screenCenter) {
    // 悬浮球在左侧，弹窗在右侧
    dialogPosition.value = {
      x: position.value.x + size + 10,
      y: position.value.y - dialogSize.height / 2 + size / 2
    };
  } else {
    // 悬浮球在右侧，弹窗在左侧
    dialogPosition.value = {
      x: position.value.x - dialogSize.width - 10,
      y: position.value.y - dialogSize.height / 2 + size / 2
    };
  }
  
  // 确保弹窗不会超出屏幕边界
  const maxX = window.innerWidth - dialogSize.width;
  const maxY = window.innerHeight - dialogSize.height;
  
  dialogPosition.value.x = Math.max(0, Math.min(dialogPosition.value.x, maxX));
  dialogPosition.value.y = Math.max(0, Math.min(dialogPosition.value.y, maxY));
  
  // 显示弹窗
  showDialog.value = true;
};

// 关闭弹窗，显示悬浮球
const closeDialog = () => {
  showDialog.value = false;
  isVisible.value = true;
};

// 开始拖动弹窗
const startDialogDrag = (e) => {
  dialogDragging.value = true;
  
  // 计算点击位置相对于弹窗的偏移量
  const rect = e.currentTarget.getBoundingClientRect();
  dialogOffset.value = {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  };
  
  e.preventDefault();
};

// 关闭悬浮球
const close = () => {
  isVisible.value = false;
  
  // 将关闭状态存储到本地存储中
  localStorage.setItem('floatingBallClosed', 'true');
};

// 鼠标进入悬浮球
const onMouseEnter = () => {
  if (!isDragging.value) {
    showCloseButton.value = true;
  }
};

// 鼠标离开悬浮球
const onMouseLeave = () => {
  // 短暂延迟，以便用户有机会点击关闭按钮
  setTimeout(() => {
    if (!isDragging.value) {
      showCloseButton.value = false;
    }
  }, 300);
};

// 跳转到完整聊天页面
const navigateToFullChat = () => {
  // 关闭弹窗
  showDialog.value = false;
  isVisible.value = true;
  
  // 跳转到聊天页面，使用正确的路由名称
  router.push({ 
    name: 'chatFullPage' 
  });
};

// 从本地存储中恢复位置和可见性状态
onMounted(() => {
  const savedPosition = localStorage.getItem('floatingBallPosition');
  const isClosed = localStorage.getItem('floatingBallClosed');
  
  if (savedPosition) {
    position.value = JSON.parse(savedPosition);
  }
  
  // 页面刷新后，无论之前是否关闭，都会显示悬浮球
  localStorage.removeItem('floatingBallClosed');
  
  // 添加事件监听器 - 使用事件选项提高性能
  window.addEventListener('mousemove', onDrag, { passive: true });
  window.addEventListener('mouseup', endDrag);
  window.addEventListener('touchmove', onDrag, { passive: true });
  window.addEventListener('touchend', endDrag);
});

onUnmounted(() => {
  // 移除事件监听器
  window.removeEventListener('mousemove', onDrag);
  window.removeEventListener('mouseup', endDrag);
  window.removeEventListener('touchmove', onDrag);
  window.removeEventListener('touchend', endDrag);
});
</script>

<template>
  <div class="fixed inset-0 w-full h-full z-[9999] pointer-events-none">
    <!-- 悬浮球 -->
    <div v-if="isVisible" class="fixed inset-0 w-full h-full z-[9999] pointer-events-none">
      <div
        class="floating-ball absolute rounded-full flex items-center justify-center text-white shadow-lg cursor-pointer select-none backdrop-blur-md border-2 border-white/30 pointer-events-auto will-change-transform transition-transform duration-200 hover:scale-105 hover:shadow-xl active:scale-[0.98]"
        :style="{
          left: `${position.x}px`,
          top: `${position.y}px`,
          width: `${size}px`,
          height: `${size}px`,
          backgroundColor: themeStore.isDark ? '#3B82F6' : '#05c1ec'
        }"
        @mousedown="startDrag"
        @touchstart="startDrag"
        @mouseenter="onMouseEnter"
        @mouseleave="onMouseLeave"
        @click="showDialogWindow">
        <div class="w-full h-full flex items-center justify-center animate-pulse">
          <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24">
            <path fill="#000000" d="M12 0C5.37 0 0 5.37 0 12c0 6.09 4.53 11.11 10.4 11.9v-2.4a1.59 1.59 0 0 0-1.08-1.53A8.41 8.41 0 0 1 3.6 11.8a8.37 8.37 0 0 1 8.49-8.2a8.4 8.4 0 0 1 8.31 8.71l-.01.07a9 9 0 0 1-.03.38c0 .07-.01.14-.02.2c0 .08-.01.16-.02.23l-.02.1c-1.03 6.78-9.85 10.58-9.9 10.61c.52.07 1.06.1 1.6.1c6.63 0 12-5.37 12-12S18.63 0 12 0"/>
          </svg>
        </div>
      </div>
      
      <!-- 关闭按钮 -->
      <button 
        v-if="showCloseButton && !isDragging" 
        class="absolute w-[22px] h-[22px] rounded-full bg-red-500 text-white flex items-center justify-center cursor-pointer border-2 border-white shadow-md pointer-events-auto z-[10000] transition-transform duration-200 hover:scale-110 hover:bg-red-400 active:scale-90"
        :style="{
          left: `${position.x + 40}px`, 
          top: `${position.y - 10}px` 
        }"
        @click.stop="close"
        @mouseenter="showCloseButton = true">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4">
          <path fill-rule="evenodd" d="M5.47 5.47a.75.75 0 011.06 0L12 10.94l5.47-5.47a.75.75 0 111.06 1.06L13.06 12l5.47 5.47a.75.75 0 11-1.06 1.06L12 13.06l-5.47 5.47a.75.75 0 01-1.06-1.06L10.94 12 5.47 6.53a.75.75 0 010-1.06z" clip-rule="evenodd" />
        </svg>
      </button>
    </div>


    <!-- 弹窗 -->
    <div
        v-if="showDialog"
        class="fixed z-[10001] flex flex-col pointer-events-auto rounded-2xl shadow-2xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-900 animate-[dialog-appear_0.3s_ease-out_forwards]"
        :style="{
    left: `${dialogPosition.x}px`,
    top: `${dialogPosition.y}px`,
    width: `${dialogSize.width}px`,
    height: `${dialogSize.height}px`
  }"
    >
      <!-- 弹窗头部 -->
      <div
          class="px-2 py-1 bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-100 font-medium flex justify-between items-center border-b border-gray-200 dark:border-gray-700 cursor-move select-none rounded-t-2xl"
          @mousedown="startDialogDrag"
          @touchstart="startDialogDrag"
      >
        <span class="text-lg font-semibold">AI 助手</span>
        <div class="flex items-center gap-2">
          <!-- 全屏跳转按钮 -->
          <button
            class="p-1.5 rounded-full hover:bg-gray-200 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 duration-200"
            @click="navigateToFullChat "
            aria-label="全屏聊天">

            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 3.75v4.5m0-4.5h4.5m-4.5 0L9 9M3.75 20.25v-4.5m0 4.5h4.5m-4.5 0L9 15M20.25 3.75h-4.5m4.5 0v4.5m0-4.5L15 9m5.25 11.25h-4.5m4.5 0v-4.5m0 4.5L15 15" />
            </svg>
          </button>
          
          <!-- 关闭按钮 -->
          <button
            class="p-1.5 rounded-full hover:bg-gray-200 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 transition-colors duration-200"
            @click="closeDialog"
            aria-label="关闭"
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5">
              <path fill-rule="evenodd" d="M5.47 5.47a.75.75 0 011.06 0L12 10.94l5.47-5.47a.75.75 0 111.06 1.06L13.06 12l5.47 5.47a.75.75 0 11-1.06 1.06L12 13.06l-5.47 5.47a.75.75 0 01-1.06-1.06L10.94 12 5.47 6.53a.75.75 0 010-1.06z" clip-rule="evenodd" />
            </svg>
          </button>
        </div>
      </div>
      
      <!-- 弹窗内容 -->
      <div class="flex-1 p-5 overflow-y-auto text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-900 rounded-b-2xl">
        <SimpleChatUI />
      </div>
    </div>



  </div>
</template>

<style>
@keyframes dialog-appear {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 悬浮球过渡动画效果 */
.floating-ball {
  transition: transform 0.2s, box-shadow 0.2s, opacity 0.3s;
}

/* 添加一个吸附效果的动画 */
@keyframes snap-to-edge {
  0% { transform: scale(1); }
  50% { transform: scale(0.9); }
  100% { transform: scale(1); }
}

.snap-animation {
  animation: snap-to-edge 0.5s ease-out;
}
</style>