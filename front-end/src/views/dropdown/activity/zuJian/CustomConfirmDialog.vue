<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '确认'
  },
  content: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'primary',  // primary, success, warning, danger, info
    validator: (value) => ['primary', 'success', 'warning', 'danger', 'info'].includes(value)
  },
  confirmText: {
    type: String,
    default: '确定'
  },
  cancelText: {
    type: String,
    default: '取消'
  },
  width: {
    type: String,
    default: 'max-w-lg'  // 新增宽度属性，可自定义
  }
});

const emit = defineEmits(['confirm', 'cancel', 'update:visible']);

const handleConfirm = () => {
  emit('confirm');
  emit('update:visible', false);
};

const handleCancel = () => {
  emit('cancel');
  emit('update:visible', false);
};
</script>

<template>
  <transition 
    enter-active-class="transition ease-out duration-300"
    enter-from-class="opacity-0 scale-95"
    enter-to-class="opacity-100 scale-100"
    leave-active-class="transition ease-in duration-200"
    leave-from-class="opacity-100 scale-100"
    leave-to-class="opacity-0 scale-95"
  >
    <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center">
      <!-- 背景遮罩 -->
      <div 
        class="fixed inset-0 bg-black/40 backdrop-blur-sm transition-opacity duration-300" 
        @click="handleCancel"
      ></div>
      
      <!-- 弹窗内容 -->
      <div 
        :class="[width]" 
        class="relative bg-white dark:bg-gray-800 rounded-2xl shadow-2xl transform transition-all w-full mx-4 overflow-hidden border border-gray-100 dark:border-gray-700"
      >
        <!-- 弹窗标题 -->
        <div 
          class="px-6 py-4 border-b border-gray-200 dark:border-gray-700 flex justify-between items-center"
          :class="{
            'bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400': type === 'primary',
            'bg-green-50 dark:bg-green-900/30 text-green-600 dark:text-green-400': type === 'success',
            'bg-yellow-50 dark:bg-yellow-900/30 text-yellow-600 dark:text-yellow-400': type === 'warning',
            'bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-400': type === 'danger',
            'bg-gray-50 dark:bg-gray-900/30 text-gray-600 dark:text-gray-400': type === 'info'
          }"
        >
          <h3 class="text-lg font-bold flex items-center">
            <span v-if="type === 'primary'" class="mr-2">
              <i class="fas fa-circle-info"></i>
            </span>
            <span v-else-if="type === 'success'" class="mr-2">
              <i class="fas fa-circle-check"></i>
            </span>
            <span v-else-if="type === 'warning'" class="mr-2">
              <i class="fas fa-triangle-exclamation"></i>
            </span>
            <span v-else-if="type === 'danger'" class="mr-2">
              <i class="fas fa-circle-exclamation"></i>
            </span>
            <span v-else-if="type === 'info'" class="mr-2">
              <i class="fas fa-circle-info"></i>
            </span>
            {{ title }}
          </h3>
          <button 
            @click="handleCancel" 
            class="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
          >
            <i class="fas fa-xmark"></i>
          </button>
        </div>
        
        <!-- 弹窗内容 -->
        <div class="px-6 py-6">
          <p class="text-gray-700 dark:text-gray-300 leading-relaxed">{{ content }}</p>
        </div>
        
        <!-- 弹窗按钮 -->
        <div class="px-6 py-4 border-t border-gray-200 dark:border-gray-700 flex justify-end space-x-4">
          <button 
            @click="handleCancel" 
            class="px-5 py-2.5 bg-gray-100 dark:bg-gray-700 rounded-lg text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors duration-200 font-medium"
          >
            {{ cancelText }}
          </button>
          <button 
            @click="handleConfirm" 
            :class="{
              'bg-blue-500 hover:bg-blue-600 active:bg-blue-700 ring-blue-200': type === 'primary',
              'bg-green-500 hover:bg-green-600 active:bg-green-700 ring-green-200': type === 'success',
              'bg-yellow-500 hover:bg-yellow-600 active:bg-yellow-700 ring-yellow-200': type === 'warning',
              'bg-red-500 hover:bg-red-600 active:bg-red-700 ring-red-200': type === 'danger',
              'bg-gray-500 hover:bg-gray-600 active:bg-gray-700 ring-gray-200': type === 'info'
            }"
            class="px-5 py-2.5 text-white rounded-lg transition-all duration-200 font-medium focus:outline-none focus:ring-4 dark:ring-opacity-30 shadow-sm"
          >
            {{ confirmText }}
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.backdrop-blur-sm {
  backdrop-filter: blur(4px);
}
</style> 