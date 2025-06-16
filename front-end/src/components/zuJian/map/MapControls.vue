<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  scaleControl: Object,
  toolBarControl: Object,
  controlBarControl: Object,
  overViewControl: Object,
  showScale: Boolean,
  showToolBar: Boolean,
  showControlBar: Boolean,
  showOverView: Boolean
});

const emit = defineEmits(['update:showScale', 'update:showToolBar', 'update:showControlBar', 'update:showOverView']);

// 控件显示/隐藏方法
const toggleScale = (visible) => {
  props.scaleControl[visible ? 'show' : 'hide']();
  emit('update:showScale', visible);
};

const toggleToolBar = (visible) => {
  props.toolBarControl[visible ? 'show' : 'hide']();
  emit('update:showToolBar', visible);
};

const toggleControlBar = (visible) => {
  props.controlBarControl[visible ? 'show' : 'hide']();
  emit('update:showControlBar', visible);
};

const toggleOverView = (visible) => {
  props.overViewControl[visible ? 'show' : 'hide']();
  emit('update:showOverView', visible);
};
</script>

<template>
  <div class="animate-fadeIn">
    <h3 class="text-lg font-semibold text-gray-800 dark:text-gray-100 pb-3 mb-3 border-b border-gray-100 dark:border-gray-700">地图控件设置</h3>
    
    <div class="space-y-3">
      <label class="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 rounded-lg cursor-pointer transition-colors duration-200">
        <div class="flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500 dark:text-purple-400 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 6l3 1m0 0l-3 9a5.002 5.002 0 006.001 0M6 7l3 9M6 7l6-2m6 2l3-1m-3 1l-3 9a5.002 5.002 0 006.001 0M18 7l3 9m-3-9l-6-2m0-2v2m0 16V5m0 16H9m3 0h3" />
          </svg>
          <span class="text-gray-700 dark:text-gray-200">比例尺</span>
        </div>
        <div class="toggle-switch-wrapper">
          <input 
            type="checkbox" 
            id="scale-toggle"
            :checked="props.showScale" 
            @change="toggleScale($event.target.checked)"
            class="toggle-switch-checkbox"
          />
          <label for="scale-toggle" class="toggle-switch">
            <span class="toggle-switch-inner"></span>
            <span class="toggle-switch-switch"></span>
          </label>
        </div>
      </label>
      
      <label class="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 rounded-lg cursor-pointer transition-colors duration-200">
        <div class="flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500 dark:text-purple-400 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
          </svg>
          <span class="text-gray-700 dark:text-gray-200">工具条</span>
        </div>
        <div class="toggle-switch-wrapper">
          <input 
            type="checkbox" 
            id="toolbar-toggle"
            :checked="props.showToolBar" 
            @change="toggleToolBar($event.target.checked)"
            class="toggle-switch-checkbox"
          />
          <label for="toolbar-toggle" class="toggle-switch">
            <span class="toggle-switch-inner"></span>
            <span class="toggle-switch-switch"></span>
          </label>
        </div>
      </label>
      
      <label class="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 rounded-lg cursor-pointer transition-colors duration-200">
        <div class="flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500 dark:text-purple-400 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
          </svg>
          <span class="text-gray-700 dark:text-gray-200">3D控制条</span>
        </div>
        <div class="toggle-switch-wrapper">
          <input 
            type="checkbox" 
            id="controlbar-toggle"
            :checked="props.showControlBar" 
            @change="toggleControlBar($event.target.checked)"
            class="toggle-switch-checkbox"
          />
          <label for="controlbar-toggle" class="toggle-switch">
            <span class="toggle-switch-inner"></span>
            <span class="toggle-switch-switch"></span>
          </label>
        </div>
      </label>
      
      <label class="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 rounded-lg cursor-pointer transition-colors duration-200">
        <div class="flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500 dark:text-purple-400 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 5a1 1 0 011-1h14a1 1 0 011 1v2a1 1 0 01-1 1H5a1 1 0 01-1-1V5zM4 13a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H5a1 1 0 01-1-1v-6zM16 13a1 1 0 011-1h2a1 1 0 011 1v6a1 1 0 01-1 1h-2a1 1 0 01-1-1v-6z" />
          </svg>
          <span class="text-gray-700 dark:text-gray-200">鹰眼</span>
        </div>
        <div class="toggle-switch-wrapper">
          <input 
            type="checkbox" 
            id="overview-toggle"
            :checked="props.showOverView" 
            @change="toggleOverView($event.target.checked)"
            class="toggle-switch-checkbox"
          />
          <label for="overview-toggle" class="toggle-switch">
            <span class="toggle-switch-inner"></span>
            <span class="toggle-switch-switch"></span>
          </label>
        </div>
      </label>
    </div>
    
    <div class="mt-4 p-3 bg-purple-50 dark:bg-purple-900/20 rounded-lg shadow-sm dark:shadow-gray-900/10">
      <p class="text-sm text-purple-800 dark:text-purple-300 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        切换控件可以改变地图显示方式
      </p>
    </div>
  </div>
</template>

<style scoped>
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.animate-fadeIn {
  animation: fadeIn 0.3s ease-out;
}

/* 新的开关样式 */
.toggle-switch-wrapper {
  position: relative;
  width: 50px;
  height: 24px;
  display: inline-block;
  vertical-align: middle;
}

.toggle-switch-checkbox {
  height: 0;
  width: 0;
  opacity: 0;
  position: absolute;
}

.toggle-switch {
  position: relative;
  display: block;
  height: 24px;
  width: 50px;
  background-color: #e2e8f0;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.dark .toggle-switch {
  background-color: #4b5563;
}

.toggle-switch-inner {
  display: block;
  width: 100%;
  height: 100%;
  background-color: transparent;
  border-radius: 24px;
  transition: all 0.2s ease-in-out;
}

.toggle-switch-switch {
  position: absolute;
  top: 2px;
  left: 2px;
  width: 20px;
  height: 20px;
  background-color: white;
  border-radius: 50%;
  transition: all 0.2s ease-in-out;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toggle-switch-checkbox:checked + .toggle-switch {
  background-color: #8b5cf6; /* 紫色 */
}

.toggle-switch-checkbox:checked + .toggle-switch .toggle-switch-switch {
  transform: translateX(26px);
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 悬停状态 */
.toggle-switch:hover .toggle-switch-switch {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.toggle-switch-checkbox:checked + .toggle-switch:hover .toggle-switch-switch {
  box-shadow: 0 2px 8px rgba(139, 92, 246, 0.4);
}

/* 聚焦状态 */
.toggle-switch-checkbox:focus + .toggle-switch {
  box-shadow: 0 0 0 2px rgba(139, 92, 246, 0.2);
}

/* 激活/按下状态 */
.toggle-switch:active .toggle-switch-switch {
  width: 24px;
}

.toggle-switch-checkbox:checked + .toggle-switch:active .toggle-switch-switch {
  transform: translateX(22px);
  width: 24px;
}

/* 暗色模式适配 */
.dark .toggle-switch-checkbox:checked + .toggle-switch {
  background-color: #a78bfa; /* 暗色模式下浅一点的紫色 */
}

.dark .toggle-switch-checkbox:checked + .toggle-switch:hover .toggle-switch-switch {
  box-shadow: 0 2px 8px rgba(167, 139, 250, 0.4);
}

.dark .toggle-switch-checkbox:focus + .toggle-switch {
  box-shadow: 0 0 0 2px rgba(167, 139, 250, 0.2);
}
</style>