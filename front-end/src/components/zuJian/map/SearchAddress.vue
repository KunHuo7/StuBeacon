<script setup>
import { ref, defineProps, defineEmits, onMounted } from 'vue';

const props = defineProps({
  map: Object,
  customLocations: Array
});

const emit = defineEmits(['panel-toggle', 'select-location']);

// 位置分类
const locationCategories = ref([
  {
    name: '教学区',
    locations: [
      { name: 'A1博雅楼', location: [118.633798,31.137461] },
      { name: 'A2博文楼', location: [118.634449,31.137642] },
      { name: 'A3博学楼', location: [118.633884,31.135905] },
      { name: 'A4博识楼', location: [118.634557,31.135793] },
      { name: 'A5博行楼', location: [118.635746,31.135746] },
      { name: 'A6博思楼', location: [118.635552,31.137778] },
      { name: 'A7求是楼', location: [118.632356,31.137275] },
      { name: 'A38健体馆', location: [118.634309,31.138821] },
      { name: '图书馆', location: [118.633128,31.136587] },
    ]
  },
  {
    name: '宿舍楼',
    locations: [
      { name: 'A17公寓楼', location: [118.630487,31.133723] },
      { name: 'A18公寓楼', location: [118.629783,31.133684] },
      { name: 'A19公寓楼', location: [118.629783,31.133684] },
      { name: 'A20公寓楼', location: [118.629783,31.133684] },
      { name: 'A21公寓楼', location: [118.627584,31.134482] },
      { name: 'A33公寓楼', location: [118.628724,31.138282] },
      { name: 'A34公寓楼', location: [118.628488,31.137465] },
      { name: 'A35公寓楼', location: [118.628299,31.136509] },
      { name: 'A36公寓楼', location: [118.628099,31.135501] }

    ]
  },
  {
    name: '运动区',
    locations: [
      { name: '体育馆', location: [118.632494,31.134292] },
      { name: '操场', location: [118.631698,31.134149] },
      { name: '体育公园', location: [118.62961,31.137694] },
      { name: '篮球场1', location: [118.628972,31.135248] },
      { name: '篮球场2', location: [118.630238,31.134907] },
      { name: '网球场', location: [118.62913,31.134458] },
      { name: '游泳馆', location: [118.632544,31.133715] }
    ]
  },
  {
    name: '生活区',
    locations: [
      { name: '大学生活动中心', location: [118.628665,31.139012] },
      { name: '紫云英创业学院', location: [118.626962,31.13349] },
      { name: '陶然餐厅', location: [118.630272,31.134527] },
      { name: '玉兔食堂', location: [118.629058,31.138965] },
      { name: '云耕餐厅', location: [118.629058,31.138965] },
      { name: '快递站', location: [118.628579,31.13857] },
      { name: '营业厅', location: [118.627358,31.134196] },
      { name: '补校园卡', location: [118.627535,31.134157] },
      { name: '医务室', location: [118.627424,31.133883] }
    ]
  },
  {
    name: '校门',
    locations: [
      { name: '学校东门', location: [118.636134,31.136694] },
      { name: '学校西门', location: [118.627838,31.13714] },
      { name: '学校南门', location: [118.63325,31.133463] }
    ]
  }
]);

// 选择位置方法
const selectLocation = (location) => {
  emit('select-location', location);
  // 选择后不关闭面板，方便用户继续选择其他位置
};

onMounted(() => {
  // 确保自定义位置数据与分类位置数据保持一致
  // 这里可以添加逻辑来合并或更新数据
});
</script>

<template>
  <!-- 校园位置面板内容 -->
  <div class="animate-fadeIn">
    <h3 class="text-lg font-semibold text-gray-800 dark:text-gray-100 pb-3 mb-3 border-b border-gray-100 dark:border-gray-700">校园位置导航</h3>
    
    <!-- 位置分类列表 -->
    <div class="space-y-4">
      <div v-for="(category, catIndex) in locationCategories" :key="catIndex" class="mb-4">
        <h4 class="text-md font-medium text-gray-700 dark:text-gray-200 mb-2 flex items-center">
          <span class="w-1 h-4 bg-blue-500 dark:bg-blue-400 rounded mr-2"></span>
          {{ category.name }}
        </h4>
        <div class="grid grid-cols-2 gap-2">
          <div 
            v-for="(location, locIndex) in category.locations" 
            :key="locIndex"
            @click="selectLocation(location)"
            class="p-3 bg-gray-50 dark:bg-gray-700 hover:bg-blue-50 dark:hover:bg-blue-900/40 rounded-md cursor-pointer transition-colors duration-200 flex items-center text-sm text-gray-700 dark:text-gray-200 hover:text-blue-700 dark:hover:text-blue-300 shadow-sm hover:shadow dark:shadow-gray-900/10"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2 text-blue-500 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            {{ location.name }}
          </div>
        </div>
      </div>
    </div>
    
    <div class="mt-4 p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg">
      <p class="text-sm text-blue-800 dark:text-blue-300 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        点击位置名称可在地图上查看详细位置
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

/* 暗色模式优化动画效果 */
@media (prefers-color-scheme: dark) {
  .animate-fadeIn {
    animation: fadeIn 0.4s ease-in-out;
  }
}
</style>