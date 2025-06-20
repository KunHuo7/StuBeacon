<script setup>
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  map: Object,
  driving: Object,
  geolocation: Object
});

const emit = defineEmits(['marker-clear']);

// 路线规划相关变量
const userPosition = ref(null);
const routeResult = ref(null);
const marker = ref(null);

// 获取用户位置
const getUserLocation = () => {
  if (!props.geolocation) {
    alert('定位服务未初始化，请稍后再试');
    return;
  }
  
  // 显示加载状态
  const loadingToast = document.createElement('div');
  loadingToast.className = 'fixed top-4 right-4 bg-blue-600 dark:bg-blue-700 text-white px-4 py-2 rounded-lg shadow-lg z-50';
  loadingToast.textContent = '正在获取位置...';
  document.body.appendChild(loadingToast);
  
  props.geolocation.getCurrentPosition(function(status, result) {
    // 移除加载提示
    document.body.removeChild(loadingToast);
    
    if (status === 'complete') {
      userPosition.value = [result.position.lng, result.position.lat];
      
      // 在地图上标记用户位置
      if (props.map) {
        // 清除之前的标记
        if (marker.value) {
          props.map.remove(marker.value);
        }
        
        // 创建新标记
        marker.value = new AMap.Marker({
          position: userPosition.value,
          icon: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
          map: props.map
        });
        
        // 将地图中心移动到用户位置
        props.map.setCenter(userPosition.value);
        
        // 显示成功提示
        const successToast = document.createElement('div');
        successToast.className = 'fixed top-4 right-4 bg-green-600 dark:bg-green-700 text-white px-4 py-2 rounded-lg shadow-lg z-50';
        successToast.textContent = '已获取您的位置';
        document.body.appendChild(successToast);
        
        // 3秒后移除提示
        setTimeout(() => {
          document.body.removeChild(successToast);
        }, 3000);
      }
    } else {
      // 定位失败时使用默认位置（可以根据需要修改）
      userPosition.value = [118.630, 31.135];
      
      // 显示错误提示
      const errorToast = document.createElement('div');
      errorToast.className = 'fixed top-4 right-4 bg-red-600 dark:bg-red-700 text-white px-4 py-2 rounded-lg shadow-lg z-50';
      errorToast.textContent = '定位失败，使用默认位置';
      document.body.appendChild(errorToast);
      
      // 3秒后移除提示
      setTimeout(() => {
        document.body.removeChild(errorToast);
      }, 3000);
      
      console.error('定位失败', result);
    }
  });
};

// 规划路线
const planRoute = () => {
  if (!userPosition.value) {
    alert('请先获取您的位置');
    return;
  }
  
  // 安徽信息工程学院的位置
  const destination = [118.636346,31.136825];
  
  // 规划路线
  props.driving.search(
    userPosition.value,
    destination,
    (status, result) => {
      if (status === 'complete') {
        routeResult.value = result;
      } else {
        alert('路线规划失败，请稍后再试');
      }
    }
  );
};

// 清除路线规划方法
const clearRoute = () => {
  if (props.driving) {
    props.driving.clear();
    routeResult.value = null;
  }
  
  // 通知父组件清除标记点
  emit('marker-clear');
};
</script>

<template>
  <div class="animate-fadeIn">
    <h3 class="text-lg font-semibold text-gray-800 dark:text-gray-100 pb-3 mb-3 border-b border-gray-100 dark:border-gray-700">路线规划</h3>
    <p class="text-sm text-gray-600 dark:text-gray-300 mb-4">从您当前位置到安徽信息工程学院</p>
    
    <div class="space-y-3">
      <button 
        @click="getUserLocation" 
        class="w-full py-2 px-4 bg-blue-50 dark:bg-blue-900/20 hover:bg-blue-100 dark:hover:bg-blue-800/30 text-blue-700 dark:text-blue-300 rounded-lg transition-colors duration-200 flex items-center justify-center gap-2 shadow-sm dark:shadow-gray-900/10"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
        <span>获取我的位置</span>
      </button>
      
      <button 
        @click="planRoute" 
        class="w-full py-2 px-4 bg-green-600 dark:bg-green-700 hover:bg-green-700 dark:hover:bg-green-600 text-white rounded-lg transition-colors duration-200 flex items-center justify-center gap-2 shadow-sm dark:shadow-gray-900/10"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
        </svg>
        <span>规划路线</span>
      </button>
      
      <button 
        v-if="routeResult" 
        @click="clearRoute" 
        class="w-full py-2 px-4 bg-red-50 dark:bg-red-900/20 hover:bg-red-100 dark:hover:bg-red-800/30 text-red-600 dark:text-red-300 rounded-lg transition-colors duration-200 flex items-center justify-center gap-2 shadow-sm dark:shadow-gray-900/10"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <span>清除路线</span>
      </button>
    </div>
    
    <div v-if="routeResult" class="mt-4 p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg shadow-sm dark:shadow-gray-900/10">
      <p class="text-sm text-blue-800 dark:text-blue-300 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        路线已规划，请查看地图
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

/* 暗色模式下的路线和标记样式优化 */
:deep(.amap-marker-label) {
  @apply dark:bg-gray-800 dark:text-white dark:border-gray-700;
}

:deep(.amap-lib-driving) {
  @apply dark:text-gray-200;
}

:deep(.amap-lib-driving-hover) {
  @apply dark:bg-blue-900/30 dark:text-blue-300;
}
</style>